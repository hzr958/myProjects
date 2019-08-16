package com.smate.center.task.dao.sns.quartz;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.sns.pub.Des3PubId;
import com.smate.core.base.utils.data.SnsHibernateDao;

@Repository
public class Des3PubIdDao extends SnsHibernateDao<Des3PubId, String> {

  public Long findDes3IdByPubId(Long pubId) {

    String hql = "from Des3PubId t where t.pubId=:pubId";

    return (Long) super.createQuery(hql).setParameter("pubId", pubId).uniqueResult();
  }


  @SuppressWarnings("unchecked")
  public List<Des3PubId> getPubList(int index, int batchSize) {
    String hql = " from Des3PubId t where t.des3Id is null";
    return super.createQuery(hql).setFirstResult(batchSize * (index - 1)).setMaxResults(batchSize).list();
  }


}
