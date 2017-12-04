package com.shen.refresh;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

/**
 * Created by ljq on 2017/10/26.
 */

public abstract class BaseRecycleAdapter {
    /**
     * 表示是否正在滚动，可根据这个值来判断加载耗时操作
     */
    private boolean isScrolling = false;

    /**
     * 返回指定类型的viewholder
     * @param parent
     * @param viewType 指定的类型
     * @return 返回viewType的viewHolder
     */
    public abstract RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType);

    /**
     * 对指定的hodler进行数据绑定
     * @param holder
     * @param position 表示此类型的位置索引
     */
    public abstract void onBindViewHolder(RecyclerView.ViewHolder holder, int position);

    /**
     * 返回指定type的具体数目
     * @return
     */
    public abstract int getItemCount();

    /**
     * 返回指定类型值 所有模块的值最好不要相同，且值越小越靠近顶部
     * @return
     */
    public abstract int getItemViewType();

    /**
     * 表示列表是否正在滚动
     * @return
     */
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
