package com.smate.center.task.service.sns.quartz;

import com.smate.center.task.exception.ServiceException;

public interface ParsePsnKeyWordsService {
  public void parsePsnKeywords(Long psnId, int result) throws ServiceException;

  public void parseSourceType(Long psnId) throws ServiceException;

  public void updatePsnKwTmp(Long psnId) throws ServiceException;

  public void statPsnKeywords(Long psnId) throws ServiceException;

}
