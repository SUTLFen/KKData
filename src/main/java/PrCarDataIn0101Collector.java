import Util.StayPointInfo;
import com.alibaba.fastjson.JSON;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fairy_LFen on 2017/1/7.
 */
public class PrCarDataIn0101Collector {

    private String dataPath = null;

    public void DataIn0101Collector(){
        initDataPath();
        collectPrCarDataIn0101();
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
                String licensePlateColor = info_arry[2];
                String plateNumber = info_arry[5];
                String dateStr = info_arry[6];
//                String dateStr_02 = info_arry[7];
//                if(!dateStr.equals(dateStr_02)){
//                    System.out.println(dateStr + "    " + dateStr_02);
//                    break;
//                }
                if(isPrivateCar(licensePlateColor, plateNumber, dateStr)){
                    StayPointInfo stayPointInfo = new StayPointInfo(info_arry);
                    list.add(stayPointInfo);
                }
                else{
                    continue;
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

    private boolean isPrivateCar(String licensePlateColor, String plateNumber, String dateStr) {
        String[] dateItems = dateStr.split("-|\\s");
        String monthStr = dateItems[1];
        String dayStr = dateItems[2];

        String dateTemp = monthStr+"-"+dayStr;

        if(dateTemp.equals("01-01")){
            if(licensePlateColor.equals("01") && plateNumber.startsWith("浙A") && !plateNumber.startsWith("浙AT") &&
                    !plateNumber.endsWith("警")&&
                    !plateNumber.endsWith("学"))
            {
                return true;
            }
            else{
                return false;
            }
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
