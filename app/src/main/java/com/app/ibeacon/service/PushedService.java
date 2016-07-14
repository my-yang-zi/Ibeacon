package com.app.ibeacon.service;

import android.app.Service;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.app.ibeacon.BroadcastReceiver.BroadcastReceiverss;
import com.app.ibeacon.R;
import com.app.ibeacon.activity.MainActivity;
import com.app.ibeacon.containers.BluetoothLeDeviceStore;
import com.app.ibeacon.containers.ShopIbean;
import com.app.ibeacon.util.Collection;
import com.app.ibeacon.util.Constants;
import com.app.ibeacon.util.IBeaconDevicesss;
import com.app.ibeacon.util.PreferenceUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import uk.co.alt236.bluetoothlelib.device.BluetoothLeDevice;
import uk.co.alt236.bluetoothlelib.device.beacon.BeaconType;
import uk.co.alt236.bluetoothlelib.device.beacon.BeaconUtils;
import uk.co.alt236.bluetoothlelib.device.beacon.ibeacon.IBeaconDevice;


/**
 * @创建者 by muyangzi
 * @创建时间 by ${Date}
 * @描述 ${TODO}
 * @更新中 by ${author}
 * @更新时间 by ${date}
 * @更新描述 by ${todo}
 */
public class PushedService extends Service {
    public static volatile boolean exit = false;
    MyThread thread;
    private String TAG = "TAG";
    BroadcastReceiverss receiver;
    double mAccuracy;
    Map<String, List<String>> mArray;

    @Override
    public void onCreate() {
        super.onCreate();

//        这边采用的是静态注册，下面是动态注册
//        IntentFilter filter = new IntentFilter();
//        filter.addAction("Pushed");
//        registerReceiver(receiver, filter);

    }

    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        //2.接受到绑定信息.使用代理模式，将服务中的myMethod()暴露出去给activity中使用

        return new MyCallMethod(); //返回的mCallmethod对象会在onServiceConnected()中调用
    }

    //        private void myMethod(){
//          Toast.makeText(getApplicationContext(), "我是服务里的方法", 0).show();
//         }
    public class MyCallMethod extends Binder {
        public PushedService getService() {
            return PushedService.this;
        }

    }

    public void init() {
        exit=false;
        thread = new MyThread();
        thread.start();
    }
    class MyThread extends    Thread {
        @Override
        public void run() {
            while (!exit) {
                final BluetoothLeDeviceStore store = MainActivity.mDeviceStore;

                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                List<BluetoothLeDevice> deviceList;
                deviceList = store.getDeviceList();
                IBeaconDevicesss iBeacon;
                Map<String, List<String>> mALLMap;
                Map<String, List<String>> mALLEat;
                Map<String, List<String>> mALLPlay;
                Map<String, List<String>> mALLTravel;
                Map<String, List<String>> mALLHotel;


                                        Log.i("TAG", "8888");
                for (BluetoothLeDevice device : deviceList)
                    if (BeaconUtils.getBeaconType(device) == BeaconType.IBEACON) {
//                            Log.i("TAG", "9999");
                        iBeacon = new IBeaconDevicesss(device);
                        String uuid = iBeacon.getUUID();
                        int major = iBeacon.getMajor();
                        int minor = iBeacon.getMinor();

                        double distance = getDistance(device, iBeacon);

//                            存储数据
//                            Log.i("PushService",major+minor+""+device.getAddress());
                        Map<String, List<String>> map;
                        map = Collection.getMap();
                        List list=new ArrayList();
                        list.add(uuid);
                        list.add(major);
                        list.add(minor);
                        map.put(device.getAddress(),list);


//                            加设备
                        ShopIbean shopIbeacon = MainActivity.getShopIbeacon();
                        shopIbeacon.addDevice(device);

//                            Log.i("Size",mALLMap.size()+"");
//                        String s=String.valueOf(distance);
//                        Log.i(TAG, s);
//
                        if (distance < 10) {
                            Intent intent = new Intent("android.intent.pushedservice");
                            Bundle bundle = new Bundle();
                            bundle.putInt("Icon", R.drawable.ic_device_ibeacon);
                            bundle.putString("uuid",uuid);
                            bundle.putInt("major", major);
                            bundle.putInt("minor", minor);
                            bundle.putInt("dis", (int) distance);
                            intent.putExtras(bundle);
                            if (major==4 && PreferenceUtils.getBoolean(PushedService.this, Constants.EAT_4)) {
                                sendBroadcast(intent);
                            }
                            if (major==2 && PreferenceUtils.getBoolean(PushedService.this, Constants.TRAVEL_2)) {
                                sendBroadcast(intent);
                            }
                            if (major==9 && PreferenceUtils.getBoolean(PushedService.this, Constants.HOTEL_9)) {
                                sendBroadcast(intent);
                            }



////                                Log.i(TAG, "N>Ot");
//                            }

                        }
                    }
            }
        }
    }
    @Override
    public void unbindService(ServiceConnection conn) {
        super.unbindService(conn);

    }

    @Override
    public void onDestroy() {
        // unregisterReceiver(receiver);
        PushedService.exit=true;
        Log.i("TAGonDestroy", "service 销毁");
        mArray = Collection.getMap();
//      initFalse(mArray);
        super.onDestroy();


    }

    public double getDistance(BluetoothLeDevice device, IBeaconDevice iBeacon) {
        int calibratedTxPower;
        double runningAverageRssi;
        calibratedTxPower = iBeacon.getCalibratedTxPower();
        runningAverageRssi = device.getRunningAverageRssi();


        mAccuracy = calculateAccuracy(calibratedTxPower, runningAverageRssi);
        return mAccuracy;

    }

    public static double calculateAccuracy(final int txPower, final double rssi) {
        if (rssi == 0) {
            return -1.0; // if we cannot determine accuracy, return -1.
        }

        final double ratio = rssi * 1.0 / txPower;
        if (ratio < 1.0) {
            return Math.pow(ratio, 10);

        } else if (1.0 <= ratio && ratio < 1.2) {

            return (0.89976) * Math.pow(ratio, 7.7095) + 0.111;
        } else if (1.2 <= ratio && ratio < 1.4) {


            return (0.89976) * Math.pow(ratio, 6.7095) + 0.111;
        } else


            return (0.89976) * Math.pow(ratio, 5.7095) + 0.111;


//        int iRssi = (int) Math.abs(rssi);
//       float power = (float) ((iRssi-59)/(10*2.0));
//        return Math.pow(10, power);
    }

    private void initFalse(Map<String, List<String>> mArray) {
        Iterator<Map.Entry<String, List<String>>> iterator = mArray.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, List<String>> next = iterator.next();
            List<String> value = next.getValue();
            Object[] array = value.toArray();
            Object uuid = array[0];
            Object major = array[1];
            Object minor = array[2];

            PreferenceUtils.putBoolean(PushedService.this, String.valueOf(uuid) + String.valueOf(major) + String.valueOf(minor), false);
            boolean aBoolean = PreferenceUtils.getBoolean(PushedService.this, String.valueOf(uuid) + String.valueOf(major) + String.valueOf(minor));
            Log.i("Size1", uuid + "" + major + minor + aBoolean);
        }

    }

}
