package com.smate.sie.center.task.service;

import java.util.Date;
import java.util.List;

import com.smate.core.base.exception.ServiceException;
import com.smate.sie.core.base.utils.model.statistics.SieSTPatSyncRefresh;
import com.smate.sie.core.base.utils.model.statistics.SieSTPubSyncRefresh;

/**
 * 社交行为付费表服务
 * 
 * @author hd
 *
 */
public interface SieKpiPayBehaveService {

  public Long cntNeedDeal(Date nowDate) throws ServiceException;

  public void setNeedDealPuball(Date now) throws ServiceException;

  public void setNeedDealPatall(Date now) throws ServiceException;

  public List<SieSTPubSyncRefresh> LoadNeedDealPubRecords(int maxSize) throws ServiceException;

  public List<SieSTPatSyncRefresh> LoadNeedDealPatRecords(int maxSize) throws ServiceException;

  public void saveSTPubSyncRefresh(SieSTPubSyncRefresh refresh) throws ServiceException;

  public void saveSTPatSyncRefresh(SieSTPatSyncRefresh refresh) throws ServiceException;


}
