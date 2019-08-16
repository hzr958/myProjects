package com.smate.center.batch.dao.sns.pub;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.smate.center.batch.exception.pub.DaoException;
import com.smate.center.batch.model.sns.pub.TaskGroupResNotify;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * @author oyh
 * 
 */
@Repository
public class TaskGroupResNotifyDao extends SnsHibernateDao<TaskGroupResNotify, Long> {

  @SuppressWarnings("unchecked")
  public List<TaskGroupResNotify> loadGroupRes() throws DaoException {
    String hql = "From TaskGroupResNotify t where t.status in(?) and t.count<=3  order by t.id asc";
    Query q = super.createQuery(hql, new Object[] {1});
    q.setFirstResult(0);
    q.setMaxResults(20);
    return q.list();
  }

  public void setFailure() throws DaoException {

    String hql = "update TaskGroupResNotify t set t.status=? where  t.status=?";

    super.createQuery(hql, new Object[] {3, 1}).executeUpdate();

  }

  public void setSuccess() throws DaoException {

    String hql = "update TaskGroupResNotify t set t.status=? where  t.status=1 or t.status=3";

    super.createQuery(hql, new Object[] {2}).executeUpdate();

  }

  public void lock() throws DaoException {

    String hql = "update TaskGroupResNotify t set t.status=1 where t.status in(?,?) and t.count<=3 ";
    super.createQuery(hql, new Object[] {0, 3}).executeUpdate();

  }

  public void unlock() throws DaoException {

    String hql = "update TaskGroupResNotify t set t.status=0 where t.status=1";
    super.createQuery(hql, new Object[] {}).executeUpdate();
  }

  public boolean isLock() throws DaoException {

    String hql = "From  TaskGroupResNotify t where t.status=?";
    List cList = super.createQuery(hql, new Object[] {1}).list();

    return (cList != null && cList.size() > 0);

  }

  public void setCount() throws DaoException {
    String hql = "update TaskGroupResNotify t set t.count=t.count+1 where t.status=1";
    super.createQuery(hql, new Object[] {}).executeUpdate();

  }

  // ==============人员合并 start============
  @SuppressWarnings("unchecked")
  public List<TaskGroupResNotify> getListByPsnId(Long delPsnId, Long groupId) throws DaoException {
    String hql = "from TaskGroupResNotify where actionPsnId=? and groupId=?";
    return super.createQuery(hql, delPsnId, groupId).list();
  }
  // ==============人员合并 end============
}
