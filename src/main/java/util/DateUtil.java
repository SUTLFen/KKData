package util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DateUtil {

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private SimpleDateFormat sdf_02 = new SimpleDateFormat("yyyy-MM-dd");

    private static DateUtil instance;

    public static DateUtil getInstance() {
        if (instance == null) {
            instance = new DateUtil();
        }
        return instance;
    }

    /*
    * the name of Weibo File is like "2016-01"
    * 通过文件名获得 ：年，月，以及这个月里有多少天
    * */
    public Map<String, Integer> getDateUnitFromFileName(String fileName) {
        Map<String, Integer> map = new HashMap<String, Integer>();
        String[] strs = fileName.split("\\-");
        int year = Integer.valueOf(strs[0]);
        int month = Integer.valueOf(strs[1]);
        int daysInMonth = getDaysByYearMonth(year, month);
        map.put("year", year);
        map.put("month", month);
        map.put("days", daysInMonth);
        return map;
    }

    /*
    * 将Unix时间转为指点格式的时间字符串
    * */
    public String getFormatTime(long timestamp) {
        Date date = new Date(timestamp);
        String dateFormatStr = sdf.format(date);
        return dateFormatStr;
    }

    public int getDaysByYearMonth(int year, int month) {
        Calendar a = Calendar.getInstance();
        a.set(Calendar.YEAR, year);
        a.set(Calendar.MONTH, month - 1);
        a.set(Calendar.DATE, 1);
        a.roll(Calendar.DATE, -1);
        int maxDate = a.get(Calendar.DATE);
        return maxDate;
    }

    /*
    * 通过指定年，月，日，小时，获得相应的Unix时间轴
    * */
    public long getTimeStamp(int year, int month, int day, int hour) {
        try {
            String yearStr, monthStr, dayStr, hourStr;
            yearStr = year + "";
            monthStr = format(month);
            dayStr = format(day);
            hourStr = format(hour);
//            System.out.println("hour : " + yearStr + "-" + monthStr + "-" + dayStr + " " + hourStr + ":00:00");
            Date date = sdf.parse(yearStr + "-" + monthStr + "-" + dayStr + " " + hourStr + ":00:00");
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public String format(int tmp) {
        if (tmp < 10) {
            return "0" + tmp;
        } else return tmp + "";
    }


    //通过时间字符串获得Calendar实例
    public Calendar getCalendar(String timeStr) {
        try {
            Date date = new Date(timeStr);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            return cal;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //通过Unix时间轴获得Calendar示例
    public Calendar getCalendar(long time) {
        Date date = new Date(time);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal;
    }

    public Map<String, Integer> getDateUnitByCal(Calendar cal) {
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int hour = cal.get(Calendar.HOUR_OF_DAY);

        Map<String, Integer> map = new HashMap<String, Integer>();
        map.put("year", year);
        map.put("month", month + 1);
        map.put("day", day);
        map.put("hour", hour);
        return map;
    }

    public String getFormatTime_02(long start) {
        Date date = new Date(start);
        String dateStr = sdf_02.format(date);
        return dateStr;
    }

    /**
     * @param timeStr "2016-01-01 00:00:00";
     * @param return  返回自1970年1月1日00:00:00 GMT已经过去的毫秒;
     * */
    public long toUnixTimestamp(String timeStr) {
        try {
            Date date = sdf.parse(timeStr);
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return -1;
        }
    }
}

