package com.smate.web.v8pub.service.handler.assembly.snspubsave;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.psn.model.PsnStatistics;
import com.smate.core.base.pub.enums.PublicationTypeEnum;
import com.smate.web.v8pub.enums.PubGenreConstants;
import com.smate.web.v8pub.exception.PubHandlerAssemblyException;
import com.smate.web.v8pub.po.sns.PubSnsPO;
import com.smate.web.v8pub.service.handler.PubDTO;
import com.smate.web.v8pub.service.handler.assembly.PubHandlerAssemblyService;
import com.smate.web.v8pub.service.sns.PsnStatisticsService;
import com.smate.web.v8pub.service.sns.PubSnsService;

/**
 * 删除成果时，人员统计数中的成果数:减一.
 * 
 * @author YJ
 *
 *         2018年8月13日
 */
@Transactional(rollbackFor = Exception.class)
public class ASPsnStatisticsSubImpl implements PubHandlerAssemblyService {
  private Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private PsnStatisticsService psnStatisticsService;
  @Autowired
  private PubSnsService pubSnsService;

  @Override
  public void checkSourcesParameter(PubDTO pub) throws PubHandlerAssemblyException {
    // TODO Auto-generated method stub
  }

  @Override
  public void checkRebuildParameter(PubDTO pub) throws PubHandlerAssemblyException {
    if (pub.psnId == null) {
      logger.error("更新人员统计数出错！psnId为null，psnId={}", pub.psnId);
      throw new PubHandlerAssemblyException(this.getClass().getSimpleName() + "更新人员统计数出错，psnId为null");
    }
  }

  @Override
  public Map<String, String> excute(PubDTO pub) throws PubHandlerAssemblyException {
    try {
      if (pub.pubGenre == PubGenreConstants.PSN_PUB) {
        PsnStatistics psnStatistics = psnStatisticsService.getPsnStatistics(pub.psnId);
        if (psnStatistics != null) {
          // 人员成果统计数
          if (psnStatistics.getPubSum() != null && psnStatistics.getPubSum() > 0) {
            psnStatistics.setPubSum(psnStatistics.getPubSum() - 1);
          }
          // 人员专利统计数
          if (pub.pubType.intValue() == PublicationTypeEnum.PATENT) {
            if (psnStatistics.getPatentSum() != null && psnStatistics.getPatentSum() > 0) {
              psnStatistics.setPatentSum(psnStatistics.getPatentSum() - 1);
            }
          }
          // 人员公开成果统计数
          if (psnStatistics.getOpenPubSum() != null && psnStatistics.getOpenPubSum() > 0) {
            if (pub.permission != null && pub.permission == 7) {
              psnStatistics.setOpenPubSum(psnStatistics.getOpenPubSum() - 1);
            }
          }
          PubSnsPO pubSns = pubSnsService.get(pub.pubId);
          int newCitedSum = psnStatistics.getCitedSum() - (pubSns.getCitations() == null ? 0 : pubSns.getCitations());
          psnStatistics.setCitedSum(newCitedSum > 0 ? newCitedSum : 0);
          psnStatistics.setHindex(psnStatisticsService.getHindex(pub.psnId));
          psnStatisticsService.savePsnStatistics(psnStatistics);
        }

      }
    } catch (Exception e) {
      logger.error("更新人员统计数出错！psnId={}", pub.psnId);
      throw new PubHandlerAssemblyException(this.getClass().getSimpleName() + "更新人员统计数出错", e);
    }
    return null;
  }
}
