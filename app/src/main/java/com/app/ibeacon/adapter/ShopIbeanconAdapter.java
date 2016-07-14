package com.app.ibeacon.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.app.ibeacon.R;
import com.app.ibeacon.util.IBeaconDevicesss;

import uk.co.alt236.bluetoothlelib.device.BluetoothLeDevice;
import uk.co.alt236.bluetoothlelib.device.beacon.BeaconType;
import uk.co.alt236.bluetoothlelib.device.beacon.BeaconUtils;
import uk.co.alt236.bluetoothlelib.device.beacon.ibeacon.IBeaconDevice;
import uk.co.alt236.easycursor.objectcursor.EasyObjectCursor;

/**
 * @创建者 by muyangzi
 * @创建时间 by ${Date}
 * @描述 ${TODO}
 * @更新中 by ${author}
 * @更新时间 by ${date}
 * @更新描述 by ${todo}
 */
public class ShopIbeanconAdapter extends SimpleCursorAdapter {

    //距离值
    static double mAccuracy;
    private final LayoutInflater mInflator;
    private final Activity mActivity;


    static String uuid;
    static int major;
    static int minor;

    public ShopIbeanconAdapter(final Activity activity, final EasyObjectCursor<BluetoothLeDevice> cursor) {
        super(activity, R.layout.list_push, cursor, new String[0], new int[0], 0);
        mInflator = activity.getLayoutInflater();
        mActivity = activity;
    }


    @SuppressWarnings("unchecked")
    @Override
    public EasyObjectCursor<BluetoothLeDevice> getCursor() {

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
        ViewHolder holder = null;

        if (view == null) {

            holder = new ViewHolder();
            view = mInflator.inflate(R.layout.list_push, null);
            holder.img = (ImageView) view.findViewById(R.id.list_push_image);

            holder.title = (TextView) view.findViewById(R.id.list_push_title);

            holder.desc = (TextView) view.findViewById(R.id.list_push_describle);

            view.setTag(holder);

        } else {
            holder = (ViewHolder) view.getTag();
        }
        String major = "";
        String uuid = "";
        String minor = "";
        final BluetoothLeDevice device = getCursor().getItem(i);
        if (BeaconUtils.getBeaconType(device) == BeaconType.IBEACON) {
            final IBeaconDevicesss iBeacon = new IBeaconDevicesss(device);
            minor = String.valueOf(iBeacon.getMinor());
            major = String.valueOf(iBeacon.getMajor());


                holder.title.setText(minor + "商店");
                holder.desc.setText(major + "你好");


        }
        return view;

    }

    public final class ViewHolder {
        public TextView title;
        public TextView desc;
        public ImageView img;

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
    }
}