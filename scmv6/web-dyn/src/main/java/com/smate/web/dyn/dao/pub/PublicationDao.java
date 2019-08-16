package com.smate.web.dyn.dao.pub;

import org.springframework.stereotype.Repository;

import com.smate.core.base.pub.model.Publication;
import com.smate.core.base.utils.data.SnsHibernateDao;


/**
 * 成果、文献DAO.
 * 
 * @author zk
 * 
 */
@Repository
public class PublicationDao extends SnsHibernateDao<Publication, Long> {
  /**
   * 获取成果所有人.
   * 
   * @param pubId
   * @return
   */
  public Long getPubOwner(Long pubId) {
    return (Long) super.createQuery("select t.ownerPsnId from Publication t where t.pubId = :pubId ")
        .setParameter("pubId", pubId).uniqueResult();
  }

  /**
   * 通过成果id获取成果拥有者及成果状态
   * 
   * @param pubId
   * @return
   */
  public Publication getPubOwnerPsnIdOrStatus(Long pubId) {
    String hql =
        "select new Publication(p.pubId,p.ownerPsnId,p.status) from Publication p where p.pubId=:id and rownum=1 ";
    Object obj = super.createQuery(hql).setParameter("id", pubId).uniqueResult();
    if (obj != null) {
      return (Publication) obj;
    }
    return null;
  }

  /*
   * 得到成果的简介 和 标题
   */
  public Publication getPubBrefAndTitle(Long pubId) {

    String hql =
        "select new Publication(p.pubId,p.zhTitle , p.enTitle , p.authorNames , p.briefDesc,p.briefDescEn,p.publishYear,p.ownerPsnId) from Publication p where p.pubId =:pubId";
    Object obj = super.createQuery(hql).setParameter("pubId", pubId).uniqueResult();
    if (obj != null) {
      return (Publication) obj;
    }
    return null;
  }

  /**
   * 获取成果评论
   */
  public Publication getPubForComments(Long pubId) {
    String hql =
        "select new Publication(p.pubStart,p.pubId,p.pubStartPsns,p.pubReviews) from Publication p where p.pubId=:id and rownum=1 ";
    Object obj = super.createQuery(hql).setParameter("id", pubId).uniqueResult();
    if (obj != null) {
      return (Publication) obj;
    }
    return null;
  }

  public void updatePub(Publication pub) {
    String hql =
        "update Publication t set t.pubStart=:pubStart,t.pubStartPsns=:pubStartPsns,t.pubReviews=:pubReviews where t.pubId=:pubId";
    this.createQuery(hql).setParameter("pubStart", pub.getPubStart())
        .setParameter("pubStartPsns", pub.getPubStartPsns()).setParameter("pubReviews", pub.getPubReviews())
        .setParameter("pubId", pub.getPubId()).executeUpdate();

  }
}
