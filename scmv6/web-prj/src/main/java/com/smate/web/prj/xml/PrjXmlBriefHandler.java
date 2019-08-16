package com.smate.web.prj.xml;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.smate.web.prj.util.BriefUtils;
import com.smate.web.prj.util.PrjBriefUtils;

/**
 * 项目Brief生成处理
 * 
 * @author liqinghua
 * 
 */
public class PrjXmlBriefHandler {

  /**
   * 字段.
   */
  private static final List<String> FIELDS = new ArrayList<String>();

  static {
    // FIELDS.add("/project/@ins_name");
    FIELDS.add("/project/@scheme_agency_name");
    FIELDS.add("/project/@scheme_name");
    FIELDS.add("/project/@amount");
    FIELDS.add("/project/@amount_unit");
    // FIELDS.add("/project/@funding_year");
    // FIELDS.add("/pub_meta/@zh_source_db_name");
    // FIELDS.add("/pub_meta/@en_source_db_name");
    FIELDS.add("/project/@start_year");
    FIELDS.add("/project/@start_month");
    FIELDS.add("/project/@start_day");
    FIELDS.add("/project/@end_year");
    FIELDS.add("/project/@end_month");
    FIELDS.add("/project/@end_day");
    FIELDS.add("/project/@scheme_agency_name_en");
    FIELDS.add("/project/@scheme_name_en");
  }
  /**
   * 格式化Pattern.
   */
  // private final String pattern =
  // "<out>${ins_name}</out><out>, ${scheme_agency_name}</out><money>,
  // ${amount}</money><out>,${funding_year}</out><out>
  // (${source_db_name})</out>";
  private final String pattern =
      "<out>${scheme_agency_name}</out><out>${scheme_name}</out><out>, ${aount_unit}</out><date_interval>, ${start_date,end_date}</date_interval><out></out>";

  private final String patternEn =
      "<out>${scheme_agency_name_en}</out><out>${scheme_name_en}</out><out>, ${aount_unit}</out><date_interval>, ${start_date,end_date}</date_interval><out></out>";

  public Map<String, String> getData(Locale locale, PrjXmlDocument xmlDocument) throws Exception {
    Map<String, String> datas = PrjBriefUtils.getFieldsData(FIELDS, xmlDocument);
    // String zhSourceDbName = datas.get(FIELDS.get(5));
    // String enSourceDbName = datas.get(FIELDS.get(6));
    // String sourceDbName =
    // XmlUtil.getLanguageSpecificText(locale.getLanguage(),
    // zhSourceDbName, enSourceDbName);
    // datas.put("source_db_name", sourceDbName);

    String startYear = datas.get(FIELDS.get(4));
    String startMonth = datas.get(FIELDS.get(5));
    String startDay = datas.get(FIELDS.get(6));
    String startDate = PrjBriefUtils.getDateString(startYear, startMonth, startDay);
    datas.put("start_date", startDate);

    String endYear = datas.get(FIELDS.get(7));
    String endMonth = datas.get(FIELDS.get(8));
    String endDay = datas.get(FIELDS.get(9));
    String endDate = PrjBriefUtils.getDateString(endYear, endMonth, endDay);
    datas.put("end_date", endDate);

    String amount = datas.get(FIELDS.get(2));
    String amountUnit = datas.get(FIELDS.get(3));
    datas.put("aount_unit", BriefUtils.moneyWithUnit(amount, amountUnit));
    datas = PrjBriefUtils.normalizeData(datas);

    return datas;
  }

  /**
   * @return pattern
   */
  public String getPattern() {
    return pattern;
  }

  /**
   * @return patternEn
   */
  public String getPatternEn() {
    return patternEn;
  }

}
