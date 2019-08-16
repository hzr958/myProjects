package com.smate.web.v8pub.dao.sns.group;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.v8pub.po.sns.group.GrpPubIndexUrlPO;

@Repository
public class GrpPubIndexUrlDAO extends SnsHibernateDao<GrpPubIndexUrlPO, Long> {
  /**
   * 通过grpId和pubId查询群组成果短地址对象GrpPubIndexUrl
   * 
   * @param grpId
   * @param pubId
   * @return
   */
  public GrpPubIndexUrlPO findByGrpIdAndPubId(Long grpId, Long pubId) {
    String hql = "from GrpPubIndexUrlPO t where t.grpId=:grpId and t.pubId=:pubId ";
    return (GrpPubIndexUrlPO) this.createQuery(hql).setParameter("grpId", grpId).setParameter("pubId", pubId)
        .uniqueResult();
  }



}
