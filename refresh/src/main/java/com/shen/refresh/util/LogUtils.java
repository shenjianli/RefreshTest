package com.shen.refresh.util;

import android.util.Log;

import java.util.Locale;

public class LogUtils {
	
	public static boolean isOutPutLog = false;
	
	public static void v(String tag,String msg){
		if(isOutPutLog){
			Log.v(tag, msg);
		}
	}
	public static void d(String tag,String msg){
		if(isOutPutLog){
			Log.d(tag, msg);
		}
	}
	public static void i(String tag,String msg){
		if(isOutPutLog){
			Log.i(tag, msg);
		}
	}
	public static void w(String tag,String msg){
		if(isOutPutLog){
			Log.w(tag, msg);
		}
	}
	public static void e(String tag,String msg){
		if(isOutPutLog){
			Log.e(tag, msg);
		}
	}
	
	
	public static void v(String msg){
		if(isOutPutLog){
			Log.v(getTag(), buildMessage(msg));
		}
	}
	public static void d(String msg){
		if(isOutPutLog){
			Log.d(getTag(), buildMessage(msg));
		}
	}
	public static void i(String msg){
		if(isOutPutLog){
			Log.i(getTag(), buildMessage(msg));
		}
	}
	public static void w(String msg){
		if(isOutPutLog){
			Log.w(getTag(), buildMessage(msg));
		}
	}
	public static void e(String msg){
		if(isOutPutLog){
			Log.e(getTag(), buildMessage(msg));
		}
	}
	
	private static String getTag() {
	    StackTraceElement[] trace = new Throwable().fillInStackTrace()
	            .getStackTrace();
	    String callingClass = "";
	    for (int i = 2; i < trace.length; i++) {
	        Class <?> clazz = trace[i].getClass();
	        if (!clazz.equals(LogUtils.class)) {
	            callingClass = trace[i].getFileName();
	            if(callingClass.contains(".")){
	            	callingClass = callingClass.substring(0,callingClass
	            			.lastIndexOf('.'));
	            }
	            break;
	        }
	    }
	    return callingClass;
	}
	
	
	private static String buildMessage(String msg) {
	    StackTraceElement[] trace = new Throwable().fillInStackTrace()
	            .getStackTrace();
	    String caller = "";
	    String lineNum = "";
	    for (int i = 2; i< trace.length; i++) {
	        Class<?> clazz = trace[i].getClass();
	        if (!clazz.equals(LogUtils.class)) {
	            caller = trace[i].getMethodName();
	            lineNum = String.valueOf(trace[i].getLineNumber());
	            break;
	        }
	    }
	    return String.format(Locale.US, "[%d] %s %s:%s", Thread.currentThread()
	            .getId(), caller, lineNum,msg);
	}


}
