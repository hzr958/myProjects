package com.smate.web.v8pub.service.handler.assembly.snspubupdate;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.smate.web.v8pub.exception.PubHandlerAssemblyException;
import com.smate.web.v8pub.service.handler.PubDTO;
import com.smate.web.v8pub.service.handler.assembly.PubHandlerAssemblyService;
import com.smate.web.v8pub.service.sns.PsnStatisticsService;

/**
 * 更新人员统计数中的引用数
 * 
 * @author YJ
 *
 *         2018年8月31日
 */
@Transactional(rollbackFor = Exception.class)
public class ASPsnStatisticCitationsUpdateImpl implements PubHandlerAssemblyService {

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private PsnStatisticsService psnStatisticsService;

  @Override
  public void checkSourcesParameter(PubDTO pub) throws PubHandlerAssemblyException {
    // TODO Auto-generated method stub

  }

  @Override
  public void checkRebuildParameter(PubDTO pub) throws PubHandlerAssemblyException {
    // TODO Auto-generated method stub

  }

  @Override
  public Map<String, String> excute(PubDTO pub) throws PubHandlerAssemblyException {
    try {
      // 开启任务，重新计算人员统计数中的引用统计数
      psnStatisticsService.updatePsnStatisticsRefresh(pub.psnId);
    } catch (Exception e) {
      logger.error("更新人员引用统计数出错！", e);
      throw new PubHandlerAssemblyException(this.getClass().getSimpleName() + "更新人员引用统计数出错！", e);
    }
    return null;
  }
}
