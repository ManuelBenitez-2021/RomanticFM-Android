package tcking.github.com.rfm.aacplay;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import io.paperdb.Paper;
import tcking.github.com.rfm.aacplay.adapter.NotificationAdapter;
import tcking.github.com.rfm.aacplay.pojos.Notificationspojo;

public class NotificationFragment extends Fragment {

    RecyclerView recycler_view;
    NotificationAdapter notificationAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        return inflater.inflate(R.layout.fragment_notification, container, false);


    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        recycler_view = (RecyclerView) view.findViewById(R.id.recycler_view);
        notificationAdapter = new NotificationAdapter(getActivity());
        final RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recycler_view.setLayoutManager(mLayoutManager);
        recycler_view.setItemAnimator(new DefaultItemAnimator());
        recycler_view.setAdapter(notificationAdapter);




        ((MainActivity) getActivity()).setFragmentRefreshListener1(new MainActivity.FragmentRefreshListener1() {


            @Override
            public void update() {
                ArrayList<Notificationspojo> stringArrayList = Paper.book("fcm").read("msgs", new ArrayList<Notificationspojo>());
                notificationAdapter.addingData(stringArrayList);
            }
        });


    }
}
