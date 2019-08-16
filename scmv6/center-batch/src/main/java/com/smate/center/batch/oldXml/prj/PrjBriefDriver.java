package com.smate.center.batch.oldXml.prj;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.smate.center.batch.context.OpenProjectContext;

/**
 * 项目Brief生成驱动.
 * 
 * @author liqinghua
 * 
 */
public class PrjBriefDriver implements IPrjBriefDriver {

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
  // ${amount}</money><out>,${funding_year}</out><out> (${source_db_name})</out>";
  private final String pattern =
      "<out>${scheme_agency_name}</out><out>${scheme_name}</out><out>, ${aount_unit}</out><date_interval>, ${start_date,end_date}</date_interval><out></out>";

  private final String patternEn =
      "<out>${scheme_agency_name_en}</out><out>${scheme_name_en}</out><out>, ${aount_unit}</out><date_interval>, ${start_date,end_date}</date_interval><out></out>";

  @Override
  public String getForTmplForm() {
    return ProjectEnterFormEnum.SCHOLAR;
  }

  @Override
  public String getPattern() {
    return pattern;
  }

  @Override
  public String getPatternEn() {
    return patternEn;
  }

  @Override
  public Map<String, String> getData(Locale locale, PrjXmlDocument xmlDocument, OpenProjectContext context)
      throws Exception {
    Map<String, String> datas = PrjBriefUtilis.getFieldsData(FIELDS, xmlDocument);
    // String zhSourceDbName = datas.get(FIELDS.get(5));
    // String enSourceDbName = datas.get(FIELDS.get(6));
    // String sourceDbName =
    // XmlUtil.getLanguageSpecificText(locale.getLanguage(),
    // zhSourceDbName, enSourceDbName);
    // datas.put("source_db_name", sourceDbName);

    String startYear = datas.get(FIELDS.get(4));
    String startMonth = datas.get(FIELDS.get(5));
    String startDay = datas.get(FIELDS.get(6));
    String startDate = PrjBriefUtilis.getDateString(startYear, startMonth, startDay);
    datas.put("start_date", startDate);

    String endYear = datas.get(FIELDS.get(7));
    String endMonth = datas.get(FIELDS.get(8));
    String endDay = datas.get(FIELDS.get(9));
    String endDate = PrjBriefUtilis.getDateString(endYear, endMonth, endDay);
    datas.put("end_date", endDate);

    String amount = datas.get(FIELDS.get(2));
    String amountUnit = datas.get(FIELDS.get(3));
    datas.put("aount_unit", BriefUtilis.moneyWithUnit(amount, amountUnit));
    datas = PrjBriefUtilis.normalizeData(datas);

    return datas;
  }

}
