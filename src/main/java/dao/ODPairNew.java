package dao;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fairy_LFen on 2017/5/13.
 */
public class ODPairNew {
    private int o_regin_index; // 起点卡口所在的聚簇
    private int d_region_index; // 终点卡口所在的聚簇
    public int weight;
    private List<Long> durationList = new ArrayList<Long>();  //起始点之间花费的时间

    public void setDurationList(List<Long> durationList) {
        this.durationList = durationList;
    }

    public List<Long> getDurationList() {
        return durationList;
    }

    public int getO_regin_index() {
        return o_regin_index;
    }

    public void setO_regin_index(int o_regin_index) {
        this.o_regin_index = o_regin_index;
    }

    public int getD_region_index() {
        return d_region_index;
    }

    public void setD_region_index(int d_region_index) {
        this.d_region_index = d_region_index;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
}
