package com.smate.center.batch.dao.sns.pub;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.sns.pub.PublicationPdwh;
import com.smate.core.base.utils.data.SnsHibernateDao;

@Repository
public class PublicationPdwhExtDao extends SnsHibernateDao<PublicationPdwh, Long> {

  /**
   * 查询pdwh cnki成果id
   * 
   * @param psnId
   * @return
   */
  public List<Long> findPdwhCnkiId(Long psnId) {
    String hql =
        "select t.cnkiId from PublicationPdwh t where exists (select t1.pubId from PubOwnerMatch t1 where t1.psnId = ? and t.pubId = t1.pubId and t1.auSeq > 0) and t.isiId is not null";
    return super.createQuery(hql, psnId).list();
  }

  /**
   * 查询基准库ISI ID
   * 
   * @param psnId
   * @return
   */
  public List<Long> findPdwhIsiId(Long psnId) {
    String hql =
        "select distinct t.isiId from PublicationPdwh t where exists (select t1.pubId from PubOwnerMatch t1 where t1.psnId = ? and t.pubId = t1.pubId and t1.auSeq > 0) and t.isiId is not null";
    return super.createQuery(hql, psnId).list();
  }

  @SuppressWarnings("unchecked")
  public List<PublicationPdwh> findByPsnId(Long psnId) {
    String hql =
        "from PublicationPdwh t where exists (select t1.pubId from PubOwnerMatch t1 where t1.psnId = ? and t.pubId = t1.pubId and t1.auSeq > 0) ";
    return super.createQuery(hql, psnId).list();
  }
}
