package tcking.github.com.rfm.aacplay.repositories.main;

import io.reactivex.Observable;
import retrofit2.http.GET;
import tcking.github.com.rfm.aacplay.pojos.LastSongPojo;
import tcking.github.com.rfm.aacplay.pojos.UpcomingSongs;

/**
 * Created by Aakash on 06/02/2018.
 */
interface MainServices {

    @GET("andro/urmatoarea_piesa_rfm.html")
    Observable<UpcomingSongs> upcomingSongs();

    @GET("andro/liverfm.php")
    Observable<LastSongPojo> lastSongs();

}
