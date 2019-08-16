package com.smate.web.group.dao.grp.pub;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.group.model.grp.pub.GrpPubIndexUrl;

@Repository
public class GrpPubIndexUrlDao extends SnsHibernateDao<GrpPubIndexUrl, Long> {
  /**
   * 通过grpId和pubId查询群组成果短地址对象GrpPubIndexUrl
   * 
   * @param grpId
   * @param pubId
   * @return
   */
  public GrpPubIndexUrl findByGrpIdAndPubId(Long grpId, Long pubId) {
    String hql = "from GrpPubIndexUrl t where t.grpId=:grpId and t.pubId=:pubId ";
    return (GrpPubIndexUrl) this.createQuery(hql).setParameter("grpId", grpId).setParameter("pubId", pubId)
        .uniqueResult();
  }
}
