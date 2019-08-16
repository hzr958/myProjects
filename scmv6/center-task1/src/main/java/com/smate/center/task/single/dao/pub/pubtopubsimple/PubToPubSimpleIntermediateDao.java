package com.smate.center.task.single.dao.pub.pubtopubsimple;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.single.model.pub.pubtopubsimple.PubToPubSimpleIntermediate;
import com.smate.core.base.utils.data.SnsHibernateDao;

@Repository
public class PubToPubSimpleIntermediateDao extends SnsHibernateDao<PubToPubSimpleIntermediate, Long> {

  @SuppressWarnings("unchecked")
  public List<Long> getPubIdList(Integer size) {

    String hql = "select t.pubId from PubToPubSimpleIntermediate t where t.status = 0 order by t.pubId asc";
    return super.createQuery(hql).setMaxResults(size).list();

  }

}
