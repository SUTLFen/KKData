package od.regions;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import dao.KKInfoNew;
import dao.ODPairNew;
import util.FileUtil;
import util.ODPair;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fairy_LFen on 2017/6/28.
 */
public class ODInRegionsGenerator {

    private String dataStr = "data_0104", segStr = "seg_03";   //需要更改的地方

    private String rawPath_ODPair = "KKData\\od-regions\\"+dataStr+"\\"+ segStr;
    private String outPath_ODInRegions = "KKData\\od-regions\\"+dataStr+"\\result\\ODPairIn0104_"+segStr+".json";   //更改月日

    private String rawPath_dbclustResult = "GeotagGraph\\src\\main\\webapp\\data\\spatial\\kk_dbscan_result_20170906_v1.json";

    private FileUtil fileUtil = FileUtil.getInstance();

    public void generateODInRegions() throws IOException {
        ArrayList<ArrayList<KKInfoNew>> kkClusters = toKKClusters(rawPath_dbclustResult);
        ArrayList<ArrayList<String>> kkClusters_new = toKKClustersNew(kkClusters);

        int size = kkClusters.size();
        ODPairNew[][] ODWeight_Arry = initODPairNewArry(size);

        File odPairParentFile = new File(rawPath_ODPair);
        File[] odPairFiles = odPairParentFile.listFiles();

        for (File odPairFile : odPairFiles) {
            calODFlowWeightInRegions(odPairFile, kkClusters_new, ODWeight_Arry);
        }

        printResultArry(ODWeight_Arry);

        String jsonStr = JSON.toJSONString(ODWeight_Arry, true);
        fileUtil.saveToFile(outPath_ODInRegions, jsonStr, false);
    }

    private ArrayList<ArrayList<String>> toKKClustersNew(ArrayList<ArrayList<KKInfoNew>> kkClusters) {

        ArrayList<KKInfoNew> cluster_old = null;
        ArrayList<ArrayList<String>> kkCluster_New = new ArrayList<ArrayList<String>>();
        ArrayList<String> cluster_new = null;

        KKInfoNew kkInfoNew = null;

        for (int i = 0; i < kkClusters.size(); i++) {
            cluster_old = kkClusters.get(i);
            cluster_new = new ArrayList<String>();
            for (int j = 0; j < cluster_old.size(); j++) {
                kkInfoNew = cluster_old.get(j);
                cluster_new.add(kkInfoNew.getKkId());
            }
            kkCluster_New.add(cluster_new);
        }
        return kkCluster_New;
    }

    /**
     * 1. 将ODPair中起始点，找到对应的Region；
     * 2. 根据Region，对应的ODWeight_arry加一。
     */
    private void calODFlowWeightInRegions(File odPairFile, ArrayList<ArrayList<String>> kkClusters_new,
                                          ODPairNew[][] odWeight_arry) throws IOException {
        List<ODPair> odPairList = toODPairList(odPairFile);
        int[] indexArry;
        for (ODPair odPair : odPairList) {
            indexArry = findReginIndexByODPair(kkClusters_new, odPair);
            if (indexArry != null) {
                int rowIndex = indexArry[0];
                int clumIndex = indexArry[1];
                 ODPairNew odPairNewTmp = odWeight_arry[rowIndex][clumIndex];
                 int weight = odPairNewTmp.getWeight();
                 odPairNewTmp.setWeight(++weight);
            }else{
                continue;
            }
        }
    }

    private int[] findReginIndexByODPair(ArrayList<ArrayList<String>> kkClusters_new, ODPair odPair) {
        int[] indexArry = new int[2];
        String o_kkid = odPair.getO_kkid();
        String d_kkid = odPair.getD_kkid();

        ArrayList<String> cluster = null;
        for (int i = 0; i < kkClusters_new.size(); i++) {
            cluster = kkClusters_new.get(i);
            if (cluster.contains(o_kkid)) {
                indexArry[0] = i;
            }
            if (cluster.contains(d_kkid)) {
                indexArry[1] = i;
            }
        }

        if (indexArry[0] >= 0 && indexArry[1] >= 0) {
            return indexArry;
        } else {
            return null;
        }
    }

    private List<ODPair> toODPairList(File odPairFile) throws IOException {
        String jsonStr = fileUtil.readJsonFileToStr(odPairFile);
        List<ODPair> list = JSON.parseArray(jsonStr, ODPair.class);
        return list;
    }

    private void printResultArry(ODPairNew[][] resultArry) {
        System.out.println("----------------------------result------------------------------");
        for (int i = 0; i < resultArry.length; i++) {
            for (int j = 0; j < resultArry[i].length; j++) {
                System.out.print(resultArry[i][j].getWeight() + "  ");
            }
            System.out.println();
        }
    }

    private ODPairNew[][] initODPairNewArry(int size) {
        ODPairNew[][] odPairNewArry = new ODPairNew[size][size];
        for (int i = 0; i < odPairNewArry.length; i++) {
            for (int j = 0; j < odPairNewArry.length; j++) {
                odPairNewArry[i][j] = new ODPairNew();
                odPairNewArry[i][j].setO_regin_index(i);
                odPairNewArry[i][j].setD_region_index(j);
            }
        }
        return odPairNewArry;
    }

    protected ArrayList<ArrayList<KKInfoNew>> toKKClusters(String rawPath_dbclustResult) throws IOException {
        String jsonStr = fileUtil.readJsonFileToStr(new File(rawPath_dbclustResult));
        ArrayList<ArrayList<KKInfoNew>> kkClusters = JSON.parseObject(jsonStr,
                new TypeReference<ArrayList<ArrayList<KKInfoNew>>>() {
                });
        return kkClusters;
    }

    public static void main(String[] args) throws IOException {
        new ODInRegionsGenerator().generateODInRegions();
    }
}
