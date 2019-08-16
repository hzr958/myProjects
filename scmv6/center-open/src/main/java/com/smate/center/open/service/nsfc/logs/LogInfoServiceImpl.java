package com.smate.center.open.service.nsfc.logs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.open.dao.nsfc.logs.LogInfoDao;
import com.smate.center.open.model.nsfc.logs.LogInfo;

/**
 * 成果在线 日志服务
 * 
 * @author tsz
 * 
 */
@Service("logInfoService")
@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
/* @Transactional(rollbackFor = Exception.class) */
public class LogInfoServiceImpl implements LogInfoService {
  protected final Logger logger = LoggerFactory.getLogger(getClass());


  @Autowired
  private LogInfoDao logInfoDao;


  /**
   * 保存日志信息
   */
  @Override
  public void saveLogInfo(LogInfo log) {
    logInfoDao.save(log);
  }


}
