package tcking.github.com.rfm.aacplay.repositories.secondary;

import java.util.List;

import io.reactivex.Observable;
import tcking.github.com.rfm.aacplay.pojos.SliderApiResponse;
import tcking.github.com.rfm.aacplay.pojos.SongDetailPojo;
import tcking.github.com.rfm.aacplay.pojos.SongImagePojo;
import tcking.github.com.rfm.aacplay.pojos.artistdetail.ArtistDetailPojo;
import tcking.github.com.rfm.aacplay.pojos.latestvideo.LatestVideoResponse;
import tcking.github.com.rfm.aacplay.pojos.newsresponse.LatestNewsResponse;

/**
 * Created by Aakash on 06/02/2018.
 */

interface SecondaryRepositoryDataSource {



    Observable<List<SongImagePojo>> songImage(String artist_name);

    Observable<List<SongDetailPojo>> songDetail(String date);

    Observable<ArtistDetailPojo> artistDetailApi();

    Observable<List<SliderApiResponse>> sliderApi();

    Observable<LatestVideoResponse> videoApi(String tag,String _page);

    Observable<LatestNewsResponse> newsApi(int _per_page,int _page);
}
