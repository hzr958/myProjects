package com.smate.web.v8pub.service.handler.assembly.pdwhpubsave;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.pub.util.PubParamUtils;
import com.smate.web.v8pub.exception.PubHandlerAssemblyException;
import com.smate.web.v8pub.po.pdwh.PdwhPubStatisticsPO;
import com.smate.web.v8pub.service.handler.PubDTO;
import com.smate.web.v8pub.service.handler.assembly.PubHandlerAssemblyService;
import com.smate.web.v8pub.service.pdwh.PdwhPubStatisticsService;

/**
 * 基准库成果统计表保存/更新
 * 
 * @author yhx
 *
 */
@Transactional(rollbackFor = Exception.class)
public class ASPdwhStatisticsSaveImpl implements PubHandlerAssemblyService {

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private PdwhPubStatisticsService pdwhPubStatisticsService;

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
    // 更新或保存成果统计记录
    PdwhPubStatisticsPO pdwhPubStatisticsPO = pdwhPubStatisticsService.get(pub.pubId);
    try {
      Integer citations = PubParamUtils.resetCitation(pub.srcDbId, pub.citations);
      if (pdwhPubStatisticsPO == null) {
        pdwhPubStatisticsPO = new PdwhPubStatisticsPO(pub.pubId, 0, 0, 0, 0, citations);
      } else {
        // 系统引用次数大则更新为系统的
        citations = PubParamUtils.maxCitations(pdwhPubStatisticsPO.getRefCount(), citations);
        pdwhPubStatisticsPO.setRefCount(citations);
      }
      pdwhPubStatisticsService.saveOrUpdate(pdwhPubStatisticsPO);
    } catch (Exception e) {
      logger.error("更新基准库成果统计表出错！pdwhPubStatisticsPO={}", pdwhPubStatisticsPO, e);
      throw new PubHandlerAssemblyException(this.getClass().getSimpleName() + "基准库保存或更新成果统计数记录出错！", e);
    }
    return null;
  }

}
