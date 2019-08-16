package com.smate.sie.center.task.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.PdwhHibernateDao;
import com.smate.sie.center.task.otherlibrary.model.SnsPdwhPubSharePO;

/**
 * 成果 分享dao
 * 
 * @author hd
 * @date 2018年5月31日
 */

@Repository
public class SnsPdwhPubShareDAO extends PdwhHibernateDao<SnsPdwhPubSharePO, Long> {

  @SuppressWarnings("unchecked")
  public List<SnsPdwhPubSharePO> getShareRecords(Long pdwhPubId) {
    String hql = "from SnsPdwhPubSharePO t where t.pdwhPubId=:pdwhPubId and t.status=0";
    return this.createQuery(hql).setParameter("pdwhPubId", pdwhPubId).list();
  }

}
