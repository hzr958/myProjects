package com.smate.sie.center.task.dao;

import com.smate.core.base.utils.data.SieHibernateDao;
import com.smate.sie.center.task.model.SieDataSrvPatTmp;
import org.springframework.stereotype.Repository;

/**
 * 专利
 * 
 * @author 叶星源
 * 
 */
@Repository
public class SieDataSrvPatTmpDao extends SieHibernateDao<SieDataSrvPatTmp, Long> {
  public void saveOrUpdate(SieDataSrvPatTmp sieDataSrvPatTmp) {
    super.getSession().saveOrUpdate(sieDataSrvPatTmp);
  }
}
