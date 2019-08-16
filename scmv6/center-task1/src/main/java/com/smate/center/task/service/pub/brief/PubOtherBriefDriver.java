package com.smate.center.task.service.pub.brief;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.smate.center.task.model.sns.quartz.PubXmlProcessContext;
import com.smate.center.task.single.enums.pub.PublicationEnterFormEnum;
import com.smate.center.task.single.oldXml.pub.PubXmlDocument;
import com.smate.center.task.single.service.pub.IBriefDriver;
import com.smate.center.task.utils.BriefUtilis;
import com.smate.core.base.pub.enums.PublicationTypeEnum;
import com.smate.core.base.utils.data.XmlUtil;

public class PubOtherBriefDriver implements IBriefDriver {
  /**
   * 字段.
   */
  private static final List<String> FIELDS = new ArrayList<String>();
  static {
    // client不需要
    FIELDS.add("/pub_other/@client");
    FIELDS.add("/publication/@publish_year");
    FIELDS.add("/publication/@publish_month");
    FIELDS.add("/publication/@publish_day");
    FIELDS.add("/publication/@pub_date_desc");
    FIELDS.add("/publication/@country_name");
    FIELDS.add("/publication/@zh_country_name");
    FIELDS.add("/publication/@en_country_name");
    FIELDS.add("/pub_meta/@zh_source_db_name");
    FIELDS.add("/pub_meta/@en_source_db_name");
  }

  /**
   * 格式化Pattern.
   */
  private final String pattern = "<out>, ${country_name}</out><out>, ${publish_date}</out><out></out>";

  @Override
  public Map<String, String> getData(Locale locale, PubXmlDocument xmlDocument, PubXmlProcessContext context)
      throws Exception {

    Map<String, String> datas = BriefUtilis.getFieldsData(FIELDS, xmlDocument);
    String year = datas.get(FIELDS.get(1));
    String month = datas.get(FIELDS.get(2));
    String day = datas.get(FIELDS.get(3));
    // scm-6684 方法中加入了一个locale参数便于获取中英文不同格式日期
    String publishDate = BriefUtilis.getDateString(locale, year, month, day);
    if (locale.getLanguage().equalsIgnoreCase("zh_CN") || locale.getLanguage().equalsIgnoreCase("zh")) {
      datas.put("publish_date", publishDate + " " + datas.get(FIELDS.get(4)));
    } else {
      datas.put("publish_date", datas.get(FIELDS.get(4)) + " " + publishDate);
    }

    String zhSourceDbName = datas.get(FIELDS.get(8));
    String enSourceDbName = datas.get(FIELDS.get(9));
    String sourceDbName = XmlUtil.getLanguageSpecificText(locale.getLanguage(), zhSourceDbName, enSourceDbName);
    datas.put("source_db_name", sourceDbName);

    String zhCountryName = datas.get(FIELDS.get(6));
    String enCountryName = datas.get(FIELDS.get(7));
    String countryName = XmlUtil.getLanguageSpecificText(locale.getLanguage(), zhCountryName, enCountryName);
    if (!"".equals(countryName)) {
      datas.put("country_name", countryName);
    } else {
      countryName = datas.get(FIELDS.get(5));
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

    return PublicationTypeEnum.OTHERS;
  }

  @Override
  public String getPattern() {

    return this.pattern;
  }

}
