package com.app.ibeacon.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.app.ibeacon.R;
import com.app.ibeacon.adapter.BluetoothManager;
import com.app.ibeacon.adapter.LeDeviceListAdapter;
import com.app.ibeacon.containers.BluetoothLeDeviceStore;
import com.app.ibeacon.util.BluetoothLeScanner;
import com.app.ibeacon.util.BluetoothUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import uk.co.alt236.bluetoothlelib.device.BluetoothLeDevice;
import uk.co.alt236.easycursor.objectcursor.EasyObjectCursor;

/**
 * @创建者 by muyangzi
 * @创建时间 by ${Date}
 * @描述 ${TODO}
 * @更新中 by ${author}
 * @更新时间 by ${date}
 * @更新描述 by ${todo}
 */
public class Location extends Activity implements AdapterView.OnItemClickListener{
    @Bind(R.id.tvBluetoothLe)
    protected TextView mTvBluetoothLeStatus;
    @Bind(R.id.tvBluetoothStatus)
    protected TextView mTvBluetoothStatus;
    @Bind(R.id.tvItemCount)
    protected TextView mTvItemCount;
    @Bind(android.R.id.list)
    protected ListView mList;
    @Bind(android.R.id.empty)
    protected View mEmpty;

    protected BluetoothUtils mBluetoothUtils;
    protected BluetoothLeScanner mScanner;
    protected LeDeviceListAdapter mLeDeviceListAdapter;
    protected BluetoothLeDeviceStore mDeviceStore;




    private int REQUEST_ENABLE_BT = 1;
//    private BluetoothAdapter.LeScanCallback mLeScanCallback;
//    BluetoothAdapter mBluetoothAdapter;
//    private UsbManager mDeviceStore;
//    private CharacterData mLeDeviceListAdapter;

    /**
     * 自定义的打开 Bluetooth 的请求码，与 onActivityResult 中返回的 requestCode 匹配。
     */
    private static final int REQUEST_CODE_BLUETOOTH_ON = 1313;

    /**
     * Bluetooth 设备可见时间，单位：秒。
     */
    private static final int BLUETOOTH_DISCOVERABLE_DURATION = 250;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.location_activity);
        ButterKnife.bind(this);
        mList.setEmptyView(mEmpty);
        mList.setOnItemClickListener(this);
        mDeviceStore = new BluetoothLeDeviceStore();
        mBluetoothUtils = new BluetoothUtils(this);
        mScanner = new BluetoothLeScanner(mLeScanCallback, mBluetoothUtils);
        updateItemCount(0);

        if ((BluetoothManager.isBluetoothSupported())
                && (!BluetoothManager.isBluetoothEnabled())) {
//            打开蓝牙
            turnOnBluetooth();
        }
        startScan();


    }


    @Override
    protected void onStart() {
        super.onStart();

    }




    /**
     * 弹出系统弹框提示用户打开 Bluetooth
     */
    public void turnOnBluetooth() {
        // 请求打开 Bluetooth
        Intent requestBluetoothOn = new Intent(
                BluetoothAdapter.ACTION_REQUEST_ENABLE);

        // 设置 Bluetooth 设备可以被其它 Bluetooth 设备扫描到
        requestBluetoothOn
                .setAction(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);

        // 设置 Bluetooth 设备可见时间
        requestBluetoothOn.putExtra(
                BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION,
                BLUETOOTH_DISCOVERABLE_DURATION);

        // 请求开启 Bluetooth
       this.startActivityForResult(requestBluetoothOn,
                REQUEST_CODE_BLUETOOTH_ON);

    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        // requestCode 与请求开启 Bluetooth 传入的 requestCode 相对应
//        if (requestCode == REQUEST_CODE_BLUETOOTH_ON) {
//            switch (resultCode) {
//                // 点击确认按钮
//                case Activity.RESULT_OK: {
//                    // TODO 用户选择开启 Bluetooth，Bluetooth 会被开启
//
//                }
//                break;
//
//                // 点击取消按钮或点击返回键
//                case Activity.RESULT_CANCELED: {
//                    // TODO 用户拒绝打开 Bluetooth, Bluetooth 不会被开启
//                }
//                break;
//                default:
//                    break;
//            }
//        }
//    }
//




      private   final BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
            @Override
            public void onLeScan(final BluetoothDevice device, final int rssi, final byte[] scanRecord) {

                final BluetoothLeDevice deviceLe = new BluetoothLeDevice(device, rssi, scanRecord, System.currentTimeMillis());
                mDeviceStore.addDevice(deviceLe);
                final EasyObjectCursor<BluetoothLeDevice> c = mDeviceStore.getDeviceCursor();
//                int size1 = mDeviceStore.getDeviceList().size();
//                String s1 = String.valueOf(size1);
//                Log.i("TAG",s1);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mLeDeviceListAdapter.swapCursor(c);
                        updateItemCount(mLeDeviceListAdapter.getCount());
                    }
                });



            }
        };

    private void displayAboutDialog() {
        // REALLY REALLY LAZY LINKIFIED DIALOG
        final int paddingSizeDp = 5;
        final float scale = getResources().getDisplayMetrics().density;
        final int dpAsPixels = (int) (paddingSizeDp * scale + 0.5f);

        final TextView textView = new TextView(this);
        final SpannableString text = new SpannableString(getString(R.string.about_dialog_text));

        textView.setText(text);
        textView.setAutoLinkMask(RESULT_OK);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        textView.setPadding(dpAsPixels, dpAsPixels, dpAsPixels, dpAsPixels);

        Linkify.addLinks(text, Linkify.ALL);
        new AlertDialog.Builder(this)
                .setTitle(R.string.menu_about)
                .setCancelable(false)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                    }
                })
                .setView(textView)
                .show();
    }
//  创建actionbar

    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        if (!mScanner.isScanning()) {
            menu.findItem(R.id.menu_stop).setVisible(false);
            menu.findItem(R.id.menu_scan).setVisible(true);
            menu.findItem(R.id.menu_refresh).setActionView(null);
        } else {
            menu.findItem(R.id.menu_stop).setVisible(true);
            menu.findItem(R.id.menu_scan).setVisible(false);
            menu.findItem(R.id.menu_refresh).setActionView(R.layout.actionbar_progress_indeterminate);
        }

        if (mList.getCount() > 0) {
            menu.findItem(R.id.menu_share).setVisible(true);
        } else {
            menu.findItem(R.id.menu_share).setVisible(false);
        }

        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        final BluetoothLeDevice device = mLeDeviceListAdapter.getItem(position);
        if (device == null) return;

        final Intent intent = new Intent(this, DeviceDetailsActivity.class);
        intent.putExtra(DeviceDetailsActivity.EXTRA_DEVICE, device);

        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_scan:
                startScan();
                break;
            case R.id.menu_stop:
                mScanner.scanLeDevice(-1, false);
                invalidateOptionsMenu();
                break;
            case R.id.menu_about:
                displayAboutDialog();
                break;
            case R.id.menu_share:
                mDeviceStore.shareDataAsEmail(this);
        }
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();
//        mScanner.scanLeDevice(-1, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        final boolean mIsBluetoothOn = mBluetoothUtils.isBluetoothOn();
        final boolean mIsBluetoothLePresent = mBluetoothUtils.isBluetoothLeSupported();
//  获得焦点，重新开始搜索
        if(mIsBluetoothLePresent&&mIsBluetoothOn){
            startScan();
        }

        if (mIsBluetoothOn) {
            mTvBluetoothStatus.setText(R.string.on);
        } else {
            mTvBluetoothStatus.setText(R.string.off);
        }

        if (mIsBluetoothLePresent) {
            mTvBluetoothLeStatus.setText(R.string.supported);
        } else {
            mTvBluetoothLeStatus.setText(R.string.not_supported);
        }

        invalidateOptionsMenu();
    }


//    打开蓝牙
    public  void  startScan(){
        final boolean mIsBluetoothOn = mBluetoothUtils.isBluetoothOn();
        final boolean mIsBluetoothLePresent = mBluetoothUtils.isBluetoothLeSupported();
        mDeviceStore.clear();
        updateItemCount(0);

        mLeDeviceListAdapter = new LeDeviceListAdapter(this, mDeviceStore.getDeviceCursor());
        mList.setAdapter(mLeDeviceListAdapter);
//请求打开蓝
//        mBluetoothUtils.askUserToEnableBluetoothIfNeeded();
        if (mIsBluetoothOn && mIsBluetoothLePresent) {
            mScanner.scanLeDevice(-1, true);
            invalidateOptionsMenu();
        }
    }

    private void updateItemCount(final int count) {
        mTvItemCount.setText(
                getString(
                        R.string.formatter_item_count,
                        String.valueOf(count)));
    }




    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
