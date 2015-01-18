package cn.edu.ustb.sem.core.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DateUtil {
	private static final String dateTimePattern = "yyyy-MM-dd HH:mm";
	private static final String datePattern = "yyyy-MM-dd";
	private static final DateFormat dfDate = new SimpleDateFormat(datePattern);
	private static final DateFormat dfDateTime = new SimpleDateFormat(dateTimePattern);
	/**
	 * pattern = yyyy-MM-dd HH:mm
	 * @param cal
	 * @return
	 */
	public static final String getDateTime(Calendar cal) {
		if (cal == null) {
			return "";
		}
		return dfDateTime.format(cal.getTime());
	}
	public static final String getDate(Calendar cal) {
		if (cal == null) {
			return "";
		}
		return new SimpleDateFormat(datePattern).format(cal.getTime());
	}
	public static final String getDate(Calendar cal, String pattern) {
		if (cal == null) {
			return "";
		}
		return new SimpleDateFormat(pattern).format(cal.getTime());
	}
	/**
	 * pattern = yyyy-MM-dd HH:mm
	 * @param str
	 * @return
	 * @throws ParseException
	 */
	public static final Calendar parseDateTime(String str) throws ParseException {
		if (str == null || str.equals("")) return Calendar.getInstance();
		Calendar cal = Calendar.getInstance();
		cal.setTime(dfDateTime.parse(str));
		return cal;
	}
	/**
	 * 
	 * @param str yyyy-mm-dd
	 * @return
	 * @throws ParseException
	 */
	public static final Calendar parseDate(String str) throws ParseException {
		if (str == null || str.equals("")) return Calendar.getInstance();
		Calendar cal = Calendar.getInstance();
		cal.setTime(dfDate.parse(str.trim()));
		return cal;
	}

	public static final Calendar parseDate(String str, String pattern) throws ParseException {
		if (str == null || str.equals("")) return Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		Calendar cal = Calendar.getInstance();
		cal.setTime(sdf.parse(str.trim()));
		return cal;
	}
	
	// 比较字符串的时间的大小，取在前的时间
	public static final String getBefore(String date1, String date2, String pattern) throws ParseException {
		Calendar cal1 = parseDate(date1, pattern);
		Calendar cal2 = parseDate(date2, pattern);
		if (cal1.before(cal2) || cal1.equals(cal2)) {
			return date1;
		} else {
			return date2;
		}
	}

	// 比较字符串的时间的大小，取在后的时间
	public static final String getAfter(String date1, String date2, String pattern) throws ParseException {
		Calendar cal1 = parseDate(date1, pattern);
		Calendar cal2 = parseDate(date2, pattern);
		if (cal1.before(cal2) || cal1.equals(cal2)) {
			return date2;
		} else {
			return date1;
		}
	}
	public static void main(String[] args) {
		double a = 20141212;
		System.out.println(new Double(a).intValue() + "");
	}
}
