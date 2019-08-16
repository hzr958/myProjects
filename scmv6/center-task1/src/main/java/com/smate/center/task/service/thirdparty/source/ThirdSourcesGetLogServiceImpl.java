package com.smate.center.task.service.thirdparty.source;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.task.dao.thirdparty.ThirdSourcesGetLogDao;
import com.smate.center.task.model.thirdparty.ThirdSourcesGetLog;

/**
 * 接口调用日志服务实现.
 * 
 * @author tsz
 *
 */
@Service("thirdSourcesGetLogService")
@Transactional(rollbackFor = Exception.class)
public class ThirdSourcesGetLogServiceImpl implements ThirdSourcesGetLogService {


  @Autowired
  private ThirdSourcesGetLogDao thirdSourcesGetLogDao;

  @Override
  public void saveLog(ThirdSourcesGetLog thirdSourcesGetLog) throws Exception {

    thirdSourcesGetLogDao.save(thirdSourcesGetLog);

  }

}
