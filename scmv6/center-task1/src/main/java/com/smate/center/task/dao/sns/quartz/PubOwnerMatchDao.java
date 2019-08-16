package com.smate.center.task.dao.sns.quartz;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.sns.quartz.PubOwnerMatch;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 成果作者匹配表，用于确定用户与作者的关系.
 * 
 * @author liqinghua
 * 
 */
@Repository
public class PubOwnerMatchDao extends SnsHibernateDao<PubOwnerMatch, Long> {
  public PubOwnerMatch getPubOwnerMatch(Long pubId) {

    String hql = "from PubOwnerMatch t where t.pubId = ? ";
    return super.findUnique(hql, pubId);
  }

}
