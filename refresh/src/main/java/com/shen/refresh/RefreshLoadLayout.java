package com.shen.refresh;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

;


/**
 * Created by jerry shen on 2017/8/2.
 */

public class RefreshLoadLayout extends RelativeLayout {


    private RecyclerLoadMoreView loadMoreLayout;

    private RecyclerRefreshLayout refreshPullLayout;

    private int mSumY = 0;

    public RefreshLoadLayout(Context context) {
        this(context, null);
    }

    public RefreshLoadLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        // 加载布局
        View sumLayout = LayoutInflater.from(context).inflate(R.layout.refresh_load_layout, this);
        refreshPullLayout = (RecyclerRefreshLayout) findViewById(R.id.refresh_pull_layout);


//        refreshPullLayout.setRefreshEnable();
        loadMoreLayout = (RecyclerLoadMoreView) findViewById(R.id.load_more_layout);

        //read custom attrs
        TypedArray typedArray = context.obtainStyledAttributes(attrs,
                R.styleable.refresh_style, 0, 0);

        boolean refreshEnbale = typedArray.getBoolean(R.styleable.refresh_style_refresh_enable, true);
        refreshPullLayout.setRefreshEnable(refreshEnbale);

        boolean loadEnable = typedArray.getBoolean(R.styleable.load_style_load_more_enable, true);
        loadMoreLayout.setLoadMoreEnable(loadEnable);


//        hSpace = t.getDimensionPixelSize(R.styleable.rainbowbar_rainbowbar_hspace, hSpace);
//        vSpace = t.getDimensionPixelOffset(R.styleable.rainbowbar_rainbowbar_vspace, vSpace);
//        barColor = t.getColor(R.styleable.rainbowbar_rainbowbar_color, barColor);
        typedArray.recycle();   // we should always recycle after used

        initListener();
    }

    private void initListener() {
        refreshPullLayout.setOnRefreshTouchListener(new RecyclerRefreshLayout.OnRefreshTouchListener() {
            @Override
            public void onRefreshMove(float moveY) {
                if (null != onRefreshActionListener) {
                    onRefreshActionListener.onRefreshMoveToTop(moveY);
                }
            }

            @Override
            public void onRefreshReset() {
                if (null != onRefreshActionListener) {
                    onRefreshActionListener.onRefreshReset();
                }
            }

            @Override
            public void onUpdateRefreshState(RecyclerRefreshLayout.REFRESH_STATE refreshState) {
                if (null != onRefreshActionListener) {
                    onRefreshActionListener.onUpdateRefreshState(refreshState);
                }
            }

            @Override
            public void onUpdateAnimState(RecyclerRefreshLayout.ANIM_STATE animState) {
                if (null != onRefreshActionListener) {
                    onRefreshActionListener.onUpdateAnimState(animState);
                }
            }
        });

        refreshPullLayout.setOnRefreshListener(new RecyclerRefreshLayout.OnRefreshListener() {
            @Override
            public void OnRefresh() {
                if (null != onRefreshActionListener) {
                    onRefreshActionListener.onRefresh();
                }
            }
        });

        loadMoreLayout.setOnLoadMoreListener(new RecyclerLoadMoreView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (null != onLoadActionListener) {
                    onLoadActionListener.onLoadMore();
                }
            }
        });

        loadMoreLayout.addOnScrollListener(
                new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                        super.onScrollStateChanged(recyclerView, newState);
                    }

                    @Override
                    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                        super.onScrolled(recyclerView, dx, dy);
                        mSumY += dy;
                        if(null != onRefreshScrollListener){
                            onRefreshScrollListener.onScrollToTop(mSumY);
                        }
                    }
                }
        );
    }


    private OnRefreshActionListener onRefreshActionListener;



    public void setOnRefreshActionListener(OnRefreshActionListener onRefreshActionListener) {
        this.onRefreshActionListener = onRefreshActionListener;
    }

    public void addLoadMoveView(BaseLoadMoreView loadmoreView) {
        if(null != loadMoreLayout){
            loadMoreLayout.addLoadMoveView(loadmoreView);
        }
    }

    public void onLoadSuccess() {
        if(null != loadMoreLayout){
            loadMoreLayout.onLoadSuccess();
        }
    }

    public void closeRefresh() {
        if(null != refreshPullLayout){
            refreshPullLayout.closeRefresh();
        }
    }

    public void setLoadMoreEnable(boolean loadMoreEnable) {
        if(null != loadMoreLayout){
            loadMoreLayout.setLoadMoreEnable(loadMoreEnable);
        }
    }

    public void addItemDecoration(RecyclerView.ItemDecoration spacesItemDecoration) {
        if(null != loadMoreLayout){
            loadMoreLayout.addItemDecoration(spacesItemDecoration);
        }
    }

    public void setLayoutManager(GridLayoutManager layoutManager) {
        if(null != layoutManager){
            loadMoreLayout.setLayoutManager(layoutManager);
        }
    }

    public void setAdapter(RefreshConentAdapter adapter) {
        if(null != loadMoreLayout){
            loadMoreLayout.setAdapter(adapter);
        }
    }

    public RecyclerRefreshLayout getRefreshLayout() {
        return refreshPullLayout;
    }

    public RecyclerLoadMoreView getLoadMoreLayout() {
        return loadMoreLayout;
    }





    public interface OnRefreshActionListener {
        /**
         * 表示下拉刷新时，距离顶部的距离
         *
         * @param moveY 距顶部的距离
         */
        public void onRefreshMoveToTop(float moveY);

        /**
         * 表示不有触发刷新，进行重置
         */
        public void onRefreshReset();

        /**
         * 表示刷新状态（上拉刷新，释放刷新，刷新中，空状态）
         *
         * @param refreshState
         */
        public void onUpdateRefreshState(RecyclerRefreshLayout.REFRESH_STATE refreshState);

        /**
         * 自定义刷新布局时，动画开始，动画结束
         *
         * @param animState
         */
        public void onUpdateAnimState(RecyclerRefreshLayout.ANIM_STATE animState);

        /**
         * 触发刷新事件
         */
        public void onRefresh();


    }

    private OnLoadActionListener onLoadActionListener;

    public void setOnLoadActionListener(OnLoadActionListener onLoadActionListener) {
        this.onLoadActionListener = onLoadActionListener;
    }

    public interface OnLoadActionListener {
        /**
         * 加载更多触发回调
         */
        public void onLoadMore();
    }


    private OnRefreshScrollListener onRefreshScrollListener;



    public void setOnRefreshScrollListener(OnRefreshScrollListener onRefreshScrollListener) {
        this.onRefreshScrollListener = onRefreshScrollListener;
    }

    public interface OnRefreshScrollListener {
        /**
         * 表示内容区域滚动距离顶端距离
         *
         * @param toTop 到顶端距离
         */
        public void onScrollToTop(int toTop);
    }
}
