package util;

/**
 * Created by Fairy_LFen on 2017/1/15.
 */
public class KKInfo {
    private String id;  //1. 序号
    private String kkId;  //2. 卡口id
    private String fxbh;  //3. 方向编号
    private String kkType;  //4. 卡口类型
    private String kkName;    //7. 卡口名称
    private String lng;    //8.经度
    private String lat;    //9.纬度

    public KKInfo() {
    }

    public KKInfo(String[] str_array) {

        this.id = str_array[0];
        this.kkId = str_array[1];
        this.fxbh = str_array[2];
        this.kkType = str_array[3];
        this.kkName = str_array[6];
        this.lng = str_array[7];
        this.lat = str_array[8];

        System.out.println(String.format("[%s, %s]", this.lng, this.lat));
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKkId() {
        return kkId;
    }

    public void setKkId(String kkId) {
        this.kkId = kkId;
    }

    public String getFxbh() {
        return fxbh;
    }

    public void setFxbh(String fxbh) {
        this.fxbh = fxbh;
    }

    public String getKkType() {
        return kkType;
    }

    public void setKkType(String kkType) {
        this.kkType = kkType;
    }

    public String getKkName() {
        return kkName;
    }

    public void setKkName(String kkName) {
        this.kkName = kkName;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }
}
