package tcking.github.com.rfm.aacplay;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import me.relex.circleindicator.CircleIndicator;
import tcking.github.com.rfm.aacplay.adapter.NewNewsAdapter;
import tcking.github.com.rfm.aacplay.adapter.SamplePagerAdapter;
import tcking.github.com.rfm.aacplay.adapter.UpcomingVideoAdapter;
import tcking.github.com.rfm.aacplay.pager.LoopViewPager;
import tcking.github.com.rfm.aacplay.pojos.SliderApiResponse;
import tcking.github.com.rfm.aacplay.pojos.latestvideo.LatestVideoResponse;
import tcking.github.com.rfm.aacplay.pojos.newsresponse.Datum;
import tcking.github.com.rfm.aacplay.pojos.newsresponse.LatestNewsResponse;
import tcking.github.com.rfm.aacplay.repositories.main.MainRepository;
import tcking.github.com.rfm.aacplay.repositories.secondary.SecondaryRepository;

/**
 * Created by Admin on 4/8/2018.
 */

public class NewsFragment  extends Fragment {

    RecyclerView rv_latest_videos,rv_new_on_capital;
    private UpcomingVideoAdapter upcomingVideoAdapter;
    SamplePagerAdapter samplePagerAdapter;

    private MainRepository mainRepository;
    private SecondaryRepository secondaryRepository;

    private NewNewsAdapter newNewsAdapter;
    LoopViewPager viewpager;
    CircleIndicator indicator;


    private int PAGE_COUNT = 1;
    private int visibleThreshold = 1;
    private boolean isUpcomingOrderLoading = false;
    SwipeRefreshLayout refreshLayout;
    private int lastVisibleItem, totalItemCount;

    TextView tv_see_more,tv_video_see_more;

    List<Datum> datumList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        return inflater.inflate(R.layout.fragment_news, container, false);
    }


    @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {



         viewpager = (LoopViewPager) view.findViewById(R.id.viewpager);
         indicator = (CircleIndicator) view.findViewById(R.id.indicator);
        tv_video_see_more= (TextView) view.findViewById(R.id.tv_video_see_more);

        rv_latest_videos = (RecyclerView) view.findViewById(R.id.rv_latest_videos);
        upcomingVideoAdapter = new UpcomingVideoAdapter(getActivity());
        final RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        rv_latest_videos.setLayoutManager(mLayoutManager);
        rv_latest_videos.setItemAnimator(new DefaultItemAnimator());
        rv_latest_videos.setAdapter(upcomingVideoAdapter);

        tv_see_more= (TextView) view.findViewById(R.id.tv_see_more);
        rv_new_on_capital= (RecyclerView) view.findViewById(R.id.rv_new_on_capital);

        newNewsAdapter = new NewNewsAdapter(getActivity());
        final RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(getActivity());
        rv_new_on_capital.setLayoutManager(mLayoutManager1);
        rv_new_on_capital.setItemAnimator(new DefaultItemAnimator());
        rv_new_on_capital.setAdapter(newNewsAdapter);

        rv_latest_videos.setNestedScrollingEnabled(false);
        rv_new_on_capital.setNestedScrollingEnabled(false);

        tv_see_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isUpcomingOrderLoading = true;
                hitNewsApi();
            }
        });


        tv_video_see_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), VideoNewsActivity.class);
                startActivity(intent);


            }
        });

      /*  rv_new_on_capital.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);


                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                totalItemCount = linearLayoutManager.getItemCount();
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                if (!isUpcomingOrderLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {


                        isUpcomingOrderLoading = true;
                        hitNewsApi();


                }

            }
        });*/

        secondaryRepository = MyApplication.getApp().getSecondRepositoryRepository();
        secondaryRepository.sliderApi()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<SliderApiResponse>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<SliderApiResponse> value) {


                        if(value.size()==0){

                        }else{
                            samplePagerAdapter = new SamplePagerAdapter(getActivity(),value);
                            viewpager.setAdapter(samplePagerAdapter);
                            indicator.setViewPager(viewpager);

                            // samplePagerAdapter.addingData(value);

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



        secondaryRepository = MyApplication.getApp().getSecondRepositoryRepository();
        secondaryRepository.videoApi("zumobile","1")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<LatestVideoResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(LatestVideoResponse value) {


                        if(value.getData().size()==0){

                        }else{
                                  /*  samplePagerAdapter = new SamplePagerAdapter(getActivity(),value);
                                    viewpager.setAdapter(samplePagerAdapter);
                                    indicator.setViewPager(viewpager);*/



                            upcomingVideoAdapter.addingData(value.getData());

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


        PAGE_COUNT = 1;
        hitNewsApi();



        ((MainActivity) getActivity()).setFragmentRefreshListener2(new MainActivity.FragmentRefreshListener2() {


            @Override
            public void update() {


                secondaryRepository = MyApplication.getApp().getSecondRepositoryRepository();
                secondaryRepository.sliderApi()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<List<SliderApiResponse>>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onNext(List<SliderApiResponse> value) {


                                if(value.size()==0){

                                }else{
                                    samplePagerAdapter = new SamplePagerAdapter(getActivity(),value);
                                    viewpager.setAdapter(samplePagerAdapter);
                                    indicator.setViewPager(viewpager);

                                    // samplePagerAdapter.addingData(value);

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



                secondaryRepository = MyApplication.getApp().getSecondRepositoryRepository();
                secondaryRepository.videoApi("zumobile","1")
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<LatestVideoResponse>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onNext(LatestVideoResponse value) {


                                if(value.getData().size()==0){

                                }else{
                                  /*  samplePagerAdapter = new SamplePagerAdapter(getActivity(),value);
                                    viewpager.setAdapter(samplePagerAdapter);
                                    indicator.setViewPager(viewpager);*/



                                     upcomingVideoAdapter.addingData(value.getData());

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


                PAGE_COUNT = 1;
                hitNewsApi();


            }
        });




    }

    private void hitNewsApi() {


        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        if(PAGE_COUNT>1)
        progressDialog.show();



        secondaryRepository = MyApplication.getApp().getSecondRepositoryRepository();
        secondaryRepository.newsApi(10,PAGE_COUNT)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<LatestNewsResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(LatestNewsResponse value) {

                        if(PAGE_COUNT>1) {
                            if (progressDialog.isShowing()) {
                                progressDialog.dismiss();

                            }
                        }


                        /*    if (PAGE_COUNT == 1) {

                              *//*  if (value.getData().size() == 0) {

                                    tv_no_customers.setVisibility(View.VISIBLE);
                                } else {
                                    tv_no_customers.setVisibility(View.GONE);
                                }*//*

                            } else {
                                progress_bar.setVisibility(View.GONE);
                            }*/


                            if (PAGE_COUNT == 1) {
                                datumList.clear();
                            }
                            PAGE_COUNT = PAGE_COUNT + 1;

                            datumList.addAll(value.getData());
                            newNewsAdapter.addingData(datumList);


                            if (value.getData().size()>0) {
                                isUpcomingOrderLoading = false;

                                tv_see_more.setVisibility(View.VISIBLE);
                            } else {
                                isUpcomingOrderLoading = true;

                                tv_see_more.setVisibility(View.GONE);
                            }




                    }

                    @Override
                    public void onError(Throwable e) {
                        if(PAGE_COUNT>1) {
                            if (progressDialog.isShowing()) {
                                progressDialog.dismiss();

                            }
                        }

                        Log.d("errorrrr","error "+e.getLocalizedMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });



    }




}
