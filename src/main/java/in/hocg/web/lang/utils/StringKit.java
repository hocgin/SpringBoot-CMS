package in.hocg.web.lang.utils;

/**
 * Created by hocgin on 2017/11/19.
 * email: hocgin@gmail.com
 */
public class StringKit {
    /**
     * 获取文件后缀
     * @param fileName
     * @return
     */
    public static String getFileSuffix(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }
}
