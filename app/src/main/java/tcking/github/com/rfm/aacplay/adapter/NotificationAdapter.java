package tcking.github.com.rfm.aacplay.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import tcking.github.com.rfm.aacplay.R;
import tcking.github.com.rfm.aacplay.pojos.Notificationspojo;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder> {


    // private List<ResponseOrder> responseOrders=new ArrayList<>();
    private ArrayList<Notificationspojo> notificationspojos=new ArrayList<>();
    Context context;



    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView txt_title,txt_date,txt_time;


        public MyViewHolder(View view) {
            super(view);
            txt_title =  (TextView)view.findViewById(R.id.txt_title);
            txt_date =  (TextView)view.findViewById(R.id.txt_date);
            txt_time =  (TextView)view.findViewById(R.id.txt_time);

        }
    }


    public NotificationAdapter(Context context) {
        this.context = context;

    }


    public void addingData(ArrayList<Notificationspojo> notificationspojos) {
        this.notificationspojos = notificationspojos;

        //Collections.reverse(this.songArrayList);

        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_notifications, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
       /* final Notificationspojo notificationspojo = notificationspojos.get(position);


        holder.txt_title.setText(notificationspojo.getMessage());
        holder.txt_date.setText(notificationspojo.getDate());
        holder.txt_time.setText(notificationspojo.getTime());*/


    }

    @Override
    public int getItemCount() {
    //    return notificationspojos.size();
        return 10;
    }




}
