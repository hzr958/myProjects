package com.smate.center.task.service.sns.quartz;

import java.util.List;

/**
 * 
 * @author LJ
 *
 *         2017年6月5日
 */
public interface CleanExpiredShortUrlService {

  public List<Long> getNeedCleanData() throws Exception;

  public void handleUrldata(Long id) throws Exception;


}
