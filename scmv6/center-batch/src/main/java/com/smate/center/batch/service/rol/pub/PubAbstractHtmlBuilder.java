package com.smate.center.batch.service.rol.pub;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.smate.center.batch.constant.TemplateConstants;
import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.oldXml.pub.PubXmlDocument;
import com.smate.core.base.utils.constant.PubXmlConstants;
import com.smate.core.base.utils.string.ServiceUtil;

/**
 * 成果公共HTML生成类，主要用于解析公共参数
 * 
 * @author Scy
 * 
 */
public abstract class PubAbstractHtmlBuilder extends AbstractFtlBuilder {

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  /**
   * 解析公共参数
   * 
   * @param params
   * @return
   * @throws ServiceException
   */
  public Map<String, Object> getCommonParams(Object... params) throws ServiceException {
    try {
      Map<String, Object> resultMap = new HashMap<String, Object>();
      PubXmlDocument xmlDocument = (PubXmlDocument) params[0];
      String zhTitle = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "zh_title_text");
      String enTitle = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "en_title_text");
      resultMap.put("zhTitle", zhTitle);
      resultMap.put("enTitle", enTitle);
      resultMap.put("authorNames", xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "author_names"));
      resultMap.put("briefDescZh", xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "brief_desc"));
      resultMap.put("briefDescEn", xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "brief_desc_en"));
      Long pubId = xmlDocument.getPubId();
      resultMap.put("pubId", pubId);
      resultMap.put(TemplateConstants.OBJ_CODE, pubId);
      resultMap.put("des3PubId", ServiceUtil.encodeToDes3(pubId.toString()));
      resultMap.put("nodeId", xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUB_META_XPATH, "record_node_id"));
      resultMap.put("dbCode", xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "source_db_code"));
      return resultMap;
    } catch (Exception e) {
      logger.error("成果html模板构造时，获取公共参数出现问题!", e);
      throw new ServiceException("成果html模板构造时，获取公共参数出现问题!", e);
    }
  }
}
