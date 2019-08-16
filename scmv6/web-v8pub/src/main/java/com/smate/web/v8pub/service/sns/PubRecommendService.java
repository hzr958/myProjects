package com.smate.web.v8pub.service.sns;

import java.util.List;
import java.util.Map;

import com.smate.web.v8pub.exception.ServiceException;
import com.smate.web.v8pub.po.sns.psn.RecommendDisciplineKey;
import com.smate.web.v8pub.service.query.PubQueryDTO;
import com.smate.web.v8pub.vo.PubListVO;

public interface PubRecommendService {

  /**
   * 初始化个人科技领域和关键词
   * 
   * @param form
   * @throws ServiceException
   */
  public void initScienAreaAndCode(Long psnId) throws ServiceException;

  public void pubRecommendListSearch(PubListVO pubListVO);

  public void pubRecommendConditionsShow(PubQueryDTO pubQueryDTO);

  public Map<String, Object> pubEditScienArea(Long psnId, String addPsnAreaCode);

  public Map<String, String> pubAddScienArea(Long psnId, String addPsnAreaCode);

  public Map<String, String> pubDeleteScienArea(Long psnId, String deletePsnAreaCode);

  public Map<String, String> pubAddKeyWord(Long currentUserId, String addPsnKeyWord);

  public Map<String, String> pubDeleteKeyWord(Long currentUserId, String deletePsnKeyWord);

  /**
   * 获取所有的关键词
   * 
   * @return
   * @throws ServiceException
   */
  public List<RecommendDisciplineKey> getPsnAllKeyWord(Long psnId) throws ServiceException;

  /**
   * 保存移动端的科技领域
   */
  public void savePsnArea(String areasStr, Long psnId) throws ServiceException;

  /**
   * 保存移动端的关键词
   */
  public void savePsnKeyWord(String jsonKeyWord, Long psnId) throws ServiceException;

  public Map<String, Object> pubSaveKeyWord(Long currentUserId, String addPsnKeyWord);

  void pubRecommendConditions(PubQueryDTO pubQueryDTO);

  void insertPubRecmRecord(Long psnId, Long pubId) throws ServiceException;
}
