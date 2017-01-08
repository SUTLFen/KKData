package Util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fairy_LFen on 2017/1/8.
 *
 * 2016-01-01这一天中，每辆车的行驶轨迹点
 *
 */
public class IndivisualPrCarInfo {
    private String plateNumber = null;
    private List<StayPoint> stayPoints = new ArrayList<StayPoint>();
    private String speed = null;
    private String kkid = null;
    private String direction = null;

    public IndivisualPrCarInfo(StayPointInfo stayPointInfo) {
        this.plateNumber = stayPointInfo.getPlateNumber();
        this.speed = stayPointInfo.getSpeed();
        this.kkid = stayPointInfo.getKkid();
        this.direction = stayPointInfo.getDirection();

        StayPoint staypoint = new StayPoint(stayPointInfo.getStayPointDate(), stayPointInfo.getKkid());
        this.stayPoints.add(staypoint);
    }

    public void addStayPoint(StayPoint stayPoint){
        stayPoints.add(stayPoint);
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public List<StayPoint> getStayPoints() {
        return stayPoints;
    }

    public void setStayPoints(List<StayPoint> stayPoints) {
        this.stayPoints = stayPoints;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
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
}
