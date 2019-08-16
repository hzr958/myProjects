package com.smate.center.task.service.thirdparty.source;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.task.dao.thirdparty.ThirdSourcesErrorLogDao;
import com.smate.center.task.model.thirdparty.ThirdSourcesErrorLog;

/**
 * 数据错误记录服务实现.
 * 
 * @author tsz
 *
 */
@Service("thirdSourcesErrorLogService")
@Transactional(rollbackFor = Exception.class)
public class ThirdSourcesErrorLogServiceImpl implements ThirdSourcesErrorLogService {

  @Autowired
  private ThirdSourcesErrorLogDao thirdSourcesErrorLogDao;

  @Override
  public void saveLog(ThirdSourcesErrorLog log) {
    thirdSourcesErrorLogDao.save(log);
  }

}
