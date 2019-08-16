package com.smate.center.task.v8pub.dao.sns;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.v8pub.sns.po.PubSharePO;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 成果 分享dao
 * 
 * @author aijiangbin
 * @date 2018年5月31日
 */

@Repository
public class PubShareDAO extends SnsHibernateDao<PubSharePO, Long> {

  public void deleteAll(Long pubId) {
    String hql = "delete from PubSharePO where pubId =:pubId";
    this.createQuery(hql).setParameter("pubId", pubId).executeUpdate();
  }

  public Long countPubShared(Long pubId) {
    String hql = "select count(1) from PubSharePO t where t.pubId =:pubId and t.status = 0";
    return (Long) this.createQuery(hql).setParameter("pubId", pubId).uniqueResult();
  }

  @SuppressWarnings("unchecked")
  public List<PubSharePO> getShareRecords(Long pubId) {
    String hql = "from PubSharePO t where t.pubId=:pubId and t.status=0";
    return this.createQuery(hql).setParameter("pubId", pubId).list();
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  public PubSharePO findShare(Long pubId, Long psnId, Integer platform, Date gmtCreate) {
    String hql =
        "from PubSharePO t where t.pubId=:pubId and t.psnId =:psnId and t.platform=:platform and t.gmtCreate = :gmtCreate";
    List list = this.createQuery(hql).setParameter("pubId", pubId).setParameter("psnId", psnId)
        .setParameter("platform", platform).setParameter("gmtCreate", gmtCreate).list();
    if (list != null && list.size() > 0) {
      return (PubSharePO) list.get(0);
    }
    return null;

  }
}
