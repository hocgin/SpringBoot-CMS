package in.hocg.web.lang;

import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by hocgin on 2017/11/23.
 * email: hocgin@gmail.com
 */
public class DateKit {
    public static Date getTodayStart() {
        Date date = new Date();
        date.setHours(0);
        date.setMinutes(0);
        date.setSeconds(0);
        return date;
    }
    
    /**
     * 两小时内
     *
     * @return
     */
    public static Date get2HoursStart() {
        Date date = new Date();
        int hours = date.getHours() - 2;
        date.setHours(hours < 0 ? 0 : hours);
        return date;
    }
    
    public static Date getTodayEnd() {
        Date date = new Date();
        date.setHours(23);
        date.setMinutes(59);
        date.setSeconds(59);
        return date;
    }
    
    public static Date format(String dateStr) {
        if (StringUtils.isEmpty(dateStr)) {
            return null;
        }
        try {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
    
}
