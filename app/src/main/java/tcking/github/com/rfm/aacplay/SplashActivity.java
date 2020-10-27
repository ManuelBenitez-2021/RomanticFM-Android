package tcking.github.com.rfm.aacplay;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;



/**
 * Created by Admin on 8/21/2017.
 */

public class SplashActivity extends Activity {

    long splashTime = 500;
    Handler handler;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        init();

        Intent intent = new Intent(getApplication(), MainActivity.class);
        startActivity(intent);
        finish();

      /*  handler.postDelayed(new Runnable() {
            @Override
            public void run() {


                Intent intent = new Intent(getApplication(), MainActivity.class);
                startActivity(intent);




                finish();
            }
        }, splashTime);*/
    }




    protected void init() {
        handler = new Handler();
    }
}
