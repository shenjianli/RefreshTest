package com.shen.refresh;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

/**
 * Created by ljq on 2017/10/26.
 */

public abstract class BaseRecycleAdapter {


    public abstract RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType);

    public abstract void onBindViewHolder(RecyclerView.ViewHolder holder, int position);


    public abstract int getItemCount();

    public abstract int getItemViewType();

}
