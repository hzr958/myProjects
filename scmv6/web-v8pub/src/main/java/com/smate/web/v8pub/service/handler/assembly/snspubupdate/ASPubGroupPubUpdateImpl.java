package com.smate.web.v8pub.service.handler.assembly.snspubupdate;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.smate.web.v8pub.exception.PubHandlerAssemblyException;
import com.smate.web.v8pub.service.handler.PubDTO;
import com.smate.web.v8pub.service.handler.assembly.PubHandlerAssemblyService;
import com.smate.web.v8pub.service.sns.GroupPubService;

/**
 * 群组与个人库成果关系 对表中update_date 字段进行更新时间
 * 
 * @author YJ
 *
 *         2018年7月20日
 */
@Transactional(rollbackFor = Exception.class)
public class ASPubGroupPubUpdateImpl implements PubHandlerAssemblyService {

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private GroupPubService groupPubService;

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

    // 更新群组成果更新字段
    try {
      groupPubService.updateGrpPubsGmtModified(pub.pubId);
      logger.debug("更新sns库群组成果修改字段成功");
    } catch (Exception e) {
      logger.error("更新sns库群组成果关系表的更新字段出错！pubId={}", pub.pubId, e);
      throw new PubHandlerAssemblyException(this.getClass().getSimpleName() + "更新sns库群组成果关系的更新字段出错!", e);
    }

    return null;
  }

}
