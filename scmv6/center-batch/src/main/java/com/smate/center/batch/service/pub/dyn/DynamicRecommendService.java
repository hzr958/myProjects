package com.smate.center.batch.service.pub.dyn;

import java.util.List;

import com.smate.center.batch.exception.pub.ServiceException;

/**
 * 推荐动态服务_SCM-5913.
 * 
 * @author mjg
 * @since 2014-11-10.
 */
public interface DynamicRecommendService {

  public static final Integer SCM_RECOMMEND_DYN_TYPE_SIZE = 3;

  /**
   * 保存推荐动态HTML内容到科研之友.
   * 
   * @param psnId
   * @param dynReType
   * @param dynContent
   */
  @SuppressWarnings("rawtypes")
  void saveRecommendDyn(int dynReType, List reDynParamList);

  /**
   * 
   * @param psnId
   * @param dynReType
   * @return
   */
  // String getReDynHtml(Long psnId, int dynReType);

  /**
   * 删除推荐动态HTML内容.
   * 
   * @param psnId
   * @param dynReType
   * @throws ServiceException
   */
  void delDynRecommendHtml(Long psnId, int dynReType) throws ServiceException;

  /**
   * 根据浏览器当前的语言获取相应的推荐动态html内容
   * 
   * @param locale
   * @param psnId
   * @param dynReType
   * @return
   */
  String getReDynHtml(String locale, Long psnId, int dynReType);

  /**
   * 批量删除推荐动态.
   * 
   * @param psnIds
   * @param dynReType
   * @throws ServiceException
   */
  void delDynRecommendBatch(List<Long> psnIds, int dynReType) throws ServiceException;
}
