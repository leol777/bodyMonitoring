package liang.junxuan.bodymonitoring.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateTimeStringConverter {

    static public String toDateStringInChinese(Date date) throws ParseException {
        SimpleDateFormat out_sdf = new SimpleDateFormat("yyyy年MM月dd日", Locale.CHINA);
        assert date != null;
        return out_sdf.format(date);
    }

    static public String toTimeStringInChinese(Date date) throws ParseException {
        SimpleDateFormat out_sdf = new SimpleDateFormat("HH时mm分ss秒", Locale.CHINA);
        assert date != null;
        return out_sdf.format(date);
    }

    static public Calendar toCalendarFromDate(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }
}
