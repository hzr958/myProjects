package com.smate.center.batch.dao.sns.pub;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.CacheMode;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.springframework.stereotype.Repository;

import com.smate.center.batch.exception.pub.DaoException;
import com.smate.center.batch.model.sns.pub.ConstDiscipline;
import com.smate.core.base.utils.data.SnsHibernateDao;


/**
 * 
 * @author liqinghua
 * 
 */
@Repository(value = "constDisciplineDao")
public class ConstDisciplineDao extends SnsHibernateDao<ConstDiscipline, Long> {

  /**
   * 删除所有数据，供数据同步时使用.
   * 
   * @throws DaoException
   */
  public void removeAll() throws DaoException {

    super.createQuery("delete from ConstDiscipline ").executeUpdate();
  }

  /**
   * 获取所有数据，直接查询数据库，未经缓存，同步数据时使用.
   * 
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public List<ConstDiscipline> getAllNoCacheConstDiscipline() throws DaoException {

    Query query = super.createQuery("from ConstDiscipline t  order by  id");
    query.setCacheMode(CacheMode.IGNORE);
    return query.list();
  }

  /**
   * 获取指定ID列表的学科列表.
   * 
   * @param ids
   * @return
   * @throws DaoException
   */
  @Deprecated
  @SuppressWarnings("unchecked")
  public List<ConstDiscipline> getConstDisciplineByIds(List<Long> ids) throws DaoException {

    return super.createQuery("from ConstDiscipline t where t.id in(:ids) order by  id").setParameterList("ids", ids)
        .list();
  }

  /**
   * 获取所有父学科列表.
   * 
   * @param ids
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public List<ConstDiscipline> getSuperConstDiscipline() throws DaoException {

    return super.createQuery("from ConstDiscipline t where superId is null order by id").list();
  }

  /**
   * 获取所有指定父学科代码的子学科列表.
   * 
   * @param ids
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public List<ConstDiscipline> getSubConstDiscipline(Long superId) throws DaoException {

    return super.createQuery("from ConstDiscipline t where superId = ? order by  id", superId).list();
  }

  /**
   * 通过CODE获取ID.
   * 
   * @param code
   * @return
   */
  public Long getIdByCode(String code) {

    if (StringUtils.isBlank(code))
      return null;
    String hql = "select id from ConstDiscipline t where t.code = ? ";
    return super.findUnique(hql, code);
  }

  public Long getDisSuperId(Long id) {
    String hql = "select superId from ConstDiscipline t where t.id = ? ";
    return super.findUnique(hql, id);
  }

  /**
   * 获取学科代码.
   * 
   * @param id
   * @return
   * @throws DaoException
   */
  public ConstDiscipline getConstDisciplineById(Long id) throws DaoException {
    String hql = "from ConstDiscipline t where t.id = ? ";
    return super.findUnique(hql, id);
  }

  /**
   * 获取学科信息.
   * 
   * @param code
   * @return
   * @throws DaoException
   */
  public ConstDiscipline getConstDisciplineByDiscCode(String discCode) throws DaoException {
    String hql = "from ConstDiscipline t where t.discCode = ? ";
    return super.findUnique(hql, discCode);
  }

  /**
   * 更新学科代码名称.
   * 
   * @param id
   * @param name
   * @param zhOrEn
   * @throws DaoException
   */
  public void updateConstDisciplineById(Long id, String name, int zhOrEn) throws DaoException {
    StringBuilder hql = new StringBuilder();
    hql.append("update ConstDiscipline t ");
    if (zhOrEn == 1) {
      hql.append(" set t.zhName=?");
    } else {
      hql.append(" set t.enName=?");
    }
    hql.append(" where t.id=?");
    super.createQuery(hql.toString(), new Object[] {name, id}).executeUpdate();
  }

  /**
   * 获取子学科领域的学科领域分类ID列表.
   * 
   * @param subDiscIds
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Long> getDiscCategory(List<Long> subDiscIds) {

    String hql =
        "select distinct id from ConstDiscipline  where discCode in(select substr(discCode,0,1) from ConstDiscipline  where id in(:ids) )";
    return super.createQuery(hql).setParameterList("ids", subDiscIds).list();
  }

  /**
   * 通过discCode获取ID.
   * 
   * @param discCode
   * @return
   */
  public Long getIdByDiscCode(String discCode) {

    if (StringUtils.isBlank(discCode))
      return null;
    String hql = "select id from ConstDiscipline t where t.discCode = ? ";
    return super.findUnique(hql, discCode);
  }

  public ConstDiscipline findByDiscCode(String discCode) {

    if (StringUtils.isBlank(discCode))
      return null;
    String hql = "from ConstDiscipline t where t.discCode = ? ";
    return super.findUnique(hql, discCode);
  }

  public String getDisCodeById(Long id) {
    String hql = "select t.discCode from ConstDiscipline t where t.id = ? ";
    return super.findUnique(hql, id);
  }

  // 通过学科代码查询学科领域数据
  @SuppressWarnings("unchecked")
  public List<ConstDiscipline> findDiscData(String discCode) throws DaoException {
    if (StringUtils.isBlank(discCode)) {
      return super.createQuery("from ConstDiscipline t where discCode like '_' order by  id").list();
    } else {
      return super.createQuery("from ConstDiscipline t where discCode like ? order by  id", discCode + "__").list();
    }

  }

  // 推荐学科领域
  @SuppressWarnings("unchecked")
  public List<ConstDiscipline> findDiscData(List<String> discCodes, int size) throws DaoException {
    return super.createQuery("from ConstDiscipline t where discCode in (:discCodes) ")
        .setParameterList("discCodes", discCodes).setMaxResults(size).list();
  }

  // 输入学科领域自动提示
  @SuppressWarnings("unchecked")
  public List<ConstDiscipline> findDiscDataByName(String startWith, int discLevel, int size) throws DaoException {
    boolean isEnglish = StringUtils.isAsciiPrintable(startWith);
    if (isEnglish) {
      startWith = startWith.trim().toLowerCase() + "%";
      return super.createQuery(
          "from ConstDiscipline t where lower(t.enName) like ? and length(t.discCode)>=? order by case when lower(t.enName) like ? then 0 else t.id end",
          "%" + startWith, 2 * discLevel - 1, startWith).setMaxResults(size).list();
    } else {
      startWith = startWith.trim() + "%";
      return super.createQuery(
          "from ConstDiscipline t where t.zhName like ?  and length(t.discCode)>=? order by case when t.zhName like ? then 0 else t.id end",
          "%" + startWith, 2 * discLevel - 1, startWith).setMaxResults(size).list();
    }
  }

  // 通过学科领域名获取学科领域名 by zk
  @SuppressWarnings("unchecked")
  public List<ConstDiscipline> findDiscData(List<Long> discIdList) {
    List<ConstDiscipline> resultList = null;
    if (CollectionUtils.isNotEmpty(discIdList)) {
      StringBuffer sql = new StringBuffer();
      sql.append("from ConstDiscipline t where 1=1 ");
      if (discIdList.size() >= 1000) {
        String sqlConditions = super.getSqlStrByList(discIdList, 800, "id");
        sql.append(sqlConditions);
        resultList = super.createQuery(sql.toString()).list();
      } else {
        resultList = super.createQuery("from ConstDiscipline t where t.id in (:discId)")
            .setParameterList("discId", discIdList).list();
      }
    }
    return resultList;
  }

  /**
   * 查询指定discCode的领域id<适用于基金类别推荐邮件>
   * 
   * zk
   */

  @SuppressWarnings("unchecked")
  public List<Long> getDiscIdForFundCategoryRecmd(String discCode) throws DaoException {
    String hql = "select id from ConstDiscipline  where discCode like ?";
    return super.createQuery(hql, discCode).list();
  }

  /**
   * 获取学科代码的所有父级学科ID.
   * 
   * @param discIdList
   * @return
   */
  @SuppressWarnings("rawtypes")
  public List<Long> getSuperDiscIdByCode(List<String> discCodeList) {
    List<Long> resultList = new ArrayList<Long>();
    if (CollectionUtils.isNotEmpty(discCodeList)) {
      StringBuilder sql = new StringBuilder();
      sql.append(
          "select distinct t.id from const_discipline t start with DISC_CODE in (:discCode) connect by prior super_id = id order by id ");
      SQLQuery sqlQuery = super.getSession().createSQLQuery(sql.toString());
      sqlQuery.setParameterList("discCode", discCodeList);
      List queryList = sqlQuery.list();
      if (CollectionUtils.isNotEmpty(queryList)) {
        for (int i = 0; i < queryList.size(); i++) {
          Object obj = queryList.get(i);
          resultList.add(((java.math.BigDecimal) obj).longValue());
        }
      }
    }
    return resultList;
  }

  /**
   * 获取学科代码的所有父级学科ID.
   * 
   * @param discIdList
   * @return
   */
  @SuppressWarnings({"rawtypes"})
  public List<Long> getSuperDiscIdList(List<Long> discIdList) {
    List<Long> resultList = new ArrayList<Long>();
    if (CollectionUtils.isNotEmpty(discIdList)) {
      StringBuilder sql = new StringBuilder();
      sql.append(
          "select distinct t.id from const_discipline t start with id in (:discId) connect by prior super_id = id order by id ");
      SQLQuery sqlQuery = super.getSession().createSQLQuery(sql.toString());
      sqlQuery.setParameterList("discId", discIdList);
      List queryList = sqlQuery.list();
      if (CollectionUtils.isNotEmpty(queryList)) {
        for (int i = 0; i < queryList.size(); i++) {
          Object obj = queryList.get(i);
          resultList.add(((java.math.BigDecimal) obj).longValue());
        }
      }
    }
    return resultList;
  }
}
