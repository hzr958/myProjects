package com.smate.center.task.v8pub.dao.sns;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.v8pub.sns.po.PubAccessoryPO;
import com.smate.core.base.utils.data.SnsHibernateDao;

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
   * 更新附件信息
   * 
   * @param p
   */
  public void updateAccessory(PubAccessoryPO p) {
    // TODO Auto-generated method stub

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

  public PubAccessoryPO getByPubIdAndFiled(Long pubId, Long fileId) {
    String hql = "from PubAccessoryPO t where t.pubId=:pubId and t.fileId=:fileId";
    List list = this.createQuery(hql).setParameter("pubId", pubId).setParameter("fileId", fileId).list();
    if (list != null && list.size() > 0) {
      return (PubAccessoryPO) list.get(0);
    }
    return null;
  }
}
