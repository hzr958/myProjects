package com.smate.web.management.dao.analysis.sns;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.management.model.analysis.DaoException;
import com.smate.web.management.model.analysis.sns.PsnKnowCopartner;

/**
 * @author lcw
 * 
 */
@Repository
public class PsnKnowCopartnerDao extends SnsHibernateDao<PsnKnowCopartner, Long> {


  /**
   * 判断是否为合作者.
   * 
   * @param psnId
   * @param coPsnId
   * @return
   * @throws DaoException
   */
  public Long isCopartner(Long psnId, Long coPsnId) throws Exception {
    String hql = "select count(*) from PsnKnowCopartner where psnId=? and cptPsnId=?";
    return findUnique(hql, psnId, coPsnId);
  }
}
