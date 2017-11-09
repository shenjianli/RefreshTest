package com.shen.refresh;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;


import static com.shen.refresh.RefreshAdapter.LOAD_MORE_FAILURE;
import static com.shen.refresh.RefreshAdapter.LOAD_MORE_LOADING;
import static com.shen.refresh.RefreshAdapter.LOAD_MORE_SUCCESS;


/**
 * Created by jerry shen on 2017/8/2.
 */
public class RecyclerLoadMoreView extends RecyclerView {


    private int mItemLastPosition = 0;
    /**
     * 加载更多监听器
     */
    private OnLoadMoreListener onLoadMoreListener;

    private boolean mIsExceed;
    private boolean mIsLoading;

    /**
     * 刷新加载更多适配器
     */
    private RefreshAdapter refreshAdapter;

    public RecyclerLoadMoreView(Context context) {
        this(context, null);
    }

    public RecyclerLoadMoreView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        addOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LayoutManager layoutManager = getLayoutManager();
                if (layoutManager != null) {
                    if (layoutManager instanceof LinearLayoutManager) {
                        int position = ((LinearLayoutManager) layoutManager).findLastCompletelyVisibleItemPosition();
                        if ((getAdapter().getItemCount() - mItemLastPosition - 1 <= position) && !mIsExceed && !mIsLoading) {
                            mIsExceed = true;
                            if (onLoadMoreListener != null && loadMoreEnable) {
                                mIsLoading = true;
                                loadMoreData();
                                setLoadItemStart(LOAD_MORE_LOADING);
                            }
                        }
                        if (getAdapter().getItemCount() - mItemLastPosition - 1 > position) {
                            mIsExceed = false;
                        }

                    } else if (layoutManager instanceof StaggeredGridLayoutManager) {             //添加当为_StaggeredGridLayoutManager_时上拉加载更多
                        StaggeredGridLayoutManager manager = (StaggeredGridLayoutManager) layoutManager;
                        int[] lastVisiblePositions = manager.findLastVisibleItemPositions(new int[manager.getSpanCount()]);
                        int lastVisiblePos = getMaxElem(lastVisiblePositions);
                        int itemCount = manager.getItemCount();

                        int position = lastVisiblePos;
                        if (lastVisiblePos == itemCount - 1 && !mIsExceed && !mIsLoading) {
                            mIsExceed = true;
                            if (onLoadMoreListener != null && loadMoreEnable) {
                                mIsLoading = true;
                                loadMoreData();
                                setLoadItemStart(LOAD_MORE_LOADING);
                            }
                        }
                        if (getAdapter().getItemCount() - mItemLastPosition - 1 > position) {
                            mIsExceed = false;
                        }
                    }
                }
            }

        });
    }

    private int getMaxElem(int[] arr) {
        int size = arr.length;
        int maxVal = Integer.MIN_VALUE;
        for (int i = 0; i < size; i++) {
            if (arr[i] > maxVal)
                maxVal = arr[i];
        }
        return maxVal;
    }

    private void setLoadItemStart(int key) {
        refreshAdapter = (RefreshAdapter) getAdapter();
        if (refreshAdapter != null) {
            refreshAdapter.updateLoadState(key);
            refreshAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
                @Override
                public void onLoadMore() {
                    loadMoreData();
                }
            });
        }
    }

    @Override
    public void setAdapter(Adapter adapter) {
        if (adapter instanceof RefreshAdapter) {
            refreshAdapter = (RefreshAdapter) adapter;
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
     * @return
     */
    public boolean isLoadMoreEnable() {
        return loadMoreEnable;
    }

    public  void setLoadMoreEnable(boolean enable){
        loadMoreEnable = enable;
        if(null != refreshAdapter){
            refreshAdapter.setLoadMoreEnable(enable);
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
     * @param state 状态值
     */
    private void updateLoadState(int state){
        if(null != refreshAdapter){
            refreshAdapter.updateLoadState(state);
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
        onLoadMoreListener.onLoadMore();
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

    public interface OnLoadMoreListener {
        void onLoadMore();
    }
}
