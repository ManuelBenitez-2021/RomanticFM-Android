package tcking.github.com.rfm.aacplay.repositories.main;

import io.reactivex.Observable;
import tcking.github.com.rfm.aacplay.pojos.LastSongPojo;
import tcking.github.com.rfm.aacplay.pojos.UpcomingSongs;

/**
 * Created by Aakash on 06/02/2018.
 */

interface MainRepositoryDataSource {



    Observable<UpcomingSongs> upcomingSongs();
    Observable<LastSongPojo> lastSongs();

}
