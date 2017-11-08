package in.hocg.web.lang;

import org.springframework.stereotype.Component;

/**
 * Created by hocgin on 2017/11/8.
 * email: hocgin@gmail.com
 */
@Component("iText")
public class HtmlUtils {
    
    /**
     * 警告信息
     * - @iText.danger('暂无')
     * @param text
     * @return
     */
    public String danger(String text){
        return String.format("<span class=\"text-danger\">%s</span>", text);
    }
    
    
    public String trCenter(int colspan, String text) {
        return String.format("<tr ><td class=\"text-center\" colspan=\"%d\">%s</td></tr>", colspan, text);
    }
}
