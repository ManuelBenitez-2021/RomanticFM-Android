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

class RemoteSecondaryRepository implements SecondaryRepositoryDataSource {

    private final SecondaryServices mainService;

    public RemoteSecondaryRepository(SecondaryServices mainService) {
        this.mainService = mainService;
    }



    @Override
    public Observable<List<SongImagePojo>> songImage(String artist_name) {
        return mainService.songImage(artist_name);
    }

    @Override
    public Observable<List<SongDetailPojo>> songDetail(String date) {
        return mainService.songDetail(date);
    }

    @Override
    public Observable<ArtistDetailPojo> artistDetailApi() {
        return mainService.artistDetailApi();
    }

    @Override
    public Observable<List<SliderApiResponse>> sliderApi() {
        return mainService.sliderApi();
    }

    @Override
    public Observable<LatestVideoResponse> videoApi(String tag, String _page) {
        return mainService.videoApi(tag,_page);
    }

    @Override
    public Observable<LatestNewsResponse> newsApi(int _per_page,int _page) {
        return mainService.newsApi(_per_page,_page);
    }

}
