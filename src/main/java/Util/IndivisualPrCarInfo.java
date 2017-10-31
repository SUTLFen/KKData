package util;

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

    public IndivisualPrCarInfo() {
    }

    public IndivisualPrCarInfo(StayPointInfo stayPointInfo) {
        this.plateNumber = stayPointInfo.getPlateNumber();

        StayPoint stayPoint = new StayPoint(stayPointInfo);
        this.stayPoints.add(stayPoint);

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
}
