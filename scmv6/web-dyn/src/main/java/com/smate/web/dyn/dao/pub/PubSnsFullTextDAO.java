package com.smate.web.dyn.dao.pub;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.dyn.model.pub.PubFullTextPO;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 个人库成果全文Dao
 * 
 * @author YJ
 *
 *         2018年5月31日
 */
@Repository
public class PubSnsFullTextDAO extends SnsHibernateDao<PubFullTextPO, Long> {

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

  public PubFullTextPO getPubFullTextByPubId(Long pubId) {
    String hql = "from PubFullTextPO p where p.pubId =:pubId order by p.gmtModified desc,p.fileId desc";
    List list = this.createQuery(hql).setParameter("pubId", pubId).list();
    if (list != null && list.size() > 0) {
      return (PubFullTextPO) list.get(0);
    }
    return null;
  }
}
