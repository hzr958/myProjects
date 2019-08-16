package com.smate.sie.center.task.service;

import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smate.center.task.exception.ServiceException;
import com.smate.sie.core.base.utils.dao.validate.KpiValidateMainDao;
import com.smate.sie.core.base.utils.model.validate.KpiValidateMain;

@Service("kpiValidateMainService")
@Transactional(rollbackOn = Exception.class)
public class KpiValidateMainServiceImpl implements KpiValidateMainService {

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private KpiValidateMainDao kpiValidateMaindao;

  /**
   * 获取待处理的总数
   */
  @Override
  public Long countNeedHandleKeyId() {
    try {
      return kpiValidateMaindao.countNeedHandleKeyId();
    } catch (Exception e) {
      logger.error("读取KPI_VALIDATE_MAIN表中需要处理的总数出错 ！", e);
      throw new ServiceException(e);
    }
  }

  /**
   * 获取待处理的数据主键uuId
   */
  @Override
  public List<KpiValidateMain> loadNeedHandleKeyId(int maxSize) {
    List<KpiValidateMain> kpiVdMains = null;
    try {
      kpiVdMains = kpiValidateMaindao.loadNeedHandleKeyId(maxSize);
    } catch (Exception e) {
      logger.error("读取KPI_VALIDATE_MAIN表中需要处理的数据主键uuId出错 ！", e);
      throw new ServiceException(e);
    }
    return kpiVdMains;
  }

  @Override
  public void updateKpiValidateMain(KpiValidateMain kpiVdMain) {
    try {
      kpiValidateMaindao.saveOrUpdate(kpiVdMain);
    } catch (Exception e) {
      logger.error("更新KPI_VALIDATE_MAIN中状态值出错, {uuId}", e, kpiVdMain.getUuId());
      throw new ServiceException(e);
    }
  }

}
