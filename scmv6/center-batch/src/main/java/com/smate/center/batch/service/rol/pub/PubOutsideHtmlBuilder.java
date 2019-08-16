package com.smate.center.batch.service.rol.pub;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.smate.center.batch.constant.PubHtmlContants;
import com.smate.center.batch.constant.TemplateConstants;
import com.smate.center.batch.exception.pub.ServiceException;

/**
 * 成果列表Html
 * 
 * @author Scy
 * 
 */
public class PubOutsideHtmlBuilder extends PubAbstractHtmlBuilder {

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  @Override
  public Map<String, Object> invoke(Object... params) throws ServiceException {
    try {
      Map<String, Object> resultMap = super.getCommonParams(params);
      resultMap.put(TemplateConstants.EN_TEMPLATE, "publication_outside_list_template_en_US.ftl");
      resultMap.put(TemplateConstants.ZH_TEMPLATE, "publication_outside_list_template_zh_CN.ftl");
      return resultMap;
    } catch (Exception e) {
      logger.error("成果html模板构造时，解析参数出现问题!", e);
      throw new ServiceException("成果html模板构造时，解析参数出现问题!", e);
    }
  }

  @Override
  public Integer getTempCode() throws ServiceException {
    return PubHtmlContants.PUB_OUTSIDE_TEM_CODE;
  }

}
