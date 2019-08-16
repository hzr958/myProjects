package com.smate.center.open.dao.publication;

import org.springframework.stereotype.Repository;

import com.smate.center.open.model.publication.ConstRefDb;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 文献数据库定义表.
 * 
 * @author liqinghua
 * 
 */
@Repository
public class ConstRefDbDao extends SnsHibernateDao<ConstRefDb, Long> {

  /**
   * 获取单个指定DbCode的数据.
   * 
   * @param id
   * @return
   * @throws DaoException
   */
  public ConstRefDb getConstRefDbByCode(String dbCode) throws Exception {

    return (ConstRefDb) super.createQuery("from ConstRefDb where upper(code) = ? ", dbCode.toUpperCase())
        .uniqueResult();
  }
}
