package com.smate.center.open.dao.prj;



import org.springframework.stereotype.Repository;

import com.smate.center.open.model.nsfc.project.NsfcProject2;
import com.smate.core.base.utils.constant.DBSessionEnum;
import com.smate.core.base.utils.data.HibernateDao;



/**
 * nsfc项目dao.
 * 
 * @author zk
 *
 */
@Repository
public class NsfcProject2Dao extends HibernateDao<NsfcProject2, Long> {
  @Override
  public DBSessionEnum getDbSession() {
    return DBSessionEnum.SNS;
  }

  public NsfcProject2 getNsfcProject2(Long nsfcPrjId) {
    return findUnique("from NsfcProject2 where nsfcPrjId=?", nsfcPrjId);
  }
}
