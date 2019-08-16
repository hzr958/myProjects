package com.smate.center.job.web.support.hibernate;

import com.sun.istack.internal.NotNull;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.apache.commons.collections4.CollectionUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;

/**
 * Hibernate Criteria 查询构建器
 *
 * @author houchuanjie
 * @date 2018/05/19 18:07
 */
public class CriteriaQueryBuilder<T> {

  private Session session;
  private Class entityClass;
  private Criteria criteria;

  public CriteriaQueryBuilder(Session session, Class entityClass) {
    this.session = session;
    this.entityClass = entityClass;
    this.criteria = session.createCriteria(entityClass);
  }

  /**
   * 添加查询条件
   *
   * @param criterions
   * @return
   */
  public CriteriaQueryBuilder<T> addCriterions(List<Criterion> criterions) {
    // 查询条件
    if (CollectionUtils.isNotEmpty(criterions)) {
      criterions.forEach(this::addCriterion);
    }
    return this;
  }

  /**
   * 添加查询条件
   *
   * @param criterions
   * @return
   */
  public CriteriaQueryBuilder<T> addCriterions(@NotNull Criterion... criterions) {
    // 查询条件
    if (Objects.nonNull(criterions) && criterions.length > 0) {
      for (Criterion criterion : criterions) {
        addCriterion(criterion);
      }
    }
    return this;
  }

  public CriteriaQueryBuilder<T> addCriterion(Criterion criterion) {
    Optional.ofNullable(criterion).ifPresent(criteria::add);
    return this;
  }

  /**
   * 添加排序规则
   *
   * @param order
   * @return
   */
  public CriteriaQueryBuilder<T> addOrder(Order order) {
    // 排序条件
    Optional.ofNullable(order).ifPresent(criteria::addOrder);
    return this;
  }

  /**
   * 分页查询
   *
   * @param pageNum 页码
   * @param pageSize 每页数量
   * @return
   */
  public CriteriaQueryBuilder<T> setPageQuery(int pageNum, int pageSize) {
    if (pageNum > 0) {
      criteria.setFirstResult((pageNum - 1) * pageSize);
    }
    if (pageSize > 0) {
      criteria.setMaxResults(pageSize);
    }
    return this;
  }

  /**
   * 构建查询列表
   *
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<T> buildQueryList() {
    return (List<T>) criteria.list();
  }

  /**
   * 构建查询数量
   *
   * @return
   */
  public Long buildQueryCount() {
    return (Long) criteria.setProjection(Projections.rowCount()).uniqueResult();
  }
}
