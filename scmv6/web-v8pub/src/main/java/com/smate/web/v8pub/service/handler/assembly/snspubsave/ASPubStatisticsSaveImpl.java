package com.smate.web.v8pub.service.handler.assembly.snspubsave;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.smate.web.v8pub.exception.PubHandlerAssemblyException;
import com.smate.web.v8pub.po.sns.PubStatisticsPO;
import com.smate.web.v8pub.service.handler.PubDTO;
import com.smate.web.v8pub.service.handler.assembly.PubHandlerAssemblyService;
import com.smate.web.v8pub.service.sns.PubStatisticsService;

/**
 * 成果统计数
 * 
 * @author YJ
 *
 *         2018年7月26日
 */
@Transactional(rollbackFor = Exception.class)
public class ASPubStatisticsSaveImpl implements PubHandlerAssemblyService {

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private PubStatisticsService pubStatisticsService;

  @Override
  public void checkSourcesParameter(PubDTO pub) throws PubHandlerAssemblyException {

  }

  @Override
  public void checkRebuildParameter(PubDTO pub) throws PubHandlerAssemblyException {
    // TODO Auto-generated method stub

  }

  @Override
  public Map<String, String> excute(PubDTO pub) throws PubHandlerAssemblyException {
    // 保存或更新成果统计记录
    PubStatisticsPO pubStatisticsPO = pubStatisticsService.get(pub.pubId);
    try {
      if (pubStatisticsPO == null) {
        pubStatisticsPO =
            new PubStatisticsPO(pub.pubId, pub.awardCount, pub.shareCount, pub.commentCount, 0, pub.citations);
      } else {
        // 有记录，只更新引用数，其他记录数不进行更新
        pubStatisticsPO.setRefCount(pub.citations);
      }
      pubStatisticsService.saveOrUpdate(pubStatisticsPO);
    } catch (Exception e) {
      logger.error("更新sns库成果统计表出错！pubStatisticsPO={}", pubStatisticsPO, e);
      throw new PubHandlerAssemblyException(this.getClass().getSimpleName() + "保存或更新sns库成果统计数出错!", e);
    }
    return null;
  }

}
