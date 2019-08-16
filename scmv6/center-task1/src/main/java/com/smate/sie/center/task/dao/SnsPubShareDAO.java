package com.smate.sie.center.task.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.sie.center.task.otherlibrary.model.SnsPubSharePO;

/**
 * 成果 分享dao
 * 
 * @author hd
 * @date 2018年5月31日
 */

@Repository
public class SnsPubShareDAO extends SnsHibernateDao<SnsPubSharePO, Long> {

  @SuppressWarnings("unchecked")
  public List<SnsPubSharePO> getShareRecords(Long pubId) {
    String hql = "from SnsPubSharePO t where t.pubId=:pubId and t.status=0";
    return this.createQuery(hql).setParameter("pubId", pubId).list();
  }

}
