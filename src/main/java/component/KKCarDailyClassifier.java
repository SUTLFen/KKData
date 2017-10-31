package component;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by Fairy_LFen on 2017/1/10.
 */
public class KKCarDailyClassifier {

    private String dataPath = "KKData\\kkData-2016-1.txt";

    public void ClassifyDailyPrCarToFile(){
        try{
            File rawDataFile = new File(dataPath);
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(rawDataFile), "GBK"));
//            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(rawDataFile)));
            Map<String, BufferedWriter> bw_map = new HashMap<String, BufferedWriter>();

            String line = null;
            line = br.readLine();

            while((line= br.readLine()) != null){
                String[] info_arry = line.split(";");
                String dateStr = info_arry[6];

                String fileName = toFileName(dateStr) + ".txt";

                BufferedWriter bw = null;
                if(!bw_map.containsKey(fileName)){
                    File newFile = new File("KKData\\data\\"+fileName);
                    if (!newFile.exists()) newFile.createNewFile();
//                    bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(newFile), "GBK"));
                    bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(newFile)));
                    bw_map.put(fileName, bw);
                }
                else{
                    bw = bw_map.get(fileName);
                }

                bw.append(line+"\n");
                bw.flush();
            }
            br.close();
            closeBw_map(bw_map);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
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

    public static void main(String[] args) {
        new KKCarDailyClassifier().ClassifyDailyPrCarToFile();
    }
}
