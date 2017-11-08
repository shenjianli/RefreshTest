package com.shen.refreshtest.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shen.refresh.BaseRecycleAdapter;
import com.shen.refreshtest.R;
import com.shen.refreshtest.app.HomeFragment;


/**

 */
public class HotSaleTitleAdapter extends BaseRecycleAdapter {

    private Context context;
    private String title;


    public HotSaleTitleAdapter(Context context, String title){
        this.context = context;
        this.title = title;
    }

    @Override
    public TitleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == HomeFragment.HOME_HOT_SALE_TITLE_TYPE){
            return new TitleViewHolder(
                    LayoutInflater.from(context).inflate(R.layout.item_hot_sale_title_layout, parent, false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof TitleViewHolder){
            ((TitleViewHolder) holder).title.setText("猜你喜欢");
        }

    }

    @Override
    public int getItemViewType() {
        return HomeFragment.HOME_HOT_SALE_TITLE_TYPE;
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    public static class TitleViewHolder extends RecyclerView.ViewHolder {

        private RelativeLayout sumLayout;
        private TextView title;

        public TitleViewHolder(View itemView) {
            super(itemView);
            sumLayout = (RelativeLayout) itemView.findViewById(R.id.item_hot_sale_title_layout);
            title = (TextView) itemView.findViewById(R.id.item_hot_sale_title);
        }
    }
}
