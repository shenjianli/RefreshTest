package com.shen.refreshtest.app;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shen.refresh.BaseLoadMoreView;
import com.shen.refreshtest.R;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.shen.refresh.RefreshLoadAdapter.LOAD_MORE_FAILURE;
import static com.shen.refresh.RefreshLoadAdapter.LOAD_MORE_LOADING;
import static com.shen.refresh.RefreshLoadAdapter.LOAD_MORE_SUCCESS;

/**
 * Created by jerry shen on 2017/11/9.
 */

public class LoadMoreCustomView extends BaseLoadMoreView {


    @Override
    public RecyclerView.ViewHolder onCreateItemView(ViewGroup parent) {
        LoadMoreViewHolder loadMoreHolder = new LoadMoreViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.loading_more_custom_layout, parent, false));
        return loadMoreHolder;
    }

    public void bindView(final RecyclerView.ViewHolder holder, int position, int loadState) {
        if( holder instanceof LoadMoreViewHolder){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onLoadMoreListener != null) {
                        if (((LoadMoreViewHolder)holder).mLoading.getVisibility() == View.GONE && ((LoadMoreViewHolder)holder).mLoaded.getVisibility() == VISIBLE) {
                            onLoadMoreListener.onLoadMore();
                        }
                    }
                }
            });
            switch (loadState) {
                case LOAD_MORE_LOADING:
                    ((LoadMoreViewHolder)holder).mLoaded.setVisibility(GONE);
                    ((LoadMoreViewHolder)holder).mLoading.setVisibility(VISIBLE);
                    break;
                case LOAD_MORE_SUCCESS:
                    ((LoadMoreViewHolder)holder).mLoaded.setVisibility(GONE);
                    ((LoadMoreViewHolder)holder).mLoading.setVisibility(GONE);
                    break;
                case LOAD_MORE_FAILURE:
                    ((LoadMoreViewHolder)holder).mLoaded.setVisibility(VISIBLE);
                    ((LoadMoreViewHolder)holder).mLoading.setVisibility(GONE);
                    break;
            }
        }

    }

    static class LoadMoreViewHolder extends RecyclerView.ViewHolder {

        LinearLayout mLoading;
        TextView mLoaded;

        public LoadMoreViewHolder(View itemView) {
            super(itemView);
            mLoading = (LinearLayout) itemView.findViewById(com.shen.refresh.R.id.loading);
            mLoaded = (TextView) itemView.findViewById(com.shen.refresh.R.id.loaded);
        }

    }
}
