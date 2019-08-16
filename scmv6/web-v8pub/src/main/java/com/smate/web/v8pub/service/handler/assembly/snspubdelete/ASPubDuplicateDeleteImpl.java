package com.smate.web.v8pub.service.handler.assembly.snspubdelete;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.smate.web.v8pub.exception.PubHandlerAssemblyException;
import com.smate.web.v8pub.service.handler.PubDTO;
import com.smate.web.v8pub.service.handler.assembly.PubHandlerAssemblyService;
import com.smate.web.v8pub.service.sns.PubDuplicateService;

/**
 * 个人库成果查重记录删除
 * 
 * @author YJ
 *
 *         2018年7月20日
 */
@Transactional(rollbackFor = Exception.class)
public class ASPubDuplicateDeleteImpl implements PubHandlerAssemblyService {

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private PubDuplicateService pubDuplicateService;

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
      if (pubDuplicateService.get(pub.pubId) != null) {
        // 有查重记录时才进行删除操作
        pubDuplicateService.deleteById(pub.pubId);
      }
    } catch (Exception e) {
      logger.error("删除sns库成果查重记录出错！pubId={}", pub.pubId, e);
      throw new PubHandlerAssemblyException(this.getClass().getSimpleName() + "删除sns库成果查重记录出错!", e);
    }

    return null;
  }

}
