
package kk.hourly.flow;

import com.alibaba.fastjson.JSON;
import component.PrCarDataIn0101Collector;
import util.DateUtil;
import util.FileUtil;
import util.StayPointInfo;

import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.*;

/**
 * Created by Fairy_LFen on 2017/6/28.
 */
public class KKHourlyFlowCaculator {
    private String rawPath = "KKData\\kk-hourly-flow\\RawData";
//    private String rawPath = "KKData\\data";
    private String prCarDataPath = "KKData\\kk-hourly-flow\\PrCarData";
    private String  hourlyFlowPath_Result = "KKData\\kk-hourly-flow\\HourlyFlowResult\\hourly_flow.json";

    Map<String, HourlyCount> hourlyCountMap = new HashMap<String, HourlyCount>();

    private FileUtil fileUtil = FileUtil.getInstance();
    private DateUtil dateUtil = DateUtil.getInstance();

    private void cal() throws IOException {
        collectPrCar();
        calHourlyFlow();
        writeMapToFile();
    }

//    将Map所有value写入到文件
    private void writeMapToFile() {



        List<HourlyCount> list = new ArrayList<HourlyCount>();
        Set<String> keyset = hourlyCountMap.keySet();
        for (String keyStr: keyset) {
            HourlyCount hourlyCount = hourlyCountMap.get(keyStr);
            list.add(hourlyCount);
        }

        String str = JSON.toJSONString(list, true);
        fileUtil.saveToFile(hourlyFlowPath_Result, str, false);
    }


    private void calHourlyFlow() throws IOException {
        File file = new File(prCarDataPath);
        File[] prCarFiles = file.listFiles();
        for (File prCarFile : prCarFiles) {
            calHourlyFlowByPrCarFile(prCarFile);
        }

    }

    private void calHourlyFlowByPrCarFile(File prCarFile) throws IOException {
       List<StayPointInfo> list = toStayPointInfoList(prCarFile);

       String stayPointDate_new;
       for (StayPointInfo pointItem : list) {
           stayPointDate_new = toStayPointDate_new(pointItem.getStayPointDate());
           if(hourlyCountMap.containsKey(stayPointDate_new)){
               HourlyCount hourlyCount = hourlyCountMap.get(stayPointDate_new);
               int count = hourlyCount.getCount();
               hourlyCount.setCount(++count);
           }else{
               HourlyCount hourlyCount = new HourlyCount(stayPointDate_new, 1);
               hourlyCountMap.put(stayPointDate_new, hourlyCount);
           }

        }
    }

    private String toStayPointDate_new(String stayPointDate) {
        long timeSecds = dateUtil.toUnixTimestamp(stayPointDate);  // "2016-06-01 00:59:00|| 23:00:00"
        int oneHour = 60*60*1000;
        String timeStr = dateUtil.getFormatTime(timeSecds + oneHour);
        String[] timeSegStrs = timeStr.split(":");
        String result = MessageFormat.format("{0}:00:00", timeSegStrs[0]);
        return result;
    }

    private List<StayPointInfo> toStayPointInfoList(File prCarFile) throws IOException {
        String jsonStr = fileUtil.readJsonFileToStr(prCarFile);
        List<StayPointInfo> list = JSON.parseArray(jsonStr, StayPointInfo.class);
        return list;
    }


    /**
     * @retun 返回“2016-01-01 00:00:00”  或者 “2016-01-1 01：00：00”
     * */
    private String toFilePrexName(File prCarFile) {
        return null;
    }

    public void collectPrCar(){
        File file = new File(rawPath);
        File[] files = file.listFiles();

        for (File rawDataFile :files) {
                new PrCarDataIn0101Collector(rawDataFile.getAbsolutePath(), prCarDataPath).fire();
        }
    }

    public static void main(String[] args) throws IOException {
        new KKHourlyFlowCaculator().cal();
    }
}
