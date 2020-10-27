package tcking.github.com.rfm.aacplay.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import tcking.github.com.rfm.aacplay.R;
import tcking.github.com.rfm.aacplay.WebViewActivity;
import tcking.github.com.rfm.aacplay.pojos.artistdetail.Datum;

public class ArtistDetailAdapter extends RecyclerView.Adapter<ArtistDetailAdapter.MyViewHolder> {


    // private List<ResponseOrder> responseOrders=new ArrayList<>();
    Context context;
    List<Datum> data=new ArrayList<>();


    public class MyViewHolder extends RecyclerView.ViewHolder {

           public ImageView img_main;
           public TextView tv_main;
           RelativeLayout rl_main;

        public MyViewHolder(View view) {
            super(view);
            img_main = (ImageView) view.findViewById(R.id.img_main);
            tv_main = (TextView) view.findViewById(R.id.tv_main);
            rl_main= (RelativeLayout) view.findViewById(R.id.rl_main);
        }
    }


    public ArtistDetailAdapter(Context context) {
        this.context = context;

    }


    public void addingData(List<Datum> data) {
        this.data = data;


        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_artist_detail, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
          final Datum datam = data.get(position);

          holder.tv_main.setText(datam.getTitlu());

        Picasso.with(context)
                .load(datam.getPoza())

                .placeholder(R.drawable.slide_logo)
                .into(holder.img_main);

      //  GlideApp.with(context).load(datam.getPoza()).into(holder.img_main);



        holder.rl_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(context, WebViewActivity.class);
                intent1.putExtra("url", datam.getLink());
                context.startActivity(intent1);
            }
        });


    }

    @Override
    public int getItemCount() {
        return data.size();
    }




}
