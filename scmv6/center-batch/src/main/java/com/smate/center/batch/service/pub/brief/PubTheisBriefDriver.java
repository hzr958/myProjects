/**
 * 
 */
package com.smate.center.batch.service.pub.brief;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.smate.center.batch.enums.pub.PublicationEnterFormEnum;
import com.smate.center.batch.model.sns.pub.PubXmlProcessContext;
import com.smate.center.batch.oldXml.prj.BriefUtilis;
import com.smate.center.batch.oldXml.pub.PubXmlDocument;
import com.smate.center.batch.service.pub.IBriefDriver;
import com.smate.core.base.pub.enums.PublicationTypeEnum;
import com.smate.core.base.utils.data.XmlUtil;

/**
 * @author yamingd 学位论文Brief生成驱动.
 */
public class PubTheisBriefDriver implements IBriefDriver {

  /**
   * 字段.
   */
  private static final List<String> FIELDS = new ArrayList<String>();
  static {
    FIELDS.add("/pub_thesis/@department");
    FIELDS.add("/pub_thesis/@zh_programme_name");
    FIELDS.add("/pub_thesis/@en_programme_name");
    FIELDS.add("/pub_thesis/@issue_org");
    FIELDS.add("/publication/@publish_year");
    FIELDS.add("/publication/@publish_month");
    FIELDS.add("/publication/@publish_day");
    // pub_date_desc不需要
    FIELDS.add("/publication/@pub_date_desc");
    FIELDS.add("/publication/@country_name");
    FIELDS.add("/publication/@zh_country_name");
    FIELDS.add("/publication/@en_country_name");
  }

  /**
   * 格式化Pattern.
   */
  private final String pattern =
      "<out>${department}</out><out>, ${programme}</out><out>, ${issue_org}</out><out>, ${country_name}</out><date>, ${publish_date}</date><out></out>";

  @Override
  public Map<String, String> getData(Locale locale, PubXmlDocument xmlDocument, PubXmlProcessContext context)
      throws Exception {

    Map<String, String> datas = BriefUtilis.getFieldsData(FIELDS, xmlDocument);

    String zhProgrammeName = datas.get(FIELDS.get(1));
    String enProgrammeName = datas.get(FIELDS.get(2));
    String programme = XmlUtil.getLanguageSpecificText(locale.getLanguage(), zhProgrammeName, enProgrammeName);
    datas.put("programme", programme);

    String year = datas.get(FIELDS.get(4));
    String month = datas.get(FIELDS.get(5));
    String day = datas.get(FIELDS.get(6));
    String publishDate = BriefUtilis.getStandardDateString(year, month, day);
    datas.put("publish_date", publishDate);

    String zhCountryName = datas.get(FIELDS.get(9));
    String enCountryName = datas.get(FIELDS.get(10));
    String countryName = XmlUtil.getLanguageSpecificText(locale.getLanguage(), zhCountryName, enCountryName);
    if (!"".equals(countryName)) {
      datas.put("country_name", countryName);
    } else {
      countryName = datas.get(FIELDS.get(8));
      if (!"".equals(countryName))
        datas.put("country_name", countryName);
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
    return PublicationTypeEnum.THESIS;
  }

  @Override
  public String getPattern() {

    return this.pattern;
  }

}
