package com.smate.web.v8pub.service.handler.assembly.pdwhpubupdate;

import java.util.Date;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import com.smate.core.base.pub.util.PubParamUtils;
import com.smate.web.v8pub.exception.PubHandlerAssemblyException;
import com.smate.web.v8pub.po.pdwh.PubPdwhPO;
import com.smate.web.v8pub.service.handler.PubDTO;
import com.smate.web.v8pub.service.handler.assembly.PubHandlerAssemblyService;
import com.smate.web.v8pub.service.pdwh.PubPdwhService;

/**
 * 更新基准库主表Citations字段
 * 
 * @author YJ
 *
 *         2018年8月29日
 */
@Transactional(rollbackFor = Exception.class)
public class ASPubPdwhCitationsUpdateImpl implements PubHandlerAssemblyService {

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private PubPdwhService pubPdwhService;

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
      PubPdwhPO pubPdwhPO = pubPdwhService.get(pub.pubId);
      if (pubPdwhPO != null) {
        Integer citations = PubParamUtils.resetCitation(pub.srcDbId, pub.citations);
        // 系统引用次数大则更新为系统的
        citations = PubParamUtils.maxCitations(pubPdwhPO.getCitations(), citations);
        pubPdwhPO.setCitations(citations);
        pubPdwhPO.setGmtModified(new Date());
        pubPdwhService.saveOrUpdate(pubPdwhPO);
      }
    } catch (Exception e) {
      logger.error("更新基准库主表Citations字段出错！", e);
      throw new PubHandlerAssemblyException(this.getClass().getSimpleName() + "更新基准库主表Citations字段出错！", e);
    }
    return null;
  }

}
