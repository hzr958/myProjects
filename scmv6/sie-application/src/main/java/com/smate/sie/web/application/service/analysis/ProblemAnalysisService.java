package com.smate.sie.web.application.service.analysis;

import java.util.List;
import java.util.Map;

import com.smate.core.base.utils.exception.SysServiceException;

public interface ProblemAnalysisService {

  /**
   * 从页面的标题和摘要抽取5个关键词
   * 
   * @param title
   * @param summary
   * @return
   * @throws SysServiceException
   */
  public List<Map<String, Object>> extractKeyWordsFormInfo(String title, String summary) throws SysServiceException;

  /**
   * 科研趋势页签
   * 
   * @param selectKeyword
   * @return
   * @throws SysServiceException
   */
  public Map<String, Object> researchTrendByKeyWords(String selectKeyword) throws SysServiceException;

  /**
   * 相关学科页签（取前5）
   * 
   * @param selectKeyword
   * @return
   * @throws SysServiceException
   */
  public Map<String, Object> relatedDisByKeyWords(String selectKeyword) throws SysServiceException;

  /**
   * 相关学者页签（取前20）
   * 
   * @param selectKeyword
   * @return
   * @throws SysServiceException
   */
  public List<Map<String, Object>> relatedResearchers(String selectKeyword) throws SysServiceException;

  /**
   * 相关单位页签（取前10）
   * 
   * @param selectKeyword
   * @return
   * @throws SysServiceException
   */
  public Map<String, Object> relatedIntitutions(String selectKeyword) throws SysServiceException;

}
