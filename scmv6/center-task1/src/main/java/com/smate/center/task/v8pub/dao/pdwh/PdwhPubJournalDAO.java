package com.smate.center.task.v8pub.dao.pdwh;

import org.springframework.stereotype.Repository;

import com.smate.center.task.v8pub.pdwh.po.PdwhPubJournalPO;
import com.smate.core.base.utils.data.PdwhHibernateDao;

/**
 * 基准库成果期刊信息dao
 * 
 * @author YJ
 *
 *         2019年3月29日
 */
@Repository
public class PdwhPubJournalDAO extends PdwhHibernateDao<PdwhPubJournalPO, Long> {

  public void deleteByPdwhPubId(Long pdwhPubId) {
    String hql = "delete from PdwhPubJournalPO t where t.pdwhPubId=:pdwhPubId";
    this.createQuery(hql).setParameter("pdwhPubId", pdwhPubId).executeUpdate();
  }

}
