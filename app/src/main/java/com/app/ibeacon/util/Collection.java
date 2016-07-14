package com.app.ibeacon.util;

import com.app.ibeacon.activity.MainActivity;
import com.app.ibeacon.containers.BluetoothLeDeviceStore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uk.co.alt236.bluetoothlelib.device.BluetoothLeDevice;
import uk.co.alt236.bluetoothlelib.device.beacon.BeaconType;
import uk.co.alt236.bluetoothlelib.device.beacon.BeaconUtils;

/**
 * @创建者 by muyangzi
 * @创建时间 by ${Date}
 * @描述 ${TODO}
 * @更新中 by ${author}
 * @更新时间 by ${date}
 * @更新描述 by ${todo}
 */
public class Collection {

    public static Map<String, List<String>> mALLlist;

    public static Map<String, List<String>> mALLEat;
    public static Map<String, List<String>> mALLPlay;
    public static Map<String, List<String>> mALLTravel;
    public static Map<String, List<String>> mALLHotel;
    public static IBeaconDevicesss iBeacon;

    public static Map<String, List<String>> getMap() {
        if (mALLlist == null) {
            mALLlist = new HashMap<String, List<String>>();
        }
        return mALLlist;
    }

    public static Map<String, List<String>> getmALLEat() {
        if (mALLEat == null) {
            mALLEat = new HashMap<String, List<String>>();

            IBeaconDevicesss device = getDevice();
            String address = device.getAddress();
            String uuid = device.getUUID();
            int major = device.getMajor();
            int minor = device.getMinor();
            if (major == 4) {
                List<String> list = new ArrayList<String>();
                list.add(uuid);
                list.add(String.valueOf(major));
                list.add(String.valueOf(minor));
                mALLEat.put(address, list);
            }


        }
        return mALLEat;
    }

    //
//
    public static Map<String, List<String>> getmALLPlay() {
        if (mALLPlay == null) {
            mALLPlay = new HashMap<String, List<String>>();
            IBeaconDevicesss device = getDevice();
            String address = device.getAddress();
            String uuid = device.getUUID();
            int major = device.getMajor();
            int minor = device.getMinor();
            if (major == 2) {
                List<String> list = new ArrayList<String>();
                list.add(uuid);
                list.add(String.valueOf(major));
                list.add(String.valueOf(minor));
                mALLEat.put(address, list);
            }
        }
        return mALLPlay;
    }

    //
    public static Map<String, List<String>> getmALLTravel() {
        if (mALLTravel == null) {
            mALLTravel = new HashMap<String, List<String>>();


            IBeaconDevicesss device = getDevice();
            String address = device.getAddress();
            String uuid = device.getUUID();
            int major = device.getMajor();
            int minor = device.getMinor();
            if (major == 1) {
                List<String> list = new ArrayList<String>();
                list.add(uuid);
                list.add(String.valueOf(major));
                list.add(String.valueOf(minor));
                mALLEat.put(address, list);
            }
        }
        return mALLTravel;
    }

    public static Map<String, List<String>> getmALLHotel() {
        if (mALLHotel == null) {
            mALLHotel = new HashMap<String, List<String>>();
            IBeaconDevicesss device = getDevice();
            String address = device.getAddress();
            String uuid = device.getUUID();
            int major = device.getMajor();
            int minor = device.getMinor();
            if (major == 9) {
                List<String> list = new ArrayList<String>();
                list.add(uuid);
                list.add(String.valueOf(major));
                list.add(String.valueOf(minor));
                mALLHotel.put(address, list);
            }

        }
        return mALLHotel;
    }


    private static IBeaconDevicesss getDevice() {
        BluetoothLeDeviceStore mDeviceStore = MainActivity.mDeviceStore;

        List<BluetoothLeDevice> deviceList = mDeviceStore.getDeviceList();

        for (BluetoothLeDevice device : deviceList) {
            if (BeaconUtils.getBeaconType(device) == BeaconType.IBEACON) {

                iBeacon = new IBeaconDevicesss(device);

            }

        }
        return iBeacon;
    }

    public  static  void  setColletionNull(){
        mALLEat=null;
        mALLlist=null;
        mALLHotel=null;
        mALLPlay=null;
        mALLTravel=null;


    }

}

