package com.smate.center.task.v8pub.dao.pdwh;

import org.springframework.stereotype.Repository;

import com.smate.center.task.v8pub.pdwh.po.PdwhPubRepeatRecordPO;
import com.smate.core.base.utils.data.PdwhHibernateDao;

/**
 * 基准库成果重复记录
 * 
 * @author YJ
 *
 *         2019年6月17日
 */

@Repository
public class PdwhPubRepeatRecordDAO extends PdwhHibernateDao<PdwhPubRepeatRecordPO, Long> {


  public PdwhPubRepeatRecordPO findRecord(Long dPubId, Long pubId) {
    String hql = "from PdwhPubRepeatRecordPO t where (t.pdwhPubId =:dPubId and t.dupPubId =:pubId) "
        + "or (t.pdwhPubId =:pubId and t.dupPubId =:dPubId)";
    return (PdwhPubRepeatRecordPO) this.createQuery(hql).setParameter("dPubId", dPubId).setParameter("pubId", pubId)
        .uniqueResult();
  }

  public void deleteByPdwhPubId(Long pubId) {
    String hql = "delete from PdwhPubRepeatRecordPO t where t.pdwhPubId =:pubId";
    this.createQuery(hql).setParameter("pubId", pubId).executeUpdate();
  }

}
