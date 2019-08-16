package com.smate.center.task.dao.email;

import org.springframework.stereotype.Repository;

import com.smate.center.task.exception.DaoException;
import com.smate.center.task.model.email.ConstEmailInterval;
import com.smate.core.base.utils.data.EmailSrvHibernateDao;


/**
 * psn邮件模板发送时间间隔实体DAO
 * 
 */
@Repository
public class ConstEmailIntervalDao extends EmailSrvHibernateDao<ConstEmailInterval, Integer> {

  /**
   * 获取任务状态位
   * 
   * @param tempId
   * @return
   * @throws DaoException
   */
  public Integer getStatus(Integer tempId) throws DaoException {
    String hql = "select c.status from  ConstEmailInterval c where c.etempCode = ?";
    return (Integer) super.createQuery(hql, tempId).uniqueResult();
  }

  /**
   * 更新状态位
   * 
   * @param tempId
   * @param status
   * @throws DaoException
   */
  public void updateStatus(Integer tempId, Integer status) throws DaoException {
    String hql = "update ConstEmailInterval c set c.status=? where c.etempCode= ? ";
    super.createQuery(hql, status, tempId).executeUpdate();
  }
}
