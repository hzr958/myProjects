package com.smate.web.v8pub.service.sns;

import java.util.List;

import com.smate.web.v8pub.exception.ServiceException;
import com.smate.web.v8pub.po.sns.PubKeywordsPO;

/**
 * 成果关键词服务接口
 * 
 * @author aijiangbin
 * @date 2018年6月1日
 */
public interface PubKeywordsService {

  public List<PubKeywordsPO> getByPubId(Long pubId) throws ServiceException;

  public void saveOrUpdate(PubKeywordsPO entity) throws ServiceException;

  public void deleteById(Long id) throws ServiceException;

  public void savePubKeywords(Long pubId, String keywords) throws ServiceException;

}
