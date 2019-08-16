package com.smate.sie.web.application.service.validate;

import java.util.Map;

import com.smate.core.base.exception.ServiceException;
import com.smate.sie.web.application.model.validate.KpiValidateVipVo;

/**
 * 人员付费功能服务接口
 * 
 * @author wsn
 * @date Feb 26, 2019
 */
public interface KpiValidatePayService {

  /**
   * 购买vip服务成功后保存记录
   * 
   * @param orderNum
   * @throws ServiceException
   */
  void saveOrUpdatePayInfo(String orderNum) throws ServiceException;


  /**
   * 检查支付状态
   * 
   * @param psnId
   * @param orderNum
   * @return
   * @throws ServiceException
   */
  boolean checkPayStatus(String orderNum) throws ServiceException;

  /**
   * 保存预支付订单记录
   * 
   * @param order
   * @param psnId
   * @return
   */
  public String savePrePayLog(KpiValidateVipVo vo);



  /**
   * 校验订单金额
   * 
   * @param orderNum
   * @param returnTotalFee
   * @return
   */
  public boolean checkOrderTotalFee(String orderNum, double returnTotalFee);

  /**
   * 保存发票信息
   * 
   * @param Vo
   */
  public void savePayInvoicesInfo(KpiValidateVipVo Vo);

  /**
   * 更新发票支付状态
   * 
   * @param orderNum
   * @param hasPaied
   */
  public void updatePayInvoicesPaied(String orderNum, Integer hasPaied);

  /**
   * 获取发票信息和订单金额及付费人员信息
   * 
   * @param orderNum
   * @return
   */
  public Map<String, Object> getINvoiceNotificationInfo(String orderNum);

  /**
   * 更新表kpi_pay_validate_invoice（科研验证付费发票信息）中的发票处理状态为，2:已邮件通知财务
   * 
   * @param orderNum
   * @return
   */
  public void updatePayValidateInvoicesStutus(String orderNum, int status);

}
