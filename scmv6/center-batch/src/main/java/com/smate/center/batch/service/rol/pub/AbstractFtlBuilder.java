package com.smate.center.batch.service.rol.pub;

import java.util.Map;

import org.apache.commons.collections4.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.batch.constant.TemplateConstants;
import com.smate.center.batch.exception.pub.ServiceException;


/**
 * freemarker模板构造抽象
 * 
 * @author zk
 * 
 */
public abstract class AbstractFtlBuilder implements FtlBuilder {

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private FreemarkerBuilderUtils freemarkerBuilderUtils;

  /**
   * 构造html
   */
  @Override
  public Map<String, String> builderHtml(Object... params) throws ServiceException {
    // 得到参数
    Map<String, Object> map = this.invoke(params);
    if (MapUtils.isEmpty(map)) {
      logger.error("freemarker模板构造时，得到的参数为空");
      throw new ServiceException("freemarker模板构造时，得到的参数为空");
    }
    Map<String, String> returnMap = freemarkerBuilderUtils.getTemplateContent(map);
    returnMap.put(TemplateConstants.TEMP_CODE, this.getTempCode().toString());
    returnMap.put(TemplateConstants.OBJ_CODE, map.get(TemplateConstants.OBJ_CODE).toString());
    if (map.get(TemplateConstants.INS_CODE) != null) {
      returnMap.put(TemplateConstants.INS_CODE, map.get(TemplateConstants.INS_CODE).toString());
    }
    return returnMap;
  }

  // 整理数据方法
  public abstract Map<String, Object> invoke(Object... params) throws ServiceException;

  // 获取模板信息
  public abstract Integer getTempCode() throws ServiceException;

}
