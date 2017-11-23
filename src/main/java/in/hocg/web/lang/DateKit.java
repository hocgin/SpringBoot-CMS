package in.hocg.web.lang;

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
    
    public static Date getTodayEnd() {
        Date date = new Date();
        date.setHours(23);
        date.setMinutes(59);
        date.setSeconds(59);
        return date;
    }
}
