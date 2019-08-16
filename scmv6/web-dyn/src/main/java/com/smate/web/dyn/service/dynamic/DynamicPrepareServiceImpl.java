package com.smate.web.dyn.service.dynamic;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.utils.exception.DynException;
import com.smate.web.dyn.form.dynamic.DynamicForm;

/**
 * 
 * @author zjh 动态实现类，注入具体的实现
 *
 */
@Service("dynamicPrepareService")
@Transactional(rollbackFor = Exception.class)
public class DynamicPrepareServiceImpl implements DynamicPrepareService {
  /**
   * 动态数据检查及生成的实现类
   */

  private Map<String, DynamicDataDealBaseService> dataDealMap;
  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Override
  public void checkAndDealData(DynamicForm form) throws DynException {
    // 根据各个动态类型获取对应的服务
    DynamicDataDealBaseService d = dataDealMap.get(form.getDynType());
    // 实现的服务数据处理
    d.checkAndDeal(form);
  }

  public Map<String, DynamicDataDealBaseService> getDataDealMap() {
    return dataDealMap;
  }

  public void setDataDealMap(Map<String, DynamicDataDealBaseService> dataDealMap) {
    this.dataDealMap = dataDealMap;
  }

}
