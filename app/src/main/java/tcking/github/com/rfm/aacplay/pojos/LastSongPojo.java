
package tcking.github.com.rfm.aacplay.pojos;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LastSongPojo {

    @SerializedName("piese")
    @Expose
    private List<Piese> piese = null;
    @SerializedName("success")
    @Expose
    private Integer success;

    public List<Piese> getPiese() {
        return piese;
    }

    public void setPiese(List<Piese> piese) {
        this.piese = piese;
    }

    public Integer getSuccess() {
        return success;
    }

    public void setSuccess(Integer success) {
        this.success = success;
    }

}
