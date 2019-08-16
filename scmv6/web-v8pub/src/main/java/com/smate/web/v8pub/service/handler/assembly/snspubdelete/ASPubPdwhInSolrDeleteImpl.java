package com.smate.web.v8pub.service.handler.assembly.snspubdelete;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.solr.service.SolrIndexService;
import com.smate.web.v8pub.exception.PubHandlerAssemblyException;
import com.smate.web.v8pub.po.pdwh.PubPdwhPO;
import com.smate.web.v8pub.service.handler.PubDTO;
import com.smate.web.v8pub.service.handler.assembly.PubHandlerAssemblyService;
import com.smate.web.v8pub.service.pdwh.PubPdwhService;

/**
 * 删除基准库论文索引
 * 
 * @author YHX
 *
 *         2019年4月17日
 */
@Transactional(rollbackFor = Exception.class)
public class ASPubPdwhInSolrDeleteImpl implements PubHandlerAssemblyService {

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private SolrIndexService solrIndexService;
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
      // 删除专利索引
      if (pubPdwhPO != null && pubPdwhPO.getPubType() == 5) {
        solrIndexService.deletePatInsolr(pub.pubId);
      } else {
        solrIndexService.deletePaperInsolr(pub.pubId);
      }
    } catch (Exception e) {
      logger.error("删除基准库论文索引出错！pubId={}", pub.pubId, e);
      throw new PubHandlerAssemblyException(e);
    }
    return null;
  }

}
