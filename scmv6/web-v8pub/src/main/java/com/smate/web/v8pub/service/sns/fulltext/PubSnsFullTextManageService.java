package com.smate.web.v8pub.service.sns.fulltext;

import java.util.List;

import com.smate.web.v8pub.exception.ServiceException;
import com.smate.web.v8pub.po.sns.PubAccessoryPO;
import com.smate.web.v8pub.service.handler.PubDTO;

public interface PubSnsFullTextManageService {

  /**
   * 上传个人库成果全文
   * 
   * @param pubDTO
   * @return
   * @throws ServiceException
   */
  String uploadPubFullText(PubDTO pubDTO) throws ServiceException;

  /**
   * 获取全文文件名
   * 
   * @param fileId
   * @return
   * @throws ServiceException
   */
  public String getFulltextName(Long fileId) throws ServiceException;

  /**
   * 获取成果的全文附件
   * 
   * @param pubId
   * @return
   * @throws ServiceException
   */
  public List<PubAccessoryPO> getPubAccessoryPOList(Long pubId) throws ServiceException;
}
