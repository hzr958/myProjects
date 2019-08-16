package com.smate.web.management.dao.journal;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.management.model.journal.ConstRefDb;

/**
 * 文献数据库定义表.
 * 
 * @author liqinghua
 * 
 */
@Repository
public class ConstRefDbDao extends SnsHibernateDao<ConstRefDb, Long> {


  /**
   * 获取单个指定ID的数据.
   * 
   * @param id
   * @return @
   */
  public ConstRefDb getConstRefDbById(Long id) {

    return super.get(id);
  }

  /**
   * 获取单个指定DbCode的数据.
   * 
   * @param id
   * @return @
   */
  public ConstRefDb getConstRefDbByCode(String dbCode) {

    return (ConstRefDb) super.createQuery("from ConstRefDb where upper(code) = ? ", dbCode.toUpperCase())
        .uniqueResult();
  }

  public Long getDbIdByCode(String dbCode) {
    return (Long) super.createQuery("select id from ConstRefDb where upper(code) = ? ", dbCode.toUpperCase())
        .uniqueResult();
  }

  public String getCodeByDbId(Long dbId) {
    return (String) super.createQuery("select code from ConstRefDb where id = ? ", dbId).uniqueResult();
  }


}
