package com.shen.refreshtest.core;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.shen.refreshtest.app.HomeFragment;

/**
 * Created by jerry on 2017/10/25.
 */

public class SpacesItemDecoration extends RecyclerView.ItemDecoration{

    private int space;

    public SpacesItemDecoration(int space) {
        this.space = space;
    }


    @Override
    public void getItemOffsets(Rect outRect, View view,
                               RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view);
        int type = parent.getAdapter().getItemViewType(position);
        if(position > 2){
            if(type == HomeFragment.HOME_HOT_SALE_TYPE || type == HomeFragment.HOME_RCM_TYPE){
                outRect.left = space;
                outRect.right = space;
                outRect.bottom = space;

                // Add top margin only for the first item to avoid double space between items
                if (parent.getChildLayoutPosition(view) == 0) {
                    outRect.top = space;
                } else {
                    outRect.top = 0;
                }
            }
        }
    }
}
