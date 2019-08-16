package com.smate.center.batch.service.pub.brief;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.smate.center.batch.enums.pub.PublicationEnterFormEnum;
import com.smate.center.batch.model.sns.pub.PubXmlProcessContext;
import com.smate.center.batch.oldXml.prj.BriefUtilis;
import com.smate.center.batch.oldXml.pub.PubXmlDocument;
import com.smate.center.batch.service.pub.IBriefDriver;
import com.smate.center.batch.util.pub.XmlFragmentCleanerHelper;
import com.smate.core.base.pub.enums.PublicationTypeEnum;
import com.smate.core.base.utils.constant.PubXmlConstants;

/**
 * @author yamingd 专利Brief生成驱动.
 */
public class PubPatentBriefDriver implements IBriefDriver {

  /**
   * 字段.
   */
  private static final List<String> FIELDS = new ArrayList<String>();
  static {
    FIELDS.add("/publication/@publish_date");
    /*
     * FIELDS.add("/publication/@country_name"); FIELDS.add("/publication/@zh_country_name");
     * FIELDS.add("/publication/@en_country_name");
     */
    FIELDS.add("/pub_patent/@patent_org");
    FIELDS.add("/pub_patent/@patent_no");
    FIELDS.add("/publication/@start_date");
    FIELDS.add("/pub_patent/@agent_org");

  }

  /**
   * 格式化Pattern.
   */
  private final String pattern =
      "<out>${start_date}</out><out>, ${patent_org}</out><out>, ${patent_no}</out><out></out>";

  @Override
  public Map<String, String> getData(Locale locale, PubXmlDocument xmlDocument, PubXmlProcessContext context)
      throws Exception {
    Map<String, String> datas = BriefUtilis.getFieldsData(FIELDS, xmlDocument);
    // scm-6684 获取start_date先拆成年月日，再根据locale获取对应格式的日期
    String chsDatePattern = PubXmlConstants.CHS_DATE_PATTERN;
    String engDatePattern = PubXmlConstants.ENG_DATE_PATTERN;
    String lang = context.getCurrentLanguage();
    String pattern = "zh".equalsIgnoreCase(lang) ? chsDatePattern : engDatePattern;
    String startDate = datas.get(FIELDS.get(0));
    if (startDate == null) {
      startDate = datas.get(FIELDS.get(3));
    }
    String[] dates = null;
    if (startDate != null && !"".equals(startDate)) {
      dates = XmlFragmentCleanerHelper.getDateYearMonth(pattern, startDate);
    }
    if (dates != null) {
      startDate = BriefUtilis.getDateString(locale, dates[0], dates[1], dates[2]);
      datas.put("start_date", startDate);
    }
    String patent_org = datas.get(FIELDS.get(1));
    if (StringUtils.isBlank(patent_org)) {
      patent_org = datas.get(FIELDS.get(4));
    }
    datas.put("patent_org", patent_org);
    /*
     * String country = datas.get(FIELDS.get(1)); String zhCountryName = datas.get(FIELDS.get(2));
     * String enCountryName = datas.get(FIELDS.get(3)); String countryName =
     * XmlUtil.getLanguageSpecificText(locale.getLanguage(), zhCountryName, enCountryName);
     * if(!"".equals(country)){ datas.put("country_name", country); }else if (!"".equals(countryName)) {
     * datas.put("country_name", countryName); } else { countryName = datas.get(FIELDS.get(1)); if
     * (!"".equals(countryName)) datas.put("country_name", countryName); }
     */
    datas = BriefUtilis.normalizeData(datas);

    return datas;
  }

  @Override
  public String getForTmplForm() {

    return PublicationEnterFormEnum.SCHOLAR;
  }

  @Override
  public int getForType() {

    return PublicationTypeEnum.PATENT;
  }

  @Override
  public String getPattern() {

    return this.pattern;
  }

}
