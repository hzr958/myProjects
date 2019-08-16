package com.smate.core.base.utils.data;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

import com.mongodb.WriteResult;
import com.smate.core.base.utils.exception.DAOException;

/**
 * 
 * @author tsz
 *
 *         常用操作数据方法封装。分页方法封装
 */
public class BaseMongoDAO<T> {

  @Autowired
  private MongoTemplate mongoTemplate;

  /**
   * T
   */
  protected Class<T> entityClass;

  /**
   * 获取操作对象
   */
  public BaseMongoDAO() {
    this.entityClass = ReflectionUtils.getSuperClassGenricType(getClass());
  }

  /**
   * 查找所有
   *
   * @return
   */
  public List<T> findAll() throws DAOException {
    try {
      return this.mongoTemplate.findAll(entityClass);
    } catch (Exception e) {
      throw new DAOException(e);
    }
  }

  /**
   * 查找单个
   *
   * @param query
   * @return
   */
  public T findOne(Query query) throws DAOException {
    try {
      return this.mongoTemplate.findOne(query, entityClass);
    } catch (Exception e) {
      throw new DAOException(e);
    }
  }

  /**
   * 判断是否存在
   *
   * @param query
   * @return
   */
  public boolean exists(Query query) throws DAOException {
    try {
      return this.mongoTemplate.exists(query, entityClass);
    } catch (Exception e) {
      throw new DAOException(e);
    }
  }

  /**
   * 根据条件查找
   *
   * @param query
   * @return
   */
  public List<T> find(Query query) throws DAOException {
    try {
      return this.mongoTemplate.find(query, entityClass);
    } catch (Exception e) {
      throw new DAOException(e);
    }
  }

  /**
   * 根据条件查找
   *
   * @param query
   * @return
   */
  public List<T> find(Query query, Class<T> entityClass) throws DAOException {
    try {
      return this.mongoTemplate.find(query, entityClass);
    } catch (Exception e) {
      throw new DAOException(e);
    }
  }

  /**
   * 根据id查找
   *
   * @param id
   * @return
   */
  public T findById(Object id) {
    try {
      return this.mongoTemplate.findById(id, entityClass);
    } catch (Exception e) {
      throw new DAOException(e);
    }
  }

  /**
   * 根据id查找
   *
   * @param id
   * @return
   */
  public T findById(Object id, Class<T> entityClass) throws DAOException {
    try {
      return this.mongoTemplate.findById(id, entityClass);
    } catch (Exception e) {
      throw new DAOException(e);
    }
  }

  /**
   * 根据条件统计
   *
   * @param query
   * @return
   */
  public long count(Query query) throws DAOException {
    try {
      return this.mongoTemplate.count(query, entityClass);
    } catch (Exception e) {
      throw new DAOException(e);
    }
  }

  /**
   * 插入（不会判断更新）
   *
   * @param entity
   */
  public void insert(T entity) throws DAOException {
    try {
      this.mongoTemplate.insert(entity);
    } catch (Exception e) {
      throw new DAOException(e);
    }
  }

  /**
   * 有id 就更新 没有id就新加
   *
   * @param entity
   */
  public void save(T entity) throws DAOException {
    try {
      this.mongoTemplate.save(entity);
    } catch (Exception e) {
      throw new DAOException(e);
    }
  }

  /**
   * 更新
   *
   * @param entity
   */
  public void update(T entity) throws DAOException {
    try {
      this.mongoTemplate.save(entity);
    } catch (Exception e) {
      throw new DAOException(e);
    }
  }

  /**
   * 删除
   *
   * @param entity
   * @return
   */
  public WriteResult remove(T entity) throws DAOException {
    try {
      return this.mongoTemplate.remove(entity);
    } catch (Exception e) {
      throw new DAOException(e);
    }
  }

  /**
   * 根据对象删除
   *
   * @param query
   * @return
   */
  public WriteResult remove(Query query) throws DAOException {
    try {
      return this.mongoTemplate.remove(query, entityClass);
    } catch (Exception e) {
      throw new DAOException(e);
    }
  }

  /**
   * 获取操作mongo 的原始对象
   *
   * @return
   */
  public MongoTemplate getMongoTemplate() {
    return mongoTemplate;
  }

}
