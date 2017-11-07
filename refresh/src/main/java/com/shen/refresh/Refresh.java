package com.shen.refresh;

import com.shen.refresh.util.LogUtils;

/**
 * Created by jerry shen on 2017/11/7.
 */

public class Refresh {

    private static Refresh refresh;

    private Refresh(){

    }

    public static Refresh getRefresh(){
        return RefreshHodler.refresh;
    }

    /**
     * 设置打印日志使能状态
     * @param enable true 表示打印日志  false 表示不打印日志
     * @return 返回日志的打印状态
     */
    public boolean setLogEnable(boolean enable){
        LogUtils.isOutPutLog = enable;
        return LogUtils.isOutPutLog;
    }


    static class RefreshHodler{
        private static Refresh refresh = new Refresh();
    }

}
