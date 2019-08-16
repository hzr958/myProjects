package com.smate.web.management.service.patent;

import java.util.Map;

import com.smate.web.management.model.patent.PatentForm;

/**
 * 专利相关服务接口
 * 
 * @author LJ
 *
 */
public interface PatentService {

  /**
   * 获取专利统计对比数据
   * 
   * @param PatentForm form
   * @return
   */
  public String getPatentCompAnalysisdata(PatentForm form) throws Exception;

  /**
   * 获取矩阵数据
   * 
   * @param form
   * @return
   * @throws Exception
   */
  public Map<String, String> getMatrixData(PatentForm form) throws Exception;

}
