package com.smate.center.batch.dao.rol.pub;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.smate.center.batch.exception.pub.DaoException;
import com.smate.center.batch.model.rol.pub.InsUnit;
import com.smate.center.batch.model.rol.pub.PubPsnRol;
import com.smate.core.base.utils.data.RolHibernateDao;
import com.smate.core.base.utils.string.ServiceUtil;

/**
 * 成果-人关系操作DAO.
 * 
 * @author yamingd
 * 
 */
@Repository
public class PubPsnRolDao extends RolHibernateDao<PubPsnRol, Long> {

  /**
   * 同步作者部门.
   * 
   * @param insId
   * @param psnId
   * @param insUnit
   * @throws DaoException
   */
  public void syncUnit(long insId, long psnId, InsUnit insUnit) throws DaoException {

    Long unitId = null;
    Long parentUnitId = null;
    if (insUnit != null) {
      unitId = insUnit.getId();
      parentUnitId = insUnit.getSuperInsUnitId();
    }
    String hql =
        "update PubPsnRol set unitId=" + unitId + ", parentUnitId=" + parentUnitId + " where insId=? and psnId=?";
    super.createQuery(hql, insId, psnId).executeUpdate();

  }

  /**
   * 删除部门时，同步清空成果人员中的部门冗余.
   * 
   * @param insId
   * @param unitId
   * @throws DaoException
   */
  public void syncRemoveUnit(Long insId, Long unitId) throws DaoException {

    Assert.notNull(unitId);
    String hql = "update PubPsnRol set unitId=null,parentUnitId=null where insId=? and (unitId=? or parentUnitId=?)";
    super.createQuery(hql, unitId, unitId);
  }

  /**
   * 大(小)部门合并到指定部门.
   * 
   * @param insId
   * @param olderParentInsId
   * @param newUnitId
   * @param newParentUnitId
   * @throws DaoException
   */
  public void syscMergeUnit(Long insId, Long olderParentInsId, Long newUnitId, Long newParentUnitId)
      throws DaoException {

    // parentUnitId,unitId改变
    String hql = "update PubPsnRol set unitId=?,parentUnitId=? where insId=? and (unitId=? or parentUnitId=?)";
    super.createQuery(hql, newUnitId, newParentUnitId == null ? -1 : newParentUnitId, insId, olderParentInsId,
        olderParentInsId);
  }

  /**
   * 合并大部门时，同步更新成果人员中的部门冗余.
   * 
   * @param insId
   * @param oldInsUnit
   * @param newInsUnit
   * @throws DaoException
   */
  public void syscMergeParentUnit(Long insId, Long olderParentUnitId, Long newParentUnitId) throws DaoException {
    // parentUnitId改变
    String hql = "update PubPsnRol set parentUnitId=? where insId=? and parentUnitId=?";
    super.createQuery(hql, newParentUnitId, insId, olderParentUnitId);
    // unitId改变
    hql = "update PubPsnRol set unitId=? where insId=? and unitId=?";
    super.createQuery(hql, newParentUnitId, insId, olderParentUnitId);
  }

  public boolean clearAll(long insId, long pubId) throws DaoException {

    String hql = "delete from PubPsnRol where insId=? and pubId=?";
    super.createQuery(hql, new Object[] {insId, pubId}).executeUpdate();

    // 同时更新pub-member的member_psn_id字段
    hql = "update PubMemberRol t set t.psnId=null where t.pubId = ? and t.psnId is not null ";
    int result = super.createQuery(hql, new Object[] {pubId}).executeUpdate();
    return result > 0;
  }

  /**
   * 获取成果人员列表.
   * 
   * @param pubId
   * @param insId
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public List<PubPsnRol> getPmEmptyPubPsnRol(Long pubId, Long insId) throws DaoException {

    String hql =
        "from PubPsnRol t where t.confirmResult in(0,1,2) and t.pubId = ? and t.insId = ? and authorPMId is null ";
    return super.createQuery(hql, pubId, insId).list();
  }

  /**
   * 获取与单位某成果关联的人员ID列表.
   * 
   * @param pubId
   * @param insId
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public List<Long> getPubPsnIds(Long pubId, Long insId) throws DaoException {

    String hql = "select psnId from PubPsnRol t where t.confirmResult in(0,1,2) and t.pubId = ? and t.insId = ? ";
    return super.createQuery(hql, pubId, insId).list();
  }

  /**
   * 获取人员和成果关系.
   * 
   * @throws DaoException
   */
  public PubPsnRol getAllStatusPubPsn(long pubId, long psnId) throws DaoException {

    // 先判断是否存在，不存在的话，直接返回（修改原因是因为PubPsnRol表太大，直接通过索引先判断比较合适）
    Long count =
        super.findUnique("select count(t.id) from PubPsnRol t where t.psnId = ? and t.pubId = ? ", psnId, pubId);
    if (count == 0) {
      return null;
    }
    List<PubPsnRol> list = super.find("from PubPsnRol t where t.psnId = ? and t.pubId = ? ", psnId, pubId);
    if (list != null && list.size() > 0) {
      return list.get(0);
    }
    return null;
  }

  /**
   * 获取指定成果PMID的关联人员列表.
   * 
   * @param pubId
   * @param pmId
   * @return
   */
  public List<PubPsnRol> getAllStatusPubPsnByPubPmId(long pubId, long pmId) {
    return super.find("from PubPsnRol t where t.pubId = ? and t.authorPMId = ?  ", pubId, pmId);
  }

  /**
   * 获取成果和人员关系.
   * 
   * @throws DaoException
   */
  public List<PubPsnRol> getAllStatusPubPsnByPubInsId(long pubId, long insId) {

    return super.find("from PubPsnRol t where t.pubId = ? and t.insId = ?  ", pubId, insId);
  }

  /**
   * 获取成果和人员关系.
   * 
   * @throws DaoException
   */
  public List<PubPsnRol> getPubPsnByPubInsId(long pubId, long insId) {

    return super.find("from PubPsnRol t where t.pubId = ? and t.insId = ?  and confirmResult in(0,1) ", pubId, insId);
  }

  /**
   * 获取成果和人员关系.
   * 
   * @throws DaoException
   */
  public List<PubPsnRol> getPubPsnByPmId(long pmId) {

    return super.find("from PubPsnRol t where t.authorPMId = ?  and confirmResult in(0,1) ", pmId);
  }

  /**
   * 获取人员和所有成果关系.
   * 
   * @throws DaoException
   */
  public List<PubPsnRol> getAllStatusPubPsnByPsnInsId(long psnId, long insId) {

    return super.find("from PubPsnRol t where t.psnId = ? and t.insId = ?  ", psnId, insId);
  }

  /**
   * 获取成果和部门关系.
   * 
   * @throws DaoException
   */
  public List<PubPsnRol> getPubPsnByPubUnitId(long pubId, long insId, long unitId) {

    return super.find(
        "from PubPsnRol t where t.pubId = ? and t.insId = ?  and confirmResult in(0,1) and (unitId = ? or parentUnitId = ?) ",
        pubId, insId, unitId, unitId);
  }

  /**
   * 保存人员和成果关系.
   * 
   * @throws DaoException
   */
  public PubPsnRol saveAuthorConfirmResult(long psnId, long pubId, Long pmId, int confirmResult) throws DaoException {
    PubPsnRol pubpsn = this.getAllStatusPubPsn(pubId, psnId);
    if (pubpsn != null) {
      pubpsn.setConfirmResult(confirmResult);
      pubpsn.setConfirmDate(new Date());
      if (pmId != null)
        pubpsn.setAuthorPMId(pmId);
    }
    return pubpsn;
  }

  /**
   * 判断成果人员是否已经被确认认领.
   * 
   * @param pmId
   * @return
   * @throws DaoException
   */
  public boolean isPubHasConfirm(Long pmId) throws DaoException {
    // 研究人成果的确认状态：0未确认/1已确认/2已拒绝
    String hql = "select count(t.id) from PubPsnRol t where t.authorPMId = ? and t.confirmResult = 1 ";
    Long count = super.findUnique(hql, pmId);
    if (count > 0) {
      return true;
    }
    return false;
  }

  /**
   * 通过pub_member.pm_id获取人员和成果关系列表.
   * 
   * @param pmId
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public List<PubPsnRol> getAllStatusPubPsnRolByPmId(Long pmId) throws DaoException {

    return super.createQuery("from PubPsnRol where authorPMId = ?  order by id asc ", pmId).list();
  }

  /**
   * 删除人和成果关系，并且获取指派ID，认领状态，方便统计数据.
   * 
   * @param pubId
   * @param psnId
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public List<Object[]> deletePubPsn(long pubId, long psnId) throws DaoException {

    // 查询删除的Id等
    List<Object[]> list = (List<Object[]>) super.createQuery(
        "select t.id,t.confirmResult from PubPsnRol t where t.pubId = ? and t.psnId = ?", new Object[] {pubId, psnId})
            .list();
    if (list.size() > 0) {
      Long[] ids = new Long[list.size()];
      for (int i = 0; i < list.size(); i++) {
        ids[i] = (Long) list.get(i)[0];
      }
      super.getSession().createQuery("delete from PubPsnRol t where t.id in(:id) ").setParameterList("id", ids)
          .executeUpdate();
    }
    return list;
  }

  /**
   * 删除人和成果关系.
   * 
   * @param pubId
   * @param psnId
   * @throws DaoException
   */
  public boolean resetMemberId(long pubId, long psnId) throws DaoException {

    int ret = super.createQuery("update PubMemberRol t set t.psnId=null where t.pubId = ? and t.psnId = ?",
        new Object[] {pubId, psnId}).executeUpdate();
    return ret > 0;
  }

  /**
   * 删除单位人员与成果关系，并且获取指派ID，人员ID，认领状态，方便统计数据.
   * 
   * @param pmId
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public List<Object[]> deletePubPsnByPmId(Long pmId) throws DaoException {

    // 查询删除的Id等
    List<Object[]> list = (List<Object[]>) super.createQuery(
        "select t.id,t.psnId,t.confirmResult from PubPsnRol t where t.authorPMId = ? ", new Object[] {pmId}).list();
    if (list.size() > 0) {
      Long[] ids = new Long[list.size()];
      for (int i = 0; i < list.size(); i++) {
        ids[i] = (Long) list.get(i)[0];
      }
      super.getSession().createQuery("delete from PubPsnRol t where t.id in(:id) ").setParameterList("id", ids)
          .executeUpdate();
    }

    return list;
  }

  /**
   * 获取待发送MQ消息的列表.
   * 
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<PubPsnRol> getNeedSendAssgin(int batchSize) {

    List<PubPsnRol> list =
        super.createQuery("select t from PubPsnRol t  where  t.isSend = 0 and t.confirmResult = 0 and t.psnLogin = 1 ")
            .setMaxResults(batchSize).list();
    if (list != null && list.size() > 0) {
      return list;
    }
    return null;
  }

  /**
   * 更新用户成果登录状态.
   * 
   * @param psnId
   */
  public void setPsnLogin(Long psnId) {

    String hql = "update PubPsnRol t set t.psnLogin = 1 where  t.psnId = ? and t.psnLogin = 0 ";
    super.createQuery(hql, psnId).executeUpdate();
  }

  /**
   * 获取待发送MQ消息的需删除指派的成果列表.
   * 
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<PubPsnRol> getNeedSendDisAssgin(int batchSize) {

    List<PubPsnRol> list =
        super.createQuery("from PubPsnRol t  where  t.confirmResult = 9 ").setMaxResults(batchSize).list();
    if (list != null && list.size() > 0) {
      return list;
    }
    return null;
  }

  /**
   * 更改MQ消息发送状态.
   * 
   * @param assignId
   * @param isSend
   */
  public void updateSendStatus(Long assignId, boolean isSend) {
    Integer send = 0;
    if (isSend) {
      send = 1;
    }
    String hql = "update PubPsnRol t set t.isSend = ? where t.id = ? ";
    super.createQuery(hql, send, assignId).executeUpdate();
  }

  /**
   * 更改MQ消息发送状态.
   * 
   * @param assignId
   * @param isSend
   */
  public void updateSendErrorStatus(Long assignId) {
    String hql = "update PubPsnRol t set t.isSend = 9 where t.id = ? ";
    super.createQuery(hql, assignId).executeUpdate();
  }

  /**
   * 获取部门关联的成果ID.
   * 
   * @param unitId
   * @param insId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Long> getAllStatusUnitPubId(Long unitId, Long insId) {

    String hql = "select pubId from PubPsnRol t where (t.unitId = ? or t.parentUnitId = ?) and insId  = ? ";
    return super.createQuery(hql, unitId, unitId, insId).list();
  }

  /**
   * 获取人员关联的成果ID.
   * 
   * @param psnId
   * @param insId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Long> getAllStatusPsnPubId(Long psnId, Long insId) {
    String hql = "select pubId from PubPsnRol t where psnId = ?  and insId  = ? ";
    return super.createQuery(hql, psnId, insId).list();
  }

  /**
   * 获取等待确认成果的单位人员数.
   * 
   * @param insId
   * @param unitId
   * @return
   */
  public Long queryNeedConfirmPsnNum(Long insId, Long unitId) {

    List<Object> param = new ArrayList<Object>();
    String hql =
        "select count(distinct t.psnId) from PubPsnRol t,RolPsnIns t1,PublicationRol t2 where t1.pk.psnId = t.psnId and t.pubId = t2.id and t2.status = 2 and t1.status = 1 and t1.pk.insId = ? and t.confirmResult = 0 and t.insId = ? and t1.cyFlag = 1 ";
    param.add(insId);
    param.add(insId);
    if (unitId != null && unitId != 0) {
      hql += " and (t.unitId = ? or t.parentUnitId = ? ) ";
      param.add(unitId);
      param.add(unitId);
    }
    return super.findUnique(hql, param.toArray());
  }

  /**
   * 获取人员需要确认的成果数.
   * 
   * @param insId
   * @param psnId
   * @return
   */
  public Long queryNeedConfirmNum(Long insId, Long psnId) {
    String hql = "select count(t.id) from PubPsnRol t where t.confirmResult = 0 and t.psnId = ? and t.insId = ? ";
    return super.findUnique(hql, psnId, insId);
  }

  /**
   * 获取人员需要确认的一条成果(指派分数最高的记录).
   * 
   * @param insId
   * @param psnId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Object[]> queryNeedConfirmPub(Long insId, Long psnId) {

    String hql =
        "select pubId from PubPsnRol t where t.confirmResult = 0 and t.psnId = ? and t.insId = ? order by score desc";
    List<Long> pubIds = super.createQuery(hql, psnId, insId).setMaxResults(1).list();
    if (CollectionUtils.isEmpty(pubIds)) {
      return null;
    }
    Long pubId = pubIds.get(0);
    String pubHql =
        "select t.id,sourceDbId,t.zhTitle,t.enTitle,authorNames,briefDesc,briefDescEn from PublicationRol t where t.id = :id  ";
    // 执行查询.
    Query query = super.createQuery(pubHql).setParameter("id", pubId);
    return query.list();
  }

  /**
   * 获取匹配到PM的人员ID列表.
   * 
   * @param pmId
   * @param exclude
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Long> queryPmPsnId(Long pmId, Long pubId, int size, List<Long> exclude) {

    // 排除离职人员
    String hql =
        "select t.psnId from PubPsnRol t where t.confirmResult in(0,1) and not exists(select t1.pk.psnId from RolPsnIns t1 where t1.pk.psnId = t.psnId and t1.pk.insId = t.insId and t1.isIns = 1) and (t.authorPMId = :pmId or t.authorPMId is null) ";
    if (exclude != null && exclude.size() > 0) {
      hql += " and t.psnId not in(:exclude) ";
    }
    hql += " and t.pubId = :pubId ";
    Query query = super.createQuery(hql).setParameter("pmId", pmId).setParameter("pubId", pubId);
    if (exclude != null && exclude.size() > 0) {
      query.setParameterList("exclude", exclude);
    }
    return query.setMaxResults(size).list();
  }

  /**
   * 查询成果作者关联个数.
   * 
   * @param pubIds
   * @param insId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Object[]> getPubPsnNum(List<Long> pubIds, Long insId) {

    String hql =
        "select t.pubId,count(t.id) from PubPsnRol t where t.confirmResult in(0,1) and t.pubId in(:pubIds) and t.insId = :insId group by t.pubId ";
    return super.createQuery(hql).setParameter("insId", insId).setParameterList("pubIds", pubIds).list();
  }

  /**
   * 过滤关联到了人员的成果ID列表.
   * 
   * @param pubIds
   * @param insId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Long> filterLinkPubPsn(List<Long> pubIds, Long insId) {

    String hql =
        "select t1.id from PublicationRol t1 where exists (select t.pubId from PubPsnRol t where t.confirmResult in(0,1) and t.pubId = t1.id ) and t1.insId = :insId and t1.id in(:pubIds) ";
    return super.createQuery(hql).setParameter("insId", insId).setParameterList("pubIds", pubIds).list();
  }

  /**
   * 过滤关联到了人员的成果ID列表.
   * 
   * @param pubIds
   * @param insId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Long> filterLinkPmPsn(List<Long> pmIds) {

    String hql =
        "select t1.id from PubMemberRol t1 where exists (select t.pubId from PubPsnRol t where t.confirmResult in(0,1) and t.authorPMId = t1.id ) and  t1.id in(:pmIds) ";
    Collection<Collection<Long>> container = ServiceUtil.splitList(pmIds, 80);
    List<Long> listResult = new ArrayList<Long>();
    for (Collection<Long> item : container) {
      listResult.addAll(super.createQuery(hql).setParameterList("pmIds", item).list());
    }
    return listResult;
  }

  /**
   * 查询人员关联成果个数.
   * 
   * @param psnIds
   * @param insId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Object[]> getPsnPubNum(List<Long> psnIds, Long insId) {

    String hql =
        "select t.psnId,count(t.id) from PubPsnRol t where t.confirmResult in(0,1) and t.psnId in(:psnIds) and t.insId = :insId group by t.psnId ";
    return super.createQuery(hql).setParameter("insId", insId).setParameterList("psnIds", psnIds).list();
  }

  /**
   * 查找成果、人员关联数据.
   * 
   * @param pubIds
   * @param psnId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<PubPsnRol> getPubPsn(List<Long> pubIds, Long psnId) {

    String hql = "from PubPsnRol t where t.confirmResult in(0,1) and t.pubId in(:pubIds) and t.psnId = :psnId ";
    return super.createQuery(hql).setParameterList("pubIds", pubIds).setParameter("psnId", psnId).list();
  }

  /**
   * 判断成果是否与其他部门人员关联.
   * 
   * @param insId
   * @param unitId
   * @return
   */
  public boolean isPubMatchOtherUnit(Long pubId, Long insId, Long unitId) {

    String hql =
        "select count(id) from PubPsnRol t where t.confirmResult in(0,1) and t.pubId =? and t.insId = ? and (t.unitId = ?  or t.parentUnitId = ? )";
    Long count = super.findUnique(hql, pubId, insId, unitId, unitId);
    return count > 0 ? true : false;
  }

  @SuppressWarnings("unchecked")
  public List<PubPsnRol> queryPubPsnRolByPsnId(Long psnId) throws DaoException {
    return super.createQuery("from PubPsnRol where psnId = ?", psnId).list();
  }

  /**
   * 通过成果ID查找相关的指派人员.
   * 
   * @param psnId
   * @return
   * @throws DaoException
   */
  public List<PubPsnRol> queryUnCfmPsnByPubIds(Collection<Long> pubIds) {

    // 对于成果指派人数多于5个的作者不推荐，避免通用姓名的人员推荐
    String hql = "from PubPsnRol t where pubId in(:pubIds) and confirmResult = 0 "
        + " and t.authorPMId not in(select t1.authorPMId from PubPsnRol t1 where t.pubId = t1.pubId group by t1.authorPMId having count(t1.authorPMId) > 5) "
        + " and not exists(select t1.id from PubPsnRol t1 where t.pubId = t1.pubId and t1.authorPMId = t.authorPMId and t1.confirmResult = 1) "
        + " order by score desc ";
    Collection<Collection<Long>> container = ServiceUtil.splitList(pubIds, 10);
    List<PubPsnRol> listResult = new ArrayList<PubPsnRol>();
    for (Collection<Long> item : container) {
      listResult.addAll(super.createQuery(hql).setParameterList("pubIds", item).setMaxResults(100).list());
      if (listResult.size() >= 100) {
        break;
      }
    }
    return listResult;
  }

  /**
   * 成果确认标记重新发送.
   * 
   * @param pubId
   */
  public void updatePubConfirmReSend(Long pubId) {

    String hql = "update PubPsnRol t set t.isSend = 0 where t.pubId = ? and confirmResult = 0 and t.isSend = 1 ";
    super.createQuery(hql, pubId).executeUpdate();
  }

  /**
   * 根据unitId统计人数
   * 
   * @param insId
   * @return
   */
  public Long countAllByUnitId(Long insId, Long unitId, Integer cyFlag) {
    Map<String, Object> params = new HashMap<String, Object>();
    String hql = "select count(distinct t.psnId) from PubPsnRol t,RolPsnIns t2 where t.confirmResult = 0"
        + " and t.insId = :insId and t2.pk.psnId = t.psnId and t2.pk.insId = t.insId and t2.status = 1 and t2.isIns !=1";
    params.put("insId", insId);
    if (unitId != null && unitId != 0) {
      hql += " and (t.unitId = :unitId or t.parentUnitId = :unitId)";
      params.put("unitId", unitId);
    }
    if (cyFlag != null) {
      hql += " and t2.cyFlag = :cyFlag";
      params.put("cyFlag", cyFlag);
    }
    return (Long) this.createQuery(hql, params).uniqueResult();
  }

  /**
   * 根据unitId统计人数
   * 
   * @param insId
   * @return
   */
  public List<Map<String, Long>> countByUnitId(Long insId, Integer cyFlag) {
    Map<String, Object> params = new HashMap<String, Object>();
    String hql =
        "select t.unitId as unitId,count(distinct t.psnId) as count from PubPsnRol t,RolPsnIns t2 where t.confirmResult = 0"
            + " and t.insId = :insId and t2.pk.psnId = t.psnId and t2.pk.insId = t.insId and t2.status = 1 and t.unitId is not null and t2.isIns !=1";
    params.put("insId", insId);
    if (cyFlag != null) {
      hql += " and t2.cyFlag = :cyFlag";
      params.put("cyFlag", cyFlag);
    }
    hql += " group by t.unitId";
    return this.createQuery(hql, params).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
  }

  /**
   * 根据parentUnitId统计人数
   * 
   * @param insId
   * @return
   */
  public List<Map<String, Long>> countByParentUnitId(Long insId, Integer cyFlag) {
    Map<String, Object> params = new HashMap<String, Object>();
    String hql =
        "select t.parentUnitId as parentUnitId,count(distinct t.psnId) as count from PubPsnRol t,RolPsnIns t2 where t.confirmResult = 0"
            + " and t.insId = :insId and t2.pk.psnId = t.psnId and t2.pk.insId = t.insId and t2.status = 1 and t.parentUnitId is not null and t2.isIns !=1";
    params.put("insId", insId);
    if (cyFlag != null) {
      hql += " and t2.cyFlag = :cyFlag";
      params.put("cyFlag", cyFlag);
    }
    hql += " group by t.parentUnitId";
    return this.createQuery(hql, params).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
  }

  /**
   * 左侧统计列表，顶层其他的总数
   * 
   * @param insId
   * @param cyFlag
   * @return
   */
  public Long countOtherPsn(Long insId, Integer cyFlag) {
    Map<String, Object> params = new HashMap<String, Object>();
    String hql =
        "select count(distinct t.psnId) from PubPsnRol t,RolPsnIns t2 where t.confirmResult = 0 and t2.isIns !=1"
            + " and t.insId = :insId and t2.pk.psnId = t.psnId and t2.pk.insId = t.insId and t2.status = 1 and t.parentUnitId is null and t.unitId is null";
    params.put("insId", insId);
    if (cyFlag != null) {
      hql += " and t2.cyFlag = :cyFlag";
      params.put("cyFlag", cyFlag);
    }
    return (Long) this.createQuery(hql, params).uniqueResult();
  }
}
