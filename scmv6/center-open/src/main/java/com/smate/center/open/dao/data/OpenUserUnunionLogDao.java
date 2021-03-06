package com.smate.center.open.dao.data;

import org.springframework.stereotype.Repository;

import com.smate.center.open.model.OpenUserUnunionLog;
import com.smate.core.base.utils.constant.DBSessionEnum;
import com.smate.core.base.utils.data.HibernateDao;

@Repository
public class OpenUserUnunionLogDao extends HibernateDao<OpenUserUnunionLog, Long> {

  @Override
  public DBSessionEnum getDbSession() {
    return DBSessionEnum.SNS;
  }


}
