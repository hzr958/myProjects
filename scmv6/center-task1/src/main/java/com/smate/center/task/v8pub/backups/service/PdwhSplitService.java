package com.smate.center.task.v8pub.backups.service;

import com.smate.center.task.exception.ServiceException;
import com.smate.web.v8pub.dom.pdwh.PubPdwhDetailDOM;

public interface PdwhSplitService {

  /**
   * 备份json数据
   * 
   * @param pubSnsDetailDOM
   * @throws ServiceException
   */
  void backUpPdwhPubJson(PubPdwhDetailDOM pubPdwhDetailDOM) throws ServiceException;

  /**
   * 备份doi数据
   * 
   * @param pubSnsDetailDOM
   * @throws ServiceException
   */
  void backUpPdwhPubDoi(PubPdwhDetailDOM pubPdwhDetailDOM) throws ServiceException;

  /**
   * 备份期刊数据
   * 
   * @param pubSnsDetailDOM
   * @throws ServiceException
   */
  void backUpPdwhPubJournal(PubPdwhDetailDOM pubPdwhDetailDOM) throws ServiceException;

  /**
   * 备份专利数据
   * 
   * @param pubSnsDetailDOM
   * @throws ServiceException
   */
  void backUpPdwhPubPatent(PubPdwhDetailDOM pubPdwhDetailDOM) throws ServiceException;

  /**
   * 备份单位数据
   * 
   * @param pubPdwhDetailDOM
   * @throws ServiceException
   */
  void backUpPdwhPubIns(PubPdwhDetailDOM pubPdwhDetailDOM) throws ServiceException;

}
