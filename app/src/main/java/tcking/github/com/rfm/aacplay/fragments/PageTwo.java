package tcking.github.com.rfm.aacplay.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import tcking.github.com.rfm.aacplay.R;
import tcking.github.com.rfm.aacplay.pojos.SongDetail;

/**
 * Created by Admin on 4/15/2018.
 */

public class PageTwo extends android.support.v4.app.Fragment {
    ImageView img_main;
    TextView tv_time,tv_on_air;
    ImageView img_main_one;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.vp_page_two, container, false);
        //  TextView tvLabel = (TextView) view.findViewById(R.id.txtMain);
        img_main = (ImageView) view.findViewById(R.id.img_main);

        tv_time = (TextView) view.findViewById(R.id.tv_time);
        tv_on_air= (TextView) view.findViewById(R.id.tv_on_air);
        img_main_one = (ImageView) view.findViewById(R.id.img_main_one);

        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().unregister(this);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(SongDetail  songDetail) {
        if(!songDetail.getImage().equalsIgnoreCase("abc")){
            tv_time.setVisibility(View.VISIBLE);
            tv_on_air.setVisibility(View.VISIBLE);
            String time ="";

            time = time +parseDateToddMMyyyy(songDetail.getStart());
            time = time +" - ";
            time = time +parseDateToddMMyyyy(songDetail.getEnd());

            tv_time.setText(songDetail.getNume()+"\n"+time);

            new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("coco", "bbbbbb");
                        img_main_one.setVisibility(View.VISIBLE);
                        img_main.setVisibility(View.GONE);
                    }
                }, 1200
            );


            Picasso.with(getActivity())
                    .load(songDetail.getImage())
                    .placeholder(R.drawable.new_logo)
//                    .placeholder(R.drawable.upper_image_1)
                    //   .transform(new CircleTransform())
               /*     .placeholder(R.mipmap.ic_launcher) //this is optional the image to display while the url image is downloading
                    .error(R.mipmap.ic_launcher)         //this is also optional if some error has occurred in downloading the image this image would be displayed
              */      .into(img_main_one);




        }else{


            tv_time.setVisibility(View.GONE);

            tv_on_air.setVisibility(View.GONE);

            img_main_one.setVisibility(View.GONE);
            img_main.setVisibility(View.VISIBLE);



        }



    }


    public String parseDateToddMMyyyy(String time) {
        String inputPattern = "HH:mm:ss";
        String outputPattern = "h:mm a";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }


}