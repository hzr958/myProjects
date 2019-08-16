package com.smate.web.v8pub.service.handler.assembly.snspubsave;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.smate.web.v8pub.enums.PubGenreConstants;
import com.smate.web.v8pub.exception.PubHandlerAssemblyException;
import com.smate.web.v8pub.po.sns.group.GrpStatistics;
import com.smate.web.v8pub.service.handler.PubDTO;
import com.smate.web.v8pub.service.handler.assembly.PubHandlerAssemblyService;
import com.smate.web.v8pub.service.sns.group.GroupStatisticsService;

/**
 * 群组统计数
 * 
 * @author YJ
 *
 *         2018年9月30日
 */
@Transactional(rollbackFor = Exception.class)
public class ASGrpStatisticsAddImpl implements PubHandlerAssemblyService {

  private Logger logger = LoggerFactory.getLogger(getClass());
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
    try {
      if (pub.pubGenre == PubGenreConstants.GROUP_PUB) {
        // 将次群组的成果统计数加1
        GrpStatistics statistics = groupStatisticsService.get(pub.grpId);
        if (statistics != null) {
          Integer sumPubs = statistics.getSumPubs();
          if (sumPubs != null) {
            sumPubs = sumPubs + 1;
          } else {
            sumPubs = 1;
          }
          statistics.setSumPubs(sumPubs);
          groupStatisticsService.saveOrUpdate(statistics);
        }
      }
    } catch (Exception e) {
      logger.error("出错！", e);
      throw new PubHandlerAssemblyException(e);
    }
    return null;
  }

}
