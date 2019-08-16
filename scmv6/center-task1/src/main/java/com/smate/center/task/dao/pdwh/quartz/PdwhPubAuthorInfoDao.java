package com.smate.center.task.dao.pdwh.quartz;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.task.model.pdwh.pub.PdwhPubAuthorInfo;
import com.smate.core.base.utils.data.PdwhHibernateDao;

@Repository
public class PdwhPubAuthorInfoDao extends PdwhHibernateDao<PdwhPubAuthorInfo, Long> {
  public void updateAuthorEmail(Long pubId, String authorName, String email) {
    String hql = "update PdwhPubAuthorInfo t set t.email=:email where t.pubId=:pubId and t.authorName=:authorName";

    super.createQuery(hql).setParameter("email", email).setParameter("pubId", pubId)
        .setParameter("authorName", authorName).executeUpdate();
  }

  @SuppressWarnings("unchecked")
  public List<PdwhPubAuthorInfo> getPubAuthorNames(Long pubId) {
    String hql = "from PdwhPubAuthorInfo t where t.pubId=:pubId ";
    return super.createQuery(hql).setParameter("pubId", pubId).list();
  }

  /**
   * 会新开事务
   * 
   * @param pubId
   * @param authorName
   * @param authorNameSpec
   * @param organization
   * @param email
   */
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void saveAuthorsInfo(Long pubId, String authorName, String authorNameSpec, String organization, String email) {
    super.save(new PdwhPubAuthorInfo(pubId, authorName, authorNameSpec, organization, email));
  }

  /**
   * 不新开事务保存
   * 
   * @param pubId
   * @param authorName
   * @param authorNameSpec
   * @param organization
   * @param organizationSpec
   */
  public void saveAuthorInfo(Long pubId, Long assignInsId, String authorName, String authorNameSpec,
      String organization, String organizationSpec) {
    super.save(new PdwhPubAuthorInfo(pubId, assignInsId, authorName, authorNameSpec, organization, organizationSpec));
  }

  @SuppressWarnings("unchecked")
  public List<PdwhPubAuthorInfo> findRecord(String name, String insName) {
    String hql = "from PdwhPubAuthorInfo t where t.authorNameSpec=:name  and t.organization=:insName ";
    return super.createQuery(hql).setParameter("name", name).setParameter("insName", insName).list();
  }

  @SuppressWarnings("unchecked")
  public List<PdwhPubAuthorInfo> findRecordBynameAndInsId(String name, Long insId) {
    String hql = "from PdwhPubAuthorInfo t where t.authorNameSpec=:name  and t.assignInsId=:insId ";
    return super.createQuery(hql).setParameter("name", name).setParameter("insId", insId).list();
  }

  @SuppressWarnings("unchecked")
  public List<PdwhPubAuthorInfo> findRecordByName(String name) {
    String hql = "from PdwhPubAuthorInfo t where t.authorNameSpec=:name  ";
    return super.createQuery(hql).setParameter("name", name).list();
  }

}
