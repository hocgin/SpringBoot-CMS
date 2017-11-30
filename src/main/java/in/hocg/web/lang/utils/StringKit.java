package in.hocg.web.lang.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hocgin on 2017/11/19.
 * email: hocgin@gmail.com
 */
public class StringKit {
    /**
     * 获取文件后缀
     *
     * @param fileName
     * @return
     */
    public static String getFileSuffix(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }
    
    /**
     * 以行方式读取
     *
     * @param text
     * @return
     */
    public static List<String> lines(String text) {
        List<String> lines = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new StringReader(text));
        try {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }
    
}
