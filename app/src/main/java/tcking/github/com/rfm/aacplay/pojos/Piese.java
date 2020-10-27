
package tcking.github.com.rfm.aacplay.pojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Piese {

    @SerializedName("data")
    @Expose
    private String data;
    @SerializedName("piesa")
    @Expose
    private String piesa;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getPiesa() {
        return piesa;
    }

    public void setPiesa(String piesa) {
        this.piesa = piesa;
    }

}
