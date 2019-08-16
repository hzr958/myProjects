package com.smate.web.v8pub.dao.searchimport;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.v8pub.po.searchimport.InsAlias;
import com.smate.web.v8pub.po.searchimport.InsAliasId;

/**
 * 
 * @author fanzhiqiang
 * 
 */
@Repository
public class InsAliasDao extends SnsHibernateDao<InsAlias, Long> {

  /**
   * 根据指定的insId,dbId获得对应的单位别名.
   * 
   * @param insId
   * @param dbId
   * @return
   */
  public InsAlias getAliasName(Long insId, Long dbId) {
    String sql = " from InsAlias where insAliasId.insId=? and insAliasId.dbId=?";
    Query query = super.createQuery(sql, insId, dbId);
    return (InsAlias) query.uniqueResult();
  }

  /**
   * 获取指定单位.
   * 
   * @param insIds
   * @param dbIds
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<InsAliasId> getAliasStatistics(List<Long> insIds, List<Long> dbIds) {

    String hql = "select insAliasId from InsAlias where insAliasId.insId in(:insIds) and insAliasId.dbId in(:dbIds)";
    return super.createQuery(hql).setParameterList("insIds", insIds).setParameterList("dbIds", dbIds).list();
  }

  /**
   * 获取单位检索式列表.
   * 
   * @param insId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<InsAlias> getInsAlias(Long insId) {

    String hql = "from InsAlias t where t.insAliasId.insId = ? ";
    return super.createQuery(hql, insId).list();
  }

  /**
   * 保存单位检索式.
   * 
   * @param insId
   * @param dbId
   * @param insName
   * @return
   */
  public InsAlias saveInsAlias(Long insId, Long dbId, String insName) {

    if (StringUtils.isBlank(insName)) {
      return null;
    }
    InsAlias insAlias = this.getAliasName(insId, dbId);
    if (insAlias == null) {
      insAlias = new InsAlias(insId, dbId, insName);
    }
    insAlias.setName(insName);
    this.save(insAlias);
    return insAlias;
  }

  /**
   * 单位别名是否完整.
   * 
   * @param insId
   * @param dbId
   * @return
   */
  public boolean isInsAliasComplete(Long insId, List<Long> dbIds) {

    String hql =
        "select count(t.insAliasId.dbId) from InsAlias t where t.insAliasId.insId = :insId and t.insAliasId.dbId in(:dbId) ";
    Long count =
        (Long) super.createQuery(hql).setParameter("insId", insId).setParameterList("dbId", dbIds).uniqueResult();
    if (count < dbIds.size()) {
      return false;
    }
    return true;
  }
}
