package com.smate.web.v8pub.service.handler.assembly.snspubupdate;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import com.smate.web.v8pub.dom.sns.PubSnsDetailDOM;
import com.smate.web.v8pub.exception.PubHandlerAssemblyException;
import com.smate.web.v8pub.service.handler.PubDTO;
import com.smate.web.v8pub.service.handler.assembly.PubHandlerAssemblyService;
import com.smate.web.v8pub.service.sns.PubSnsDetailService;

/**
 * 更新成果详情表的citations字段值
 * 
 * @author YJ
 *
 *         2018年8月29日
 */
@Transactional(rollbackFor = Exception.class)
public class ASPubDetailCitationsUpdateImpl implements PubHandlerAssemblyService {

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
      PubSnsDetailDOM pubSnsDetail = pubSnsDetailService.getByPubId(pub.pubId);
      if (pubSnsDetail != null) {
        pubSnsDetail.setCitations(pub.citations);
        pubSnsDetailService.saveOrUpdate(pubSnsDetail);
      }
    } catch (Exception e) {
      logger.error("更新成果详情表的citations字段值出错！", e);
      throw new PubHandlerAssemblyException(this.getClass().getSimpleName() + "更新成果详情表的citations字段值出错！", e);
    }
    return null;
  }

}
