package component;

import com.alibaba.fastjson.JSON;
import common.AbstractComponent;
import org.apache.commons.io.IOUtils;
import util.IndivisualPrCarInfo;
import util.StayPoint;
import util.StayPointInfo;

import java.io.*;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Fairy_LFen on 2017/1/10.
 *
 * 从文件"result/PrCarDataIn0101.json"中，整理出每辆私家车在这一天的轨迹点。
 * 结果存储在"result/PrCarTrajStayPoints.json"。
 */
public class PrCarTrajStayPointsCollector extends AbstractComponent {

    private String rawParentPath = null;
    private String outParentPath = null;

    public PrCarTrajStayPointsCollector(String rawParentPath, String outParentPath) {
        this.rawParentPath = rawParentPath;
        this.outParentPath = outParentPath;
    }

    @Override
    public void fire() {
        File rawFile = new File(rawParentPath);
        File[] files = rawFile.listFiles();
        for (File file :files) {
            this.includeIndivisualCarInfo(file);
        }
    }

    public void includeIndivisualCarInfo(File curFile) {
        List<StayPointInfo> list = readJSONToList(curFile);
        Map<String, IndivisualPrCarInfo> map = new HashMap<String, IndivisualPrCarInfo>();

        for(int i = 0 ; i < list.size(); i++){
            StayPointInfo stayPointInfo = list.get(i);
            String plateNumber = stayPointInfo.getPlateNumber();

            if(map.isEmpty() || !map.containsKey(plateNumber)){
                IndivisualPrCarInfo prCarInfo = new IndivisualPrCarInfo();
                prCarInfo.setPlateNumber(plateNumber);
                List<StayPoint> list02 = prCarInfo.getStayPoints();
                list02.add(new StayPoint(stayPointInfo));

                map.put(stayPointInfo.getPlateNumber(),  prCarInfo);
            }
            else{
                IndivisualPrCarInfo prCarInfo = map.get(plateNumber);
                StayPoint stayPoint = new StayPoint(stayPointInfo);
                prCarInfo.addStayPoint(stayPoint);
            }
        }
        writeCarInfoToFile(map, curFile.getName());
    }

    public void writeCarInfoToFile(Map<String, IndivisualPrCarInfo> map, String prefixName) {
        try {
            String jsonStr = JSON.toJSONString(map, true);
            String outPath = toOutPath(outParentPath,prefixName);
            File file = new File(outPath);
            if (!file.exists()) {
                file.createNewFile();
            }

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
            bw.append(jsonStr);
            bw.flush();

            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String toOutPath(String outParentPath, String prefixName) {
        int index = prefixName.indexOf(".");
        String fileName = prefixName.substring(0, index)+"_Traj.json";
        String str = MessageFormat.format("{0}\\{1}", new Object[]{outParentPath, fileName});
        return str;
    }

    private List<StayPointInfo> readJSONToList(File prCarDataFile) {
        try {
            InputStream inputstream = new FileInputStream(prCarDataFile);
            String text = IOUtils.toString(inputstream);
            List<StayPointInfo> list = JSON.parseArray(text, StayPointInfo.class);
            return list;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
