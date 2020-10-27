package tcking.github.com.rfm.aacplay.repositories.main;

import io.reactivex.Observable;
import tcking.github.com.rfm.aacplay.pojos.LastSongPojo;
import tcking.github.com.rfm.aacplay.pojos.UpcomingSongs;

/**
 * Created by Aakash on 06/02/2018.
 */

public class MainRepository implements MainRepositoryDataSource {

    private final MainRepositoryDataSource remoteGitHubRepository;

    private static MainRepository INSTANCE;


    public static MainRepository getInstance(MainServices gitHubService) {
        if (INSTANCE == null)
            INSTANCE = new MainRepository(gitHubService);
        return INSTANCE;
    }

    private MainRepository(MainServices gitHubService) {
        remoteGitHubRepository = new RemoteMainRepository(gitHubService);
    }





    @Override
    public Observable<UpcomingSongs> upcomingSongs() {
        return remoteGitHubRepository.upcomingSongs();
    }

    @Override
    public Observable<LastSongPojo> lastSongs() {
        return remoteGitHubRepository.lastSongs();
    }


}
