package com.smate.web.psn.v8pub.dao.sns.pub;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.psn.v8pub.model.sns.pub.PubSharePO;

/**
 * 成果 分享dao
 * 
 * @author aijiangbin
 * @date 2018年5月31日
 */

@Repository
public class PubShareDAO extends SnsHibernateDao<PubSharePO, Long> {

  /**
   * 统计人员成果分享数
   * 
   * @param psnId
   * @return
   */
  public Long countPsnPubShareSum(Long psnId) {
    String hql =
        "select count(t.pubId) from PubSharePO t where t.status=0 and exists(select 1 from PsnPubPO t1 where t1.pubId=t.pubId and t1.ownerPsnId=:psnId and t1.status=0 )";
    return (Long) super.createQuery(hql).setParameter("psnId", psnId).uniqueResult();
  }

}
