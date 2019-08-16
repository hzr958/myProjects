package com.smate.center.open.dao.nsfc;

import org.springframework.stereotype.Repository;

import com.smate.core.base.pub.model.IrisExcludedPub;
import com.smate.core.base.utils.constant.DBSessionEnum;
import com.smate.core.base.utils.data.HibernateDao;

/**
 * 
 * @author pwl
 * 
 */
@Repository
public class IrisExcludedPubDao extends HibernateDao<IrisExcludedPub, Long> {

  @Override
  public DBSessionEnum getDbSession() {
    return DBSessionEnum.SNS;
  }

  public int deleteIrisExcludedPub(String uuid) {
    return super.createQuery("delete from IrisExcludedPub t where t.uuid=?", uuid).executeUpdate();
  }
}
