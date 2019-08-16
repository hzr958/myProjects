package com.smate.center.batch.dao.pdwh.pub;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.HibernateCallback;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.util.Assert;

import com.smate.center.batch.exception.pub.DaoException;
import com.smate.core.base.utils.constant.DBSessionEnum;
import com.smate.core.base.utils.data.HibernateDao;
import com.smate.core.base.utils.data.ReflectionUtils;
import com.smate.core.base.utils.model.Page;


/**
 * @param <T> DAO操作的对象类型
 * @param <PK> 主键类型
 * 
 * 
 */
@SuppressWarnings("unchecked")
public class HibernateTemplateDao<T, PK extends Serializable> extends HibernateDao<T, PK> {

  protected Logger logger = LoggerFactory.getLogger(getClass());
  protected Class<T> entityClass;
  @Autowired
  protected HibernateTemplate hibernateTemplate;

  public HibernateTemplateDao() {
    this.entityClass = ReflectionUtils.getSuperClassGenricType(getClass());
  }

  public Page<T> getAll(final Page<T> page, final Object entity) throws DaoException {
    Assert.notNull(hibernateTemplate);
    try {
      page.setTotalCount(dataCounct(entity));
      List list = hibernateTemplate.findByCriteria(searchCriteria(entity), (page.getFirst() - 1), page.getPageSize());
      page.setResult(list);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return page;
  }

  protected int dataCounct(final Object entity) {
    Assert.notNull(hibernateTemplate);
    final DetachedCriteria criteria = searchCriteria(entity);
    Object count = hibernateTemplate.execute(new HibernateCallback() {
      public Object doInHibernate(Session s) throws HibernateException {
        Criteria c = criteria.getExecutableCriteria(s);
        return c.setProjection(Projections.rowCount()).uniqueResult();
      }
    });
    return Integer.parseInt(String.valueOf(count));
  }

  protected DetachedCriteria searchCriteria(final Object entity) {
    DetachedCriteria criteria = DetachedCriteria.forClass(entity.getClass());
    return criteria;
  }

  public List<T> find(final DetachedCriteria criteria) throws DaoException {
    return (List<T>) hibernateTemplate.findByCriteria(criteria);
  }

  /**
   * 保存新增或修改的对象.
   * 
   * @param entity
   * 
   */
  public void saveOrUpdate(final T entity) {
    Assert.notNull(entity, "entity不能为空");
    super.save(entity);
  }

  public Criterion[] crtCriterion(final Object entity) {
    Assert.notNull(entity, "entity不能为空间");
    List<Criterion> criterionList = new ArrayList<Criterion>();
    // criterionList.add(Restrictions.ge(propertyName, propertyValue));
    return criterionList.toArray(new Criterion[criterionList.size()]);
  }

  @Override
  public DBSessionEnum getDbSession() {

    return DBSessionEnum.PDWH;
  }

}
