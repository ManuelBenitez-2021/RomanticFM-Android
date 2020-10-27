package tcking.github.com.rfm.aacplay.adapter;

import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lb.auto_fit_textview.AutoResizeTextView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import tcking.github.com.rfm.aacplay.MyApplication;
import tcking.github.com.rfm.aacplay.R;
import tcking.github.com.rfm.aacplay.pojos.Song;
import tcking.github.com.rfm.aacplay.pojos.SongImagePojo;
import tcking.github.com.rfm.aacplay.repositories.secondary.SecondaryRepository;

/**
 * Created by Admin on 4/9/2018.
 */

public class UpcomingSongsAdapter extends RecyclerView.Adapter<UpcomingSongsAdapter.MyViewHolder> {


     private List<Song> songArrayList=new ArrayList<>();
    Context context;

    private SecondaryRepository secondaryRepository;

   // String img_url="";


    public class MyViewHolder extends RecyclerView.ViewHolder {

           public TextView txt_next;

           public AutoResizeTextView txt_title,txt_song_name;

           public RelativeLayout rl_main;
           public ImageView img_artist;

        public MyViewHolder(View view) {
            super(view);
            txt_title = (AutoResizeTextView) view.findViewById(R.id.txt_title);
            txt_next = (TextView) view.findViewById(R.id.txt_next);

            txt_song_name = (AutoResizeTextView) view.findViewById(R.id.txt_song_name);
            rl_main = (RelativeLayout) view.findViewById(R.id.rl_main);
            img_artist = (ImageView) view.findViewById(R.id.img_artist);

        }
    }


    public UpcomingSongsAdapter(Context context) {
        this.context = context;

    }


 /*   public void addingImageUrl(String img_url) {
        this.img_url = img_url;
        notifyDataSetChanged();
    }*/


    public void addingData(List<Song> songArrayList) {
        this.songArrayList = songArrayList;

        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_upcoming_songs, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(UpcomingSongsAdapter.MyViewHolder holder, int position) {
           final Song song = songArrayList.get(position);


        holder.txt_title.setText(song.getArtist());


        holder.txt_song_name.setText(song.getSong());

        holder.txt_title.setSelected(true);
        holder.txt_song_name.setSelected(true);

    /*    if(img_url.equalsIgnoreCase("")){
            holder.img_artist.setImageResource(R.drawable.no_artist);
        }else{
            Picasso.with(context)
                    .load(img_url)
                    .placeholder(R.drawable.no_artist)
                       .into(holder.img_artist);
        }
*/




        if(position==0){
//            holder.rl_main.setBackgroundColor(Color.parseColor("#C1CE21"));

//            setImageOne(song.getArtist(),holder.img_artist);
            holder.txt_next.setVisibility(View.VISIBLE);

        }else{
//            holder.rl_main.setBackgroundColor(Color.parseColor("#00A388"));

//            setImageTwo(song.getArtist(),holder.img_artist);
            holder.txt_next.setVisibility(View.GONE);
        }
    }

    private void setImageTwo(String artist, final ImageView img_artist) {

        secondaryRepository = MyApplication.getApp().getSecondRepositoryRepository();
        secondaryRepository.songImage(artist/*"Andra"*/)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<SongImagePojo>>() {
                    @Override
                    public void onSubscribe(Disposable d) {


                    }

                    @Override
                    public void onNext(List<SongImagePojo> value) {


                        if(value.size()==0){

                            Picasso.with(context)
                                    .load(R.drawable.ic_launcher)

                                    .into(img_artist);
                        }else{



                            Picasso.with(context)
                                    .load(value.get(0).getPoza())
                                    .placeholder(R.drawable.ic_launcher)
                                    .into(img_artist);

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



    private void setImageOne(String artist_name, final ImageView v) {


        if (isConnectedToInternet(context)) {


            secondaryRepository = MyApplication.getApp().getSecondRepositoryRepository();
            secondaryRepository.songImage(artist_name/*"Andra"*/)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<List<SongImagePojo>>() {
                        @Override
                        public void onSubscribe(Disposable d) {


                        }

                        @Override
                        public void onNext(List<SongImagePojo> value) {


                            if(value.size()==0){

                                Picasso.with(context)
                                        .load(R.drawable.splash_two)

                                        .into(v);
                            }else{



                                    Picasso.with(context)
                                            .load(value.get(0).getPoza())
                                            .placeholder(R.drawable.splash_two)
                                            .into(v);

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
    public int getItemCount() {
        return songArrayList.size()/*responseOrders.size()*/;
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
