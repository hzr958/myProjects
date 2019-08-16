package com.smate.web.v8pub.service.handler;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.web.v8pub.enums.PubHandlerStatusEnum;
import com.smate.web.v8pub.exception.PubHandlerCheckParameterException;
import com.smate.web.v8pub.exception.PubHandlerException;
import com.smate.web.v8pub.exception.ServiceException;

/**
 * 成果数据服务实现
 * 
 * @author tsz
 *
 * @date 2018年6月6日
 */
public class PubDataServiceImpl implements PubDataService, InitializingBean, ApplicationContextAware {

  Logger logger = LoggerFactory.getLogger(getClass());

  private PubHandlerLoader pubHandlerLoader;// 成果处理器加载器

  /**
   * 调用具体的服务
   */
  @Override
  public String pubHandleByType(PubDTO pub) throws ServiceException {
    Map<String, String> result = new HashMap<>();
    try {
      // TODO 前期参数校验 数据格式转换 (统一数据格式转换)
      checkCommonParameter(pub);
      PubHandlerService pubhandlerService = pubHandlerLoader.getPubHandlerByPubHandlerName(pub.pubHandlerName);
      result = pubhandlerService.pubHandle(pub);

      result.put("status", PubHandlerStatusEnum.SUCCESS.getValue());
    } catch (PubHandlerCheckParameterException e) {
      logger.error("参数检验出错" + e.getMessage(), e);
      result.put("status", PubHandlerStatusEnum.ERROR.getValue());
      result.put("msg", e.getMessage());
    } catch (PubHandlerException e) {
      logger.error("成果处理出错" + e.getMessage(), e);
      result.put("status", PubHandlerStatusEnum.ERROR.getValue());
      result.put("msg", e.getMessage());
    } catch (Exception e) {
      logger.error("调用成果处理器出错" + e.getMessage(), e);
      result.put("status", PubHandlerStatusEnum.ERROR.getValue());
      result.put("msg", e.getMessage());
    }
    return JacksonUtils.jsonMapSerializer(result);
  }

  /**
   * 公用参数 校验
   * 
   * @param pub
   * @throws PubHandlerCheckParameterException
   */
  void checkCommonParameter(PubDTO pub) throws PubHandlerCheckParameterException {
    if (StringUtils.isBlank(pub.pubHandlerName)) {
      throw new PubHandlerCheckParameterException("参数 pubHandlerName 不能为空");
    }
  }

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    pubHandlerLoader = new PubHandlerLoader(applicationContext);
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    logger.debug("加载并验证所有的成果处理器");
    pubHandlerLoader.loadPubHandler();
    logger.debug("验证PubDTO");
    pubHandlerLoader.checkPubDTO();
  }

}
