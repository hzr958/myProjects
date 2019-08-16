package com.smate.web.v8pub.dao.sns;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.v8pub.po.sns.PubAccessoryPO;

@Repository
public class PubAccessoryPODao extends SnsHibernateDao<PubAccessoryPO, Long> {

  public List<PubAccessoryPO> getPubAccessoryPOList(Long pubId) {
    String hql = "from PubAccessoryPO t where t.pubId=:pubId";
    return super.createQuery(hql).setParameter("pubId", pubId).list();
  }
}
