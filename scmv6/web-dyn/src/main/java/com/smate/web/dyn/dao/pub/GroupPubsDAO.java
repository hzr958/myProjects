package com.smate.web.dyn.dao.pub;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.dyn.model.pub.GroupPubPO;

@Repository
public class GroupPubsDAO extends SnsHibernateDao<GroupPubPO, Long> {

  public GroupPubPO findByPubId(Long pubId) {
    String hql = " from  GroupPubPO t  where t.pubId =:pubId order by t.createDate desc";
    List list = super.createQuery(hql).setParameter("pubId", pubId).list();
    if (list != null && list.size() > 0) {
      return (GroupPubPO) list.get(0);
    }
    return null;
  }

}
