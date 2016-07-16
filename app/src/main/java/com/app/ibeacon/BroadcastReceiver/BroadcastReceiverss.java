package com.app.ibeacon.BroadcastReceiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.app.ibeacon.R;
import com.app.ibeacon.activity.UpdateActivity;
import com.app.ibeacon.util.PreferenceUtils;

import java.util.List;
import java.util.Map;

/**
 * @创建者 by muyangzi
 * @创建时间 by ${Date}
 * @描述 ${TODO}
 * @更新中 by ${author}
 * @更新时间 by ${date}
 * @更新描述 by ${todo}
 */
public class BroadcastReceiverss extends android.content.BroadcastReceiver {
    NotificationManager manager;
    NotificationCompat.Builder mBuilder;
    private PendingIntent mResultIntent;
    Map<String, List<String>> mArray;
    Context mContext;
    @Override
    public void onReceive(Context context, Intent intent) {
        String uuid;
        int icon;
        int dis;
        mContext = context;
        String major;
        String minor;
        boolean aBoolean = false;
        int NOTIFICATION_ID;
        int flagActivityNewTask = Intent.FLAG_ACTIVITY_NEW_TASK;
        NOTIFICATION_ID = (int) (Math.random() * 10000);
        if (intent.getAction().equals("android.intent.pushedservice")) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                icon = (int) bundle.get("Icon");
                uuid = (String) bundle.get("uuid");
                major = String.valueOf(bundle.get("major"));
                minor = String.valueOf(bundle.get("minor"));
                dis = (int) bundle.get("dis");
//               Log.i("TAGonReceive", uuid + major + minor);
                //                跳转页面
                aBoolean = PreferenceUtils.getBoolean(context, uuid + major + minor, false);
                Log.i("TAGonReceive", uuid + major + minor + aBoolean);
                if (aBoolean) {
                    aBoolean = false;
                } else {
                    aBoolean = true;
                }

                if (aBoolean) {

                    if (major.equals("2")) {
                        Intent mintent = new Intent(context, UpdateActivity.class);
                        mResultIntent = PendingIntent.getActivity(context, NOTIFICATION_ID, mintent,
                                flagActivityNewTask);
                        manager = (NotificationManager) context.
                                getSystemService(Context.NOTIFICATION_SERVICE);

                        mBuilder = new NotificationCompat.Builder(context);
                        mBuilder.setContentTitle(major + "  " + minor)//设置通知栏标题
                                .setContentText("测试内容") //设置通知栏显示内容
                                //设置通知栏点击意图
//  .setNumber(number) //设置通知集合的数量
                                .setTicker("有新消息来啦") //通知首次出现在通知栏，带上升动画效果的
                                .setWhen(System.currentTimeMillis())//通知产生的时间，会在通知信息里显示，一般是系统获取到的时间
                                .setPriority(Notification.PRIORITY_DEFAULT) //设置该通知优先级
//  .setAutoCancel(true)//设置这个标志当用户单击面板就可以让通知将自动取消
                                .setOngoing(false)//ture，设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)
                                //向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合
//                            Notification.DEFAULT_SOUND 添加声音 // requires VIBRATE permission
                                .setSmallIcon(R.drawable.ic_device_ibeacon)//设置通知小ICON
                                .setContentIntent(mResultIntent).setAutoCancel(true);
                        mBuilder.setSound(Uri.parse("android.resource://com.app.ibeacon/raw/hello"));
                        manager.notify(NOTIFICATION_ID, mBuilder.build());
                        PreferenceUtils.putBoolean(context, uuid + major + minor, true);
                        Log.i("TAG11", "成功"+major);
                    } else {
                        Intent mintent = new Intent(context, UpdateActivity.class);
                        mResultIntent = PendingIntent.getActivity(context, NOTIFICATION_ID, mintent,
                               flagActivityNewTask );
                        manager = (NotificationManager) context.
                                getSystemService(Context.NOTIFICATION_SERVICE);

                        mBuilder = new NotificationCompat.Builder(context);
                        mBuilder.setContentTitle(major + "  " + minor)//设置通知栏标题
                                .setContentText("测试内容") //设置通知栏显示内容
                                //设置通知栏点击意图
//  .setNumber(number) //设置通知集合的数量
                                .setTicker("有新消息来啦") //通知首次出现在通知栏，带上升动画效果的
                                .setWhen(System.currentTimeMillis())//通知产生的时间，会在通知信息里显示，一般是系统获取到的时间
                                .setPriority(Notification.PRIORITY_DEFAULT) //设置该通知优先级
//  .setAutoCancel(true)//设置这个标志当用户单击面板就可以让通知将自动取消
                                .setOngoing(false)//ture，设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)
                                //向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合
//                            Notification.DEFAULT_SOUND 添加声音 // requires VIBRATE permission
                                .setSmallIcon(R.drawable.ic_device_ibeacon)//设置通知小ICON
                                .setContentIntent(mResultIntent).setAutoCancel(true);
                        mBuilder.setDefaults(Notification.DEFAULT_SOUND);
                        manager.notify(NOTIFICATION_ID, mBuilder.build());
                        PreferenceUtils.putBoolean(context, uuid + major + minor, true);
                        Log.i("TAG11", "成功"+major);
                    }


                }

            }
        }
    }
}


