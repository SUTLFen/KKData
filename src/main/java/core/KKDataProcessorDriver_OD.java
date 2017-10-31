package core;

import component.PrCarODStayPointsCollector;
import component.PrCarODStayPointsFormater;
import component.PrCarTrajStayPointsCollector;

import java.io.IOException;

/**
 * Created by Fairy_LFen on 2017/6/27.
 * <p>
 * 生成ODPair
 */
public class KKDataProcessorDriver_OD {


    private String prCarParentPath = "KKData\\data_0104\\01_PrCar";
    private String prCarTrajParentPath = "KKData\\data_0104\\02_TrajPoints";

    private String odPairSegs = "KKData\\data_0104\\ODPairSegs";
    private String odPairSegs_new = "KKData\\data_0104\\ODPairSegs_new";


    private void process() throws IOException {

        new PrCarTrajStayPointsCollector(prCarParentPath, prCarTrajParentPath).fire();  //从私家车数据——01中，整理车每辆车在今日的轨迹点-02（result/PrCarTrajStay.json）
        new PrCarODStayPointsCollector(prCarTrajParentPath, odPairSegs).fire();   // 从每辆车今日的轨迹点数据-02中，整理车每辆车在各个时间的OD点-03（data/ODPair_01_01_seg_**.json）
        new PrCarODStayPointsFormater(odPairSegs, odPairSegs_new).fire(); // 格式规范化-03(data/ODPair_0101_seg_*.json)，得到-04(data/ODPair_0101_seg_01_new.json)

    }

    public static void main(String[] args) throws IOException {
        new KKDataProcessorDriver_OD().process();
    }


}
