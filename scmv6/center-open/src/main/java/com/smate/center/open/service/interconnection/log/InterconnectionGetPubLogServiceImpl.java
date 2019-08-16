package com.smate.center.open.service.interconnection.log;

import java.util.Date;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.open.consts.OpenConsts;
import com.smate.center.open.dao.interconnection.log.InterconnectionGetPubLogDao;
import com.smate.center.open.model.interconnection.log.InterconnectionGetPubLog;


/**
 * 成果拉取日志服务
 * 
 * @author tsz
 *
 */
@Service("interconnectionGetPubLogService")
@Transactional(rollbackFor = Exception.class)
public class InterconnectionGetPubLogServiceImpl implements InterconnectionGetPubLogService {

  protected Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private InterconnectionGetPubLogDao interconnectionGetPubLogDao;

  @Override
  public void saveGetPubLog(int accessType, int nums, Map<String, Object> dataParamet) throws Exception {
    InterconnectionGetPubLog log = new InterconnectionGetPubLog();
    log.setAccessType(accessType);
    log.setPubNumbers(nums);
    log.setOpenId(Long.valueOf(dataParamet.get(OpenConsts.MAP_OPENID).toString()));
    log.setToken(dataParamet.get(OpenConsts.MAP_TOKEN).toString());
    log.setAccessDate(new Date());
    if (dataParamet.get(OpenConsts.MAP_HISTORY_PUBLICATION) != null) {
      log.setDescribe("同步历史数据");
    } else {
      log.setDescribe("同步成果数据");
    }


    interconnectionGetPubLogDao.save(log);
  }

}
