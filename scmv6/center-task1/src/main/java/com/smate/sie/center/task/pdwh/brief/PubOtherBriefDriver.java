package com.smate.sie.center.task.pdwh.brief;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.smate.center.task.single.enums.pub.PublicationEnterFormEnum;
import com.smate.core.base.pub.enums.PublicationTypeEnum;
import com.smate.sie.center.task.pdwh.task.PubXmlDocument;
import com.smate.sie.center.task.pdwh.task.PubXmlProcessContext;
import com.smate.sie.center.task.pdwh.utils.BriefUtilis;

/**
 * @author yamingd Others类型Brief生成驱动.
 */
public class PubOtherBriefDriver implements IBriefDriver {

  /**
   * 字段.
   */
  private static final List<String> FIELDS = new ArrayList<String>();
  static {
    FIELDS.add("/publication/@publish_year");
    FIELDS.add("/publication/@publish_month");
    FIELDS.add("/publication/@publish_day");
    FIELDS.add("/publication/@country_name");
  }

  /**
   * 格式化Pattern.
   */
  private final String pattern = "<out>, ${country_name}</out><out>, ${publish_date}</out><out></out>";

  @Override
  public Map<String, String> getData(Locale locale, PubXmlDocument xmlDocument, PubXmlProcessContext context)
      throws Exception {

    Map<String, String> datas = BriefUtilis.getFieldsData(FIELDS, xmlDocument);
    String year = datas.get(FIELDS.get(0));
    String month = datas.get(FIELDS.get(1));
    String day = datas.get(FIELDS.get(2));
    String publishDate = BriefUtilis.getDateString(locale, year, month, day);
    datas.put("publish_date", publishDate);

    String zhCountryName = datas.get(FIELDS.get(3));
    if (!"".equals(zhCountryName)) {
      datas.put("country_name", zhCountryName);
    }
    datas = BriefUtilis.normalizeData(datas);

    return datas;
  }

  @Override
  public String getForTmplForm() {

    return PublicationEnterFormEnum.SCHOLAR;
  }

  @Override
  public int getForType() {

    return PublicationTypeEnum.OTHERS;
  }

  @Override
  public String getPattern() {

    return this.pattern;
  }

}
