package util;

/**
 * Created by Fairy_LFen on 2017/1/7.
 *
 * 每一辆车经过卡口时的数据。
 */
public class StayPointInfo {
    private String kkid;
    private String direction;
    private String plateColor;
    private String vehicleType;
    private String speed;
    private String plateNumber;
    private String stayPointDate;
    private String stayPointDuration;
    private String vehicleColor;

    public StayPointInfo() {
    }

    public StayPointInfo(String[] info_arry) {
        this.kkid = info_arry[0];
        this.direction = info_arry[1];
        this.plateColor = info_arry[2];
        this.vehicleType = info_arry[3];
        this.speed = info_arry[4];
        this.plateNumber = info_arry[5];
        this.stayPointDate = info_arry[6];
        this.stayPointDuration = info_arry[7];
        this.vehicleColor = info_arry[8];
    }

    public String getKkid() {
        return kkid;
    }

    public void setKkid(String kkid) {
        this.kkid = kkid;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getPlateColor() {
        return plateColor;
    }

    public void setPlateColor(String plateColor) {
        this.plateColor = plateColor;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public String getStayPointDate() {
        return stayPointDate;
    }

    public void setStayPointDate(String stayPointDate) {
        this.stayPointDate = stayPointDate;
    }

    public String getStayPointDuration() {
        return stayPointDuration;
    }

    public void setStayPointDuration(String stayPointDuration) {
        this.stayPointDuration = stayPointDuration;
    }

    public String getVehicleColor() {
        return vehicleColor;
    }

    public void setVehicleColor(String vehicleColor) {
        this.vehicleColor = vehicleColor;
    }
}
