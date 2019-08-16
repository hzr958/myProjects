package com.smate.center.batch.dao.sns.pub;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.smate.center.batch.exception.pub.DaoException;
import com.smate.center.batch.model.sns.pub.NsfcPrpPub;
import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.core.base.utils.security.SecurityUtils;

/**
 * 
 * 杰青论著Dao.
 * 
 * @author oyh
 * 
 */
@Repository
public class NsfcPrpPubDao extends SnsHibernateDao<NsfcPrpPub, Long> {

  @SuppressWarnings("unchecked")
  public List<NsfcPrpPub> loadPrpPubsByGuid(String guid) throws DaoException {

    String hql =
        "select t from NsfcPrpPub t where t.pubId is not null and exists(select p.isisGuid from NsfcProposal p where p.isisGuid = t.isisGuid and p.isisGuid = ? and p.prpPsnId = ?) order by t.seqNo,pType,t.pubYear desc,t.pubMonth desc,t.pubDay desc";
    return super.createQuery(hql, new Object[] {guid, SecurityUtils.getCurrentUserId()}).list();
  }

  @SuppressWarnings("unchecked")
  public List<NsfcPrpPub> loadPrpPubsOrderByType(String guid) throws DaoException {

    String hql =
        "select t from NsfcPrpPub t where t.pubId is not null and exists(select p.isisGuid from NsfcProposal p where p.isisGuid = t.isisGuid and p.isisGuid = ? and p.prpPsnId = ?) order by pType,t.seqNo,t.pubYear desc,t.pubMonth desc,t.pubDay desc";
    return super.createQuery(hql, new Object[] {guid, SecurityUtils.getCurrentUserId()}).list();
  }

  @SuppressWarnings("unchecked")
  public Set<Long> getPubIdsByGuid(String isisGuid) throws DaoException {

    String hql =
        "select t.pubId from NsfcPrpPub t where t.pubId is not null and exists(select p.isisGuid from NsfcProposal p where p.isisGuid = t.isisGuid and p.isisGuid = ? and p.prpPsnId = ?) ";
    List<Long> queryList = super.createQuery(hql, new Object[] {isisGuid, SecurityUtils.getCurrentUserId()}).list();
    Set retList = new HashSet<Long>();
    if (CollectionUtils.isNotEmpty(queryList)) {
      for (int i = 0; i < queryList.size(); i++) {
        Object obj = (Object) queryList.get(i);
        if (obj != null) {
          Long pubId = Long.valueOf(ObjectUtils.toString(obj));
          retList.add(pubId);
        }
      }
    }
    return retList;

  }

  public void deletePrpPub(Long pubId, String isisGuid) throws DaoException {
    String hql = "delete NsfcPrpPub t where t.isisGuid=? and t.pubId=?  ";
    super.createQuery(hql, new Object[] {isisGuid, pubId}).executeUpdate();

  }

  public void deletePrpPub(String isisGuids) throws DaoException {

    if (StringUtils.isBlank(isisGuids)) {
      return;
    }
    List<String> guidList = new ArrayList<String>();
    for (String guid : isisGuids.split(",")) {
      guidList.add(guid);
    }
    StringBuffer sb = new StringBuffer();
    sb.append("delete NsfcPrpPub t  where t.isisGuid in(:guidList)");
    super.createQuery(sb.toString()).setParameterList("guidList", guidList).executeUpdate();

  }

  @SuppressWarnings("unchecked")
  public List<NsfcPrpPub> getPubsByGuid(String isisGuid, Long psnId) throws DaoException {

    String hql =
        " Select t from NsfcPrpPub t where t.pubId is not null and exists(select p.isisGuid from NsfcProposal p where p.isisGuid = t.isisGuid and p.isisGuid = ? and p.prpPsnId = ?) order by t.seqNo,pType,t.pubYear desc,t.pubMonth desc,t.pubDay desc";
    List<NsfcPrpPub> queryList = super.createQuery(hql, new Object[] {isisGuid, psnId}).list();

    return queryList;

  }

  @SuppressWarnings("unchecked")
  public List<NsfcPrpPub> getPubsOrderByType(String isisGuid, Long psnId) throws DaoException {

    String hql =
        " Select t from NsfcPrpPub t where t.pubId is not null and exists(select p.isisGuid from NsfcProposal p where p.isisGuid = t.isisGuid and p.isisGuid = ? and p.prpPsnId = ?) order by pType,t.seqNo,t.pubYear desc,t.pubMonth desc,t.pubDay desc";
    List<NsfcPrpPub> queryList = super.createQuery(hql, new Object[] {isisGuid, psnId}).list();

    return queryList;

  }

  public NsfcPrpPub getPrpPub(Long pubId, String isisGuid) throws DaoException {

    String hql = "From NsfcPrpPub t where t.isisGuid=? and t.pubId=?";
    return (NsfcPrpPub) super.createQuery(hql, new Object[] {isisGuid, pubId}).uniqueResult();

  }

  @SuppressWarnings("unchecked")
  public Long loadRepPubs(String guid, Long psnId) throws DaoException {
    String hql =
        " select count(t.id)  From NsfcPrpPub t where t.pubId is not null and exists(select p.isisGuid from NsfcProposal p where p.isisGuid = t.isisGuid and p.isisGuid = ? and p.prpPsnId = ?) and pType=0";
    return (Long) super.createQuery(hql, new Object[] {guid, psnId}).uniqueResult();
  }

  public Integer getPubCountByIsisGuid(String isisGuid) {
    String hql = "select count(*) from NsfcPrpPub t where t.isisGuid = ?";
    Long result = this.countHqlResult(hql, isisGuid);
    return result.intValue();
  }

  public Integer getTotalBy(String isisGuid, String pubType) throws DaoException {
    String[] pubTypeArray = StringUtils.split(pubType, ",");
    List<Integer> pubTypeList = new ArrayList<Integer>();
    for (String pubTypeStr : pubTypeArray) {
      pubTypeList.add(Integer.valueOf(pubTypeStr));
    }
    StringBuilder sb = new StringBuilder();
    sb.append("select count(t.id) from NsfcPrpPub t where t.isisGuid=:isisGuid  and t.pubType in (:pubTypeList)");
    Long count = (Long) this.createQuery(sb.toString()).setParameter("isisGuid", isisGuid)
        .setParameterList("pubTypeList", pubTypeList).uniqueResult();
    return count.intValue();
  }

  public void savePrpPubs(List<NsfcPrpPub> prpPubs) throws DaoException {
    for (NsfcPrpPub pub : prpPubs) {
      this.save(pub);
    }
  }

  public List<Long> getPrpPubOrder(String guid) {
    String hql =
        "select t.pubId from NsfcPrpPub t where t.isisGuid=? order by t.seqNo,t.pubYear desc,t.pubMonth desc,t.pubDay desc";
    return this.createQuery(hql, new Object[] {guid}).list();
  }

  public void updatePrpPubOrder(Integer seqNo, Long pubId, String isisGuid) {
    String hql = "update NsfcPrpPub t set t.seqNo=?  where t.isisGuid=? and t.pubId=?";
    this.createQuery(hql, new Object[] {seqNo, isisGuid, pubId}).executeUpdate();
  }

  public Integer getPtypeTotalBy(String isisGuid, String pType) throws DaoException {

    String[] pTypeArray = StringUtils.split(pType, ",");
    List<Integer> ptypeList = new ArrayList<Integer>();
    for (String ptypeStr : pTypeArray) {
      ptypeList.add(Integer.valueOf(ptypeStr));
    }
    StringBuilder sb = new StringBuilder();
    sb.append("select count(t.id) from NsfcPrpPub t where t.isisGuid=:isisGuid  and t.treatiseType in (:ptypeList)");
    Long count = (Long) this.createQuery(sb.toString()).setParameter("isisGuid", isisGuid)
        .setParameterList("ptypeList", ptypeList).uniqueResult();
    return count.intValue();
  }

  public List<NsfcPrpPub> findNsfcPrpPubByPubId(Long pubId) throws DaoException {
    String hql = "from NsfcPrpPub t where t.pubId = ?";
    return super.createQuery(hql, pubId).list();
  }

  // 更新序号
  public Integer updatePubSeq(String isisGuid, Integer pType) {
    Integer seq = 1;
    String hqlLast = "select max(t.seqNo) from NsfcPrpPub t where t.isisGuid=? and t.treatiseType <= ?";
    Integer maxSeq = (Integer) this.createQuery(hqlLast, isisGuid, pType).uniqueResult();
    if (maxSeq != null) {
      seq = maxSeq.intValue() + 1;
    }
    String hql = "update NsfcPrpPub t set t.seqNo=t.seqNo + 1  where t.isisGuid=? and t.treatiseType > ?";
    this.createQuery(hql, isisGuid, pType).executeUpdate();
    return seq;
  }

  public List<NsfcPrpPub> findPubsByOrder(String isisGuid, String order) throws DaoException {

    String hql = " From NsfcPrpPub t where t.isisGuid=? order by " + order;
    List<NsfcPrpPub> queryList = super.createQuery(hql, new Object[] {isisGuid}).list();

    return queryList;

  }

  public List<NsfcPrpPub> findPrpPub(Long id, int size) throws DaoException {
    String hql = "from NsfcPrpPub t where t.id > ? order by t.id";
    Query query = super.createQuery(hql, id);
    query.setMaxResults(size);
    return query.list();
  }

}
