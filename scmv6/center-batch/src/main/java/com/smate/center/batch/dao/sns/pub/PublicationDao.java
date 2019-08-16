package com.smate.center.batch.dao.sns.pub;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.hibernate.Query;
import org.hibernate.criterion.CriteriaSpecification;
import org.springframework.stereotype.Repository;

import com.smate.center.batch.exception.pub.DaoException;
import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.sns.pub.PubErrorFields;
import com.smate.center.batch.model.sns.pub.PubFolderItems;
import com.smate.center.batch.model.sns.pub.PubIns;
import com.smate.center.batch.model.sns.pub.PubKnow;
import com.smate.center.batch.model.sns.pub.PubMember;
import com.smate.center.batch.model.sns.pub.Publication;
import com.smate.center.batch.model.sns.pub.PublicationList;
import com.smate.center.batch.service.pub.ConstPdwhPubRefDb;
import com.smate.core.base.psn.consts.PsnCnfConst;
import com.smate.core.base.utils.constant.ServiceConstants;
import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.core.base.utils.model.Page;
import com.smate.core.base.utils.string.ServiceUtil;

/**
 * 成果、文献DAO.
 * 
 * @author liqinghua
 * 
 */
@Repository
public class PublicationDao extends SnsHibernateDao<Publication, Long> {

  /**
   * 保存成果、文献与单位的关系信息.
   * 
   * @param pubIns
   * @throws DaoException
   */
  public void savePubIns(PubIns pubIns) throws DaoException {

    super.getSession().save(pubIns);
  }

  /**
   * 获取成果所有人.
   * 
   * @param pubId
   * @return
   */
  public Long getPubOwner(Long pubId) {

    return super.findUnique("select psnId from Publication t where t.id = ? ", pubId);
  }

  /**
   * 获取成果单位关系列表.
   * 
   * @param pubId 成果ID
   * @return List<PubIns>
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public List<PubIns> getPubInsList(Long pubId) throws DaoException {
    String hql = "from PubIns t where t.id.pubId = ? ";
    return super.createQuery(hql, new Object[] {pubId}).list();
  }

  public Publication getPubOwnerPsnIdOrStatus(Long pubId) {
    String hql = "select new Publication(id,psnId,status) from Publication where id=? and rownum=1 ";
    Object obj = super.createQuery(hql, pubId).uniqueResult();
    if (obj != null) {
      return (Publication) obj;
    }
    return null;
  }

  /**
   * 更新单位人员成果状态.
   * 
   * @param psnId
   * @param insId
   * @param status
   * @throws DaoException
   */
  public void updatePubInsPubStatus(Long[] pubIds, Long insId, Integer status) throws DaoException {

    String hql = "update PubIns set pubStatus = :pubStatus where id.pubId in (:pubId) and id.insId = :insId ";
    super.getSession().createQuery(hql).setParameter("pubStatus", status).setParameterList("pubId", pubIds)
        .setParameter("insId", insId).executeUpdate();
  }

  /**
   * 更新单位人员成果状态.
   * 
   * @param pubId
   * @param status
   * @throws DaoException
   */
  public void updatePubInsPubStatus(Long pubId, Integer status) throws DaoException {

    String hql = "update PubIns set pubStatus = :pubStatus where id.pubId = :pubId ";
    super.getSession().createQuery(hql).setParameter("pubStatus", status).setParameter("pubId", pubId).executeUpdate();
  }

  /**
   * 获取成果、文献与单位的关系信息.
   * 
   * @param pubId
   * @param insId
   * @return
   * @throws DaoException
   */
  public PubIns getPubInsByPubInsId(Long pubId, Long insId) throws DaoException {

    return (PubIns) super.findUnique("from PubIns t where t.id.pubId = ? and t.id.insId = ?  ",
        new Object[] {pubId, insId});
  }

  /**
   * 删除成果、文献与单位的关系信息.
   * 
   * @param pubId
   * @param insId
   * @throws DaoException
   */
  public void removePubIns(Long pubId, Long insId) throws DaoException {

    PubIns pubIns = getPubInsByPubInsId(pubId, insId);
    super.getSession().delete(pubIns);
  }

  /**
   * 删除成果、文献与单位的关系信息.
   * 
   * @param pubIns
   * @throws DaoException
   */
  public void removePubIns(PubIns pubIns) throws DaoException {

    super.getSession().delete(pubIns);
  }

  /**
   * 获取成果/人员关系信息.
   * 
   * @param id
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public List<PubMember> getPubMembersByPubId(Long pubId) throws DaoException {
    try {
      Query query = super.createQuery("from PubMember t where t.pubId= ? order by seqNo", new Object[] {pubId});
      List list = query.list();
      return list;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * 获取成果/人员关系信息.
   * 
   * @param id
   * @return
   * @throws DaoException
   */
  public PubMember getPubMemberById(Long id) throws DaoException {

    return (PubMember) super.findUnique("from PubMember t where t.id= ? ", new Object[] {id});
  }

  /**
   * 获取成果/人员关系信息.
   * 
   * @param pubId
   * @param pmId
   * @return
   * @throws DaoException
   */
  public PubMember getPubMemeberByPubPmId(Long pubId, Long pmId) throws DaoException {

    return (PubMember) super.findUnique("from PubMember t where t.pubId = ? and t.id= ? ", new Object[] {pubId, pmId});
  }

  /**
   * 删除成果/人员关系信息.
   * 
   * @param id
   * @throws DaoException
   */
  public PubMember removePubMemberById(Long id) throws DaoException {

    PubMember pubMember = this.getPubMemberById(id);
    super.getSession().delete(pubMember);
    return pubMember;
  }

  /**
   * 删除成果/人员保留之外的人员信息.
   * 
   * @param remainPmId
   * @param pubId
   */
  @SuppressWarnings("unchecked")
  public void removeOtherPubmember(List<Long> remainPmId, long pubId) {
    if (remainPmId == null || remainPmId.size() == 0) {
      String hql = "delete from PubMember t where t.pubId = ? ";
      super.createQuery(hql, pubId).executeUpdate();
    } else {
      // 找出删除的PMID
      String hql = "select t.id from PubMember t where t.pubId = ? ";
      List<Long> oldPmIds = super.createQuery(hql, pubId).list();
      List<Long> delPmIds = new ArrayList<Long>();
      outerLoop: for (Long oldPmId : oldPmIds) {
        for (Long rpmId : remainPmId) {
          if (oldPmId.equals(rpmId)) {
            continue outerLoop;
          }
        }
        delPmIds.add(oldPmId);
      }
      // 如果无删除成果ID，退出
      if (CollectionUtils.isEmpty(delPmIds)) {
        return;
      }
      // 拆分80一组，如果参数超过100，SQL报错
      Collection<Collection<Long>> container = ServiceUtil.splitList(delPmIds, 80);
      hql = "delete from PubMember t where t.pubId = :pubId and t.id in(:ids)";
      for (Collection<Long> item : container) {
        super.createQuery(hql).setParameter("pubId", pubId).setParameterList("ids", item).executeUpdate();
      }
    }
  }

  /**
   * 保存成果/人员关系信息.
   * 
   * @param pubMember
   * @throws DaoException
   */
  public void savePubMember(PubMember pubMember) throws DaoException {

    super.getSession().save(pubMember);
  }

  /**
   * 保存成果的错误信息.
   * 
   * @param error
   * @throws DaoException
   */
  public void savePubErrorFields(PubErrorFields error) throws DaoException {

    super.getSession().save(error);
  }

  /**
   * 获取成果错误信息.
   * 
   * @param pubId
   * @return
   * @throws DaoException
   */
  public List<PubErrorFields> getPubErrorFields(Long pubId) throws DaoException {

    String hql = "from PubErrorFields t where t.pubId = ? order by id asc ";
    return super.createQuery(hql, pubId).list();
  }

  /**
   * 删除成果的错误信息.
   * 
   * @param pubId
   * @throws DaoException
   */
  public void removePubErrorFieldsByPubId(Long pubId) throws DaoException {

    super.createQuery("delete from PubErrorFields t where t.pubId = ? ", new Object[] {pubId}).executeUpdate();

  }

  /**
   * 判断成果是否在收藏夹中.
   * 
   * @param pubId
   * @param fdId
   * @return
   * @throws DaoException
   */
  public boolean isPubInFolder(Long pubId, Long fdId) throws DaoException {

    Long count = super.findUnique(
        "select count(t.id) from Publication t  inner join t.pubFolder pf where t.id =? and pf.folder.id = ? ",
        new Object[] {pubId, fdId});
    if (count > 0) {
      return true;
    }
    return false;
  }

  /**
   * 通过成果ID(，逗号分隔)获取成果列表.
   * 
   * @param pubIds
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public List<Publication> getPublicationByPubIds(String pubIds) throws DaoException {

    if (pubIds != null && pubIds.matches(ServiceConstants.IDPATTERN)) {
      return super.createQuery("from Publication t where t.id in (" + pubIds + ")", new Object[] {}).list();
    }
    return null;
  }

  /**
   * 获取指定的成果列表.
   * 
   * @param psnId
   * @param pubIds
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Publication> getPublication(Long psnId, List<Long> pubIds) {

    String hql = "from Publication t where t.psnId = :psnId and t.id in(:pubIds) order by id asc";

    return super.createQuery(hql).setParameter("psnId", psnId).setParameterList("pubIds", pubIds).list();
  }

  /**
   * 保持成果、收藏夹之间的关系.
   * 
   * @param pf
   * @throws DaoException
   */
  public void savePubFolderItems(PubFolderItems pf) throws DaoException {

    super.getSession().save(pf);
  }

  /**
   * 移除成果收藏夹之间的关系.
   * 
   * @param pubIds
   * @param folderId
   * @throws DaoException
   */
  public void removePubFromPubFolder(String pubIds, Long folderId) throws DaoException {

    if (pubIds.matches(ServiceConstants.IDPATTERN)) {
      String hql = "delete from PubFolderItems t where t.pubId in(" + pubIds + ") and t.folderId = ?";
      super.createQuery(hql, folderId).executeUpdate();
    }

  }

  public void updatePublicationType(String pubIds) {
    if (pubIds != null && pubIds.matches(ServiceConstants.IDPATTERN)) {
      super.getSession().createQuery("update Publication t set t.articleType=2 where t.id in (" + pubIds + ")")
          .executeUpdate();
    }
  }

  /**
   * 获取个人成果引用数
   * 
   * @param psnId
   * @return
   */
  public Long getTotalCitedTimesByPsnId(Long psnId) {
    Long count = super.findUnique(
        "select sum(nvl(t.citedTimes,0)) from Publication t where t.psnId=? and t.articleType = 1  and t.status=0",
        new Object[] {psnId});
    return count;
  }

  /**
   * 个人成果的总数.
   * 
   * @param psnId
   * @throws DaoException
   */
  public Long getTotalPubsByPsnId(Long psnId) throws DaoException {
    Long count =
        super.findUnique("select count(t.id) from Publication t where t.psnId=? and t.articleType = 1  and t.status=0",
            new Object[] {psnId});
    return count;
  }

  /*
   * 每个人的成果总数
   */
  public List getTotalPubsByPsnIds(Integer size) throws DaoException {

    String hql =
        "select count(t.id) as count,psnId as psnId from Publication t where  t.articleType = 1  and t.status=0 group by psnId";
    return super.createQuery(hql).setResultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP).setMaxResults(100)
        .setFirstResult(size * 100).list();
  }

  public Long getTotalRefsByPsnId(Long psnId) throws DaoException {
    Long count =
        super.findUnique("select count(t.id) from Publication t where t.psnId=? and t.articleType = 2  and t.status=0",
            new Object[] {psnId});
    return count;
  }

  /**
   * 个人成果的总数（不区分成果来源类型）.
   * 
   * @param psnId
   * @throws DaoException
   */
  public Long getPsnMergeTotalPubs(Long psnId) throws DaoException {
    Long count = super.findUnique(
        "select count(t.id) from Publication t where t.psnId=? and t.articleType = 1  and t.status in(0,2,3,4,5)",
        new Object[] {psnId});
    return count;
  }

  /**
   * 获取上个月的个人成果引用数 zk
   */
  @SuppressWarnings("rawtypes")
  public List getLastMonthCitedTimes(Integer size) throws DaoException {
    // String hql =
    // "select count(p.id) as count,p.psnId as psnId from Publication p
    // where p.createDate>=trunc(last_day(add_months(sysdate,-2))+1)and
    // p.createDate<trunc(last_day(add_months(sysdate,-1))+1) and
    // p.articleType = 1 and p.status =0 group by p.psnId having
    // count(p.psnId)>=1";
    // 最近1周，七天
    String hql =
        "select  sum(nvl(p.citedTimes,0)) as count,p.psnId  as psnId from Publication p where trunc( p.createDate)>=trunc(sysdate-7) and p.articleType = 1  and p.status =0  group by p.psnId having count(p.psnId)>=1";
    return super.createQuery(hql).setResultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP).setMaxResults(100)
        .setFirstResult(size * 100).list();
  }

  /**
   * 获取上个月的个人成果总数 zk
   */
  @SuppressWarnings("rawtypes")
  public List getLastMonthPsnPubs(Integer size) throws DaoException {
    // String hql =
    // "select count(p.id) as count,p.psnId as psnId from Publication p
    // where p.createDate>=trunc(last_day(add_months(sysdate,-2))+1)and
    // p.createDate<trunc(last_day(add_months(sysdate,-1))+1) and
    // p.articleType = 1 and p.status =0 group by p.psnId having
    // count(p.psnId)>=1";
    // 最近一周，七天
    String hql =
        "select  count(p.id) as count,p.psnId  as psnId from Publication p where trunc( p.createDate)>=trunc(sysdate-7) and p.articleType = 1  and p.status =0  group by p.psnId having count(p.psnId)>=1 ";
    return super.createQuery(hql).setResultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP).setMaxResults(100)
        .setFirstResult(size * 100).list();
  }

  /**
   * 获取有成果的人的psnId
   */
  @SuppressWarnings("unchecked")
  public List<Long> getPsnId(Integer size) throws DaoException {
    return super.createQuery("select distinct p.psnId from Publication p where p.articleType = 1  and p.status = 0 ")
        .setMaxResults(100).setFirstResult(size * 100).list();
  }

  /**
   * 个人文献的总数（不区分成果来源类型）.
   * 
   * @param psnId
   * @throws DaoException
   */
  public Long getPsnMergeTotalRefs(Long psnId) throws DaoException {
    Long count = super.findUnique(
        "select count(t.id) from Publication t where t.psnId=? and t.articleType = 2  and t.status in(0,2,3,4,5)",
        new Object[] {psnId});
    return count;
  }

  /**
   * 通过ID获取成果.
   * 
   * @param pubId
   * @return
   * @throws DaoException
   */
  public Publication findPublicationByPubId(Long pubId) throws DaoException {
    String hql = "from Publication t where t.id=?";
    return this.findUnique(hql, pubId);
  }

  /**
   * 更新成的状态.
   * 
   * @param pubId
   * @param status
   */
  public void updatePublicationStatus(Long pubId, Integer status) throws DaoException {
    String hql = "update Publication t set t.status=? where t.id=?";
    this.createQuery(hql, new Object[] {status, pubId}).executeUpdate();
  }

  /**
   * 获取同步数据的新成果ID.
   * 
   * @param oldPubId
   * @param psnId
   * @return
   */
  public Long getPubIdByOldPub(Long oldPubId, Long psnId) {

    String sql = "select id from Publication where oldPubId = ? and psnId = ? ";

    return super.findUnique(sql, oldPubId, psnId);
  }

  /**
   * 获取同步数据的新成果.
   * 
   * @param oldPubId
   * @param psnId
   * @return
   */
  public Publication getPubByOldPub(Long oldPubId, Long psnId) {

    String sql = "from Publication where oldPubId = ? and psnId = ? order by id desc";
    List<Publication> list = super.find(sql, oldPubId, psnId);
    if (list != null && list.size() > 0) {
      return list.get(0);
    }
    return null;
  }

  /**
   * 批量获取publication表数据，数据同步后重构XML用.
   * 
   * @param lastId
   * @param batchSize
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Publication> getPubByBatchForOld(Long lastId, int batchSize) {

    String sql = "from Publication p where p.id > ? order by id asc";

    return super.createQuery(sql, lastId).setMaxResults(batchSize).list();
  }

  /**
   * 批量查询指定期刊的期刊类型成果或文献.
   * 
   * @param jid
   * @param lastId
   * @param batchSize
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public List<Publication> queryJnlPubByJid(Long jid, Long lastId, int batchSize) throws DaoException {

    return super.createQuery(
        "select new Publication(p.id, p.psnId, p.articleType, p.status) from Publication p where p.id > ? and p.status=? and p.typeId = ? and p.jid = ? order by id asc",
        lastId, 0, 4, jid).setMaxResults(batchSize).list();
  }

  /**
   * 批量获取publication表数据，数据同步后重构XML用. source_db_id=4，用于修正source_url错误.
   * 
   * @param lastId
   * @param batchSize
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Publication> getPubByBatchForOldDbId4(Long lastId, int batchSize) {

    String sql = "from Publication p where p.id > ? and sourceDbId=4 order by id asc";

    return super.createQuery(sql, lastId).setMaxResults(batchSize).list();
  }

  @SuppressWarnings("unchecked")
  public List<Publication> loadRebuildPubId(Long lastId, int batchSize) {

    String sql =
        "select new Publication(p.id) from Publication p where p.id > ? and p.status not in(1) and p.articleType = 1 order by id asc";
    return super.createQuery(sql, lastId).setMaxResults(batchSize).list();
  }

  @SuppressWarnings("unchecked")
  public List<Publication> loadRebuildPubPsn(Long lastId, int batchSize) {

    String sql =
        "select new Publication(p.id,p.psnId) from Publication p where p.id > ? and p.status=0 and p.articleType = 1 order by id asc";
    return super.createQuery(sql, lastId).setMaxResults(batchSize).list();
  }

  public int getPubPsnMatchPubAuthors(Long pubId, String zhName, String enName, String otherName) {
    return super.queryForInt(
        "select count(1) from pub_member t where (t.member_name=? or t.member_name=? or t.member_name=?) and t.pub_id=?",
        new Object[] {zhName, enName, otherName, pubId});
  }

  /**
   * 重构成果引用次数URL获取成果ID.
   * 
   * @param lastId
   * @param batchSize
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Publication> loadRebuildCiteRecordUrlPubId(Long lastId, int batchSize) {

    String sql =
        "select new Publication(p.id) from Publication p where p.id > ? and p.isiId is not null and p.sourceDbId in(15,16,17) and p.citedTimes > 0 and p.status not in(1) order by id asc";
    return super.createQuery(sql, lastId).setMaxResults(batchSize).list();
  }

  @SuppressWarnings("unchecked")
  public List<Publication> loadRebuildPubId(Long lastId, Long maxId, int batchSize) {

    String sql = "select new Publication(p.id) from Publication p where status.status = 0 and p.id > ? ";
    if (maxId != null) {
      sql += " and p.id <=  ? ";
    }
    sql += " order by id asc ";
    Query query = super.createQuery(sql).setParameter(0, lastId);
    if (maxId != null) {
      query.setParameter(1, maxId);
    }
    return query.setMaxResults(batchSize).list();
  }

  @SuppressWarnings("unchecked")
  public List<Publication> loadRebuildIsiId(Long lastId, int batchSize) {

    String sql =
        "select new Publication(p.id,p.isiId) from Publication p where p.id > ? and p.isiId is not null order by id asc";

    return super.createQuery(sql, lastId).setMaxResults(batchSize).list();
  }

  @SuppressWarnings("unchecked")
  public List<Publication> impactFactorsSort(Long lastId, int batchSize) {
    String hql = "from Publication p where p.id > ? and p.impactFactors is not null order by p.id asc";
    return super.createQuery(hql, lastId).setMaxResults(batchSize).list();
  }

  @SuppressWarnings("unchecked")
  public List<Publication> briefDesc(Long lastId, int batchSize) {
    String hql = "from Publication p where p.id > ? order by p.id asc";
    return super.createQuery(hql, lastId).setMaxResults(batchSize).list();
  }

  @SuppressWarnings("unchecked")
  public List<PublicationList> getPubListByBatchForSourceDbId2(Long lastId, int batchSize) {
    String hql =
        "select t1 from PublicationList t1,Publication t2 where t1.id=t2.id and (t1.listSci=1 or t1.listIstp=1 or t1.listSsci=1) and t2.sourceDbId=2 and t1.id > ? order by t1.id asc";
    return super.createQuery(hql, lastId).setMaxResults(batchSize).list();
  }

  @SuppressWarnings("unchecked")
  public List<Long> findSamePubByPsn(int pubType, Integer zhTitleHash, Integer enTitleHash, List<Long> psnIds) {
    zhTitleHash = zhTitleHash == null ? 0 : zhTitleHash;
    enTitleHash = enTitleHash == null ? 0 : enTitleHash;
    String hql =
        "select distinct psnId from PubKnow where articleType=:type and (zhTitleHash=:zhHash or enTitleHash=:enHash) and isPubAuthors=1 and psnId not in(:ids) and status in(0,2,3,4,5) and rownum<=9";
    return createQuery(hql).setParameter("type", pubType).setParameter("zhHash", zhTitleHash)
        .setParameter("enHash", enTitleHash).setParameterList("ids", psnIds).list();
  }

  @SuppressWarnings("unchecked")
  public List<Long> findSamePubByPsn(int pubType, Integer zhTitleHash, Integer enTitleHash) {
    zhTitleHash = zhTitleHash == null ? 0 : zhTitleHash;
    enTitleHash = enTitleHash == null ? 0 : enTitleHash;
    String hql =
        "select distinct psnId from PubKnow where articleType=:type and (zhTitleHash=:zhHash or enTitleHash=:enHash) and isPubAuthors=1 and status in(0,2,3,4,5) and rownum<=9";
    return createQuery(hql).setParameter("type", pubType).setParameter("zhHash", zhTitleHash)
        .setParameter("enHash", enTitleHash).list();
  }

  /**
   * 获取文件夹成果，文献统计.
   * 
   * @param pubType
   * @param articleType
   * @param psnId
   * @return
   */
  public int getPubTypeNum(int pubType, int articleType, long psnId) {
    String hql =
        "select count(id) from Publication t where t.status = 0 and t.typeId = ? and articleType=? and t.psnId = ? ";
    Long count = super.findUnique(hql, pubType, articleType, psnId);
    return count.intValue();
  }

  /**
   * 获取引用类别成果,文献统计.
   * 
   * @param listType
   * @param articleType
   * @param psnId
   * @return
   */
  public int getPubListNum(String listType, int articleType, long psnId) {

    StringBuilder hql =
        new StringBuilder("select count(id) from Publication t where t.status = 0 and articleType=? and t.psnId = ? ");

    if ("ei".equalsIgnoreCase(listType)) {
      hql.append(" and t.id in(select l.id from PublicationList l where l.listEi=1)");
    } else if ("sci".equalsIgnoreCase(listType)) {
      hql.append(" and t.id in(select l.id from PublicationList l where l.listSci=1)");

    } else if ("istp".equalsIgnoreCase(listType)) {
      hql.append(" and t.id in(select l.id from PublicationList l where l.listIstp=1)");
    } else if ("ssci".equalsIgnoreCase(listType)) {
      hql.append(" and t.id in(select l.id from PublicationList l where l.listSsci=1)");
    } else {
      return 0;
    }
    Long count = super.findUnique(hql.toString(), articleType, psnId);
    return count.intValue();
  }

  /**
   * 统计未分类成果文件夹.
   * 
   * @param psnId
   * @param articleType
   * @return
   */
  public int getNoFolderPubNum(Long psnId, Integer articleType) {

    String hql =
        "select count(t.id) from Publication t where t.psnId = ? and t.articleType = ?  and status = 0 and t.id not in("
            + "select t2.pubId from PubFolderItems t2 join t2.pubFolder t3  where t3.psnId = ? and t3.articleType = ?)";
    Long count = super.findUnique(hql, psnId, articleType, psnId, articleType);
    return count.intValue();
  }

  /**
   * 获取具体文件夹的成果统计数.
   * 
   * @param folderId
   * @return
   */
  public int getFolderPubNum(Long folderId) {

    String hql = "select count(t.id) from PubFolderItems t where t.folderId = ? ";
    Long count = super.findUnique(hql, folderId);
    return count.intValue();
  }

  /**
   * 获取具体年份成果、文献统计数据.
   * 
   * @param year
   * @param articleType
   * @param psnId
   * @return
   */
  public int getPubYearNum(Integer year, Integer articleType, Long psnId) {
    String hql =
        "select count(id) from Publication t where t.status = 0 and t.publishYear = ? and articleType=? and t.psnId = ? ";
    Long count = super.findUnique(hql, year, articleType, psnId);
    return count.intValue();
  }

  /**
   * 获取未归类年份成果、文献统计数据.
   * 
   * @param year
   * @param articleType
   * @param psnId
   * @return
   */
  public int getNoPubYearNum(Integer articleType, Long psnId) {
    String hql =
        "select count(id) from Publication t where t.status = 0 and t.publishYear is null and articleType=? and t.psnId = ? ";
    Long count = super.findUnique(hql, articleType, psnId);
    return count.intValue();
  }

  /**
   * 获取加入群组的成果统计数.
   * 
   * @param groupId
   * @param articleType
   * @param psnId
   * @return
   */
  public int getGroupPubNum(Long groupId, Integer articleType, Long psnId) {
    String hql =
        "select count(id) from Publication t where t.status in(0,2,3,4,5) and articleType=? and t.psnId = ? and exists ("
            + "select 1 from GroupPubNode where groupId = ? and articleType = ? and psnId = ? and pubId=t.id)";
    Long count = super.findUnique(hql, articleType, psnId, groupId, articleType, psnId);
    return count.intValue();
  }

  /**
   * 获取当前登录人加入群组的成果数.
   * 
   * @param groupId
   * @param articleType
   * @param psnId
   * @return
   */
  public int getCurrGroupPubNum(Long groupId, Integer articleType, Long psnId) {
    String hql = "select count(id) from Publication t where t.status=0 and articleType=? and t.psnId = ? and exists ("
        + "select 1 from GroupPubNode where groupId = ? and articleType = ? and psnId = ? and pubId=t.id)";
    Long count = super.findUnique(hql, articleType, psnId, groupId, articleType, psnId);
    return count.intValue();
  }

  public List<Publication> getPubByTitle(Integer titleHashCode, Integer articleType, Long psnId) {
    String hql =
        "from Publication t where t.status = 0 and (t.zhTitleHash = ? or t.enTitleHash =?) and articleType=? and t.psnId = ? ";
    return super.createQuery(hql, titleHashCode, titleHashCode, articleType, psnId).list();
  }

  @SuppressWarnings("unchecked")
  public String getFileDesc(Long fileId, Integer isFullLinkFile) {
    String sql = "";
    if (isFullLinkFile != null) {
      sql = "select t.file_name from station_file t where t.file_id=?";
    } else {
      sql = "select t.file_name from archive_files t where t.file_id=?";
    }
    List list = super.queryForList(sql, new Object[] {fileId});
    String fileName = CollectionUtils.isEmpty(list) ? "" : String.valueOf(list.get(0));
    return StringUtils.isNotBlank(fileName) ? fileName.substring(fileName.indexOf(".") + 1, fileName.length() - 1) : "";
  }

  /**
   * 计算在指定的成果中某个成果类型的个数.
   * 
   * @param pubIds
   * @param type
   * @return
   * @throws ServiceException
   */
  public Integer queryPubTotalByPubIdAndType(String pubIds, String type) throws DaoException {
    String hql = "select count(*) from Publication t  where t.id in (" + pubIds + ")  and  t.typeId in (" + type
        + ") and t.status=0";
    Long count = (Long) super.createQuery(hql, new Object[] {}).uniqueResult();
    return count.intValue();
  }

  @SuppressWarnings("unchecked")
  public List<Publication> findPubByPnsId(Long psnId) throws ServiceException {
    String hql =
        "select distinct new Publication(t.psnId,t.articleType,t.zhTitleHash,t.enTitleHash) from PubKnow t where t.psnId=? and t.status in (0,2,3,4,5) and t.articleType=1";
    return super.createQuery(hql, new Object[] {psnId}).list();
  }

  @SuppressWarnings("unchecked")
  public List<Map<String, Object>> matchPubPsnIds(Publication pub) throws ServiceException {
    String hql =
        "select t1.psnId,count(t1.psnId) from PubKnow t1,PersonKnow t2 where t1.psnId=t2.personId and t1.status=0 and t1.articleType=1 and t2.isAddFrd=0 and t2.isPrivate=0 and t2.isLogin=1 and t1.psnId not in(select t5.tempPsnId from FriendTempSys t5 where t5.psnId=?) ";
    List<Object> params = new ArrayList<Object>();
    params.add(pub.getPsnId());
    if (pub.getZhTitleHash() != null) {
      hql += "and t1.zhTitleHash=? ";
      params.add(pub.getZhTitleHash());
    }
    if (pub.getEnTitleHash() != null) {
      hql += "and t1.enTitleHash=? ";
      params.add(pub.getEnTitleHash());
    }
    hql += "group by t1.psnId";
    return rebuildResutl(hql, params);
  }

  public List<Map<String, Object>> matchRefPsnIds(Publication pub) throws ServiceException {
    String hql =
        "select t1.psnId,count(t1.psnId) from PubKnow t1,PersonKnow t2 where t1.psnId=t2.personId and t1.status=0 and t1.articleType=2 and t2.isAddFrd=0 and t2.isPrivate=0 and t2.isLogin=1 and t1.psnId not in(select t3.friendPsnId from Friend t3 where t3.psnId=?) and t1.psnId not in(select t5.tempPsnId from FriendTempSys t5 where t5.psnId=?) ";
    List<Object> params = new ArrayList<Object>();
    params.add(pub.getPsnId());
    params.add(pub.getPsnId());
    if (pub.getZhTitleHash() != null) {
      hql += "and t1.zhTitleHash=? ";
      params.add(pub.getZhTitleHash());
    }
    if (pub.getEnTitleHash() != null) {
      hql += "and t1.enTitleHash=? ";
      params.add(pub.getEnTitleHash());
    }
    hql += "group by t1.psnId";
    return rebuildResutl(hql, params);
  }

  @SuppressWarnings("unchecked")
  private List<Map<String, Object>> rebuildResutl(String hql, List<Object> params) {
    List<Object[]> objList = super.createQuery(hql, params.toArray()).list();
    if (CollectionUtils.isEmpty(objList)) {
      return null;
    }
    List<Map<String, Object>> listMap = new ArrayList<Map<String, Object>>();
    for (Object[] objects : objList) {
      Map<String, Object> map = new HashMap<String, Object>();
      map.put("psnId", Long.valueOf(String.valueOf(objects[0])));
      map.put("count", Integer.valueOf(String.valueOf(objects[1])));
      listMap.add(map);
    }
    return listMap;
  }

  public List<Map<String, Object>> matchPubConfimPsnIds(Publication pub) throws ServiceException {
    String hql =
        "select t1.psnId,count(t1.psnId) from PubConfirmKnowFields t1,PersonKnow t2 where t1.psnId=t2.personId and t2.isAddFrd=0 and t2.isPrivate=0 and t2.isLogin=1 and t1.psnId not in(select t5.tempPsnId from FriendTempSys t5 where t5.psnId=?) ";
    List<Object> params = new ArrayList<Object>();
    params.add(pub.getPsnId());
    if (pub.getZhTitleHash() != null) {
      hql += "and t1.zhTitleHash=? ";
      params.add(pub.getZhTitleHash());
    }
    if (pub.getEnTitleHash() != null) {
      hql += "and t1.enTitleHash=? ";
      params.add(pub.getEnTitleHash());
    }
    hql += "group by t1.psnId";
    return rebuildResutl(hql, params);
  }

  @Override
  public void save(Publication entity) {
    super.save(entity);
  }

  /**
   * 删除pubknow.
   * 
   * @param pubId
   */
  public void delPubKnow(Long pubId) {
    super.createQuery("delete from PubKnow where id=?", pubId).executeUpdate();
  }

  /**
   * 保存成果know匹配结果.
   * 
   * @param pub
   */
  public void savePubKnow(Publication pub, int isOwner, int seq) {

    PubKnow pubKnow = super.findUnique("from PubKnow where id=?", pub.getId());
    if (pubKnow == null) {
      pubKnow = new PubKnow();
      pubKnow.setId(pub.getId());
      pubKnow.setPsnId(pub.getPsnId());
    }
    pubKnow.setArticleType(pub.getArticleType());
    pubKnow.setTypeId(pub.getTypeId());
    pubKnow.setStatus(pub.getStatus());
    pubKnow.setZhTitleHash(pub.getZhTitleHash());
    pubKnow.setEnTitleHash(pub.getEnTitleHash());
    if (isOwner == 1 && seq != -1) {
      pubKnow.setSeqNo(seq);
    } else {
      pubKnow.setSeqNo(null);
    }
    pubKnow.setIsPubAuthors(isOwner);
    if (pub.getJnlId() != null) {
      pubKnow.setJnlId(pub.getJnlId());
    }
    super.getSession().saveOrUpdate(pubKnow);
  }

  /**
   * 获取成果列表记录(最多获取一百条记录).
   * 
   * @param params
   * @return
   */
  @SuppressWarnings({"unchecked", "rawtypes"})
  public List<Publication> findPublicationListByParam(Map<String, Object> params) {
    // 检索权限设置为公开且在基准库中存在的成果记录.
    String hql =
        "select new Publication(id,psnId,impactFactors,zhTitle,enTitle,articleType) from Publication p where p.status='0' "
            + "and exists (select 1 from PublicationPdwh pp where pp.pubId=p.id) "
            + "and exists (select 1 from PsnConfig pc,PsnConfigPub pcp where pc.cnfId=pcp.id.cnfId and pcp.id.pubId=p.id and pcp.anyUser=7) "
            + "and p.enTitle is not null ";
    if (params != null && params.size() > 0) {
      // 遍历参数集合.
      Iterator iterator = params.keySet().iterator();
      while (iterator.hasNext()) {
        String key = ObjectUtils.toString(iterator.next());// 参数名.
        String value = ObjectUtils.toString(params.get(key));// 参数值.
        // 拼装参数字符串.
        hql += "and p." + key + " like '" + value + "%' ";
      }
    }
    List<Publication> result = super.createQuery(hql).setMaxResults(100).list();
    return result;
  }

  @SuppressWarnings("rawtypes")
  public int pubMatchName(Long tmPsnId, String zhName, String likeName) {
    zhName = StringUtils.isBlank(zhName) ? "" : zhName.trim();
    likeName = StringUtils.isBlank(likeName) ? "" : likeName.trim().toLowerCase();
    String hql =
        "select count(t1.id) from PubKnow t1,PubMember t2 where t1.id=t2.pubId and (t2.name=? or lower(t2.name) like ?) and t1.psnId=?";
    List list = createQuery(hql, zhName, likeName, tmPsnId).list();
    return CollectionUtils.isEmpty(list) ? 0 : Integer.valueOf(list.get(0).toString());
  }

  public void updatePubStatus(List<Long> resRecIds) throws DaoException {
    String hql =
        "update Publication t set t.status=1 where t.id in(select t.resId from PsnResReceiveRes t where t.status<>1 and t.resRecId in(:resRecIds))";
    super.createQuery(hql).setParameterList("resRecIds", resRecIds);
  }

  @SuppressWarnings("rawtypes")
  public List findPubIdsBatch(Long lastId, int batchSize) throws DaoException {
    String sql =
        "select t.pub_id from task_pub_ids t where t.pub_id > ? and t.status =? and rownum <= ? order by t.pub_id asc";
    return super.queryForList(sql, new Object[] {lastId, 0, batchSize});
  }

  public void updatePubBriefDescTaskStatus(Long pubId, int status) throws DaoException {
    String sql = "update task_pub_ids t set t.status = ? where t.pub_id= ?";
    super.update(sql, new Object[] {status, pubId});
  }

  /**
   * 
   * @author liangguokeng
   */
  @SuppressWarnings("unchecked")
  public List<Publication> findPubIdsByPsnId(Long psnId) throws DaoException {
    String hql = "from Publication t where t.status=0 and t.articleType=1 and t.psnId=? ";
    return super.createQuery(hql, psnId).list();
  }

  @SuppressWarnings({"rawtypes"})
  public List findRebuildPubId(int size) throws DaoException {
    String sql = "select t.pub_id from task_pub_author_ids t where t.status=? and rownum<=?";
    return super.queryForList(sql, new Object[] {1, size});
  }

  @SuppressWarnings("unchecked")
  public List<PubMember> findPubMemberByPubId(Long pubId) {
    String hql = "from PubMember where pubId=?";
    return super.createQuery(hql, pubId).list();
  }

  public void updatePubAuthorNames(Long pubId, String newAuthorNames) throws DaoException {
    String hql = "update Publication set authorNames=? where id=?";
    super.createQuery(hql, newAuthorNames, pubId).executeUpdate();
  }

  public void updateTaskPubAuthor(Long pubId) {
    String sql = "update task_pub_author_ids set status=? where pub_id=?";
    super.update(sql, new Object[] {0, pubId});
  }

  public boolean isCurrPsnPub(Long pubId, Long psnId) {
    String hql = "select count(t.id) from Publication t where t.id=? and t.psnId=? and t.status=?";
    return (Long) super.findUnique(hql, pubId, psnId, 0) > 0;
  }

  @SuppressWarnings("unchecked")
  public List<Publication> findPdwhPubLinkeScmPubs(Long pubId, int dbid) {
    String hql = "select t1 from Publication t1,PublicationPdwh t2 where t1.id=t2.pubId ";
    if (dbid == 2) {
      hql += " and t2.isiId=?";
    }
    if (dbid == 14) {
      hql += " and t2.eiId=?";
    }
    if (dbid == 8) {
      hql += " and t2.spsId=?";
    }
    if (dbid == 4) {
      hql += " and t2.cnkiId=?";
    }
    if (dbid == 11) {
      hql += " and t2.cniprId=?";
    }
    if (dbid == 10) {
      hql += " and t2.wfId=?";
    }
    hql += " and t1.status = 0";
    return super.createQuery(hql, pubId).setMaxResults(10).list();
  }

  @SuppressWarnings("unchecked")
  public List<Publication> findPdwhRelatedPubs(Page page, Long pubId, int dbid) {
    String listHql = "select t1 ";
    String countHql = "select count(t1.id) ";
    String hql = " from Publication t1,PublicationPdwh t2 where t1.id=t2.pubId ";
    if (ArrayUtils.contains(ConstPdwhPubRefDb.ISI_LIST, Integer.valueOf(dbid))) {
      hql += " and t2.isiId=?";
    } else if (dbid == 14) {
      hql += " and t2.eiId=?";
    } else if (dbid == 8) {
      hql += " and t2.spsId=?";
    } else if (dbid == 4) {
      hql += " and t2.cnkiId=?";
    } else if (dbid == 11) {
      hql += " and t2.cniprId=?";
    } else if (dbid == 10) {
      hql += " and t2.wfId=?";
    } else {
      return null;
    }
    hql += " and t1.status = 0";

    Long totalCount = super.findUnique(countHql + hql, pubId);
    page.setTotalCount(totalCount);
    Query queryResult = super.createQuery(listHql + hql, pubId);
    queryResult.setFirstResult(page.getFirst() - 1);
    queryResult.setMaxResults(page.getPageSize());
    return queryResult.list();
  }

  @SuppressWarnings("unchecked")
  public List<Publication> findPdwhRelatedPub(boolean isFindFullText, Long pubId, int dbid) throws DaoException {
    StringBuilder hql = new StringBuilder("select t1 from Publication t1 where t1.status = 0 ");
    if (isFindFullText) {
      hql.append(" and t1.fulltextFileid is not null");
    }
    hql.append(" and exists(");
    hql.append("select 1 from PublicationPdwh t2 where t2.pubId=t1.id ");
    if (ArrayUtils.contains(ConstPdwhPubRefDb.ISI_LIST, Integer.valueOf(dbid))) {
      hql.append(" and t2.isiId=?");
    } else if (dbid == 14) {
      hql.append(" and t2.eiId=?");
    } else if (dbid == 8) {
      hql.append(" and t2.spsId=?");
    } else if (dbid == 4) {
      hql.append(" and t2.cnkiId=?");
    } else if (dbid == 11) {
      hql.append(" and t2.cniprId=?");
    } else if (dbid == 10) {
      hql.append(" and t2.wfId=?");
    } else {
      return null;
    }
    hql.append(")");
    return super.createQuery(hql.toString(), pubId).setMaxResults(1).list();
  }

  /**
   * 查找有多少个成果有全文附件
   * 
   * @param pubIds
   * @param status
   * @return
   * @throws DaoException
   */
  public Long countFullText(List<Long> pubIds) throws DaoException {
    String hql =
        "select count(*) from Publication t where t.id in (:pubIds) and t.fulltextFileid is not null and t.status = 0";
    return (Long) super.createQuery(hql).setParameterList("pubIds", pubIds).uniqueResult();

  }

  /**
   * 通过id删除成果人员记录.
   * 
   * @param id
   * @throws DaoException
   */
  public void deletePubMemberById(Long id) throws DaoException {
    super.createQuery("delete from PubMember t where t.id=?", id).executeUpdate();
  }

  /**
   * 通过id更新成果人员记录.
   * 
   * @param id
   * @throws DaoException
   */
  public void updatePubMemberById(Long id, Long psnId) throws DaoException {
    super.createQuery("update PubMember t set t.psnId=? where t.id=?", psnId, id).executeUpdate();
  }

  /**
   * 删除指定成果对应的成果与单位关系.
   * 
   * @param pubId
   * @param insId
   * @throws DaoException
   */
  public void deletePubInsById(Long pubId, Long insId) throws DaoException {
    super.createQuery("delete from PubIns t where t.id.pubId=? and t.id.insId=?", pubId, insId).executeUpdate();
  }

  /**
   * 查询成果与单位关系.
   * 
   * @param pubId
   * @param insId
   * @param psnId
   * @return
   * @throws DaoException
   */
  public PubIns queryPubIns(Long pubId, Long insId, Long psnId) throws DaoException {
    return (PubIns) super.createQuery("from PubIns t where t.psnId=? and t.id.pubId=? and t.id.insId=?", psnId, pubId,
        insId).uniqueResult();
  }

  public void updatePubInsById(Long pubId, Long insId, Long psnId) throws DaoException {
    super.createQuery("update PubIns t set t.psnId=? where t.id.pubId=? and t.id.insId=?", psnId, pubId, insId)
        .executeUpdate();
  }

  public void deletePublicationById(Long id) throws DaoException {
    super.createQuery("delete from Publication t where t.id=?", id).executeUpdate();
  }

  public void updatePublicationById(Long id, Long psnId) throws DaoException {
    super.createQuery("update Publication t set t.psnId=? where t.id=?", psnId, id).executeUpdate();
  }

  /**
   * 判断指定id是否有全文
   */

  public Integer hasFullText(Long pubId) throws DaoException {
    return (Integer) super.createQuery(
        "select count(1) from Publication t where t.id = ? and t.fulltextFileid is not null and t.status = 0", pubId)
            .uniqueResult();
  }

  /**
   * psn_kw_pub需要的成果信息.
   * 
   * @param pubId
   * @return
   */
  public Publication getPubInfoForPsnKw(Long pubId) {
    String hql = "select new Publication(id, typeId, publishYear) from Publication t where t.id = ? ";
    return super.findUnique(hql, pubId);
  }

  public List<Long> findPsnPubByJids(Long psnId, int articleType) {
    String hql =
        "select t.jid from Publication t where t.psnId=? and t.articleType=? and t.status=? and t.jid is not null";
    return super.find(hql, psnId, articleType, 0);
  }

  /**
   * IRIS业务系统接口查询某人的公开成果总数.
   * 
   * @param psnId
   * @param keywords
   * @param uuid
   * @param permissions
   * @return
   * @throws DaoException
   */
  public Long queryPsnPublicPubCount(Long psnId, String keywords, String uuid, List<Integer> permissions,
      List<Integer> pubTypeList) throws DaoException {
    StringBuffer sb = new StringBuffer(
        "select count(t.id) from Publication t where t.articleType=:arcticleType and t.status=:status and t.psnId=:psnId");
    if (CollectionUtils.isNotEmpty(pubTypeList)) {
      sb.append(" and t.typeId in(:pubTypeList)");
    }
    if (uuid != null) {
      sb.append(" and t.id not in(select t3.pubId from IrisExcludedPub t3 where t3.uuid=:uuid)");
    }
    sb.append(
        " and exists(select 1 from PsnConfig pc,PsnConfigPub pcp where pc.cnfId=pcp.id.cnfId and pcp.id.pubId=t.id and pcp.anyUser in(:permissions) )");
    if (StringUtils.isNotBlank(keywords)) {
      sb.append(" and (lower(t.zhTitle) like :zhTitle or lower(t.enTitle) like :enTitle)");
    }
    Query query = super.createQuery(sb.toString()).setParameter("arcticleType", 1).setParameter("status", 0)
        .setParameter("psnId", psnId);
    if (CollectionUtils.isNotEmpty(pubTypeList)) {
      query.setParameterList("pubTypeList", pubTypeList);
    }
    if (uuid != null) {
      query.setParameter("uuid", uuid);
    }
    query.setParameterList("permissions", permissions);
    if (StringUtils.isNotBlank(keywords)) {
      query.setParameter("zhTitle", "%" + keywords.toLowerCase() + "%").setParameter("enTitle",
          "%" + keywords.toLowerCase() + "%");
    }
    return (Long) query.uniqueResult();
  }

  public Long getPsnNotExistsResumePubCount(Long psnId) {
    String hql =
        "select count(t.id) from Publication t where t.articleType=:arcticleType and t.status=:status and t.psnId=:psnId ";
    hql += " and not exists(select 1 from PsnConfigPub t1 where t.id=t1.id.pubId)";
    Query query =
        super.createQuery(hql).setParameter("arcticleType", 1).setParameter("status", 0).setParameter("psnId", psnId);
    return (Long) query.uniqueResult();
  }

  /**
   * IRIS业务系统接口分页查询某人的公开成果记录.
   * 
   * @param psnId
   * @param keywords
   * @param uuid
   * @param permissions
   * @param sortType
   * @param page
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public Page<Publication> queryPsnPublicPubByPage(Long psnId, String keywords, String uuid, List<Integer> permissions,
      String sortType, Page<Publication> page, List<Integer> pubTypeList) throws DaoException {
    StringBuffer sb = new StringBuffer(
        "from Publication t where t.articleType=:arcticleType and t.status=:status and t.psnId=:psnId");
    if (CollectionUtils.isNotEmpty(pubTypeList)) {
      sb.append(" and t.typeId in(:pubTypeList)");
    }
    if (uuid != null) {
      sb.append(" and t.id not in(select t3.pubId from IrisExcludedPub t3 where t3.uuid=:uuid)");
    }
    sb.append(
        " and exists(select 1 from PsnConfig pc,PsnConfigPub pcp where pc.cnfId=pcp.id.cnfId and pcp.id.pubId=t.id and pcp.anyUser in(:permissions) )");
    if (StringUtils.isNotBlank(keywords)) {
      sb.append(" and (lower(t.zhTitle) like :zhTitle or lower(t.enTitle) like :enTitle)");
    }

    Query query = super.createQuery("select count(t.id) " + sb.toString()).setParameter("arcticleType", 1)
        .setParameter("status", 0).setParameter("psnId", psnId);
    if (CollectionUtils.isNotEmpty(pubTypeList)) {
      query.setParameterList("pubTypeList", pubTypeList);
    }
    if (uuid != null) {
      query.setParameter("uuid", uuid);
    }
    query.setParameterList("permissions", permissions);
    if (StringUtils.isNotBlank(keywords)) {
      query.setParameter("zhTitle", "%" + keywords.toLowerCase() + "%").setParameter("enTitle",
          "%" + keywords.toLowerCase() + "%");
    }
    Long count = (Long) query.uniqueResult();
    page.setTotalCount(count);

    if ("updateTime".equals(sortType)) {
      sb.append(" order by t.updateDate desc, t.id desc");
    } else {
      sb.append(" order by nvl(t.publishYear,0) desc,nvl(t.publishMonth,0) desc,nvl(t.publishDay,0) desc,t.id desc");
    }

    query = super.createQuery(
        "select new Publication(t.id, t.zhTitle, t.enTitle, t.authorNames, t.typeId, t.briefDesc, t.briefDescEn, t.citedList, t.citedTimes, t.doi, t.publishYear, t.publishMonth, t.publishDay, t.updateDate) "
            + sb.toString()).setFirstResult(page.getFirst() - 1).setMaxResults(page.getPageSize())
                .setParameter("arcticleType", 1).setParameter("status", 0).setParameter("psnId", psnId);
    if (CollectionUtils.isNotEmpty(pubTypeList)) {
      query.setParameterList("pubTypeList", pubTypeList);
    }
    if (uuid != null) {
      query.setParameter("uuid", uuid);
    }
    query.setParameterList("permissions", permissions);
    if (StringUtils.isNotBlank(keywords)) {
      query.setParameter("zhTitle", "%" + keywords.toLowerCase() + "%").setParameter("enTitle",
          "%" + keywords.toLowerCase() + "%");
    }
    page.setResult(query.list());
    return page;
  }

  /**
   * IRIS业务系统接口查询某人的成果总数.
   * 
   * @param psnId
   * @param keywords
   * @param pubTypes
   * @return
   * @throws DaoException
   */
  public Long queryPsnPubCount(Long psnId, String keywords, String authors, String uuid, List<Integer> pubTypes)
      throws DaoException {
    StringBuffer sb = new StringBuffer(
        "select count(t.id) from Publication t where t.articleType=:articleType and t.status=:status and t.psnId=:psnId");
    if (uuid != null) {
      sb.append(" and t.id not in(select t3.pubId from IrisExcludedPub t3 where t3.uuid=:uuid)");
    }
    if (CollectionUtils.isNotEmpty(pubTypes)) {
      sb.append(" and t.typeId in(:pubTypes)");
    }
    if (StringUtils.isNotBlank(authors)) {
      sb.append(" and lower(t.authorNames) like:authors ");
    }
    if (StringUtils.isNotBlank(keywords)) {
      sb.append(" and (lower(t.zhTitle) like:zhTitle or lower(t.enTitle) like:enTitle)");
    }
    Query q = super.createQuery(sb.toString());
    q.setParameter("articleType", 1);
    q.setParameter("status", 0);
    q.setParameter("psnId", psnId);
    if (uuid != null) {
      q.setParameter("uuid", uuid);
    }
    if (CollectionUtils.isNotEmpty(pubTypes)) {
      q.setParameterList("pubTypes", pubTypes);
    }
    if (StringUtils.isNotBlank(authors)) {
      q.setParameter("authors", "%" + authors.toLowerCase() + "%");
    }
    if (StringUtils.isNotBlank(keywords)) {
      q.setParameter("zhTitle", "%" + keywords.toLowerCase() + "%");
      q.setParameter("enTitle", "%" + keywords.toLowerCase() + "%");
    }
    return (Long) q.uniqueResult();
  }

  /**
   * IRIS业务系统接口分页查询某人的成果记录.
   * 
   * @param psnId
   * @param keywords
   * @param sortType
   * @param page
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public Page<Publication> queryPsnPubByPage(Long psnId, String keywords, String authors, String uuid, String sortType,
      Page<Publication> page, List<Integer> pubTypeList) throws DaoException {
    StringBuffer sb =
        new StringBuffer("from Publication t where t.articleType=:articleType and t.status=:status and t.psnId=:psnId");
    if (CollectionUtils.isNotEmpty(pubTypeList)) {
      sb.append(" and t.typeId in(:pubTypeList)");
    }
    if (uuid != null) {
      sb.append(" and t.id not in(select t3.pubId from IrisExcludedPub t3 where t3.uuid=:uuid)");
    }
    if (StringUtils.isNotBlank(authors)) {
      sb.append(" and lower(t.authorNames) like :authorNames ");
    }
    if (StringUtils.isNotBlank(keywords)) {
      sb.append(" and (lower(t.zhTitle) like :zhTitle or lower(t.enTitle) like :enTitle)");
    }
    Query query = super.createQuery("select count(t.id) " + sb.toString()).setParameter("articleType", 1)
        .setParameter("status", 0).setParameter("psnId", psnId);
    if (CollectionUtils.isNotEmpty(pubTypeList)) {
      query.setParameterList("pubTypeList", pubTypeList);
    }
    if (uuid != null) {
      query.setParameter("uuid", uuid);
    }
    if (StringUtils.isNotBlank(authors)) {
      query.setParameter("authorNames", "%" + authors.toLowerCase() + "%");
    }
    if (StringUtils.isNotBlank(keywords)) {
      query.setParameter("zhTitle", "%" + keywords.toLowerCase() + "%").setParameter("enTitle",
          "%" + keywords.toLowerCase() + "%");
    }
    Long count = (Long) query.uniqueResult();
    page.setTotalCount(count);

    if ("updateTime".equals(sortType)) {
      sb.append(" order by t.updateDate desc, t.id desc");
    } else {
      sb.append(" order by nvl(t.publishYear,0) desc,nvl(t.publishMonth,0) desc,nvl(t.publishDay,0) desc,t.id desc");
    }

    query = super.createQuery(
        "select new Publication(t.id, t.zhTitle, t.enTitle, t.authorNames, t.typeId, t.briefDesc, t.briefDescEn, t.citedList, t.citedTimes, t.doi, t.publishYear, t.publishMonth, t.publishDay, t.updateDate) "
            + sb.toString()).setFirstResult(page.getFirst() - 1).setMaxResults(page.getPageSize())
                .setParameter("articleType", 1).setParameter("status", 0).setParameter("psnId", psnId);
    if (CollectionUtils.isNotEmpty(pubTypeList)) {
      query.setParameterList("pubTypeList", pubTypeList);
    }
    if (uuid != null) {
      query.setParameter("uuid", uuid);
    }
    if (StringUtils.isNotBlank(authors)) {
      query.setParameter("authorNames", "%" + authors.toLowerCase() + "%");
    }
    if (StringUtils.isNotBlank(keywords)) {
      query.setParameter("zhTitle", "%" + keywords.toLowerCase() + "%").setParameter("enTitle",
          "%" + keywords.toLowerCase() + "%");
    }
    page.setResult(query.list());
    return page;
  }

  /**
   * 通过成果ID和人员ID查询成果数.
   * 
   * @param pubId
   * @param psnId
   * @return
   * @throws DaoException
   */
  public Long queryPubCountByPubIdAnPsnId(Long pubId, Long psnId) throws DaoException {
    return (Long) super.createQuery("select count(t.id) from Publication t where t.id=? and t.psnId=?", pubId, psnId)
        .uniqueResult();
  }

  /**
   * 获取上周发表过论文的 zk add.
   */
  @SuppressWarnings("unchecked")
  public List<Long> getPsnIdByLastWeekly(Integer size, Long lastPsnId) throws DaoException {
    // 上周一
    Calendar lastWeekM = Calendar.getInstance();
    // 上周日
    Calendar lastWeekS = Calendar.getInstance();
    int dayOfWeek = lastWeekM.get(Calendar.DAY_OF_WEEK) - 1;
    lastWeekM.add(Calendar.DATE, (1 - dayOfWeek) - 7);
    lastWeekS.add(Calendar.DATE, (7 - dayOfWeek) - 7);
    String hql =
        " select distinct p.psnId from Publication p where p.createDate >= ? and p.createDate <= ? and p.articleType=1 and p.recordFrom=4 and p.status=0 order by p.psnId ";
    List<Long> returnList = super.createQuery(hql, lastWeekM.getTime(), lastWeekS.getTime()).setMaxResults(100)
        .setFirstResult(100 * size).list();
    // 排除掉上次最后一位psnid,解决边界问题
    returnList.remove(lastPsnId);
    return returnList;
  }

  /***
   * 获取psnId上周发表的成果信息，成果标题，成果数据
   */

  @SuppressWarnings("unchecked")
  @Deprecated
  public Map<String, Object> getPubInfoByConfig(Long psnId, Long configId) throws DaoException {

    // 上周一
    Calendar lastWeekM = Calendar.getInstance();
    // 上周日
    Calendar lastWeekS = Calendar.getInstance();
    int dayOfWeek = lastWeekM.get(Calendar.DAY_OF_WEEK) - 1;
    lastWeekM.add(Calendar.DATE, (1 - dayOfWeek) - 7);
    lastWeekS.add(Calendar.DATE, (7 - dayOfWeek) - 7);

    String hqlSuffix =
        "select  new Publication(p.id,p.zhTitle,p.enTitle,p.briefDesc,p.briefDescEn,p.authorNames,p.fulltextFileid,p.fulltextUrl)";

    String hql =
        " from Publication p,PsnConfig pc where p.createDate >= ? and p.createDate <= ? and p.articleType=1 and p.status=0 and p.recordFrom=4 and  p.psnId=?"
            + " and p.psnId=pc.psnId"
            + " and exists(select 1 from PsnConfigPub pcp where pcp.anyUser>? and pcp.id.cnfId=pc.cnfId and pcp.id.pubId=p.id )";
    // 获取总数
    Long count = super.countHqlResult(hql, lastWeekM.getTime(), lastWeekS.getTime(), psnId, PsnCnfConst.ALLOWS_SELF);
    // 获取数据
    List<Publication> pubList =
        super.createQuery(hqlSuffix + hql, lastWeekM.getTime(), lastWeekS.getTime(), psnId, PsnCnfConst.ALLOWS_SELF)
            .setMaxResults(3).list();

    if (CollectionUtils.isNotEmpty(pubList) && count > 0) {

      Map<String, Object> returnMap = new HashMap<String, Object>();
      returnMap.put("pubList", pubList);
      returnMap.put("count", count);
      return returnMap;
    }
    return null;
  }

  @SuppressWarnings("unchecked")
  public Map<String, Object> getPubInfoByConfig(Long psnId) throws DaoException {

    // 上周一
    Calendar lastWeekM = Calendar.getInstance();
    // 上周日
    Calendar lastWeekS = Calendar.getInstance();
    int dayOfWeek = lastWeekM.get(Calendar.DAY_OF_WEEK) - 1;
    lastWeekM.add(Calendar.DATE, (1 - dayOfWeek) - 7);
    lastWeekS.add(Calendar.DATE, (7 - dayOfWeek) - 7);

    String hqlSuffix =
        "select  new Publication(p.id,p.zhTitle,p.enTitle,p.briefDesc,p.briefDescEn,p.authorNames,p.fulltextFileid,p.fulltextUrl)";

    String hql =
        " from Publication p,PsnConfig pc where p.createDate >= ? and p.createDate <= ? and p.articleType=1 and p.status=0 and p.recordFrom=4 and  p.psnId=?"
            + " and p.psnId=pc.psnId"
            + " and exists(select 1 from PsnConfigPub pcp where pcp.anyUser>? and pcp.id.cnfId=pc.cnfId and pcp.id.pubId=p.id )";
    // 获取总数
    Long count = super.countHqlResult(hql, lastWeekM.getTime(), lastWeekS.getTime(), psnId, PsnCnfConst.ALLOWS_SELF);
    // 获取数据
    List<Publication> pubList =
        super.createQuery(hqlSuffix + hql, lastWeekM.getTime(), lastWeekS.getTime(), psnId, PsnCnfConst.ALLOWS_SELF)
            .setMaxResults(3).list();

    if (CollectionUtils.isNotEmpty(pubList) && count > 0) {

      Map<String, Object> returnMap = new HashMap<String, Object>();
      returnMap.put("pubList", pubList);
      returnMap.put("count", count);
      return returnMap;
    }
    return null;
  }

  /**
   * 获取符合推荐条件的成果期刊(只查询成果所属人ID和成果ID).
   * 
   * @param timeLimit 时间限制(小时数).
   * @param maxSize
   * @return
   */
  @SuppressWarnings("rawtypes")
  public List<Publication> getPubReJnlTaskList(int timeLimit, int maxSize, Integer size) {
    List<Publication> resultList = new ArrayList<Publication>();
    String hql =
        "select t.psnId,t.id,t.zhTitle,t.enTitle from Publication t where t.createDate>? and t.typeId=3 and t.status='0' order by id ";
    // 计算限制的时间(以当前时间往前推算起始计算时间).
    Date date = new Date((new Date().getTime()) - 1000L * timeLimit * 60 * 60);
    List pubInfoList = super.createQuery(hql, date).setMaxResults(maxSize).setFirstResult(size * maxSize).list();
    if (CollectionUtils.isNotEmpty(pubInfoList)) {
      for (int i = 0; i < pubInfoList.size(); i++) {
        Object[] obj = (Object[]) pubInfoList.get(i);
        Publication pub = new Publication();
        pub.setPsnId(Long.valueOf(String.valueOf(obj[0])));
        pub.setId(Long.valueOf(String.valueOf(obj[1])));
        pub.setZhTitle(String.valueOf(obj[2]));
        pub.setEnTitle(String.valueOf(obj[3]));
        resultList.add(pub);
      }
    }
    return resultList;
  }

  /**
   * 获取符合推荐条件的有成果的人员<根据论文的期刊推荐用>
   * 
   * @param timeLimit 时间间隔，单位：小时
   * 
   * @param maxSize 每次取的量
   * 
   * @param size 第几批
   * 
   * @param lastPsnId 上次取的list中最后一名人员
   * 
   * 
   */
  @SuppressWarnings("unchecked")
  public List<Long> getPsnIdForPubJnlRecmd(Integer timeLimit, Integer maxSize, Integer size, Long lastPsnId) {

    String hql =
        "select distinct t.psnId from Publication t where t.createDate>? and t.typeId=3 and t.psnId>? and t.status='0' order by psnId ";
    // 计算限制的时间(以当前时间往前推算起始计算时间).
    Date date = new Date((new Date().getTime()) - 1000L * timeLimit * 60 * 60);
    List<Long> returnList = super.createQuery(hql, date, lastPsnId).setMaxResults(maxSize).list();

    return returnList;

  }

  /**
   * 获取条例推荐条件的人员的成果标题
   **/
  @SuppressWarnings("unchecked")
  public List<Publication> getPubTitleForPubJnlRecmd(Integer timeLimit, Integer maxSize, Long psnId, Long startPubId) {
    String hql =
        "select new Publication(t.psnId,t.id,t.zhTitle,t.enTitle) from Publication t where t.createDate>? and t.id>? and t.typeId=3 and t.status='0' and t.psnId=? order by id ";
    // 计算限制的时间(以当前时间往前推算起始计算时间).
    Date date = new Date((new Date().getTime()) - 1000L * timeLimit * 60 * 60);
    return super.createQuery(hql, date, startPubId, psnId).setMaxResults(maxSize).list();
  }

  /**
   * 取最新一条会议论文期刊
   */
  public Publication getPubTitleForPubJnlRecmdOne(Integer maxSize, Long psnId, Long startPubId) {

    StringBuffer sb = new StringBuffer();

    sb.append(
        " select new Publication(t.psnId,t.id,t.zhTitle,t.enTitle) from Publication t where t.typeId=3 and t.status='0' and t.psnId=?");

    if (startPubId != 0) {
      sb.append(" and id < ? ");
    } else {
      sb.append(" and  id > ? ");
    }

    sb.append(" order by createDate desc ");

    return (Publication) super.createQuery(sb.toString(), psnId, startPubId).setMaxResults(maxSize).uniqueResult();

  }

  /**
   * 通过成果id,获取成果标题信息
   */
  public Publication getPubTitleById(Long pubId) {
    String hql =
        "select  new Publication(p.id,p.zhTitle,p.enTitle,p.briefDesc,p.briefDescEn,p.authorNames,p.fulltextFileid,p.fulltextUrl)  from Publication p where p.id=?";

    return (Publication) super.createQuery(hql, pubId).uniqueResult();
  }

  /**
   * 通过成果id,获取成果标题，来源，作者，来源库信息
   */
  public Publication getPubById(Long pubId) {
    String hql =
        "select  new Publication(p.id,p.zhTitle,p.enTitle,p.briefDesc,p.briefDescEn,p.authorNames,p.fulltextFileid,p.fulltextUrl,p.sourceDbId)  from Publication p where p.id=?";

    return (Publication) super.createQuery(hql, pubId).uniqueResult();
  }

  /**
   * 根据pubIds获取指定的成果列表.
   * 
   * @param pubIds
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Publication> findPublicationList(List<Long> pubIds) {
    String hql = "from Publication t where t.id in(:pubIds)";
    return super.createQuery(hql).setParameterList("pubIds", pubIds).list();
  }

  @SuppressWarnings("unchecked")
  public List<Publication> getPubFullTexts(List<Long> pubIds) throws DaoException {
    String hql =
        "select new Publication(t.id,t.psnId,t.fulltextFileid,t.articleType) from Publication t where t.id in(:pubIds) and t.status=0";
    return super.createQuery(hql).setParameterList("pubIds", pubIds).list();
  }

  @SuppressWarnings("unchecked")
  public List<Publication> queryNeedRefreshPub(int maxSize) throws DaoException {
    String hql =
        "select new Publication(t.id, t.isiId) from Publication t where t.isiId is not null and not exists(select 1 from PublicationPdwh t1 where t1.pubId=t.id) order by t.id desc";
    return super.createQuery(hql).setMaxResults(maxSize).list();
  }

  /**
   * 查找所有成果ID
   * 
   * @param lastPubId
   * @param size
   * @return
   */
  public List<Long> findAllPubIds(Long lastPubId, Integer size) {
    String hql = "select t.id from Publication t where t.id > ? order by t.id asc";
    return this.createQuery(hql, lastPubId).setMaxResults(size).list();
  }

  /**
   * 查询成果推荐基本信息.
   * 
   * @param pubId
   * @return
   */
  public Publication getPubBasicRcmdInfo(Long pubId) {

    String hql =
        "select new Publication(id, psnId, typeId, publishYear,publishMonth,publishDay,impactFactors,citedTimes,sourceDbId,doi,isiId) from"
            + " Publication t where t.id = ? ";
    return super.findUnique(hql, pubId);
  }

  /**
   * 获取来源于群组的成果.
   * 
   * @param pubId
   * @return
   */
  public Publication getPubFromGroup(Long pubId) {
    String hql = "from Publication t where t.id=? and t.status=5";
    return super.findUnique(hql, pubId);
  }

  @SuppressWarnings("rawtypes")
  public List findPubMatchOwnerIds(int size) {
    String sql =
        "select t.pub_id from task_pub_match_owner_ids t where t.status =? and rownum <= ? order by t.pub_id asc";
    return super.queryForList(sql, new Object[] {0, size});
  }

  public void executedPubMatchOwnerIds(Long pubId) {
    String sql = "update task_pub_match_owner_ids t set t.status = ? where t.pub_id= ?";
    super.update(sql, new Object[] {1, pubId});
  }

  // 更新成果brief_desc_en字段用
  @SuppressWarnings("rawtypes")
  public List findPubIdsBatchByPubType(Long lastId, int batchSize) throws DaoException {
    String sql =
        "select p.pub_id from (select t.pub_id  from Publication t,CONST_PUB_TYPE c where t.pub_id > ? and t.status =? and t.article_type = 1 and t.pub_type=c.TYPE_ID and (t.pub_type = 3 or t.pub_type = 4 or t.pub_type = 5 or t.pub_type = 7) order by t.pub_id asc) p where rownum<= ?";
    // String sql = "select t.pub_id from Publication t where t.pub_id > ?
    // and t.status =? and t.article_type = 1 and rownum <= ? and
    // (t.pub_type = 3 or t.pub_type = 4 or t.pub_type = 5 or t.pub_type =
    // 7) order by t.pub_id asc";
    return super.queryForList(sql, new Object[] {lastId, 0, batchSize});
  }

  /**
   * scm-6684 更新briefDesc或briefDescEn字段
   * 
   * @param locale
   * @param brief
   * @param pubId
   * @throws DaoException
   */
  public void updateBriefDesc(String locale, String brief, Long pubId) throws DaoException {
    String hql = "";
    if (locale.equals("zh_CN")) {
      hql = "update Publication p set p.briefDesc = ? where p.id = ?";
    } else {
      hql = "update Publication p set p.briefDescEn = ? where p.id = ?";
    }
    super.createQuery(hql, brief, pubId).executeUpdate();
  }

  // 用于检索时查询pub
  @SuppressWarnings("rawtypes")
  public List findPubByBatchSize(Long lastId, Integer batchSize) {
    String hql =
        "from Publication t where t.status = 0 and t.articleType = 1 and (t.typeId = 3 or t.typeId = 4 or t.typeId = 7) and t.id>:lastId order by t.id asc";
    // return super.queryForList(sql, new Object[] { lastId, lastId +
    // batchSize, 0 });
    return super.createQuery(hql).setParameter("lastId", lastId).setMaxResults(batchSize).list();
  }

  // 用于检索时查询pub
  @SuppressWarnings("rawtypes")
  public List findPatentByBatchSize(Long lastId, Integer batchSize) {
    String hql =
        "from Publication t where t.status = 0 and t.articleType = 1 and t.typeId = 5 and t.id>:lastId order by t.id asc";
    return super.createQuery(hql).setParameter("lastId", lastId).setMaxResults(batchSize).list();
  }

  @SuppressWarnings("unchecked")
  public List<Long> getPubIdListByPsnId(Long psnId) {
    String hql = "select t.id from Publication t where t.status = 0 and t.articleType = 1 and t.psnId = :psnId";
    return super.createQuery(hql).setParameter("psnId", psnId).list();
  }

}
