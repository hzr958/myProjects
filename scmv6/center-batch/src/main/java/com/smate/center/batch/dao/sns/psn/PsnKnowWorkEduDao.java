package com.smate.center.batch.dao.sns.psn;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import com.smate.center.batch.exception.pub.DaoException;
import com.smate.center.batch.model.psn.PsnKnowWorkEdu;
import com.smate.center.batch.model.sns.pub.FriendTempSys;
import com.smate.center.batch.util.pub.FsysPage;
import com.smate.core.base.psn.model.PsnEdu;
import com.smate.core.base.psn.model.PsnWork;
import com.smate.core.base.utils.common.HqlUtils;
import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.core.base.utils.model.Page;
import com.smate.core.base.utils.security.SecurityUtils;

/**
 * @author lcw
 * 
 */
@Repository
@SuppressWarnings("unchecked")
public class PsnKnowWorkEduDao extends SnsHibernateDao<PsnKnowWorkEdu, Long> {

  public List<Long> filterDefPsnIds(List<Long> psnIds) {
    String hql = "select t.personId from PersonKnow t where t.isPrivate=0 and t.isAddFrd=0 and t.personId in(:psnIds)";
    return createQuery(hql).setParameterList("psnIds", psnIds).list();
  }

  public List<Long> findWorkInsIdsByPsnId(Long psnId) {
    String hql = "select t.insId from PsnWork t where t.psnId=? and t.insId is not null order by t.insId asc";
    return createQuery(hql, psnId).list();
  }

  public List<PsnWork> findWorkByPsnId(Long psnId) {
    String hql = "from PsnWork t where t.psnId=?";
    return createQuery(hql, psnId).list();
  }

  public List<Long> findEduInsIdsByPsnId(Long psnId) {
    String hql = "select t.insId from PsnEdu t where t.psnId=? and t.insId is not null order by t.insId asc";
    return createQuery(hql, psnId).list();
  }

  public List<PsnEdu> findEduByPsnId(Long psnId) {
    String hql = "from PsnEdu t where t.psnId=?";
    return createQuery(hql, psnId).list();
  }

  public List<PsnKnowWorkEdu> getPsnKnowWorkEdu(Long psnId) throws DaoException {
    return super.createQuery("from PsnKnowWorkEdu where psnId=?", psnId).list();
  }

  public void delPsnKnowWorkEdu(Long psnId) {
    createQuery("delete from PsnKnowWorkEdu where psnId=?", psnId).executeUpdate();
  }

  public void deleteAll() {
    super.update("truncate table psn_know_work_edu");
  }

  public List<FriendTempSys> searchKnowByPsnIns(String sortWorkInsIds, String sortEduInsIds, Long lastPsnId) {
    Long psnId = SecurityUtils.getCurrentUserId();
    String hql =
        "select distinct new FriendTempSys(t.psnId,t.name,t.headUrl,t.viewTitel,t.firstName,t.lastName) from PsnKnowWorkEdu t where t.psnId>:lastPsnId and t.psnId not in(select t4.friendPsnId from Friend t4 where t4.psnId=:psnId2) and t.psnId not in(select t5.tempPsnId from FriendTempSys t5 where t5.psnId=:psnId3) and t.psnId not in(:psnId4)";
    hql = hqlInsIdsRes(sortWorkInsIds, sortEduInsIds, hql);
    hql += getPsnWorkEduOrder(sortWorkInsIds, sortEduInsIds);

    Query q = super.createQuery(hql);
    q.setParameter("lastPsnId", lastPsnId);
    q.setParameter("psnId2", psnId);
    q.setParameter("psnId3", psnId);
    q.setParameter("psnId4", psnId);
    return q.setMaxResults(24).list();
  }

  @SuppressWarnings("rawtypes")
  public List<Long> searchKnowPsnIdsByPsnIns(String sortWorkInsIds, String sortEduInsIds, FsysPage page) {
    Long psnId = SecurityUtils.getCurrentUserId();
    String hql =
        "select t.psnId from PsnKnowWorkEdu t where t.psnId not in(select t4.friendPsnId from Friend t4 where t4.psnId=?) and t.psnId not in(select t5.tempPsnId from FriendTempSys t5 where t5.psnId=?) and t.psnId not in(select t7.tempPsnId from FriendTemp t7 where t7.psnId=? and t7.tempPsnId is not null) and t.psnId not in(?)";
    hql = hqlInsIdsRes(sortWorkInsIds, sortEduInsIds, hql);
    hql += getPsnWorkEduOrder(sortWorkInsIds, sortEduInsIds);
    Query q = createQuery(hql, psnId, psnId, psnId, psnId);
    if (page.isAutoCount()) {
      long totalCount = countHqlResult(hql, psnId, psnId, psnId, psnId);
      page.setTotalCount(totalCount);
    }
    q.setFirstResult(page.getFirst() - 1);
    q.setMaxResults(page.getPageSize());
    return q.list();
  }

  @SuppressWarnings("rawtypes")
  public FsysPage<FriendTempSys> searchKnowByPsnIns(String sortWorkInsIds, String sortEduInsIds, FsysPage page) {
    Long psnId = SecurityUtils.getCurrentUserId();
    String hql =
        "from PsnKnowWorkEdu t where t.psnId not in(select t4.friendPsnId from Friend t4 where t4.psnId=?) and t.psnId not in(select t5.tempPsnId from FriendTempSys t5 where t5.psnId=?) and t.psnId not in(select t7.tempPsnId from FriendTemp t7 where t7.psnId=? and t7.tempPsnId is not null) and t.psnId not in(?)";
    hql = hqlInsIdsRes(sortWorkInsIds, sortEduInsIds, hql);
    hql += getPsnWorkEduOrder(sortWorkInsIds, sortEduInsIds);
    FsysPage<FriendTempSys> pageNew = new FsysPage<FriendTempSys>(page.getPageSize());
    Query q = createQuery(hql, psnId, psnId, psnId, psnId);
    if (page.isAutoCount()) {
      long totalCount = countHqlResult(hql, psnId, psnId, psnId, psnId);
      page.setTotalCount(totalCount);
    }
    q.setFirstResult(page.getFirst() - 1);
    q.setMaxResults(page.getPageSize());
    List result = q.list();
    page.setResult(result);

    List<PsnKnowWorkEdu> list = page.getResult();
    if (!CollectionUtils.isEmpty(list)) {
      pageNew.setTotalCount(page.getTotalCount());
      List<FriendTempSys> listNew = new ArrayList<FriendTempSys>();
      for (PsnKnowWorkEdu psnKnowWorkEdu : list) {
        FriendTempSys fts = new FriendTempSys();
        fts.setTempPsnId(psnKnowWorkEdu.getPsnId());
        fts.setTempPsnName(psnKnowWorkEdu.getName());
        fts.setTempPsnHeadUrl(psnKnowWorkEdu.getHeadUrl());
        fts.setTempPsnTitel(psnKnowWorkEdu.getViewTitel());
        fts.setTempPsnFirstName(psnKnowWorkEdu.getFirstName());
        fts.setTempPsnLastName(psnKnowWorkEdu.getLastName());
        listNew.add(fts);
      }
      pageNew.setResult(listNew);
    }
    return pageNew;
  }

  @SuppressWarnings("rawtypes")
  public List<Long> searchKnowPsnIdsBySearchName(String sortWorkInsIds, String sortEduInsIds, String searchName,
      FsysPage page) {
    Long psnId = SecurityUtils.getCurrentUserId();
    String hql =
        "select psnId from PsnKnowWorkEdu t where t.psnId not in(select t4.friendPsnId from Friend t4 where t4.psnId=?) and t.psnId not in(select t5.tempPsnId from FriendTempSys t5 where t5.psnId=?) and t.psnId not in(select t7.tempPsnId from FriendTemp t7 where t7.psnId=? and t7.tempPsnId is not null) and t.psnId not in(?) and (lower(t.name) like ? or lower(replace(t.firstName,' ','')) like ? or lower(replace(t.lastName,' ','')) like ?)";
    hql = hqlInsIdsRes(sortWorkInsIds, sortEduInsIds, hql);
    hql += getPsnWorkEduOrder(sortWorkInsIds, sortEduInsIds);
    Query q = createQuery(hql, psnId, psnId, psnId, psnId, searchName, searchName.replace(" ", ""),
        searchName.replace(" ", ""));
    if (page.isAutoCount()) {
      long totalCount = countHqlResult(hql, psnId, psnId, psnId, psnId, searchName, searchName.replace(" ", ""),
          searchName.replace(" ", ""));
      page.setTotalCount(totalCount);
    }
    q.setFirstResult(page.getFirst() - 1);
    q.setMaxResults(page.getPageSize());
    return q.list();
  }

  @SuppressWarnings("rawtypes")
  public FsysPage<FriendTempSys> searchKnowBySearchName(String sortWorkInsIds, String sortEduInsIds, String searchName,
      FsysPage page) {
    Long psnId = SecurityUtils.getCurrentUserId();
    String hql =
        "from PsnKnowWorkEdu t where t.psnId not in(select t4.friendPsnId from Friend t4 where t4.psnId=?) and t.psnId not in(select t5.tempPsnId from FriendTempSys t5 where t5.psnId=?) and t.psnId not in(select t7.tempPsnId from FriendTemp t7 where t7.psnId=? and t7.tempPsnId is not null) and t.psnId not in(?) and (lower(t.name) like ? or lower(replace(t.lastName||t.firstName,' ','')) like ? or lower(replace(t.firstName||t.lastName,' ','')) like ?)";
    hql = hqlInsIdsRes(sortWorkInsIds, sortEduInsIds, hql);
    hql += getPsnWorkEduOrder(sortWorkInsIds, sortEduInsIds);
    FsysPage<FriendTempSys> pageNew = new FsysPage<FriendTempSys>(page.getPageSize());
    Query q = createQuery(hql, psnId, psnId, psnId, psnId, searchName, searchName.replace(" ", ""),
        searchName.replace(" ", ""));
    if (page.isAutoCount()) {
      long totalCount = countHqlResult(hql, psnId, psnId, psnId, psnId, searchName, searchName.replace(" ", ""),
          searchName.replace(" ", ""));
      page.setTotalCount(totalCount);
    }
    q.setFirstResult(page.getFirst() - 1);
    q.setMaxResults(page.getPageSize());
    List result = q.list();
    page.setResult(result);
    List<PsnKnowWorkEdu> list = page.getResult();
    if (!CollectionUtils.isEmpty(list)) {
      pageNew.setTotalCount(page.getTotalCount());
      List<FriendTempSys> listNew = new ArrayList<FriendTempSys>();
      for (PsnKnowWorkEdu psnKnowWorkEdu : list) {
        FriendTempSys fts = new FriendTempSys();
        fts.setTempPsnId(psnKnowWorkEdu.getPsnId());
        fts.setTempPsnName(psnKnowWorkEdu.getName());
        fts.setTempPsnHeadUrl(psnKnowWorkEdu.getHeadUrl());
        fts.setTempPsnTitel(psnKnowWorkEdu.getViewTitel());
        fts.setTempPsnFirstName(psnKnowWorkEdu.getFirstName());
        fts.setTempPsnLastName(psnKnowWorkEdu.getLastName());
        listNew.add(fts);
      }
      pageNew.setResult(listNew);
    }
    return pageNew;
  }

  public List<FriendTempSys> searchKnowByInsPartners(String sortWorkInsIds, String sortEduInsIds, String pubPartners,
      String prjPartners, Long lastPsnId) {
    Long psnId = SecurityUtils.getCurrentUserId();
    pubPartners = StringUtils.trimToEmpty(pubPartners);
    prjPartners = StringUtils.trimToEmpty(prjPartners);
    String hql =
        "select new FriendTempSys(t.psnId,t.name,t.headUrl,t.viewTitel,t.firstName,t.lastName) from PsnKnowWorkEdu t,PsnKnowCopartner t2 where t.psnId=t2.cptPsnId and t.psnId>:lastPsnId and t2.psnId=:cptPsnId and (t2.cptTypes like:pubType or t2.cptTypes like:prjType) and t.psnId not in(select t4.friendPsnId from Friend t4 where t4.psnId=:psnId2) and t.psnId not in(select t5.tempPsnId from FriendTempSys t5 where t5.psnId=:psnId3) and t.psnId not in(:psnId4)";
    hql = hqlInsIdsRes(sortWorkInsIds, sortEduInsIds, hql);
    hql += getPsnWorkEduOrder(sortWorkInsIds, sortEduInsIds);

    Query q = super.createQuery(hql);
    q.setParameter("lastPsnId", lastPsnId);
    q.setParameter("cptPsnId", psnId);
    q.setParameter("pubType", pubPartners);
    q.setParameter("prjType", prjPartners);
    q.setParameter("psnId2", psnId);
    q.setParameter("psnId3", psnId);
    q.setParameter("psnId4", psnId);
    return q.setMaxResults(24).list();
  }

  /**
   * 用于psnHtml_zk
   * 
   * @param sortWorkInsIds
   * @param sortEduInsIds
   * @param pubPartners
   * @param prjPartners
   * @param page
   * @return
   */
  @SuppressWarnings("rawtypes")
  public List<Long> searchKnowPsnIdsByInsPartners(String sortWorkInsIds, String sortEduInsIds, String pubPartners,
      String prjPartners, FsysPage page) {
    Long psnId = SecurityUtils.getCurrentUserId();
    pubPartners = StringUtils.trimToEmpty(pubPartners);
    prjPartners = StringUtils.trimToEmpty(prjPartners);
    String hql =
        "select t.psnId from PsnKnowWorkEdu t,PsnKnowCopartner t2 where t.psnId=t2.cptPsnId and t2.psnId=:cptPsnId and (t2.cptTypes like:pubType or t2.cptTypes like:prjType) and t.psnId not in(select t4.friendPsnId from Friend t4 where t4.psnId=:psnId2) and t.psnId not in(select t5.tempPsnId from FriendTempSys t5 where t5.psnId=:psnId3) and t.psnId not in(select t7.tempPsnId from FriendTemp t7 where t7.psnId=:psnId7 and t7.tempPsnId is not null) and t.psnId not in(:psnId4)";
    hql = hqlInsIdsRes(sortWorkInsIds, sortEduInsIds, hql);
    hql += getPsnWorkEduOrder(sortWorkInsIds, sortEduInsIds);

    Query q = super.createQuery(hql);
    q.setParameter("cptPsnId", psnId);
    q.setParameter("pubType", pubPartners);
    q.setParameter("prjType", prjPartners);
    q.setParameter("psnId2", psnId);
    q.setParameter("psnId3", psnId);
    q.setParameter("psnId4", psnId);
    q.setParameter("psnId7", psnId);
    if (page.isAutoCount()) {
      Long count = 0L;
      String fromHql = hql;
      fromHql = "from " + StringUtils.substringAfter(fromHql, "from");
      fromHql = StringUtils.substringBefore(fromHql, "order by");
      String countHql = "select count(t.psnId) " + fromHql;
      try {
        Query qcount = super.createQuery(countHql);
        qcount.setParameter("cptPsnId", psnId);
        qcount.setParameter("pubType", pubPartners);
        qcount.setParameter("prjType", prjPartners);
        qcount.setParameter("psnId2", psnId);
        qcount.setParameter("psnId3", psnId);
        qcount.setParameter("psnId4", psnId);
        qcount.setParameter("psnId7", psnId);
        count = (Long) qcount.uniqueResult();
      } catch (Exception e) {
        throw new RuntimeException("hql can't be auto count, hql is:" + countHql, e);
      }
      page.setTotalCount(count);
    }
    q.setFirstResult(page.getFirst() - 1);
    q.setMaxResults(page.getPageSize());
    return q.list();
  }

  @SuppressWarnings("rawtypes")
  public FsysPage<FriendTempSys> searchKnowByInsPartners(String sortWorkInsIds, String sortEduInsIds,
      String pubPartners, String prjPartners, FsysPage page) {
    List<FriendTempSys> newList = new ArrayList<FriendTempSys>();
    Long psnId = SecurityUtils.getCurrentUserId();
    pubPartners = StringUtils.trimToEmpty(pubPartners);
    prjPartners = StringUtils.trimToEmpty(prjPartners);
    String hql =
        "select new FriendTempSys(t.psnId,t.name,t.headUrl,t.viewTitel,t.firstName,t.lastName,t2.cptTypes) from PsnKnowWorkEdu t,PsnKnowCopartner t2 where t.psnId=t2.cptPsnId and t2.psnId=:cptPsnId and (t2.cptTypes like:pubType or t2.cptTypes like:prjType) and t.psnId not in(select t4.friendPsnId from Friend t4 where t4.psnId=:psnId2) and t.psnId not in(select t5.tempPsnId from FriendTempSys t5 where t5.psnId=:psnId3) and t.psnId not in(select t7.tempPsnId from FriendTemp t7 where t7.psnId=:psnId7 and t7.tempPsnId is not null) and t.psnId not in(:psnId4)";
    hql = hqlInsIdsRes(sortWorkInsIds, sortEduInsIds, hql);
    hql += getPsnWorkEduOrder(sortWorkInsIds, sortEduInsIds);

    Query q = super.createQuery(hql);
    q.setParameter("cptPsnId", psnId);
    q.setParameter("pubType", pubPartners);
    q.setParameter("prjType", prjPartners);
    q.setParameter("psnId2", psnId);
    q.setParameter("psnId3", psnId);
    q.setParameter("psnId4", psnId);
    q.setParameter("psnId7", psnId);
    if (page.isAutoCount()) {
      Long count = 0L;
      String fromHql = hql;
      fromHql = "from " + StringUtils.substringAfter(fromHql, "from");
      fromHql = StringUtils.substringBefore(fromHql, "order by");
      String countHql = "select count(t.psnId) " + fromHql;
      try {
        Query qcount = super.createQuery(countHql);
        qcount.setParameter("cptPsnId", psnId);
        qcount.setParameter("pubType", pubPartners);
        qcount.setParameter("prjType", prjPartners);
        qcount.setParameter("psnId2", psnId);
        qcount.setParameter("psnId3", psnId);
        qcount.setParameter("psnId4", psnId);
        qcount.setParameter("psnId7", psnId);
        count = (Long) qcount.uniqueResult();
      } catch (Exception e) {
        throw new RuntimeException("hql can't be auto count, hql is:" + countHql, e);
      }
      page.setTotalCount(count);
    }
    q.setFirstResult(page.getFirst() - 1);
    q.setMaxResults(page.getPageSize());

    List<FriendTempSys> list = q.list();
    if (!CollectionUtils.isEmpty(list)) {
      for (FriendTempSys sys : list) {
        sys.setIscpt(true);
        newList.add(sys);
      }
    }

    page.setResult(newList);
    return page;
  }

  /**
   * 用于psnHtml_zk
   * 
   * @param sortWorkInsIds
   * @param sortEduInsIds
   * @param pubPartners
   * @param prjPartners
   * @param searchName
   * @param page
   * @return
   */
  @SuppressWarnings("rawtypes")
  public List<Long> searchKnowPsnIdsByInsPartnersSearchName(String sortWorkInsIds, String sortEduInsIds,
      String pubPartners, String prjPartners, String searchName, FsysPage page) {
    Long psnId = SecurityUtils.getCurrentUserId();
    pubPartners = StringUtils.trimToEmpty(pubPartners);
    prjPartners = StringUtils.trimToEmpty(prjPartners);
    String hql =
        "select t.psnId from PsnKnowWorkEdu t,PsnKnowCopartner t2 where t.psnId=t2.cptPsnId and t2.psnId=:cptPsnId and (t2.cptTypes like:pubType or t2.cptTypes like:prjType) and t.psnId not in(select t4.friendPsnId from Friend t4 where t4.psnId=:psnId2) and t.psnId not in(select t5.tempPsnId from FriendTempSys t5 where t5.psnId=:psnId3) and t.psnId not in(select t7.tempPsnId from FriendTemp t7 where t7.psnId=:psnId7 and t7.tempPsnId is not null) and t.psnId not in(:psnId4) "
            + "and (t.name like :searchName1 or lower(replace(t.lastName|| t.firstName,' ','')) like :searchName2)";
    hql = hqlInsIdsRes(sortWorkInsIds, sortEduInsIds, hql);
    hql += getPsnWorkEduOrder(sortWorkInsIds, sortEduInsIds);

    Query q = super.createQuery(hql);
    q.setParameter("cptPsnId", psnId);
    q.setParameter("pubType", pubPartners);
    q.setParameter("prjType", prjPartners);
    q.setParameter("psnId2", psnId);
    q.setParameter("psnId3", psnId);
    q.setParameter("psnId4", psnId);
    q.setParameter("psnId7", psnId);
    q.setParameter("searchName1", searchName);
    q.setParameter("searchName2", searchName.replace(" ", ""));
    if (page.isAutoCount()) {
      Long count = 0L;
      String fromHql = hql;
      fromHql = "from " + StringUtils.substringAfter(fromHql, "from");
      fromHql = StringUtils.substringBefore(fromHql, "order by");
      String countHql = "select count(*) " + fromHql;
      try {
        Query qcount = super.createQuery(countHql);
        qcount.setParameter("cptPsnId", psnId);
        qcount.setParameter("pubType", pubPartners);
        qcount.setParameter("prjType", prjPartners);
        qcount.setParameter("psnId2", psnId);
        qcount.setParameter("psnId3", psnId);
        qcount.setParameter("psnId4", psnId);
        qcount.setParameter("psnId7", psnId);
        qcount.setParameter("searchName1", searchName);
        qcount.setParameter("searchName2", searchName.replace(" ", ""));
        count = (Long) qcount.uniqueResult();
      } catch (Exception e) {
        throw new RuntimeException("hql can't be auto count, hql is:" + countHql, e);
      }
      page.setTotalCount(count);
    }
    q.setFirstResult(page.getFirst() - 1);
    q.setMaxResults(page.getPageSize());
    return q.list();
  }

  @SuppressWarnings("rawtypes")
  public FsysPage<FriendTempSys> searchKnowByInsPartnersSearchName(String sortWorkInsIds, String sortEduInsIds,
      String pubPartners, String prjPartners, String searchName, FsysPage page) {
    Long psnId = SecurityUtils.getCurrentUserId();
    pubPartners = StringUtils.trimToEmpty(pubPartners);
    prjPartners = StringUtils.trimToEmpty(prjPartners);
    String hql =
        "select new FriendTempSys(t.psnId,t.name,t.headUrl,t.viewTitel,t.firstName,t.lastName,t2.cptTypes) from PsnKnowWorkEdu t,PsnKnowCopartner t2 where t.psnId=t2.cptPsnId and t2.psnId=:cptPsnId and (t2.cptTypes like:pubType or t2.cptTypes like:prjType) and t.psnId not in(select t4.friendPsnId from Friend t4 where t4.psnId=:psnId2) and t.psnId not in(select t5.tempPsnId from FriendTempSys t5 where t5.psnId=:psnId3) and t.psnId not in(select t7.tempPsnId from FriendTemp t7 where t7.psnId=:psnId7 and t7.tempPsnId is not null) and t.psnId not in(:psnId4) "
            + "and (t.name like :searchName1 or lower(replace(t.lastName|| t.firstName,' ','')) like :searchName2)";
    hql = hqlInsIdsRes(sortWorkInsIds, sortEduInsIds, hql);
    hql += getPsnWorkEduOrder(sortWorkInsIds, sortEduInsIds);

    Query q = super.createQuery(hql);
    q.setParameter("cptPsnId", psnId);
    q.setParameter("pubType", pubPartners);
    q.setParameter("prjType", prjPartners);
    q.setParameter("psnId2", psnId);
    q.setParameter("psnId3", psnId);
    q.setParameter("psnId4", psnId);
    q.setParameter("psnId7", psnId);
    q.setParameter("searchName1", searchName);
    q.setParameter("searchName2", searchName.replace(" ", ""));
    if (page.isAutoCount()) {
      Long count = 0L;
      String fromHql = hql;
      fromHql = "from " + StringUtils.substringAfter(fromHql, "from");
      fromHql = StringUtils.substringBefore(fromHql, "order by");
      String countHql = "select count(*) " + fromHql;
      try {
        Query qcount = super.createQuery(countHql);
        qcount.setParameter("cptPsnId", psnId);
        qcount.setParameter("pubType", pubPartners);
        qcount.setParameter("prjType", prjPartners);
        qcount.setParameter("psnId2", psnId);
        qcount.setParameter("psnId3", psnId);
        qcount.setParameter("psnId4", psnId);
        qcount.setParameter("psnId7", psnId);
        qcount.setParameter("searchName1", searchName);
        qcount.setParameter("searchName2", searchName.replace(" ", ""));
        count = (Long) qcount.uniqueResult();
      } catch (Exception e) {
        throw new RuntimeException("hql can't be auto count, hql is:" + countHql, e);
      }
      page.setTotalCount(count);
    }
    q.setFirstResult(page.getFirst() - 1);
    q.setMaxResults(page.getPageSize());
    List<FriendTempSys> list = q.list();
    if (!CollectionUtils.isEmpty(list)) {
      for (FriendTempSys fts : list) {
        fts.setIscpt(true);
      }
    }
    page.setResult(list);
    return page;
  }

  private static String getPsnWorkEduOrder(String sortWorkInsIds, String sortEduInsIds) {
    String order = " order by decode(t.primaryInsId,";
    List<Long> orderList = new ArrayList<Long>();
    if (StringUtils.isNotBlank(sortWorkInsIds)) {
      String[] ids = sortWorkInsIds.split(",");
      for (String insId : ids) {
        if (StringUtils.isNotBlank(insId) && NumberUtils.isNumber(insId))
          orderList.add(Long.valueOf(insId));
      }
    }
    if (StringUtils.isNotBlank(sortEduInsIds)) {
      String[] ids = sortEduInsIds.split(",");
      for (String insId : ids) {
        if (StringUtils.isNotBlank(insId) && NumberUtils.isNumber(insId))
          orderList.add(Long.valueOf(insId));
      }
    }
    orderList = HqlUtils.removeDuplicateWithOrder(orderList);
    for (int i = 0; i < orderList.size(); i++) {
      order += orderList.get(i) + ",-" + (orderList.size() - i) + ",";
    }
    order += "null,99999, t.primaryInsId) asc,t.psnId asc";
    return order;
  }

  private String hqlInsIdsRes(String sortWorkInsIds, String sortEduInsIds, String hql) {
    if (StringUtils.isNotBlank(sortWorkInsIds)) {
      hql += " and (";
      String[] ids = sortWorkInsIds.split(",");
      for (int i = 0; i < ids.length; i++) {
        if (StringUtils.isBlank(ids[i]))
          continue;
        try {
          Long.parseLong(ids[i]);
          hql += "t.workInsIds like '%," + ids[i] + ",%' ";
        } catch (Exception e) {
          hql += "t.workInsNames like '%," + ids[i] + ",%' ";
        }
        if (i < ids.length - 1)
          hql += " or ";
      }
      hql += ")";
    }
    if (StringUtils.isNotBlank(sortEduInsIds)) {
      hql += " and (";
      String[] ids = sortEduInsIds.split(",");
      for (int i = 0; i < ids.length; i++) {
        if (StringUtils.isBlank(ids[i]))
          continue;
        try {
          Long.parseLong(ids[i]);
          hql += "t.eduInsIds like '%," + ids[i] + ",%' ";
        } catch (Exception e) {
          hql += "t.eduInsNames like '%," + ids[i] + ",%' ";
        }
        if (i < ids.length - 1)
          hql += " or ";
      }
      hql += ")";
    }
    return hql;
  }

  public List<FriendTempSys> searchKnowByPartners(String pubPartners, String prjPartners, Long lastPsnId) {
    Long psnId = SecurityUtils.getCurrentUserId();
    String hql =
        "select distinct new FriendTempSys(t.cptPsnId,t.cptName,t.cptHeadUrl,t.cptViewTitel,t.cptFirstName,t.cptLastName) from PsnKnowCopartner t where t.psnId=? and t.cptPsnId>? and (t.cptTypes like ? or t.cptTypes like ?) and t.cptPsnId not in(select t5.tempPsnId from FriendTempSys t5 where t5.psnId=?) and t.cptPsnId not in(select t6.tempPsnId from FriendTemp t6 where t6.psnId=? and t6.tempPsnId is not null) order by t.cptPsnId asc";
    return super.createQuery(hql, psnId, lastPsnId, pubPartners, prjPartners, psnId, psnId).setMaxResults(24).list();
  }

  public List<FriendTempSys> findPartnersTempKnow(Long psnId, String partnersType, int pageSize) {
    String hql =
        "select distinct new FriendTempSys(t.cptPsnId,t.cptName,t.cptHeadUrl,t.cptViewTitel,t.cptFirstName,t.cptLastName) from PsnKnowCopartner t where t.psnId=? and t.cptTypes like ? and t.cptPsnId not in(select t5.tempPsnId from FriendTempSys t5 where t5.psnId=?) and t.cptPsnId not in(select t6.tempPsnId from FriendTemp t6 where t6.psnId=? and t6.tempPsnId is not null) order by t.cptPsnId asc";
    return super.createQuery(hql, psnId, "%" + partnersType + "%", psnId, psnId).setMaxResults(pageSize).list();
  }

  public Long findNewColleague(Long psnId, Date regDate) throws DaoException {
    String workInsHql = "from PsnKnowWorkEdu t where t.psnId = ?";
    PsnKnowWorkEdu pwe = (PsnKnowWorkEdu) super.createQuery(workInsHql, psnId).uniqueResult();
    if (pwe != null) {
      String workIns = pwe.getWorkInsIds();
      if (StringUtils.isNotBlank(workIns)) {
        workIns = workIns.substring(1, workIns.length() - 1);
        String hql =
            "select count(*) from PsnKnowWorkEdu t where t.primaryInsId in (:workIns) and t.regDate >= :regDate and t.psnId != :psnId  and t.psnId not in (select t6.tempPsnId from FriendTemp t6 where t6.psnId= :psnId and t6.tempPsnId is not null) and t.psnId not in (select t7.friendPsnId from Friend t7 where t7.psnId = :psnId)";
        return (Long) super.createQuery(hql).setParameterList("workIns", strArrayToLong(workIns))
            .setParameter("regDate", regDate).setParameter("psnId", psnId).uniqueResult();
      }
    }
    return 0L;
  }

  public Page<PsnKnowWorkEdu> findNewColleaguePage(Page<PsnKnowWorkEdu> page, Long psnId, Date regDate)
      throws DaoException {
    String workInsHql = "from PsnKnowWorkEdu t where t.psnId = ?";
    PsnKnowWorkEdu pwe = (PsnKnowWorkEdu) super.createQuery(workInsHql, psnId).uniqueResult();
    if (pwe != null) {
      String workIns = pwe.getWorkInsIds();
      if (StringUtils.isNotBlank(workIns)) {
        workIns = workIns.substring(1, workIns.length() - 1);
        String hql =
            "from PsnKnowWorkEdu t where t.primaryInsId in (:workIns) and t.regDate >= :regDate and t.psnId != :psnId and t.psnId not in (select t6.tempPsnId from FriendTemp t6 where t6.psnId= :psnId and t6.tempPsnId is not null) and t.psnId not in (select t7.friendPsnId from Friend t7 where t7.psnId = :psnId)";
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("workIns", strArrayToLong(workIns));
        parameters.put("regDate", regDate);
        parameters.put("psnId", psnId);
        return super.findPage(page, hql, parameters);
      }
    }
    return page;
  }

  public Long fingNewSchoolmateCount(Long psnId, Date regDate) throws DaoException {
    String workInsHql = "from PsnKnowWorkEdu t where t.psnId = ?";
    PsnKnowWorkEdu pwe = (PsnKnowWorkEdu) super.createQuery(workInsHql, psnId).uniqueResult();
    if (pwe != null) {
      String eduIds = pwe.getEduInsIds();
      if (StringUtils.isNotBlank(eduIds)) {
        StringBuilder sb = new StringBuilder();
        Map<String, Object> parameters = new HashMap<String, Object>();
        sb.append("select count(*) from PsnKnowWorkEdu t where ( 1!=1 ");
        eduIds = eduIds.substring(1, eduIds.length() - 1);
        String[] array = eduIds.split(",");
        int i = 1;
        for (String eduId : array) {
          sb.append("or t.eduInsIds like :eduId" + i + " ");
          parameters.put("eduId" + i, "%," + eduId + ",%");
          i++;
        }
        sb.append(
            ") and t.regDate >= :regDate and t.psnId != :psnId and t.psnId not in (select t6.tempPsnId from FriendTemp t6 where t6.psnId= :psnId and t6.tempPsnId is not null) and t.psnId not in (select t7.friendPsnId from Friend t7 where t7.psnId = :psnId)");
        parameters.put("regDate", regDate);
        parameters.put("psnId", psnId);
        return (Long) super.createQuery(sb.toString(), parameters).uniqueResult();
      }
    }
    return 0L;
  }

  @SuppressWarnings("rawtypes")
  public Page<PsnKnowWorkEdu> fingNewSchoolmatePage(Page page, Long psnId, Date regDate) throws DaoException {
    String workInsHql = "from PsnKnowWorkEdu t where t.psnId = ?";
    PsnKnowWorkEdu pwe = (PsnKnowWorkEdu) super.createQuery(workInsHql, psnId).uniqueResult();
    if (pwe != null) {
      String eduIds = pwe.getEduInsIds();
      if (StringUtils.isNotBlank(eduIds)) {
        StringBuilder sb = new StringBuilder();
        Map<String, Object> parameters = new HashMap<String, Object>();
        sb.append("from PsnKnowWorkEdu t where ( 1!=1 ");
        eduIds = eduIds.substring(1, eduIds.length() - 1);
        String[] array = eduIds.split(",");
        int i = 1;
        for (String eduId : array) {
          sb.append("or t.eduInsIds like :eduId" + i + " ");
          parameters.put("eduId" + i, "%," + eduId + ",%");
          i++;
        }
        sb.append(
            ") and t.regDate >= :regDate and t.psnId != :psnId and t.psnId not in (select t6.tempPsnId from FriendTemp t6 where t6.psnId= :psnId and t6.tempPsnId is not null) and t.psnId not in (select t7.friendPsnId from Friend t7 where t7.psnId = :psnId)");
        parameters.put("regDate", regDate);
        parameters.put("psnId", psnId);
        return super.findPage(page, sb.toString(), parameters);
      }
    }
    return page;
  }

  private Long[] strArrayToLong(String str) {
    if (StringUtils.isNotBlank(str)) {
      String[] strArray = str.split(",");
      Long[] longArray = new Long[strArray.length];
      for (int i = 0; i < strArray.length; i++) {
        longArray[i] = Long.valueOf(strArray[i]);
      }
      return longArray;
    } else {
      return null;
    }
  }

  /**
   * 获取psnid的记录
   */
  public PsnKnowWorkEdu getPsnKnowWorkEduById(Long psnId) {
    return super.findUnique("from PsnKnowWorkEdu p where p.psnId = ?", psnId);
  }

  public List<Long> searchKnowForPsnIdsByPsnIns(String sortWorkInsIds, String sortEduInsIds) throws DaoException {
    Long psnId = SecurityUtils.getCurrentUserId();
    String hql =
        "select t.psnId from PsnKnowWorkEdu t where t.psnId not in(select t4.friendPsnId from Friend t4 where t4.psnId=?) and t.psnId not in(select t5.tempPsnId from FriendTempSys t5 where t5.psnId=?) and t.psnId not in(select t7.tempPsnId from FriendTemp t7 where t7.psnId=? and t7.tempPsnId is not null) and t.psnId not in(?)";
    hql = hqlInsIdsRes(sortWorkInsIds, sortEduInsIds, hql);
    hql += getPsnWorkEduOrder(sortWorkInsIds, sortEduInsIds);
    Query q = createQuery(hql, psnId, psnId, psnId, psnId).setMaxResults(1000);
    return q.list();
  }

  public List<PsnKnowWorkEdu> searchKnowListByPsnIds(List<Long> psnIds) throws DaoException {
    String hql = "from PsnKnowWorkEdu t where t.psnId in(:psnIds)";
    return super.createQuery(hql).setParameterList("psnIds", psnIds).list();
  }
}
