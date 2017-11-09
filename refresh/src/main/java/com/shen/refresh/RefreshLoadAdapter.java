package com.shen.refresh;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

/**
 * Created by jerry shen on 2017/11/9.
 */

public abstract class RefreshLoadAdapter extends RecyclerView.Adapter {

    /**
     * 表示加载更多状态
     */
    public static final int LOAD_MORE_SUCCESS = 1;
    public static final int LOAD_MORE_FAILURE = 2;
    public static final int LOAD_MORE_LOADING = 3;

    private int currentLoadState = LOAD_MORE_LOADING;

    private RecyclerLoadMoreView.OnLoadMoreListener  onLoadMoreListener;

    public void setOnLoadMoreListener(RecyclerLoadMoreView.OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    public void updateLoadState(int state){
        currentLoadState = state;
        notifyDataSetChanged();
    }

    /**
     * 加载更多类型
     */
    private static final int LOAD_MORE_TYPE = 707001;

    /**
     * 表示是可以加载更多
     */
    private boolean loadMoreEnable = false;

    /**
     * 设置加载更多使能状态
     * @param enable
     */
    public void setLoadMoreEnable(boolean enable) {
        this.loadMoreEnable = enable;
        notifyDataSetChanged();
    }

    private BaseLoadMoreView baseLoadMoreView;

    public void addLoadMoreViewHolder(BaseLoadMoreView loadMoreViewHolder){
        if(loadMoreViewHolder instanceof BaseLoadMoreView){
            this.baseLoadMoreView = loadMoreViewHolder;
        }
        else {
            throw new RuntimeException("传入的类型有误，需要 BaseLoadMoreView 类型");
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == LOAD_MORE_TYPE && loadMoreEnable) {
            if(baseLoadMoreView == null){
                baseLoadMoreView = new LoadMoreDefaultView();
            }
            baseLoadMoreView.setOnLoadMoreListener(onLoadMoreListener);
            return baseLoadMoreView.onCreateItemView(parent);
        } else {
            return onCreateViewHolderToRecyclerLoadMoreView(parent, viewType);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == LOAD_MORE_TYPE && loadMoreEnable) {
            baseLoadMoreView.bindView(holder,position, currentLoadState);
        } else {
            onBindViewHolderToRecyclerLoadMoreView(holder, position);
        }
    }

    @Override
    public int getItemCount() {
        if (loadMoreEnable) {
            return getItemCountToRecyclerLoadMoreView() + 1;
        }
        return getItemCountToRecyclerLoadMoreView();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == getItemCountToRecyclerLoadMoreView() && loadMoreEnable) {
            return LOAD_MORE_TYPE;
        }
        return getItemViewTypeToRecyclerLoadMoreView(position);
    }

    /**
     * 根据不同的类型加载不同的ViewHolder布局
     *
     * @param parent   父布局
     * @param viewType 具体的类型
     * @return 根据viewType返回具体的ViewHolder布局
     */
    protected abstract RecyclerView.ViewHolder onCreateViewHolderToRecyclerLoadMoreView(ViewGroup parent, int viewType);

    /**
     * 根据位置索引及ViewHolder布局来进行动态数据绑定
     *
     * @param holder   布局
     * @param position 位置索引
     */
    protected abstract void onBindViewHolderToRecyclerLoadMoreView(RecyclerView.ViewHolder holder, int position);

    /**
     * 返回列表中数据集合个数
     *
     * @return 集合大小
     */
    protected abstract int getItemCountToRecyclerLoadMoreView();

    /**
     * 返回指定位置的item类型值
     *
     * @param position 位置索引
     * @return 类型值
     */
    protected abstract int getItemViewTypeToRecyclerLoadMoreView(int position);

}
