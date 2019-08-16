package com.smate.center.batch.service.pub;

import java.io.Serializable;
import java.util.List;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.sns.pub.PubFundinfo;

/**
 * 成果基金标注SERVICE接口.
 * 
 * @author xys
 * 
 */
public interface PubFundinfoService extends Serializable {

  /**
   * 同步成果基金标注.
   * 
   * @param pubId
   * @param psnId
   * @param typeId
   * @param fundinfo
   * @throws ServiceException
   */
  void syncPubFundinfo(Long pubId, Long psnId, Integer typeId, String fundinfo) throws ServiceException;

  /**
   * 根据人员id与基金标注匹配查找项目相关成果id.
   * 
   * @param psnId
   * @param fundinfo
   * @return
   * @throws ServiceException
   */
  List<Long> getPrjRelatedPubIds(Long psnId, String fundinfo) throws ServiceException;

  /**
   * 获取成果基金标注.
   * 
   * @param pubId
   * @return
   * @throws ServiceException
   */
  PubFundinfo getPubFundinfo(Long pubId) throws ServiceException;
}
