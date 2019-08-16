package com.smate.web.mobile.v8pub.service;


import com.smate.web.mobile.v8pub.vo.pdwh.PubRecommendVO;

/**
 * 成果推荐接口
 * 
 * @author ltl
 *
 */
public interface PubRecommendRestService {
  /**
   * 论文推荐获取设置条件接口
   * 
   * @return
   */
  public PubRecommendVO pubRecommendConditional(Long psnId, PubRecommendVO pubVO);

  /**
   * 论文推荐查询接口
   * 
   * @param pubQueryDTO
   * @return
   */
  public void searchPubList(PubRecommendVO pubRecommendVO);

  /**
   * 添加科技领域
   */
  public String addScienArea(String des3PsnId, String addPsnAreaCode);

  /**
   * 删除科技领域
   */
  public String deleteScienArea(String des3PsnId, String deletePsnAreaCode);

  /**
   * 增加关键词
   */
  public String addKeyWord(String des3PsnId, String addPsnKeyWord);

  /**
   * 删除关键词
   */
  public String deleteKeyWord(String des3PsnId, String deletePsnKeyWord);

  /**
   * 移动端添加科技领域
   */
  public String addMobileScienAreas(String des3PsnId, String addPsnAreaCodes);

  /**
   * 移动端增加关键词
   */
  public String addMobileKeyWords(String des3PsnId, String addPsnKeyWords);

  /**
   * 获取所有的科技领域
   * 
   * @return
   * @throws ServiceException
   */
  public void getAllScienceArea(PubRecommendVO pubRecommendVO);

  /**
   * 获取所有的关键词
   * 
   * @return
   * @throws ServiceException
   */
  public void getPsnAllKeyWord(PubRecommendVO pubRecommendVO);
}
