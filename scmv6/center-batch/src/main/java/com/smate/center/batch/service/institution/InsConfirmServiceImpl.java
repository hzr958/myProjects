package com.smate.center.batch.service.institution;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.dao.rol.InsConfirmDao;

/**
 * 单位确认成果配置
 * 
 * @author Scy
 * 
 */
@Service("insConfirmService")
@Transactional(rollbackFor = Exception.class)
public class InsConfirmServiceImpl implements InsConfirmService {

  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private InsConfirmDao insConfirmDao;

  @Override
  public Integer findInsConfirmStatus(Long insId) {
    try {
      return this.insConfirmDao.findInsConfirmStatus(insId);
    } catch (Exception e) {
      logger.error("查找单位是否需要确认成果的状态出错，insId=" + insId, e);
    }
    return 0;
  }

}
