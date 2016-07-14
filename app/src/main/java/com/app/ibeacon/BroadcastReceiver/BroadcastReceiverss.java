package com.app.ibeacon.BroadcastReceiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.app.ibeacon.R;
import com.app.ibeacon.activity.Push;
import com.app.ibeacon.util.Collection;
import com.app.ibeacon.util.PreferenceUtils;

import java.util.Iterator;
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
    @Override
    public void onReceive(Context context, Intent intent) {
        String uuid;
        int icon;
        int dis;
        String major;
        String minor;
        boolean aBoolean = false;

        int flagActivityNewTask = Intent.FLAG_ACTIVITY_NEW_TASK;
        if (intent.getAction().equals("android.intent.pushedservice")) {
//            Log.i("TAG", "成功1");
//
//            Iterator<Map.Entry<String, List<String>>> iterator = mArray.entrySet().iterator();
//            while (iterator.hasNext()) {
//                Map.Entry<String, List<String>> next = iterator.next();
//                List<String> value = next.getValue();
//                Object[] array = value.toArray();
//                uuid = (String) array[0];
//                major = (String) array[1];
//                minor = (String) array[2];
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
                Log.i("TAGonReceive", uuid + major + minor+aBoolean);
                if (aBoolean) {
                    aBoolean = false;
                } else {
                    aBoolean = true;
                }
                if (aBoolean) {
                    Intent mintent = new Intent(context, Push.class);
                    mResultIntent = PendingIntent.getActivity(context, 1, mintent,
                            flagActivityNewTask);
                    manager = (NotificationManager) context.
                            getSystemService(Context.NOTIFICATION_SERVICE);

                    mBuilder = new NotificationCompat.Builder(context);
                    mBuilder.setContentTitle(major+"  "+minor)//设置通知栏标题
                            .setContentText("测试内容") //设置通知栏显示内容
                            //设置通知栏点击意图
//  .setNumber(number) //设置通知集合的数量
                            .setTicker("有新消息来啦") //通知首次出现在通知栏，带上升动画效果的
                            .setWhen(System.currentTimeMillis())//通知产生的时间，会在通知信息里显示，一般是系统获取到的时间
                            .setPriority(Notification.PRIORITY_DEFAULT) //设置该通知优先级
//  .setAutoCancel(true)//设置这个标志当用户单击面板就可以让通知将自动取消
                            .setOngoing(false)//ture，设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)
                            .setDefaults(Notification.DEFAULT_VIBRATE)//向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合
                            //Notification.DEFAULT_ALL  Notification.DEFAULT_SOUND 添加声音 // requires VIBRATE permission
                            .setSmallIcon(R.drawable.ic_device_ibeacon)//设置通知小ICON
                            .setContentIntent(mResultIntent).setAutoCancel(true);
                    manager.notify(3, mBuilder.build());

                    Log.i("TAG11", "成功");
                }
                PreferenceUtils.putBoolean(context, uuid + major + minor, true);

            }
        }


    }

}