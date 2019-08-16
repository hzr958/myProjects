package com.smate.center.task.dao.sns.psn;

import com.smate.center.task.exception.DaoException;
import com.smate.center.task.model.sns.psn.ETemplateInfluenceCount;
import com.smate.core.base.utils.data.SnsHibernateDao;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 科研影响力 zk add.
 */

@Repository
public class ETemplateInfluenceCountDao extends SnsHibernateDao<ETemplateInfluenceCount, Long> {

  @SuppressWarnings("unchecked")
  public List<ETemplateInfluenceCount> getETemplateInfluenceCount(int size) {
    return super.createQuery("from ETemplateInfluenceCount e where e.status in (1,2,-1)order by e.psnId ")
        .setMaxResults(100).setFirstResult(size * 100).list();
  }

  public ETemplateInfluenceCount getETemplateInfluenceCount(Long psnId) throws DaoException {
    return (ETemplateInfluenceCount) super.createQuery(
        " from ETemplateInfluenceCount e where e.psnId=? and e.status = 0", psnId).uniqueResult();
  }

  @Transactional(rollbackFor = Exception.class)
  public void saveETemplateInfluenceCount(ETemplateInfluenceCount influenceCount) throws DaoException {

    super.save(influenceCount);
    // super.getSession().save(influenceCount);
  }

  @SuppressWarnings("unchecked")
  public List<ETemplateInfluenceCount> findETemplateInfluenceCounts(Integer size) throws DaoException {
    return super.createQuery("from ETemplateInfluenceCount where status = 0 order by id ").setMaxResults(100)
        .setFirstResult(size * 100).list();
  }

  public void updateInfluence(Long psnId) throws DaoException {
    super.createQuery("update ETemplateInfluenceCount set status = 1 where psnId= ? and status = 0", psnId)
        .executeUpdate();
  }

  // 防止意外重复发送
  public Long checkLastMonthHadSend() throws DaoException {
    String hql = "select  count(c.psnId) from ETemplateInfluenceCount c where  trunc(c.createDate)>=trunc(sysdate-7) ";
    return (Long) super.createQuery(hql).uniqueResult();
  }

}
