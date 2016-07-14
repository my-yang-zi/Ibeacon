package com.app.ibeacon.adapter;

import android.app.Activity;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.ibeacon.R;
import com.app.ibeacon.util.Constants;
import com.app.ibeacon.util.IBeaconDevicesss;

import uk.co.alt236.bluetoothlelib.device.BluetoothLeDevice;
import uk.co.alt236.bluetoothlelib.device.beacon.BeaconType;
import uk.co.alt236.bluetoothlelib.device.beacon.BeaconUtils;
import uk.co.alt236.bluetoothlelib.device.beacon.ibeacon.IBeaconDevice;
import uk.co.alt236.easycursor.objectcursor.EasyObjectCursor;

// Adapter for holding devices found through scanning.
public class LeDeviceListAdapter extends SimpleCursorAdapter {
    //距离值
    static double mAccuracy;
    private final LayoutInflater mInflator;
    private final Activity mActivity;


    static String uuid;
    static int major;
    static int minor;

    public LeDeviceListAdapter(final Activity activity, final EasyObjectCursor<BluetoothLeDevice> cursor) {
        super(activity, R.layout.list_item_device, cursor, new String[0], new int[0], 0);
        mInflator = activity.getLayoutInflater();
        mActivity = activity;
    }

    @SuppressWarnings("unchecked")
    @Override
    public   EasyObjectCursor<BluetoothLeDevice> getCursor() {

        return ((EasyObjectCursor<BluetoothLeDevice>) super.getCursor());
    }

    @Override
    public BluetoothLeDevice getItem(final int i) {
        return getCursor().getItem(i);
    }

    @Override
    public long getItemId(final int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, final ViewGroup viewGroup) {
        final ViewHolder viewHolder;
        // General ListView optimization code.
        if (view == null) {
            view = mInflator.inflate(R.layout.list_item_device, null);
            viewHolder = new ViewHolder();
            viewHolder.deviceAddress = (TextView) view.findViewById(R.id.device_address);
            viewHolder.deviceName = (TextView) view.findViewById(R.id.device_name);
            viewHolder.deviceRssi = (TextView) view.findViewById(R.id.device_rssi);
            viewHolder.deviceIcon = (ImageView) view.findViewById(R.id.device_icon);
            viewHolder.deviceLastUpdated = (TextView) view.findViewById(R.id.device_last_update);
            viewHolder.ibeaconMajor = (TextView) view.findViewById(R.id.ibeacon_major);
            viewHolder.ibeaconMinor = (TextView) view.findViewById(R.id.ibeacon_minor);
            viewHolder.ibeaconDistance = (TextView) view.findViewById(R.id.ibeacon_distance);
            viewHolder.ibeaconUUID = (TextView) view.findViewById(R.id.ibeacon_uuid);
            viewHolder.ibeaconTxPower = (TextView) view.findViewById(R.id.ibeacon_tx_power);
            viewHolder.ibeaconSection = view.findViewById(R.id.ibeacon_section);
            viewHolder.ibeaconDistanceDescriptor = (TextView) view.findViewById(R.id.ibeacon_distance_descriptor);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        final BluetoothLeDevice device = getCursor().getItem(i);
        final String deviceName = device.getName();
        final double rssi = device.getRssi();

        if (deviceName != null && deviceName.length() > 0) {
            viewHolder.deviceName.setText(deviceName);
        } else {
            viewHolder.deviceName.setText(R.string.unknown_device);
        }

        if (BeaconUtils.getBeaconType(device) == BeaconType.IBEACON) {

            final IBeaconDevicesss iBeacon = new IBeaconDevicesss(device);
            String accuracy = Constants.DOUBLE_TWO_DIGIT_ACCURACY.format(getDistance(device, iBeacon));
            viewHolder.deviceIcon.setImageResource(R.drawable.ic_device_ibeacon);
            viewHolder.ibeaconSection.setVisibility(View.VISIBLE);
            viewHolder.ibeaconMajor.setText(String.valueOf(iBeacon.getMajor()));
            viewHolder.ibeaconMinor.setText(String.valueOf(iBeacon.getMinor()));
            viewHolder.ibeaconTxPower.setText(String.valueOf(iBeacon.getCalibratedTxPower()));
            viewHolder.ibeaconUUID.setText(iBeacon.getUUID());
            viewHolder.ibeaconDistance.setText(
                    mActivity.getString(R.string.formatter_meters, accuracy));
            viewHolder.ibeaconDistanceDescriptor.setText(iBeacon.getDistanceDescriptor().toString());



        } else {
            viewHolder.deviceIcon.setImageResource(R.drawable.ic_bluetooth);
            viewHolder.ibeaconSection.setVisibility(View.GONE);
        }

        final String rssiString =
                mActivity.getString(R.string.formatter_db, String.valueOf(rssi));
        final String runningAverageRssiString =
                mActivity.getString(R.string.formatter_db, String.valueOf(device.getRunningAverageRssi()));

        viewHolder.deviceLastUpdated.setText(
                android.text.format.DateFormat.format(
                        Constants.TIME_FORMAT, new java.util.Date(device.getTimestamp())));
        viewHolder.deviceAddress.setText(device.getAddress());
        viewHolder.deviceRssi.setText(rssiString + " / " + runningAverageRssiString);
        return view;
    }


    static class ViewHolder {
        TextView deviceName;
        TextView deviceAddress;
        TextView deviceRssi;
        TextView ibeaconUUID;
        TextView ibeaconMajor;
        TextView ibeaconMinor;
        TextView ibeaconTxPower;
        TextView ibeaconDistance;
        TextView ibeaconDistanceDescriptor;
        TextView deviceLastUpdated;
        View ibeaconSection;
        ImageView deviceIcon;
    }


    public double getDistance(BluetoothLeDevice device, IBeaconDevice iBeacon) {
        int calibratedTxPower;
        double runningAverageRssiString;
        calibratedTxPower = iBeacon.getCalibratedTxPower();
        double runningAverageRssi = device.getRunningAverageRssi();


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




}

