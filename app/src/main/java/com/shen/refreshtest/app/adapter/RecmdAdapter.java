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
import com.shen.refreshtest.Constants;
import com.shen.refreshtest.R;
import com.shen.refreshtest.app.HomeFragment;
import com.shen.refreshtest.model.Product;

import java.util.List;


/**
 * 大数据推荐adapter
 */
public class RecmdAdapter extends BaseRecycleAdapter {

    private Context context;
    private List<Product> products;


    public RecmdAdapter(Context context, List<Product> products){
        this.context = context;
        this.products = products;
    }

    /**
     * 创建大数据的view holder
     * @param parent
     * @param viewType 指定的类型
     * @return
     */
    @Override
    public RcmViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == Constants.HOME_RCM_TYPE){
            return new RcmViewHolder(
                    LayoutInflater.from(context).inflate(R.layout.item_hot_sale_layout, parent, false));
        }
        return null;
    }

    /**
     * 对大数据进行数据绑定
     * @param holder
     * @param position 表示此类型的位置索引
     */
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

    /**
     * 返回大数据类型值
     * @return
     */
    @Override
    public int getItemViewType() {
        return Constants.HOME_RCM_TYPE;
    }

    /**
     * 返回大数据推荐的数目
     * @return
     */
    @Override
    public int getItemCount() {
        return null == products ? 0 : products.size();
    }

    /**
     * 大数据推荐view holder
     */
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
