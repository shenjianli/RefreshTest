package com.shen.refresh;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
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
 *
 * getItemViewType(获取显示类型，返回值可在onCreateViewHolder中拿到，以决定加载哪种ViewHolder)

 * onCreateViewHolder(加载ViewHolder的布局)

 * onViewAttachedToWindow（当Item进入这个页面的时候调用）

 * onBindViewHolder(将数据绑定到布局上，以及一些逻辑的控制就写这啦)

 * onViewDetachedFromWindow（当Item离开这个页面的时候调用）

 * onViewRecycled(当Item被回收的时候调用)
 *
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


    private Map<String,BaseRecycleAdapter> holderTypeMap = new HashMap<>();

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
                    RecyclerView.ViewHolder homeViewHolder = baseHomeAdapter.onCreateViewHolder(parent, viewType);

                    String holderName = homeViewHolder.getClass().getName();

                    if(!holderTypeMap.containsKey(holderName)){
                        holderTypeMap.put(homeViewHolder.getClass().getName(),baseHomeAdapter);
                    }

                    return homeViewHolder;

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
                if(-1 != firstIndex){
                    BaseRecycleAdapter baseHomeAdapter = baseHomeAdapters.get(viewType);
                    //根据位置索引对加入的Adapter进行绑定数据有调用
                    if (null != baseHomeAdapter) {
                        long time = System.currentTimeMillis();
                        baseHomeAdapter.setScrolling(isScrolling());
                        baseHomeAdapter.onBindViewHolder(holder, position - firstIndex);
                        LogUtils.i("类型：" + viewType  + "，进行数据绑定");
                        LogUtils.i(viewType + "耗时：" +  (System.currentTimeMillis() - time));
                    }
                }
            }
//        }
    }

    /**
     * 当Item进入这个页面的时候调用
     */
    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        String holderKey = holder.getClass().getName();
        if(!TextUtils.isEmpty(holderKey)){
            if(null != holderTypeMap && holderTypeMap.size() > 0){
                BaseRecycleAdapter recycleAdapter = holderTypeMap.get(holderKey);
                if(null != recycleAdapter){
                    recycleAdapter.onViewAttachedToWindow(holder);
                    LogUtils.i(holderKey + " 正在进入视野...");
                }
            }
        }
    }


    @Override
    public void onViewRecycled(RecyclerView.ViewHolder holder) {
        super.onViewRecycled(holder);
        String holderKey = holder.getClass().getName();
        if(!TextUtils.isEmpty(holderKey)){
            if(null != holderTypeMap && holderTypeMap.size() > 0){
                BaseRecycleAdapter recycleAdapter = holderTypeMap.get(holderKey);
                if(null != recycleAdapter){
                    recycleAdapter.onViewRecycled(holder);
                    LogUtils.i(holderKey + " 正在进行回收...");
                }
            }
        }
    }

    /**
     * 根据类型值返回该类型第一项值的位置索引
     * @param viewType 具体的类型
     * @return 返回位置索引
     */
    public int getFirstItemByType(int viewType) {
        int index = - 1;
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

    /**
     * 根据类型值返回该类型最后一项值的位置索引
     * @param viewType 具体的类型
     * @return 返回位置索引
     */
    public int getLastItemIndexByType(int viewType) {
        int index = -1;
        for (int i = 0; i < refreshDatas.size(); i++){
            RefreshData refreshData = refreshDatas.get(i);
            if(null != refreshData){
                if(viewType == refreshData.getType()){
                    index = i;
                }
                //如果遍历到大于viewType的类型则结束
                if(viewType < refreshData.getType()){
                    break;
                }
            }
        }
        return index;
    }

    /**
     * 根据类型值返回该类型数目
     * @param viewType 具体的类型
     * @return 返回类型数目
     */
    public int getItemSizeByType(int viewType) {
        int num = 0;
        for (int i = 0; i < refreshDatas.size(); i++){
            RefreshData refreshData = refreshDatas.get(i);
            if(null != refreshData){
                if(viewType == refreshData.getType()){
                    num = num + 1;
                }
                //如果遍历到大于viewType的类型则结束
                if(viewType < refreshData.getType()){
                    break;
                }
            }
        }
        return num;
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
                            //表示要删除所有所有类型项
                            if(hasCount > 0 && count == 0){
                                removeHomeItemByType(type);
                                return ;
                            }
							//表示删除原来项中-updateCnt项
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

    /**
     * 根据类型刷新相应的item
     * @param itemType
     */
    public void notifyItemChangedByType(int itemType) {
        int firstIndex = getFirstItemByType(itemType);
        int itemCount = getItemSizeByType(itemType);
        if(-1 != firstIndex){
            if(1 == itemCount){
                notifyItemChanged(firstIndex);
            }
            else {
                notifyItemRangeChanged(firstIndex, itemCount);
            }
        }
    }
}
