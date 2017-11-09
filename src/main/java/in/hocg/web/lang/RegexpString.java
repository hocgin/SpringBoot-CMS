package in.hocg.web.lang;

/**
 * Created by hocgin on 2017/11/9.
 * email: hocgin@gmail.com
 */
public interface RegexpString {
    String NUMBER = "0-9";
    String LOWER_LETTER = "a-z";
    String UPPERCASE_LETTER = "A-Z";
    // 中文
    String CHINESE = "\\u4e00-\\u9fa5";
    
    
    /**
     * ^[0-9a-zA-Z\u4e00-\u9fa5\\._]+
     * 数字 字母 中文 . _
     *
     * @return
     */
    String N1 = String.format("^[%s%s%s%s\\._]+",
            NUMBER,
            LOWER_LETTER,
            UPPERCASE_LETTER,
            CHINESE);
    
    /**
     * ^[0-9a-zA-Z\\._]+
     * 数字 字母 . _
     * @return
     */
    static String get2() {
        return String.format("^[%s%s%s\\._]+",
                NUMBER,
                LOWER_LETTER,
                UPPERCASE_LETTER);
    }
}
