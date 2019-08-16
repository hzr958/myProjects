package com.smate.web.file.dao;

import org.hibernate.transform.ResultTransformer;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.smate.core.base.pub.model.Publication;
import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.file.model.PubSimpleQuery;

/**
 * 成果查询dao
 *
 * @author houchuanjie
 * @date 2017年11月29日 下午4:52:24
 */
@Repository
public class PubQueryDao extends SnsHibernateDao<Publication, Long> {
  /**
   * 获取成果所有人id.
   * 
   * @param pubId
   * @return
   */
  public Long getPubOwnerId(Long pubId) {
    String hql = "select t.ownerPsnId from Publication t where t.pubId = :pubId";
    Long ownerPsnId = (Long) super.createQuery(hql).setParameter("pubId", pubId).uniqueResult();
    if (ownerPsnId == null) {
      hql = "select t.OWNER_PSN_ID from V_PUB_SIMPLE t where t.PUB_ID = :pubId";
      ownerPsnId = (Long) getSession().createSQLQuery(hql).setParameter("pubId", pubId)
          .setResultTransformer(Transformers.aliasToBean(Long.class)).uniqueResult();
    }
    return ownerPsnId;
  }

  public PubSimpleQuery getPubSimpleQuery(Long pubId) {
    String hql = "select t from PubSimpleQuery t where t.pubId = :pubId";
    return (PubSimpleQuery) super.createQuery(hql).setParameter("pubId", pubId).uniqueResult();
  }

  public Publication getPublication(Long pubId) {
    return (Publication) super.get(pubId);
  }
}
