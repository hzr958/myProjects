package com.smate.web.v8pub.service.handler.assembly.pdwhpubsave;

import java.util.Date;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.smate.web.v8pub.exception.PubHandlerAssemblyException;
import com.smate.web.v8pub.po.pdwh.PdwhPubIndexUrl;
import com.smate.web.v8pub.service.handler.PubDTO;
import com.smate.web.v8pub.service.handler.assembly.PubHandlerAssemblyService;
import com.smate.web.v8pub.service.pdwh.indexurl.PdwhPubIndexUrlService;

/**
 * 基准库成果主表 保存/更新
 * 
 * @author YJ
 *
 *         2018年7月25日
 */
@Transactional(rollbackFor = Exception.class)
public class ASPdwhIndexUrlSaveImpl implements PubHandlerAssemblyService {

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private PdwhPubIndexUrlService pdwhPubIndexUrlService;

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
      PdwhPubIndexUrl pdwhPubIndexUrl = pdwhPubIndexUrlService.get(pub.pubId);
      if (pdwhPubIndexUrl == null) {
        pdwhPubIndexUrl = new PdwhPubIndexUrl();
        pdwhPubIndexUrl.setPubId(pub.pubId);
      } else {
        pub.pubIndexUrl = pdwhPubIndexUrl.getPubIndexUrl();
      }
      pdwhPubIndexUrl.setPubIndexUrl(pub.pubIndexUrl);
      pdwhPubIndexUrl.setUpdateDate(new Date());
      pdwhPubIndexUrlService.saveOrUpdate(pdwhPubIndexUrl);
    } catch (Exception e) {
      logger.error("基准库成果短地址服务:保存或更新成果短地址出错！pdwhPubId={}", pub.pubId, e);
      throw new PubHandlerAssemblyException(this.getClass().getSimpleName() + "基准库保存或更新成果短地址表出错！", e);
    }
    return null;
  }

}
