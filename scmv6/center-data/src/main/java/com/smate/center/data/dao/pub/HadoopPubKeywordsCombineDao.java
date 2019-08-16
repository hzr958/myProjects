package com.smate.center.data.dao.pub;

import org.springframework.stereotype.Repository;

import com.smate.center.data.model.pub.HadoopPubKeywordsCombine;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 
 * @author lhd
 *
 */
@Repository
public class HadoopPubKeywordsCombineDao extends SnsHibernateDao<HadoopPubKeywordsCombine, Long> {

  public Boolean check(String pubKey) {
    String hql = "from HadoopPubKeywordsCombine t where t.pubKey=:pubKey";
    Object obj = super.createQuery(hql).setParameter("pubKey", pubKey).uniqueResult();
    if (obj != null) {
      return true;
    }
    return false;
  }

}
