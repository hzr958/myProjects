package com.smate.web.dyn.service.dynamic;

import com.smate.web.dyn.form.dynamic.DynamicInfluenceForm;

/**
 * 动态影响力服务类接口
 * 
 * @author yhx
 *
 */
public interface DynamicInfluenceService {

  /**
   * 动态认领论文获取
   * 
   * @param form
   * @throws Exception
   */
  public void dynConfirmPaper(DynamicInfluenceForm form) throws Exception;

  /**
   * 动态上传论文获取
   * 
   * @param form
   * @throws Exception
   */
  public void dynUploadPaper(DynamicInfluenceForm form) throws Exception;

  public String dynSharePsnUrl(Long psnId) throws Exception;

  public boolean judgeImpact(Long psnId) throws Exception;

}
