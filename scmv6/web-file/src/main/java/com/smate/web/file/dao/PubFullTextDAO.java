package com.smate.web.file.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.file.model.fulltext.PubFullTextPO;

/**
 * 个人库成果全文Dao
 * 
 * @author YJ
 *
 *         2018年5月31日
 */
@Repository
public class PubFullTextDAO extends SnsHibernateDao<PubFullTextPO, Long> {

  /**
   * 根据pubId和fileId获取成果全文对象
   * 
   * @param pubId 成果id
   * @param fileId 文件id
   * @return
   */
  public PubFullTextPO getPubFullTextByPubIdAndFileId(Long pubId, Long fileId) {
    String hql = "form PubFullTextPO p where p.pubId:=pubId and p.fileId:=fileId";
    return (PubFullTextPO) this.createQuery(hql).setParameter("pubId", pubId).setParameter("fileId", fileId)
        .uniqueResult();
  }

  public List<PubFullTextPO> getByPubIds(List<Long> pubIds) {
    String hql = "from PubFullTextPO p where p.pubId in (:pubIds)";
    List list = this.createQuery(hql).setParameterList("pubIds", pubIds).list();
    return list;
  }

  /**
   * 根据pubIds查询公开权限的成果全文条数
   * 
   * @param pubIds
   * @return
   */
  public Long getPubFulltextCount(List<Long> pubIds) {
    String hql = "select count(t.pubId) from PubFullTextPO t where t.pubId in(:pubIds) and t.permission=0";
    return (Long) super.createQuery(hql).setParameterList("pubIds", pubIds).uniqueResult();
  }

  /**
   * 根据pubIds查询公开权限的成果全文
   * 
   * @param pubIds
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<PubFullTextPO> getPubFulltextList(List<Long> pubIds) {
    String hql = "from PubFullTextPO t where t.pubId in(:pubIds) and t.permission=0";
    return super.createQuery(hql).setParameterList("pubIds", pubIds).list();

  }

  /**
   * 根据pubId获取成果全文对象
   * 
   * @param pubId
   * @return
   */
  public PubFullTextPO getPubFullTextByPubId(Long pubId) {
    String hql = "from PubFullTextPO p where p.pubId =:pubId order by p.gmtCreate desc";
    List list = this.createQuery(hql).setParameter("pubId", pubId).list();
    if (list != null && list.size() > 0) {
      return (PubFullTextPO) list.get(0);
    }
    return null;
  }

  /**
   * 根据pubIds查询公开权限的成果全文
   * 
   * @param pubIds
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Long> getPubFulltextIdsList(List<Long> pubIds) {
    String hql = "select t.pubId from PubFullTextPO t where t.pubId in(:pubIds) and t.permission=0";
    return super.createQuery(hql).setParameterList("pubIds", pubIds).list();

  }
}
