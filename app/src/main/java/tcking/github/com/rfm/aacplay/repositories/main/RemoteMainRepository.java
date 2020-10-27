package tcking.github.com.rfm.aacplay.repositories.main;

import io.reactivex.Observable;
import tcking.github.com.rfm.aacplay.pojos.LastSongPojo;
import tcking.github.com.rfm.aacplay.pojos.UpcomingSongs;

/**
 * Created by Aakash on 06/02/2018.
 */

class RemoteMainRepository implements MainRepositoryDataSource {

    private final MainServices mainService;

    public RemoteMainRepository(MainServices mainService) {
        this.mainService = mainService;
    }


    @Override
    public Observable<UpcomingSongs> upcomingSongs() {
        return mainService.upcomingSongs();
    }

    @Override
    public Observable<LastSongPojo> lastSongs() {
        return mainService.lastSongs();
    }


}
