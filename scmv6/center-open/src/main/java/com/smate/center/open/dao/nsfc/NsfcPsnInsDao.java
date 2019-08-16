package com.smate.center.open.dao.nsfc;

import org.springframework.stereotype.Repository;

import com.smate.center.open.model.nsfc.NsfcPsnIns;
import com.smate.center.open.model.nsfc.NsfcPsnInsPk;
import com.smate.core.base.utils.constant.DBSessionEnum;
import com.smate.core.base.utils.data.HibernateDao;



/**
 * nsfc同义词表NsfcPsnInsDao
 * 
 * @author zk
 *
 */
@Repository
public class NsfcPsnInsDao extends HibernateDao<NsfcPsnIns, NsfcPsnInsPk> {

  @Override
  public DBSessionEnum getDbSession() {
    return DBSessionEnum.SNS;
  }
}
