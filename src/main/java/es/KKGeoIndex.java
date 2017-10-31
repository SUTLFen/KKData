package es;

import com.alibaba.fastjson.JSON;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.transport.TransportClient;
import util.ESUtil;
import util.FileUtil;
import util.KKInfo;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fairy_LFen on 2017/2/12.
 */
public class KKGeoIndex {
    private String kkGeoPath = "KKData//data//kk_info.txt";
    private String outDataPath = "KKData//data//kk_info.json";

    private FileUtil fileUtil = FileUtil.getInstance();
    private ESUtil esUtil = ESUtil.getInstance();

    private TransportClient client = null;
    private IndexResponse response = null;


    public void indexKKGeo() throws IOException {
        File kkGeoFile = new File(kkGeoPath);
        BufferedReader br = fileUtil.getBufferedReader(kkGeoFile, "GBK");
        String line = br.readLine();

        client = esUtil.getTransportClient();

        String mapping_str = "{\n" +
                "    \"kkInfo_type_new\": { \n" +
                "      \"_all\":       { \"enabled\": false  }, \n" +
                "      \"properties\": { \n" +
                "        \"id\":    { \"type\": \"keyword\"  }, \n" +
                "        \"kkId\":     { \"type\": \"keyword\"  }, \n" +
                "        \"fxbh\"  :   { \"type\": \"keyword\" },\n" +
                "        \"kkType\"  :   { \"type\": \"keyword\" },\n" +
                "        \"kkName\": {\n" +
                "            \"type\": \"text\",\n" +
                "            \"analyzer\": \"ik_max_word\",\n" +
                "            \"search_analyzer\": \"ik_max_word\",\n" +
                "            \"include_in_all\": \"true\",\n" +
                "            \"boost\": 8\n" +
                "        },\n" +
                "        \"lng\": {\"type\": \"keyword\"},\n" +
                "        \"lat\": {\"type\": \"keyword\"}\n" +
                "        }\n" +
                "      }\n" +
                "    }";
        client.admin().indices().prepareCreate("kkinfo_index_new")
                .addMapping("kkInfo_type_new", mapping_str)
                .get();

        while((line = br.readLine()) != null){
            String kKJsonStr = converToJSONStr(line);
            response = client.prepareIndex("kkinfo_index_new", "kkInfo_type_new")
                .setSource(kKJsonStr)
                .get();
        }

        client.close();
    }

    public void kkGeoToJSONFile() throws IOException {
        File kkGeoFile = new File(kkGeoPath);
        BufferedReader br = fileUtil.getBufferedReader(kkGeoFile, "GBK");
        String line = br.readLine();

        List<KKInfo> kkinfo_list = new ArrayList<KKInfo>();
        while((line = br.readLine()) != null){
            String[] str_arry = line.split(";");
            KKInfo kkInfo = new KKInfo(str_arry);
            if(kkInfo.getLat().equals("") || kkInfo.getLng().equals("")){
                continue;
            }else{
                kkinfo_list.add(kkInfo);
            }
        }
        String jsonStr = JSON.toJSONString(kkinfo_list);
        fileUtil.saveToFile(outDataPath, jsonStr, true);
    }
    private String converToJSONStr(String line) {
        String[] str_arry = line.split(";");
        KKInfo kkInfo = new KKInfo(str_arry);
        String jsonStr = JSON.toJSONString(kkInfo);
        return jsonStr;
    }

    public static void main(String[] args) throws IOException {
            new KKGeoIndex().indexKKGeo();
//            new KKGeoIndex().kkGeoToJSONFile();
    }
}

