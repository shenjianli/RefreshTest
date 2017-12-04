package com.shen.refreshtest.app;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.shen.refresh.RefreshConentAdapter;
import com.shen.refreshtest.Constants;

/**
 * Created by jerry on 2017/10/25.
 *  主要用来对首页模块增加分割线
 */

public class SpacesItemDecoration extends RecyclerView.ItemDecoration {




    public SpacesItemDecoration() {
    }


    @Override
    public void getItemOffsets(Rect outRect, View view,
                               RecyclerView parent, RecyclerView.State state) {

        int homePosition = parent.getChildAdapterPosition(view);

        RecyclerView.Adapter adapter = parent.getAdapter();

        if (null != adapter) {
            if (adapter instanceof RefreshConentAdapter) {
                int type = parent.getAdapter().getItemViewType(homePosition);
                /**
                 * 对不同的type进行添加不同的分割线
                 */
                if (type == Constants.HOME_HOT_SALE_TYPE || type == Constants.HOME_RCM_TYPE) {
                    int firstIndex = ((RefreshConentAdapter) adapter).getFirstItemByType(type);
                    int position = homePosition - firstIndex;
                    if (position % 2 == 0) {
                        outRect.left = 0;
                        outRect.right = 5;
                    } else {
                        outRect.left = 0;
                        outRect.right = 0;
                    }
                    outRect.bottom = 5;
                    outRect.top = 0;
                } else if (type == Constants.HOME_NOTICE_TYPE) {
                    outRect.bottom = 25;
                    outRect.top = 25;
                }
                else if (type == Constants.HOME_IMG_AD_TYPE) {
                    outRect.top = 0;
                }
            }
        }
    }
}
