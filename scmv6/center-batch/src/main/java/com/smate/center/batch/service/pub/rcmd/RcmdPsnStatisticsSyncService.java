package com.smate.center.batch.service.pub.rcmd;

import com.smate.center.batch.model.rcmd.pub.RcmdPsnStatistics;



/**
 * 推荐服务人员统计信息服务类
 * 
 * @author zk
 * 
 */
public interface RcmdPsnStatisticsSyncService {

  public void save(RcmdPsnStatistics statistics);

  public RcmdPsnStatistics findByPsnId(Long psnId);
}
