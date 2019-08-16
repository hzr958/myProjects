package com.smate.web.v8pub.dao.sns;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.v8pub.po.sns.PubSharePO;

/**
 * 成果 分享dao
 * 
 * @author aijiangbin
 * @date 2018年5月31日
 */

@Repository
public class PubShareDAO extends SnsHibernateDao<PubSharePO, Long> {
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
