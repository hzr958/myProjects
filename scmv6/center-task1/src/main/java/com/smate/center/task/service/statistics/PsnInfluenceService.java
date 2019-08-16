package com.smate.center.task.service.statistics;

import java.util.List;
import java.util.Map;

import com.smate.center.task.exception.ServiceException;
import com.smate.center.task.model.sns.psn.ETemplateInfluenceCount;
import com.smate.core.base.psn.model.PsnStatistics;


/**
 * 人员影响力服务
 * 
 * @author wsn
 * @date 2018年6月11日
 */
public interface PsnInfluenceService {

  public List<Map> findVistPsn(Integer size) throws ServiceException;

  public List<Map> findAwardPsn(Integer size) throws ServiceException;

  public List<Map> findSharePsn(Integer size) throws ServiceException;

  public List<Map> findDownloadCollectStatistics(Integer size) throws ServiceException;

  public void findDownloadCollectStatistics(List<Map> dcRecordList) throws ServiceException;

  public List<Map> getLastMonthPsnPubs(Integer size) throws ServiceException;

  public List<Map> getLastMonthPsnCitedTimes(Integer size) throws ServiceException;

  public Long checkLastMonthHadSend() throws ServiceException;

  List<ETemplateInfluenceCount> findETemplateInfluenceCount(int size) throws ServiceException;

  public void updateInfluence(ETemplateInfluenceCount influenCount) throws ServiceException;

  public void findPubStatistics(List<Map> monthPubs) throws ServiceException;

  public void findCitedTimesStatistics(List<Map> monthPubsCitedTimes) throws ServiceException;

  public void findReadStatistics(List<Map> psnStatistics) throws ServiceException;

  public void findAwardStatistics(List<Map> awardList) throws ServiceException;

  public void findShareStatistics(List<Map> shareList) throws ServiceException;

  public List<ETemplateInfluenceCount> findETemplateInfluenceCounts(Integer size) throws ServiceException;

  public List<PsnStatistics> getHindexByPsnIds(List<Long> psnIdList) throws ServiceException;

  public void findHindexStatistics(List<PsnStatistics> pstList) throws ServiceException;

  public void dealCount(Long valueOf, ETemplateInfluenceCount influence) throws ServiceException;

}
