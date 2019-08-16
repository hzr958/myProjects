package com.smate.sie.center.open.service.valisent;

import java.util.Map;

/**
 * 付费验证服务
 * 
 * @author xr
 * @date 2019年8月1日
 */
public interface KpiPaymentValidateService {

  /**
   * 开题分析付费验证
   * 
   * @param psnId
   * @param ip
   * @return
   */
  Map<String, Object> paymentAnalysisVarification(Long psnId, String ip);

  /**
   * 科研验证付费验证
   * 
   * @param psnId
   * @param ip
   * @return
   */
  Map<String, Object> paymentValidateVarification(Long psnId, String ip);

  /**
   * 项目助理付费验证
   * 
   * @param psnId
   * @param ip
   * @return
   */
  Map<String, Object> paymentProjectVarification(Long psnId, String ip);

}
