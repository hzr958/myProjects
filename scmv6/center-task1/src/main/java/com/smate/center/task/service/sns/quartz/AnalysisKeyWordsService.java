package com.smate.center.task.service.sns.quartz;

import java.util.List;

import com.smate.center.task.exception.ServiceException;
import com.smate.center.task.model.sns.quartz.PubKeyWords;
import com.smate.center.task.model.sns.quartz.PubMember;

/**
 * 解析关键词
 * 
 * @author Administrator
 *
 */
public interface AnalysisKeyWordsService {
  public void analyzeKeyWords(Long psnId) throws ServiceException;

  public void analysisKeyWords(List<Long> psnKwEptRefreshList, String startId) throws ServiceException;

  PubMember matchOwnerPubMember(Long psnId, List<PubMember> pubMemberList) throws ServiceException;

  void buildkw(PubKeyWords rcmdPubKeyword) throws ServiceException;

}
