package com.app.ibeacon.activity;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;

import com.app.ibeacon.R;
import com.app.ibeacon.adapter.BluetoothManager;
import com.app.ibeacon.containers.BluetoothLeDeviceStore;
import com.app.ibeacon.containers.ShopIbean;
import com.app.ibeacon.service.PushedService;
import com.app.ibeacon.util.BluetoothUtils;
import com.app.ibeacon.util.Collection;
import com.app.ibeacon.util.PreferenceUtils;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import uk.co.alt236.bluetoothlelib.device.BluetoothLeDevice;


public class MainActivity extends Activity {
    private View mLocation;
    private View mPrice;
    private View mPushed;
    private View mPersonalization;
    static MyServiceConnection mycon;
    private PushedService msgService;
    Intent mService;

    Map<String, List<String>> mArray;
    public static BluetoothLeDeviceStore mDeviceStore;
    public final static String DATA_URL = "/data/data/";
    protected BluetoothUtils mBluetoothUtils;
    private String TAG = "TAG";
    /**
     * 自定义的打开 Bluetooth 的请求码，与 onActivityResult 中返回的 requestCode 匹配。
     */
    private static final int REQUEST_CODE_BLUETOOTH_ON = 1313;

    /**
     * Bluetooth 设备可见时间，单位：秒。
     */
    private static final int BLUETOOTH_DISCOVERABLE_DURATION = 250;
    private static ShopIbean mALLDeviceMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initview();
        mService = new Intent(MainActivity.this, PushedService.class);
        mycon = new MyServiceConnection();
        mDeviceStore = new BluetoothLeDeviceStore();
        mDeviceStore.clear();
        mArray = Collection.getMap();
        mBluetoothUtils = new BluetoothUtils(this);
        if (getShopIbeacon() == null) {
            mALLDeviceMap = new ShopIbean();
        }
        if ((BluetoothManager.isBluetoothSupported())
                && (!BluetoothManager.isBluetoothEnabled())) {
//            打开蓝牙
            turnOnBluetooth();
        }

        initFalse(mArray);
    }


    private final BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(final BluetoothDevice device, final int rssi, final byte[] scanRecord) {

            final BluetoothLeDevice deviceLe = new BluetoothLeDevice(device, rssi, scanRecord, System.currentTimeMillis());
            mDeviceStore.addDevice(deviceLe);

        }
    };


    //    调取服务
    public class MyServiceConnection implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // TODO Auto-generated method stub

            // 3,接口的方式绑定服务
//                 MainActivity.this.mBinder = (MyInterface) service;
//                     mBinder.sendInformation();
            msgService = ((PushedService.MyCallMethod) service).getService();
//        调取服务中的方法
            msgService.init();

        }


        @Override
        public void onServiceDisconnected(ComponentName name) {
            // TODO Auto-generated method stub


        }

    }


    @Override
    protected void onStart() {
        super.onStart();
        Log.i("TAG", "activity start");
//        mBluetoothUtils.getBluetoothAdapter().startLeScan(mLeScanCallback);
        init();
        bindService(mService, mycon, Context.BIND_AUTO_CREATE);


    }


    @Override
    protected void onResume() {
        super.onResume();
        if (BluetoothManager.isBluetoothSupported() && BluetoothManager.isBluetoothEnabled()) {

            BluetoothAdapter.getDefaultAdapter().startLeScan(mLeScanCallback);
        }


    }


    @Override
    protected void onPause() {
        super.onPause();

    }

    private void initview() {
        mLocation = findViewById(R.id.location);
        mPersonalization = findViewById(R.id.Personalization);
        mPrice = findViewById(R.id.Price);
        mPushed = findViewById(R.id.Pushed);
    }

    private void init() {

        mLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(MainActivity.this, Location.class);
                startActivity(mIntent);
            }
        });


        mPersonalization.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(MainActivity.this, Personalization.class);
                startActivity(mIntent);
            }
        });
        mPushed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(MainActivity.this, Push.class);
                startActivity(mIntent);
            }
        });
        mPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(MainActivity.this, Price.class);
                startActivity(mIntent);
            }
        });

    }


    public void turnOnBluetooth() {
        // 请求打开 Bluetooth
        Intent requestBluetoothOn = new Intent(
                BluetoothAdapter.ACTION_REQUEST_ENABLE);

        // 设置 Bluetooth 设备可以被其它 Bluetooth 设备扫描到
        requestBluetoothOn
                .setAction(BluetoothAdapter.ACTION_REQUEST_ENABLE);

        // 设置 Bluetooth 设备可见时间
        requestBluetoothOn.putExtra(
                BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION,
                BLUETOOTH_DISCOVERABLE_DURATION);

        // 请求开启 Bluetooth
        this.startActivityForResult(requestBluetoothOn,
                REQUEST_CODE_BLUETOOTH_ON);

    }


    @Override
    protected void onDestroy() {
        mBluetoothUtils.getBluetoothAdapter().stopLeScan(mLeScanCallback);

        Log.i("TAG", "activity 销毁");

        //将已经推送过的改为fale

        Log.i("LLLL", mArray.size() + "");
        unbindService(mycon);
        stopService(mService);

        Log.i("TAGunb", "activity  unb 销毁");


        super.onDestroy();
    }

    public static ShopIbean getShopIbeacon() {
        return mALLDeviceMap;
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

            PreferenceUtils.putBoolean(MainActivity.this, String.valueOf(uuid) + String.valueOf(major) + String.valueOf(minor), false);
            boolean aBoolean = PreferenceUtils.getBoolean(MainActivity.this, String.valueOf(uuid) + String.valueOf(major) + String.valueOf(minor));
            Log.i("Size1", uuid + "" + major + minor + aBoolean);
        }

    }

}



