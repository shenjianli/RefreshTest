package com.shen.refresh;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


/**
 * Created by jerry shen on 2017/8/2.
 */
public class RecyclerLoadMoreView extends RecyclerView {
    private int mItemLastPosition = 0;
    private OnRefreshListener mRefreshListener;
    private boolean mIsExceed;
    private boolean mIsLoading;
    private Adapter mAdapter;
    private static final int KEY_LOAD_SUCCESS = 1;
    private static final int KEY_LOAD_FAILURE = 2;
    private static final int KEY_LOADING = 3;

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
                            if (mRefreshListener != null && isLoadMore) {
                                mIsLoading = true;
                                loadMoreData();
                                setLoadItemStart(KEY_LOADING);
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
                            if (mRefreshListener != null && isLoadMore) {
                                mIsLoading = true;
                                loadMoreData();
                                setLoadItemStart(KEY_LOADING);
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
        mAdapter = (Adapter) getAdapter();
        if (mAdapter != null) {
            mAdapter.setLoadItemStart(key);
            mAdapter.setOnRefreshListener(new OnRefreshListener() {
                @Override
                public void onRefresh() {
                    loadMoreData();
                }
            });
        }
    }



    @Override
    public void setAdapter(RecyclerView.Adapter adapter) {
        if (adapter instanceof Adapter) {
            super.setAdapter(adapter);
        } else {
            throw new RuntimeException("使用RecyclerLoadMoreView.Adapter");
        }
    }

    private static boolean isLoadMore = false;

    public static void setLoadMoreEnable(boolean enable){
        isLoadMore = enable;
    }

    public static boolean canLoadMore(){
        return  isLoadMore;
    }

    public void onLoadSuccess() {
        mIsLoading = false;
        setLoadItemStart(KEY_LOAD_SUCCESS);
    }

    public void onLoadFailure() {
        mIsLoading = false;
        setLoadItemStart(KEY_LOAD_FAILURE);
    }

    private void loadMoreData() {
        mRefreshListener.onRefresh();
        setLoadItemStart(KEY_LOADING);
    }

    /**
     * 滑动到倒数第几个条目开始刷新
     *
     * @param itemLastPosition
     */
    public void setLoadMoreOpportunity(int itemLastPosition) {
        mItemLastPosition = itemLastPosition;
    }

    public void setOnRefreshListener(OnRefreshListener l) {
        mRefreshListener = l;
    }

    public interface OnRefreshListener {
        void onRefresh();
    }

    public static abstract class Adapter extends RecyclerView.Adapter {

        private static final int KEY_ITEM_TYPE_LOADING = 707001;
        public static View mLoadingView;
        private LinearLayout mLoading;
        private TextView mLoaded;
        private OnRefreshListener mRefreshListener;

        @Override
        public final ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == KEY_ITEM_TYPE_LOADING && isLoadMore) {
                mLoadingView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_setting_container_loading, parent, false);
                //mLoadingView = LayoutInflater.from(parent.getContext()).inflate(R.layout.load_more_anim_layout, parent, false);
                mLoadingView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mRefreshListener != null) {
                            if (mLoading.getVisibility() == View.GONE && mLoaded.getVisibility() == VISIBLE) {
                                mRefreshListener.onRefresh();
                            }
                        }
                    }
                });
                return new ViewHolderFromLoading(mLoadingView);
            } else {
                return onCreateViewHolderToRecyclerLoadMoreView(parent, viewType);
            }
        }


        @Override
        public final void onBindViewHolder(ViewHolder holder, int position) {
            if (getItemViewType(position) == KEY_ITEM_TYPE_LOADING && isLoadMore) {
                ((ViewHolderFromLoading) holder).bindView();
            } else {
                onBindViewHolderToRecyclerLoadMoreView(holder, position);
            }
        }


        @Override
        public final int getItemCount() {
            if(isLoadMore){
                return getItemCountToRecyclerLoadMoreView() + 1;
            }
            return getItemCountToRecyclerLoadMoreView();
        }


        @Override
        public final int getItemViewType(int position) {
            if (position == getItemCountToRecyclerLoadMoreView() && isLoadMore) {
                return KEY_ITEM_TYPE_LOADING;
            }
            return getItemViewTypeToRecyclerLoadMoreView(position);
        }

        /**
         * 根据不同的类型加载不同的ViewHolder布局
         * @param parent 父布局
         * @param viewType 具体的类型
         * @return 根据viewType返回具体的ViewHolder布局
         */
        protected abstract ViewHolder onCreateViewHolderToRecyclerLoadMoreView(ViewGroup parent, int viewType);

        /**
         * 根据位置索引及ViewHolder布局来进行动态数据绑定
         * @param holder 布局
         * @param position 位置索引
         */
        protected abstract void onBindViewHolderToRecyclerLoadMoreView(ViewHolder holder, int position);

        /**
         * 返回列表中数据集合个数
         * @return 集合大小
         */
        protected abstract int getItemCountToRecyclerLoadMoreView();

        /**
         * 返回指定位置的item类型值
         * @param position 位置索引
         * @return 类型值
         */
        protected abstract int getItemViewTypeToRecyclerLoadMoreView(int position);

        public void setLoadItemStart(int key) {
            if (mLoadingView != null) {
                mLoading = (LinearLayout) mLoadingView.findViewById(R.id.loading);
                mLoaded = (TextView) mLoadingView.findViewById(R.id.loaded);
                switch (key) {
                    case KEY_LOADING:
                        mLoaded.setVisibility(GONE);
                        mLoading.setVisibility(VISIBLE);
                        break;
                    case KEY_LOAD_SUCCESS:
                        mLoaded.setVisibility(GONE);
                        mLoading.setVisibility(GONE);
                        break;
                    case KEY_LOAD_FAILURE:
                        mLoaded.setVisibility(VISIBLE);
                        mLoading.setVisibility(GONE);
                        break;
                }
            }
        }

        public void setOnRefreshListener(OnRefreshListener l) {
            mRefreshListener = l;
        }


    }

    static class ViewHolderFromLoading extends ViewHolder {

        public ViewHolderFromLoading(View itemView) {
            super(itemView);
        }

        public void bindView() {

        }
    }

}
