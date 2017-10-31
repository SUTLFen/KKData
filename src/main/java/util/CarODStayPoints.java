package util;

/**
 * Created by Fairy_LFen on 2017/1/10.
 */
public class CarODStayPoints {
    private String plateNumber = null;
    private StayPoint o_staypoint = null;
    private StayPoint d_staypoint = null;

    public CarODStayPoints() {
    }

    public CarODStayPoints(String plateNumber, StayPoint o_staypoint, StayPoint d_staypoint) {
        this.plateNumber = plateNumber;
        this.o_staypoint = o_staypoint;
        this.d_staypoint = d_staypoint;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public StayPoint getO_staypoint() {
        return o_staypoint;
    }

    public void setO_staypoint(StayPoint o_staypoint) {
        this.o_staypoint = o_staypoint;
    }

    public StayPoint getD_staypoint() {
        return d_staypoint;
    }

    public void setD_staypoint(StayPoint d_staypoint) {
        this.d_staypoint = d_staypoint;
    }
}
