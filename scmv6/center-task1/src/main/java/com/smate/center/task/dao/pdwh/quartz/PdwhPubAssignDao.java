package com.smate.center.task.dao.pdwh.quartz;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.pdwh.quartz.PdwhPubAssign;
import com.smate.core.base.utils.data.PdwhHibernateDao;

/**
 * 成果匹配dao
 * 
 * @author zjh
 *
 */
@Repository
public class PdwhPubAssignDao extends PdwhHibernateDao<PdwhPubAssign, Long> {
  /**
   * 获取PdwhPubAssign.
   * 
   * @param pubId
   * @param insId
   * @return
   */
  public PdwhPubAssign getPdwhPubAssign(Long pubId, Long insId) {
    String hql = "from PdwhPubAssign t where t.pubId= :pubId and t.insId = :insId ";
    return (PdwhPubAssign) super.createQuery(hql).setParameter("pubId", pubId).setParameter("insId", insId)
        .uniqueResult();
  }

  /**
   * 获取成果对应已经匹配上单位的insid
   * 
   * @param pubId
   * @return
   */
  public List<Long> getPubAssignId(Long pubId) {
    String hql = "select t.insId from PdwhPubAssign t where t.pubId= :pubId and t.result=1";
    return super.createQuery(hql).setParameter("pubId", pubId).list();
  }

}
