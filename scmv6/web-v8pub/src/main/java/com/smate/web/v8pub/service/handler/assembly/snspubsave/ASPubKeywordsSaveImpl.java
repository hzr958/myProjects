package com.smate.web.v8pub.service.handler.assembly.snspubsave;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.smate.web.v8pub.exception.PubHandlerAssemblyException;
import com.smate.web.v8pub.exception.ServiceException;
import com.smate.web.v8pub.service.handler.PubDTO;
import com.smate.web.v8pub.service.handler.assembly.PubHandlerAssemblyService;
import com.smate.web.v8pub.service.sns.PubKeywordsService;

/**
 * 个人库成果关键字保存
 * 
 * @author yhx
 *
 */
@Transactional(rollbackFor = Exception.class)
public class ASPubKeywordsSaveImpl implements PubHandlerAssemblyService {

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private PubKeywordsService pubKeywordsService;

  @Override
  public void checkSourcesParameter(PubDTO pub) throws PubHandlerAssemblyException {

  }

  @Override
  public void checkRebuildParameter(PubDTO pub) throws PubHandlerAssemblyException {
    // TODO Auto-generated method stub

  }

  @Override
  public Map<String, String> excute(PubDTO pub) throws PubHandlerAssemblyException {
    try {
      // 保存成果关键字
      pubKeywordsService.savePubKeywords(pub.pubId, pub.keywords);
      logger.debug("更新或保存成果关键词记录成功");
    } catch (ServiceException e) {
      logger.error("更新sns库成果关键字表出错！pubId={}", pub.pubId, e);
      throw new PubHandlerAssemblyException(this.getClass().getSimpleName() + "保存或更新sns库关键词出错!", e);
    }
    return null;
  }

}
