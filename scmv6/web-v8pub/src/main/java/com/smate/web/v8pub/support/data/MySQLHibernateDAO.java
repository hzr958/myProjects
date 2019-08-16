package com.smate.web.v8pub.support.data;

import java.io.Serializable;

import org.springframework.util.Assert;

import com.smate.core.base.utils.constant.DBSessionEnum;
import com.smate.core.base.utils.data.HibernateDao;
import com.smate.core.base.utils.exception.DAOException;

/**
 * 使用MySQL的HibernateDAO基础类
 * 
 * @author houchuanjie
 * @date 2018/06/01 16:56
 */
public class MySQLHibernateDAO<T, PK extends Serializable> extends HibernateDao<T, PK> {

  /**
   * 指定使用MySQL数据源
   * 
   * @return
   */
  @Override
  public DBSessionEnum getDbSession() {
    return DBSessionEnum.MYSQL;
  }

  @Override
  public void save(T entity) throws DAOException {
    Assert.notNull(entity, "entity不能为空");
    try {
      getSession().save(entity);
      logger.debug("save entity: {}", entity);
    } catch (Exception e) {
      throw new DAOException(e);
    }
  }

  public void update(T entity) throws DAOException {
    Assert.notNull(entity, "entity不能为空");
    try {
      getSession().update(entity);
      logger.debug("update entity: {}", entity);
    } catch (Exception e) {
      throw new DAOException(e);
    }
  }

  public void saveOrUpdate(T entity) throws DAOException {
    Assert.notNull(entity, "entity不能为空");
    try {
      getSession().saveOrUpdate(entity);
      logger.debug("save or update entity: {}", entity);
    } catch (Exception e) {
      throw new DAOException(e);
    }
  }
}
