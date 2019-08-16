package com.smate.center.task.v8pub.backups.service;

import java.util.List;
import com.smate.center.task.exception.ServiceException;
import com.smate.center.task.v8pub.backups.model.PubDataBackups;

/**
 * 成果备份处理的Service
 * 
 * @author YJ
 *
 *         2018年11月15日
 */
public interface PubBackupsService {

  /**
   * 获取一次需要处理的数据集合
   * 
   * @param SIZE 一次性处理的数量
   * @return
   * @throws ServiceException
   */
  List<PubDataBackups> findPubNeedDeal(Integer SIZE, Integer dataType) throws ServiceException;

  /**
   * 保存备份数据
   * 
   * @param pubData
   * @throws ServiceException
   */
  void save(PubDataBackups pubData) throws ServiceException;

  /**
   * sns库成果数据备份
   * 
   * @param pubData
   */
  void backupSnsData(PubDataBackups pubData) throws ServiceException;

  /**
   * pdwh库成果数据备份
   * 
   * @param pubData
   */
  void backupPdwhData(PubDataBackups pubData) throws ServiceException;


}
