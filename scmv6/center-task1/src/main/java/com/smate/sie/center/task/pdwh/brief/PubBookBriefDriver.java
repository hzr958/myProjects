package com.smate.sie.center.task.pdwh.brief;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.smate.center.task.single.enums.pub.PublicationEnterFormEnum;
import com.smate.core.base.pub.enums.PublicationTypeEnum;
import com.smate.core.base.utils.data.XmlUtil;
import com.smate.sie.center.task.pdwh.task.PubXmlDocument;
import com.smate.sie.center.task.pdwh.task.PubXmlProcessContext;
import com.smate.sie.center.task.pdwh.utils.BriefUtilis;

/**
 * @author yamingd 书籍类型Brief生成驱动.
 */
public class PubBookBriefDriver implements IBriefDriver {

  /**
   * 字段.
   */
  private static final List<String> FIELDS = new ArrayList<String>();
  static {
    FIELDS.add("/pub_book/@publisher");
    FIELDS.add("/publication/@publish_year");
    FIELDS.add("/publication/@publish_month");
    FIELDS.add("/publication/@publish_day");
    FIELDS.add("/pub_book/@language");
  }

  /**
   * 格式化Pattern.
   */
  private final String pattern =
      "<out>${publisher}</out><page>, ${total_pages}</page><date>, ${publish_date}</date><out>, ${language}</out><out></out>";

  @Override
  public Map<String, String> getData(Locale locale, PubXmlDocument xmlDocument, PubXmlProcessContext context)
      throws Exception {

    Map<String, String> datas = BriefUtilis.getFieldsData(FIELDS, xmlDocument);
    String year = datas.get(FIELDS.get(1));
    String month = datas.get(FIELDS.get(2));
    String day = datas.get(FIELDS.get(3));
    String publishDate = BriefUtilis.getStandardDateString(year, month, day);
    datas.put("publish_date", publishDate);
    String language = datas.get(FIELDS.get(4));
    if (StringUtils.isNotBlank(language)) {
      // 兼容处理语言被设置为1和2的情况，其他不变
      if ("1".equals(language)) {
        language = XmlUtil.getLanguageSpecificText(locale.getLanguage(), "中文", "Chinese");
        datas.put("/pub_book/@language", language);
      } else if ("2".endsWith(language)) {
        language = XmlUtil.getLanguageSpecificText(locale.getLanguage(), "外文", "Foreign language");
        datas.put("/pub_book/@language", language);
      }
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

    return PublicationTypeEnum.BOOK;
  }

  @Override
  public String getPattern() {

    return this.pattern;
  }

}
