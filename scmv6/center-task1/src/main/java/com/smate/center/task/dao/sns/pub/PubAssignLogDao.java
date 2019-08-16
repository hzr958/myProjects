package com.smate.center.task.dao.sns.pub;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.task.model.sns.pub.PubAssignLog;
import com.smate.core.base.utils.data.SnsHibernateDao;

@Repository
public class PubAssignLogDao extends SnsHibernateDao<PubAssignLog, Long> {

  public void savePubAssignLog(PubAssignLog assignLog) {
    super.getSession().saveOrUpdate(assignLog);
  }

  public PubAssignLog getPubAssignLog(Long pdwhPubId, Long psnId) {
    String hql = "from PubAssignLog t where t.pdwhPubId= :pdwhPubId and t.psnId = :psnId  and t.status=0";
    return (PubAssignLog) super.createQuery(hql).setParameter("pdwhPubId", pdwhPubId).setParameter("psnId", psnId)
        .uniqueResult();
  }

  public void remove(Long pdwhPubId, Long psnId) {
    String hql = "delete from PubAssignLog t where t.pdwhPubId= :pdwhPubId and t.psnId = :psnId  and t.status=0";
    super.createQuery(hql).setParameter("pdwhPubId", pdwhPubId).setParameter("psnId", psnId).executeUpdate();
  }

  @SuppressWarnings("unchecked")
  public List<Long> getNeedSendMailPsnIds(Integer size) {
    String hql =
        "select distinct(t.psnId) from PubAssignLog t where t.confirmResult not in (1,2) and t.isSendMail in (0,2)  and t.status=0 and nvl(t.score,0) > 0 order by t.psnId ";
    return super.createQuery(hql).setMaxResults(size).list();
  }

  public void UpdateMailSendStatus(Long psnId, Integer result) {
    String hql =
        "update PubAssignLog t set t.isSendMail = :result, t.sendMailDate= :sendMailDate  where t.psnId = :psnId  and t.status=0";
    super.createQuery(hql).setParameter("result", result).setParameter("psnId", psnId)
        .setParameter("sendMailDate", new Date()).executeUpdate();
  }

  public Long getConfirmPubCount(Long psnId) {
    String hql =
        "select count(t.pdwhPubId) from PubAssignLog t where t.confirmResult not in (1,2) and  t.psnId = :psnId  and t.status=0 and nvl(t.score,0) > 0 ";
    return (Long) super.createQuery(hql).setParameter("psnId", psnId).uniqueResult();
  }

  @SuppressWarnings("unchecked")
  public List<Long> getConfirmPubIds(Long psnId, int size) {
    String hql =
        "select t.pdwhPubId from PubAssignLog t where t.confirmResult not in (1,2) and t.psnId = :psnId and t.status=0 and nvl(t.score,0) > 0 order by t.createDate desc";
    return super.createQuery(hql).setParameter("psnId", psnId).setMaxResults(size).list();
  }

  @SuppressWarnings("unchecked")
  public List<Long> getReSendMailPsnIds(Integer size) {
    String hql =
        "select distinct(t.psnId) from PubAssignLog t where t.confirmResult not in (1,2) and t.isSendMail= 1 and t.sendMailDate <= add_months(sysdate, -1) and t.status=0 order by t.psnId ";
    return super.createQuery(hql).setMaxResults(size).list();
  }

  public Long getconfirmCount(Long psnId) {
    String countHql =
        "select count( t.pdwhPubId) from PubAssignLog t where t.confirmResult=0 and t.psnId =:psnId and t.status=0 and nvl(t.score,0) > 0";
    return (Long) super.createQuery(countHql).setParameter("psnId", psnId).uniqueResult();
  }

  /**
   * 处理联合唯一索引保存报错新开事务单独保存。
   * 
   * @date 2018年7月5日
   */
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void saveWithNewTransaction(PubAssignLog pubAssignLog) {
    super.saveOrUpdate(pubAssignLog);
  }

}
