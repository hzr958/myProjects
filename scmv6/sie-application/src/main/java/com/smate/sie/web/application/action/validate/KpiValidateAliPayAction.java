package com.smate.sie.web.application.action.validate;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.convention.annotation.Action;
import org.codehaus.plexus.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.struts2.Struts2Utils;
import com.smate.sie.web.application.model.validate.KpiValidateVipVo;
import com.smate.sie.web.application.service.validate.KpiValidateAliPayService;

/**
 * 阿里支付宝支付Action
 * 
 * @author wsn
 * @date Feb 28, 2019
 */
public class KpiValidateAliPayAction extends ActionSupport implements ModelDriven<KpiValidateVipVo>, Preparable {
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  private KpiValidateVipVo Vo;
  @Autowired
  private KpiValidateAliPayService kpiValidateAliPayService;
  @Value("${aliPayReturnUrl}")
  private String aliPayReturnUrl;
  @Value("${aliPayNotifyUrl}")
  private String aliPayNotifyUrl;
  @Value("${domainscm}")
  private String domainScm;



  @Override
  public void prepare() throws Exception {
    if (Vo == null) {
      Vo = new KpiValidateVipVo();
    }
    Vo.setNotifyUrl(aliPayNotifyUrl);
    Vo.setReturnUrl(aliPayReturnUrl);
  }

  @Override
  public KpiValidateVipVo getModel() {
    return Vo;
  }


  /**
   * 科研验证付费预处理 。若想给BizContent增加其他可选请求参数，以增加自定义超时时间参数timeout_express来举例说明
   * alipayRequest.setBizContent("{\"out_trade_no\":\""+ out_trade_no +"\"," + "\"total_amount\":\""+
   * total_amount +"\"," + "\"subject\":\""+ subject +"\"," + "\"body\":\""+ body +"\"," +
   * "\"timeout_express\":\"10m\"," + "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");
   * 请求参数可查阅【电脑网站支付的API文档-alipay.trade.page.pay-请求参数】章节
   */
  @Action("/application/alipay/pre")
  public void KpiValidatePayPrepare() {
    Long psnId = SecurityUtils.getCurrentUserId();
    HttpServletResponse response = Struts2Utils.getResponse();
    try {
      if (StringUtils.isNotBlank(Vo.getGrade())) {
        Vo.setPsnId(psnId);
        // 请求
        String result = kpiValidateAliPayService.alipayPostFormStr(Vo);
        // 输出
        response.setContentType("text/html;charset=utf-8");
        response.getWriter().write(result);// 直接将完整的表单html输出到页面
        response.getWriter().flush();
        response.getWriter().close();
      }
    } catch (Exception e) {
      logger.error("科研验证支付宝付费预处理异常， psnId={}", psnId, e);
    }
  }

  /**
   * 支付结果同步通知
   * 
   * @return
   * @throws IOException
   */
  @Action("/application/outside/alipay/return")
  public String aliPayReturn() throws IOException {
    logger.info("-------------------------------------支付宝支付同步回调了-----------------------------");
    try {
      kpiValidateAliPayService.dealWithAlipayCallBack(Struts2Utils.getRequest(), "return");
    } catch (Exception e) {
      logger.error("处理支付宝支付通知异常", e);
    }
    Struts2Utils.getResponse().sendRedirect(domainScm + "/application/validate/toadd");
    return null;
  }


  /**
   * 支付结果异步通知
   * 
   * @return
   * @throws IOException
   */
  @Action("/application/outside/alipay/notify")
  public String aliPayNotify() throws IOException {
    HttpServletRequest request = Struts2Utils.getRequest();
    HttpServletResponse response = Struts2Utils.getResponse();
    String responseText = "success";
    logger.info("-------------------------------------支付宝支付异步回调了-----------------------------");
    try {
      responseText = kpiValidateAliPayService.dealWithAlipayCallBack(Struts2Utils.getRequest(), "syncCallback");
    } catch (Exception e) {
      logger.error("处理支付宝支付通知异常", e);
      responseText = "failure";
    }
    response.getWriter().write(responseText);
    return null;
  }

}
