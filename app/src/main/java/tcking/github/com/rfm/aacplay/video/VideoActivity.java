package tcking.github.com.rfm.aacplay.video;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.github.tcking.viewquery.ViewQuery;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import tcking.github.com.rfm.aacplay.pojos.Finnish;
import tcking.github.com.rfm.aacplay.pojos.PlaySong;
import tcking.github.com.rfm.aacplay.pojos.StopSong;
import tcking.github.com.rfm.aacplay.pojos.StopVideo;
import tcking.github.com.giraffeplayer2.GiraffePlayer;
import tcking.github.com.giraffeplayer2.PlayerManager;

public class VideoActivity extends AppCompatActivity /*implements OrientationManager.OrientationChangeListener*/ {

    private ViewQuery $;
   // private OrientationManager orientationManager;
    private int orientation;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        GiraffePlayer.debug = true;//show java logs
        GiraffePlayer.nativeDebug = false;//not show native logs

       /* orientationManager = OrientationManager.getInstance(this);
        orientationManager.setOrientationChangedListener(this);
        orientationManager.enable();*/

        getSupportFragmentManager().beginTransaction().add(android.R.id.content, new VideoFragment()).commit();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission Granted
            } else {
                Toast.makeText(this, "please grant read permission", Toast.LENGTH_SHORT).show();
            }
        }
    }

/*    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        PlayerManager.getInstance().onConfigurationChanged(newConfig);
    }*/

    @Override
    public void onBackPressed() {
        if (PlayerManager.getInstance().onBackPressed()) {
            return;
        }
        super.onBackPressed();
    }



    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().unregister(this);
        EventBus.getDefault().register(this);
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(Finnish finnish) {
        Log.d("aaaaaaaa","bbbbbbbb");
        finish();

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(StopVideo stopVideo) {
        Log.d("aaaaaaaa","ccccccccc");

        finish();

    }

    @Override
    protected void onPause() {
        super.onPause();
        if(PlayerManager.getInstance().getCurrentPlayer().getCurrentState() ==1 ||
        PlayerManager.getInstance().getCurrentPlayer().getCurrentState() ==2 ||
        PlayerManager.getInstance().getCurrentPlayer().getCurrentState() ==3
                ){
            PlaySong playSong =new  PlaySong();
            EventBus.getDefault().post(playSong);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        StopSong stopSong= new StopSong();
        EventBus.getDefault().post(stopSong);


    }

    /* @Override
    public void onOrientationChanged(int newOrientation) {
        orientation = newOrientation;
       *//* if (newOrientation == Configuration.ORIENTATION_LANDSCAPE){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
            PlayerManager.getInstance().getCurrentPlayer().toggleLandscape();
        }else{
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            PlayerManager.getInstance().getCurrentPlayer().togglePotrait();
        }*//*


        if(newOrientation<45 && newOrientation >315){
            orientation=1;
        }
        else if(newOrientation>45 && newOrientation<135){
            orientation=2;
        }
        else if(newOrientation>135 && newOrientation<225){
            orientation=3;
        }
        else if(newOrientation>225 && newOrientation<315){
            orientation=4;
        }


        Log.d("aaaaaaaa","dddddddd "+orientation);


    }*/
}
