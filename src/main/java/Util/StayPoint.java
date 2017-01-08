package Util;

/**
 * Created by Fairy_LFen on 2017/1/8.
 *
 * 每辆车经过的卡口点（时间、地点（卡口id））
 *
 */
public class StayPoint {
    private String kkid = null;
    private String time = null;

    public StayPoint(String stayPointDate, String kkid) {
        this.kkid = kkid;
        this.time = stayPointDate;
    }

    public String getKkid() {
        return kkid;
    }

    public void setKkid(String kkid) {
        this.kkid = kkid;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
