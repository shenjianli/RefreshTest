package com.shen.refreshtest.app.adapter;

import android.app.Activity;
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
import com.shen.refreshtest.core.NoticeView;
import com.shen.refreshtest.model.NoticeData;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**

 */
public class NoticeAdapter extends BaseRecycleAdapter {

    private Context context;
    private List<NoticeData> noticeDatas;

    public NoticeAdapter(Context context, List<NoticeData> noticeDatas){
        this.context = context;
        this.noticeDatas = noticeDatas;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == HomeFragment.HOME_NOTICE_TYPE){
            return new NoticeHolder(
                    LayoutInflater.from(context).inflate(R.layout.view_notice, parent, false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(null != noticeDatas){
            if(holder instanceof NoticeHolder){
                List<String> notices = getNotices();
                if(null != notices){
                    ((NoticeHolder)holder).noticeView.addNotice(notices);
                    ((NoticeHolder)holder).noticeView.startFlipping();
                    ((NoticeHolder)holder).noticeView.setOnNoticeClickListener(new NoticeView.OnNoticeClickListener() {
                        @Override
                        public void onNotieClick(int position, String notice) {
                            NoticeData noticeData = noticeDatas.get(position);
                            if (null != noticeData){
                                String action = noticeData.getMsgUrl();

                            }
                        }
                    });
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        if(null != noticeDatas && noticeDatas.size() > 0){
            return 1;
        }
        return 0;
    }

    @Override
    public int getItemViewType() {
        return HomeFragment.HOME_NOTICE_TYPE;
    }


    public List<String> getNotices() {
        List<String> notices = new ArrayList<>();
        if(null != noticeDatas){
            for (NoticeData noticeData : noticeDatas) {
                notices.add(noticeData.getMsgDesc());
            }
        }
        return notices;
    }

    static class NoticeHolder extends RecyclerView.ViewHolder{
        RelativeLayout noticeViewLayout;
        NoticeView noticeView;
        TextView noticeMore;
        public NoticeHolder(View itemView) {
            super(itemView);
            noticeViewLayout = (RelativeLayout) itemView.findViewById(R.id.notice_view_layout);
            noticeView = (NoticeView) itemView.findViewById(R.id.notice_view);
            noticeMore = (TextView) itemView.findViewById(R.id.notice_more);
        }
    }
}
