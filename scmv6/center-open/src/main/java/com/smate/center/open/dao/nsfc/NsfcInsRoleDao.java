package com.smate.center.open.dao.nsfc;

import org.springframework.stereotype.Repository;

import com.smate.center.open.model.nsfc.NsfcInsRole;
import com.smate.center.open.model.nsfc.NsfcInsRoleId;
import com.smate.core.base.utils.constant.DBSessionEnum;
import com.smate.core.base.utils.data.HibernateDao;



/**
 * NSFC人员角色表[同义词]DAO ,
 * 
 * @author zk
 *
 */
@Repository
public class NsfcInsRoleDao extends HibernateDao<NsfcInsRole, NsfcInsRoleId> {

  @Override
  public DBSessionEnum getDbSession() {
    return DBSessionEnum.SNS;
  }
}
