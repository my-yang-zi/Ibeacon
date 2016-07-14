package com.app.ibeacon.context;

import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.os.Handler;

import java.util.HashMap;
import java.util.Map;

/**
 * 创建者     伍碧林
 * 创建时间   2016/1/23 11:12
 * 描述	     全局的盒子,单例
 * 描述	     存放一些共有的,大家都需要的对象,这样就可以创建一次
 * <p/>
 * 更新者     $Author: admin $
 * 更新时间   $Date: 2016-01-27 09:13:20 +0800 (星期三, 27 一月 2016) $
 * 更新描述   ${TODO}
 */
public class MyApplication extends Application {

    private static Context mContext;
    private static Handler mHandler;
    private static long mMainThreadId;

    //定义一个协议内存缓存的容器/存储结构
    private Map<String, String> mProtocolHashMap = new HashMap<>();
    private static BluetoothManager mBluetoothManager;
    private  static BluetoothAdapter mBluetoothAdapter;
    public Map<String, String> getProtocolHashMap() {

        return mProtocolHashMap;
    }

    /**
     * 得到上下文
     */
    public static Context getContext() {
        return mContext;
    }

//    获得蓝牙管理器
    public  static BluetoothManager getBluetoothManager(){
        return mBluetoothManager;
    }
//    获得蓝牙适配器

    public   static BluetoothAdapter getBluetoothAdapter(){


        return mBluetoothAdapter;
    }

    /**
     * 得到主线程的handler
     */
    public static Handler getHandler() {
        return mHandler;
    }


    /**
     * 得到主线程的id
     */
    public static long getMainThreadId() {
        return mMainThreadId;
    }

    @Override
    public void onCreate() {//程序的入口方法
        //1.上下文
        mContext = getApplicationContext();

        //2.得到主线程的handler
        mHandler = new Handler();

        //3.得到主线程的id
        mMainThreadId = android.os.Process.myTid();
        /**
         Tid: Thread
         Uid:User
         Pid:Process
         */

        super.onCreate();
    }

}
