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

public class SecondaryRepository implements SecondaryRepositoryDataSource {

    private final SecondaryRepositoryDataSource remoteGitHubRepository;

    private static SecondaryRepository INSTANCE;


    public static SecondaryRepository getInstance(SecondaryServices gitHubService) {
        if (INSTANCE == null)
            INSTANCE = new SecondaryRepository(gitHubService);
        return INSTANCE;
    }

    private SecondaryRepository(SecondaryServices gitHubService) {
        remoteGitHubRepository = new RemoteSecondaryRepository(gitHubService);
    }




    @Override
    public Observable<List<SongImagePojo>> songImage(String artist_name) {
        return remoteGitHubRepository.songImage(artist_name);
    }

    @Override
    public Observable<List<SongDetailPojo>> songDetail(String song_name) {
        return remoteGitHubRepository.songDetail(song_name);
    }

    @Override
    public Observable<ArtistDetailPojo> artistDetailApi() {
        return remoteGitHubRepository.artistDetailApi();
    }



    @Override
    public Observable<List<SliderApiResponse>> sliderApi() {
        return remoteGitHubRepository.sliderApi();
    }

    @Override
    public Observable<LatestVideoResponse> videoApi(String tag, String _page) {
        return remoteGitHubRepository.videoApi(tag,_page);
    }

    @Override
    public Observable<LatestNewsResponse> newsApi(int _per_page,int _page) {
        return remoteGitHubRepository.newsApi(_per_page,_page);
    }


}
