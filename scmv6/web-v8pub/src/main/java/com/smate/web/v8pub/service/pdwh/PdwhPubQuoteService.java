package com.smate.web.v8pub.service.pdwh;

import java.util.List;
import java.util.Map;

import com.smate.web.v8pub.exception.ServiceException;
import com.smate.web.v8pub.vo.PubQuoteVO;

public interface PdwhPubQuoteService {

  /**
   * 获取成果的所有引用模板
   * 
   * @param pubId
   * @return
   * @throws ServiceException
   */
  public List<PubQuoteVO> findPubQuote(Long pubId) throws ServiceException;

}
