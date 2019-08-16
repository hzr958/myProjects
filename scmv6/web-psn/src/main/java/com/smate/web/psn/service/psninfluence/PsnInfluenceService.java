package com.smate.web.psn.service.psninfluence;

import java.util.Map;

import com.smate.web.psn.exception.PsnException;
import com.smate.web.psn.exception.ServiceException;
import com.smate.web.psn.model.influence.InfluenceForm;

/**
 * 人员影响力服务
 * 
 * @author wsn
 * @date 2018年6月11日
 */
public interface PsnInfluenceService {

  /**
   * 查找人员各项统计数
   * 
   * @param form
   * @return InfluenceForm对象
   * @throws PsnException
   */
  public InfluenceForm findPsnStatistics(InfluenceForm form) throws PsnException;

  /**
   * 查找人员阅读趋势数据
   * 
   * @param form
   * @return
   * @throws PsnException
   */
  public InfluenceForm findPsnResReadTrend(InfluenceForm form) throws PsnException;

  /**
   * 获取人员成果Hindex画图所需信息
   * 
   * @param form
   * @return
   * @throws PsnException
   */
  public InfluenceForm findPsnHindexInfo(InfluenceForm form) throws PsnException;

  /**
   * 获取阅读省份统计数
   * 
   * @param psnId
   * @param resultMap
   * @return
   * @throws ServiceException
   */
  public Map<String, Object> getVisitMapData(Long psnId, Map<String, Object> resultMap) throws ServiceException;

  /**
   * 单位分布
   * 
   * @param form
   * @throws ServiceException
   */
  public void getVisitIns(InfluenceForm form) throws ServiceException;

  /**
   * 职称分布分布
   * 
   * @param form
   * @throws ServiceException
   */
  public void getVisitPos(InfluenceForm form) throws ServiceException;

  /**
   * 查找人员hindex在好友中的排名
   * 
   * @param form
   * @return
   * @throws PsnException
   */
  public InfluenceForm findPsnHindexRanking(InfluenceForm form) throws PsnException;

  /**
   * 查找人员访问数在好友中的排名
   * 
   * @param form
   * @return
   * @throws PsnException
   */
  public InfluenceForm findPsnVisitSumRanking(InfluenceForm form) throws PsnException;

  /**
   * 查找引用数趋势数据
   * 
   * @param form
   * @throws PsnException
   */
  public void findPubCiteTrend(InfluenceForm form) throws PsnException;

  /**
   * 更新人员H-index值
   * 
   * @throws PsnException
   */
  public void updatePsnHindex(InfluenceForm form) throws PsnException;
}
