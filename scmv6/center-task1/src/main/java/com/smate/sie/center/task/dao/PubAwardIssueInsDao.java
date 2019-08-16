package com.smate.sie.center.task.dao;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.smate.center.task.exception.DaoException;
import com.smate.core.base.utils.data.SieHibernateDao;
import com.smate.sie.center.task.model.PubAwardIssueIns;

/**
 * 授奖机构自动提示.
 * 
 * @author liqinghua
 * 
 */
@Repository
public class PubAwardIssueInsDao extends SieHibernateDao<PubAwardIssueIns, Long> {

  /**
   * 获取智能匹配授奖机构自动提示列表.
   * 
   * @param searchKey
   * @return
   * @throws DaoException
   */
  public List<PubAwardIssueIns> getAwardIssueIns(String searchKey) throws DaoException {
    String hql = "from PubAwardIssueIns t where t.query like ?";
    Query query = super.createQuery(hql, new Object[] {"%" + searchKey.trim().toLowerCase() + "%"});
    List<PubAwardIssueIns> list = query.list();
    return list;
  }

  /**
   * 保存授奖机构自动提示.
   * 
   * @param obj
   * @throws DaoException
   */
  public void saveAwardIssueIns(PubAwardIssueIns obj) throws DaoException {

    super.save(obj);
  }

  public boolean isExistQuery(String query) throws DaoException {
    // 若为空的情况也return
    if (query == null || "".equals(query)) {
      return true;
    }
    String hql = "select count(code) from PubAwardIssueIns t where  t.query = ? ";
    Long count = findUnique(hql, query);
    if (count > 0)
      return true;
    return false;
  }
}
