package core;

import common.Component;
import component.PrCarDataIn0101Collector;

import java.io.IOException;
import java.text.MessageFormat;

/**
 * Created by Fairy_LFen on 2017/2/14.
 * 搜集所有私家车数据
 */
public class KKDataProcessorDriver extends AbstractKKDataProcessorDriver{
//    private String dataOfDay = "data_0101";

//    private String prCarData_fileName = "PrCarDataIn0101_%5_4.json";

//    private String rawPath = "KKData\\"+dataOfDay+"\\2016-01-01.txt";
//    private String prCarPath = "KKData\\"+dataOfDay+"\\"+prCarData_fileName;

    private String dayStr = "0104";
    private String rawFileName = "2016-01-04.txt";
    private String rawPath = MessageFormat.format("KKData\\data_{0}\\{1}", dayStr, rawFileName);
    private String outPath = MessageFormat.format("KKData\\data_{0}\\01_PrCar", dayStr);
    private String outFileNamePrex = MessageFormat.format("PrCarDataIn{0}", dayStr);

//    private String prCarTrajPath = "KKData\\"+dataOfDay+"\\PrCarTrajStayPointsIn0104_%5.json";
//    private String odPairSegs = "KKData\\"+dataOfDay+"\\ODPairSegs";
//    private String odPairSegs_new = "KKData\\"+dataOfDay+"\\ODPairSegs_new";
//    private String odPairSegs_new_kkGeo = "KKData\\"+dataOfDay+"\\ODPairSegs_new_kkGeo";

    @Override
    public void process() throws IOException {
        Component[] chain = new Component[]{
                new PrCarDataIn0101Collector(rawPath, outPath, outFileNamePrex),    //从一月份的卡口数据中收集日期01-01的私家车数据——01（result/PrCarDataIn0101.json）

//                new PrCarDataIn0101Collector(rawPath, prCarPath),    //从一月份的卡口数据中收集日期01-01的私家车数据——01（result/PrCarDataIn0101.json）
//                new PrCarTrajStayPointsCollector(prCarPath, prCarTrajPath),   //从私家车数据——01中，整理车每辆车在今日的轨迹点-02（result/PrCarTrajStay.json）
//                new PrCarODStayPointsCollector(prCarTrajPath, odPairSegs),   // 从每辆车今日的轨迹点数据-02中，整理车每辆车在各个时间的OD点-03（data/ODPair_01_01_seg_**.json）
//                new PrCarODStayPointsFormater(odPairSegs, odPairSegs_new),  // 格式规范化-03(data/ODPair_0101_seg_*.json)，得到-04(data/ODPair_0101_seg_01_new.json)
//                new KKId2KKGeo(odPairSegs_new, odPairSegs_new_kkGeo),  //将数据-04中，根据卡口ID，添加卡口lnglat数据(result/ODPair_0101_seg_01_new_01.json)
        };
        run(chain);
    }

    public static void main(String[] args) throws IOException {
        start(KKDataProcessorDriver.class);
    }
}
