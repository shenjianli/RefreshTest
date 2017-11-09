package com.shen.refresh;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.shen.refresh.util.LogUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by Administrator on 2017/8/4.
 * 包名:com.young.jdmall.ui.adapter
 * 时间:2017/8/4
 */

public class RefreshConentAdapter extends RefreshLoadAdapter {

    /**
     *用于存储相对位置的type类型数据
     */
    private List<RefreshData> refreshDatas = new ArrayList<>();
    /**
     *相关类型对应的adapter的map集合
     */
    private Map<Integer, BaseRecycleAdapter> baseHomeAdapters = new HashMap<>();
    /**
     *创建的上下文
     */
    private Context context;

    public RefreshConentAdapter(){}

    public RefreshConentAdapter(Context context) {
        this.context = context;
    }

    /**
     * 增加新的apter对象
     * @param baseHomeAdapter 具体的adapter
     */
    public void addAdapter(BaseRecycleAdapter baseHomeAdapter) {
        if (null != baseHomeAdapter) {
            if(!baseHomeAdapters.containsValue(baseHomeAdapter)){
                int type = baseHomeAdapter.getItemViewType();
                if (!baseHomeAdapters.containsKey(type)) {
                    baseHomeAdapters.put(type, baseHomeAdapter);
                    LogUtils.i("进行增加Adapter对象");
                }
                int count = baseHomeAdapter.getItemCount();
                if (count > 0) {
                    RefreshData refreshData;
                    for (int i = 0; i < count; i++) {
                        refreshData = new RefreshData();
                        refreshData.setType(type);
                        refreshDatas.add(refreshData);
                    }
                    LogUtils.i("新增加" + type + "类型的Item" + count + "项");
                }
            }else{
                LogUtils.i("该对象已经添加过了，不需要重新添加");
                LogUtils.i("若要刷新，请调用 updateItemNumByType 方法");
            }
        }
        else {
            LogUtils.i("增加的Adapter对象为空");
        }
    }


    /**
     * 增加新的apter对象
     * @param baseHomeAdapter 具体的adapter
     */
    public void appendAdapter(BaseRecycleAdapter baseHomeAdapter) {
        if (null != baseHomeAdapter) {
            if(!baseHomeAdapters.containsValue(baseHomeAdapter)){
                int type = baseHomeAdapter.getItemViewType();
                if (!baseHomeAdapters.containsKey(type)) {
                    baseHomeAdapters.put(type, baseHomeAdapter);
                    LogUtils.i("进行增加Adapter对象");
                    addHomeItemByType(type);
                }
                else {
                    updateItemNumByType(type);
                }
            }else{
                LogUtils.i("该对象已经添加过了，不需要重新添加");
                LogUtils.i("若要刷新，请调用 updateItemNumByType 方法");
            }
        }
        else {
            LogUtils.i("增加的Adapter对象为空");
        }
    }

    @Override
    protected RecyclerView.ViewHolder onCreateViewHolderToRecyclerLoadMoreView(ViewGroup parent, int viewType) {
            if (null != baseHomeAdapters) {
                BaseRecycleAdapter baseHomeAdapter = baseHomeAdapters.get(viewType);
                //根据viewType调用加入的adapter的创建指定布局的方法
                if (null != baseHomeAdapter) {
                    return baseHomeAdapter.onCreateViewHolder(parent, viewType);
                }
            }
//        }
        return null;
    }

    @Override
    protected void onBindViewHolderToRecyclerLoadMoreView(RecyclerView.ViewHolder holder, final int position) {
            if (null != baseHomeAdapters) {
                RefreshData refreshData = refreshDatas.get(position);
                int viewType = refreshData.getType();
                int firstIndex = getFirstItemByType(viewType);
                BaseRecycleAdapter baseHomeAdapter = baseHomeAdapters.get(viewType);
                //根据位置索引对加入的Adapter进行绑定数据有调用
                if (null != baseHomeAdapter) {
                    baseHomeAdapter.onBindViewHolder(holder, position - firstIndex);
                }
            }
//        }
    }


    /**
     * 根据类型值返回该类型第一项值的位置索引
     * @param viewType 具体的类型
     * @return 返回位置索引
     */
    private int getFirstItemByType(int viewType) {
        int index = 0;
        for (int i = 0; i < refreshDatas.size(); i++){
            RefreshData refreshData = refreshDatas.get(i);
            if(null != refreshData){
                if(viewType == refreshData.getType()){
                    index = i;
                    break;
                }
            }
        }
        return index;
    }

    @Override
    protected int getItemCountToRecyclerLoadMoreView() {
        if (refreshDatas != null) {
            if (refreshDatas.size() != 0) {
                return refreshDatas.size();
            }
        }
        return 0;
    }

    /**
     * 返回列表数据集合大小
     * @return
     */
    public int getItemNum() {
        if (null != refreshDatas && refreshDatas.size() > 0) {
            return refreshDatas.size();
        }
        return 0;
    }

    @Override
    protected int getItemViewTypeToRecyclerLoadMoreView(int position) {

        if (null != refreshDatas && refreshDatas.size() > 0 && position < refreshDatas.size()) {
            return refreshDatas.get(position).getType();
        }
        return -1;

    }


    /**
     * 查找列表是否有某类型的view
     * @param itemType 具体的类型
     * @return
     */
    public boolean hasItem(int itemType) {
        if (null != refreshDatas) {
            for (int i = 0; i < refreshDatas.size(); i++) {
                RefreshData refreshData = refreshDatas.get(i);
                int type = refreshData.getType();
                if (itemType == type) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 全部删除指定类型的数据
     * @param type 具体类型
     */
    public void removeHomeItemByType(int type) {
        List<RefreshData> deletes = new ArrayList<>();
        if (null != refreshDatas && refreshDatas.size() > 0) {
            for (RefreshData refreshData : refreshDatas) {
                int itemType = refreshData.getType();
                if (itemType == type) {
                    deletes.add(refreshData);
                }
            }
            refreshDatas.removeAll(deletes);
        }

    }

    /**
     * 当界面不再有type类型的界面时，新增加type进可以调用此方法
     * @param type 具体类型
     */
    public void addHomeItemByType(int type) {

        if (null != refreshDatas) {

            if (null != baseHomeAdapters) {
                if(baseHomeAdapters.containsKey(type)){
                    BaseRecycleAdapter baseRecycleAdapter = baseHomeAdapters.get(type);
                    if (null != baseRecycleAdapter) {
                        //需要新增数据的索引
                        int insertIndex = -1;
                        //查的需要新增数据的位置
                        for (int i = 0; i < refreshDatas.size(); i++) {
                            RefreshData refreshData = refreshDatas.get(i);
                            int itemType = refreshData.getType();
                            if(itemType == type){
                                LogUtils.i("集合中已经有" + type + "类型的数据，请调用 updateItemNumByType 进行更新");
                                return;
                            }
                            if (itemType > type) {
                                insertIndex = i;
                                break;
                            }
                        }

                        int count = baseRecycleAdapter.getItemCount();
                        if (count > 0) {
                            RefreshData refreshData;
                            // 进行新增数据
                            for (int i = 0; i < count; i++) {
                                refreshData = new RefreshData();
                                refreshData.setType(type);
                                if (-1 != insertIndex) {
                                    refreshDatas.add(insertIndex, refreshData);
                                } else {
                                    refreshDatas.add(refreshData);
                                }

                            }

                            LogUtils.i("成功新增加" + type + "类型的数据" + count + "个");
                        }
                    }
                }
            }
        }

    }

    /**
     * 根据类型来更新界面
     * @param type 具体的类型
     */
    public void updateItemNumByType(int type){
        if(null != baseHomeAdapters){
           BaseRecycleAdapter baseRecycleAdapter =  baseHomeAdapters.get(type);
            if(null != baseRecycleAdapter){
                //表示要插入或是删除位置
                int firstIndex = -1;
                int hasCount = 0;
                int count = baseRecycleAdapter.getItemCount();

                if(null != refreshDatas){
                    //遍历查找需要插入或删除的位置
                    for(int i = 0; i < refreshDatas.size(); i++){
                        RefreshData refreshData = refreshDatas.get(i);
                        if(null != refreshData){
                            if(type == refreshData.getType()){
                                hasCount ++;
                                if(-1 == firstIndex){
                                    firstIndex = i;
                                }
                            }
                            //如果剩下的type类型都比要更新大，则可以结束查找
                            if(refreshData.getType() > type){
                                break;
                            }
                        }
                    }
                    //如果集合中现有项与更新后项数目不同时
                    if(hasCount != count){
                        int updateCnt = count - hasCount;
                        //表示新增加的需要插入
                        if(updateCnt > 0 ){
                            //表示列表中完全没有了type这一项，新增加type项
                            if(hasCount == 0 && count > 0){
                                addHomeItemByType(type);
                                return ;
                            }
                            //表示原来有type这一项，只是数目上增多了
                            for (int i = 0; i < updateCnt; i++){
                                RefreshData refreshData = new RefreshData();
                                refreshData.setType(type);
                                refreshDatas.add(firstIndex, refreshData);
                            }
                        } //表示原来有type这一项，需要删除，数目上减少
                        else{
                            if(hasCount > 0 && count == 0){
                                removeHomeItemByType(type);
                                return ;
                            }

                            List<RefreshData> deletes = new ArrayList<>();
                            for (int i = 0; i < -updateCnt; i++){
                                deletes.add(refreshDatas.get(firstIndex + i));
                            }
                            if(deletes.size() > 0){
                                refreshDatas.removeAll(deletes);
                            }
                        }
                    }

                }
            }
        }

    }

}
