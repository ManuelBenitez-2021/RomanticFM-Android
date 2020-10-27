package tcking.github.com.rfm.aacplay.repositories.secondary;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;
import tcking.github.com.rfm.aacplay.pojos.SliderApiResponse;
import tcking.github.com.rfm.aacplay.pojos.SongDetailPojo;
import tcking.github.com.rfm.aacplay.pojos.SongImagePojo;
import tcking.github.com.rfm.aacplay.pojos.artistdetail.ArtistDetailPojo;
import tcking.github.com.rfm.aacplay.pojos.latestvideo.LatestVideoResponse;
import tcking.github.com.rfm.aacplay.pojos.newsresponse.LatestNewsResponse;

/**
 * Created by Aakash on 06/02/2018.
 */
interface SecondaryServices {



    @GET("api/artisti")
    Observable<java.util.List<SongImagePojo>> songImage(
            @Query("nume") String nume);


    @GET("api/program")
    Observable<java.util.List<SongDetailPojo>> songDetail(
            @Query("start") String start);


    @GET("api/stiri-live")
    Observable<ArtistDetailPojo> artistDetailApi();

    @GET("api/slider")
    Observable<java.util.List<SliderApiResponse>> sliderApi();

    @GET("api/stiri")
    Observable<LatestVideoResponse> videoApi(
            @Query("tag") String tag,
            @Query("_page") String _page);

    @GET("api/noutati")
    Observable<LatestNewsResponse> newsApi(
            @Query("_per_page") int _per_page,
            @Query("_page") int _page);

}
