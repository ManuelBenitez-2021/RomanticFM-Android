
package tcking.github.com.rfm.aacplay.pojos.newsresponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum {

    @SerializedName("Id")
    @Expose
    private Integer id;
    @SerializedName("Titlu")
    @Expose
    private String titlu;
    @SerializedName("Data")
    @Expose
    private String data;
    @SerializedName("Section")
    @Expose
    private String section;
    @SerializedName("Url")
    @Expose
    private String url;
    @SerializedName("Poza")
    @Expose
    private String poza;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitlu() {
        return titlu;
    }

    public void setTitlu(String titlu) {
        this.titlu = titlu;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPoza() {
        return poza;
    }

    public void setPoza(String poza) {
        this.poza = poza;
    }

}
