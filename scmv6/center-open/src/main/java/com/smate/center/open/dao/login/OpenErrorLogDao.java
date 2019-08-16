package com.smate.center.open.dao.login;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.smate.center.open.exception.OpenDataSaveErrorLogException;
import com.smate.center.open.model.OpenErrorLog;
import com.smate.core.base.utils.constant.DBSessionEnum;
import com.smate.core.base.utils.data.HibernateDao;

@Repository
public class OpenErrorLogDao extends HibernateDao<OpenErrorLog, Long> {

  @Override
  public DBSessionEnum getDbSession() {
    return DBSessionEnum.SNS;
  }

  protected Logger logger = LoggerFactory.getLogger(getClass());

  public void saveOpenErrorLog(OpenErrorLog openErrorLog) {

    try {
      if (openErrorLog == null) {
        return;
      }
      super.save(openErrorLog);
    } catch (Exception e) {
      logger.error("保存错误日志异常");
      throw new OpenDataSaveErrorLogException(e);
    }
  }

}
