import Util.StayPointInfo;
import com.alibaba.fastjson.JSON;

import java.io.*;
import java.util.*;

/**
 * Created by Fairy_LFen on 2017/1/7.
 */
public class PrCarDataIn0101Collector {

    private String dataPath = "KKData\\kkData-2016-1.txt";

    public void DataIn0101Collector(){
        initDataPath();
        ClassifyDailyPrCarToFile();
        collectPrCarDataIn0101();
    }

    private void ClassifyDailyPrCarToFile() {
        try{
            File rawDataFile = new File(dataPath);
            File outFile = new File("KKData\\PrCarData.json");
            if(!outFile.exists()){ outFile.createNewFile();}
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(rawDataFile)));
            Map<String, BufferedWriter> bw_map = new HashMap<String, BufferedWriter>();

            String line = null;
            line = br.readLine();

            while((line= br.readLine()) != null){
                String[] info_arry = line.split(";");
                String dateStr = info_arry[6];
                String fileName = toFileName(dateStr);

                if(!bw_map.containsKey("fileName")){
                    File newFile = new File("data\\"+fileName);
                    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(newFile)));
                    bw_map.put(fileName, bw);
                    bw.append(line+"\n");
                    bw.flush();
                }
                else{
                    BufferedWriter bw = bw_map.get(fileName);
                    bw.append(line+"\n");
                    bw.flush();
                }
            }
            br.close();
            closeBw_map(bw_map);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void closeBw_map(Map<String,BufferedWriter>  bw_map) {
        try{
            Set<String> keyset = bw_map.keySet();
            for (String fileName : keyset){
                BufferedWriter bw = bw_map.get(fileName);
                bw.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String toFileName(String dateStr) {
        String[] dateItems = dateStr.split("-|\\s");
        String yearStr = dateItems[0];
        String monthStr = dateItems[1];
        String dayStr = dateItems[2];
        return yearStr+"-"+monthStr+"-"+dayStr;
    }

    private void collectPrCarDataIn0101() {
        try {
            File rawDataFile = new File(dataPath);
            File outFile = new File("KKData\\PrCarData.json");
            if(!outFile.exists()){ outFile.createNewFile();}
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(rawDataFile)));
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile)));

            String line = null;
            line = br.readLine();
            List<StayPointInfo> list = new ArrayList<StayPointInfo>();
            while((line= br.readLine()) != null){
                String[] info_arry = line.split(";");

                if(isIn0101(info_arry)){
                    if(isPrivateCar(info_arry)){
                        StayPointInfo stayPointInfo = new StayPointInfo(info_arry);
                        list.add(stayPointInfo);
                    }
                }
            }
            String jsonStr = JSON.toJSONString(list, true);
            bw.append(jsonStr);
            bw.flush();

            br.close();
            bw.close();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean isIn0101(String[] info_arry) {
        String dateStr = info_arry[6];
        String[] dateItems = dateStr.split("-|\\s");
        String monthStr = dateItems[1];
        String dayStr = dateItems[2];

        String dateTemp = monthStr+"-"+dayStr;

        if(dateTemp.equals("01-01")) {
            return true;
        }
        else return false;
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
        new PrCarDataIn0101Collector().DataIn0101Collector();
    }
    private void initDataPath(){
            File file = new File("KKData\\kkData-2016-1.txt");
            dataPath = file.getAbsolutePath();
        System.out.println("dataPath : " + dataPath);
    }
}
