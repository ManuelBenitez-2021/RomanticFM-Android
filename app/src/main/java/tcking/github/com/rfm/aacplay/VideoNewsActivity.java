package tcking.github.com.rfm.aacplay;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import tcking.github.com.rfm.aacplay.adapter.VideoNewsAdapter;
import tcking.github.com.rfm.aacplay.pojos.latestvideo.Datum;
import tcking.github.com.rfm.aacplay.pojos.latestvideo.LatestVideoResponse;
import tcking.github.com.rfm.aacplay.repositories.secondary.SecondaryRepository;

public class VideoNewsActivity extends AppCompatActivity {

    ImageView img_back;

    private RecyclerView recyclerView;
    private VideoNewsAdapter mAdapter;

    private TextView tv_no_customers;
    private ProgressBar progress_bar;
    private int PAGE_COUNT = 1;

    private Disposable disposable;
    private SecondaryRepository secondaryRepository;

    private int visibleThreshold = 1;
    private boolean isUpcomingOrderLoading = false;

    private int lastVisibleItem, totalItemCount;

    List<Datum> datumList = new ArrayList<>();
    //SwipeRefreshLayout refreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_news);

        img_back = (ImageView) findViewById(R.id.img_back);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //refreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh);


        progress_bar= (ProgressBar) findViewById(R.id.progress_bar);



        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mAdapter = new VideoNewsAdapter(VideoNewsActivity.this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(VideoNewsActivity.this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        if (isConnectedToInternet(VideoNewsActivity.this)) {

            videoApi();

        } else {
            Toast.makeText(VideoNewsActivity.this,"Please check your internet connection",Toast.LENGTH_LONG).show();

        }


        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);


                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                totalItemCount = linearLayoutManager.getItemCount();
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                if (!isUpcomingOrderLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {

                    Log.d("pagination", "true");
                    if (isConnectedToInternet(VideoNewsActivity.this)) {
                        isUpcomingOrderLoading = true;
                        videoApi();
                    }

                    else {
                    //    CommonUtils.showSnackbar(OrdersActivity.this, getString(R.string.please_check_your_internet_connection));

                        Toast.makeText(VideoNewsActivity.this,"Please check your internet connection",Toast.LENGTH_LONG).show();
                    }
                }

            }
        });



     /*   refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                if (isConnectedToInternet(VideoNewsActivity.this)) {
                    PAGE_COUNT = 1;
                    customerListApi();

                } else {
                    CommonUtils.check_your_network_available(AllCustomersActivity.this);
                }
                refreshLayout.setRefreshing(false);


            }
        });*/


    }

    ProgressDialog progressDialog;

    private void videoApi() {

        if (PAGE_COUNT == 1) {

            progressDialog = new ProgressDialog(VideoNewsActivity.this);
            progressDialog.setMessage("Loading...");
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
        }

        else
            progress_bar.setVisibility(View.VISIBLE);



        secondaryRepository = MyApplication.getApp().getSecondRepositoryRepository();
        secondaryRepository.videoApi("zumobile",""+PAGE_COUNT)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<LatestVideoResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(LatestVideoResponse value) {

                        //  mainActivityViewInterface.logMessage(TAGMESSAGE,"value "+value.get(0).getName());


                        progress_bar.setVisibility(View.GONE);
                            if (PAGE_COUNT == 1) {
                                if (progressDialog.isShowing()) {
                                    progressDialog.dismiss();

                                }

                              /*  if (value.getData().size() == 0) {

                                    tv_no_customers.setVisibility(View.VISIBLE);
                                } else {
                                    tv_no_customers.setVisibility(View.GONE);
                                }*/

                            }


                            if (PAGE_COUNT == 1) {
                                datumList.clear();
                            }
                            PAGE_COUNT = PAGE_COUNT + 1;

                            datumList.addAll(value.getData());
                            mAdapter.addingData(datumList);


                            if (value.getData().size() != 0) {
                                isUpcomingOrderLoading = false;

                            } else {
                                isUpcomingOrderLoading = true;
                            }




                    }

                    @Override
                    public void onError(Throwable e) {

                        progress_bar.setVisibility(View.GONE);
                        if (PAGE_COUNT == 1) {
                            if (progressDialog.isShowing()) {
                                progressDialog.dismiss();

                            }
                        }

                    }

                    @Override
                    public void onComplete() {

                    }
                });


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
