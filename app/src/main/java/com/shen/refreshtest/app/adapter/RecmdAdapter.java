package com.shen.refreshtest.app.adapter;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.shen.refresh.BaseRecycleAdapter;
import com.shen.refresh.util.LogUtils;
import com.shen.refreshtest.R;
import com.shen.refreshtest.app.HomeFragment;
import com.shen.refreshtest.model.Product;

import java.util.List;


/**

 */
public class RecmdAdapter extends BaseRecycleAdapter {

    private Context context;
    private List<Product> products;


    public RecmdAdapter(Context context, List<Product> products){
        this.context = context;
        this.products = products;
    }


    @Override
    public RcmViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == HomeFragment.HOME_RCM_TYPE){
            return new RcmViewHolder(
                    LayoutInflater.from(context).inflate(R.layout.item_hot_sale_layout, parent, false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if(holder instanceof RcmViewHolder){
            final Product product = products.get(position);
            Uri uri = Uri.parse(product.getImgUrl());
            ((RcmViewHolder) holder).prodImg.setImageURI(uri);
            ((RcmViewHolder) holder).prodName.setText(product.getName());
            ((RcmViewHolder) holder).prodPrice.setText(product.getPrice());
            ((RcmViewHolder) holder).prodSumLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String url = product.getUrl();
                    if(!TextUtils.isEmpty(url)){
                        LogUtils.i("点击跳转地址：" + url);
                    }
                }
            });
        }
    }

    @Override
    public int getItemViewType() {
        return HomeFragment.HOME_RCM_TYPE;
    }

    @Override
    public int getItemCount() {
        return null == products ? 0 : products.size();
    }


    public static class RcmViewHolder extends RecyclerView.ViewHolder {

        private RelativeLayout prodSumLayout;
        private SimpleDraweeView prodImg;
        private TextView prodName;
        private TextView prodPrice;
        private TextView prodLabel;


        public RcmViewHolder(View itemView) {
            super(itemView);
            prodSumLayout = (RelativeLayout) itemView.findViewById(R.id.grid_sum_layout);
            prodImg = (SimpleDraweeView) itemView.findViewById(R.id.prod_grid_img_sdv);
            prodName = (TextView) itemView.findViewById(R.id.prod_grid_name);;
            prodPrice = (TextView) itemView.findViewById(R.id.prod_grid_price_tv);;
            prodLabel = (TextView) itemView.findViewById(R.id.prod_grid_label_tv);;
        }
    }
}
