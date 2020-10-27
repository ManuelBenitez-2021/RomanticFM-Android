package tcking.github.com.rfm.aacplay.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import tcking.github.com.rfm.aacplay.R;
import tcking.github.com.rfm.aacplay.pojos.Piese;

/**
 * Created by Admin on 4/11/2018.
 */

public class LastSongsAdapter extends RecyclerView.Adapter<LastSongsAdapter.MyViewHolder> {


    // private List<ResponseOrder> responseOrders=new ArrayList<>();
    private List<Piese> piese=new ArrayList<>();
    Context context;



    public class MyViewHolder extends RecyclerView.ViewHolder {

           public TextView txt_song_name,txt_title;


        public MyViewHolder(View view) {
            super(view);
            txt_song_name =  (TextView)view.findViewById(R.id.txt_song_name);
            txt_title=  (TextView)view.findViewById(R.id.txt_title);

        }
    }


    public LastSongsAdapter(Context context) {
        this.context = context;

    }


    public void addingData(List<Piese> piese) {
        this.piese = piese;

        //Collections.reverse(this.songArrayList);

        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_last_songs, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
           final Piese piese_obj = piese.get(position);

        holder.txt_song_name.setText(piese_obj.getPiesa());

        if(position==0){
            holder.txt_title.setVisibility(View.VISIBLE);
        }else{
            holder.txt_title.setVisibility(View.GONE);
        }

      /*  final String CurrentString = song_name;
        String[] separated = CurrentString.split("-");



        if(separated.length==1){
            holder.txt_song_name.setText(""+(position+1)+". "+song_name);
        }else{
            holder.txt_song_name.setText(""+(position+1)+". "+separated[1].trim());

        }*/

    }

    @Override
    public int getItemCount() {
        return piese.size();
    }




}
