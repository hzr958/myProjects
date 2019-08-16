package com.smate.center.open.dao.nsfc.logs;

import org.springframework.stereotype.Repository;

import com.smate.center.open.model.nsfc.logs.LogInfo;
import com.smate.core.base.utils.constant.DBSessionEnum;
import com.smate.core.base.utils.data.HibernateDao;



/**
 * 日志DAO
 * 
 * @author tsz
 * 
 */

@Repository
public class LogInfoDao extends HibernateDao<LogInfo, Long> {
  @Override
  public DBSessionEnum getDbSession() {
    return DBSessionEnum.SNS;
  }
}
