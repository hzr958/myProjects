package com.smate.web.dyn.dao.pdwhpub;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.PdwhHibernateDao;
import com.smate.web.dyn.model.pub.PubPdwhPO;

/**
 * @author houchuanjie
 * @date 2018/06/01 17:44
 */
@Repository
public class PubPdwhDAO extends PdwhHibernateDao<PubPdwhPO, Long> {
  public Long getPubCount(List<Long> pubPdwhIds) {
    if (pubPdwhIds.size() != 0) {
      String hql = "select count(pubId) from PubPdwhPO p where p.pubId in (:ids) and p.status = 0 ";
      return (Long) this.createQuery(hql).setParameterList("ids", pubPdwhIds).uniqueResult();
    }
    return 0L;
  }
}
