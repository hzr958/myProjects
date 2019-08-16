package com.smate.center.task.service.pdwh.quartz;

import java.util.List;

import com.smate.center.task.exception.ServiceException;
import com.smate.center.task.model.pdwh.pub.PdwhPubKeywords20180511;

/**
 * 基准库成果关键词服务
 * 
 * @author aijiangbin
 * @date 2018年4月24日
 */
public interface PdwhPublicationKeywordService {

  public List<PdwhPubKeywords20180511> getNoDealPdwhPubKeywordsList(int size) throws ServiceException;

  public void dealPdwhPubKeywords(PdwhPubKeywords20180511 pdwhPubKeywords) throws ServiceException;


}
