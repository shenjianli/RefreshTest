package com.shen.refreshtest.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shen.refresh.BaseRecycleAdapter;
import com.shen.refreshtest.Constants;
import com.shen.refreshtest.R;
import com.shen.refreshtest.core.NoticeView;
import com.shen.refreshtest.model.NoticeData;

import java.util.ArrayList;
import java.util.List;

/**
 * 快讯模块adapter
 */
public class NoticeAdapter extends BaseRecycleAdapter {

    private Context context;
    private List<NoticeData> noticeDatas;

    public NoticeAdapter(Context context, List<NoticeData> noticeDatas){
        this.context = context;
        this.noticeDatas = noticeDatas;
    }

    /**
     * 创建快讯view holder
     * @param parent
     * @param viewType 指定的类型
     * @return
     */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == Constants.HOME_NOTICE_TYPE){
            return new NoticeHolder(
                    LayoutInflater.from(context).inflate(R.layout.view_notice, parent, false));
        }
        return null;
    }

    /**
     * 进行快讯数据绑定
     * @param holder
     * @param position 表示此类型的位置索引
     */
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

    /**
     * 返回快讯类型值
     * @return
     */
    @Override
    public int getItemViewType() {
        return Constants.HOME_NOTICE_TYPE;
    }

    /**
     * 获取需要显示的快讯文字集合
     * @return
     */
    public List<String> getNotices() {
        List<String> notices = new ArrayList<>();
        if(null != noticeDatas){
            for (NoticeData noticeData : noticeDatas) {
                notices.add(noticeData.getMsgDesc());
            }
        }
        return notices;
    }

    /**
     * 快讯view holder
     */
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
