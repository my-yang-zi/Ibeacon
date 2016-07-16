package com.app.ibeacon.util;

import uk.co.alt236.bluetoothlelib.device.BluetoothLeDevice;
import uk.co.alt236.bluetoothlelib.device.beacon.ibeacon.IBeaconDevice;
import uk.co.alt236.bluetoothlelib.device.beacon.ibeacon.IBeaconDistanceDescriptor;
import uk.co.alt236.bluetoothlelib.device.beacon.ibeacon.IBeaconManufacturerData;


/**
 * @创建者 by muyangzi
 * @创建时间 by ${Date}
 * @描述 ${TODO}
 * @更新中 by ${author}
 * @更新时间 by ${date}
 * @更新描述 by ${todo}
 */
public class IBeaconDevicesss extends IBeaconDevice {

    private final IBeaconManufacturerData mIBeaconData;

    public IBeaconDevicesss(BluetoothLeDevice device) {
        super(device);
        mIBeaconData = new IBeaconManufacturerData(this);
    }

    public IBeaconDistanceDescriptor getDistanceDescriptor() {
        return IBeaconUtilsss.getDistanceDescriptor(getAccuracy());
    }

}
