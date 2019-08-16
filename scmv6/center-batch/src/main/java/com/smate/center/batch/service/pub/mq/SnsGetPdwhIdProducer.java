package com.smate.center.batch.service.pub.mq;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.smate.center.batch.base.AppSettingConstants;
import com.smate.center.batch.base.AppSettingContext;
import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.core.base.utils.security.SecurityUtils;

/**
 * 发送获取基准库查询ID.
 * 
 * @author liqinghua
 * 
 */
@Component("snsGetPdwhIdProducer")
public class SnsGetPdwhIdProducer {

  @Autowired
  private GetPdwhIdConsumer getPdwhIdConsumer;

  /**
   * 发送获取基准库查询ID.
   * 
   * @param pubId
   * @param dbId
   * @param titleHash
   * @param unitHash
   * @param sourceIdHash
   * @param patentHash
   * @param psnId
   * @throws ServiceException
   */
  public void sendGetPdwhId(Long pubId, Integer dbId, Long titleHash, Long unitHash, Long sourceIdHash, Long patentHash,
      Long psnId) throws ServiceException {
    if (!isEnabled()) {
      return;
    }
    GetPdwhIdMessage msg = new GetPdwhIdMessage(pubId, psnId, dbId, titleHash, patentHash, unitHash, sourceIdHash,
        SecurityUtils.getCurrentAllNodeId().get(0));
    getPdwhIdConsumer.receive(msg);;
  }

  private boolean isEnabled() {
    return AppSettingContext.getIntValue(AppSettingConstants.GET_PDWHID_ENABLED) == 1;
  }


}
