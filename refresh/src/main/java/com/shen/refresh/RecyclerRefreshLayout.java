package com.shen.refresh;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.shen.refresh.util.LogUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

import static com.shen.refresh.RecyclerRefreshLayout.REFRESH_STATE.PULL_TO_REFRESH;
import static com.shen.refresh.RecyclerRefreshLayout.REFRESH_STATE.REFRESHING;
import static com.shen.refresh.RecyclerRefreshLayout.REFRESH_STATE.REFRESH_NONE;
import static com.shen.refresh.RecyclerRefreshLayout.REFRESH_STATE.RELEASE_TO_REFRESH;

/**
 * Created by jerry shen on 2017/8/2.
 */

public class RecyclerRefreshLayout extends RelativeLayout {

    private static final String TAG = "RecyclerRefreshLayout";
    private RecyclerView mRecyclerView;
    private float mStartX;
    private float mStartY;
    //private View mProgressView;
    private ImageView mPeople;
    private ImageView mGoods;
    private TextView mText;
    private RelativeLayout mStartLoad;
    private ImageView mLoadIng;
    private AnimationDrawable mDrawable;
    private boolean mRefreshing;
    private OnRefreshListener mListener;
    private boolean mToggle;

    public RecyclerRefreshLayout(Context context) {
        this(context, null);
    }

    public RecyclerRefreshLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        createProgressView();
        insureChildViewCount();
    }

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
        if (position == 0) {
            return true;
        }
        return false;
    }

    private void insureChildViewCount() {
        int childCount = getChildCount();
        if (childCount > 2) {
            throw new RuntimeException("只能有一个Child");
        }
    }


    private TextView tvTip;
    private TextView lastRefreshTv;
    private ImageView iconRefresh;

    private void createProgressView() {
        if(null == refreshHeaderLayout){
            LogUtils.i("为空使用默认加载布局");
            refreshHeaderLayout = View.inflate(getContext(), R.layout.view_load_more, null);
            mGoods = (ImageView) refreshHeaderLayout.findViewById(R.id.goods);
            mPeople = (ImageView) refreshHeaderLayout.findViewById(R.id.people);
//        mLoading = (LinearLayout) refreshHeaderLayout.findViewById(R.id.loading);
            mText = (TextView) refreshHeaderLayout.findViewById(R.id.text_hint);
            mStartLoad = (RelativeLayout) refreshHeaderLayout.findViewById(R.id.start_load);
            mLoadIng = (ImageView) refreshHeaderLayout.findViewById(R.id.load_ing);
            mDrawable = (AnimationDrawable) mLoadIng.getBackground();
        }


        //mProgressView = View.inflate(getContext(), R.layout.layout_refresh_header, null);


//        lastRefreshTv = (TextView) mProgressView.findViewById(R.id.last_update_date);
//        String lastRefreshDate = SharedPrefUtils.readStringFromSharedPref(getContext(),"last_update_date");
//        if(!TextUtils.isEmpty(lastRefreshDate)){
//            lastRefreshTv.setText("上次刷新时间：" + lastRefreshDate);
//        }
//        tvTip=(TextView)mProgressView.findViewById(R.id.refresh_tip_text);
//        iconRefresh=(ImageView)mProgressView.findViewById(R.id.logo_anim);
//        iconRefresh.setImageResource(R.drawable.refresh_tween_anim);


        addView(refreshHeaderLayout);
    }

    public void closeRefresh() {
        mRefreshing = false;
//        SimpleDateFormat format = new SimpleDateFormat("MM月dd日 HH:mm");
//        String date = format.format(new Date());
//        lastRefreshTv.setText("上次刷新时间：" + date);
//        SharedPrefUtils.writeStringSharedPref(getContext(),"last_update_date", date);
        startAnimation(getScrollY(), 0);
    }

    public void setToggle(boolean toggle) {
        mToggle = !toggle;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mRefreshing) {
            return false;
        }

        if (mToggle) {
            return false;
        }

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mStartX = event.getX();
                mStartY = event.getY();
                LogUtils.i("外层----下");
                break;
            case MotionEvent.ACTION_MOVE:

                LogUtils.i("刷新布局高度：" + refreshHeaderLayout.getMeasuredHeight());


                LogUtils.i("外层----移动");
                float x = event.getX();
                float y = event.getY();
//                boolean isDownOrUp = Math.abs(mStartX - x) < Math.abs(mStartY - y);
//                if (isDownOrUp) {
                setScrollY(-(int) ((y - mStartY) / 2.5));
                if (getScrollY() >= 0) {
                    setScrollY(0);
                }

                if (Math.abs(getScrollY()) > refreshHeaderLayout.getMeasuredHeight()) {
                    mText.setText("松开刷新...");
                    refreshState = RELEASE_TO_REFRESH;
                    if(null != onRefreshTouchListener){
                        onRefreshTouchListener.onUpdateRefreshState(REFRESH_STATE.RELEASE_TO_REFRESH);
                    }
                    LogUtils.i("松开刷新");
                } else {
                    mText.setText("下拉刷新...");
                    refreshState = PULL_TO_REFRESH;
                    if(null != onRefreshTouchListener){
                        onRefreshTouchListener.onUpdateRefreshState(PULL_TO_REFRESH);
                    }
                    LogUtils.i("下拉刷新...");
                }
//                Log.d(TAG, "onTouchEvent: " + getScrollY() + "|" + mLoading.getMeasuredHeight());
//                }
                if(null != onRefreshTouchListener){
                    onRefreshTouchListener.onRefreshMove(y - mStartY);
                }
                break;
            case MotionEvent.ACTION_UP:
                LogUtils.i("外层----抬起");
                if (Math.abs(getScrollY()) > refreshHeaderLayout.getMeasuredHeight()) {
                    mRefreshing = true;
                    mText.setText("更新中...");
                    LogUtils.i("更新中...");
                    refreshState = REFRESHING;
                    if(null != onRefreshTouchListener){
                        onRefreshTouchListener.onUpdateRefreshState(REFRESHING);
                    }
                    startAnimation(getScrollY(), refreshHeaderLayout.getMeasuredHeight());
                } else {
                    mText.setText("下拉刷新...");
                    LogUtils.i("下拉刷新...");
                    refreshState = PULL_TO_REFRESH;
                    if(null != onRefreshTouchListener){
                        onRefreshTouchListener.onUpdateRefreshState(PULL_TO_REFRESH);
                    }
                    startAnimation(getScrollY(), 0);
                }
                break;
        }

        return super.onTouchEvent(event);
    }

    private void startAnimation(float start, final float end) {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(Math.abs(start), Math.abs(end));
        valueAnimator.start();
        valueAnimator.setDuration(200);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float fraction = (float) animation.getAnimatedValue();
                setScrollY((int) -fraction);
                Log.d(TAG, "onAnimationUpdate: " + fraction);
            }
        });

        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if(!mRefreshing){
                    if(null != onRefreshTouchListener){
                        onRefreshTouchListener.onRefreshReset();
                    }
                }
                if (end == 0) {
                    mLoadIng.setVisibility(View.GONE);
                    mStartLoad.setVisibility(View.VISIBLE);
                    mDrawable.stop();
                    LogUtils.i("停止动画---" + refreshState);
                    if(REFRESHING == refreshState){
                        if(null != onRefreshTouchListener){
                            onRefreshTouchListener.onUpdateAnimState(ANIM_STATE.ANIM_STOP);
                        }
                    }

                } else {
                    mLoadIng.setVisibility(View.VISIBLE);
                    mStartLoad.setVisibility(View.GONE);
                    mDrawable.start();
                    LogUtils.i("开始动画---" + refreshState);
                    if(REFRESHING == refreshState){
                        if(null != onRefreshTouchListener){
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

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (mRefreshing) {
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
                if (isDownOrUp && isDown) {
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

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        measureChildren(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        refreshHeaderLayout.layout(l, t - refreshHeaderLayout.getMeasuredHeight(), r, t);
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


    private View refreshHeaderLayout;
    /**
     * 动态增加下拉刷新布局
     * @param refreshHeaderLayout 刷新布局
     */
    public void addRefreshLayout(View refreshHeaderLayout) {
        removeView(this.refreshHeaderLayout);
        this.refreshHeaderLayout = refreshHeaderLayout;
        addView(this.refreshHeaderLayout);

    }

    public interface OnRefreshListener {
        void OnRefresh();
    }

    public void setOnRefreshTouchListener(OnRefreshTouchListener onRefreshTouchListener) {
        this.onRefreshTouchListener = onRefreshTouchListener;
    }

    private OnRefreshTouchListener onRefreshTouchListener;

    public interface OnRefreshTouchListener{

        public void onRefreshMove(float moveY);

        public void onRefreshReset();

        public void onUpdateRefreshState(REFRESH_STATE refreshState);

        public void onUpdateAnimState(ANIM_STATE animState);

    }

    private REFRESH_STATE refreshState = REFRESH_NONE;

    public enum REFRESH_STATE {
        PULL_TO_REFRESH, RELEASE_TO_REFRESH, REFRESHING, REFRESH_NONE
    }

    public enum ANIM_STATE {
        ANIM_NONE, ANIM_START, ANIM_STOP,
    }
}
