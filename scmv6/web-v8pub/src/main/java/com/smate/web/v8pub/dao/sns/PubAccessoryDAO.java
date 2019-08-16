package com.smate.web.v8pub.dao.sns;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.v8pub.po.sns.PubAccessoryPO;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 成果附件dao
 * 
 * @author aijiangbin
 * @date 2018年5月31日
 */

@Repository
public class PubAccessoryDAO extends SnsHibernateDao<PubAccessoryPO, Long> {

  public PubAccessoryPO findByPubIdAndFileId(Long pubId, Long fileId) {
    String hql = "from PubAccessoryPO p where p.pubId =:pubId and p.fileId =:fileId";
    Object object = this.createQuery(hql).setParameter("pubId", pubId).setParameter("fileId", fileId).uniqueResult();
    if (object != null) {
      return (PubAccessoryPO) object;
    }
    return null;
  }

  /**
   * 查询成果的全文
   * 
   * @param pubId
   * @return
   */
  public List<PubAccessoryPO> findByPubId(Long pubId) {
    String hql = "from PubAccessoryPO p where p.pubId =:pubId ";
    List list = this.createQuery(hql).setParameter("pubId", pubId).list();

    return list;
  }

  public void deleteAll(Long pubId) {
    String hql = "delete from PubAccessoryPO p where p.pubId =:pubId";
    this.createQuery(hql).setParameter("pubId", pubId).executeUpdate();
  }
}
