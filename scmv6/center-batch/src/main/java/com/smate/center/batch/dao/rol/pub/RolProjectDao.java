package com.smate.center.batch.dao.rol.pub;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.smate.center.batch.exception.pub.DaoException;
import com.smate.center.batch.model.rol.pub.RolProject;
import com.smate.core.base.project.model.PrjErrorFields;
import com.smate.core.base.utils.constant.ServiceConstants;
import com.smate.core.base.utils.data.RolHibernateDao;
import com.smate.core.base.utils.string.ServiceUtil;

/**
 * 项目DAO.
 * 
 * @author liqinghua
 * 
 */
@Repository
public class RolProjectDao extends RolHibernateDao<RolProject, Long> {

  /**
   * 保存项目的错误信息.
   * 
   * @param error
   * @throws DaoException
   */
  public void savePrjErrorFields(PrjErrorFields error) throws DaoException {

    super.getSession().save(error);
  }

  /**
   * 删除项目的错误信息.
   * 
   * @param pubId
   * @throws DaoException
   */
  public void removePrjErrorFieldsByPrjId(Long prjId) throws DaoException {

    super.createQuery("delete from PrjErrorFields t where t.prjId = ? ", prjId).executeUpdate();

  }

  /**
   * 通过项目ID(，逗号分隔)获取项目列表.
   * 
   * @param prjIds
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<RolProject> getProjectByPrjIds(List<Long> prjIds) {

    if (prjIds != null && prjIds.size() > 0) {
      String hql = "from RolProject t where t.id in (:prjIds)";
      return super.createQuery(hql).setParameterList("prjIds", prjIds).list();
    }
    return null;
  }

  /**
   * 通过项目ID(，逗号分隔)获取项目列表.
   * 
   * @param prjIds
   * @return
   * @throws DaoException
   */
  public List<RolProject> getProjectByPrjIds(String prjIds) throws DaoException {

    if (prjIds != null && prjIds.matches(ServiceConstants.IDPATTERN)) {
      return this.getProjectByPrjIds(ServiceUtil.splitStrToLong(prjIds));
    }
    return null;
  }

  /**
   * 批量获取RolProject表数据，数据同步后重构XML用.
   * 
   * @param lastId
   * @param batchSize
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<RolProject> getPrjByBatchForOld(Long lastId, int batchSize) {

    String sql = "from RolProject p where p.id > ? order by id asc";

    return super.createQuery(sql, lastId).setMaxResults(batchSize).list();
  }

  @SuppressWarnings("unchecked")
  public List<Long> getPrjByBatchAmountUnitDbId13(Long lastId, int batchSize) {
    String hql = "select id from RolProject where id > ? and dbId=13 and amountUnit is not null order by id asc";
    return super.createQuery(hql, lastId).setMaxResults(batchSize).list();
  }

  /**
   * 批量获取数据库表中的数据重构XML用.
   * 
   * @param lastId
   * @param batchSize
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<RolProject> loadRebuildPrjId(Long lastId, int batchSize) {

    String hql = "select new RolProject(id) from RolProject where id > ? order by id asc";
    return super.createQuery(hql, lastId).setMaxResults(batchSize).list();
  }

  /**
   * 批量获取项目.
   * 
   * @param lastId
   * @param batchSize
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public List<Long> findPrjIdsBatch(Long lastId, int batchSize) throws DaoException {
    String hql = "select t.id from RolProject t where t.id > ?";
    return super.createQuery(hql, lastId).setMaxResults(batchSize).list();
  }

  /**
   * 获取资助机构定义的项目编号.
   * 
   * @param prjId
   * @return
   */
  public String getPrjExternalNo(Long prjId) {

    String hql = "select externalNo from RolProject t where t.id = ? and t.status = 0 ";
    return super.findUnique(hql, prjId);
  }

  /**
   * 传入成果项目信息，查找包含资助编号的项目ID.
   * 
   * @param insId
   * @param pubFundInfo
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Long> findPrjByPubFund(Long insId, String pubFundInfo) {

    if (StringUtils.isBlank(pubFundInfo)) {
      return null;
    }
    String hql =
        "select id from RolProject t where t.ownerInsId = ? and t.status = 0 and ? like '%'||lower(t.externalNo)||'%' and t.externalNo is not null";
    return super.createQuery(hql, insId, pubFundInfo.trim().toLowerCase()).list();
  }

  @SuppressWarnings("unchecked")
  public List<RolProject> queryRolPrjByUpdatePsnId(Long psnId) {
    return super.createQuery("from RolProject t where t.updatePsnId=?", psnId).list();
  }

  @SuppressWarnings("unchecked")
  public List<RolProject> queryRolPrjByCreatePsnId(Long psnId) {
    return super.createQuery("from RolProject t where t.createPsnId=?", psnId).list();
  }

  public Long getInsProTotalNum(Long insId) {
    String hql = "select count(id) from RolProject where ownerInsId = ? ";
    return super.findUnique(hql, insId);
  }

}
