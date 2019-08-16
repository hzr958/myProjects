package com.smate.center.task.single.service.pub;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;

/**
 * 研究领域关键词认同服务接口.
 * 
 * @author zyx
 * 
 */
public interface KeywordIdentificationService extends Serializable {
  /**
   * 查询推荐关键词,排除已保存的关键词
   * 
   * @param psnId
   * @param locale 语言
   * @return 不为空的List对象
   * @throws ServiceException
   */
  List<String> recommendKw(Long psnId, Locale locale) throws Exception;

}
