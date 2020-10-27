package tcking.github.com.rfm.aacplay;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.github.tcking.viewquery.ViewQuery;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import tcking.github.com.rfm.aacplay.adapter.ArtistDetailAdapter;
import tcking.github.com.rfm.aacplay.adapter.CustomPagerAdapter;
import tcking.github.com.rfm.aacplay.adapter.UpcomingSongsAdapter;
import tcking.github.com.rfm.aacplay.library.radio.RadioListener;
import tcking.github.com.rfm.aacplay.library.radio.RadioManager;
import tcking.github.com.rfm.aacplay.pojos.SongDetail;
import tcking.github.com.rfm.aacplay.pojos.SongDetailPojo;
import tcking.github.com.rfm.aacplay.pojos.SongImage;
import tcking.github.com.rfm.aacplay.pojos.SongImagePojo;
import tcking.github.com.rfm.aacplay.pojos.UpcomingSongs;
import tcking.github.com.rfm.aacplay.pojos.artistdetail.ArtistDetailPojo;
import tcking.github.com.rfm.aacplay.repositories.main.MainRepository;
import tcking.github.com.rfm.aacplay.repositories.secondary.SecondaryRepository;
import tcking.github.com.rfm.aacplay.video.VideoActivity;
import tcking.github.com.giraffeplayer2.EventBusDataSharing;
import tcking.github.com.giraffeplayer2.Option;
import tcking.github.com.giraffeplayer2.PlayerManager;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

/**
 * Created by TangChao on 2017/6/15.
 */

public class MainFragment extends Fragment implements RadioListener {
    private ViewQuery $;
  //  private final String[] RADIO_URL = {"http://5.254.113.34:9123/radiozu.aacp"};

    private final String[] RADIO_URL = {"http://live.romanticfm.ro:9123/rfmmobile.aacp"};

   // ImageView img_view;

    RadioManager mRadioManager;
    private UpcomingSongsAdapter upcomingSongsAdapter;

    FragmentPagerAdapter adapterViewPager;

    private Disposable disposable;
    private MainRepository mainRepository;
    private SecondaryRepository secondaryRepository;
    RecyclerView rv_next_songs;
    RecyclerView rv_latest_videos;
    private ArtistDetailAdapter artistDetailAdapter;


    ViewPager viewPager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //set global configuration: turn on multiple_requests
        PlayerManager.getInstance().getDefaultVideoInfo().addOption(Option.create(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "multiple_requests", 1L));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    public int deviceWidth, deviceHeight;
    LinearLayout rl_audio_video;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        $ = new ViewQuery(view);


     /*   Point size = new Point();
        WindowManager w = getActivity().getWindowManager();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            w.getDefaultDisplay().getSize(size);
            deviceWidth = size.x;
            deviceHeight = size.y;

        } else {
            Display d = w.getDefaultDisplay();
            deviceWidth = d.getWidth();
            deviceHeight = d.getHeight();

        }

        Log.d("deviceWidth","deviceWidth "+deviceWidth+" deviceHeight "+deviceHeight);*/

        mRadioManager = RadioManager.with(getActivity());
        mRadioManager.registerListener(this);

        mRadioManager.setLogging(true);

        String testUrl = "https://play.myovn.com/s1/cache/radiozu/playlist.m3u8";

     /*   rl_audio_video= (LinearLayout) view.findView
     ById(R.id.rl_audio_video);

        if(deviceHeight>1200) {
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) rl_audio_video.getLayoutParams();
            layoutParams.height = (int) getResources().getDimension(R.dimen._250sdp);
            rl_audio_video.setLayoutParams(layoutParams);
        }*/
        rv_next_songs = (RecyclerView) view.findViewById(R.id.rv_next_songs);
        upcomingSongsAdapter = new UpcomingSongsAdapter(getActivity());
        final RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        rv_next_songs.setLayoutManager(mLayoutManager);
        rv_next_songs.setItemAnimator(new DefaultItemAnimator());
        rv_next_songs.setAdapter(upcomingSongsAdapter);


        rv_latest_videos = (RecyclerView) view.findViewById(R.id.rv_latest_videos);
        artistDetailAdapter = new ArtistDetailAdapter(getActivity());
        final RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        rv_latest_videos.setLayoutManager(mLayoutManager1);
        rv_latest_videos.setItemAnimator(new DefaultItemAnimator());
        rv_latest_videos.setAdapter(artistDetailAdapter);

        rv_latest_videos.setHasFixedSize(true);
        rv_latest_videos.setItemViewCacheSize(20);
        rv_latest_videos.setDrawingCacheEnabled(true);
        rv_latest_videos.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        viewPager = (ViewPager)view. findViewById(R.id.viewpager);
       // viewPager.setAdapter(new CustomPagerAdapter(this));
        adapterViewPager = new CustomPagerAdapter(getActivity().getSupportFragmentManager());
        viewPager.setAdapter(adapterViewPager);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Log.d("position","position "+position);
                if (isConnectedToInternet(getActivity())) {
                    Date c = Calendar.getInstance().getTime();
                 //   SimpleDateFormat df = new SimpleDateFormat("yyyy-dd-MM");
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                    String formattedDate = df.format(c);

                    secondaryRepository = MyApplication.getApp().getSecondRepositoryRepository();
                    secondaryRepository.songDetail(formattedDate)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Observer<List<SongDetailPojo>>() {
                                @Override
                                public void onSubscribe(Disposable d) {
                                    disposable = d;
                                }

                                @Override
                                public void onNext(List<SongDetailPojo> value) {
                                    if(value.size()==0){
                                        SongDetail songDetail = new SongDetail();
                                        songDetail.setImage("abc");
                                        EventBus.getDefault().post(songDetail);
                              //          upcomingSongsAdapter.addingImageUrl("");
                                    }else{

                                        SongDetail songDetail = new SongDetail();
                                        songDetail.setImage(value.get(0).getPoza());

                                        songDetail.setNume(value.get(0).getNume());
                                        songDetail.setDescriere(value.get(0).getDescriere());
                                        songDetail.setDay(value.get(0).getDay());
                                        songDetail.setStart(value.get(0).getStart());
                                        songDetail.setEnd(value.get(0).getEnd());


                                        EventBus.getDefault().post(songDetail);
                                 //       upcomingSongsAdapter.addingImageUrl(value.get(0).getPoza());
                                    }
                                }

                                @Override
                                public void onError(Throwable e) {

                                    Log.d("errorrrr","error "+e.getLocalizedMessage());
                                }

                                @Override
                                public void onComplete() {

                                }
                            });
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // viewPager.setAdapter(new CustomPagerAdapter(this));
                if(getActivity()!=null && getActivity().getSupportFragmentManager()!=null) {
                    adapterViewPager = new CustomPagerAdapter(getActivity().getSupportFragmentManager());
                    viewPager.setAdapter(adapterViewPager);
                }
            }
        }, 5000);

        ((MainActivity) getActivity()).setFragmentRefreshListener(new MainActivity.FragmentRefreshListener() {

            @Override
            public void onPlay() {
                Log.d("coco","ccccccccccc");
                        mRadioManager.startRadio(RADIO_URL[0]);
                        mListener.onStateChangeListener("pause");
            }

            @Override
            public void onStop() {
                Log.d("coco","eeeeeeee");
                        mRadioManager.stopRadio();
                        mListener.onStateChangeListener("play");
            }

            @Override
            public void stopPlaying() {
                if (mRadioManager.isPlaying()) {
                    Log.d("coco","fffffffff");
                    mRadioManager.stopRadio();
                }
                mListener.onStateChangeListener("play");
            }

            @Override
            public void hitPlaying() {
                upcomingSongApi();
            }

            @Override
            public void unregister() {
                mRadioManager.unregisterListener(MainFragment.this);
                mRadioManager.disconnect();
                //mRadioManager=null;

            }

        });



    upcomingSongApi();

    }

    private void upcomingSongApi() {
        if (isConnectedToInternet(getActivity())) {
            mainRepository = MyApplication.getApp().getMainRepository();
            mainRepository.upcomingSongs()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<UpcomingSongs>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            disposable = d;
                        }

                        @Override
                        public void onNext(UpcomingSongs value) {
                            //  mainActivityViewInterface.logMessage(TAGMESSAGE,"value "+value.get(0).getName());
                            upcomingSongsAdapter.addingData(value.getList().getSongs());
                            if (isConnectedToInternet(getActivity())) {
                                hitArtistDetailApi();
                            }
                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        }

    }


    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().unregister(this);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        // EventBus.getDefault().unregister(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EventBusDataSharing eventBusDataSharing) {
        Log.d("statuss", "statuss " + eventBusDataSharing.getStatus() + " " + eventBusDataSharing.getCurrentPlayer().getCurrentState());
        if (eventBusDataSharing.getCurrentPlayer().getCurrentState() == 1 ||
                eventBusDataSharing.getCurrentPlayer().getCurrentState() == 2 ||
                eventBusDataSharing.getCurrentPlayer().getCurrentState() == 3) {
            if (eventBusDataSharing.getStatus().equalsIgnoreCase("pause")) {
                Log.d("coco","dddddddd");
                mRadioManager.startRadio(RADIO_URL[0]);
            } else {
                Log.d("coco","ggggggggg");
                mRadioManager.stopRadio();
             /*   NotificationManager notificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
                // Stop the Notification, after logout
                notificationManager.cancel(001);
                notificationManager.cancelAll();*/
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(String notiClicked) {
        Log.d("coco","eeeeeeeee");
        if(notiClicked.equalsIgnoreCase("stop")){
            if (!mRadioManager.isPlaying()) {
                mRadioManager.startRadio(RADIO_URL[0]);
                mListener.onStateChangeListener("pause");
            }

        }else{
            if (!mRadioManager.isPlaying()) {
                mRadioManager.startRadio(RADIO_URL[0]);
                mListener.onStateChangeListener("pause");
            } else {
                Log.d("coco","hhhhhhhh");
                mRadioManager.stopRadio();
                mListener.onStateChangeListener("play");
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(Boolean goToVideoPlayer) {
        Log.d("workkkkk","workkkk");
     /*   String testUrl = "https://play.myovn.com/s1/cache/radiozu/playlist.m3u8";
        Uri uri =  Uri.parse( testUrl );
        GiraffePlayer.play(getContext(), new VideoInfo(uri));*/

        Log.d("coco","iiiiiiiiii");
        mRadioManager.stopRadio();
        mListener.onStateChangeListener("play");
        Intent intent = new Intent(getActivity(), VideoActivity.class);
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        mRadioManager.connect();
        Log.d("onResumeonResume","onResume");
        if(mRadioManager!=null) {
            if (mRadioManager.isPlaying()) {
                mListener.onStateChangeListener("pause");
            } else {
                mListener.onStateChangeListener("play");
            }
        }
    }

    @Override
    public void onRadioLoading() {

    }

    @Override
    public void onRadioConnected() {

    }

    @Override
    public void onRadioStarted() {

    }

    @Override
    public void onRadioStopped() {

    }

    @Override
    public void onMetaDataReceived(final String s, final String s2) {
        if (getActivity() != null) {

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //TODO Do UI works here.

                    if (s2.matches("\\d+(?:\\.\\d+)?")) {
                        //
                    } else {
                        Log.d("songname", "s1 " + s + "\ns2 " + s2);
                        if (s!=null && s.equalsIgnoreCase("StreamTitle")) {
                            mListener.onSongChangeListener(s2);
                            upcomingSongApi();

                            String CurrentString = s2;
                            String[] separated = CurrentString.split(" - ");

                            if(separated.length==1){
                              /*  if(separated[0].trim().equalsIgnoreCase("")){
                                    mRadioManager.updateNotification(separated[0].trim(),
                                            "pub",1,1);
                                }else{
                                    mRadioManager.updateNotification(separated[0].trim(),
                                            separated[0].trim(),1,1);
                                }*/

                                mRadioManager.updateNotification(separated[0].trim(),
                                       "",1,1);
                            }else{
                                mRadioManager.updateNotification(separated[0].trim(),
                                        separated[1].trim(),1,1);
                            }


                            if (isConnectedToInternet(getActivity())) {
                                String artist = "";
                                if(separated[0].trim().equalsIgnoreCase("")){
                                    artist ="pub";
                                }else{
                                    artist=  separated[0].trim();
                                }

                                //aakash change
                                secondaryRepository = MyApplication.getApp().getSecondRepositoryRepository();
                                secondaryRepository.songImage(artist/*"MALUMA"*/)
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(new Observer<List<SongImagePojo>>() {
                                            @Override
                                            public void onSubscribe(Disposable d) {
                                                disposable = d;
                                            }

                                            @Override
                                            public void onNext(List<SongImagePojo> value) {
                                                if(value.size()==0){
                                                    SongImage songImage = new SongImage();
                                                    songImage.setImage("abc");
                                                    EventBus.getDefault().post(songImage);
                                                 //   upcomingSongsAdapter.addingImageUrl("");

                                                    mListener.onImageChanged("");

                                                }else{
                                                    SongImage songImage = new SongImage();
                                                    songImage.setImage(value.get(0).getPoza());
                                                    EventBus.getDefault().post(songImage);

                                                    mListener.onImageChanged(value.get(0).getPoza());
                                                //    upcomingSongsAdapter.addingImageUrl(value.get(0).getPoza());
                                                }

                                            }

                                            @Override
                                            public void onError(Throwable e) {
                                                Log.d("errorrrr","error "+e.getLocalizedMessage());
                                            }

                                            @Override
                                            public void onComplete() {

                                            }
                                        });
                            }
                          /*  if (isConnectedToInternet(getActivity())) {
                                hitArtistDetailApi();
                            }*/
                        }
                    }
                }
            });
        }
    }

    private void hitArtistDetailApi() {
        secondaryRepository = MyApplication.getApp().getSecondRepositoryRepository();
        secondaryRepository.artistDetailApi()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ArtistDetailPojo>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;
                    }

                    @Override
                    public void onNext(ArtistDetailPojo value) {
                        artistDetailAdapter.addingData(value.getData());
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("errorrrr","error "+e.getLocalizedMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void onError() {

    }

    public interface OnStateChangeListener {
        void onStateChangeListener(String text);
        void onSongChangeListener(String text);
        void onImageChanged(String text);
    }

    private OnStateChangeListener mListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof OnStateChangeListener) {
            mListener = (OnStateChangeListener) activity;
        } else {
            throw new IllegalArgumentException("Containing activity must implement OnSearchListener interface");
        }
    }

    public static boolean isConnectedToInternet(Context act) {
        boolean isNetConnected;
        if(act!=null){
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
        }
        else{
            return true;
        }
    }

}
//https://firebase.google.com/docs/cloud-messaging/admin/