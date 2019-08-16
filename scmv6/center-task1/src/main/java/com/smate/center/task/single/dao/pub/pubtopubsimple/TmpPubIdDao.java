package com.smate.center.task.single.dao.pub.pubtopubsimple;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.single.model.pub.pubtopubsimple.TmpPubId;
import com.smate.core.base.utils.data.SnsHibernateDao;

@Repository
public class TmpPubIdDao extends SnsHibernateDao<TmpPubId, Long> {

  @SuppressWarnings("unchecked")
  public List<TmpPubId> getAll() {
    String hql = "from TmpPubId t where t.status = 0";
    return super.createQuery(hql).list();
  }

}
