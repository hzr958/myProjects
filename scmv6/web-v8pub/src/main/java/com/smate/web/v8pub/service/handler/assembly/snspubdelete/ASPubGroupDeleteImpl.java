package com.smate.web.v8pub.service.handler.assembly.snspubdelete;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.smate.web.v8pub.exception.PubHandlerAssemblyException;
import com.smate.web.v8pub.po.sns.group.GrpStatistics;
import com.smate.web.v8pub.service.handler.PubDTO;
import com.smate.web.v8pub.service.handler.assembly.PubHandlerAssemblyService;
import com.smate.web.v8pub.service.sns.GroupPubService;
import com.smate.web.v8pub.service.sns.group.GroupStatisticsService;

/**
 * 群组与个人库成果关系删除
 * 
 * @author YJ
 *
 *         2018年7月20日
 */
@Transactional(rollbackFor = Exception.class)
public class ASPubGroupDeleteImpl implements PubHandlerAssemblyService {

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private GroupPubService groupPubService;
  @Autowired
  private GroupStatisticsService groupStatisticsService;

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

    /**
     * 删除群组成果 删除个人库群组成果 本质上是更新个人库成果与群组的关系表状态为删除状态
     */
    try {
      groupPubService.updateStatusByGrpIdAndPubId(pub.grpId, pub.pubId, 1);
      // 将次群组的成果统计数减一
      GrpStatistics statistics = groupStatisticsService.get(pub.grpId);
      if (statistics != null) {
        Integer sumPubs = statistics.getSumPubs();
        if (sumPubs != null && sumPubs != 0) {
          statistics.setSumPubs(sumPubs - 1);
          groupStatisticsService.save(statistics);
        }
      }
    } catch (Exception e) {
      logger.error("删除sns库群组成果出错！grpId={},pubId={}", new Object[] {pub.grpId, pub.pubId}, e);
      throw new PubHandlerAssemblyException(this.getClass().getSimpleName() + "删除sns库群组成果出错!", e);
    }

    return null;
  }

}
