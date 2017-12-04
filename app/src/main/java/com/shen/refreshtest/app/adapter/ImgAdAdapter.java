package com.shen.refreshtest.app.adapter;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.view.SimpleDraweeView;

import com.shen.refresh.BaseRecycleAdapter;
import com.shen.refresh.util.LogUtils;
import com.shen.refreshtest.Constants;
import com.shen.refreshtest.R;
import com.shen.refreshtest.app.HomeFragment;
import com.shen.refreshtest.model.ImgData;

import java.util.List;

/**
 * 图片广告模块的adapter
 */
public class ImgAdAdapter extends BaseRecycleAdapter {

    private Context context;
    private List<ImgData> imgAdItems;

    public ImgAdAdapter(Context context, List<ImgData> imgAdItems){
        this.context = context;
        this.imgAdItems = imgAdItems;
    }

    /**
     * 创建图片广告的的viewholder
     * @param parent
     * @param viewType 指定的类型
     * @return
     */
    @Override
    public ImgAdHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == Constants.HOME_IMG_AD_TYPE){
            return new ImgAdHolder(
                    LayoutInflater.from(context).inflate(R.layout.view_image_ad, parent, false));
        }
        return null;
    }

    /**
     * 对view holder 进行数据绑定
     * @param holder
     * @param position 表示此类型的位置索引
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ImgAdHolder) {
            ImgData imgData = imgAdItems.get(position);
            if(null != imgData){
                String imgUrl = imgData.getImgUrl();
                final String imgAction  = imgData.getHrefUrl();
                Uri uri = Uri.parse(imgUrl);
                ((SimpleDraweeView)holder.itemView).setImageURI(uri);

                GenericDraweeHierarchy hierarchy = ((SimpleDraweeView)holder.itemView).getHierarchy();
                hierarchy.setPlaceholderImage(R.mipmap.ic_launcher, ScalingUtils.ScaleType.FIT_XY);
                hierarchy.setFailureImage(R.mipmap.ic_launcher, ScalingUtils.ScaleType.FIT_XY);


                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        LogUtils.i("点击地址：" + imgAction);
                    }
                });
            }

        }
    }

    @Override
    public int getItemCount() {
        return null == imgAdItems ? 0: imgAdItems.size();
    }

    @Override
    public int getItemViewType() {
        return Constants.HOME_IMG_AD_TYPE;
    }

    /**
     * 图片广告的view holder类
     */
    static class ImgAdHolder extends RecyclerView.ViewHolder{
        public ImgAdHolder(View itemView) {
            super(itemView);
        }
    }
}
