package com.smate.web.v8pub.service.handler.assembly.snspubsave;

import java.util.Date;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.pub.model.PubIndexUrl;
import com.smate.web.v8pub.exception.PubHandlerAssemblyException;
import com.smate.web.v8pub.service.handler.PubDTO;
import com.smate.web.v8pub.service.handler.assembly.PubHandlerAssemblyService;
import com.smate.web.v8pub.service.sns.indexurl.PubIndexUrlService;

/**
 * 个人库成果短地址保存/更新
 * 
 * @author YJ
 *
 *         2018年7月24日
 */
@Transactional(rollbackFor = Exception.class)
public class ASPubIndexUrlSaveImpl implements PubHandlerAssemblyService {

  private Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private PubIndexUrlService pubIndexUrlService;

  @Override
  public void checkSourcesParameter(PubDTO pub) throws PubHandlerAssemblyException {}

  @Override
  public void checkRebuildParameter(PubDTO pub) throws PubHandlerAssemblyException {
    // TODO Auto-generated method stub

  }

  @Override
  public Map<String, String> excute(PubDTO pub) throws PubHandlerAssemblyException {
    try {
      PubIndexUrl pubIndexUrl = pubIndexUrlService.get(pub.pubId);
      if (pubIndexUrl == null) {
        pubIndexUrl = new PubIndexUrl();
        pubIndexUrl.setPubId(pub.pubId);
      }
      pubIndexUrl.setPubIndexUrl(pub.pubIndexUrl);
      pubIndexUrl.setUpdateDate(new Date());
      pubIndexUrl.setPubLongIndexUrl(pub.pubLongIndexUrl);
      pubIndexUrlService.saveOrUpdate(pubIndexUrl);
      logger.debug("更新或保存成果短地址记录成功");
    } catch (Exception e) {
      logger.error("成果短地址服务:保存或更新成果短地址出错！pubId={}", pub.pubId);
      throw new PubHandlerAssemblyException(this.getClass().getSimpleName() + "保存或更新sns库成果短地址出错!", e);
    }

    return null;
  }

}
