package com.smate.web.v8pub.service.handler.assembly.snspubdelete;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.smate.web.v8pub.exception.PubHandlerAssemblyException;
import com.smate.web.v8pub.po.repeatpub.PubSameItemPO;
import com.smate.web.v8pub.po.repeatpub.PubSameRecordPO;
import com.smate.web.v8pub.service.handler.PubDTO;
import com.smate.web.v8pub.service.handler.assembly.PubHandlerAssemblyService;
import com.smate.web.v8pub.service.repeatpub.PubSameItemService;
import com.smate.web.v8pub.service.repeatpub.PubSameRecordService;

/**
 * 删除重复成果记录
 * 
 * @author YJ
 *
 *         2018年9月13日
 */
@Transactional(rollbackFor = Exception.class)
public class ASRepeatPubRecordDeleteImpl implements PubHandlerAssemblyService {

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private PubSameItemService pubSameItemService;
  @Autowired
  private PubSameRecordService pubSameRecordService;

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
      List<PubSameItemPO> samePubList = pubSameItemService.getByPsnIdAndPubId(pub.pubId, pub.psnId);
      if (samePubList != null && samePubList.size() > 0) {
        for (PubSameItemPO pubSameItemPO : samePubList) {
          // 设置重复成果删除
          pubSameItemPO.setDealStatus(2);
          pubSameItemService.update(pubSameItemPO);
          List<PubSameItemPO> items = pubSameItemService.getNoDealPubSameItems(pubSameItemPO.getRecordId(),
              pubSameItemPO.getPsnId(), pubSameItemPO.getId());
          if (items != null && items.size() == 1) {
            // 设置为保留
            PubSameItemPO item = items.get(0);
            item.setDealStatus(1);
            item.setUpdateDate(new Date());
            pubSameItemService.update(item);
            // 设置组状态为已处理
            PubSameRecordPO pubSameRecord = pubSameRecordService.get(pubSameItemPO.getRecordId());
            pubSameRecord.setDealStatus(1);
            item.setUpdateDate(new Date());
            pubSameRecordService.update(pubSameRecord);
          }
        }
      }
    } catch (Exception e) {
      logger.error("删除重复成果记录出错！", e);
    }
    return null;
  }

}
