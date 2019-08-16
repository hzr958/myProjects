package com.smate.center.batch.dao.sns.pub;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.sns.pub.ConPrjRptPub;
import com.smate.core.base.utils.data.SnsHibernateDao;

@Repository
public class ConPrjRptPubDao extends SnsHibernateDao<ConPrjRptPub, Long> {

  /**
   * 查看某人已经提交的成果
   * 
   * @param psnId
   * @param nsfcRptId
   * @return
   */
  public List<ConPrjRptPub> findRptPubs(Long psnId, Long nsfcRptId) {
    String hql = "select t from ConPrjRptPub t,ContinueProjectReport b,ContinueProject c"
        + " where t.nsfcRptId = b.nsfcRptId and b.nsfcPrjCode = c.nsfcPrjCode and t.nsfcRptId = ? and c.psnId = ?"
        + " order by t.seqNo asc,t.pubYear desc";
    return this.createQuery(hql, nsfcRptId, psnId).list();
  }

  /**
   * 移除成果
   * 
   * @param nsfcRptId
   * @param pubIds
   */
  public void removePubFromRpt(Long nsfcRptId, List<Long> pubIds) {
    String hql = "delete from ConPrjRptPub t where t.nsfcRptId = :nsfcRptId and t.pubId in (:pubIds)";
    this.createQuery(hql).setParameter("nsfcRptId", nsfcRptId).setParameterList("pubIds", pubIds).executeUpdate();
  }

  /**
   * 查找最大序号
   * 
   * @param nsfcRptId
   * @return
   */
  public Integer findMaxSeq(Long nsfcRptId) {
    String hql = "select count(*) from ConPrjRptPub t where t.nsfcRptId = ?";
    Long seqNo = (Long) this.createQuery(hql, nsfcRptId).uniqueResult();
    if (seqNo == null) {
      return 0;
    }
    return seqNo.intValue();
  }

  /**
   * 查询成果信息
   * 
   * @param nsfcRptId
   * @param pubId
   * @return
   */
  public ConPrjRptPub findPubInfo(Long nsfcRptId, Long pubId) {
    String hql = "from ConPrjRptPub t where t.nsfcRptId = ? and t.pubId = ?";
    return (ConPrjRptPub) this.createQuery(hql, nsfcRptId, pubId).uniqueResult();
  }

  /**
   * 查找已经提交的报告的成果ID
   * 
   * @param nsfcRptId
   * @return
   */
  public List<Long> findPubIds(Long nsfcRptId) {
    String hql = "select t.pubId from ConPrjRptPub t where t.nsfcRptId = ?";
    return this.createQuery(hql, nsfcRptId).list();
  }

  /**
   * 根据ID获取成果信息
   * 
   * @param pubId
   * @return
   */
  public List<ConPrjRptPub> findPubList(Long pubId) {
    String hql = "from ConPrjRptPub t where t.pubId = ?";
    return this.createQuery(hql, pubId).list();
  }
}
