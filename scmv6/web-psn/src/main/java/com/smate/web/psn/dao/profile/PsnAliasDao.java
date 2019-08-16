package com.smate.web.psn.dao.profile;

import java.util.List;
import java.util.Map;

import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.psn.exception.DaoException;
import com.smate.web.psn.model.profile.PsnAlias;

@Repository
public class PsnAliasDao extends SnsHibernateDao<PsnAlias, Long> {

  public boolean updatePsnAliasStatus(Integer status, Long psnId, Integer dbId) {
    boolean flag = true;
    String hql = "update PsnAlias t set t.status = ? where t.psnId = ? and t.dbId = ?";
    try {
      super.createQuery(hql, status, psnId, dbId).executeUpdate();
    } catch (Exception e) {
      logger.debug(e.toString() + "PsnAlias数据更新错误");
      flag = false;
    }
    return flag;
  }

  @SuppressWarnings("unchecked")
  public List<PsnAlias> findAllPsnAlias(Long psnId, Long dbId) {
    String hql = "from PsnAlias t where t.psnId = ? and t.dbId = ?";
    List<PsnAlias> psnAlias = null;
    try {
      psnAlias = super.createQuery(hql, psnId, dbId).list();
    } catch (Exception e) {
      logger.debug(e.toString() + "PsnAlias数据取出出错");
      return null;
    }
    return psnAlias;
  }

  /**
   * 查询指定人员在指定的dbID列表中的所有别名排除
   * 
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<PsnAlias> getPsnAliasInDbIdList(Long psnId, String psnName, Long... dbIds) {
    String hql = "from PsnAlias t where t.psnId =:psnId and t.psnName =:psnName and t.status<>0 and t.dbId in (:dbIds)";
    return super.createQuery(hql).setParameter("psnId", psnId).setParameter("psnName", psnName)
        .setParameterList("dbIds", dbIds).list();
  }

  public PsnAlias findUniquePsnAlias(Long psnId, String psnName, Long dbId, Long hashName) throws DaoException {
    String hql = "from PsnAlias t where t.psnId = ? and t.psnName = ? and t.dbId = ? and t.hashName = ?";
    PsnAlias psnAlias = null;
    try {
      psnAlias = (PsnAlias) super.createQuery(hql, psnId, psnName, dbId, hashName).uniqueResult();
    } catch (Exception e) {
      logger.debug(e.toString() + "PsnAlias单个数据取出错误");
      return null;
    }
    return psnAlias;
  }

  @SuppressWarnings("unchecked")
  public List<Map> findPsnAliasFiled(Long psnId, String psnName, Long dbId) throws DaoException {
    String hql =
        "select t.id ,t.name from Psn_Alias t where t.psn_Id = ? and t.psn_name = ? and t.dbId = ? and t.status <> 0";
    List<Map> psnAlias = null;
    try {
      psnAlias = super.getSession().createSQLQuery(hql).setParameter(0, psnId).setParameter(1, psnName)
          .setParameter(2, dbId).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
    } catch (Exception e) {
      logger.debug(e.toString() + "PsnAlias数据取出出错");
      return null;
    }

    return psnAlias;
  }

  public PsnAlias getPsnAliasCount(PsnAlias entity) {
    String hql = "from PsnAlias t where t.psnId = ? and t.psnName = ? and t.dbId = ? and t.hashName=?";
    return super.findUnique(hql, entity.getPsnId(), entity.getpsnName(), entity.getDbId(), entity.getHashName());
  }

  @Override
  public void save(PsnAlias entity) {
    PsnAlias psnAlias = getPsnAliasCount(entity);
    if (psnAlias == null) {
      super.save(entity);
    }
  }

  public void deleteByPsnId(Long psnId) {
    String hql = "delete from PsnAlias where psnId=:psnId";
    super.createQuery(hql).setParameter("psnId", psnId).executeUpdate();
  }

  public List<PsnAlias> getPsnAlias(Long psnId) throws DaoException {
    return super.createQuery("from PsnAlias where psnId = ?", psnId).list();
  }
}
