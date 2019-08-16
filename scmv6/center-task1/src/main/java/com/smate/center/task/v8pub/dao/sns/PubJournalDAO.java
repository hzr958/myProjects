package com.smate.center.task.v8pub.dao.sns;

import org.springframework.stereotype.Repository;

import com.smate.center.task.v8pub.sns.po.PubJournalPO;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 基准库成果期刊信息dao
 * 
 * @author YJ
 *
 *         2019年3月29日
 */
@Repository
public class PubJournalDAO extends SnsHibernateDao<PubJournalPO, Long> {

  public void deleteByPubId(Long pubId) {
    String hql = "delete from PubJournalPO t where t.pubId=:pubId";
    this.createQuery(hql).setParameter("pubId", pubId).executeUpdate();
  }
}
