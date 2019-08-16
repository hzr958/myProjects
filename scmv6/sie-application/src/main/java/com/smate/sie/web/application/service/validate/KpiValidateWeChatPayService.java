package com.smate.sie.web.application.service.validate;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.exception.ServiceException;
import com.smate.core.base.pay.model.PayBaseVo;
import com.smate.core.base.pay.service.wechatpay.WeChatPayBaseService;
import com.smate.core.base.utils.common.CommonUtils;
import com.smate.core.base.utils.struts2.Struts2Utils;
import com.smate.sie.core.base.utils.pay.wechat.WXPayConstants;
import com.smate.sie.web.application.model.validate.KpiValidateVipVo;
import com.smate.sie.web.application.model.validate.VipProduct;
import com.smate.sie.web.application.service.email.EmailNotificationService;

/**
 * 科研验证微信支付服务
 * 
 * @author wsn
 * @date Mar 5, 2019
 */
@Service("kpiValidateWeChatPayService")
@Transactional(rollbackFor = Exception.class)
public class KpiValidateWeChatPayService extends WeChatPayBaseService {

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private KpiValidatePayService kpiValidatePayService;

  @Autowired
  private EmailNotificationService emailNotificationService;

  @Override
  public Map<String, String> buildWeChatUnifiedOrderReqData(PayBaseVo Vo) throws ServiceException {
    KpiValidateVipVo kpiValidateVo = (KpiValidateVipVo) Vo;
    VipProduct product = this.buildVipProductInfo(kpiValidateVo);
    Map<String, String> reqData = new HashMap<String, String>();
    kpiValidateVo.setOrderNum(this.createOrderNum());
    // 商品描述
    reqData.put("body", product.getDescription());
    // 交易类型
    reqData.put("trade_type", WXPayConstants.TRADE_TYPE_NATIVE);
    // 商户订单号
    reqData.put("out_trade_no", kpiValidateVo.getOrderNum());
    // 支付金额(单位：分)
    reqData.put("total_fee", Objects.toString((int) (product.getPrice() * 100), ""));
    // 用户ip地址
    reqData.put("spbill_create_ip", Struts2Utils.getRemoteAddr());
    // 设置商品id
    reqData.put("product_id", Objects.toString(product.getId(), ""));
    // 接收支付结果的地址
    reqData.put("notify_url", kpiValidateVo.getNotifyUrl());
    return reqData;
  }

  @Override
  public void dealWithUnifiedOrderSuccessBusiness(PayBaseVo vo, Map<String, String> unifiedOrderReturn)
      throws ServiceException {
    KpiValidateVipVo kpiValidateVo = (KpiValidateVipVo) vo;
    kpiValidateVo.setTradeType(WXPayConstants.TRADE_TYPE_NATIVE);
    kpiValidatePayService.savePrePayLog(kpiValidateVo);
    if (CommonUtils.compareIntegerValue(kpiValidateVo.getNeedInvoices(), 1)) {
      kpiValidatePayService.savePayInvoicesInfo(kpiValidateVo);
    }
  }

  @Override
  public void dealWithUnifiedOrderFailed(PayBaseVo vo) {
    // TODO Auto-generated method stub

  }

  @Override
  public boolean checkLocaleOrderHasPaied(String orderNum) {
    return kpiValidatePayService.checkPayStatus(orderNum);
  }

  @Override
  public boolean checkOrderTotalAmount(String orderNum, String totalAmount) {
    DecimalFormat df = new DecimalFormat("0.00");// 设置保留位数
    String returnTotalFee = df.format(NumberUtils.toFloat(totalAmount, 0) / 100);
    return kpiValidatePayService.checkOrderTotalFee(orderNum, NumberUtils.toDouble(returnTotalFee, 0));
  }

  @Override
  public void dealWithPaySuccessBusiness(Map<String, String> returnParams) {
    String orderNum = returnParams.get("out_trade_no");
    kpiValidatePayService.saveOrUpdatePayInfo(orderNum);
    kpiValidatePayService.updatePayInvoicesPaied(orderNum, 1);
    // 微信支付成功方法，收集邮件必要信息发送邮件给财务
    try {
      this.sendEmailTonotificationInvice(orderNum);
    } catch (Exception e) {
      logger.error("开发票通知财务邮件发送失败", e.getMessage());
      e.printStackTrace();
    }
    // 邮件发送成功则更新表kpi_pay_validate_invoice（科研验证付费发票信息）中的发票处理状态为，2:已邮件通知财务
    kpiValidatePayService.updatePayValidateInvoicesStutus(orderNum, 2);
  }

  private void sendEmailTonotificationInvice(String orderNum) throws Exception {
    Map<String, Object> hashInput = kpiValidatePayService.getINvoiceNotificationInfo(orderNum);
    if (hashInput != null) {
      emailNotificationService.sendEmailInvoiceInformFinance(hashInput);
    }
  }

  @Override
  public void dealWithPayFailedBusiness(Map<String, String> returnParams) {
    // TODO Auto-generated method stub

  }

  @Override
  public void dealWithCallbackException(HttpServletRequest request) {
    // TODO Auto-generated method stub

  }



  protected VipProduct buildVipProductInfo(KpiValidateVipVo Vo) {
    VipProduct pro = new VipProduct();
    String grade = Vo.getGrade();
    boolean isRun = "run".equalsIgnoreCase(System.getenv("RUN_ENV"));
    switch (grade) {
      case "A":
        pro.setPrice(isRun ? 998 : 0.02);
        Vo.setPrice(isRun ? 998 : 0.02);
        pro.setProductName("科研之友-升级VIP账号12个月");
        pro.setDescription("科研之友-升级VIP账号12个月");
        pro.setId(1L);
        break;
      case "B":
        pro.setPrice(isRun ? 1800 : 0.03);
        Vo.setPrice(isRun ? 1800 : 0.03);
        pro.setProductName("科研之友-升级VIP账号24个月");
        pro.setDescription("科研之友-升级VIP账号24个月");
        pro.setId(2L);
        break;
      case "C":
        pro.setPrice(isRun ? 98 : 0.01);
        Vo.setPrice(isRun ? 98 : 0.01);
        pro.setProductName("科研之友-升级VIP账号1个月");
        pro.setDescription("科研之友-升级VIP账号1个月");
        pro.setId(3L);
        break;
      default:
        break;
    }
    return pro;
  }

  /**
   * 构建订单号
   * 
   * @return
   */
  protected String createOrderNum() {
    int number = (int) ((Math.random() * 9) * 1000);// 随机数
    DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");// 时间
    return dateFormat.format(new Date()) + number;
  }

}
