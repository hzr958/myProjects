package com.smate.center.task.service.sns.quartz;

import java.util.List;

import com.smate.center.task.exception.ServiceException;

/**
 * 人员关键词任务service
 * 
 * @author zjh
 *
 */
public interface PsnKwEptSevice {
  public int getKeyWordsCommendFlag() throws ServiceException;

  public List<Long> getRefreshPsn(Long StartId) throws ServiceException;

  public void updateFlag() throws ServiceException;


}
