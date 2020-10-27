package tcking.github.com.rfm.aacplay;

/**
 * Created by Admin on 4/5/2018.
 */

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.util.Log;


import io.paperdb.Paper;
import tcking.github.com.rfm.aacplay.globals.GlobalConstants;
import tcking.github.com.rfm.aacplay.helper.ConnectivityReceiver;
import tcking.github.com.rfm.aacplay.repositories.main.MainRepository;
import tcking.github.com.rfm.aacplay.repositories.main.MainServiceGenerator;
import tcking.github.com.rfm.aacplay.repositories.secondary.SecondaryRepository;
import tcking.github.com.rfm.aacplay.repositories.secondary.SecondaryServiceGenerator;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class MyApplication extends Application {

    private static final String TAG = MyApplication.class.getSimpleName();

    private static MyApplication app;

    private MainRepository mainRepository;

    private SecondaryRepository secondaryRepository;

    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;
        app = this;
        Paper.init(getApplicationContext());

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/OpenSans-Regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );

        mainRepository = MainRepository.getInstance(MainServiceGenerator.mainServices(GlobalConstants.BASE_URL));
        secondaryRepository = SecondaryRepository.getInstance(SecondaryServiceGenerator.mainServices(GlobalConstants.BASE_URL_TWO));
        Log.i(TAG, "onCreate()");
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Log.i(TAG, "onLowMemory()");
    }



    public static boolean isActivityVisible() {
        return activityVisible;
    }

    public static void activityResumed() {
        activityVisible = true;
    }

    public static void activityPaused() {
        activityVisible = false;
    }

    private static boolean activityVisible;


    public static MyApplication getApp() {
        return app;
    }

    public MainRepository getMainRepository() {
        return mainRepository;
    }
    public SecondaryRepository getSecondRepositoryRepository() {
        return secondaryRepository;
    }


    private static MyApplication mInstance;

    public static synchronized MyApplication getInstance() {
        return mInstance;
    }

    public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
        ConnectivityReceiver.connectivityReceiverListener = listener;
    }


    @Override
    protected void attachBaseContext(Context newBase) {

        super.attachBaseContext(newBase);

        MultiDex.install(this);
    }

}