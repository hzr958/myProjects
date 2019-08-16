package com.smate.web.v8pub.service.handler.assembly.pdwhpubsave;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.smate.web.v8pub.exception.PubHandlerAssemblyException;
import com.smate.web.v8pub.service.handler.PubDTO;
import com.smate.web.v8pub.service.handler.assembly.PubHandlerAssemblyService;
import com.smate.web.v8pub.service.pdwh.PdwhPubKeywordsService;

@Transactional(rollbackFor = Exception.class)
public class ASPdwhKeywordsSaveImpl implements PubHandlerAssemblyService {

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private PdwhPubKeywordsService pdwhPubKeywordsService;

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
      pdwhPubKeywordsService.savePubKeywords(pub.pubId, pub.keywords);
    } catch (Exception e) {
      logger.error("基准库保存关键词服务：保存关键词出错！keywords={}", pub.keywords, e);
      throw new PubHandlerAssemblyException(this.getClass().getSimpleName() + "基准库保存成果关键词出错！", e);
    }
    return null;
  }

}
