package com.smate.center.task.service.pub.brief;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.smate.center.task.model.sns.quartz.PubXmlProcessContext;
import com.smate.center.task.single.enums.pub.PublicationEnterFormEnum;
import com.smate.center.task.single.oldXml.pub.PubXmlDocument;
import com.smate.center.task.single.service.pub.IBriefDriver;
import com.smate.center.task.utils.BriefUtilis;
import com.smate.center.task.utils.XmlFragmentCleanerHelper;
import com.smate.core.base.pub.enums.PublicationTypeEnum;
import com.smate.core.base.utils.constant.PubXmlConstants;
import com.smate.core.base.utils.data.XmlUtil;

public class PubConfPaperBriefDriver implements IBriefDriver {
  /**
   * 字段.
   */
  private static final List<String> FIELDS = new ArrayList<String>();
  private static final Map<String, String> ZHPAPERTYPE = new HashMap<String, String>();
  private static final Map<String, String> ENPAPERTYPE = new HashMap<String, String>();
  static {
    FIELDS.add("/pub_conf_paper/@conf_name");
    FIELDS.add("/pub_conf_paper/@start_date");
    FIELDS.add("/pub_conf_paper/@end_date");
    FIELDS.add("/publication/@start_page");
    FIELDS.add("/publication/@end_page");
    FIELDS.add("/publication/@country_name");
    FIELDS.add("/publication/@city");
    FIELDS.add("/publication/@publish_year");
    FIELDS.add("/publication/@publish_month");
    FIELDS.add("/publication/@publish_day");
    FIELDS.add("/pub_conf_paper/@paper_type");

    ZHPAPERTYPE.put("A", "特邀报告");
    ZHPAPERTYPE.put("B", "口头报告");
    ZHPAPERTYPE.put("C", "墙报展示");

    ENPAPERTYPE.put("A", "Invited Presentation");
    ENPAPERTYPE.put("B", "Oral Presentation");
    ENPAPERTYPE.put("C", "Poster Presentation");
  }

  /**
   * 格式化Pattern.
   */
  private final String pattern =
      "<out>${conf_name}</out><date_interval>, ${start_date,end_date}</date_interval><page>, ${start_page,end_page}</page><out>, ${city}</out><date>, ${publish_date}</date><out>, ${confType}</out><out></out>";

  @Override
  public Map<String, String> getData(Locale locale, PubXmlDocument xmlDocument, PubXmlProcessContext context)
      throws Exception {

    Map<String, String> datas = BriefUtilis.getFieldsData(FIELDS, xmlDocument);
    // scm-6684 先获取开始日期和结束日期，拆分成年月日，再根据locale获取对应的格式的日期
    String chsDatePattern = PubXmlConstants.CHS_DATE_PATTERN;
    String engDatePattern = PubXmlConstants.ENG_DATE_PATTERN;
    String lang = context.getCurrentLanguage();
    String pattern = "zh".equalsIgnoreCase(lang) ? chsDatePattern : engDatePattern;
    String startDate = datas.get(FIELDS.get(1));
    String endDate = datas.get(FIELDS.get(2));
    String[] startDates = null;
    String[] endDates = null;
    if (startDate != null && !"".equals(startDate)) {
      startDates = XmlFragmentCleanerHelper.getDateYearMonth(pattern, startDate);
    }
    if (endDate != null && !"".equals(endDate)) {
      endDates = XmlFragmentCleanerHelper.getDateYearMonth(pattern, endDate);
    }
    if (startDates != null) {
      startDate = BriefUtilis.getDateString(locale, startDates[0], startDates[1], startDates[2]);
      datas.put("start_date", startDate);
    }
    if (endDates != null) {
      endDate = BriefUtilis.getDateString(locale, endDates[0], endDates[1], endDates[2]);
      datas.put("end_date", endDate);
    }
    String year = datas.get(FIELDS.get(7));
    String month = datas.get(FIELDS.get(8));
    String day = datas.get(FIELDS.get(9));
    String publishDate = BriefUtilis.getStandardDateString(year, month, day);
    datas.put("publish_date", publishDate);

    String confType = datas.get(FIELDS.get(10));
    confType =
        XmlUtil.getLanguageSpecificText(locale.getLanguage(), ZHPAPERTYPE.get(confType), ENPAPERTYPE.get(confType));
    datas.put("confType", confType);
    datas = BriefUtilis.normalizeData(datas);

    return datas;
  }

  @Override
  public String getForTmplForm() {

    return PublicationEnterFormEnum.SCHOLAR;
  }

  @Override
  public int getForType() {

    return PublicationTypeEnum.CONFERENCE_PAPER;
  }

  @Override
  public String getPattern() {

    return this.pattern;
  }
}
