package com.smate.center.task.dao.sns.msg;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.sns.msg.InitMsgForm;
import com.smate.core.base.utils.data.SnsHibernateDao;

@Repository
public class InitMsgFormDao extends SnsHibernateDao<InitMsgForm, Long> {
  public List<InitMsgForm> getList(Integer batchSize) {
    String hql = "from InitMsgForm t where t.taskStatus = 0";
    return this.createQuery(hql).setMaxResults(batchSize).list();
  }

  public List<Object[]> getResRecInfoList(Long resRecId) {
    String sql =
        "select t.res_Id as resId,t.res_Type as resType from PSN_RES_RECEIVE_RES t where t.res_Rec_Id=:resRecId ";
    return this.getSession().createSQLQuery(sql).setParameter("resRecId", resRecId).list();
  }
}
