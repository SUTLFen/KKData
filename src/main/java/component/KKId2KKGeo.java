package component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import common.AbstractComponent;
import dao.KKInfoNew;
import org.elasticsearch.client.transport.TransportClient;
import util.ESUtil;
import util.FileUtil;
import util.GeoDistance;
import util.ODPair;

import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fairy_LFen on 2017/2/12.
 */
public class KKId2KKGeo extends AbstractComponent {

    private TransportClient client = null;
    private ESUtil esUtil = ESUtil.getInstance();

    private String rawDataPath = "KKData\\data_0104\\ODPair_0104_seg_02_new.json";
    private String outDataPath = "KKData\\data_0104\\ODPair_0104_seg_02_new_01.json";
    private String rawParentPath;
    private String outParentPath;

    private String clustCentersPath = "KKData\\data_0104\\kk_dbscan_centers.json";

//    private String kkInfoNewPath = "KKData\\kkInfoData\\kk_dbscan_result.json";
    private String kkInfoNewPath = "KKData\\kkInfoData\\kk_dbscan_result_20170627_v1.json";
    private ArrayList<ArrayList<KKInfoNew>> clusters = null;
    private ArrayList<KKInfoNew> centers = null;

    private FileUtil fileUtil = FileUtil.getInstance();
    private GeoDistance geoDistance = GeoDistance.getinstance();

    public KKId2KKGeo() {
    }

    public KKId2KKGeo(String odPairSegs_new, String odPairSegs_new_kkGeo) throws IOException {
        this.rawDataPath = odPairSegs_new + "\\ODPair_0104_seg_02_new.json";
        this.outDataPath = odPairSegs_new_kkGeo + "\\ODPair_0104_seg_02_new_kkGeo.json";

        this.clusters = toClusts(kkInfoNewPath);
        this.centers = findClustCenters(clusters);

        String str = JSON.toJSONString(centers);
        fileUtil.saveToFile(clustCentersPath, str, false);
    }

    public KKId2KKGeo(String odPairSegs_new, String odPairSegs_new_kkGeo, int i) throws IOException {
        this.rawParentPath = odPairSegs_new;
        this.outParentPath = odPairSegs_new_kkGeo;

        this.clusters = toClusts(kkInfoNewPath);
        this.centers = findClustCenters(clusters);

        String str = JSON.toJSONString(centers);
        fileUtil.saveToFile(clustCentersPath, str, false);
    }

    @Override
    public void fire() {
        try{
            File file = new File(rawParentPath);
            File[] files = file.listFiles();

            for (File rawFile : files) {
                this.convertKKId2KKGeo(rawFile);
            }

        }catch(IOException e){
            e.printStackTrace();
        }
    }

    private ArrayList<ArrayList<KKInfoNew>> toClusts(String rawPath) throws IOException {
        File clustsFile = new File(rawPath);
        String str = fileUtil.readJsonFileToStr(clustsFile);
        ArrayList<ArrayList<KKInfoNew>> result = JSON.parseObject(str, new TypeReference<ArrayList<ArrayList<KKInfoNew>>>(){});
        return result;
    }

    private ArrayList<KKInfoNew> findClustCenters(ArrayList<ArrayList<KKInfoNew>> cluster) {
        ArrayList<KKInfoNew> centers = new ArrayList<>();
        for (ArrayList<KKInfoNew> clust : cluster){
            KKInfoNew center = findCenter(clust);
            centers.add(center);
        }
        return centers;
    }

    private KKInfoNew findCenter(ArrayList<KKInfoNew> clust) {
        double minSum = 9999f;
        KKInfoNew center = null;
        KKInfoNew kkInfoNew_cur = null;
        for (int i = 0; i < clust.size(); i++) {
            kkInfoNew_cur = clust.get(i);
            double sum = 0.0;
            for (int j = 0; j < clust.size() && j != i; j++) {
                KKInfoNew kkInfoNew_02 = clust.get(j);
                double lat_01 = Double.valueOf(kkInfoNew_cur.getLat());
                double lng_01 = Double.valueOf(kkInfoNew_cur.getLng());
                double lat_02 = Double.valueOf(kkInfoNew_02.getLat());
                double lng_02 = Double.valueOf(kkInfoNew_02.getLng());
                double disTmp = geoDistance.GetDistance(lat_01, lng_01, lat_02, lng_02);
                sum += disTmp;
            }
            if (sum < minSum) {
                minSum = sum;
                center = clust.get(i);
            }
        }
        return center;
    }

    public void convertKKId2KKGeo(File rawFile) throws IOException {

        String jsonStr = fileUtil.toJsonStr(rawFile);
        List<ODPair> odPairs_list = JSON.parseArray(jsonStr, ODPair.class);

        int i = 0;
        System.out.println("list.size() : " + odPairs_list.size());

        client = esUtil.getTransportClient();

        List<ODPair> odPair_list_new = new ArrayList<ODPair>();
        for (ODPair odPair_item : odPairs_list) {
            String d_kkid = odPair_item.getD_kkid();
            String d_direction = odPair_item.getD_direction();

            String o_kkid = odPair_item.getO_kkid();
            String o_direction = odPair_item.getO_direction();

//            String d_lnglat = searchLnglatByKKId(d_kkid, d_direction);
//            String o_lnglat = searchLnglatByKKId(o_kkid, o_direction);

            String d_lnglat = toClustCenterLngLat(d_kkid);
            String o_lnglat = toClustCenterLngLat(o_kkid);

            if(d_lnglat != null && o_lnglat != null){
                odPair_item.setD_lnglat(d_lnglat);
                odPair_item.setO_lnglat(o_lnglat);

                if( d_lnglat.equals(o_lnglat) ){
                    continue;
                }else{
                    odPair_list_new.add(odPair_item);
                }
            }

        }
        client.close();

        String resultJSONList = JSON.toJSONString(odPair_list_new, false);
        String outDataPath = toOutDataPath(outParentPath, rawFile);
        fileUtil.saveToFile(outDataPath, resultJSONList, false);
    }

    private String toOutDataPath(String outParentPath, File rawFile) {
        String rawFileName = rawFile.getName();
        int index = rawFileName.indexOf(".");
        String outPath = MessageFormat.format("{0}\\{1}_kkGeo.json",
                new Object[]{outParentPath, rawFileName.substring(0, index)});
        return outPath;
    }

    private String toClustCenterLngLat(String kkid) {
        ArrayList<KKInfoNew> clust = null;
        int flag = -1;
        for (int i = 0; i < clusters.size(); i++) {
            clust = clusters.get(i);
            for (KKInfoNew item: clust) {
                String kkidTmp = item.getKkId();
                if (kkidTmp.equals(kkid)){
                    flag = i;
                    break;
                }
            }
        }

        if( flag != -1){
            KKInfoNew target = centers.get(flag);
            return target.getLng() +","+  target.getLat();
        }
        else return null;
    }

//    private String searchLnglatByKKId(String kkid, String direction) {
////        QueryBuilder qb = QueryBuilders.queryStringQuery(kkid);
//        System.out.println(kkid +" "+ direction);
//
////        QueryBuilder qb = QueryBuilders.termQuery("kkId", kkid);
//        QueryBuilder qb =  QueryBuilders.boolQuery()
//                .must(QueryBuilders.termQuery("kkId", kkid))
//                .must(QueryBuilders.termQuery("fxbh", direction));
//
//        SearchResponse searchResponse = client.prepareSearch("kkinfo_index")
//                .setTypes("kkInfo_type")
//                .setQuery(qb)
//                .execute()
//                .actionGet();
//
//        SearchHit[] hit_arry = searchResponse.getHits().getHits();
//        String resultStr ;
//        if(hit_arry.length > 0){
//            Map<String, Object> source_map = hit_arry[0].getSource();
//            String latStr = (String) source_map.get("lat");
//            String lngStr = (String) source_map.get("lng");
//            if(latStr.equals("") || lngStr.equals("")){
//                resultStr = null;
//            }
//            else{
//                resultStr = lngStr+","+latStr;
//            }
//        } else resultStr = null;
//        return resultStr;
//    }

    public static void main(String[] args) throws IOException {
    }
}
