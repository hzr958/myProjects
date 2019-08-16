package com.smate.web.v8pub.service.handler.assembly.snspubdelete;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.pub.enums.PubPdwhStatusEnum;
import com.smate.web.v8pub.exception.PubHandlerAssemblyException;
import com.smate.web.v8pub.service.handler.PubDTO;
import com.smate.web.v8pub.service.handler.assembly.PubHandlerAssemblyService;
import com.smate.web.v8pub.service.pdwh.PubPdwhService;

/**
 * 更新pdwh库成果status字段为删除状态
 * 
 * @author YHX
 *
 *         2019年4月17日
 */
@Transactional(rollbackFor = Exception.class)
public class ASPubPdwhDeleteImpl implements PubHandlerAssemblyService {

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
      pubPdwhService.updateStatusByPubId(pub.pubId, PubPdwhStatusEnum.DELETED);
    } catch (Exception e) {
      logger.error("更新pdwh库成果主表的status字段出错！pdwhPubId={}", pub.pubId, e);
      throw new PubHandlerAssemblyException(e);
    }
    return null;
  }

}
