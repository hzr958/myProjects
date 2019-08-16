package com.smate.sie.center.task.pdwh.task;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.core.base.utils.constant.PubXmlConstants;
import com.smate.core.base.utils.exception.SysServiceException;
import com.smate.core.base.utils.model.consts.SieConstRegion;
import com.smate.core.base.utils.service.consts.SieConstRegionService;
import com.smate.core.base.utils.string.IrisNumberUtils;

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
  private SieConstRegionService sieConstRegionService;

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

    String countryIdStr = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "country_id");
    // 国家处理
    if (StringUtils.isNotBlank(countryIdStr)) {
      SieConstRegion region = null;
      Long countryId = IrisNumberUtils.createLong(countryIdStr);
      try {
        region = sieConstRegionService.getRegionById(countryId);
      } catch (Exception e) {
        logger.error("读取国家/地区错误", e);
        throw new SysServiceException("匹配国家/地区出错");
      }
      if (region != null) {
        xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "country_name", region.getZhName());
        xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "zh_country_name", region.getZhName());
        xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "en_country_name", region.getEnName());
      } else {
        xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "country_name", "");
        xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "zh_country_name", "");
        xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "en_country_name", "");
        logger.warn("找不到国家/地区");
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
