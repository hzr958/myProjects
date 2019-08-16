package com.smate.center.batch.dao.sns.prj;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.sns.nsfc.TmpDest;
import com.smate.core.base.utils.data.SnsHibernateDao;

@Repository
public class TmpDestDao extends SnsHibernateDao<TmpDest, Long> {

  public List<String> finListByCateGory(String category) {
    String hql = "select t.kws from TmpDest t where t.appId =:category";
    return this.createQuery(hql).setParameter("category", category.toUpperCase()).list();
  }

}
