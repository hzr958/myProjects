package com.smate.center.batch.service.rol.pub;

import com.smate.center.batch.model.rol.pub.SiePsnStatistics;



/**
 * 同步PsnStatistics记录到Rol.
 * 
 * @author zyx
 * 
 */
public interface SiePsnStatisticsSyncService {
  public void save(SiePsnStatistics statistics);

  public SiePsnStatistics findByPsnId(Long psnId);
}
