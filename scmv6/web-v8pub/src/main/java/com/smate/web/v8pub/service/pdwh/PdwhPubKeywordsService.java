package com.smate.web.v8pub.service.pdwh;

import java.util.List;

import com.smate.web.v8pub.exception.ServiceException;
import com.smate.web.v8pub.po.pdwh.PdwhPubKeywordsPO;
import com.smate.web.v8pub.service.BaseService;

/**
 * 基准库成果关键词服务接口
 * 
 * @author aijiangbin
 * @date 2018年6月1日
 */
public interface PdwhPubKeywordsService extends BaseService<Long, PdwhPubKeywordsPO> {

  public List<PdwhPubKeywordsPO> getByPubId(Long pubId) throws ServiceException;

  /**
   * 保存成果关键词
   * 
   * @param pubId
   * @param keywords
   * @throws ServiceException
   */
  void savePubKeywords(Long pdwhPubId, String keywords) throws ServiceException;
}
