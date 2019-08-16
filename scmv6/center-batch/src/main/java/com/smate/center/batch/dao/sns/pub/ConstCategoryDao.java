package com.smate.center.batch.dao.sns.pub;

import java.io.Serializable;
import java.util.List;

import org.hibernate.CacheMode;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.smate.center.batch.exception.pub.DaoException;
import com.smate.center.batch.model.sns.pub.ConstCategory;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 常量类别.
 * 
 * @author liqinghua
 * 
 */
@Repository
public class ConstCategoryDao extends SnsHibernateDao<ConstCategory, String> implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 1116417416393104629L;

  /**
   * 判断常量类别是否已经存在.
   * 
   * @param id
   * @return
   * @throws DaoException
   */
  public boolean isConstCategoryExit(String id) throws DaoException {

    String hql = "select count(t.category) from ConstCategory t where t.category = ? ";
    Long count = super.findUnique(hql, id);
    if (count > 0) {
      return true;
    }
    return false;
  }

  /**
   * 移除常量.
   * 
   * @param id
   * @return
   * @throws DaoException
   */
  public Integer removeConstCategory(String id) throws DaoException {

    super.createQuery("delete from ConstDictionary t where t.key.category = ? ", id).executeUpdate();
    Integer result = super.createQuery("delete from ConstCategory t where t.category = ? ", id).executeUpdate();
    return result;
  }

  /**
   * 删除所有的常量类别.
   * 
   * @throws DaoException
   */
  public void removeAll() throws DaoException {

    super.createQuery("delete from ConstCategory ").executeUpdate();
  }

  /**
   * 获取所有常量类别，直接查询数据库，未经缓存，同步数据时使用.
   * 
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public List<ConstCategory> getAllNoCacheConstCategory() throws DaoException {

    Query query = super.createQuery("from ConstCategory t ");
    query.setCacheMode(CacheMode.IGNORE);
    return query.list();
  }
}
