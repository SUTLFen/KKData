package component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import common.AbstractComponent;
import org.apache.commons.io.IOUtils;
import util.*;

import java.io.*;
import java.util.*;

/**
 * Created by Fairy_LFen on 2017/1/10.
 *
 * 从文件"result/PrCarTrajStayPoints.json"中，整理出每辆私家车在这一天的轨迹点。
 * 结果存储在""。
 */
public class PrCarODStayPointsCollector extends AbstractComponent {

    private String rawParentPath = null;
    private String outParentPath = null;

    private Map<String, IndivisualPrCarInfo> map = new HashMap<String, IndivisualPrCarInfo>();

    private String fileNamePrex = "ODPair_0104_";
    private String suffix = null;


    public PrCarODStayPointsCollector(String rawParentPath, String outParentPath) {
        this.rawParentPath = rawParentPath;
        this.outParentPath = outParentPath;
    }

    @Override
    public void fire() {
        File rawFile = new File(rawParentPath);
        File[] files = rawFile.listFiles();
        for (File file :files) {
            this.includeCarODPairInfo(file);
        }
    }

    public void includeCarODPairInfo(File rawFile){
        Map<String, IndivisualPrCarInfo> map = readJSONToMap(rawFile);
        segStayPointToODPairs(map, rawFile);
    }

    private Map<String, IndivisualPrCarInfo> readJSONToMap(File file) {
        try {
            InputStream inputstream = new FileInputStream(file);
            String text = IOUtils.toString(inputstream);
            //String : 车牌号 ， IndivisualPrCarInfo : 经过的卡口点。
            Map<String, IndivisualPrCarInfo> map = JSON.parseObject(text, new TypeReference<Map<String, IndivisualPrCarInfo>>(){});
            return map;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void segStayPointToODPairs(Map<String, IndivisualPrCarInfo> map, File rawFile) {
            Set<String> carNameSet = map.keySet();
            List<CarODStayPoints> carODStayPoints_List = new ArrayList<CarODStayPoints>();

            int flag = 0;
        //对于每辆车。
            for(String carName : carNameSet){
//                flag ++;
//                if(flag % 4 == 0){
                    IndivisualPrCarInfo indivisualPrCarInfo = map.get(carName);
                    String plateNumber = indivisualPrCarInfo.getPlateNumber();
                    Map<String, ArrayList<StayPoint>> seg_map = splitTrajPointLstToDiffList(indivisualPrCarInfo.getStayPoints());
                    Map<String, StayPoint[]> odPair_map = toODPairs(seg_map);
                    writeODPairToDiffFile(plateNumber, odPair_map, rawFile);
//                }
            }
    }

    private void writeODPairToDiffFile(String plateNumber, Map<String, StayPoint[]> odPair_map, File rawFile) {
        try{
            String rawFileName = rawFile.getName();
            int index = rawFileName.indexOf(".");
            fileNamePrex = rawFileName.substring(0, index);

            Set<String> flag_set = odPair_map.keySet();
            Map<String, BufferedWriter> bw_map = new HashMap<String, BufferedWriter>();
            for(String flag : flag_set){
                String fileName = fileNamePrex + "_" + flag + ".json";
                String filePath = outParentPath +"\\"+ fileName;

                File curFile = toCurFile(filePath);

                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath, true)));
                ODPair odPair = new ODPair(plateNumber, odPair_map.get(flag));
                String jsonStr = JSON.toJSONString(odPair);

                bw.append(jsonStr+"\n");
                bw.flush();
                bw.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private File toCurFile(String filePath) throws IOException {
        File file = new File(filePath);
        if (!file.exists())
            file.createNewFile();
        return file;
    }

    private Map<String, StayPoint[]> toODPairs(Map<String, ArrayList<StayPoint>> seg_map) {
        Map<String, StayPoint[]> map = new HashMap<String, StayPoint[]>();
        Set<String> flag_set = seg_map.keySet();
        for(String flagStr : flag_set){
            ArrayList stayPoint_seglst = seg_map.get(flagStr);
            StayPoint[] od_stayPoints = ODStayPointsOf(stayPoint_seglst);
            map.put(flagStr, od_stayPoints);
        }
        return map;
    }

    private Map<String, ArrayList<StayPoint>> splitTrajPointLstToDiffList(List<StayPoint> stayPoints_lst) {

        Map<String, ArrayList<StayPoint>> resultMap = new HashMap<String, ArrayList<StayPoint>>();
        for(StayPoint stayPoint : stayPoints_lst){
            String timeStr = stayPoint.getTime();
            String flag = classifyToDiffSegment(timeStr);

            if(!resultMap.containsKey(flag)){
                ArrayList<StayPoint> list_tmp = new ArrayList<StayPoint>();
                list_tmp.add(stayPoint);
                resultMap.put(flag,list_tmp);
            }else{
                ArrayList list = resultMap.get(flag);
                list.add(stayPoint);
            }
        }


        return resultMap;
    }


    public String classifyToDiffSegment(String timeStr) {
        String[] strs = timeStr.split("-|\\s|:");
        String hourStr = strs[3];
        int hour = Integer.valueOf(hourStr);
        String flag = null;
        if(hour >= 6 && hour <= 10 )
        {
            flag = "seg_01";
        }else if(hour > 10 && hour <= 16){
            flag = "seg_02";
        }else if(hour > 16 && hour < 20){
            flag = "seg_03";
        }else if(hour >=20 && hour <= 23){
            flag = "seg_04";
        }
        return flag;
    }


    protected StayPoint[] ODStayPointsOf(ArrayList<StayPoint> seg_list) {
        DateUtil dateUtil = DateUtil.getInstance();

        StayPoint stayPoint_temp = seg_list.get(0);
        String time_temp = stayPoint_temp.getTime();
        long secds_temp = dateUtil.toUnixTimestamp(time_temp);

        long min = secds_temp,
                max = secds_temp;

        StayPoint o_staypoint = null, d_staypoint = null;

        for(StayPoint stayPoint : seg_list){
            String timeStr = stayPoint.getTime();
            long secds = dateUtil.toUnixTimestamp(timeStr);
            if(secds <= min) {
                min = secds;
                o_staypoint = stayPoint;
            }
            if(secds >= max){
                max = secds;
                d_staypoint = stayPoint;
            }
        }
        return new StayPoint[]{o_staypoint, d_staypoint};
    }


}
