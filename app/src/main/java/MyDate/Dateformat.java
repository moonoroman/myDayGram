package MyDate;

import java.util.Calendar;

/**
 * Created by Roman on 2017/4/15.
 */
public class Dateformat {
    private final static String[] weeks = {"SUN","MON","TUE","WED","THU","FRI","SAT"};
    private final static String[] months = {"JANUARY","FEBRUARY","MARCH","APRIL","MAY","JUNE",
            "JULY","AUGUST","SEPTEMBER","OCTOBER","DECEMBER","NOVEMBER"};

    //获取星期
    public static String getDayOfWeek(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.set(date.getYear(),date.getMonth(),date.getDay());
        int week = calendar.get(Calendar.DAY_OF_WEEK)-1;
        return weeks[week];
    }

    //获取月份
    public static int getMonth(Date date){
        return date.getMonth();
    }

    //获取月份名称
    public static String getMonthString(int month){
        return months[month];
    }


    //获取年份
    public static int getYear(Date date){
        return date.getYear();
    }

    //获取日
    public static int getDay(Date date){
        return date.getDay();
    }

    //获取指定年月的天数
    public static int getDaynumOfMonth(int year,int month){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR,year);
        calendar.set(Calendar.MONTH,month);
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }
}
