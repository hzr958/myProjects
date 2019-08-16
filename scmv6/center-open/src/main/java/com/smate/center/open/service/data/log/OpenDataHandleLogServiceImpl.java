package com.smate.center.open.service.data.log;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.open.dao.data.log.OpenDataHandleLogDao;
import com.smate.center.open.model.data.log.OpenDataHandleLog;

/**
 * open系统 接口 请求处理统计日志
 * 
 * @author tsz
 *
 */
@Service("openDataHandleLogService")
@Transactional(rollbackFor = Exception.class)
public class OpenDataHandleLogServiceImpl implements OpenDataHandleLogService {

  @Autowired
  private OpenDataHandleLogDao openDataHandleLogDao;

  @Override
  public void saveLog(String token, String serviceType, String disc) {
    // 没有记录就生成 新记录 有记录 就加统计次数
    OpenDataHandleLog openDataHandleLog = openDataHandleLogDao.getByTokenAndType(token, serviceType);
    if (openDataHandleLog == null) {
      openDataHandleLog = new OpenDataHandleLog();
      openDataHandleLog.setToken(token);
      openDataHandleLog.setServiceType(serviceType);
      openDataHandleLog.setSum(0);
    }
    openDataHandleLog.setLastAccess(new Date());
    if (!StringUtils.isBlank(disc)) {
      openDataHandleLog.setDisc(disc);
    }
    openDataHandleLog.setSum(openDataHandleLog.getSum() + 1);

    openDataHandleLogDao.save(openDataHandleLog);
  }

}
