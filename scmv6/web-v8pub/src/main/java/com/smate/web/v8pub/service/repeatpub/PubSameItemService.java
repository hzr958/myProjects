package com.smate.web.v8pub.service.repeatpub;

import com.smate.web.v8pub.exception.ServiceException;
import com.smate.web.v8pub.po.repeatpub.PubSameItemPO;
import com.smate.web.v8pub.service.BaseService;

import java.util.List;

public interface PubSameItemService extends BaseService<Long, PubSameItemPO> {

  /**
   * 获取还未处理的重复成果数组
   * 
   * @param recordId
   * @param userId
   * @param pubSameItemId
   * @return
   */
  List<PubSameItemPO> getNoDealPubSameItems(Long recordId, Long userId, Long pubSameItemId);

  /**
   * 通过psnId和pubId获取重复成果数组
   * 
   * @param pubId
   * @param psnId
   * @return
   * @throws ServiceException
   */
  List<PubSameItemPO> getByPsnIdAndPubId(Long pubId, Long psnId) throws ServiceException;

}
