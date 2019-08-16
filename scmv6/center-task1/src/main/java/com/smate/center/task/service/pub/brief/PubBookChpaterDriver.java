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

/**
 * @author yamingd 书籍章节Brief生成驱动.
 */
public class PubBookChpaterDriver implements IBriefDriver {
  /**
   * 字段.
   */
  private static final List<String> FIELDS = new ArrayList<String>();
  static {
    FIELDS.add("/pub_book/@book_title");
    // FIELDS.add("/pub_book/@series_name");
    FIELDS.add("/pub_book/@editors");
    FIELDS.add("/pub_book/@publisher");
    FIELDS.add("/publication/@publish_year");
    FIELDS.add("/publication/@publish_month");
    FIELDS.add("/publication/@publish_day");
    // pub_date_desc不需要
    // FIELDS.add("/publication/@pub_date_desc");
    FIELDS.add("/publication/@start_page");
    FIELDS.add("/publication/@end_page");
    // FIELDS.add("/pub_book/@total_pages");
    FIELDS.add("/publication/@country_name");
    FIELDS.add("/publication/@zh_country_name");
    FIELDS.add("/publication/@en_country_name");
    // FIELDS.add("/pub_meta/@zh_source_db_name");
    // FIELDS.add("/pub_meta/@en_source_db_name");
  }

  /**
   * 格式化Pattern.
   */
  private final String pattern =
      "<out>${book_title}</out><out>, ${editors}</out><out>, ${publisher}</out><page>, ${start_page,end_page}</page><out>, ${country_name}</out><date>, ${publish_date}</date><out></out>";

  @Override
  public Map<String, String> getData(Locale locale, PubXmlDocument xmlDocument, PubXmlProcessContext context)
      throws Exception {

    Map<String, String> datas = BriefUtilis.getFieldsData(FIELDS, xmlDocument);
    String year = datas.get(FIELDS.get(3));
    String month = datas.get(FIELDS.get(4));
    String day = datas.get(FIELDS.get(5));
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

    return PublicationTypeEnum.BOOK_CHAPTER;
  }

  @Override
  public String getPattern() {

    return this.pattern;
  }
}
