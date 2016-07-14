package com.app.ibeacon.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.app.ibeacon.R;
import com.app.ibeacon.adapter.ShopIbeanconAdapter;
import com.app.ibeacon.containers.ShopIbean;
import com.app.ibeacon.util.Collection;
import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;

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
public class Push extends Activity {
    Map<String, List<String>> mArray;
    protected ListView mListView;
    protected MaterialRefreshLayout materialRefreshLayout;
    Map<String, List<String>> map;
    ShopIbean mALLDeviceMap;
    ShopIbeanconAdapter myAdapper;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.push_activity);
        intview();
        mArray = Collection.getMap();
        mALLDeviceMap = MainActivity.getShopIbeacon();

    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    private void intview() {

        mListView = (ListView) findViewById(R.id.push_list);
    }

    @Override

    protected void onResume() {
        super.onResume();
        myAdapper = new ShopIbeanconAdapter(Push.this, mALLDeviceMap.getDeviceCursor());
        mListView.setAdapter(myAdapper);
//        刷新
        materialRefreshLayout = (MaterialRefreshLayout) findViewById(R.id.refresh);
        materialRefreshLayout.setMaterialRefreshListener(new MaterialRefreshListener() {
                          @Override
                     public void onRefresh(final MaterialRefreshLayout materialRefreshLayout) {
                        //refreshing...
                              materialRefreshLayout.postDelayed(new Runnable() {
                                  @Override
                                  public void run() {

                                      myAdapper = new ShopIbeanconAdapter(Push.this, mALLDeviceMap.getDeviceCursor());
                                      mListView.setAdapter(myAdapper);
                                      materialRefreshLayout.finishRefresh();
                                  }
                              }, 2000);
                          }

                       @Override
                             public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                                  //load more refreshing...
                                }
                });

// refresh complete

    materialRefreshLayout.autoRefresh();
// load more refresh complete
       materialRefreshLayout.finishRefreshLoadMore();

    }

    public final class ViewHolder {
        public TextView title;
        public TextView desc;
        public ImageView img;

    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();


    }
}