package com.smate.web.v8pub.service.handler.assembly.pdwhpubupdate;

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
 * 更新基准库成果统计表引用数
 * 
 * @author YJ
 *
 *         2018年8月31日
 */
@Transactional(rollbackFor = Exception.class)
public class ASPdwhStatisticCitationsUpdateImpl implements PubHandlerAssemblyService {

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
    try {
      PdwhPubStatisticsPO pdwhPubStatisticsPO = pdwhPubStatisticsService.get(pub.pubId);
      if (pdwhPubStatisticsPO != null) {
        Integer citations = PubParamUtils.resetCitation(pub.srcDbId, pub.citations);
        // 系统引用次数大则更新为系统的
        citations = PubParamUtils.maxCitations(pdwhPubStatisticsPO.getRefCount(), citations);
        pdwhPubStatisticsPO.setRefCount(citations);
        pdwhPubStatisticsService.saveOrUpdate(pdwhPubStatisticsPO);
      }
    } catch (Exception e) {
      logger.error("更新基准库成果统计表引用数出错！", e);
      throw new PubHandlerAssemblyException(this.getClass().getSimpleName() + "更新基准库成果统计表引用数出错！", e);
    }
    return null;
  }

}
