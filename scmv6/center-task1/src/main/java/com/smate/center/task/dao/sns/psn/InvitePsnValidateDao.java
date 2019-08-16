package com.smate.center.task.dao.sns.psn;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.sns.psn.InvitePsnValidate;
import com.smate.core.base.utils.data.SnsHibernateDao;

@Repository
public class InvitePsnValidateDao extends SnsHibernateDao<InvitePsnValidate, Long> {

  @SuppressWarnings("unchecked")
  public List<InvitePsnValidate> getInvitePsnValidate() {
    String hql = " from InvitePsnValidate t where t.flag in (1,2) and t.sendStatus=0";
    return super.createQuery(hql).setMaxResults(1000).list();
  }

  public void updateSendStatus(Long psnId, Integer result) {
    String hql = "update InvitePsnValidate t set t.sendStatus = :result where t.psnId = :psnId";
    super.createQuery(hql).setParameter("result", result).setParameter("psnId", psnId).executeUpdate();
  }

}
