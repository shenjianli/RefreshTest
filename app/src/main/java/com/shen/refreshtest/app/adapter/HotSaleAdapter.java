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
import com.facebook.stetho.common.StringUtil;
import com.shen.refresh.BaseRecycleAdapter;
import com.shen.refresh.util.LogUtils;
import com.shen.refreshtest.Constants;
import com.shen.refreshtest.R;
import com.shen.refreshtest.app.HomeFragment;
import com.shen.refreshtest.model.Product;

import java.util.List;


/**
 * 热销商品模块adapter
 */
public class HotSaleAdapter extends BaseRecycleAdapter {

    private Context context;
    /**
     * 热销商品数据集合
     */
    private List<Product> products;

    public HotSaleAdapter(Context context, List<Product> products){
        this.context = context;
        this.products = products;
    }

    /**
     * 创建热销商品的view holder
     * @param parent
     * @param viewType 指定的类型
     * @return
     */
    @Override
    public HotSaleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == Constants.HOME_HOT_SALE_TYPE){
            return new HotSaleViewHolder(
                    LayoutInflater.from(context).inflate(R.layout.item_hot_sale_layout, parent, false));
        }
        return null;
    }

    /**
     * 对数据进行绑定
     * @param holder
     * @param position 表示此类型的位置索引
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if( holder instanceof HotSaleViewHolder){
            final Product product = products.get(position);
            Uri uri = Uri.parse(product.getImgUrl());
            ((HotSaleViewHolder) holder).prodImg.setImageURI(uri);
            ((HotSaleViewHolder) holder).prodName.setText(product.getName());
            ((HotSaleViewHolder) holder).prodPrice.setText(product.getPrice());
            ((HotSaleViewHolder) holder).prodSumLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String url = product.getUrl();
                    if(!TextUtils.isEmpty(url)){
                        LogUtils.i("点击地址：" + url);
                    }
                }
            });
        }
    }

    /**
     * 返回热销商品模块类型值，这个值越大越靠近顶部
     * @return
     */
    @Override
    public int getItemViewType() {
        return Constants.HOME_HOT_SALE_TYPE;
    }

    /**
     * 返回热销商品的个数
     * @return
     */
    @Override
    public int getItemCount() {
        return null == products ? 0 : products.size();
    }

    /**
     * 热销模块的布局viewholder
     */
    public static class HotSaleViewHolder extends RecyclerView.ViewHolder {

        private RelativeLayout prodSumLayout;
        private SimpleDraweeView prodImg;
        private TextView prodName;
        private TextView prodPrice;
        private TextView prodLabel;


        public HotSaleViewHolder(View itemView) {
            super(itemView);
            prodSumLayout = (RelativeLayout) itemView.findViewById(R.id.grid_sum_layout);
            prodImg = (SimpleDraweeView) itemView.findViewById(R.id.prod_grid_img_sdv);
            prodName = (TextView) itemView.findViewById(R.id.prod_grid_name);;
            prodPrice = (TextView) itemView.findViewById(R.id.prod_grid_price_tv);;
            prodLabel = (TextView) itemView.findViewById(R.id.prod_grid_label_tv);;
        }
    }
}
