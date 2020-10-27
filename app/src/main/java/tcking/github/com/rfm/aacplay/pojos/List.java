
package tcking.github.com.rfm.aacplay.pojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class List {

    @SerializedName("songs")
    @Expose
    private java.util.List<Song> songs = null;

    public java.util.List<Song> getSongs() {
        return songs;
    }

    public void setSongs(java.util.List<Song> songs) {
        this.songs = songs;
    }

}
