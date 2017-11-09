package com.shen.refresh;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.shen.refresh.RefreshLoadAdapter.LOAD_MORE_FAILURE;
import static com.shen.refresh.RefreshLoadAdapter.LOAD_MORE_LOADING;
import static com.shen.refresh.RefreshLoadAdapter.LOAD_MORE_SUCCESS;

/**
 * Created by jerry shen on 2017/11/9.
 */

public abstract class BaseLoadMoreView {

    /**
     * 加载更多的回调方法，方便加载失败后，点击重试后进行调用
     */
    protected RecyclerLoadMoreView.OnLoadMoreListener onLoadMoreListener;

    /**
     * 设置加载更多
     * @param onLoadMoreListener
     */
    public void setOnLoadMoreListener(RecyclerLoadMoreView.OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    public abstract RecyclerView.ViewHolder onCreateItemView(ViewGroup parent);

    public abstract void bindView(RecyclerView.ViewHolder holder, int position, int loadState);
}
