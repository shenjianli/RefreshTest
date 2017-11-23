package com.shen.refresh;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

/**
 * Created by ljq on 2017/10/26.
 */

public abstract class BaseRecycleAdapter {

    private boolean isScrolling = false;

    public abstract RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType);

    public abstract void onBindViewHolder(RecyclerView.ViewHolder holder, int position);


    public abstract int getItemCount();

    public abstract int getItemViewType();

    public boolean isScrolling() {
        return isScrolling;
    }

    public void setScrolling(boolean scrolling) {
        isScrolling = scrolling;
    }


    /**
     * 当Item进入这个页面的时候调用
     * @param holder
     */
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {

    }

    /**
     * 当Item被回收的时候调用
     * @param holder
     */
    public void onViewRecycled(RecyclerView.ViewHolder holder) {

    }
}
