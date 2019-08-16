package com.smate.center.task.v8pub.backups.service;

import com.smate.center.task.exception.ServiceException;
import com.smate.web.v8pub.dom.sns.PubSnsDetailDOM;

public interface PubSplitService {

  /**
   * 备份json数据
   * 
   * @param pubSnsDetailDOM
   * @throws ServiceException
   */
  void backUpSnsPubJson(PubSnsDetailDOM pubSnsDetailDOM) throws ServiceException;

  /**
   * 备份doi数据
   * 
   * @param pubSnsDetailDOM
   * @throws ServiceException
   */
  void backUpSnsPubDoi(PubSnsDetailDOM pubSnsDetailDOM) throws ServiceException;

  /**
   * 备份期刊数据
   * 
   * @param pubSnsDetailDOM
   * @throws ServiceException
   */
  void backUpSnsPubJournal(PubSnsDetailDOM pubSnsDetailDOM) throws ServiceException;

  /**
   * 备份专利数据
   * 
   * @param pubSnsDetailDOM
   * @throws ServiceException
   */
  void backUpSnsPubPatent(PubSnsDetailDOM pubSnsDetailDOM) throws ServiceException;
}
