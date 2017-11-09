package com.shen.refresh;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;


import static com.shen.refresh.RefreshLoadAdapter.LOAD_MORE_FAILURE;
import static com.shen.refresh.RefreshLoadAdapter.LOAD_MORE_LOADING;
import static com.shen.refresh.RefreshLoadAdapter.LOAD_MORE_SUCCESS;


/**
 * Created by jerry shen on 2017/8/2.
 */
public class RecyclerLoadMoreView extends RecyclerView {


    private int mItemLastPosition = 0;
    /**
     * 加载更多监听器
     */
    private OnLoadMoreListener onLoadMoreListener;

    /**
     * 是否上拉
     */
    private boolean mIsExceed;
    /**
     * 是否触发加载更多且处理加载方法回调中
     */
    private boolean mIsLoading;

    /**
     * 刷新加载更多适配器
     */
    private RefreshLoadAdapter refreshLoadAdapter;

    public RecyclerLoadMoreView(Context context) {
        this(context, null);
    }

    public RecyclerLoadMoreView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        //增加滚动监听
        addOnScrollListener(new RefreshLoadScrollListener());
    }

    /**
     * 获取数据中的最大数值
     * @param arr
     * @return
     */
    private int getMaxElem(int[] arr) {
        int size = arr.length;
        int maxVal = Integer.MIN_VALUE;
        for (int i = 0; i < size; i++) {
            if (arr[i] > maxVal)
                maxVal = arr[i];
        }
        return maxVal;
    }

    /**
     * 设置加载更多状态
     * @param key
     */
    private void setLoadMoreState(int key) {
        if (refreshLoadAdapter != null) {
            refreshLoadAdapter.updateLoadState(key);
        }
    }

    /**
     * 结recyclerview设置适配器
     * @param adapter 具体的适配器
     */
    @Override
    public void setAdapter(Adapter adapter) {
        if (adapter instanceof RefreshLoadAdapter) {
            refreshLoadAdapter = (RefreshLoadAdapter) adapter;
            if(null != baseLoadMoreView && null != refreshLoadAdapter){
                refreshLoadAdapter.addLoadMoreViewHolder(baseLoadMoreView);
            }
            refreshLoadAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
                @Override
                public void onLoadMore() {
                    loadMoreData();
                }
            });
            super.setAdapter(adapter);
        } else {
            throw new RuntimeException("使用RecyclerLoadMoreView.Adapter");
        }
    }

    /**
     * 表示是可以加载更多
     */
    private boolean loadMoreEnable = false;

    /**
     * 判断是否可以使用加载更多
     *
     * @return
     */
    public boolean isLoadMoreEnable() {
        return loadMoreEnable;
    }

    /**
     * 设置加载更多是否可用
     * @param enable
     */
    public void setLoadMoreEnable(boolean enable) {
        loadMoreEnable = enable;
        if (null != refreshLoadAdapter) {
            refreshLoadAdapter.setLoadMoreEnable(enable);
        }
    }

    /**
     * 数据加载成功后进行调用，关闭加载更多布局
     */
    public void onLoadSuccess() {
        mIsLoading = false;
        updateLoadState(LOAD_MORE_SUCCESS);
    }

    /**
     * 更新加载更多状态
     *
     * @param state 状态值
     */
    private void updateLoadState(int state) {
        if (null != refreshLoadAdapter) {
            refreshLoadAdapter.updateLoadState(state);
        }

    }

    /**
     * 表示数据加载失败，显示失败布局
     */
    public void onLoadFailure() {
        mIsLoading = false;
        updateLoadState(LOAD_MORE_FAILURE);
    }

    /**
     * 触发加载更多数据
     */
    private void loadMoreData() {
        if(null != onLoadMoreListener){
            onLoadMoreListener.onLoadMore();
        }
        updateLoadState(LOAD_MORE_LOADING);
    }

    /**
     * 滑动到倒数第几个条目开始刷新
     *
     * @param itemLastPosition
     */
    public void setLoadMoreOpportunity(int itemLastPosition) {
        mItemLastPosition = itemLastPosition;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    /**
     * 自定义加载动画
     */
    private BaseLoadMoreView baseLoadMoreView;

    public void addLoadMoveView(BaseLoadMoreView loadmoreView) {
        this.baseLoadMoreView = loadmoreView;
        if(null != baseLoadMoreView && null != refreshLoadAdapter){
            refreshLoadAdapter.addLoadMoreViewHolder(baseLoadMoreView);
        }
    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    /**
     * recyclerview 滚动监听类
     */
    private class RefreshLoadScrollListener extends OnScrollListener {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            LayoutManager layoutManager = getLayoutManager();
            if (layoutManager != null) {
                if (layoutManager instanceof LinearLayoutManager) {


                    int position = ((LinearLayoutManager) layoutManager).findLastCompletelyVisibleItemPosition();

                    // 如果滑动到的当前位置大于等于触发加载更多时，且不在加载中，没有超出
                    if ((getAdapter().getItemCount() - mItemLastPosition - 1 <= position) && !mIsExceed && !mIsLoading) {
                        mIsExceed = true;
                        if (onLoadMoreListener != null && loadMoreEnable) {
                            mIsLoading = true;
                            loadMoreData();
                            setLoadMoreState(LOAD_MORE_LOADING);
                        }
                    }
                    //当前显示项小于---触发加载——--位置
                    if (getAdapter().getItemCount() - mItemLastPosition - 1 > position) {
                        mIsExceed = false;
                    }

                } else if (layoutManager instanceof StaggeredGridLayoutManager) {             //添加当为_StaggeredGridLayoutManager_时上拉加载更多

                    StaggeredGridLayoutManager manager = (StaggeredGridLayoutManager) layoutManager;

                    int[] lastVisiblePositions = manager.findLastVisibleItemPositions(new int[manager.getSpanCount()]);
                    int lastVisiblePos = getMaxElem(lastVisiblePositions);
                    int itemCount = manager.getItemCount();

                    int position = lastVisiblePos;
                    // 如果滑动到的当前位置大于等于触发加载更多时，且不在加载中，没有超出
                    if (lastVisiblePos == itemCount - 1 && !mIsExceed && !mIsLoading) {
                        mIsExceed = true;
                        if (onLoadMoreListener != null && loadMoreEnable) {
                            mIsLoading = true;
                            loadMoreData();
                            setLoadMoreState(LOAD_MORE_LOADING);
                        }
                    }
                    //当前显示项小于---触发加载——--位置
                    if (getAdapter().getItemCount() - mItemLastPosition - 1 > position) {
                        mIsExceed = false;
                    }
                }
            }
        }

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }
    }
}
