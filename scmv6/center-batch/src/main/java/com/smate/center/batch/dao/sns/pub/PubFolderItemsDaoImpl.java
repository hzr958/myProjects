package com.smate.center.batch.dao.sns.pub;

import java.util.List;

import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.sns.pub.PubFolderItems;
import com.smate.core.base.utils.constant.ServiceConstants;
import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.core.base.utils.string.ServiceUtil;

/**
 * 成果/文献文件夹关系数据库访问接口.
 * 
 * @author LY
 * 
 */
@Repository
public class PubFolderItemsDaoImpl extends SnsHibernateDao<PubFolderItems, Long> implements PubFolderItemsDao {

  /*
   * (non-Javadoc)
   * 
   * @seecom.iris.scm.scmweb.dao.folder.PubFolderItemsDao#
   * addPublicationToPubFolder(com.iris.scm.scmweb.model.folder. PubFolderItems)
   */
  @Override
  public Long addPublicationToPubFolder(PubFolderItems pubFolderItems) {
    return (Long) super.getSession().save(pubFolderItems);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.iris.scm.scmweb.dao.folder.PubFolderItemsDao#getPubFolderItemsById (java.lang.Integer)
   */
  public List<PubFolderItems> getPubFolderItemsById(Long folderId) {
    return super.find("from PubFolderItems t where t.folderId=?", new Object[] {folderId});
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.iris.scm.scmweb.dao.folder.PubFolderItemsDao#getStatByFolderId(java .lang.Long)
   */
  @Override
  public Long getStatByFolderId(Long folderId) {
    String sql = "select count(t.id) from PubFolderItems t where t.folderId = ? and t.pubFolder.enabled = 1 ";
    Long count = super.findUnique(sql, folderId);
    return count;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.iris.scm.scmweb.dao.folder.PubFolderItemsDao#getStatByPubId(java. lang.Long,
   * java.lang.Long)
   */
  @Override
  public Long getStatByPubId(Long folderId, Long pubId) {
    String sql = "select count(t.id) from PubFolderItems t where t.folderId = ? and t.pubId = ? ";
    Long count = super.findUnique(sql, folderId, pubId);
    return count;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.iris.scm.scmweb.dao.folder.PubFolderItemsDao#
   * removePublicationFromPubFolder(java.lang.Long, java.lang.Long)
   */
  @Override
  public void removePublicationFromPubFolder(PubFolderItems pubFolderItems) {
    super.getSession().delete(pubFolderItems);

  }

  /*
   * (non-Javadoc)
   * 
   * @see com.iris.scm.scmweb.dao.folder.PubFolderItemsDao#getPubFolderItems(java .lang.Long,
   * java.lang.Long)
   */
  @Override
  public PubFolderItems getPubFolderItems(Long folderId, Long pubId) {
    String sql = "from PubFolderItems t where t.folderId=? and t.pubId=?";
    return super.findUnique(sql, folderId, pubId);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.iris.scm.scmweb.dao.folder.PubFolderItemsDao#removePublicationByFolderId
   * (java.lang.Long)
   */
  @Override
  public void removePublicationByFolderId(Long folderId) {
    String hql = "delete from PubFolderItems t where t.folderId=?";
    super.createQuery(hql, new Object[] {folderId}).executeUpdate();
  }

  @Override
  public void removePublicationFromPubFolder(String pubIds) {
    if (pubIds != null && pubIds.matches(ServiceConstants.IDPATTERN)) {
      this.removePublicationFromPubFolder(ServiceUtil.splitStrToLong(pubIds));
    }
  }

  @Override
  public void removePublicationFromPubFolder(List<Long> pubIds) {
    super.getSession().createQuery("delete from PubFolderItems t where t.pubId in (:pubIds) ")
        .setParameterList("pubIds", pubIds).executeUpdate();
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  @Override
  public List changePubFolders(Long psnId, Object[] pubIds, int articleType) {
    String sql =
        "select t.folder_id,count(1) from pub_folder_items t where t.folder_id in(select t2.folder_id from pub_folder t2 where t2.owner_psn_id=:psnId and t2.article_type=:type and t2.enabled=:enabled) and t.pub_id in(:pubIds) group by t.folder_id";
    SQLQuery sqlQuery =
        (SQLQuery) super.getSession().createSQLQuery(sql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
    sqlQuery.setParameter("psnId", psnId);
    sqlQuery.setParameter("type", articleType);
    sqlQuery.setParameter("enabled", 1);
    sqlQuery.setParameterList("pubIds", pubIds);
    return sqlQuery.list();
  }

  @Override
  public void deleteFdFlByUnchangingIds(Object[] pubIds, Object[] folderIds) {
    StringBuilder hql = new StringBuilder();
    hql.append("delete from PubFolderItems t where t.pubId in(:pubIds) and t.folderId not in(:folderIds)");
    super.createQuery(hql.toString()).setParameterList("pubIds", pubIds).setParameterList("folderIds", folderIds)
        .executeUpdate();
  }

  @Override
  public void removePubFolderItemsById(Long id) {
    String hql = "delete from PubFolderItems t where t.id=?";
    super.createQuery(hql, new Object[] {id}).executeUpdate();
  }

  public List<PubFolderItems> getPubFolderItemsByPubId(Long pubId) {
    return super.find("from PubFolderItems t where t.pubId=?", new Object[] {pubId});
  }

  @Override
  public void savePubFolderItems(PubFolderItems pubFolderItems) {
    super.save(pubFolderItems);

  }
}
