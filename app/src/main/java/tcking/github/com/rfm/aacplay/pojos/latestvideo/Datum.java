
package tcking.github.com.rfm.aacplay.pojos.latestvideo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum {

    @SerializedName("StiriID")
    @Expose
    private Integer stiriID;
    @SerializedName("Titlu")
    @Expose
    private String titlu;
    @SerializedName("Categorie")
    @Expose
    private String categorie;
    @SerializedName("CMStags")
    @Expose
    private String cMStags;
    @SerializedName("Data")
    @Expose
    private String data;
    @SerializedName("Poza")
    @Expose
    private String poza;
    @SerializedName("Url")
    @Expose
    private String url;

    public Integer getStiriID() {
        return stiriID;
    }

    public void setStiriID(Integer stiriID) {
        this.stiriID = stiriID;
    }

    public String getTitlu() {
        return titlu;
    }

    public void setTitlu(String titlu) {
        this.titlu = titlu;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public String getCMStags() {
        return cMStags;
    }

    public void setCMStags(String cMStags) {
        this.cMStags = cMStags;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getPoza() {
        return poza;
    }

    public void setPoza(String poza) {
        this.poza = poza;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
