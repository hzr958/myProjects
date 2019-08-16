package com.smate.center.batch.service.pub;

import java.io.Serializable;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.core.base.utils.data.HibernateDao;
import com.smate.core.base.utils.model.Page;
import com.smate.core.base.utils.model.PropertyFilter;

/**
 * 领域对象业务管理类基类.
 * 
 * @param <T> 领域对象类型
 * @param <PK> 领域对象的主键类型
 * 
 *        eg. public class UserManager extends EntityManager<User, Long>{ }
 * 
 * 
 */
@Transactional(rollbackFor = Exception.class)
public abstract class EntityManagerImpl<T, PK extends Serializable> implements EntityManager<T, PK> {

  protected Logger logger = LoggerFactory.getLogger(getClass());

  protected abstract HibernateDao<T, PK> getEntityDao();

  // CRUD函数 //

  /*
   * (non-Javadoc)
   * 
   * @see com.iris.scm.service.orm.Hibernate.EntityManager#get(PK)
   */
  @Transactional(rollbackFor = Exception.class, readOnly = true)
  public T get(final PK id) {
    return getEntityDao().get(id);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.iris.scm.service.orm.Hibernate.EntityManager#getAll(org.springside.modules.orm.Page)
   */
  @Transactional(rollbackFor = Exception.class, readOnly = true)
  public Page<T> getAll(final Page<T> page) {
    return getEntityDao().getAll(page);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.iris.scm.service.orm.Hibernate.EntityManager#getAll()
   */
  @Transactional(rollbackFor = Exception.class, readOnly = true)
  public List<T> getAll() {
    return getEntityDao().getAll();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.iris.scm.service.orm.Hibernate.EntityManager#search(org.springside.modules.orm.Page,
   * java.util.List)
   */
  @Transactional(rollbackFor = Exception.class, readOnly = true)
  public Page<T> search(final Page<T> page, final List<PropertyFilter> filters) {
    return getEntityDao().findPage(page, filters);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.iris.scm.service.orm.Hibernate.EntityManager#save(T)
   */
  public void save(final T entity) {
    getEntityDao().save(entity);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.iris.scm.service.orm.Hibernate.EntityManager#delete(PK)
   */
  public void delete(final PK id) throws ServiceException {
    getEntityDao().delete(id);
  }
}
