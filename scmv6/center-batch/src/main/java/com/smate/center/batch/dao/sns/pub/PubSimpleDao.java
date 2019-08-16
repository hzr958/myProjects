package com.smate.center.batch.dao.sns.pub;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.sns.pub.PubSimple;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * V_PUB_SIMPLE表实体Dao
 * 
 * @author hzr
 * 
 */
@Repository
public class PubSimpleDao extends SnsHibernateDao<PubSimple, Long> {

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

  /**
   * 修改PubSimple的状态值
   * 
   * @param status
   * @param version
   * @param pubId
   */
  public void updateStatus(Integer status, Long version, Long pubId) {
    String hql =
        "update PubSimple t set t.status=:status, t.simpleVersion=:simpleVersion ,t.updateDate =:updateDate where t.pubId=:pubId";
    super.createQuery(hql).setParameter("status", status).setParameter("simpleVersion", version)
        .setParameter("updateDate", new Date()).setParameter("pubId", pubId).executeUpdate();
  }

  /**
   * 修改PubSimple的状态值
   * 
   * @param status
   * @param pubId
   */
  public void updateStatus(Integer status, Long pubId) {
    String hql = "update PubSimple t set t.status=:status ,t.updateDate =:updateDate where t.pubId=:pubId";
    super.createQuery(hql).setParameter("status", status).setParameter("updateDate", new Date())
        .setParameter("pubId", pubId).executeUpdate();
  }

}
