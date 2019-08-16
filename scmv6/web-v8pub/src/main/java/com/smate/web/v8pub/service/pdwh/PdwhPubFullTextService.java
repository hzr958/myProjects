package com.smate.web.v8pub.service.pdwh;

import com.smate.web.v8pub.exception.ServiceException;
import com.smate.web.v8pub.po.pdwh.PdwhPubFullTextPO;
import com.smate.web.v8pub.service.BaseService;
import com.smate.web.v8pub.service.query.PubQueryDTO;

import java.util.List;

public interface PdwhPubFullTextService extends BaseService<Long, PdwhPubFullTextPO> {

  /**
   * 通过ids 查找全文
   * 
   * @param pubQueryDTO
   * @throws ServiceException
   */
  List<PdwhPubFullTextPO> queryPdwhFullTextByIds(PubQueryDTO pubQueryDTO) throws ServiceException;

  /**
   * 通过pubId获取成果全文对象
   * 
   * @param pubId
   * @return
   */
  PdwhPubFullTextPO getPdwhPubfulltext(Long pubId) throws ServiceException;

  /**
   * 通过pubId和fileId删除全文
   * 
   * @param pubId
   * @param fileId
   * @throws ServiceException
   */
  public PdwhPubFullTextPO getFullText(Long pubId, Long fileId) throws ServiceException;

  /**
   * 获取基准库成果全文的数量
   * 
   * @param pdwhPubId
   * @return
   */
  Long getCountByPdwhPubId(Long pdwhPubId) throws ServiceException;
}
