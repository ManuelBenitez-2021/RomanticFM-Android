
package tcking.github.com.rfm.aacplay.pojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SongImagePojo {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("nume")
    @Expose
    private String nume;
    @SerializedName("poza")
    @Expose
    private String poza;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public String getPoza() {
        return poza;
    }

    public void setPoza(String poza) {
        this.poza = poza;
    }

}
