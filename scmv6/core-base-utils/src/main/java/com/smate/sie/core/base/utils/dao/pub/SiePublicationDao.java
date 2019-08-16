package com.smate.sie.core.base.utils.dao.pub;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SieHibernateDao;
import com.smate.core.base.utils.model.Page;
import com.smate.sie.core.base.utils.model.pub.SiePubErrorField;
import com.smate.sie.core.base.utils.model.pub.SiePublication;

/**
 * 成果Dao
 * 
 * @author hd
 *
 */
@Repository
public class SiePublicationDao extends SieHibernateDao<SiePublication, Long> {

  /**
   * 单位的成果统计
   * 
   * @param insId
   * @return
   */
  public Long getConfirmPubTotalNumByInsId(Long insId) {
    String hql = "select count(id) from SiePublication where status <> '1' and insId = ?";
    return super.findUnique(hql, insId);
  }

  /**
   * 通过INSID来查询成果的列表内容
   */
  @SuppressWarnings("unchecked")
  public List<SiePublication> getListByInsId(Long insId) {
    return super.createQuery(" from SiePublication where insId =? order by pubId ", insId).list();
  }

  /**
   * 获取随机的maxSize条数据
   */
  @SuppressWarnings("unchecked")
  public List<SiePublication> getListByInsIdOrderRandom(Long insId, int maxSize) {
    String sql =
        "select t.* from publication t where t.is_public = 1 and t.status=0 and t.ins_id=:insId order by dbms_random.value ";
    Query query = super.getSession().createSQLQuery(sql).addEntity(SiePublication.class);
    query.setParameter("insId", insId);
    query.setFirstResult(0);
    query.setMaxResults(maxSize);
    return query.list();
  }

  /**
   * 保存错误字段.
   * 
   * @param errorField
   * @throws DaoException
   */
  public void saveErrorField(SiePubErrorField errorField) {
    super.getSession().save(errorField);
  }

  /**
   * 删除错误字段.
   * 
   * @param pubId
   * @throws DaoException
   */
  public void deleteErrorFields(long pubId) {
    super.createQuery("delete from SiePubErrorField t where t.pubId = ?", new Object[] {pubId}).executeUpdate();
  }

  /**
   * 批量获取成果
   * 
   * @param insId
   * @param page
   * @return
   */
  @SuppressWarnings("unchecked")
  public Page<SiePublication> findPublicationByInsId(Long insId, Page<SiePublication> page) {
    String hql = "from SiePublication where insId =? order by pubId asc";
    Object[] objects = new Object[] {insId};
    Query q = createQuery(hql, objects);
    if (page.isAutoCount()) {
      long totalCount = countHqlResult(hql, objects);
      page.setTotalCount(totalCount);
    }
    setPageParameter(q, page);
    List<SiePublication> result = q.list();
    page.setResult(result);
    return page;
  }


  /**
   * 联表(classSimpleName对应的表) 批量获取成果
   * 
   * @param insId
   * @param page
   * @param types
   * @param endTime
   * @param beginTime
   * @param classSimpleName
   * @return
   */
  @SuppressWarnings("unchecked")
  public Page<SiePublication> findPublicationByInsIdWithBhX(Long insId, Page<SiePublication> page, String types,
      Date beginTime, Date endTime, String classSimpleName) {

    StringBuilder listhql = new StringBuilder();
    StringBuilder countHql = new StringBuilder();
    StringBuilder publicHql = new StringBuilder();

    publicHql.append("from SiePublication t, " + classSimpleName + " r ");
    publicHql.append("where t.insId =? and t.pubId=r.keyId ");
    publicHql.append("and r.type in (" + types + ") ");
    publicHql.append("and r.creDate >=? and r.creDate<? ");


    listhql.append("select distinct new SiePublication(t.pubId, t.zhTitle, t.enTitle) ");
    listhql.append(publicHql.toString());
    listhql.append("order by t.pubId asc ");

    countHql.append("select count(distinct t.pubId) ");
    countHql.append(publicHql.toString());

    Object[] objects = new Object[] {insId, beginTime, endTime};

    Query q = createQuery(listhql.toString(), objects);
    if (page.isAutoCount()) {
      long totalCount = findUnique(countHql.toString(), objects);
      page.setTotalCount(totalCount);
    }
    setPageParameter(q, page);
    List<SiePublication> result = q.list();
    page.setResult(result);
    return page;
  }

  public static void main(String args[]) {
    String name = SiePublication.class.getSimpleName();

    System.out.println(name);
  }
}
