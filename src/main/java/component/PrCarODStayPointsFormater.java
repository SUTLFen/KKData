package component;

import com.alibaba.fastjson.JSON;
import common.AbstractComponent;
import util.FileUtil;
import util.ODPair;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fairy_LFEn on 2017/2/4/0004.
 */
public class PrCarODStayPointsFormater extends AbstractComponent {

    private FileUtil fileUtil = FileUtil.getInstance();

    private String rawParentPath;
    private String outParentPath;


    public PrCarODStayPointsFormater(String odPairSegs, String odPairSegs_new) {
        this.rawParentPath = odPairSegs;
        this.outParentPath = odPairSegs_new;
    }

    @Override
    public void fire() {
        File file = new File(rawParentPath);
        File[] files = file.listFiles();

        for (File fileTmp: files) {
            this.format(fileTmp);
        }
    }

    public void format(File rawFile){
        try{
            List<ODPair> odPairList = new ArrayList<ODPair>();

            BufferedReader br =  fileUtil.getBufferedReader(rawFile);
            String line;
            while((line = br.readLine()) != null){
                ODPair odPair = JSON.parseObject(line, ODPair.class);
                odPairList.add(odPair);
            }
            String jsonStr = JSON.toJSONString(odPairList, true);
            String outPath = toOutPath(outParentPath, rawFile);
            fileUtil.saveToFile(outPath, jsonStr, false);

        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String toOutPath(String outParentPath, File rawFile) {
        String rawFileName = rawFile.getName();
        int index = rawFileName.indexOf(".");
        String fileName = rawFileName.substring(0, index);
        String outPath = MessageFormat.format("{0}\\{1}_new.json",
                new Object[]{outParentPath, fileName});
        return outPath;
    }

    public static void main(String[] args) {
        File file = new File("test.json");
        String fileName = file.getName();
        int index = fileName.indexOf(".");
        System.out.println(fileName.substring(0, index));
    }


}
