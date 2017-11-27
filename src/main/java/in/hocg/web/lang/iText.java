package in.hocg.web.lang;

import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by hocgin on 2017/11/8.
 * email: hocgin@gmail.com
 */
@Component("iText")
public class iText {
    
    /**
     * 警告信息
     * - @iText.danger('暂无')
     *
     * @param text
     * @return
     */
    public String danger(String text) {
        return String.format("<span class=\"text-danger\">%s</span>", text);
    }
    
    /**
     * 表格文字居中
     *
     * @param colspan
     * @param text
     * @return
     */
    public String trCenter(int colspan, String text) {
        return String.format("<tr ><td class=\"text-center\" colspan=\"%d\">%s</td></tr>", colspan, text);
    }
    
    /**
     * 格式化时间
     *
     * @param date
     * @return
     */
    public String format(Date date) {
        return ObjectUtils.isEmpty(date) ? danger("暂无") : new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
    }
    
    /**
     * 开启状态
     * @param available
     * @return
     */
    public String available(boolean available) {
        return String.format("<i class=\"fa fa-circle %s\"></i>", available ? "text-success" : "text-danger");
    }
}
