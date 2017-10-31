package util;

/**
 * Created by Fairy_LFen on 2017/3/23.
 */
public class GeoDistance {
    private static final double EARTH_RADIUS = 6378.137;
    private double rad(double d)
    {
        return d * Math.PI / 180.0;
    }

    private static GeoDistance instance = null;

    public static GeoDistance getinstance(){
        if(instance == null){
            instance = new GeoDistance();
            return instance;
        }else
            return instance;
    }

/**
 * 计算两个经纬坐标距离
 * @param lat1 纬度1
 * @param lng1 经度1
 * @param lat2 纬度2
 * @param lng2 经度2
 * @return 结果为千米
 * */
    public double GetDistance(double lat1, double lng1, double lat2, double lng2)
    {
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double a = radLat1 - radLat2;
        double b = rad(lng1) - rad(lng2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a/2),2) +
                Math.cos(radLat1)*Math.cos(radLat2)*Math.pow(Math.sin(b/2),2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 10000) / 10000;
        return s;
    }
}
