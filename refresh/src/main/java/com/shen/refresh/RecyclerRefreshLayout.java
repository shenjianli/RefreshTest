package com.shen.refresh;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shen.refresh.util.LogUtils;

import static com.shen.refresh.RecyclerRefreshLayout.REFRESH_STATE.PULL_TO_REFRESH;
import static com.shen.refresh.RecyclerRefreshLayout.REFRESH_STATE.REFRESHING;
import static com.shen.refresh.RecyclerRefreshLayout.REFRESH_STATE.REFRESH_NONE;
import static com.shen.refresh.RecyclerRefreshLayout.REFRESH_STATE.RELEASE_TO_REFRESH;

/**
 * Created by jerry shen on 2017/8/2.
 */

public class RecyclerRefreshLayout extends RelativeLayout {

    /**
     * 表示刷新状态，默认为NONE
     */
    private REFRESH_STATE refreshState = REFRESH_NONE;

    public enum REFRESH_STATE {
        PULL_TO_REFRESH, RELEASE_TO_REFRESH, REFRESHING, REFRESH_NONE
    }

    /**
     * 动画状态
     */
    public enum ANIM_STATE {
        ANIM_NONE, ANIM_START, ANIM_STOP,
    }

    /**
     * 主要内容容器列表
     */
    private RecyclerView mRecyclerView;

    /**
     * 按下的x轴坐标
     */
    private float mStartX;
    /**
     * 按下的的y轴坐标
     */
    private float mStartY;

    /**
     * 表示刷新关键广告提示语---让购物更健康
     */
    private TextView refreshKeyText;
    /**
     * 默认刷新文字提示语---下拉刷新
     */
    private TextView refreshText;
    /**
     * 下拉刷新总布局
     */
    private RelativeLayout pullRefreshLayout;
    /**
     * 刷新图片控件
     */
    private ImageView refreshLoadingImg;
    /**
     * 刷新背景动画
     */
    private AnimationDrawable refreshingDrawable;

    /**
     * 下拉刷新监听器
     */
    private OnRefreshListener mListener;

    /**
     * 刷新使能状态，默认可以刷新
     */
    private boolean refreshEnable = true;


    /**
     * 刷新头布局
     */
    private View refreshHeaderLayout;

    /**
     * 表示是否使用默认刷新布局
     */
    private boolean isUseDefaultRefreshLayout = true;

    private String pullToRefreshText = "下拉可以刷新";
    private String releaseToRefreshText = "释放进行刷新";
    private String refreshingText = "正在刷新";

    public RecyclerRefreshLayout(Context context) {
        this(context, null);
    }

    public RecyclerRefreshLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        createDefaultRefreshView();
        insureChildViewCount();
    }

    /**
     * 判断是否位于recyclerview的顶部
     * @return true 到达顶部  fasle表示未到达顶部
     */
    private boolean isChildViewGetToTop() {

        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            if (view instanceof RecyclerView) {
                mRecyclerView = (RecyclerView) view;
                break;
            }
        }

        GridLayoutManager layoutManager1 = (GridLayoutManager) mRecyclerView.getLayoutManager();
        int gridPosition = layoutManager1.findFirstCompletelyVisibleItemPosition();

        LinearLayoutManager layoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
        int position = layoutManager.findFirstCompletelyVisibleItemPosition();


//        Log.d(TAG, "insureChildViewGetToTop: " + position);
        LogUtils.i("表示滑动过的距离：" + computeVerticalScrollOffset());
        LogUtils.i("滑动 grid positon ：" + gridPosition);
        LogUtils.i("滑动 linear positon ：" + position);

        //如果为0，表示已经到达顶部
        if (position == 0) {
            return true;
        }
        return false;
    }

    /**
     * 确认是否刷新布局只有一个子view
     */
    private void insureChildViewCount() {
        int childCount = getChildCount();
        if (childCount > 2) {
            throw new RuntimeException("只能有一个Child");
        }
    }


    /**
     * 创建默认的刷新布局
     */
    private void createDefaultRefreshView() {
        if (null == refreshHeaderLayout) {
            refreshHeaderLayout = View.inflate(getContext(), R.layout.refresh_pull_down_default_layout, null);

            refreshKeyText = (TextView) refreshHeaderLayout.findViewById(R.id.refresh_key_text);
            refreshText = (TextView) refreshHeaderLayout.findViewById(R.id.refresh_text_hint);
            pullRefreshLayout = (RelativeLayout) refreshHeaderLayout.findViewById(R.id.pull_refresh_start_layout);
            refreshLoadingImg = (ImageView) refreshHeaderLayout.findViewById(R.id.refresh_loading_img);
            refreshingDrawable = (AnimationDrawable) refreshLoadingImg.getBackground();
            isUseDefaultRefreshLayout = true;
        }
        else {
            isUseDefaultRefreshLayout = false;
        }
        //增加默认刷新布局
        addView(refreshHeaderLayout);
    }

    /**
     * 下拉刷新后，结束刷新操作
     */
    public void closeRefresh() {
        refreshState = REFRESH_NONE;
        startAnimation(getScrollY(), 0);
    }


    /**
     * 重载view的事件处理方法
     *
     * @param event 触摸事件
     * @return true 表示消费事件 false 不处理事件，回传上层view处理
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        /**
         * 表示禁用刷新
         */
        if (!refreshEnable) {
            return false;
        }
        /**
         * 表示正在刷新不处理
         */
        if (refreshState == REFRESHING) {
            return false;
        }

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //记录下按下事件
                mStartX = event.getX();
                mStartY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:

                LogUtils.i("刷新布局高度：" + refreshHeaderLayout.getMeasuredHeight());

                float x = event.getX();
                float y = event.getY();
//                boolean isDownOrUp = Math.abs(mStartX - x) < Math.abs(mStartY - y);
//                if (isDownOrUp) {
                setScrollY(-(int) ((y - mStartY) / 2.5));
                if (getScrollY() >= 0) {
                    setScrollY(0);
                }
                //如果垂直滚动距离大于等于刷新布局大小，则转化状态为"释放刷新"
                if (Math.abs(getScrollY()) >= refreshHeaderLayout.getMeasuredHeight()) {

                    //使用默认刷新布局时，设置刷新提示
                    if(isUseDefaultRefreshLayout){
                        refreshText.setText(releaseToRefreshText);
                    }
                    refreshState = RELEASE_TO_REFRESH;
                    if (null != onRefreshTouchListener) {
                        onRefreshTouchListener.onUpdateRefreshState(REFRESH_STATE.RELEASE_TO_REFRESH);
                    }
                    LogUtils.i("松开刷新");
                } else {
                    //如果垂直滚动距离小于刷新布局大小，则转化状态为"下拉刷新"
                    // //使用默认刷新布局时，设置刷新提示
                    if(isUseDefaultRefreshLayout){
                        refreshText.setText(pullToRefreshText);
                    }
                    refreshState = PULL_TO_REFRESH;
                    //回调监听器的刷新状态
                    if (null != onRefreshTouchListener) {
                        onRefreshTouchListener.onUpdateRefreshState(PULL_TO_REFRESH);
                    }
                    LogUtils.i("下拉刷新...");
                }
//                Log.d(TAG, "onTouchEvent: " + getScrollY() + "|" + mLoading.getMeasuredHeight());
//                }
                /**
                 * 表示垂直刷新已经滚动距离，可以用来显示，隐藏顶部布局
                 */
                if (null != onRefreshTouchListener) {
                    onRefreshTouchListener.onRefreshMove(y - mStartY);
                }
                break;
            case MotionEvent.ACTION_UP:
                LogUtils.i("外层----抬起");
                //如果抬起手指时，垂直距离大于等于刷新布局高度，则表示处于"刷新中"
                if (Math.abs(getScrollY()) >= refreshHeaderLayout.getMeasuredHeight()) {

                    //使用默认刷新布局时，设置刷新提示
                    if(isUseDefaultRefreshLayout){
                        refreshText.setText(refreshingText);
                    }



                    LogUtils.i("更新中...");
                    refreshState = REFRESHING;
                    //回调监听器的刷新状态
                    if (null != onRefreshTouchListener) {
                        onRefreshTouchListener.onUpdateRefreshState(REFRESHING);
                    }
                    startAnimation(getScrollY(), refreshHeaderLayout.getMeasuredHeight());
                } else {
                    //如果抬起手指时，垂直距离小于刷新布局高度，则表示处于"下拉刷新"

                    //使用默认刷新布局时，设置刷新提示
                    if(isUseDefaultRefreshLayout){
                        refreshText.setText(pullToRefreshText);
                    }

                    LogUtils.i("下拉刷新...");
                    refreshState = REFRESH_NONE;
                    //回调监听器的刷新状态
                    if (null != onRefreshTouchListener) {
                        onRefreshTouchListener.onUpdateRefreshState(REFRESH_NONE);
                    }
                    startAnimation(getScrollY(), 0);
                }
                break;
        }

        return super.onTouchEvent(event);
    }

    /**
     * 刷新布局进行动态的慢慢回原位
     */
    private void startAnimation(float start, final float end) {
        //布局进行垂直动画回原位
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(Math.abs(start), Math.abs(end));
        valueAnimator.start();
        valueAnimator.setDuration(200);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float fraction = (float) animation.getAnimatedValue();
                setScrollY((int) -fraction);
                LogUtils.d("onAnimationUpdate: " + fraction);
            }
        });

        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {

                LogUtils.i("垂直移动动画结束---" + refreshState);
                //如果不是刷新中
                if (REFRESHING != refreshState) {
                    //表示刷新状态重置

                    if (null != onRefreshTouchListener) {
                        onRefreshTouchListener.onRefreshReset();
                    }

                    // 使用默认刷新布局时，禁用动画
                    if(isUseDefaultRefreshLayout){
                        refreshLoadingImg.setVisibility(View.GONE);
                        pullRefreshLayout.setVisibility(View.VISIBLE);
                        refreshingDrawable.stop();
                    }



                    if (null != onRefreshTouchListener) {
                        onRefreshTouchListener.onUpdateAnimState(ANIM_STATE.ANIM_STOP);
                    }

                } else {
                    //表示处于刷新状态

                    // 使用默认刷新布局时，启动动画
                    if(isUseDefaultRefreshLayout){
                        refreshLoadingImg.setVisibility(View.VISIBLE);
                        pullRefreshLayout.setVisibility(View.GONE);
                        refreshingDrawable.start();
                    }

                    LogUtils.i("开始动画---" + refreshState);
                    if (REFRESHING == refreshState) {
                        if (null != onRefreshTouchListener) {
                            onRefreshTouchListener.onUpdateAnimState(ANIM_STATE.ANIM_START);
                        }
                    }
                    if (mListener != null) {
                        mListener.OnRefresh();
                    }
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    /**
     * 拦截触摸事件
     * @param ev 触摸事件
     * @return
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        //表示处于刷新中，需要消费事件，但不进行移动，不让子布局移动
        if (refreshState == REFRESHING) {
            return true;
        }
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                LogUtils.i("拦截到----下");
                mStartX = ev.getX();
                mStartY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                LogUtils.i("拦截到----移动");
                boolean isDownOrUp = Math.abs(mStartX - ev.getX()) < Math.abs(mStartY - ev.getY());
                boolean isDown = ev.getY() > mStartY;
                //表示是正在下拉，而不上滑
                if (isDownOrUp && isDown) {
                    LogUtils.i("表示正在下拉，而不是上拉");
                    //如果是下拉，且已经到达recyclerview的顶部
                    if (isChildViewGetToTop()) {
                        LogUtils.i("返回 true 调用 onTouchEvent()");
                        return true;
                    }
                }
                break;
        }
        LogUtils.i("调用上层方法");
        return super.onInterceptTouchEvent(ev);
    }

    /**
     * 事件分发器
     * @param ev 事件
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 测量布局宽高
     * @param widthMeasureSpec 表示宽
     * @param heightMeasureSpec 表示高
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        measureChildren(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * 布局位置
     * @param changed
     * @param l
     * @param t
     * @param r
     * @param b
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        //对刷新布局进行位置确定，使其recyclerview顶部之上
        refreshHeaderLayout.layout(l, t - refreshHeaderLayout.getMeasuredHeight(), r, t);
        //对子布局进行位置确定
        View childView = null;
        for (int i = 0; i < getChildCount(); i++) {
            childView = getChildAt(i);
            if (childView != refreshHeaderLayout) {
                childView.layout(l, t, r, b);
            }
        }
    }

    public void setOnRefreshListener(OnRefreshListener listener) {
        mListener = listener;
    }



    /**
     * 动态增加下拉刷新布局
     *
     * @param refreshHeaderLayout 刷新布局
     */
    public void addRefreshLayout(View refreshHeaderLayout) {
        removeView(this.refreshHeaderLayout);
        this.refreshHeaderLayout = refreshHeaderLayout;
        addView(this.refreshHeaderLayout);
        isUseDefaultRefreshLayout = false;
    }

    /**
     * 设置刷新布局顶部的广告语，例如：让购物更健康
     * @param key
     */
    public void setRefreshAdText(String key){
        if(null != refreshKeyText && !TextUtils.isEmpty(key)){
            refreshKeyText.setText(key);
        }
    }


    /**
     * 设置下拉刷新提示语，例如：下拉可以刷新
     */
    public void setPullToRefreshText(String pullToRefreshText){
        if(!TextUtils.isEmpty(pullToRefreshText)){
            this.pullToRefreshText = pullToRefreshText;
        }
    }

    /**
     * 设置释放可以刷新提示语，例如：释放可以刷新
     */
    public void setReleaseToRefreshText(String releaseToRefreshText){
        if(!TextUtils.isEmpty(releaseToRefreshText)){
            this.releaseToRefreshText = releaseToRefreshText;
        }
    }

    /**
     * 设置正在刷新中提示语，例如：正在刷新中
     */
    public void setRefreshingText(String refreshingText){
        if(!TextUtils.isEmpty(refreshingText)){
            this.refreshingText = refreshingText;
        }
    }

    public void setOnRefreshTouchListener(OnRefreshTouchListener onRefreshTouchListener) {
        this.onRefreshTouchListener = onRefreshTouchListener;
    }

    /**
     * 刷新事件监听器
     */
    private OnRefreshTouchListener onRefreshTouchListener;



    public boolean isRefreshEnable() {
        return refreshEnable;
    }

    public void setRefreshEnable(boolean refreshEnable) {
        this.refreshEnable = refreshEnable;
    }

    public interface OnRefreshListener {
        void OnRefresh();
    }


    public interface OnRefreshTouchListener {
        /**
         * 表示下拉刷新时，距离顶部的距离
         * @param moveY 距顶部的距离
         */
        public void onRefreshMove(float moveY);

        /**
         * 表示不有触发刷新，进行重置
         */
        public void onRefreshReset();

        /**
         * 表示刷新状态（上拉刷新，释放刷新，刷新中，空状态）
         * @param refreshState
         */
        public void onUpdateRefreshState(REFRESH_STATE refreshState);

        /**
         * 自定义刷新布局时，动画开始，动画结束
         * @param animState
         */
        public void onUpdateAnimState(ANIM_STATE animState);

    }

}
