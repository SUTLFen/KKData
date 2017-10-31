package component;

import com.alibaba.fastjson.JSON;
import common.AbstractComponent;
import util.FileUtil;
import util.StayPointInfo;

import java.io.*;
import java.text.MessageFormat;
import java.util.*;

/**
 * Created by Fairy_LFen on 2017/1/7.
 *
 * 从文件"data/2016-01-10.txt"中，整理出在2016-01-01的私家车数据。。。
 * 结果数据存储在“result/PrCarDataIn0101.json”中
 */
public class PrCarDataIn0101Collector extends AbstractComponent {

    private String rawPath;
    private String outParentPath;
    private String outFilePrexName;
    private int flag;
    private final int NUM = 5;
    private Map<String, List<StayPointInfo>> map = new HashMap<>();

    private FileUtil fileUtil = FileUtil.getInstance();

    public PrCarDataIn0101Collector() {
    }

    public PrCarDataIn0101Collector(String rawPath, String outPath) {
        this.rawPath = rawPath;
        this.outParentPath = outPath;
    }

    public PrCarDataIn0101Collector(String rawPath, String outPath, String outFileNamePrex) {
        this.rawPath =rawPath;
        this.outParentPath = outPath;
        this.outFilePrexName = outFileNamePrex;
    }

    @Override
    public void fire(){
        initMap();
        collectPrCarDataIn0101();
    }

    private void initMap() {
        for (int i = 0; i < NUM; i++) {
            List<StayPointInfo> list = new ArrayList<StayPointInfo>();
            map.put(i+"", list);
        }
    }

    public void collectPrCarDataIn0101() {
        try{
            File rawDataFile = new File(rawPath);
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(rawDataFile)));

            String rawFileName = rawDataFile.getName();
            int index = rawFileName.indexOf(".");
            outFilePrexName = rawFileName.substring(0, index);

            String line = null;
            List<StayPointInfo> list ;

            int flag = 0;
            while((line= br.readLine()) != null){
                String[] info_arry = line.split(";");
                if(isPrivateCar(info_arry)){
                    StayPointInfo stayPointInfo = new StayPointInfo(info_arry);
                    int tmp = flag % 5;
                    list = map.get(tmp + "");
                    list.add(stayPointInfo);
                    flag ++ ;
                }
            }

            writeMapToFile();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void writeMapToFile() {
        Set<String> flagSet = map.keySet();
        List<StayPointInfo> list = null;

        String outPath = null;

        for (String flagStr : flagSet) {
            list = map.get(flagStr);
            outPath = MessageFormat.format("{0}\\{1}_%5_{2}.json",
                    new Object[]{outParentPath, outFilePrexName, flagStr});
            String str = JSON.toJSONString(list);

            fileUtil.saveToFile(outPath, str, false);
        }
    }

    private boolean isPrivateCar(String[] info_arry) {
        String licensePlateColor = info_arry[2];
        String plateNumber = info_arry[5];
        if(licensePlateColor.equals("01") && plateNumber.startsWith("浙A") && !plateNumber.startsWith("浙AT") &&
                !plateNumber.endsWith("警")&&
                !plateNumber.endsWith("学")) {
             return true;
        }
        else{
            return false;
        }
    }

    public static void main(String[] args) {
        new PrCarDataIn0101Collector().collectPrCarDataIn0101();
    }
}
