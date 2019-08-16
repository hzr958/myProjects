package com.smate.web.psn.dao.rcmdsync;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.RcmdHibernateDao;
import com.smate.web.psn.model.rcmd.PsnStatisticsRcmd;

/**
 * 推荐库人员信息统计Dao.
 * 
 * @author lichangwen
 * 
 */

@Repository
public class PsnStatisticsRcmdDao extends RcmdHibernateDao<PsnStatisticsRcmd, Long> {

  public PsnStatisticsRcmd findByPsnId(Long psnId) {
    String hql = "from PsnStatisticsRcmd t where t.psnId = :psnId";
    return (PsnStatisticsRcmd) super.createQuery(hql).setParameter("psnId", psnId).uniqueResult();
  }

}
