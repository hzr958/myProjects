package com.smate.web.fund.agency.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.fund.agency.model.AgencySearchForm;
import com.smate.web.fund.agency.model.PsnAwardIns;

/**
 * 机构赞DAO
 * 
 * @author wsn
 *
 */
@Repository
public class PsnAwardInsDao extends SnsHibernateDao<PsnAwardIns, Long> {

  /**
   * 用户赞/取消赞记录
   * 
   * @param form
   */
  public int hasAwardRecord(Long indId, Long psnId, Integer status) {
    String hql =
        "select count(t.recordId) from PsnAwardIns t where t.awardPsnId=:awardPsnId and t.insId=:insId and t.status=:status";
    Long count = (Long) super.createQuery(hql).setParameter("awardPsnId", psnId).setParameter("insId", indId)
        .setParameter("status", status).uniqueResult();
    return count.intValue();
  }

  /**
   * 获取赞记录
   * 
   * @param form
   * @return
   */
  public PsnAwardIns getPsnAwardIns(AgencySearchForm form) {
    String hql = "from PsnAwardIns t where t.awardPsnId=:awardPsnId and t.insId=:insId";
    return (PsnAwardIns) super.createQuery(hql).setParameter("awardPsnId", form.getPsnId())
        .setParameter("insId", form.getInsId()).uniqueResult();
  }

  /**
   * 获取人员对机构赞状态的id
   * 
   * @param indId
   * @param psnId
   * @param status
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Long> findInsAwardStatus(List<Long> insIds, Long psnId, Integer status) {
    String hql =
        "select t.insId from PsnAwardIns t where t.awardPsnId=:awardPsnId and t.insId in (:insIds) and t.status = :status";
    return super.createQuery(hql).setParameter("awardPsnId", psnId).setParameterList("insIds", insIds)
        .setParameter("status", status).list();
  }
}
