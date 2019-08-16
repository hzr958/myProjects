package com.smate.center.open.dao.snscode;

import org.springframework.stereotype.Repository;

import com.smate.center.open.model.snscode.IrisSnsCode;
import com.smate.core.base.utils.constant.DBSessionEnum;
import com.smate.core.base.utils.data.HibernateDao;

/**
 * IRIS业务系统与SNS用户关联的验证码DAO.
 * 
 * @author pwl
 * 
 */
@Repository
public class IrisSnsCodeDao extends HibernateDao<IrisSnsCode, Long> {

  @Override
  public DBSessionEnum getDbSession() {
    // TODO Auto-generated method stub
    return DBSessionEnum.SNS;
  }
}
