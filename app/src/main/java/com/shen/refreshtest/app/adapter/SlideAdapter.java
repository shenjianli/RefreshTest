package com.shen.refreshtest.app.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.shen.refresh.BaseRecycleAdapter;
import com.shen.refreshtest.R;
import com.shen.refreshtest.app.HomeFragment;
import com.shen.refreshtest.core.FrescoImageLoader;
import com.shen.refreshtest.model.ImgData;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.List;


/**

 */
public class SlideAdapter extends BaseRecycleAdapter {

    private Context context;
    private List<ImgData> slideItems;
    public SlideAdapter(Context context, List<ImgData> slideItems){
        this.context = context;
        this.slideItems = slideItems;
    }

    @Override
    public SlideViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == HomeFragment.HOME_SLIDE_TYPE){
            return new SlideViewHolder(
                    LayoutInflater.from(context).inflate(R.layout.view_pager, parent, false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof SlideViewHolder) {
            List<String> prodImgs = getProdImgs();
            if (null != prodImgs && prodImgs.size() > 0) {
                ((SlideViewHolder) holder).banner.setImages(prodImgs)
                        .setImageLoader(new FrescoImageLoader())
                        .start();
                ((SlideViewHolder) holder).banner.setOnBannerListener(new OnBannerListener() {
                    @Override
                    public void OnBannerClick(int position) {
                        if (context instanceof Activity) {
                            ImgData product = slideItems.get(position);
                            if (null != product) {
                                String httpUrl = slideItems.get(position).getHrefUrl();

                            }
                        }

                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        if(null != slideItems && slideItems.size() > 0){
            return 1;
        }
        return 0;
    }

    @Override
    public int getItemViewType() {
        return HomeFragment.HOME_SLIDE_TYPE;
    }

    public List<String> getProdImgs() {
        List<String> prodImgs = new ArrayList<>();
        if(null != slideItems && slideItems.size() > 0){
            for (ImgData product:slideItems) {
                if(null != product){
                    prodImgs.add(product.getImgUrl());
                }
            }
        }
        return prodImgs;
    }

    static class SlideViewHolder extends RecyclerView.ViewHolder {

        Banner banner;

        public SlideViewHolder(View itemView) {
            super(itemView);
            banner = (Banner) itemView.findViewById(R.id.home_banner_view);
            banner.setIndicatorGravity(BannerConfig.RIGHT);
            banner.setDelayTime(5000);
        }

    }
}
