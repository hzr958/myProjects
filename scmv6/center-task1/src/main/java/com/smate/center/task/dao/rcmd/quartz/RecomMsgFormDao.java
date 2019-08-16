package com.smate.center.task.dao.rcmd.quartz;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.rcmd.quartz.RecomMsgForm;
import com.smate.core.base.utils.data.RcmdHibernateDao;

/**
 * 成果认领、全文认领任务表
 * 
 * @author cht
 *
 */
@Repository
public class RecomMsgFormDao extends RcmdHibernateDao<RecomMsgForm, Long> {

  public List<RecomMsgForm> findRecomMsgListByType(Long type, Integer maxResults) {
    String hql = "from RecomMsgForm t where t.type=:type and t.taskStatus=0 ";
    return this.createQuery(hql).setParameter("type", type).setMaxResults(maxResults).list();
  }

  public List<RecomMsgForm> findRecomMsgList(Integer maxResults) {
    String hql = "from RecomMsgForm t where t.taskStatus=0 ";
    return this.createQuery(hql).setMaxResults(maxResults).list();
  }
}
