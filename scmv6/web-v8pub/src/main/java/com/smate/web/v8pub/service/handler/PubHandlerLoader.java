package com.smate.web.v8pub.service.handler;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.support.AopUtils;
import org.springframework.context.ApplicationContext;

import com.smate.web.v8pub.exception.PubHandlerCheckParameterException;
import com.smate.web.v8pub.exception.PubHandlerException;

/**
 * 成果处理实现加载器
 * 
 * @author tsz
 *
 * @date 2018年6月6日
 */
public class PubHandlerLoader {

  protected Logger logger = LoggerFactory.getLogger(getClass());

  private ApplicationContext applicationContext;

  /*
   * 成果处理器存放
   */
  private HashMap<String, PubHandlerService> pubHandlers = new HashMap<String, PubHandlerService>();

  public PubHandlerLoader(ApplicationContext applicationContext) {
    super();
    this.applicationContext = applicationContext;
  }

  /**
   * 获取成果处理器
   * 
   * @param pubHandlerName
   * @return
   * @throws PubHandlerException
   */
  public PubHandlerService getPubHandlerByPubHandlerName(String pubHandlerName)
      throws PubHandlerCheckParameterException {
    PubHandlerService pubHandlerService = pubHandlers.get(pubHandlerName);
    if (pubHandlerService == null) {
      logger.error("没有定义" + pubHandlerName + "成果处理器");
      throw new PubHandlerCheckParameterException("没有定义" + pubHandlerName + "成果处理器");
    }
    return pubHandlerService;
  }

  /**
   * 处理器加载
   */
  public void loadPubHandler() throws PubHandlerException {
    // TODO 有没有可能 是懒加载
    Map<String, Object> beansMap = this.applicationContext.getBeansWithAnnotation(PubHandlerMapping.class);
    if (beansMap == null) {
      logger.debug("没有需要加载的成果处理器");
      return;
    }
    Class<?> type;
    for (Object v : beansMap.values()) {
      // type = v.getClass();
      type = AopUtils.getTargetClass(v);
      if (!PubHandlerServiceBaseBean.class.isAssignableFrom(type)) {
        logger.error("{} 类使用了 @PubHandlermapping 注解 但没有继承PubHandlerServiceBaseBean.class", type.getName());
        throw new PubHandlerException("使用了 @PubHandlermapping 注解 但没有继承PubHandlerServiceBaseBean.class");
      }
      if (!type.getPackage().getName().contains(PubHandlerServiceBaseBean.class.getPackage().getName())) {
        logger.error("{} 类使用了 @PubHandlermapping 注解,该类的包必须为PubHandlerServiceBaseBean 类包的子包", type.getName());
        throw new PubHandlerException("使用了 @PubHandlermapping 注解,该类的包必须为PubHandlerServiceBaseBean 类包的子包");
      }
      // TODO 是否可判断校验写的对不对

      // TODO 校验各个链配置 是否符合规范

      // TODO 判断组装类是否存在死循环

      String name = type.getAnnotation(PubHandlerMapping.class).pubHandlerName();
      pubHandlers.put(name, (PubHandlerService) v);
      logger.debug("成果加载成果处理器" + name);
    }

  }

  /**
   * 成果传输对象检查
   */
  public void checkPubDTO() {
    // TODO 对象检查 主要看有没有不符合规范的属性定义,比如不能使用 深度数组嵌套＝＝
  }

}
