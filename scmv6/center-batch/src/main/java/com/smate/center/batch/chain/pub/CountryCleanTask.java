package com.smate.center.batch.chain.pub;

import java.util.Locale;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;

import com.smate.center.batch.model.sns.pub.ConstRegion;
import com.smate.center.batch.model.sns.pub.PubXmlProcessContext;
import com.smate.center.batch.oldXml.pub.PubXmlDocument;
import com.smate.center.batch.service.pub.ConstRegionService;
import com.smate.center.batch.service.pub.IPubXmlTask;
import com.smate.core.base.utils.constant.PubXmlConstants;
import com.smate.core.base.utils.data.XmlUtil;


/**
 * 读取国家/地区.
 * 
 * @author yamingd
 */
public class CountryCleanTask implements IPubXmlTask {

  /**
   * 
   */
  private final Logger logger = LoggerFactory.getLogger(getClass());
  /**
   * 
   */
  private final String name = "country_clean";

  @Autowired
  private ConstRegionService constRegionService;

  @Override
  public String getName() {
    return this.name;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.iris.scm.xml.IXmlTask#can(com.iris.scm.xml.XmlDocument,
   * com.iris.scm.xml.XmlProcessContext)
   */
  @Override
  public boolean can(PubXmlDocument xmlDocument, PubXmlProcessContext context) {
    return true;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.iris.scm.xml.IXmlTask#run(com.iris.scm.xml.XmlDocument,
   * com.iris.scm.xml.XmlProcessContext)
   */
  @Override
  public boolean run(PubXmlDocument xmlDocument, PubXmlProcessContext context) throws Exception {

    String country = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "country");
    String countryName = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "country_name");
    country = StringUtils.isBlank(country) ? countryName : country;
    Locale locale = LocaleContextHolder.getLocale();
    // 国家处理
    if (StringUtils.isNotBlank(country)) {
      ConstRegion region = null;
      try {
        region = constRegionService.getConstRegionByName(country);
      } catch (Exception e) {
        logger.error("读取国家/地区错误", e);
      }
      if (region != null) {
        xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "country_id",
            String.valueOf(region.getId()));
        xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "zh_country_name", region.getZhName());
        xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "en_country_name", region.getEnName());
        if (StringUtils.isBlank(countryName)) {
          countryName = XmlUtil.getLanguageSpecificText(locale.getLanguage(), region.getZhName(), region.getEnName());
          xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "country_name", countryName);
        }
      } else {
        xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "country_name", country);
        xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "zh_country_name", country);
        xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "en_country_name", country);
        logger.warn("找不到国家/地区, 参数=" + country);
      }
    }
    // conf_venue 城市处理 (导入) city（录入）
    String confVenue = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "conf_venue");
    String city = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "city");
    if (StringUtils.isNotBlank(city)) {
      xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "city", city);
    } else if (StringUtils.isNotBlank(confVenue)) {
      xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "city", confVenue);
    }
    return true;
  }

}
