package com.smate.sie.core.base.utils.dao.pub;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SieHibernateDao;
import com.smate.core.base.utils.model.Page;
import com.smate.sie.core.base.utils.model.pub.SiePatErrorFields;
import com.smate.sie.core.base.utils.model.pub.SiePatent;

/**
 * 专利Dao
 * 
 * @author hd
 *
 */
@Repository
public class SiePatentDao extends SieHibernateDao<SiePatent, Long> {
  /**
   * 单位专利统计
   * 
   * @param insId
   * @return
   */
  public Long getConfirmPtTotalNumByInsId(Long insId) {
    String hql = "select count(id) from SiePatent where status <> '1' and insId = ? ";
    return super.findUnique(hql, insId);
  }


  /**
   * 保存错误字段.
   * 
   * @param errorField
   * @throws DaoException
   */
  public void saveErrorField(SiePatErrorFields errorField) {
    super.getSession().save(errorField);
  }

  /**
   * 删除错误字段.
   * 
   * @param pubId
   * @throws DaoException
   */
  public void deleteErrorFields(Long patId) {
    super.createQuery("delete from SiePatErrorFields t where t.patId = ?", new Object[] {patId}).executeUpdate();
  }

  @SuppressWarnings("unchecked")
  public List<SiePatent> getListByInsId(Long insId) {
    String hql = " from SiePatent t where t.insId= ? order by patId ";
    return super.createQuery(hql, insId).list();
  }

  /**
   * 获取随机的maxSize条数据
   */
  @SuppressWarnings("unchecked")
  public List<SiePatent> getListByInsIdOrderRandom(Long insId, int maxSize) {
    String sql =
        "select t.* from patent t where t.is_public = 1 and t.status=0 and t.ins_id=:insId order by dbms_random.value ";
    Query query = super.getSession().createSQLQuery(sql).addEntity(SiePatent.class);
    query.setParameter("insId", insId);
    query.setFirstResult(0);
    query.setMaxResults(maxSize);
    return query.list();
  }

  /**
   * 批量获取成果
   * 
   * @param insId
   * @param page
   * @return
   */
  @SuppressWarnings("unchecked")
  public Page<SiePatent> findPatentByInsId(Long insId, Page<SiePatent> page) {
    String hql = "from SiePatent where insId =? order by patId asc";
    Object[] objects = new Object[] {insId};
    Query q = createQuery(hql, objects);
    if (page.isAutoCount()) {
      long totalCount = countHqlResult(hql, objects);
      page.setTotalCount(totalCount);
    }
    setPageParameter(q, page);
    List<SiePatent> result = q.list();
    page.setResult(result);
    return page;
  }

  /**
   * 联表(classSimpleName对应的表) 批量获取
   * 
   * @param insId
   * @param page
   * @param types
   * @param endTime
   * @param beginTime
   * @param classSimpleName
   * @return
   */
  public Page<SiePatent> findPatentByInsIdWithBhX(Long insId, Page<SiePatent> page, String types, Date beginTime,
      Date endTime, String classSimpleName) {
    StringBuilder listhql = new StringBuilder();
    StringBuilder countHql = new StringBuilder();
    StringBuilder publicHql = new StringBuilder();

    publicHql.append("from SiePatent t, " + classSimpleName + " r ");
    publicHql.append("where t.insId =? and t.patId=r.keyId ");
    publicHql.append("and r.type in (" + types + ") ");
    publicHql.append("and r.creDate >=? and r.creDate<? ");


    listhql.append("select distinct new SiePatent(t.patId, t.zhTitle, t.enTitle) ");
    listhql.append(publicHql.toString());
    listhql.append("order by t.patId asc ");

    countHql.append("select count(distinct t.patId) ");
    countHql.append(publicHql.toString());

    Object[] objects = new Object[] {insId, beginTime, endTime};

    Query q = createQuery(listhql.toString(), objects);
    if (page.isAutoCount()) {
      long totalCount = findUnique(countHql.toString(), objects);
      page.setTotalCount(totalCount);
    }
    setPageParameter(q, page);
    List<SiePatent> result = q.list();
    page.setResult(result);
    return page;
  }
}
