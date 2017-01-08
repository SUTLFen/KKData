import Util.IndivisualPrCarInfo;
import Util.StayPoint;
import Util.StayPointInfo;
import com.alibaba.fastjson.JSON;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Fairy_LFen on 2017/1/7.
 */
public class ODDataProcessor {
    private List<StayPointInfo> list = null;
    private Map<String, IndivisualPrCarInfo> map = new HashMap<String, IndivisualPrCarInfo>();

    public void organizeIndivisualCarData(){
        readJSONToList();
        includeIndivisualCarInfo();
        writeCarInfoToFile();
    }

    private void writeCarInfoToFile() {
        try {
            String jsonStr = JSON.toJSONString(map, true);
            File file = new File("KKData\\indivisualPrCarInfo.json");
            if (!file.exists()) {
                file.createNewFile();
            }

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
            bw.append(jsonStr);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void includeIndivisualCarInfo() {
        for(int i = 0 ; i < list.size(); i++){
            StayPointInfo stayPointInfo = list.get(i);
            String plateNumber = stayPointInfo.getPlateNumber();

            if(map.isEmpty() || !map.containsKey(plateNumber)){
                IndivisualPrCarInfo prCarInfo = new IndivisualPrCarInfo(stayPointInfo);
                map.put(stayPointInfo.getPlateNumber(),  prCarInfo);
            }
            else{
                IndivisualPrCarInfo prCarInfo = map.get(plateNumber);
                StayPoint stayPoint = new StayPoint(stayPointInfo.getStayPointDate(), stayPointInfo.getKkid());
                prCarInfo.addStayPoint(stayPoint);
            }
        }
    }

    private void readJSONToList() {
        try {
            File prCarDataFile = new File("KKData\\PrCarData.json");
            InputStream inputstream = new FileInputStream(prCarDataFile);
            String text = IOUtils.toString(inputstream);
            list = JSON.parseArray(text, StayPointInfo.class);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        ODDataProcessor odDataProcessor = new ODDataProcessor();
        odDataProcessor.organizeIndivisualCarData();
    }
}
