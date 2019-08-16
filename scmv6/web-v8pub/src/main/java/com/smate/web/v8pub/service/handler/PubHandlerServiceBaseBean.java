package com.smate.web.v8pub.service.handler;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.smate.web.v8pub.exception.PubHandlerAssemblyException;
import com.smate.web.v8pub.exception.PubHandlerCheckParameterException;
import com.smate.web.v8pub.exception.PubHandlerException;
import com.smate.web.v8pub.service.PubErrorMessageService;
import com.smate.web.v8pub.service.handler.assembly.PubHandlerAssemblyService;

/**
 * 成果处理基类
 * 
 * @author tsz
 *
 * @date 2018年6月6日
 * @param <T>组装实现类
 *
 */
@Transactional(rollbackFor = Exception.class)
public abstract class PubHandlerServiceBaseBean
    implements PubHandlerService, InitializingBean, ApplicationContextAware {

  Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private PubErrorMessageService pubErrorMessageService;

  private ApplicationContext applicationContext;

  /**
   * 要用到的参数 以及对应配置
   */
  private List<CheckConfig> checkConfigList = new ArrayList<CheckConfig>();

  /**
   * 设置校验参数 需要的参数 必须配置，不然取不到参数
   * 
   * @param checkConfigList
   */
  protected abstract void setCheckConfig(List<CheckConfig> checkConfigList);

  /**
   * 具体参数校验 验证不通过就抛异常
   * 
   * @param from
   * @throws PubHandlerCheckParameterException
   */
  protected abstract void checkParameter(PubDTO pub) throws PubHandlerCheckParameterException;

  /**
   * 具体业务处理 出错就抛出异常
   * 
   * @param from
   * @throws PubHandlerException
   */
  protected Map<String, String> handle(PubDTO pub, List<Object> ASChain) throws PubHandlerException {
    Map<String, String> result = new HashMap<String, String>();
    logger.debug("准备调用成果处理链");
    chainTwoForEach(ASChain, pub, result);
    // 循环调用检查参数参数
    logger.debug("成果处理成功");
    return result;
  }

  @SuppressWarnings("unchecked")
  private Map<String, String> chainTwoForEach(List<Object> ASChain, PubDTO pub, Map<String, String> result) {
    ASChain.forEach((v) -> {
      if (v instanceof PubHandlerAssemblyService) {
        boolean excete = true;
        try {
          ((PubHandlerAssemblyService) v).checkRebuildParameter(pub);
        } catch (Exception e) {
          pubErrorMessageService.save(pub, e.getMessage());
          excete = false;
        }
        if (excete) {
          try {
            Map<String, String> map = ((PubHandlerAssemblyService) v).excute(pub);
            if (!CollectionUtils.isEmpty(map)) {
              result.putAll(map);
            }
          } catch (Exception e) {
            pubErrorMessageService.save(pub, e.getMessage());
            throw new PubHandlerException("执行成果组装类业务出错", e);
          }
        }
      }
      if (v instanceof ArrayList) {
        chainTwoForEach((ArrayList<Object>) v, pub, result);
      }
    });
    return result;
  }

  /**
   * 处理调用
   */
  @SuppressWarnings("unchecked")
  @Override
  public Map<String, String> pubHandle(PubDTO pub) throws PubHandlerException {
    logger.debug("通过配置校验参数");
    checkParameterByConfig(pub);
    logger.debug("业务参数校验");
    this.checkParameter(pub);
    // 通过 handlerName 获取调用 链
    // 有没有这个配置 在前面校验
    List<Object> ASChain = applicationContext.getBean(pub.pubHandlerName, List.class);
    // 校验 影响整个主线的参数
    logger.debug("校验影响主线的业务参数 ");
    chainOneForEach(ASChain, pub);
    return this.handle(pub, ASChain);

  }

  @SuppressWarnings("unchecked")
  private void chainOneForEach(List<Object> ASChain, PubDTO pub) {
    ASChain.forEach((v) -> {
      try {
        if (v instanceof PubHandlerAssemblyService) {
          ((PubHandlerAssemblyService) v).checkSourcesParameter(pub);
        }
        if (v instanceof ArrayList) {
          chainOneForEach((ArrayList<Object>) v, pub);
        }
      } catch (PubHandlerAssemblyException e) {
        pubErrorMessageService.save(pub, e.getMessage());
        throw new PubHandlerException("必要参数校验不通过", e);
      }
    });
  }

  /**
   * 校验参数 根据配置(不在配置里面的参数会被清理)
   * 
   * @throws PubHandlerCheckParameterException
   */
  protected void checkParameterByConfig(PubDTO pub) throws PubHandlerCheckParameterException {
    Class<? extends PubDTO> pubClass = pub.getClass();
    try {
      // TODO 这两个循环怎么整合?
      for (int i = 0; i < checkConfigList.size(); i++) {
        checkParameterByOnConfig(pub, pubClass, checkConfigList.get(i));
      }
      // 清理多于的参数
      Field[] fields = pubClass.getFields();
      loop1: for (Field field : fields) {
        for (int i = 0; i < checkConfigList.size(); i++) {
          if (field.getName().equals(checkConfigList.get(i).keyName) || "pubHandlerName".equals(field.getName())) {
            continue loop1;
          }
        }
        field.set(pub, null);
      }
    } catch (Exception e) {
      logger.error("参数校验出错");
      throw new PubHandlerCheckParameterException(e);
    }
  }

  private void checkParameterByOnConfig(PubDTO pub, Class<? extends PubDTO> pubClass, CheckConfig config)
      throws PubHandlerCheckParameterException {
    Field field = null;
    Class<?> sourceType = null;
    Object value = null;
    try {
      field = pubClass.getField(config.keyName);
      sourceType = field.getType();
      value = field.get(pub);
    } catch (Exception e) {
      logger.error("参数对象出错 {}", config.keyName);
      throw new PubHandlerCheckParameterException("参数对象出错", e);
    }
    // TODO 需要重新调整结构
    if (!sourceType.equals(config.type)) {
      logger.error("参数{} 类型不正确 ,需要:{},给到的是:{}", config.keyName, config.type, field.getType());
      throw new PubHandlerCheckParameterException(
          "参数" + config.keyName + "类型不正确,需要:" + config.type + ",给到的是:" + sourceType);
    }
    // 判断是否能为空
    boolean valueIsBlank = value == null || StringUtils.isEmpty(String.valueOf(value));
    if ((!config.nullable) && valueIsBlank) {
      logger.error("参数{}不能为空", config.keyName);
      throw new PubHandlerCheckParameterException("参数" + config.keyName + "不能为空");
    }
    // 判断长度大小 按类型
    if (!valueIsBlank && config.minLength >= 0 && config.maxLength > config.minLength) {
      checkValueRange(value, config, sourceType);
    }
  }

  /**
   * 范围检查
   * 
   * @param pub
   * @param config
   * @param field
   * @throws IllegalArgumentException
   * @throws IllegalAccessException
   */
  @SuppressWarnings("unchecked")
  private void checkValueRange(Object value, CheckConfig config, Class<?> sourceType)
      throws PubHandlerCheckParameterException {
    boolean isNumberType = value instanceof Integer || value instanceof Long;
    if (isNumberType) {
      long v = Long.parseLong(value.toString());
      if (v < config.minLength || v > config.maxLength) {
        logger.error("参数{}={}超出范围 ,最大:{},最小:{}", config.keyName, v, config.maxLength, config.minLength);
        throw new PubHandlerCheckParameterException("参数超出范围");
      }
    }
    boolean isString = value instanceof String;
    if (isString) {
      long length = value.toString().length();
      if (length < config.minLength || length > config.maxLength) {
        logger.error("参数{}={}超出范围 ,最大:{},最小:{}", config.keyName, value.toString(), config.maxLength, config.minLength);
        throw new PubHandlerCheckParameterException("参数超出范围");
      }
    }
    // 数组
    boolean isArray = sourceType.isArray();
    if (isArray) {
      Object[] objs = ArrayUtils.toArray(value);
      long length = objs.length;
      if (length < config.minLength || length > config.maxLength) {
        logger.error("参数{}大小{}超出范围 ,最大:{},最小:{}", config.keyName, length, config.maxLength, config.minLength);
        throw new PubHandlerCheckParameterException("参数超出范围");
      }
    }

    boolean islist = List.class.isAssignableFrom(sourceType);
    if (islist) {
      List<Object> objs = (List<Object>) (value);
      long length = objs.size();
      if (length < config.minLength || length > config.maxLength) {
        logger.error("参数{}大小{}超出范围 ,最大:{},最小:{}", config.keyName, length, config.maxLength, config.minLength);
        throw new PubHandlerCheckParameterException("参数超出范围");
      }
    }
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    this.setCheckConfig(checkConfigList);
  }

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    this.applicationContext = applicationContext;
  }

  /**
   * 参数 校验配置
   * 
   * @author tsz
   *
   * @date 2018年6月7日
   */
  protected static class CheckConfig {
    String keyName; // 名字
    boolean nullable = true; // 是否可为空
    long minLength = 0; // 最小/短
    long maxLength = 0; // 最大/长
    Class<?> type = Object.class; // 类型
    boolean runFormat = true; // 是否格式化 (有统一的默认的参数 处理方法)

    public CheckConfig(String keyName) {
      super();
      this.keyName = keyName;
    }

    public CheckConfig(String keyName, boolean nullable) {
      super();
      this.keyName = keyName;
      this.nullable = nullable;
    }

    public CheckConfig(String keyName, Class<?> type) {
      super();
      this.keyName = keyName;
      this.type = type;
    }

    public CheckConfig(String keyName, Class<?> type, boolean nullable) {
      super();
      this.keyName = keyName;
      this.type = type;
      this.nullable = nullable;
    }

    public CheckConfig(String keyName, long minLength, long maxLength, Class<?> type) {
      super();
      this.keyName = keyName;
      this.minLength = minLength;
      this.maxLength = maxLength;
      this.type = type;
    }

    public CheckConfig(String keyName, boolean nullable, long minLength, long maxLength, Class<?> type) {
      super();
      this.keyName = keyName;
      this.nullable = nullable;
      this.minLength = minLength;
      this.maxLength = maxLength;
      this.type = type;
    }
  }
}
