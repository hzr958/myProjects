package com.smate.center.batch.service.rol.pub;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import com.smate.center.batch.constant.TemplateConstants;
import com.smate.center.batch.exception.pub.ServiceException;

import freemarker.template.Configuration;

/**
 * freemarker构造工具类
 * 
 * @author zk
 * 
 */

@Component("freemarkerBuilderUtils")
public class FreemarkerBuilderUtils {

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private Configuration freemarkerConfiguration;

  /**
   * 构造freemarker模板
   * 
   * @param params
   * @return
   * @throws ServiceException
   */
  public Map<String, String> getTemplateContent(Map<String, Object> params) throws ServiceException {
    Map<String, String> returnMap = null;
    try {
      returnMap = new HashMap<String, String>();
      // 构造中文html
      String zhContent = FreeMarkerTemplateUtils.processTemplateIntoString(freemarkerConfiguration.getTemplate(
          ObjectUtils.toString(params.get(TemplateConstants.ZH_TEMPLATE)), TemplateConstants.ENCODING), params);
      // 构造英文html
      String enContent = FreeMarkerTemplateUtils.processTemplateIntoString(freemarkerConfiguration.getTemplate(
          ObjectUtils.toString(params.get(TemplateConstants.EN_TEMPLATE)), TemplateConstants.ENCODING), params);
      // 保存中英文结果
      returnMap.put(TemplateConstants.ZH_HTML, zhContent);
      returnMap.put(TemplateConstants.EN_HTML, enContent);
      return returnMap;
    } catch (Exception e) {
      logger.error("构造freemarker模板出错", e);
      return returnMap;
    }
  }
}
