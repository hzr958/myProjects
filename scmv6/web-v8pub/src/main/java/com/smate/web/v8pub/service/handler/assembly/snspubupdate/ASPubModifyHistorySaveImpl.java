package com.smate.web.v8pub.service.handler.assembly.snspubupdate;

import java.util.Date;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.smate.web.v8pub.exception.PubHandlerAssemblyException;
import com.smate.web.v8pub.po.sns.PubModifyHistory;
import com.smate.web.v8pub.po.sns.PubSnsPO;
import com.smate.web.v8pub.service.handler.PubDTO;
import com.smate.web.v8pub.service.handler.assembly.PubHandlerAssemblyService;
import com.smate.web.v8pub.service.sns.PubModifyHistoryService;
import com.smate.web.v8pub.service.sns.PubSnsService;

/**
 * 保存成果修改历史记录
 * 
 * @author yhx
 *
 */
@Transactional(rollbackFor = Exception.class)
public class ASPubModifyHistorySaveImpl implements PubHandlerAssemblyService {

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private PubModifyHistoryService pubModifyHistoryService;
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
    PubModifyHistory pubModifyHistory = pubModifyHistoryService.findListByPubIdAndPsnId(pub.pubId, pub.psnId);
    PubModifyHistory newPubModify = new PubModifyHistory();
    try {
      if (pubModifyHistory != null) {
        newPubModify.setVersion(pubModifyHistory.getVersion() + 1);
      } else {
        newPubModify.setVersion(10000L);// 版本号 默认10000
      }
      Date date = new Date();
      newPubModify.setPsnId(pub.psnId);
      newPubModify.setPubId(pub.pubId);
      newPubModify.setPubJson(pub.historyDetailsJson);
      newPubModify.setCreateDate(date);
      pubModifyHistoryService.savePubModifyHistory(newPubModify);
      // 更新个人库成果的版本号
      PubSnsPO pubSns = pubSnsService.queryPubSns(newPubModify.getPubId());
      pubSns.setVersion(newPubModify.getVersion());
      pubSns.setGmtModified(date);
      pubSnsService.save(pubSns);
    } catch (Exception e) {
      logger.error("保存成果修改历史记录表对象出错！对象属性={}", newPubModify, e);
      throw new PubHandlerAssemblyException(this.getClass().getSimpleName() + "保存成果修改历史记录表出错!", e);
    }
    return null;
  }

}
