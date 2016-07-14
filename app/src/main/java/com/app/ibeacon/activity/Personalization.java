package com.app.ibeacon.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.app.ibeacon.R;
import com.app.ibeacon.roundview.RoundTextView;
import com.app.ibeacon.roundview.RoundViewDelegate;
import com.app.ibeacon.util.Constants;
import com.app.ibeacon.util.PreferenceUtils;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * @创建者 by muyangzi
 * @创建时间 by ${Date}
 * @描述 ${TODO}
 * @更新中 by ${author}
 * @更新时间 by ${date}
 * @更新描述 by ${todo}
 */


public class Personalization extends Activity {
    @Bind(R.id.eat)
    RoundTextView mEat;
    @Bind(R.id.travel)
    RoundTextView mTravel;
    @Bind(R.id.hotel)
    RoundTextView mHotel;
    @Bind(R.id.play)
    RoundTextView mPlay;
    RoundViewDelegate Eat_delegate;
    RoundViewDelegate Hotel_delegate;
    RoundViewDelegate Play_delegate;
    RoundViewDelegate Travel_delegate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personalization_activity);
        ButterKnife.bind(Personalization.this);
        Hotel_delegate = mHotel.getDelegate();
        Eat_delegate = mEat.getDelegate();
        Play_delegate = mPlay.getDelegate();
        Travel_delegate = mTravel.getDelegate();
        init();





    }


    @Override
    protected void onStart() {
        super.onStart();
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initView() {
//        boolean aBoolean = PreferenceUtils.getBoolean(Personalization.this, Constants.EAT);
//       mEat.setBackgroundColor(getResources().getColor(R.color.colorAccent));
//       Eat_delegate.setBackgroundColor(getResources().getColor(R.color.colorAccent)).update();

//饮食
        if ( PreferenceUtils.getBoolean(Personalization.this, Constants.EAT)) {
            Eat_delegate.setBackgroundColor(getResources().getColor(R.color.red)).update();
//            设置逻辑
            PreferenceUtils.putBoolean(Personalization.this,Constants.EAT_4,true);

        } else {
            Eat_delegate.setBackgroundColor(getResources().getColor(R.color.yellow)).update();
            PreferenceUtils.putBoolean(Personalization.this,Constants.EAT_4,false);
        }

        if (PreferenceUtils.getBoolean(Personalization.this, Constants.HOTEL)) {
            Hotel_delegate.setBackgroundColor(getResources().getColor(R.color.red)).update();
            PreferenceUtils.putBoolean(Personalization.this,Constants.HOTEL_9,true);

        } else {
            Hotel_delegate.setBackgroundColor(getResources().getColor(R.color.yellow)).update();
            PreferenceUtils.putBoolean(Personalization.this,Constants.HOTEL_9,false);

        }
        if (PreferenceUtils.getBoolean(Personalization.this, Constants.PLAY)) {
            Play_delegate.setBackgroundColor(getResources().getColor(R.color.red)).update();
            PreferenceUtils.putBoolean(Personalization.this,Constants.PLAY_10,true);

        } else {
            PreferenceUtils.putBoolean(Personalization.this,Constants.PLAY_10,false);
            Play_delegate.setBackgroundColor(getResources().getColor(R.color.yellow)).update();
        }
        if (PreferenceUtils.getBoolean(Personalization.this, Constants.TRAVEL)) {
            PreferenceUtils.putBoolean(Personalization.this,Constants.TRAVEL_2,true);
            Travel_delegate.setBackgroundColor(getResources().getColor(R.color.red)).update();
        } else {
            PreferenceUtils.putBoolean(Personalization.this,Constants.TRAVEL_2,false);
            Travel_delegate.setBackgroundColor(getResources().getColor(R.color.yellow)).update();
        }

    }

    private void init() {
        mEat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Eat_delegate.setBackgroundColor(
                        Eat_delegate.getBackgroundColor() == getResources().getColor(R.color.yellow)
                                ? getResources().getColor(R.color.red) : getResources().getColor(R.color.yellow))
                        .update();
                boolean aBoolean = PreferenceUtils.getBoolean(Personalization.this, Constants.EAT);
                if (aBoolean) {
                    PreferenceUtils.putBoolean(Personalization.this, Constants.EAT, false);
                    PreferenceUtils.putBoolean(Personalization.this,Constants.EAT_4,false);
                } else {
                    PreferenceUtils.putBoolean(Personalization.this, Constants.EAT, true);
                    PreferenceUtils.putBoolean(Personalization.this,Constants.EAT_4,true);
                }
            }
        });
        mHotel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Hotel_delegate.setBackgroundColor(
                        Hotel_delegate.getBackgroundColor() ==  getResources().getColor(R.color.yellow)
                                ? getResources().getColor(R.color.red) : getResources().getColor(R.color.yellow))
                        .update();
                boolean aBoolean = PreferenceUtils.getBoolean(Personalization.this, Constants.HOTEL);
                if (aBoolean) {
                    PreferenceUtils.putBoolean(Personalization.this, Constants.HOTEL_9, false);
                    PreferenceUtils.putBoolean(Personalization.this, Constants.HOTEL, false);
                } else {
                    PreferenceUtils.putBoolean(Personalization.this, Constants.HOTEL, true);
                    PreferenceUtils.putBoolean(Personalization.this, Constants.HOTEL_9, true);
                }
            }
        });
        mPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Play_delegate.setBackgroundColor(
                        Play_delegate.getBackgroundColor() ==  getResources().getColor(R.color.yellow)
                                ? getResources().getColor(R.color.red) : getResources().getColor(R.color.yellow))
                        .update();
                boolean aBoolean = PreferenceUtils.getBoolean(Personalization.this, Constants.PLAY);
                if (aBoolean) {
                    PreferenceUtils.putBoolean(Personalization.this, Constants.PLAY_10, false);
                    PreferenceUtils.putBoolean(Personalization.this, Constants.PLAY, false);
                } else {
                    PreferenceUtils.putBoolean(Personalization.this, Constants.PLAY_10, true);
                    PreferenceUtils.putBoolean(Personalization.this, Constants.PLAY, true);
                }
            }
        });
        mTravel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Travel_delegate.setBackgroundColor(
                        Travel_delegate.getBackgroundColor() ==  getResources().getColor(R.color.yellow)
                                ? getResources().getColor(R.color.red) : getResources().getColor(R.color.yellow))
                        .update();
                boolean aBoolean = PreferenceUtils.getBoolean(Personalization.this, Constants.TRAVEL);
                if (aBoolean) {
                    PreferenceUtils.putBoolean(Personalization.this, Constants.TRAVEL_2, false);
                    PreferenceUtils.putBoolean(Personalization.this, Constants.TRAVEL, false);
                } else {
                    PreferenceUtils.putBoolean(Personalization.this, Constants.TRAVEL_2, true);
                    PreferenceUtils.putBoolean(Personalization.this, Constants.TRAVEL, true);
                }
            }
        });
    }

}
