package com.smate.sie.core.base.utils.json.pub;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;

import com.smate.core.base.utils.common.MoneyFormatterUtils;
import com.smate.core.base.utils.data.XmlUtil;

/**
 * @author yamingd 成果Brief生成(即页面表格的来源字段)
 */
public class BriefFormatter {

    /**
     * nvl(a,b)如a为空，则返回b.
     */
    private static final String TOKEN_NVL = "nvl";
    /**
     * 斜体.
     */
    private static final String TOKEN_I = "i";
    /**
     * 截断字符串.
     */
    private static final String TOKEN_TRUNCATE = "truncate";
    /**
     * 编辑格式化.
     */
    private static final String TOKEN_EDITORS = "editors";
    /**
     * 卷号/期号.
     */
    private static final String TOKEN_VOL_ISSUE = "vol_issue";
    /**
     * 卷号.
     */
    private static final String TOKEN_VOLUME = "volume";
    /**
     * 版本.
     */
    private static final String TOKEN_EDITION = "edition";
    /**
     * 输出文本.
     */
    private static final String TOKEN_OUT = "out";
    /**
     * 本地化文本.
     */
    private static final String TOKEN_LOCALE = "locale";
    /**
     * 发表日期格式化.
     */
    private static final String TOKEN_PUBLISH_DATE = "publish_date";
    /**
     * 日期格式化为Month Year.
     */
    private static final String TOKEN_MY = "my";
    /**
     * 在文本前后加双印号.
     */
    private static final String TOKEN_QUOTE = "quote";
    /**
     * ISBN格式化.
     */
    private static final String TOKEN_ISBN = "isbn";
    /**
     * 页码格式化.
     */
    private static final String TOKEN_PAGE = "page";
    /**
     * 日期间隔格式化. 2001/1/1-2012/1/1 2001/1/1 2012/1/1
     */
    private static final String DATE_INTERVAL = "date_interval";
    /**
     * 日期区间格式化.
     */
    private static final String TOKEN_DATE_PERIOD = "date_period";
    /**
     * 长日期格式格式化.
     */
    private static final String TOKEN_DATE_LONG = "date_long";
    /**
     * 日期格式化.
     */
    private static final String TOKEN_DATE = "date";
    /**
     * 货币格式化.
     */
    private static final String TOKEN_MONEY = "money";

    /**
     * 中文日期格式.
     */
    public static final String CHS_DATE_FORMATS = "yyyy/MM/dd";
    /**
     * 英文日期格式.
     */
    public static final String ENG_DATE_FORMATS = "dd/MM/yyyy";

    /**
     * 货币单位.
     */
    public static final Map<String, String> MONEY_UNITS = new HashMap<String, String>();

    /**
     * 月份长格式.
     */
    public static String[] EN_LONG_DATE_MONTHS = new String[] { "", "January", "February", "March", "April", "May",
            "June", "July", "August", "September", "October", "November", "December" };
    /**
     * 月份缩写.
     */
    public static String[] EN_SHORT_DATE_MONTHS = new String[] { "", "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul",
            "Aug", "Sep", "Oct", "Nov", "Dec" };
    /**
     * 罗马数字.
     */
    public static String[][] ROMAN_NUMERALS = new String[][] {
            { "", "I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX" },
            { "", "X", "XX", "XXX", "XL", "L", "LX", "LXX", "LXXX", "XCC" },
            { "", "C", "CC", "CCC", "CD", "D", "DC", "DCC", "DCCC", "CM" }, { "", "M", "MM", "MMM" } }; /* 建立对照表 */
    /**
     * token集合
     */
    private static List<String> tokens = new ArrayList<String>();
    /**
     * 字段文本最大支持的长度.
     */
    public static int TRUNCATE_LENGTH = 100;
    /**
     * 输出最大支持的长度.
     */
    public static int OUTPUT_MAX_LENGTH = 2000;

    static {
        tokens.add(TOKEN_DATE); // 01/06/2008
        tokens.add(TOKEN_DATE_LONG); // 1 June 2008
        tokens.add(DATE_INTERVAL); // 2001/1/1-2012/1/1 2001/1/1 2012/1/1
        tokens.add(TOKEN_DATE_PERIOD); // 1-2 June 2008, 1 June-2 July 2008, 1
        // June 2007-2 July 2008, 2008
        tokens.add(TOKEN_PAGE);
        tokens.add(TOKEN_ISBN);
        tokens.add(TOKEN_QUOTE);
        tokens.add(TOKEN_MY); // June 2008(month year)
        tokens.add(TOKEN_PUBLISH_DATE);
        tokens.add(TOKEN_LOCALE);
        tokens.add(TOKEN_OUT);
        tokens.add(TOKEN_EDITION);
        tokens.add(TOKEN_VOLUME);
        tokens.add(TOKEN_VOL_ISSUE);
        tokens.add(TOKEN_EDITORS);
        tokens.add(TOKEN_TRUNCATE);
        tokens.add(TOKEN_I); // italic
        tokens.add(TOKEN_NVL); // null then return empty
        tokens.add(TOKEN_MONEY);

        MONEY_UNITS.put("zh", "元");
        MONEY_UNITS.put("en", "元");
    }

    /**
     * 当前语言.
     */
    private Locale locale = null;
    /**
     * 数据源.
     */
    @SuppressWarnings("unchecked")
    private Map dataSource = null;
    /**
     * 输出缓冲区.
     */
    private StringBuffer output = null;

    /**
     * @param locale
     *            Locale
     * @param data
     *            数据源
     * @throws Exception
     *             Exception
     */
    @SuppressWarnings("unchecked")
    public BriefFormatter(Locale locale, Map data) throws Exception {
        if (locale == null) {
            throw new NullPointerException(TOKEN_LOCALE);
        }

        this.locale = locale;
        this.dataSource = data;
    }

    /**
     * @param value
     *            Locale
     */
    public void setLocale(Locale value) {
        this.locale = value;
    }

    /**
     * @return Locale
     */
    public Locale getLocale() {
        return locale;
    }

    /**
     * 是否是日期对象.
     * 
     * @param object
     *            参数对象
     * @return boolean
     */
    private boolean isDate(Object object) {

        if (object == null) {
            return true;
        }

        String name = object.getClass().getName();
        boolean flag = name.equalsIgnoreCase("java.sql.Timestamp") || name.equalsIgnoreCase("java.util.Date");

        return flag;
    }

    /**
     * 是否是书籍类型.
     * 
     * @return boolean
     */
    public boolean isBook() {
        String pubType = String.valueOf(this.dataSource.get("pub_type"));
        if ("2".equals(pubType)) {
            return true;
        }
        return false;
    }

    /**
     * 获取日期格式.
     * 
     * @return String
     */
    public String getDateFormat() {
        String lang = locale.getLanguage();
        if (lang.equals("zh")) {
            return CHS_DATE_FORMATS;
        } else {
            return ENG_DATE_FORMATS;
        }
    }

    /**
     * 格式化日期对象.
     * 
     * @param date
     *            日期/字符日期
     * @return String dd/mm/yyyy
     */
    public String formatDate(Object date) {
        if (date == null) {
            return "";
        }

        if (this.isDate(date)) {
            return DateFormatUtils.format((Date) date, this.getDateFormat());
        }
        String val = XmlUtil.filterNull(date);
        if ("".equals(val)) {
            return "";
        }
        String[] temp = val.split("-");
        String year = "0";
        String month = "0";
        String day = "0";
        if (temp.length == 3) {
            year = temp[0];
            month = temp[1];
            day = temp[2];
        } else if (temp.length == 2) {
            year = temp[0];
            month = temp[1];
        } else if (temp.length == 1) {
            year = temp[0];
        }
        if ("0".equals(year)) {
            return "";
        }

        if ("0".equals(month)) {
            return year;
        }

        if (this.locale != null && "zh".equalsIgnoreCase(this.locale.getLanguage())) {
            if ("0".equals(day)) {
                return String.format("%s.%s", year, month);
            }

            return String.format("%s.%s.%s", year, month, day);
        }

        if ("0".equals(day)) {
            return String.format("%s.%s", month, year);
        }

        return String.format("%s.%s.%s", day, month, year);
    }

    /**
     * 格式化月份为全写格式.
     * 
     * @param month
     *            月份
     * @return String
     */
    public String getMonthLongString(int month) {
        if (month <= 0 || month > 12) {
            return "";
        }

        return BriefFormatter.EN_LONG_DATE_MONTHS[month];
    }

    /**
     * 格式化月份为缩写形式.
     * 
     * @param month
     *            月份
     * @return String
     */
    public String getMonthShortString(int month) {
        if (month <= 0 || month > 12) {
            return "";
        }

        return BriefFormatter.EN_SHORT_DATE_MONTHS[month];
    }

    /**
     * 格式化日期为全写形式.
     * 
     * @param date
     *            日期
     * @return String 17 March 2007
     */
    public String formatDateAsLongString(Date date) {
        String tmp = "";
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DATE);

        tmp = String.format("%s %s %s", day, this.getMonthLongString(month), year);

        return tmp;
    }

    /**
     * 格式化日期为全写形式.
     * 
     * @param date
     *            日期
     * @return String 17 March 2007
     */
    public String formatDOAsLongString(Object date) {
        if (date == null) {
            return "";
        }
        if (isDate(date)) {
            return this.formatDateAsLongString((Date) date);
        }

        String val = XmlUtil.filterNull(date);
        if ("".equals(val)) {
            return "";
        }
        // yyyy-M-d
        String[] temp = val.split("-");
        int year = Integer.parseInt(temp[0]);
        int month = Integer.parseInt(temp[1]);
        int day = Integer.parseInt(temp[2]);
        if (year == 0) {
            return "";
        }
        if (day == 0) { // invalid day
            if (month == 0) { // invalid month
                return String.valueOf(year);
            } else {
                return String.format("%s %s", this.getMonthLongString(month), year);
            }
        } else {
            return String.format("%s %s %s", day, this.getMonthLongString(month), year);
        }
    }

    /**
     * 格式化日期为缩写形式.
     * 
     * @param date
     *            日期
     * @return String
     */
    public String formatDateAsShortString(Date date) {
        String tmp = "";
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DATE);

        tmp = String.format("%s %s %s", day, this.getMonthShortString(month), year);

        return tmp;
    }

    /**
     * 斜体文本.
     * 
     * @param text
     *            文本
     * @return String
     */
    public String italicString(String text) {
        if (text == null || text.equals("") || text.trim().equals("")) {
            return "";
        }
        return String.format("&lt;i&gt;%s&lt;/i&gt;", text);
    }

    /**
     * 变粗文本.
     * 
     * @param text
     *            文本
     * @return String
     */
    public String boldString(String text) {
        if (text == null || text.equals("") || text.trim().equals("")) {
            return "";
        }
        return String.format("<strong>%s</strong>", text);
    }

    /**
     * 加下划线.
     * 
     * @param text
     *            文本
     * @return String
     */
    public String underLineString(String text) {
        if (text == null || text.equals("") || text.trim().equals("")) {
            return "";
        }
        return String.format("<u>%s</u>", text);
    }

    /**
     * 加双引号.
     * 
     * @param text
     *            文本
     * @return String
     */
    public String quotationString(String text) {
        if (text == null || text.equals("") || text.trim().equals("")) {
            return "";
        }
        return String.format("\"%s\"", text);
    }

    /**
     * 加括号.
     * 
     * @param text
     *            文本
     * @return String
     */
    public String bracketString(String text) {
        if (text == null || text.equals("") || text.trim().equals("")) {
            return "";
        }
        return String.format("(%s)", text);
    }

    /**
     * 格式化页码.
     * 
     * @param startPage
     *            开始页
     * @param endPage
     *            结束页
     * @param totalPage
     *            总页数
     * @return String
     */
    public String formatPage(String totalPage) {

        if (XmlUtil.isEmpty(totalPage)) {
            return "";
        } else {
            return String.format("%s pp", totalPage);
        }
    }

    /**
     * 格式化页码.
     * 
     * @param startPage
     * @param endPage
     *            总页数：xxx p 第x页：p x 第x-y页 pp x-y
     * @return String
     */
    public String formatPage(String startPage, String endPage) {
        startPage = org.apache.commons.lang.StringUtils.trimToEmpty(startPage);
        endPage = org.apache.commons.lang.StringUtils.trimToEmpty(endPage);

        if (XmlUtil.isEmpty(startPage) && XmlUtil.isEmpty(endPage)) {
            return "";
        }
        if (startPage.equalsIgnoreCase(endPage)) {
            endPage = "";
        }

        boolean isbook = this.isBook();

        if (XmlUtil.isEmpty(endPage)) {
            return isbook ? startPage + " pp" : "pp " + startPage;
        }

        if (XmlUtil.isEmpty(startPage)) {
            return isbook ? endPage + " pp" : "pp " + endPage;
        }

        return String.format("pp %s-%s", startPage, endPage);
    }

    /**
     * 格式化页码.
     * 
     * @param startPage
     *            开始页
     * @param endPage
     *            结束页
     * @param totalPage
     *            总页数
     * @return String
     */
    public String formatPage(String startPage, String endPage, String totalPage) {
        startPage = startPage.trim();
        endPage = endPage.trim();

        if (XmlUtil.isEmpty(startPage) && XmlUtil.isEmpty(endPage)) {
            if (XmlUtil.isEmpty(totalPage)) {
                return "";
            } else {
                return String.format("%s pp", totalPage);
            }
        }

        if (startPage.equalsIgnoreCase(endPage)) {
            endPage = "";
        }

        boolean isbook = this.isBook();
        String res = "";

        if (XmlUtil.isEmpty(endPage) && !XmlUtil.isEmpty(startPage)) {
            res = isbook ? startPage + " pp" : "pp " + startPage;
        }

        if (!XmlUtil.isEmpty(endPage) && XmlUtil.isEmpty(startPage)) {
            res = isbook ? endPage + " pp" : "pp " + endPage;
        }

        if (!XmlUtil.isEmpty(endPage) && !XmlUtil.isEmpty(startPage)) {
            res = String.format("pp %s-%s", startPage, endPage);
        }

        if (!XmlUtil.isEmpty(totalPage)) {
            res = String.format("%s pp, %s", totalPage, res);
        }
        return res;
    }

    /**
     * 格式化ISBN.
     * 
     * @param isbn
     *            ISBN
     * @return String
     */
    public String formatISBN(String isbn) {
        if (XmlUtil.isEmpty(isbn)) {
            return "";
        }
        return String.format("ISBN: %s", isbn);
    }

    /**
     * 格式专利号.
     * 
     * @param patentNo
     *            专利号
     * @return String
     */
    public String formatPatentNo(String patentNo) {
        if (XmlUtil.isEmpty(patentNo)) {
            return "";
        }
        if ("zh".equalsIgnoreCase(this.locale.getLanguage())) {
            return String.format("专利号: %s", patentNo);
        }

        return String.format("Patent No: %s", patentNo);
    }

    /**
     * 日期间隔格式化
     * 
     * @param startD
     * @param endD
     * @return
     */
    public String formatDateInterval(Object startD, Object endD) {
        if (startD == null && endD == null) {
            return "";
        }
        String start = XmlUtil.filterNull(startD);
        String end = XmlUtil.filterNull(endD);
        if ("".equals(start) && "".equals(end)) {
            return "";
        }
        if (start != null) {
            start = start.replaceAll("/", ".");
        }
        if (end != null) {
            end = end.replaceAll("/", ".");
        }
        if (StringUtils.isNotBlank(start) && StringUtils.isNotBlank(end)) {
            return start + "-" + end;
        } else {
            return start + end;
        }
    }

    /**
     * 格式化日期区间.
     * 
     * @param start
     *            开始日期
     * @param end
     *            结束日期
     * @return String
     */
    public String formatDatePeriod(Date start, Date end) {
        String str = this.formatSEDatePeriod(start, end, "long");
        return str;
    }

    /**
     * 格式化日期区间.
     * 
     * @param startD
     *            开始日期 (yyyy-MM-dd or yyyy-MM or yyyy)
     * @param endD
     *            结束日期 (yyyy-MM-dd or yyyy-MM or yyyy)
     * @param style
     *            格式
     * @return String
     */
    public String formatSEDatePeriod(Object startD, Object endD, String style) {
        Date startDate = null, endDate = null;
        if (startD == null && endD == null) {
            return "";
        }

        if (startD != null && isDate(startD)) {
            startDate = (Date) startD;
        }
        if (endD != null && isDate(endD)) {
            endDate = (Date) endD;
        }
        if (startDate != null || endDate != null) {
            return this.formatDatePeriod(startDate, endDate, style, style);
        }

        String start = XmlUtil.filterNull(startD);
        String end = XmlUtil.filterNull(endD);

        if ("".equals(start) && "".equals(end)) {
            return "";
        }

        String[] temp = start.split("-");
        String startStyle = style, endStyle = style;

        boolean year = true, month = true, day = true;

        if (!"".equals(start)) {
            for (int i = 0; i < temp.length; i++) {
                if (XmlUtil.isEmpty(temp[i]) || "0".equals(temp[i])) {
                    temp[i] = "1";
                    if (i == 0) {
                        year = false;
                    } else if (i == 1) {
                        month = false;
                        startStyle = "y";
                    } else {
                        day = false;
                        startStyle = month ? TOKEN_MY : "y";
                    }
                }
            }

            try {
                String str = String.format("%04d-%02d-%02d", Integer.parseInt(temp[0]), Integer.parseInt(temp[1]),
                        Integer.parseInt(temp[2]));
                startDate = !year ? null : DateUtils.parseDate(str, new String[] { "yyyy-MM-dd" });
            } catch (ParseException e) {
                startDate = null;
            }
        } else {
            startDate = null;

        }

        if (!"".equals(end)) {
            year = true;
            month = true;
            day = true;

            temp = end.split("-");
            for (int i = 0; i < temp.length; i++) {
                if (XmlUtil.isEmpty(temp[i]) || "0".equals(temp[i])) {
                    temp[i] = "1";
                    if (i == 0) {
                        year = false;
                    } else if (i == 1) {
                        month = false;
                        endStyle = "y";
                    } else {
                        day = false;
                        endStyle = month ? TOKEN_MY : "y";
                    }
                }
            }

            try {
                String str = String.format("%04d-%02d-%02d", Integer.parseInt(temp[0]), Integer.parseInt(temp[1]),
                        Integer.parseInt(temp[2]));
                endDate = !year ? null : DateUtils.parseDate(str, new String[] { "yyyy-MM-dd" });
            } catch (ParseException e) {
                endDate = null;
            }
        } else {
            endDate = null;
        }

        if (startDate == null && endDate == null) {
            return "";
        }

        String str = this.formatDatePeriod(startDate, endDate, startStyle, endStyle);
        return str;

    }

    /**
     * 格式化日期区间.
     * 
     * @param start
     *            开始日期
     * @param end
     *            结束日期
     * @param startStyle
     *            开始日期格式
     * @param endStyle
     *            结束日期格式
     * @return
     */
    public String formatDatePeriod(Date start, Date end, String startStyle, String endStyle) {
        if (start == null) {
            return "";
        }

        startStyle = startStyle.replaceAll("[\"|']", "");
        endStyle = endStyle.replaceAll("[\"|']", "");

        if (start != null && end == null) {
            if (TOKEN_MY.equalsIgnoreCase(startStyle)) {
                String startStr = this.formatMonthYear(start);
                return startStr;
            } else if ("y".equalsIgnoreCase(startStyle)) {
                return String.valueOf(start.getYear() + 1900);
            } else {
                String startStr = this.formatDateAsLongString(start);
                return startStr;
            }
        } else if (start == null && end != null) {
            if (TOKEN_MY.equalsIgnoreCase(endStyle)) {
                String startStr = this.formatMonthYear(end);
                return startStr;
            } else if ("y".equalsIgnoreCase(endStyle)) {
                return String.valueOf(end.getYear() + 1900);
            } else {
                String startStr = this.formatDateAsLongString(end);
                return startStr;
            }
        }

        Calendar startCal = Calendar.getInstance();
        startCal.setTime(start);
        Calendar endCal = Calendar.getInstance();
        endCal.setTime(end);

        String startStr = "", endStr = "";
        // 都只有年
        if ("y".equalsIgnoreCase(startStyle) && "y".equalsIgnoreCase(endStyle)) {

            startStr = String.valueOf(startCal.get(Calendar.YEAR));
            endStr = String.valueOf(endCal.get(Calendar.YEAR));

            if (startStr.equals(endStr)) {
                return startStr;
            }
            return String.format("%s - %s", startStr, endStr);
        }

        if (endCal.get(Calendar.YEAR) != startCal.get(Calendar.YEAR)) {
            // 不是同一年

            if ("y".equalsIgnoreCase(startStyle)) {
                startStr = String.valueOf(startCal.get(Calendar.YEAR));
            } else if (TOKEN_MY.equalsIgnoreCase(startStyle)) {
                startStr = this.formatMonthYear(start);
            } else {
                startStr = this.formatDateAsLongString(start);
            }

            if ("y".equalsIgnoreCase(endStyle)) {
                endStr = String.valueOf(endCal.get(Calendar.YEAR));
            } else if (TOKEN_MY.equalsIgnoreCase(endStyle)) {
                endStr = this.formatMonthYear(end);
            } else {
                endStr = this.formatDateAsLongString(end);
            }

            return String.format("%s - %s", startStr, endStr);
        } else {

            String smonth = String.valueOf(startCal.get(Calendar.MONTH) + 1);
            String emonth = String.valueOf(endCal.get(Calendar.MONTH) + 1);

            smonth = this.getMonthLongString(Integer.parseInt(smonth));
            emonth = this.getMonthLongString(Integer.parseInt(emonth));

            if (!smonth.equals(emonth)) {
                // 不是同一个月份 1 July - 1 September 2008
                if (TOKEN_MY.equalsIgnoreCase(startStyle) || TOKEN_MY.equalsIgnoreCase(endStyle)) {
                    return String.format("%s - %s %s", smonth, emonth, startCal.get(Calendar.YEAR));
                }
                return String.format("%s %s - %s %s %s", startCal.get(Calendar.DATE), smonth, endCal.get(Calendar.DATE),
                        emonth, startCal.get(Calendar.YEAR));
            } else {
                // 同一个月份 1 - 10 September 2008
                if (startCal.get(Calendar.DATE) != endCal.get(Calendar.DATE)) {
                    return String.format("%s-%s %s %s", startCal.get(Calendar.DATE), endCal.get(Calendar.DATE), emonth,
                            startCal.get(Calendar.YEAR));
                } else {
                    if (TOKEN_MY.equalsIgnoreCase(startStyle)) {
                        return String.format("%s %s", emonth, startCal.get(Calendar.YEAR));
                    } else {
                        return String.format("%s %s %s", startCal.get(Calendar.DATE), emonth,
                                startCal.get(Calendar.YEAR));
                    }
                }
            }
        }
    }

    /**
     * 格式化日期为年月格式.
     * 
     * @param date
     *            日期
     * @return String (e.g. Jan 2008)
     */
    public String formatMonthYear(Object date) {
        if (date == null) {
            return "";
        }
        if (this.isDate(date)) {
            Calendar cal = Calendar.getInstance();
            cal.setTime((Date) date);

            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH) + 1;

            return this.formatMonthYear(String.valueOf(year), String.valueOf(month));
        }
        String val = XmlUtil.filterNull(date);
        if ("".equals(val)) {
            return "";
        }

        String[] temp = val.split("-");
        String year = temp[0];
        if (temp.length == 2) {
            String month = temp[1];

            return this.formatMonthYear(year, month);
        }
        return year;
    }

    /**
     * 格式化日期为年月格式.
     * 
     * @param year
     *            年
     * @param month
     *            月
     * @return String (e.g. Jan 2008)
     */
    public String formatMonthYear(String year, String month) {
        try {
            month = this.getMonthLongString(Integer.parseInt(month));
            String str = String.format("%s %s", month, year).trim();
            return str;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 连接字符数组.
     * 
     * @param items
     *            数组
     * @param seprator
     *            分隔符
     * @return String
     */
    public String Join(String[] items, String seprator) {
        if (items == null || seprator == null) {
            return "";
        }
        if (items.length == 0) {
            return "";
        }
        if (items.length == 1) {
            return items[0];
        }

        StringBuffer tmp = new StringBuffer();
        for (int i = 0; i < items.length; i++) {
            if (!XmlUtil.isEmpty(items[i])) {
                if (i > 0) {
                    tmp.append(seprator);
                }
                tmp.append(items[i]);
            }
        }
        return tmp.toString();
    }

    /**
     * 格式化发表日期.
     * 
     * @param date
     *            日期
     * @param year
     *            年
     * @param month
     *            月
     * @param style
     *            格式
     * @return String
     */
    public String formatPublishDate(Object date, String year, String month, String style) {
        style = style.replaceAll("[\"|']", "");
        if (date != null) {
            Date pdate = (Date) date;
            if (TOKEN_MY.equalsIgnoreCase(style)) {
                return this.formatMonthYear(pdate);
            } else if (TOKEN_DATE.equalsIgnoreCase(style)) {
                return this.formatDate(pdate);
            } else if (TOKEN_DATE_LONG.equalsIgnoreCase(style)) {
                return this.formatDateAsLongString(pdate);
            }
        } else if (!XmlUtil.isEmpty(year)) {
            if (XmlUtil.isEmpty(month)) {
                return year;
            } else {
                return this.formatMonthYear(year, month);
            }
        }
        return "";
    }

    /**
     * 格式化数字为顺序数字.
     * 
     * @param number
     *            数字
     * @return String
     */
    public String getNumberOrdinalStyle(int number) {
        if (number % 100 >= 11 && number % 100 <= 13) {
            return String.format("%sth", number);
        }
        switch (number % 10) {
        case 1:
            return String.format("%sst", number);
        case 2:
            return String.format("%snd", number);
        case 3:
            return String.format("%srd", number);
        }
        return String.format("%sth", number);
    }

    /**
     * 格式化罗马数字.
     * 
     * @param number
     *            数字
     * @return String
     */
    public String getNumberRomanStyle(int number) {
        if (number == 0) {
            return "";
        }

        String str = "";
        int t, i, m;
        int maxPos = 4;
        int baseNo = (int) Math.pow(10, maxPos);// 基数

        if (number >= baseNo) {
            return String.valueOf(number);
        }

        for (m = 0, i = baseNo; m < maxPos; m++, i /= 10) {
            t = (number % i) / (i / 10); /* 从高位向低位依次取各位的数字 */
            str += BriefFormatter.ROMAN_NUMERALS[maxPos - 1 - m][t]; /* 通过对照表翻译输出 */
        }

        return str;
    }

    /**
     * 版本号.
     * 
     * @param number
     *            数字
     * @return String
     */
    public String formatEditionNo(String number) {
        if (!XmlUtil.isEmpty(number)) {
            int no = Integer.parseInt(number);
            String str = this.getNumberOrdinalStyle(no);
            return String.format("%s edition", str);
        }
        return "";
    }

    /**
     * 卷号.
     * 
     * @param number
     *            数字
     * @return String
     */
    public String formatVolumeNo(String number) {
        number = XmlUtil.filterNull(number);

        if (!XmlUtil.isEmpty(number)) {
            if (XmlUtil.isNumeric(number)) {
                int no = Integer.parseInt(number);
                // String str = this.getNumberRomanStyle(no);
                /*
                 * if ("zh".equalsIgnoreCase(this.locale.getLanguage())) { return String.format("%s卷", no); }
                 * 
                 * return String.format("Vol %s", no);
                 */
                return String.format("%s", no);
            }
            if (number.toLowerCase().contains("vol")) {
                return number;
            } else {
                /*
                 * if ("zh".equalsIgnoreCase(this.locale.getLanguage())) { return String.format("%s卷", number); }
                 * 
                 * return String.format("Vol %s", number);
                 */
                return String.format("%s", number);
            }
        }
        return "";
    }

    /**
     * 卷号、期号.
     * 
     * @param vol
     *            卷号
     * @param issue
     *            期号
     * @return String
     */
    public String formatVolumeIssue(String vol, String issue) {
        vol = XmlUtil.filterNull(vol);
        issue = XmlUtil.filterNull(issue);

        String res = "";
        if (!XmlUtil.isEmpty(vol) && !XmlUtil.isEmpty(issue)) {
            return String.format("%s(%s)", vol, issue);
        }
        if (!XmlUtil.isEmpty(vol)) {
            /*
             * if (XmlUtil.isNumeric(vol)) { int no = Integer.parseInt(vol); // String str =
             * this.getNumberRomanStyle(no); if ("zh".equalsIgnoreCase(this.locale.getLanguage())) { return
             * String.format("%s卷", no); }
             * 
             * return String.format("Vol %s", no); } if (vol.toLowerCase().contains("vol")) { return vol; } else
             * { if ("zh".equalsIgnoreCase(this.locale.getLanguage())) { return String.format("%s卷", vol); }
             * 
             * return String.format("Vol %s", vol); }
             */
            if (XmlUtil.isNumeric(vol)) {
                return String.format("%s", vol);
            } else {
                return vol;
            }
        }
        if (!XmlUtil.isEmpty(issue)) {
            /*
             * if (XmlUtil.isNumeric(issue)) { if ("zh".equalsIgnoreCase(this.locale.getLanguage())) { return
             * String.format("%s期", issue); }
             * 
             * return String.format("Issue %s", issue); } if (issue.toLowerCase().contains("issue")) { return
             * issue; } else { if ("zh".equalsIgnoreCase(this.locale.getLanguage())) { return
             * String.format("%s期", issue); }
             * 
             * return String.format("Issue %s", issue); }
             */
            if (XmlUtil.isNumeric(issue)) {
                return String.format("(%s)", issue);
            } else {
                return issue;
            }
        }
        return res;
    }

    /**
     * 格式化编辑.
     * 
     * @param names
     *            编辑者名称
     * @return String
     */
    public String formatEditors(String names) {
        if (!XmlUtil.isEmpty(names)) {
            names = org.apache.commons.lang.StringUtils.chomp(names, ";");
            names = org.apache.commons.lang.StringUtils.chomp(names, ",");
            String[] items = names.split(";");
            if (items.length > 3) {
                names = String.format("%s, %s and %s et al. (eds)", items[0], items[1], items[2]);
            } else if (items.length == 3) {
                names = String.format("%s, %s and %s (eds)", items[0], items[1], items[2]);
            } else if (items.length == 2) {
                names = String.format("%s and %s (eds)", items[0], items[1]);
            } else if (items.length == 1) {
                names = String.format("%s (ed)", names.replace(";", ", "));
            }
            return names.trim();
        }
        return "";
    }

    /**
     * @param ctext
     *            中文文本
     * @param etext
     *            英文文本
     * @return String
     */
    public String getLanguageSpecificText(String ctext, String etext) {
        if (XmlUtil.isEmpty(ctext)) {
            ctext = "";
        }
        if (XmlUtil.isEmpty(etext)) {
            etext = "";
        }

        String lan = this.locale.getLanguage();
        if (lan.equalsIgnoreCase("zh")) {
            if (!"".equals(ctext)) {
                return ctext;
            } else {
                return etext;
            }
        } else {
            if (!"".equals(etext)) {
                return etext;
            } else {
                return ctext;
            }
        }
    }

    /**
     * 截断.
     * 
     * @param source
     *            字符串
     * @return String
     */
    public String truncate(String source) {
        if (XmlUtil.isEmpty(source)) {
            return "";
        }
        source = source.trim();

        int maxLength = BriefFormatter.OUTPUT_MAX_LENGTH - this.output.length() - 3;// 3为省略号长度
        maxLength = maxLength > BriefFormatter.TRUNCATE_LENGTH ? BriefFormatter.TRUNCATE_LENGTH : maxLength;

        if (source.length() > maxLength) {
            source = source.substring(0, TRUNCATE_LENGTH) + "...";
        }
        return source;
    }

    /**
     * @param pattern
     *            格式配置
     * @return String
     * @throws Exception
     *             Exception
     */
    public String format(String pattern) throws Exception {
        if (pattern == null || pattern.equals("") || pattern.trim().equals("")) {
            return "";
        }
        pattern = pattern.trim();

        // System.out.println("Pattern: "+pattern);

        int pos = 0;

        Stack<Integer> tokenPos = new Stack<Integer>();

        boolean isTagBegin = false;
        boolean isTagEnd = false;
        int tagBeginPos = -1;
        int tagEndPos = -1;

        output = new StringBuffer();
        output.ensureCapacity(pattern.length() * 10);

        output.append(pattern);

        while (pos < output.length()) {
            if (output.charAt(pos) == '<') {
                if (pos + 1 == output.length()) {
                    break;
                }

                if (output.charAt(pos + 1) == '/') {
                    isTagBegin = true;
                    tagEndPos = pos;
                } else {
                    isTagEnd = true;
                    tagBeginPos = pos;
                }
            } else if (output.charAt(pos) == '>') {
                if (isTagBegin) {
                    String name = output.substring(tagEndPos, pos + 1);
                    name = name.replaceAll("[</>]", "");
                    int begin = tokenPos.pop();

                    if (tokens.contains(name)) {
                        String tokenBody = output.substring(begin, pos + 1);
                        int tstart = tokenBody.indexOf(name + ">");
                        int tend = tokenBody.indexOf("</" + name);

                        String innerCode = "";
                        try {
                            innerCode = tokenBody.substring(tstart + 1 + name.length(), tend);
                        } catch (Exception e) {
                            System.out.println(tokenBody);
                            throw e;
                        }
                        String innerText = tokenBody.replaceAll("\\<.*?>", "");

                        // System.out.println(String.format("Token:name=%s,innerCode=%s",name,innerCode));
                        String value = this.evalTokenValue(name, innerText, innerCode);
                        // System.out.println(String.format("Eval Token
                        // value:name=%s,value=%s",name,value));

                        if (!value.equals(tokenBody)) {
                            // value = value.replace("&", "&amp;");
                            // value = value.replace("<", "&lt;").replace(">",
                            // "&gt;");

                            output.replace(begin, pos + 1, value);

                            if (value.length() > tokenBody.length()) {
                                pos = pos + (value.length() - tokenBody.length());
                            }
                            if (value.length() < tokenBody.length()) {
                                pos = pos - (tokenBody.length() - value.length());
                            }
                            // System.out.println(output);
                        }
                    }
                } else if (isTagEnd) {
                    tokenPos.push(tagBeginPos);
                }
                isTagBegin = false;
                isTagEnd = false;
            }
            pos++;
        }

        String res = output.toString();
        Pattern reg = Pattern.compile("<i>,?\\s*</i>");
        Matcher m = reg.matcher(res);
        res = m.replaceAll("");

        reg = Pattern.compile("<truncate>,?\\s*</truncate>");
        m = reg.matcher(res);
        res = m.replaceAll("");

        res = res.replace("&lt;i&gt;", "<i>").replace("&lt;/i&gt;", "</i>");
        reg = Pattern.compile("<i>,?\\s*</i>");
        m = reg.matcher(res);
        res = m.replaceAll("");

        res = org.apache.commons.lang.StringUtils.strip(res, ",").trim();

        res = res.replace("<FONT ", "<FONTT ").replace("<font ", "<fontt ");
        res = res.replace("FONT-SIZE=", "FONTT-SIZE=").replace("font-size=", " fontt-size=");
        res = res.replace("FONT-SIZE:", "FONTT-SIZE:").replace("font-size:", " fontt-size:");
        res = res.replace(" FONT-COLOR=", " FONTT-COLOR=").replace(" font-color=", " fontt-color=");
        res = res.replace(" FONT-COLOR:", " FONTT-COLOR:").replace(" font-color:", " fontt-color:");
        res = res.replace(" COLOR=", " CCOLOR=").replace(" color=", " ccolor=");
        res = res.replace(" COLOR:", " CCOLOR:").replace(" color:", " ccolor:");
        res = res.replace(" FONT-FAMILY:", " FONTT-FAMILY:").replace(" font-family:", " fontt-family:");
        res = res.replace(" STYLE=", " SSTYLE=").replace(" style=", " sstyle=");

        res = res.replace("<A ", "<AA ").replace("<a ", "<aa ");
        res = res.replace("<P>", "<br />").replace("<p>", "<br />");
        res = res.replace("<P ", "<br ").replace("<p ", "<br ");
        res = res.replace("</P>", "<br />").replace("</p>", "<br />");
        res = res.trim();

        /*
         * if (!"".equals(res)) { res += "."; }
         */

        // System.out.println(res);

        return res;
    }

    /**
     * @param text
     *            文本
     * @return String
     */
    public String outValue(String text) {

        if (text == null || text == "") {
            return "";
        }
        text = text.trim();
        String ttmp = org.apache.commons.lang.StringUtils.chomp(text, ",");
        if ("".equals(ttmp) || ",".equals(ttmp) || ", ()".equals(ttmp) || "()".equals(ttmp)) {
            text = "";
        }
        return text;
    }

    /**
     * @param name
     *            Token名
     * @param innerText
     *            文本
     * @param innerCode
     *            文本
     * @return String
     */
    private String evalTokenValue(String name, String innerText, String innerCode) {
        if (innerText == null || "".equals(innerText)) {
            return "";
        }

        int begin = innerText.indexOf("${");
        int end = innerText.indexOf("}");
        if (begin >= 0 && end > 0) {
            String[] key = innerText.substring(begin + 2, end).replaceAll("\\s+", "").toUpperCase().split(",");
            for (int i = 0; i < key.length; i++) {
                key[i] = key[i].trim();
            }

            Object value = dataSource.get(key[0]);
            String tmp = "";
            if (TOKEN_DATE.equalsIgnoreCase(name)) {
                tmp = this.formatDate(value);
            } else if (TOKEN_DATE_LONG.equalsIgnoreCase(name)) {
                tmp = this.formatDOAsLongString(value);
            } else if (DATE_INTERVAL.equalsIgnoreCase(name)) {
                tmp = this.formatDateInterval(value, dataSource.get(key[1]));
            } else if (TOKEN_DATE_PERIOD.equalsIgnoreCase(name)) {
                if (key.length == 2) {
                    tmp = this.formatSEDatePeriod(value, dataSource.get(key[1]), "long");
                } else if (key.length == 3) {
                    tmp = this.formatSEDatePeriod(value, dataSource.get(key[1]), key[2]);
                }

            } else if (TOKEN_PAGE.equalsIgnoreCase(name)) {
                if (key.length == 1) {
                    tmp = this.formatPage(String.valueOf(value));
                } else if (key.length == 2) {
                    tmp = this.formatPage(String.valueOf(value), String.valueOf(dataSource.get(key[1])));
                } else if (key.length == 3) {
                    tmp = this.formatPage(String.valueOf(value), String.valueOf(dataSource.get(key[1])),
                            String.valueOf(dataSource.get(key[2])));
                }
            } else if (TOKEN_ISBN.equalsIgnoreCase(name)) {
                tmp = this.formatISBN(String.valueOf(value));
            } else if (TOKEN_MY.equalsIgnoreCase(name)) {
                tmp = this.formatMonthYear(value);
            } else if (TOKEN_PUBLISH_DATE.equalsIgnoreCase(name)) {
                String pubDateDesc = XmlUtil.filterNull(dataSource.get("pub_date_desc"));
                String pub_year = XmlUtil.filterNull(dataSource.get("publish_year"));
                if ("".equals(pubDateDesc)) {
                    tmp = this.formatPublishDate(value, String.valueOf(dataSource.get(key[1])),
                            String.valueOf(dataSource.get(key[2])), key[3]);
                } else {
                    tmp = pubDateDesc + " " + pub_year;
                }
                tmp = tmp.replaceAll("-", "."); // 出版年份用.分开
            } else if (TOKEN_LOCALE.equalsIgnoreCase(name)) {
                String ctext = String.valueOf(dataSource.get(key[0]));
                String etext = String.valueOf(dataSource.get(key[1]));
                tmp = this.getLanguageSpecificText(ctext, etext);
            } else if (TOKEN_OUT.equalsIgnoreCase(name)) {
                tmp = XmlUtil.filterNull(dataSource.get(key[0]));
                if (TOKEN_PUBLISH_DATE.toUpperCase().equals(key[0]) || "START_DATE".equals(key[0])
                        || "END_DATE".equals(key[0])) {
                    tmp = tmp.replaceAll("-", "."); // 出版年份用.分开
                }
                if (StringUtils.isNotEmpty(tmp) && Pattern.compile("^\\S+[;；]+$").matcher(tmp).matches()) {
                    // 修正部分数据结尾可能会有多余中英文分号的情况
                    tmp = tmp.replaceAll("[;；]+$", "");
                }
            } else if (TOKEN_EDITION.equalsIgnoreCase(name)) {
                tmp = this.formatEditionNo(String.valueOf(value));
            } else if (TOKEN_VOLUME.equalsIgnoreCase(name)) {
                tmp = this.formatVolumeNo(String.valueOf(value));
            } else if (TOKEN_VOL_ISSUE.equalsIgnoreCase(name)) {
                tmp = this.formatVolumeIssue(String.valueOf(value), String.valueOf(dataSource.get(key[1])));
            } else if (TOKEN_EDITORS.equalsIgnoreCase(name)) {
                tmp = this.formatEditors(String.valueOf(value));
            } else if (TOKEN_QUOTE.equalsIgnoreCase(name)) {
                tmp = XmlUtil.filterNull(value);
            } else if (TOKEN_I.equalsIgnoreCase(name)) {
                tmp = XmlUtil.filterNull(value);
            } else if (TOKEN_MONEY.equalsIgnoreCase(name)) {
                tmp = XmlUtil.filterNull(value);
                tmp = MoneyFormatterUtils.format(tmp);
                if (!"".equals(tmp)) {
                    tmp = tmp + MONEY_UNITS.get(this.locale.getLanguage().toLowerCase());
                }
            } else if (TOKEN_NVL.equalsIgnoreCase(name)) {
                tmp = XmlUtil.filterNull(dataSource.get(key[0]));
            } else {
                return innerCode;
            }

            String val = innerText.substring(0, begin) + tmp + innerText.substring(end + 1);
            val = val.trim();
            if (TOKEN_I.equalsIgnoreCase(name)) {
                val = this.italicString(val);
            } else if (TOKEN_QUOTE.equalsIgnoreCase(name)) {
                val = this.quotationString(val);
            } else if (TOKEN_NVL.equalsIgnoreCase(name) && "".equals(XmlUtil.filterNull(dataSource.get(key[0])))) {
                val = "";
            }
            val = outValue(val);
            return val;
        } else if (TOKEN_QUOTE.equalsIgnoreCase(name)) {
            return this.quotationString(innerCode);
        } else if (TOKEN_TRUNCATE.equalsIgnoreCase(name)) {
            return this.truncate(innerCode);
        } else if (TOKEN_OUT.equalsIgnoreCase(name)) {
            String tmp = outValue(innerCode);
            return tmp;
        } else if (TOKEN_I.equalsIgnoreCase(name)) {
            String tmp = italicString(innerCode);
            return tmp;
        }
        return innerCode;
    }

    /**
     * @param args
     *            参数
     */
    public static void main(final String[] args) {

        try {

            Date pdate = DateUtils.parseDate("2007-1-01", new String[] { "yyyy-MM-dd" });

            System.out.println(pdate);

            Map<String, Object> data = new HashMap<String, Object>();
            data.put(TOKEN_DATE, new Date(System.currentTimeMillis()));
            data.put("start_page", "122");
            data.put("end_page", "5858");

            BriefFormatter formatter = new BriefFormatter(Locale.US, data);
            // System.out.println(formatter.formatPage("10", "20"));
            // System.out.println(formatter.formatDateAsLongString(new
            // Date(System.currentTimeMillis())));

            // String result =
            // formatter.format("<u><quote><date>${date}</date> lslslslsl
            // <page>${start_page,end_page}</page> io90888 </quote> pspspsp
            // </u>lslsllsl,<page>${start_page,end_page}</page>,<date_long>${date}</date_long>");
            // System.out.println(result);
            // System.out.println(formatter.getNumberOrdinalStyle(10));

            System.out.println(formatter.formatEditors("aaa"));
            System.out.println(formatter.formatEditors("bb;cc;dd"));
            System.out.println(formatter.formatEditors(";bb;"));
            System.out.println(formatter.formatEditors(",cc;dd,ee;"));

            String date = "2018-10-12";
            date = formatter.formatDate(date);
            System.out.println("test = " + date);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
