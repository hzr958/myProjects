package com.smate.web.v8pub.service.sns;

import java.util.List;

import com.smate.web.v8pub.exception.ServiceException;
import com.smate.web.v8pub.vo.PubQuoteVO;

/**
 * 成果引用服务
 * 
 * @author lhd
 *
 */
public interface PubQuoteService {

  /**
   * 获取成果的所有引用模板
   * 
   * @param pubId
   * @return
   * @throws ServiceException
   */
  public List<PubQuoteVO> findPubQuote(Long pubId) throws ServiceException;
}
