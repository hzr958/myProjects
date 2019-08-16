package com.smate.web.v8pub.service.handler.assembly.snspubupdate;

import java.util.Date;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import com.smate.web.v8pub.exception.PubHandlerAssemblyException;
import com.smate.web.v8pub.po.sns.PubSnsPO;
import com.smate.web.v8pub.service.handler.PubDTO;
import com.smate.web.v8pub.service.handler.assembly.PubHandlerAssemblyService;
import com.smate.web.v8pub.service.sns.PubSnsService;

/**
 * 更新成果主表的citations字段值
 * 
 * @author YJ
 *
 *         2018年8月29日
 */
@Transactional(rollbackFor = Exception.class)
public class ASPubSnsCitationsUpdateImpl implements PubHandlerAssemblyService {

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private PubSnsService pubSnsService;

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
      PubSnsPO pubSnsPO = pubSnsService.get(pub.pubId);
      if (pubSnsPO != null) {
        pubSnsPO.setCitations(pub.citations);
        pubSnsPO.setGmtModified(new Date());
        pubSnsService.saveOrUpdate(pubSnsPO);
      }
    } catch (Exception e) {
      logger.error("更新成果主表的citations字段值出错！", e);
      throw new PubHandlerAssemblyException(this.getClass().getSimpleName() + "更新成果主表的citations字段值出错", e);
    }
    return null;
  }

}
