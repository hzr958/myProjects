package com.smate.web.v8pub.service.handler.assembly.snspubdelete;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.smate.web.v8pub.exception.PubHandlerAssemblyException;
import com.smate.web.v8pub.service.handler.PubDTO;
import com.smate.web.v8pub.service.handler.assembly.PubHandlerAssemblyService;
import com.smate.web.v8pub.service.sns.PubPdwhSnsRelationService;

/**
 * 删除基准库与个人库成果关系记录
 * 
 * @author YHX
 *
 *         2019年4月17日
 */
@Transactional(rollbackFor = Exception.class)
public class ASPubPdwhSnsRelationDeleteImpl implements PubHandlerAssemblyService {

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private PubPdwhSnsRelationService pubPdwhSnsRelationService;

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
      pubPdwhSnsRelationService.delPubPdwhSnsRelationByPdwhPubId(pub.pubId);
    } catch (Exception e) {
      logger.error("根据pdwhPubId删除基准库与个人库成果关系出错！pdwhPubId={}", pub.pubId, e);
      throw new PubHandlerAssemblyException(e);
    }
    return null;
  }

}
