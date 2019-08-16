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

/**
 * @author yamingd 奖励类型Brief生成驱动.
 */
public class PubAwardBriefDriver implements IBriefDriver {

  /**
   * 字段.
   */
  private static final List<String> FIELDS = new ArrayList<String>();
  static {
    FIELDS.add("/pub_award/@issue_ins_name");
    FIELDS.add("/pub_award/@award_category");
    FIELDS.add("/pub_award/@award_grade");
    FIELDS.add("/publication/@publish_year");
    FIELDS.add("/publication/@publish_month");
    FIELDS.add("/publication/@publish_day");
  }

  /**
   * 格式化Pattern.
   */
  private final String pattern =
      "<out>${issue_ins_name}</out><out>, ${award_category}</out><out>, ${award_grade}</out><date>, ${award_date}</date><out></out>";

  @Override
  public Map<String, String> getData(Locale locale, PubXmlDocument xmlDocument, PubXmlProcessContext context)
      throws Exception {

    Map<String, String> datas = BriefUtilis.getFieldsData(FIELDS, xmlDocument);

    String year = datas.get(FIELDS.get(3));
    String month = datas.get(FIELDS.get(4));
    String day = datas.get(FIELDS.get(5));
    String awardDate = BriefUtilis.getStandardDateString(year, month, day);
    datas.put("award_date", awardDate);

    datas = BriefUtilis.normalizeData(datas);

    return datas;
  }

  @Override
  public String getForTmplForm() {

    return PublicationEnterFormEnum.SCHOLAR;
  }

  @Override
  public int getForType() {
    return PublicationTypeEnum.AWARD;
  }

  @Override
  public String getPattern() {
    return this.pattern;
  }

}
