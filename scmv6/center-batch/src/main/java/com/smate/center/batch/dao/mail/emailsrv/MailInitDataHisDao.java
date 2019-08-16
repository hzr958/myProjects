package com.smate.center.batch.dao.mail.emailsrv;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.exception.pub.DaoException;
import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.mail.emailsrv.MailInitDataHis;
import com.smate.core.base.utils.data.EmailSrvHibernateDao;


/**
 * 
 * 各节点初始数据表dao
 * 
 * @author zk
 * 
 */
@Repository
public class MailInitDataHisDao extends EmailSrvHibernateDao<MailInitDataHis, Long> {

  /**
   * 获取指定月份前size条记录
   * 
   * @param assignMoths
   * @param size
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public List<MailInitDataHis> getMIDHisBeforeAssignMonths(Date assignMoths, Integer size) throws DaoException {

    String hql = "from MailInitDataHis m where m.createDate<?";
    return super.createQuery(hql, assignMoths).setMaxResults(size).list();
  }

  /**
   * 获取指定月份前size条记录id
   * 
   * @param assignMoths
   * @param size
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public List<Long> getMIDHisIdsBeforeAssignMonths(Date assignMoths, Integer size) throws DaoException {

    String hql = "select m.id from MailInitDataHis m where m.createDate<?";
    return super.createQuery(hql, assignMoths).setMaxResults(size).list();
  }

  /**
   * 清理指定id数据
   * 
   * @param midHisIds
   * @throws ServiceException
   */
  public void clearAssignId(List<Long> midHisIds) throws ServiceException {
    String hql = "delete from MailInitDataHis m where m.id in (:ids)";
    super.createQuery(hql).setParameterList("ids", midHisIds).executeUpdate();
  }
}
