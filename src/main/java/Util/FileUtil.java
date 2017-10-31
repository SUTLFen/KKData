package util;
import org.apache.commons.io.IOUtils;

import java.io.*;

/**
 * Created by Fairy_LFen on 2017/1/14.
 */
public class FileUtil {
    private static FileUtil instance;

    public static FileUtil getInstance(){
        if(instance == null){
            instance = new FileUtil();
            return instance;
        }
        else{
            return instance;
        }
    }

    /**
     * 将文本内容写入文件
     *
     * @param filePath : 文件相对路径
     * @param content : 写入内容
     * @param b : 是否追加在文件末尾
     * */
    public void saveToFile(String filePath, String content, boolean b) {
        try{
            File file = new File(filePath);
            if(!file.exists()){
                file.createNewFile();
            }
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, b)));
            bw.append(content);
            bw.flush();
            bw.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public BufferedReader getBufferedReader(File file, String decodeType){
        try{
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), decodeType));
            return br;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }
    public BufferedReader getBufferedReader(File file) throws FileNotFoundException {
        BufferedReader br =   new BufferedReader(new InputStreamReader(new FileInputStream(file)));
        return br;
    }

    /**
     * 将文件所有内容写入字符串
     * @param file : 文件
     * @return  : 文件所有内容
     * */
    public String toJsonStr(File file) throws IOException {
        InputStream inputstream = new FileInputStream(file);
        String text = IOUtils.toString(inputstream);
        return text;
    }

    public String readJsonFileToStr(File file) throws IOException {
        InputStream inputstream = new FileInputStream(file);
        String text = IOUtils.toString(inputstream);
        return text;
    }
}






