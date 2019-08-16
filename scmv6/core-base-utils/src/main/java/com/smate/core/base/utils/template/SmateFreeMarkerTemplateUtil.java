package com.smate.core.base.utils.template;

import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import com.smate.core.base.utils.constant.EmailConstants;
import com.smate.core.base.utils.exception.DynException;

import freemarker.template.Configuration;

/**
 * 
 * @author zjh 构造模板工具类
 *
 */
@Component("smateFreeMarkerTemplateUtil")
public class SmateFreeMarkerTemplateUtil {

  protected final Logger logger = LoggerFactory.getLogger(SmateFreeMarkerTemplateUtil.class);

  @Resource(name = "freemarkerConfiguration")
  private Configuration freemarkerConfiguration;

  /**
   * 填充数据构造模板
   * 
   * @param params
   * @return
   * @throws DynException
   */
  public String produceTemplate(Map<String, Object> params, String dynTemplate) throws DynException {
    String content = "";
    try {
      content = FreeMarkerTemplateUtils
          .processTemplateIntoString(freemarkerConfiguration.getTemplate(dynTemplate, EmailConstants.ENCODING), params);
    } catch (Exception e) {
      logger.error("生成模板数据出错", e);
      throw new DynException(e);

    }
    return content;

  }

}
