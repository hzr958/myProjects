package com.smate.core.base.consts.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Repository;

import com.smate.core.base.consts.model.ConstDiscipline;
import com.smate.core.base.exception.DAOException;
import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.core.base.utils.string.StringUtils;

/**
 * 学科代码表
 * 
 * @author zk
 * 
 */
@Repository(value = "constDisciplineDao")
public class ConstDisciplineDao extends SnsHibernateDao<ConstDiscipline, Long> implements Serializable {

  private static final long serialVersionUID = -5340741606759540092L;

  /**
   * 获取学科代码.
   * 
   * @param id
   * @return
   * @throws DaoException
   */
  public ConstDiscipline getConstDisciplineById(Long id) throws Exception {
    String hql = "from ConstDiscipline t where t.id = ? ";
    return super.findUnique(hql, id);
  }

  /**
   * 获取学科代码.
   *
   * @param discCode
   * @return
   * @throws Exception
   */
  public ConstDiscipline getConstDisciplineByDisCode(String discCode) {
    if (StringUtils.isBlank(discCode))
      return null;
    String hql = "from ConstDiscipline t where t.discCode = ? ";
    return super.findUnique(hql, discCode);
  }

  /**
   * 获取一级科技领域
   * 
   * @return
   * @throws Exception
   */
  public List<ConstDiscipline> getConstDiscipline() throws Exception {
    String hql = "from ConstDiscipline t where t.superId is null";
    return super.createQuery(hql).list();
  }

  /**
   * 获取一级科技领域下的所有子几点的领域id
   * 
   * @return
   * @throws Exception
   */
  @SuppressWarnings("unchecked")
  public List<Long> getConstDisciplineSubAll(Long firstDisId) throws Exception {
    String sql =
        "select distinct t.ID from const_discipline t start with t.SUPER_ID=:firstDisId connect by prior t.id=t.SUPER_ID";
    List<Object> list = super.getSession().createSQLQuery(sql).setParameter("firstDisId", firstDisId).list();
    List<Long> ll = new ArrayList<Long>();
    if (CollectionUtils.isNotEmpty(list)) {
      for (Object obj : list) {
        ll.add(NumberUtils.toLong(obj.toString()));
      }
    }
    return ll;
  }

  /**
   * 获取科技领域的父节点和自己的科技领域名称
   * 
   * @return
   * @throws Exception
   */
  @SuppressWarnings("unchecked")
  public List<Object> getConstDisciplineName(Long subDisId) throws Exception {
    String sql =
        "select distinct t.id,t.zh_name,t.en_name from const_discipline t start with t.id=:subDisId connect by prior t.SUPER_ID=t.id";
    return super.getSession().createSQLQuery(sql).setParameter("subDisId", subDisId).list();
  }

  /**
   * 获取科技领域的名称
   * 
   * @return
   * @throws Exception
   */
  @SuppressWarnings("unchecked")
  public List<ConstDiscipline> getConstDisciplineNameByfundId(List<Long> disIdList) throws Exception {
    String hql = "select new ConstDiscipline(t.id,t.zhName,t.enName) from ConstDiscipline t where t.id in(:disIdList)";
    return super.createQuery(hql).setParameterList("disIdList", disIdList).list();
  }

  /**
   * 通过学科代码查询学科领域数据
   *
   * @author houchuanjie
   * @date 2018年3月21日 下午2:47:21
   * @param discCode
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public List<ConstDiscipline> findByDiscCode(String discCode) {
    if (StringUtils.isBlank(discCode)) {
      return super.createQuery("from ConstDiscipline t where discCode like '_' order by id").list();
    } else {
      return super.createQuery("from ConstDiscipline t where discCode like ? order by id", discCode + "__").list();
    }

  }

  // 通过学科代码查询学科领域数据
  @SuppressWarnings("unchecked")
  public List<ConstDiscipline> findDiscData(String discCode) {
    if (StringUtils.isBlank(discCode)) {
      return super.createQuery("from ConstDiscipline t where discCode like '_' order by  id").list();
    } else {
      return super.createQuery("from ConstDiscipline t where discCode like ? order by  id", discCode + "__").list();
    }

  }

  /**
   * 删除所有数据，供数据同步时使用.
   * 
   * @throws DaoException
   */
  public void removeAll() throws DAOException {

    super.createQuery("delete from ConstDiscipline ").executeUpdate();
  }

  /**
   * 获取所有父学科列表.
   * 
   * @param ids
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public List<ConstDiscipline> getSuperConstDiscipline() throws DAOException {

    return super.createQuery("from ConstDiscipline t where superId is null order by id").list();
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

  /**
   * 更新学科代码名称.
   * 
   * @param id
   * @param name
   * @param zhOrEn
   * @throws DaoException
   */
  public void updateConstDisciplineById(Long id, String name, int zhOrEn) throws DAOException {
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
   * 获取所有指定父学科代码的子学科列表.
   * 
   * @param ids
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public List<ConstDiscipline> getSubConstDiscipline(Long superId) throws DAOException {

    return super.createQuery("from ConstDiscipline t where superId = ? order by  id", superId).list();
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

  public String getDisCodeById(Long id) {
    String hql = "select t.discCode from ConstDiscipline t where t.id = ? ";
    return super.findUnique(hql, id);
  }

  /**
   * 通过学科名称获取学科代码id（中英文都可以）
   *
   * @param name
   * @return
   */
  public Long getIdByName(String name) {
    String hql = "select id from ConstDiscipline t where LOWER(t.zhName) = LOWER(?) or LOWER(t.enName)=LOWER(?)";
    return super.findUnique(hql, name, name);
  }


}
