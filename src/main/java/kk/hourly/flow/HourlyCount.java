package kk.hourly.flow;

/**
 * Created by Fairy_LFen on 2017/6/25.
 */
public class HourlyCount {
    private String hourStr = null;
    private int count;

/*
* dataStr : "2016-01-04 01:00:00"
* */
    public HourlyCount(String hourStr, int count) {
        this.hourStr = hourStr;
        this.count = count;
    }

    public String getHourStr() {
        return hourStr;
    }

    public void setHourStr(String hourStr) {
        this.hourStr = hourStr;
        }

public int getCount() {
        return count;
        }

public void setCount(int count) {
        this.count = count;
        }
        }
