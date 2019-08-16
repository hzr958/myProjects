package com.smate.web.v8pub.service.handler.assembly.snspubupdate;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.utils.number.NumberUtils;
import com.smate.web.v8pub.dom.sns.PubSnsDetailDOM;
import com.smate.web.v8pub.exception.PubHandlerAssemblyException;
import com.smate.web.v8pub.service.handler.PubDTO;
import com.smate.web.v8pub.service.handler.assembly.PubHandlerAssemblyService;
import com.smate.web.v8pub.service.sns.PubSnsDetailService;

/**
 * sns库成果详情fulltextId字段更新
 * 
 * @author YJ
 *
 *         2018年8月27日
 */
@Transactional(rollbackFor = Exception.class)
public class ASSnsDetailFulltextIdUpdateImpl implements PubHandlerAssemblyService {

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private PubSnsDetailService pubSnsDetailService;

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
      PubSnsDetailDOM pubDetail = pubSnsDetailService.get(pub.pubId);
      if (pubDetail != null && !NumberUtils.isNullOrZero(pub.fulltextId)) {
        pubDetail.setFulltextId(pub.fulltextId);
        pubSnsDetailService.saveOrUpdate(pubDetail);
      }
    } catch (Exception e) {
      logger.error("sns库成果详情fulltextId字段更新出错！", e);
      throw new PubHandlerAssemblyException(this.getClass().getSimpleName() + "sns库成果详情fulltextId字段更新出错", e);
    }
    return null;
  }

}
