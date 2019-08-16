package com.smate.center.task.v8pub.dao.sns;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.v8pub.sns.po.PubFullTextPO;
import com.smate.core.base.utils.data.SnsHibernateDao;

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
    String hql = "from PubFullTextPO p where p.pubId = :pubId and p.fileId = :fileId";
    return (PubFullTextPO) this.createQuery(hql).setParameter("pubId", pubId).setParameter("fileId", fileId)
        .uniqueResult();
  }

  public List<PubFullTextPO> getByPubIds(List<Long> pubIds) {
    String hql = "from PubFullTextPO p where p.pubId in (:pubIds)";
    List list = this.createQuery(hql).setParameterList("pubIds", pubIds).list();
    return list;
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

  public Long getFullTextIdByPubId(Long pubId) {
    String hql = "select max(id) from PubFullTextPO where pubId =:pubId";
    return (Long) this.createQuery(hql).setParameter("pubId", pubId).uniqueResult();
  }

  public PubFullTextPO getByPubId(Long pubId) {
    String hql = "from PubFullTextPO where pubId =:pubId";
    List list = this.createQuery(hql).setParameter("pubId", pubId).list();
    if (list != null && list.size() > 0) {
      return (PubFullTextPO) list.get(0);
    }
    return null;
  }

  public void deletByPubId(Long pubId) {
    String hql = "delete from PubFullTextPO where pubId =:pubId";
    this.createQuery(hql).setParameter("pubId", pubId).executeUpdate();
  }

  public Integer getPermission(Long snsPubId, Long fulltextFileId) {
    String hql = "select p.permission from PubFullTextPO p where p.pubId = :pubId and p.fileId = :fileId";
    return (Integer) this.createQuery(hql).setParameter("pubId", snsPubId).setParameter("fileId", fulltextFileId)
        .uniqueResult();
  }
}
