package com.smate.center.batch.dao.rol.pub;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.smate.center.batch.constant.PublicationRolStatusEnum;
import com.smate.center.batch.exception.pub.DaoException;
import com.smate.center.batch.model.rol.pub.PubErrorFieldRol;
import com.smate.center.batch.model.rol.pub.PublicationRol;
import com.smate.core.base.utils.data.RolHibernateDao;
import com.smate.core.base.utils.string.ServiceUtil;

/**
 * 单位成果Dao:新增、修改、删除、获取详情.
 * 
 * @author yamingd
 * 
 */
@Repository
public class PublicationRolDao extends RolHibernateDao<PublicationRol, Long> {

  @Autowired
  private InsUnitDao insUnitDao;

  /***
   * 获取成果标题，作者信息，用于成果相关邮件 zk add.
   */
  @SuppressWarnings({"unchecked", "rawtypes"})
  public List<PublicationRol> getPublicationRol(List pubIds) throws DaoException {

    String hql =
        "select new PublicationRol(id,zhTitle,enTitle,authorNames,fulltextFileid,insId,briefDesc) from PublicationRol where id in (:ids)";
    return super.createQuery(hql).setParameterList("ids", pubIds).list();
  }

  /**
   * 删除成果：设置status=3.
   * 
   * @param pubRol PublicationRol
   * @throws DaoException
   */
  public void deletePublication(PublicationRol pubRol) throws DaoException {

    pubRol.setDupGroupId(null);
    pubRol.setStatus(3);
    super.save(pubRol);
  }

  /**
   * 更新组，如果组中的成果个数小于2，则将该组删除.
   * 
   * @param groupId
   * @throws DaoException
   */
  public void updateDupGroup(Long groupId) throws DaoException {

    Long count = super.findUnique("select count(t.id) from PublicationRol t where t.dupGroupId = ? ", groupId);
    if (count < 2) {
      super.createQuery("update PublicationRol t set t.dupGroupId = null where t.dupGroupId = ?", groupId)
          .executeUpdate();
    }
  }

  /**
   * 更新成果状态.
   * 
   * @param pubId
   * @param status
   * @throws DaoException
   */
  public void updateStatus(Long pubId, Integer status) throws DaoException {
    // 批准成果，说明成果已经有人确认了
    if (PublicationRolStatusEnum.APPROVED == status) {
      super.createQuery("update PublicationRol t set t.status = ?,t.confirm = 1 where t.id = ?", status, pubId)
          .executeUpdate();
    } else {
      super.createQuery("update PublicationRol t set t.status = ? where t.id = ?", status, pubId).executeUpdate();
    }

  }

  /**
   * 保存错误字段.
   * 
   * @param errorField
   * @throws DaoException
   */
  public void saveErrorField(PubErrorFieldRol errorField) throws DaoException {

    super.getSession().save(errorField);
  }

  /**
   * 保存错误字段.
   * 
   * @param pubId
   * @throws DaoException
   */
  public void deleteErrorFields(long pubId) throws DaoException {

    super.createQuery("delete from PubErrorFieldRol t where t.pubId = ?", new Object[] {pubId}).executeUpdate();
  }

  /**
   * 读取成果错误字段列表.
   * 
   * @param pubId
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public List<PubErrorFieldRol> getErrorFields(long pubId) throws DaoException {
    return super.createQuery("from PubErrorFieldRol t where t.pubId = ?", new Object[] {pubId}).list();
  }

  /**
   * 
   * @param pubId
   * @param insId
   * @return
   * @throws DaoException
   */
  public PublicationRol getInsApprovedPub(Long pubId, Long insId) throws DaoException {

    return super.findUnique("from PublicationRol t where t.id = ? and t.insId = ? and t.status = ? ", pubId, insId,
        PublicationRolStatusEnum.APPROVED);
  }

  /**
   * 更新成果状态为已完成匹配.
   * 
   * @param pubIds
   * @param insId
   * @throws DaoException
   */
  public void updatePubAuthorState(Long[] pubIds, Long insId) throws DaoException {

    String hql = "update PublicationRol t set t.authorState = 1 where t.id in (:id) and t.insId = :insId ";
    Query query = super.getSession().createQuery(hql);
    query.setParameterList("id", pubIds);
    query.setParameter("insId", insId);
    query.executeUpdate();
  }

  /**
   * 设置成果确认状态.
   * 
   * @param pubIds
   * @param confirmResult
   */
  public void setPubConfirmResult(List<Long> pubIds, Integer confirmResult) {
    String hql = "update PublicationRol t set t.confirm = :confirm where t.id in (:id)";
    Query query = super.getSession().createQuery(hql);
    query.setParameter("confirm", confirmResult);
    query.setParameterList("id", pubIds);
    query.executeUpdate();
  }

  /**
   * 设置成果发布状态.
   * 
   * @param pubIds
   * @param confirmResult
   */
  public void setPubPublish(List<Long> pubIds, Integer openStatus) {
    String hql = "update PublicationRol t set t.isOpen = :isOpen where t.id in (:id)";
    Query query = super.getSession().createQuery(hql);
    query.setParameter("isOpen", openStatus);
    query.setParameterList("id", pubIds);
    query.executeUpdate();
  }

  /**
   * 批量获取publication表数据，数据同步后重构XML用.
   * 
   * @param lastId
   * @param batchSize
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<PublicationRol> getPubByBatchForOld(Long lastId, int batchSize) {

    String sql = "from PublicationRol p where p.id > ? order by id asc";

    return super.createQuery(sql, lastId).setMaxResults(batchSize).list();
  }

  @SuppressWarnings("unchecked")
  public List<PublicationRol> loadRebuildPubId(Long lastId, int batchSize) {

    String sql = "select new PublicationRol(p.id) from PublicationRol p where p.id > ? order by id asc";

    return super.createQuery(sql, lastId).setMaxResults(batchSize).list();
  }

  /**
   * 重构成果引用次数URL获取成果ID.
   * 
   * @param lastId
   * @param batchSize
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<PublicationRol> loadRebuildCiteRecordUrlPubId(Long lastId, int batchSize) {

    String sql =
        "select new PublicationRol(p.id) from PublicationRol p where p.id > ? and p.isiId is not null and p.sourceDbId in(15,16,17) and p.citedTimes > 0 and p.status not in(3) order by id asc";

    return super.createQuery(sql, lastId).setMaxResults(batchSize).list();
  }

  @SuppressWarnings("unchecked")
  public List<PublicationRol> loadRebuildIsiId(Long lastId, int batchSize) {

    String sql =
        "select new PublicationRol(p.id,p.isiId) from PublicationRol p where p.id > ? and p.isiId is not null  order by id asc";

    return super.createQuery(sql, lastId).setMaxResults(batchSize).list();
  }

  /**
   * 批量获取publication表数据，用于重构pub_member数据.
   * 
   * @param lastId
   * @param batchSize
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<PublicationRol> loadRebuildPubMember(Long lastId, int batchSize) {

    String sql =
        "select new PublicationRol(p.id,insId,sourceDbId) from PublicationRol p where p.id > ? and status = 2 order by id asc";

    return super.createQuery(sql, lastId).setMaxResults(batchSize).list();
  }

  /**
   * 批量获取publication表数据，用于重构数据使用.
   * 
   * @param lastId
   * @param batchSize
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<PublicationRol> loadRebuildPub(Long lastId, int batchSize) {
    String sql = "from PublicationRol p where p.id > ? and status = 2 order by id asc";

    return super.createQuery(sql, lastId).setMaxResults(batchSize).list();
  }

  /**
   * 查询不完整KPI统计的成果数量.
   * 
   * @param insId
   * @param unitId
   * @return
   */
  public Long queryUnKpiValid(Long insId, Long unitId) {
    List<Object> param = new ArrayList<Object>();
    String hql = "select count(id) from PublicationRol t where t.status = 2 and t.kpiValid = 0 and t.insId = ? ";
    param.add(insId);
    // 部门管理员录入的成果，也算是这个部门的
    if (unitId != null && unitId > 0) {
      hql +=
          " and (exists(select t1.id from PubPsnRol t1 where  t1.confirmResult in(0,1) and (t1.unitId = ? or t1.parentUnitId = ? ) and t1.insId = ? and t.id = t1.pubId) or exists(select t1.pubId from PubUnitOwner t1 where (t1.unitId = ? or t1.parentUnitId = ?) and t1.insId = ? and t.id = t1.pubId))";
      param.add(unitId);
      param.add(unitId);
      param.add(insId);
      param.add(unitId);
      param.add(unitId);
      param.add(insId);
    }
    return super.findUnique(hql, param.toArray());
  }

  /**
   * 查询需要合并的成果数量.
   * 
   * @param insId
   * @param unitId
   * @return
   */
  public Long queryNeedMergePub(Long insId, Long unitId) {

    List<Object> param = new ArrayList<Object>();

    StringBuilder hql = new StringBuilder();
    hql.append(
        "select count(id) from PublicationRol t where t.status = 2 and t.articleType= 1 and t.dupGroupId is not null and t.insId = ? ");
    param.add(insId);

    // 部门管理员录入的成果，也算是这个部门的
    if (unitId != null && unitId > 0) {
      hql.append(
          " and (exists(select id from PubPsnRol  where  confirmResult in(0,1) and (unitId = ? or parentUnitId = ? ) and insId = ? and pubId =  t.id ) or exists(select pubId from PubUnitOwner  where (unitId = ? or parentUnitId = ?) and insId = ? and pubId = t.id))");
      param.add(unitId);
      param.add(unitId);
      param.add(insId);
      param.add(unitId);
      param.add(unitId);
      param.add(insId);
    }
    // 组成果个数必须大于2
    hql.append(" and exists(");
    hql.append(
        "select t2.dupGroupId from PublicationRol t2 where t2.status = 2 and t2.articleType= 1 and t2.dupGroupId is not null and t2.insId = ? ");
    param.add(insId);
    if (unitId != null && unitId > 0) {
      hql.append(
          " and (exists(select id from PubPsnRol  where  confirmResult in(0,1) and (unitId = ? or parentUnitId = ? ) and insId = ? and pubId =  t2.id ) or exists(select pubId from PubUnitOwner  where (unitId = ? or parentUnitId = ?) and insId = ? and pubId = t2.id))");
      param.add(unitId);
      param.add(unitId);
      param.add(insId);
      param.add(unitId);
      param.add(unitId);
      param.add(insId);
    }
    hql.append(" and t2.dupGroupId = t.dupGroupId group by t2.dupGroupId having count(t2.dupGroupId) > 1");
    hql.append(")");
    return super.findUnique(hql.toString(), param.toArray());
  }

  /**
   * 标记成果不重复.
   * 
   * @param groupId
   * @param insId
   */
  public void markPubNoDup(List<Long> pubIds, Long insId) {

    String hql = "update PublicationRol t set t.dupGroupId = null where t.id in(:ids) and t.insId = :insId ";
    super.createQuery(hql).setParameterList("ids", pubIds).setParameter("insId", insId).executeUpdate();
  }

  /**
   * 批量获取单位成果.
   * 
   * @param pubIds
   * @param insId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<PublicationRol> getByIds(List<Long> pubIds, Long insId) {
    String hql = "from PublicationRol t where t.id in(:ids) and t.insId = :insId";
    return super.createQuery(hql).setParameterList("ids", pubIds).setParameter("insId", insId).list();
  }

  /**
   * 过滤出不是删除状态的成果ID.
   * 
   * @param pubIds
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Object[]> getFilterNotDelPubId(Collection<Long> pubIds) {

    String hql =
        "select t.id,t.sourceDbId from PublicationRol t where t.id in(:ids) and (t.status = 2 or t.status = 4)";
    Collection<Collection<Long>> container = ServiceUtil.splitList(pubIds, 90);
    List<Object[]> listResult = new ArrayList<Object[]>();
    for (Collection<Long> item : container) {
      listResult.addAll(super.createQuery(hql).setParameterList("ids", item).list());
    }
    return listResult;
  }

  /**
   * 获取成果状态.
   * 
   * @param id
   * @return
   */
  public Integer getPubStatusById(Long id) {

    String hql = "select status from PublicationRol t where t.id  = ? ";
    return super.findUnique(hql, id);
  }

  public List findPubIdsBatch(Long lastId, int batchSize) throws DaoException {
    String sql =
        "select t.pub_id from task_pub_ids t where t.pub_id > ? and t.status != 1 and rownum <= ? order by t.pub_id asc";
    return super.queryForList(sql, new Object[] {lastId, batchSize});
  }

  public void updatePubBriefDescTaskStatus(Long pubId, int status) throws DaoException {
    String sql = "update task_pub_ids t set t.status = ? where t.pub_id= ?";
    super.update(sql, new Object[] {status, pubId});
  }

  /**
   * 批量获取系统成果ID.
   * 
   * @param lastId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Long> findAllPubIdsBatch(Long lastId, int size) {

    String hql = "select id from PublicationRol t where t.id > ?  and t.status = 2 order by id asc";
    return super.createQuery(hql, lastId).setMaxResults(size).list();
  }

  /**
   * 通过期刊id取得期刊成果.
   * 
   * @param jid
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public List<PublicationRol> queryPublicationByJid(Long jid) throws DaoException {
    String hql = "from PublicationRol t where t.jid=?";
    return super.createQuery(hql, jid).list();
  }

  /**
   * 通过期刊Id分页查询期刊成果.
   * 
   * @param jid
   * @param pubId
   * @param maxResult
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public List<PublicationRol> queryPublicationByJid(Long jid, Long pubId, int maxResult) throws DaoException {
    Query query = super.createQuery("select new PublicationRol(t.id, t.insId, t.sourceDbId) "
        + "from PublicationRol t where t.id > :pubId and t.jid = :jid order by t.id asc").setParameter("pubId", pubId)
            .setParameter("jid", jid).setMaxResults(maxResult);

    return query.list();
  }

  public List<PublicationRol> queryPublicationByPsnId(Long psnId) throws DaoException {
    return super.find("from PublicationRol t where t.createPsnId=?", psnId);
  }

  public List<PublicationRol> queryPubByUpPsnId(Long updatePsnId) throws DaoException {
    return super.find("from PublicationRol t where t.updatePsnId=?", updatePsnId);
  }

  /**
   * 查询系的成果数.
   * 
   * @param unitId
   * @param insId
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public List<Map<String, Long>> queryPubCountByUnitId(List<Long> unitIdList, Long insId) throws DaoException {
    StringBuilder hql = new StringBuilder();
    hql.append("select t1.unitId as unitId, count(distinct t1.pubId) as pubCount")
        .append(" from PubPsnRol t1, PublicationRol t2")
        .append(
            " where t2.status = 2 and t2.articleType = 1 and t2.insId = :insId and t1.confirmResult in(0, 1) and t1.unitId in(:unitIdList) and t2.id = t1.pubId")
        .append(" group by t1.unitId");

    Query query = super.createQuery(hql.toString()).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
        .setParameter("insId", insId);
    int startIndex = 0;
    int toIndex = unitIdList.size() >= 100 ? 99 : unitIdList.size();
    List<Map<String, Long>> tmpMapList = new ArrayList<Map<String, Long>>();
    while (startIndex < unitIdList.size()) {
      tmpMapList.addAll(query.setParameterList("unitIdList", unitIdList.subList(startIndex, toIndex)).list());
      startIndex = toIndex + 1;
      toIndex = (toIndex + 100) > unitIdList.size() ? unitIdList.size() : (toIndex + 100);
    }

    return tmpMapList;
  }

  /**
   * 查询院下全部系的成果数.
   * 
   * @param superUnitId
   * @param insId
   * @return
   * @throws DaoException
   */
  public Long queryPubCountBySuperUnitId(Long superUnitId, Long insId) throws DaoException {
    StringBuilder hql = new StringBuilder();
    hql.append(" select count(distinct t1.pubId) from PubPsnRol t1, PublicationRol t2  ").append(
        " where t2.status = 2 and t2.articleType = 1 and t2.insId = :insId and t1.confirmResult in(0, 1) and t1.parentUnitId = :superUnitId and t2.id = t1.pubId");

    Long count = (Long) super.createQuery(hql.toString()).setParameter("superUnitId", superUnitId)
        .setParameter("insId", insId).uniqueResult();

    return count;
  }

  /**
   * 查询系的成果数.
   * 
   * @param unitId
   * @param insId
   * @return
   * @throws DaoException
   */
  public Long queryPubCountByUnitId(Long unitId, Long insId) throws DaoException {
    StringBuilder hql = new StringBuilder();
    hql.append(
        "select count(distinct t.id) from PublicationRol t  where t.insId = ? and t.articleType=1 and t.status = 2   and (  exists (select pubId from PubPsnRol where confirmResult in(0,1) and (unitId = ? or parentUnitId=?) and insId = ? and pubId = t.id )");
    boolean isSecondUnit = insUnitDao.isSecondUnit(insId, unitId);
    if (isSecondUnit) {// rol-2321 二级部门
      // hql.append(" or exists(select pubId from PubUnitOwner where
      // (unitId = ? or parentUnitId=?) and insId = ? and pubId = t.id)
      // ");
      hql.append(" )");
    } else {
      // hql.append(" or exists(select pubId from PubUnitOwner where
      // (unitId = ? or parentUnitId=?) and insId = ? and pubId = t.id)
      // ");
      hql.append(" )");
    }
    Long count = (Long) super.createQuery(hql.toString(), new Object[] {insId, unitId, unitId, insId}).uniqueResult();
    return count;
  }

  /**
   * 查询全部的成果数.
   * 
   * @param unitId
   * @param insId
   * @return
   */
  public Long queryPubCountAllByUnitId(Long unitId, Long insId) {
    StringBuilder hql = new StringBuilder();
    Map<String, Object> params = new HashMap<String, Object>();
    if (unitId != null && unitId != 0) {
      hql.append("select count(distinct t1.id) from PublicationRol t1");
      hql.append(" where (exists (select pubId from PubPsnRol where confirmResult in(0,1)")
          .append(" and (unitId = :unitId or parentUnitId=:unitId) and insId = :insId and pubId = t1.id)")
          // 本部门管理员/秘书录入的成果，也算是这个部门的
          .append(
              " or exists(select pubId from PubUnitOwner where (unitId = :unitId or parentUnitId=:unitId) and insId = :insId and pubId = t1.id))")
          .append(" and t1.status = 2 and t1.articleType = 1 and t1.insId = :insId");
      params.put("insId", insId);
      params.put("unitId", unitId);
    } else {
      hql.append("select count(distinct t2.id) from PublicationRol t2")
          .append(" where t2.status = 2 and t2.articleType = 1 and t2.insId = :insId");
      params.put("insId", insId);
    }
    Long count = (Long) super.createQuery(hql.toString(), params).uniqueResult();
    return count;
  }

  /**
   * 统计其他成果数
   * 
   * @param insId
   * @param parentUnitId
   * @return
   */
  public Long queryPubOtherCount(Long insId, Long parentUnitId) {
    StringBuilder hql = new StringBuilder();
    Map<String, Object> params = new HashMap<String, Object>();
    if (parentUnitId == null || parentUnitId == 0) {// rol-2767
      hql.append(
          "select count(distinct t.id) from PublicationRol t  where t.insId = :insId and t.articleType=1 and t.status = 2 and (  exists (select pubId from PubPsnRol where confirmResult in(0,1) and (unitId is null and parentUnitId is null) and insId = :insId and pubId = t.id )");
      params.put("insId", insId);
      hql.append(")");
    } else {// rol-2321
      hql.append(
          "select count(distinct t.id) from PublicationRol t  where t.insId = :insId and t.articleType=1 and t.status = 2 and (  exists (select pubId from PubPsnRol where confirmResult in(0,1) and (unitId = :parentUnitId and parentUnitId is null) and insId = :insId and pubId = t.id )");
      params.put("insId", insId);
      params.put("parentUnitId", parentUnitId);
      hql.append(")");
    }
    return (Long) super.createQuery(hql.toString(), params).uniqueResult();
  }

  /**
   * 查询单位人员成果数.
   * 
   * @param psnIdList
   * @param insId
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public List<Map<String, Long>> queryPubCountByPsnId(List<Long> psnIdList, Long insId) throws DaoException {
    StringBuilder hql = new StringBuilder();
    hql.append("select t2.psnId as psnId, count(distinct t1.id) as pubCount from PublicationRol t1, PubPsnRol t2")
        .append(
            " where t1.status = 2 and t1.articleType = 1 and t1.insId = :insId and t2.confirmResult in(0, 1) and t2.psnId in(:psnIdList) and t2.pubId = t1.id")
        .append(" group by t2.psnId");
    Query query = super.createQuery(hql.toString()).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
        .setParameter("insId", insId);
    int startIndex = 0;
    int toIndex = psnIdList.size() >= 100 ? 99 : psnIdList.size();
    List<Map<String, Long>> tmpMapList = new ArrayList<Map<String, Long>>();
    while (startIndex < psnIdList.size()) {
      tmpMapList.addAll(query.setParameterList("psnIdList", psnIdList.subList(startIndex, toIndex)).list());
      startIndex = toIndex + 1;
      toIndex = (toIndex + 100) > psnIdList.size() ? psnIdList.size() : (toIndex + 100);
    }

    return tmpMapList;
  }

  /**
   * 单位的成果统计
   * 
   * @param insId
   * @return
   */
  public Long getInsPubTotalNum(Long insId) {
    String hql = "select count(id) from PublicationRol where insId = ? ";
    return super.findUnique(hql, insId);
  }

  /**
   * 单位的专利统计
   * 
   * @param insId
   * @return
   */
  public Long getInsPatTotalNum(Long insId) {
    String hql = "select count(id) from PublicationRol where insId = ? and  typeId=5 ";
    return super.findUnique(hql, insId);
  }

  public List<PublicationRol> queryPubByIds(List<Long> pubIds, Integer articleType) {
    String hql =
        "from PublicationRol t where t.articleType = :articleType and id in (:ids) order by t.publishYear desc,t.publishMonth desc,t.publishDay desc";
    return super.createQuery(hql).setParameter("articleType", articleType).setParameterList("ids", pubIds).list();
  }

  @SuppressWarnings("unchecked")
  public List<PublicationRol> getPublicationList(int batchSize, Long lastPubId) {
    String hql =
        "select new PublicationRol(t.id,t.insId,t.createPsnId) from PublicationRol t where t.status in (2,4) and t.createPsnId = 2  and t.id > :lastPubId order by t.id  desc ";
    return super.createQuery(hql).setParameter("lastPubId", lastPubId).setMaxResults(batchSize).list();
  }

}
