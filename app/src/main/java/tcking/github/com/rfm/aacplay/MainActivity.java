package tcking.github.com.rfm.aacplay;

import android.app.Dialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.tcking.viewquery.ViewQuery;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.lb.auto_fit_textview.AutoResizeTextView;
import com.squareup.picasso.Picasso;
import com.suke.widget.SwitchButton;
import com.wefika.flowlayout.FlowLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import io.paperdb.Paper;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import tcking.github.com.rfm.aacplay.adapter.LastSongsAdapter;
import tcking.github.com.rfm.aacplay.helper.ConnectivityReceiver;
import tcking.github.com.rfm.aacplay.pojos.LastSongPojo;
import tcking.github.com.rfm.aacplay.pojos.Notificationspojo;
import tcking.github.com.rfm.aacplay.pojos.PlaySong;
import tcking.github.com.rfm.aacplay.pojos.StopSong;
import tcking.github.com.rfm.aacplay.pojos.StopVideo;
import tcking.github.com.rfm.aacplay.pojos.UpdateNotification;
import tcking.github.com.rfm.aacplay.pojos.UrlString;
import tcking.github.com.rfm.aacplay.pojos.User;
import tcking.github.com.rfm.aacplay.repositories.main.MainRepository;
import tcking.github.com.giraffeplayer2.GiraffePlayer;
import tcking.github.com.giraffeplayer2.PlayerManager;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by TangChao on 2017/6/15.
 */

public class MainActivity extends AppCompatActivity implements View.OnClickListener, MainFragment.OnStateChangeListener,
        ConnectivityReceiver.ConnectivityReceiverListener,
        SetTimerFragment.OnTimeSetChangeListener {
    private ViewQuery $;
    // FrameLayout fl_main;

    LinearLayout left_drawer, bottom_sheet;
    ImageView open_drawer/*,img_play_pause*/, img_no_net, img_play, img_pause, img_splash, img_back;
    DrawerLayout drawerLayout;
    RelativeLayout rl_live_video, rl_news, rl_settings, rl_bottom_layout, layout_notifications;
    TextView txt_header, txt_no_song_yet;
    AutoResizeTextView txt_song_name, txt_title, txt_song_title_name;
    LinearLayout ll_main;
    Boolean retartApp = false;

    // BottomSheetLayout layout;

    BottomSheetBehavior sheetBehavior;

    FrameLayout fl_main, fl_news, fl_set_time, fl_notifications;
    RecyclerView rv_last_songs;

    private static final String TAG = MainActivity.class.getSimpleName();
    LastSongsAdapter lastSongsAdapter;

    //List<String> songArrayList=new ArrayList<>();

    int bottomSheetState = 4;

    int STATE_HIDDEN = 5;
    int STATE_EXPANDED = 3;
    int STATE_COLLAPSED = 4;
    int STATE_DRAGGING = 1;
    int STATE_SETTLING = 2;

    private Disposable disposable;
    private MainRepository mainRepository;

    private DatabaseReference mDatabase;

    SharedPreferences pref;
    SharedPreferences.Editor editor;

    SwitchButton switch_button;

    ImageView img_fb, img_yt, img_ig, img_artist;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = pref.edit();

        String token = FirebaseInstanceId.getInstance().getToken();

        mDatabase = FirebaseDatabase.getInstance().getReference();

        String android_id = Settings.Secure.getString(getContentResolver(),
                Settings.Secure.ANDROID_ID);

        if (token != null) {

            String reqString = Build.MANUFACTURER
                    + " " + Build.MODEL + " " + Build.VERSION.RELEASE
                    + " " + Build.VERSION_CODES.class.getFields()[android.os.Build.VERSION.SDK_INT].getName();

            User user = new User();
            user.setToken(token);
            user.setDevice_name(reqString);
            user.setDevice_type("Android");


            mDatabase.child(android_id).child("token").setValue(user);

        }

        // FirebaseDatabase.getInstance().getReference().child("users/${user.uid}/tokens").child(token).setValue(true);

      /*  SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        Log.d("fcm_msgs", pref.getString("fcm_msgs", ""));*/

     /*   ArrayList<UrlString> urlStrings = new ArrayList<>();

        UrlString urlString = new UrlString();
        urlString.setText("tset");
        urlString.setUrl(false);
        urlStrings.add(urlString);

        alertPopUp(MainActivity.this,urlStrings);*/




        switch_button = (SwitchButton) findViewById(R.id.switch_button);


        if (pref.getBoolean("isNotificationOn", true)) {
            FirebaseMessaging.getInstance().subscribeToTopic("on");
            switch_button.setChecked(true);
        } else {
            FirebaseMessaging.getInstance().unsubscribeFromTopic("on");
            switch_button.setChecked(false);
        }


        switch_button.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                if (isChecked) {
                    editor.putBoolean("isNotificationOn", true);
                    editor.commit();
                    FirebaseMessaging.getInstance().subscribeToTopic("on");
                } else {
                    editor.putBoolean("isNotificationOn", false);
                    editor.commit();
                    FirebaseMessaging.getInstance().unsubscribeFromTopic("on");
                }
            }
        });


        GiraffePlayer.debug = true;//show java logs
        GiraffePlayer.nativeDebug = false;//not show native logs

        getSupportFragmentManager().beginTransaction().replace(R.id.fl_main, new MainFragment()).commit();

        getSupportFragmentManager().beginTransaction().replace(R.id.fl_news, new NewsFragment()).commit();

        getSupportFragmentManager().beginTransaction().replace(R.id.fl_set_time, new SetTimerFragment()).commit();

        getSupportFragmentManager().beginTransaction().replace(R.id.fl_notifications, new NotificationFragment()).commit();


        left_drawer = (LinearLayout) findViewById(R.id.left_drawer);
        open_drawer = (ImageView) findViewById(R.id.open_drawer);
        open_drawer.setOnClickListener(this);


        img_back = (ImageView) findViewById(R.id.img_back);
        img_back.setOnClickListener(this);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);

        fl_main = (FrameLayout) findViewById(R.id.fl_main);

        fl_news = (FrameLayout) findViewById(R.id.fl_news);
        fl_set_time = (FrameLayout) findViewById(R.id.fl_set_time);
        fl_notifications = (FrameLayout) findViewById(R.id.fl_notifications);
        ll_main = (LinearLayout) findViewById(R.id.ll_main);

        rl_live_video = (RelativeLayout) findViewById(R.id.rl_live_video);
        rl_news = (RelativeLayout) findViewById(R.id.rl_news);
        rl_settings = (RelativeLayout) findViewById(R.id.rl_settings);
        rl_live_video.setOnClickListener(this);
        rl_news.setOnClickListener(this);
        rl_settings.setOnClickListener(this);

        layout_notifications = (RelativeLayout) findViewById(R.id.layout_notifications);
        layout_notifications.setOnClickListener(this);

        rl_bottom_layout = (RelativeLayout) findViewById(R.id.rl_bottom_layout);
        rl_bottom_layout.setOnClickListener(this);
        //   layout = (BottomSheetLayout) findViewById(R.id.bottom_sheet_layout);

        bottom_sheet = (LinearLayout) findViewById(R.id.bottom_sheet);
        sheetBehavior = BottomSheetBehavior.from(bottom_sheet);

        sheetBehavior.setHideable(false);

        rv_last_songs = (RecyclerView) findViewById(R.id.rv_last_songs);

        lastSongsAdapter = new LastSongsAdapter(this);
        final RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        rv_last_songs.setLayoutManager(mLayoutManager);
        rv_last_songs.setItemAnimator(new DefaultItemAnimator());
        rv_last_songs.setAdapter(lastSongsAdapter);


        img_fb = (ImageView) findViewById(R.id.img_fb);
        img_fb.setOnClickListener(this);
        img_yt = (ImageView) findViewById(R.id.img_yt);
        img_yt.setOnClickListener(this);
        img_ig = (ImageView) findViewById(R.id.img_ig);
        img_ig.setOnClickListener(this);

        img_artist = (ImageView) findViewById(R.id.img_artist);

      /*  img_play_pause= (ImageView) findViewById(R.id.img_play_pause);
        img_play_pause.setOnClickListener(this);*/

        img_play = (ImageView) findViewById(R.id.img_play);
        img_play.setOnClickListener(this);
        img_pause = (ImageView) findViewById(R.id.img_pause);
        img_pause.setOnClickListener(this);

        img_splash = (ImageView) findViewById(R.id.img_splash);
        img_no_net = (ImageView) findViewById(R.id.img_no_net);
        txt_song_name = (AutoResizeTextView) findViewById(R.id.txt_song_name);
        txt_title = (AutoResizeTextView) findViewById(R.id.txt_title);
        txt_header = (TextView) findViewById(R.id.txt_header);

        txt_song_title_name = (AutoResizeTextView) findViewById(R.id.txt_song_title_name);

        txt_no_song_yet = (TextView) findViewById(R.id.txt_no_song_yet);

        txt_title.setSelected(true);
        txt_song_name.setSelected(true);
        txt_song_title_name.setSelected(true);



        if (isConnectedToInternet(MainActivity.this)) {
         retartApp = false;
        } else {
            retartApp = true;
        }


        // getSupportFragmentManager().beginTransaction().add(android.R.id.content, new MainFragment()).commit();

        //  fl_main = (FrameLayout) findViewById(R.id.fl_main);


        //aakash
       /* if(songArrayList.size()==0){
            txt_no_song_yet.setVisibility(View.VISIBLE);
            rv_last_songs.setVisibility(View.GONE);
        }else{
            txt_no_song_yet.setVisibility(View.GONE);
            rv_last_songs.setVisibility(View.VISIBLE);
        }
        lastSongsAdapter.addingData(songArrayList);*/

        if (isConnectedToInternet(MainActivity.this)) {

            hitLastSongsApi();

        }


        Log.i(TAG, "onCreate()");
        Intent serviceIntent = new Intent(this, MyService.class);
        startService(serviceIntent);

        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);


        fl_main.setVisibility(View.GONE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {


                Log.d("coco", "aaaaaaaa");

                if (getFragmentRefreshListener() != null) {
                    getFragmentRefreshListener().onPlay();
                } else {
                    EventBus.getDefault().post("stop");
                }

            }
        }, 2100);


        new Handler().postDelayed(new Runnable() {
                                      @Override
                                      public void run() {

                                          Log.d("coco", "bbbbbb");


                                          if (getFragmentRefreshListener() != null) {
                                             // getFragmentRefreshListener().onStop();

                                              getFragmentRefreshListener().onPlay();

                                          } else {
                                              EventBus.getDefault().post("stop");
                                          }


                                      }
                                  }, 4000
        );

        new Handler().postDelayed(new Runnable() {
                                      @Override
                                      public void run() {


                                          ArrayList<Notificationspojo> stringArrayList = Paper.book("fcm").read("msgs", new ArrayList<Notificationspojo>());


                                          if (stringArrayList.size() > 0) {


                                              String[] parts = (stringArrayList.get(0).getMessage()).split("\\s");
                                              ArrayList<UrlString> urlStrings = new ArrayList<>();

                                              for (int i = 0; i < parts.length; i++) {
                                                  if (Patterns.WEB_URL.matcher(parts[i]).matches()
                                                          ) {

                                                      UrlString urlString = new UrlString();
                                                      urlString.setText(parts[i]);
                                                      urlString.setUrl(true);
                                                      urlStrings.add(urlString);


                                                  }else{
                                                      UrlString urlString = new UrlString();
                                                      urlString.setText(parts[i]);
                                                      urlString.setUrl(false);
                                                      urlStrings.add(urlString);
                                                  }

                                              }

                                            alertPopUp(MainActivity.this,urlStrings);





                                              Paper.book("fcm").write("msgs", new ArrayList<Notificationspojo>());
                                          }


                                          fl_main.setVisibility(View.VISIBLE);
                                          img_splash.setVisibility(View.GONE);

                                          AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                                          audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 7, 0);
                                      }
                                  }, 5000
        );

 /*       if(SharedPrefUtil.isVideoPlaying(getApplicationContext())==99){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    if(getFragmentRefreshListener()!=null){
                        getFragmentRefreshListener().onPlay();
                    }

                }
            }, 2000);
        }
*/


        /**
         * bottom sheet state change listener
         * we are changing button text when sheet changed state
         * */
        sheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_HIDDEN:
                        bottomSheetState = newState;

                        break;
                    case BottomSheetBehavior.STATE_EXPANDED: {
                        bottomSheetState = newState;

                    }
                    break;
                    case BottomSheetBehavior.STATE_COLLAPSED: {
                        bottomSheetState = newState;

                    }
                    break;
                    case BottomSheetBehavior.STATE_DRAGGING:

                        bottomSheetState = newState;
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        bottomSheetState = newState;
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });


    }


  /*  @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission Granted
            } else {
                Toast.makeText(this, "please grant read permission", Toast.LENGTH_SHORT).show();
            }
        }
    }*/

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        PlayerManager.getInstance().onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed() {

        if (txt_header.getText().toString().equalsIgnoreCase("Știri")
                || txt_header.getText().toString().equalsIgnoreCase("Oprire Programată")

            /* &&
                !drawerLayout.isDrawerOpen(left_drawer)*/) {


            if (drawerLayout.isDrawerOpen(left_drawer)) {
                //   drawerLayout.closeDrawer(left_drawer);
                drawerLayout.closeDrawers();
            } else {
                drawerLayout.closeDrawers();
                txt_header.setText("Ascultă Romantic FM");
                fl_main.setVisibility(View.VISIBLE);
                fl_news.setVisibility(View.GONE);
                fl_set_time.setVisibility(View.GONE);
                fl_notifications.setVisibility(View.GONE);


                img_back.setVisibility(View.GONE);
                open_drawer.setVisibility(View.VISIBLE);
            }


        } else {


            if (drawerLayout.isDrawerOpen(left_drawer)) {
                //   drawerLayout.closeDrawer(left_drawer);
                drawerLayout.closeDrawers();
            } else {
                moveTaskToBack(true);
            }

        }
    }

    @Override
    protected void onDestroy() {

        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        // Stop the Notification, after logout
        notificationManager.cancel(001);

        notificationManager.cancelAll();

        EventBus.getDefault().unregister(this);
        super.onDestroy();
        Log.i(TAG, "onDestroy()");

    }


    @Override
    protected void onResume() {
        super.onResume();
        MyApplication.getInstance().setConnectivityListener(this);
        MyApplication.activityResumed();


        if (isConnectedToInternet(MainActivity.this)) {
            ll_main.setVisibility(View.VISIBLE);
            img_no_net.setVisibility(View.GONE);
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            getFragmentRefreshListener().hitPlaying();
        } else {

            drawerLayout.closeDrawers();
                    img_no_net.setVisibility(View.VISIBLE);
                    ll_main.setVisibility(View.GONE);
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        MyApplication.activityPaused();
    }


    public static String FACEBOOK_URL = "https://www.facebook.com/RomanticFM";
    public static String FACEBOOK_PAGE_ID = "RomanticFM";

    public String getFacebookPageURL(Context context) {
        PackageManager packageManager = context.getPackageManager();
        try {
            int versionCode = packageManager.getPackageInfo("com.facebook.katana", 0).versionCode;
            if (versionCode >= 3002850) { //newer versions of fb app
                return "fb://facewebmodal/f?href=" + FACEBOOK_URL;
            } else { //older versions of fb app
                return "fb://page/" + FACEBOOK_PAGE_ID;
            }
        } catch (PackageManager.NameNotFoundException e) {
            return FACEBOOK_URL; //normal web url
        }
    }
    @Override
    public void onClick(View v) {

        switch (v.getId()) {


            case R.id.img_fb:
/*
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://www.facebook.com/radiozu/"));
                startActivity(intent);*/

                Intent facebookIntent = new Intent(Intent.ACTION_VIEW);
                String facebookUrl = getFacebookPageURL(this);
                facebookIntent.setData(Uri.parse(facebookUrl));
                if (facebookIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(facebookIntent);
                }




                break;

//            case R.id.img_yt:
//
//                Intent intent2 = new Intent(Intent.ACTION_VIEW);
//                intent2.setData(Uri.parse("https://www.youtube.com/user/videozufm"));
//                startActivity(intent2);
//
//                break;

            case R.id.img_ig:

                Intent intent3 = new Intent(Intent.ACTION_VIEW);
                intent3.setData(Uri.parse("https://www.instagram.com/romanticfm/"));
                startActivity(intent3);

                break;

            case R.id.open_drawer:

                //   Crashlytics.getInstance().crash();

                if (drawerLayout.isDrawerOpen(left_drawer)) {
                    //   drawerLayout.closeDrawer(left_drawer);

                } else {
                    drawerLayout.openDrawer(left_drawer);
                }


                break;

          /*  case R.id.img_play_pause:

                if(getFragmentRefreshListener()!=null){
                    getFragmentRefreshListener().onRefresh();
                }

                break;*/

            case R.id.img_play:


                if (getFragmentRefreshListener() != null) {
                    getFragmentRefreshListener().onPlay();
                } else {
                    EventBus.getDefault().post("notiClicked");
                }

                /*   expand(fl_main);*/

                break;

            case R.id.img_pause:

                if (getFragmentRefreshListener() != null) {
                    getFragmentRefreshListener().onStop();
                } else {
                    EventBus.getDefault().post("notiClicked");
                }

                break;

            case R.id.rl_bottom_layout:

                //  layout.toggle();

                if (sheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                    sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

                } else {
                    sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

                }

                break;


            case R.id.rl_live_video:
                drawerLayout.closeDrawers();
                txt_header.setText("Ascultă Romantic FM");
                fl_main.setVisibility(View.VISIBLE);

                fl_news.setVisibility(View.GONE);

                fl_set_time.setVisibility(View.GONE);

                break;


            case R.id.rl_news:


                drawerLayout.closeDrawers();
                if (getFragmentRefreshListener2() != null) {
                    getFragmentRefreshListener2().update();
                }

                txt_header.setText("Știri");
                fl_news.setVisibility(View.VISIBLE);
                fl_main.setVisibility(View.GONE);

                fl_set_time.setVisibility(View.GONE);
                fl_notifications.setVisibility(View.GONE);

                break;

            case R.id.rl_settings:

                drawerLayout.closeDrawers();
                txt_header.setText("Oprire Programată");
                fl_main.setVisibility(View.GONE);

                fl_news.setVisibility(View.GONE);
                fl_set_time.setVisibility(View.VISIBLE);
                fl_notifications.setVisibility(View.GONE);

                img_back.setVisibility(View.VISIBLE);
                open_drawer.setVisibility(View.GONE);
                break;

            case R.id.layout_notifications:

              /*  getFragmentRefreshListener1().update();


                fl_main.setVisibility(View.GONE);

                fl_news.setVisibility(View.GONE);
                fl_set_time.setVisibility(View.GONE);
                fl_notifications.setVisibility(View.VISIBLE);

                img_back.setVisibility(View.VISIBLE);
                open_drawer.setVisibility(View.GONE);


                drawerLayout.closeDrawers();*/
                break;


            case R.id.img_back:

                txt_header.setText("Ascultă Romantic FM");
                fl_main.setVisibility(View.VISIBLE);
                fl_news.setVisibility(View.GONE);
                fl_set_time.setVisibility(View.GONE);
                img_back.setVisibility(View.GONE);
                fl_notifications.setVisibility(View.GONE);
                open_drawer.setVisibility(View.VISIBLE);


                break;


        }

    }

    @Override
    public void onStateChangeListener(final String text) {

        if (text.equalsIgnoreCase("pause")) {
            //  img_play_pause.setImageDrawable(getResources().getDrawable(R.drawable.stop_icon));


            img_pause.setVisibility(View.VISIBLE);
            img_play.setVisibility(View.GONE);
        } else {
            //  img_play_pause.setImageDrawable(getResources().getDrawable(R.drawable.play_icon));

            img_pause.setVisibility(View.GONE);
            img_play.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onSongChangeListener(final String text) {

        long delayTime;
        if (bottomSheetState == STATE_DRAGGING || bottomSheetState == STATE_SETTLING) {
            sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            delayTime = 500;
        } else {
            delayTime = 100;
        }


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {


                final String CurrentString = text;
                String[] separated = CurrentString.split(" - ");


                if (separated.length == 1) {
                    txt_song_title_name.setText(text);
                    txt_song_title_name.setVisibility(View.VISIBLE);
                    txt_title.setVisibility(View.GONE);
                    txt_song_name.setVisibility(View.GONE);


                } else {

                    txt_title.setText(separated[0].trim());
                    txt_song_name.setText(separated[1].trim());

                    txt_song_title_name.setVisibility(View.GONE);
                    txt_title.setVisibility(View.VISIBLE);
                    txt_song_name.setVisibility(View.VISIBLE);

                }


                if (isConnectedToInternet(MainActivity.this)) {

                    hitLastSongsApi();

                }

            }


        }, delayTime);

    }

    @Override
    public void onImageChanged(String text) {

        //aakash
        if (text.equalsIgnoreCase("")) {
            img_artist.setVisibility(View.GONE);
        } else {
            img_artist.setVisibility(View.VISIBLE);

            Picasso.with(MainActivity.this)
                    .load(text)
                    //    .transform(new BlurTransformation(getActivity(), 25, 1))
                    // .error(R.mipmap.ic_launcher)

                    .into(img_artist);
        }

    }

    private void hitLastSongsApi() {


     /*
        lastSongsAdapter.addingData(songArrayList);*/


        mainRepository = MyApplication.getApp().getMainRepository();
        mainRepository.lastSongs()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<LastSongPojo>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;

                    }

                    @Override
                    public void onNext(LastSongPojo value) {

                        //  mainActivityViewInterface.logMessage(TAGMESSAGE,"value "+value.get(0).getName());

                        if (value.getPiese().size() == 0) {
                            txt_no_song_yet.setVisibility(View.VISIBLE);
                            rv_last_songs.setVisibility(View.GONE);
                        } else {
                            txt_no_song_yet.setVisibility(View.GONE);
                            rv_last_songs.setVisibility(View.VISIBLE);
                        }

                        lastSongsAdapter.addingData(value.getPiese());

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });


    }


    public Handler myHandler = new Handler();
    Runnable myRunnable;

    int hour = 0;
    int minutue = 0;

    long delayTime = 0;

    @Override
    public void onTimeSetChangeListener(String hours, String minutes) {

        Log.d("timee", "hours " + hours + " minutes " + minutes);


        hour = Integer.parseInt(hours);
        minutue = Integer.parseInt(minutes);
        if (minutue > 0) {
            delayTime = minutue * 60 * 1000;
        }
        if (hour > 0) {
            delayTime = delayTime + hour * 60 * 60 * 1000;
        }

        if (hour == 0 && minutue != 0) {
            Toast.makeText(getApplicationContext(),
                    "Sleep time of " + minutue + " minute has been set.", Toast.LENGTH_LONG).show();
        } else if (hour != 0 && minutue == 0) {
            Toast.makeText(getApplicationContext(),
                    "Sleep time of " + hour + " hour has been set.", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(),
                    "Sleep time of " + hour + " hour " + minutue + " minute has been set.", Toast.LENGTH_LONG).show();
        }


        img_back.setVisibility(View.GONE);
        fl_notifications.setVisibility(View.GONE);


        txt_header.setText("AAscultă Romantic FM");
        fl_main.setVisibility(View.VISIBLE);

        fl_news.setVisibility(View.GONE);
        fl_set_time.setVisibility(View.GONE);


        open_drawer.setVisibility(View.VISIBLE);


        if (myRunnable != null) {
            myHandler.removeCallbacks(myRunnable);
        }

        myRunnable = new Runnable() {
            @Override
            public void run() {
                // your code here
                Log.d("timerr", "it worrked 1");

                if (getFragmentRefreshListener() != null) {
                    Log.d("timerr", "it worrked 2");
                    getFragmentRefreshListener().stopPlaying();


                    EventBus.getDefault().post(new StopVideo());


              /*      NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
                    notificationManager.cancel(001);
                    notificationManager.cancelAll();*/
/*
                    int id= android.os.Process.myPid();
                    android.os.Process.killProcess(id);
                    Intent intent = new Intent(Intent.ACTION_MAIN);
                    intent.addCategory(Intent.CATEGORY_HOME);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);

                    finish();
                    System.exit(0);*/


                } else {
                    Log.d("timerr", "it didnt worrked 2");
                }


            }
        };

        myHandler.postDelayed(myRunnable, delayTime);


    }


    public interface FragmentRefreshListener {
        // void onRefresh();
        void onPlay();

        void onStop();

        void stopPlaying();

        void hitPlaying();
        void unregister();

    }


    public FragmentRefreshListener getFragmentRefreshListener() {
        return fragmentRefreshListener;
    }

    public void setFragmentRefreshListener(FragmentRefreshListener fragmentRefreshListener) {
        this.fragmentRefreshListener = fragmentRefreshListener;
    }

    private FragmentRefreshListener fragmentRefreshListener;


    public interface FragmentRefreshListener1 {


        void update();
    }


    public FragmentRefreshListener1 getFragmentRefreshListener1() {
        return fragmentRefreshListener1;
    }

    public void setFragmentRefreshListener1(FragmentRefreshListener1 fragmentRefreshListener1) {
        this.fragmentRefreshListener1 = fragmentRefreshListener1;
    }

    private FragmentRefreshListener1 fragmentRefreshListener1;


    public interface FragmentRefreshListener2 {

        void update();
    }


    public FragmentRefreshListener2 getFragmentRefreshListener2() {
        return fragmentRefreshListener2;
    }

    public void setFragmentRefreshListener2(FragmentRefreshListener2 fragmentRefreshListener2) {
        this.fragmentRefreshListener2 = fragmentRefreshListener2;
    }

    private FragmentRefreshListener2 fragmentRefreshListener2;


    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {

        if (isConnected) {


            if(retartApp) {
               // retartApp = false;
                getFragmentRefreshListener().unregister();
                Intent intent = new Intent(getApplication(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                finish();
                startActivity(intent);

               //
            }else{
                ll_main.setVisibility(View.VISIBLE);
                img_no_net.setVisibility(View.GONE);
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                getFragmentRefreshListener().hitPlaying();
            }


         /*   ll_main.setVisibility(View.VISIBLE);
            img_no_net.setVisibility(View.GONE);
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            getFragmentRefreshListener().hitPlaying();

            getFragmentRefreshListener().unregister();
            getSupportFragmentManager().beginTransaction().replace(R.id.fl_main, new MainFragment()).commit();*/

        } else {

            drawerLayout.closeDrawers();

                    img_no_net.setVisibility(View.VISIBLE);
                    ll_main.setVisibility(View.GONE);

            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }
        Log.d("isConnected", "isConnected " + isConnected);

    }


    public static boolean isConnectedToInternet(Context act) {
        boolean isNetConnected;
        if (act != null) {
            try {
                ConnectivityManager ConMgr = (ConnectivityManager) act
                        .getSystemService(Context.CONNECTIVITY_SERVICE);
                isNetConnected = ConMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED
                        || ConMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
                        .getState() == NetworkInfo.State.CONNECTED;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            return isNetConnected;
        } else {
            return true;
        }
    }


    public static void expand(final View v) {
        v.measure(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        final int targetHeight = v.getMeasuredHeight();

        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        v.getLayoutParams().height = 1;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = interpolatedTime == 1
                        ? LinearLayout.LayoutParams.WRAP_CONTENT
                        : (int) (targetHeight * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration(3000/*(int)(targetHeight / v.getContext().getResources().getDisplayMetrics().density)*/);
        v.startAnimation(a);
    }

    public static void collapse(final View v) {
        final int initialHeight = v.getMeasuredHeight();

        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if (interpolatedTime == 1) {
                    v.setVisibility(View.GONE);
                } else {
                    v.getLayoutParams().height = initialHeight - (int) (initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration((int) (initialHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }


    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().unregister(this);
        EventBus.getDefault().register(this);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(UpdateNotification updateNotification) {

        ArrayList<Notificationspojo> stringArrayList = Paper.book("fcm").read("msgs", new ArrayList<Notificationspojo>());


        if (stringArrayList.size() > 0) {


            String[] parts = (stringArrayList.get(0).getMessage()).split("\\s");
            ArrayList<UrlString> urlStrings = new ArrayList<>();

            for (int i = 0; i < parts.length; i++) {
                if (Patterns.WEB_URL.matcher(parts[i]).matches()
                        ) {

                    UrlString urlString = new UrlString();
                    urlString.setText(parts[i]);
                    urlString.setUrl(true);
                    urlStrings.add(urlString);





                }else{
                    UrlString urlString = new UrlString();
                    urlString.setText(parts[i]);
                    urlString.setUrl(false);
                    urlStrings.add(urlString);
                }

            }

            alertPopUp(MainActivity.this,urlStrings);





            Paper.book("fcm").write("msgs", new ArrayList<Notificationspojo>());
        }


    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(PlaySong playSong) {
        Log.d("aaaaaaaa", "nnnnnnn");

        if (getFragmentRefreshListener() != null) {
            getFragmentRefreshListener().onPlay();
        } else {
            EventBus.getDefault().post("notiClicked");
        }

        new Handler().postDelayed(new Runnable() {
                                      @Override
                                      public void run() {

                                          Log.d("coco", "bbbbbb");


                                          img_pause.setVisibility(View.VISIBLE);
                                          img_play.setVisibility(View.GONE);


                                      }
                                  }, 500
        );


    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(StopSong stopSong) {
        Log.d("aaaaaaaa", "mmmmmmmm");

        if (getFragmentRefreshListener() != null) {
            getFragmentRefreshListener().onStop();
        } else {
            EventBus.getDefault().post("notiClicked");
        }

    }


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


    Dialog dialog;

    public void alertPopUp(final Context activity, ArrayList<UrlString> urlStrings) {
        try {
            hideDialog();
            dialog = new Dialog(activity,
                    R.style.Theme_AppCompat_Translucent);

            // Configure dialog box
            dialog.setContentView(R.layout.custom_dialog_neutral_layout);
            WindowManager.LayoutParams layoutParams = dialog.getWindow()
                    .getAttributes();
            layoutParams.dimAmount = 0.6f;
            dialog.getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);

            // set the message
           /* TextView dialogMessage = (TextView) dialog.findViewById(R.id.dialog_message);
            dialogMessage.setText(message);*/

            FlowLayout fl_text = (FlowLayout) dialog.findViewById(R.id.fl_text);

            for(int i = 0; i<urlStrings.size();i++){

                if(urlStrings.get(i).isUrl()){

                    FlowLayout.LayoutParams lparams = new FlowLayout.LayoutParams(
                            FlowLayout.LayoutParams.WRAP_CONTENT, FlowLayout.LayoutParams.WRAP_CONTENT);

                    TextView tv = new TextView(MainActivity.this);
                    tv.setLayoutParams(lparams);
                    tv.setText(urlStrings.get(i).getText().replaceAll("http://https://","http://")+" ");
                    tv.setTextColor(getResources().getColor(R.color.blue));
                    fl_text.addView(tv);

                    tv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (((TextView)v).getText().toString().contains("http://")
                                    || ((TextView)v).getText().toString().contains("https://")) {


                                Intent intent1 = new Intent(MainActivity.this, WebViewActivity.class);
                                intent1.putExtra("url", ((TextView)v).getText().toString().replaceAll("https://","http://"));
                                startActivity(intent1);
                                dialog.dismiss();

                            } else {

                                Intent intent1 = new Intent(MainActivity.this, WebViewActivity.class);
                                intent1.putExtra("url", "http://" +((TextView)v).getText().toString());
                                startActivity(intent1);
                                dialog.dismiss();

                            }
                        }
                    });


                }else{
                    FlowLayout.LayoutParams lparams = new FlowLayout.LayoutParams(
                            FlowLayout.LayoutParams.WRAP_CONTENT, FlowLayout.LayoutParams.WRAP_CONTENT);

                    TextView tv = new TextView(MainActivity.this);
                    tv.setLayoutParams(lparams);
                    tv.setText(urlStrings.get(i).getText()+" ");
                    tv.setTextColor(getResources().getColor(R.color.black));
                    fl_text.addView(tv);

                }



            }


            TextView actionNeutral = (TextView) dialog.findViewById(R.id.action_neutral);
          //actionNeutral.setText(R.string.ok);
            actionNeutral.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            try {
                dialog.show();
            } catch (Exception e) {
                Log.v("CPS_Dialog_crash", e.getMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // hide the progress dialog
    public void hideDialog() {
        try {
            if (dialog != null) {
                if (dialog.isShowing()) {
                    dialog.dismiss();

                }
                dialog = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}


