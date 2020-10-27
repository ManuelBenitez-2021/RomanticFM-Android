package tcking.github.com.rfm.aacplay.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import tcking.github.com.rfm.aacplay.R;
import tcking.github.com.rfm.aacplay.pojos.SongImage;

/**
 * Created by Admin on 4/15/2018.
 * Created by Admin on 4/15/2018.
 */

public class PageOne extends android.support.v4.app.Fragment {

    ImageView img_main,img_switch_to_video;
    ImageView img_main_one;




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.vp_page_one, container, false);
         img_main = (ImageView) view.findViewById(R.id.img_main);

        img_main_one = (ImageView) view.findViewById(R.id.img_main_one);


        img_switch_to_video= (ImageView) view.findViewById(R.id.img_switch_to_video);

        img_switch_to_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(true);
            }
        });



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
    public void onMessageEvent(SongImage songImage) {


        if(!songImage.getImage().equalsIgnoreCase("abc")){


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
                    .load(songImage.getImage())
//                    .placeholder(R.drawable.new_logo)
                    .placeholder(R.drawable.upper_image_1)
                 //   .transform(new CircleTransform())
               /*     .placeholder(R.mipmap.ic_launcher) //this is optional the image to display while the url image is downloading
                    .error(R.mipmap.ic_launcher)         //this is also optional if some error has occurred in downloading the image this image would be displayed
              */      .into(img_main_one);





        }else{


            img_main_one.setVisibility(View.GONE);
            img_main.setVisibility(View.VISIBLE);



        }



    }





}
