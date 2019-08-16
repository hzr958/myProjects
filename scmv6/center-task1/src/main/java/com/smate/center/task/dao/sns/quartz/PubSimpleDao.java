package com.smate.center.task.dao.sns.quartz;

import java.util.Date;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Repository;

import com.smate.center.task.model.sns.quartz.PubSimple;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * V_PUB_SIMPLE表实体Dao
 * 
 * @author hzr
 * 
 */
@Repository
public class PubSimpleDao extends SnsHibernateDao<PubSimple, Long> {

  public Date getPubUpdateDate(Long pubId) {
    String hql = "select t.updateDate from PubSimple t where t.pubId=:pubId ";
    return (Date) super.createQuery(hql).setParameter("pubId", pubId).uniqueResult();

  }

  /**
   * 获取该人员分组成果的最后更新时间
   * 
   * @param psnId
   * @param dupPubIds
   * @return
   */
  @SuppressWarnings("unchecked")
  public Date getPubNewestUpdateDate(Long psnId, List<Long> dupPubIds) {
    String hql =
        "select t.updateDate from PubSimple t where t.ownerPsnId=:psnId and  t.pubId in(:dupPubIds) order by t.updateDate desc";
    List<Date> list =
        super.createQuery(hql).setParameter("psnId", psnId).setParameterList("dupPubIds", dupPubIds).list();
    if (CollectionUtils.isNotEmpty(list)) {
      return list.get(0);
    }
    return null;
  }

  /**
   * 删除
   *
   * @author LXZ
   * 
   * @since 6.0.1
   * @version 6.0.1
   * @param pubId
   */
  public void deleteData(Long pubId) {
    String hql = "delete from PubSimple where pubId=?";
    super.createQuery(hql, pubId).executeUpdate();
  }

  @SuppressWarnings({"unchecked"})
  public List<Long> getPublicationFromSns(Integer size, Long startPubId, Long endPubId) {

    String hql =
        "select t.id from Publication t where t.status in (0,3,4,5) and t.id > ? and t.id <= ? and not exists (select 1 from PubSimple ps where ps.pubId = t.id) and not exists (select 1 from PubToPubSimpleErrorLog pl where pl.pubId = t.id) order by t.id asc";

    List<Long> pubList = super.createQuery(hql, startPubId, endPubId).setMaxResults(size).list();

    return pubList;
  }

  @SuppressWarnings("unchecked")
  public List<Long> getPubIdsByPsnId(Long psnId) {
    String hql = "select t.pubId from PubSimple t where t.ownerPsnId=:psnId";
    return super.createQuery(hql).setParameter("psnId", psnId).list();
  }

  @SuppressWarnings("unchecked")
  public List<Long> getPubSimpleIds(Integer size, Long startPubId, Long endPubId) {
    String hql =
        "select t.pubId from PubSimple t where t.pubId >:startPubId and t.pubId<=:endPubId order by t.pubId asc";
    return super.createQuery(hql).setParameter("startPubId", startPubId).setParameter("endPubId", endPubId)
        .setMaxResults(size).list();
  }

  @SuppressWarnings("unchecked")
  public Long getPubOwnerPsnId(Long pubId) {
    String hql = "select t.ownerPsnId from PubSimple t where t.pubId=:pubId ";
    return (Long) super.createQuery(hql).setParameter("pubId", pubId).uniqueResult();
  }

  @SuppressWarnings("unchecked")
  public List<PubSimple> getPubByPsnId(Long psnId) {
    String hql =
        "select new PubSimple(pubId, pubType, updateDate, publishYear, zhTitle, enTitle) from PubSimple t where t.ownerPsnId=:psnId and t.articleType=1 and t.status=0";
    return super.createQuery(hql).setParameter("psnId", psnId).list();

  }

  public void updateBrief(String zhBrief, String enBrief, Long pubId) {
    String hql = "update PubSimple set briefDesc=:zhBrief,briefDescEn=:enBrief where pubId=:pubId";
    super.createQuery(hql).setParameter("zhBrief", zhBrief).setParameter("enBrief", enBrief)
        .setParameter("pubId", pubId).executeUpdate();
  }

  /**
   * 获取个人所有成果的最后更新时间
   * 
   * @param psnId
   * @param dupPubIds
   * @return
   */
  @SuppressWarnings("unchecked")
  public Date getMyPubNewestUpdateDate(Long psnId) {
    String hql =
        "select t.updateDate from PubSimple t where t.ownerPsnId=:psnId  order by t.updateDate desc nulls last";
    List<Date> list = super.createQuery(hql).setParameter("psnId", psnId).list();
    if (CollectionUtils.isNotEmpty(list)) {
      return list.get(0);
    }
    return null;
  }

  /**
   * 
   * @param size
   */
  public List<Long> findPubId(Integer size) {

    String hql = "select t.pubId from PubSimple t where";

    return null;
  }
}
