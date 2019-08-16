package com.smate.sie.center.task.dao;

import com.smate.core.base.utils.data.SieHibernateDao;
import com.smate.sie.center.task.model.SieDataSrvPubTmp;
import org.springframework.stereotype.Repository;

/**
 * 成果
 * 
 * @author 叶星源
 * 
 */
@Repository
public class SieDataSrvPubTmpDao extends SieHibernateDao<SieDataSrvPubTmp, Long> {

  public void saveOrUpdate(SieDataSrvPubTmp sieDataSrvPubTmp) {
    super.getSession().saveOrUpdate(sieDataSrvPubTmp);
  }

}
