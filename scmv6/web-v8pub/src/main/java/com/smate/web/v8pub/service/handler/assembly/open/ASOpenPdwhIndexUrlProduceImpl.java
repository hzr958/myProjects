package com.smate.web.v8pub.service.handler.assembly.open;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.utils.constant.ShortUrlConst;
import com.smate.web.v8pub.exception.PubHandlerAssemblyException;
import com.smate.web.v8pub.service.handler.PubDTO;
import com.smate.web.v8pub.service.handler.assembly.PubHandlerAssemblyService;
import com.smate.web.v8pub.service.sns.indexurl.PubIndexUrlService;

/**
 * open系统-产生成果短地址服务
 * 
 * @author YJ
 *
 *         2018年7月26日
 */
@Transactional(rollbackFor = Exception.class)
public class ASOpenPdwhIndexUrlProduceImpl implements PubHandlerAssemblyService {

  private Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private PubIndexUrlService pubIndexUrlService;

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
      if (!pub.pubHandlerName.contains("save")) {
        // 保存成果产生短地址
        return null;
      }
      pub.pubIndexUrl = pubIndexUrlService.producePubShortUrl(pub.pubId, ShortUrlConst.S_TYPE);
    } catch (Exception e) {
      logger.error("生成成果短地址异常！pubId={}", pub.pubId, e);
      throw new PubHandlerAssemblyException(this.getClass().getSimpleName() + "基准库生成成果短地址出错！", e);
    }
    return null;
  }

}
