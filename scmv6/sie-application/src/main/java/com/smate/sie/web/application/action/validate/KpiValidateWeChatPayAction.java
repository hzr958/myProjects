package com.smate.sie.web.application.action.validate;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.convention.annotation.Action;
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
import com.smate.sie.web.application.service.validate.KpiValidatePayService;
import com.smate.sie.web.application.service.validate.KpiValidateWeChatPayService;


/**
 * 微信支付控制器
 * 
 * @author wsn
 * @date Feb 26, 2019
 */
public class KpiValidateWeChatPayAction extends ActionSupport implements ModelDriven<KpiValidateVipVo>, Preparable {

  protected final Logger logger = LoggerFactory.getLogger(getClass());
  private KpiValidateVipVo Vo;
  @Autowired
  private KpiValidatePayService kpiValidatePayService;
  @Value("${wechat_pay_notifyurl}")
  private String wechatPayNotifyUrl;
  @Autowired
  private KpiValidateWeChatPayService kpiValidateWeChatPayService;

  /**
   * 微信支付结果回调处理
   * 
   * @param result
   * @return
   * @throws IOException
   */
  @Action("/application/outside/pay/callback")
  public void receive() {
    try {
      kpiValidateWeChatPayService.dealWithWeChatPayCallBack(Struts2Utils.getRequest());
      // 通知微信我们收到了。如果微信没有收到回复，会间隔一段时间又通知一遍，这样的话容易出现业务重复处理操作
      logger.info("-------------------------------------微信支付回调了-----------------------------");
      HttpServletResponse response = Struts2Utils.getResponse();
      PrintWriter out = response.getWriter();
      out.println("<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml>");
      out.flush();
      out.close();
    } catch (Exception e) {
      logger.error("处理微信支付结果异常", e);
    }
  }


  /**
   * 校验支付状态
   * 
   * @param orderNumber
   * @return
   * @throws IOException
   */
  @Action("/application/pay/ajaxcheck")
  public Object checkOrder() throws IOException {
    Map<String, Object> result = new HashMap<String, Object>();
    // TODO 检查订单状态，确认已支付，返回success:true
    boolean status = kpiValidatePayService.checkPayStatus(Vo.getOrderNum());
    result.put("result", status ? "paied" : "not_paied");
    Struts2Utils.renderJson(result, "encoding:utf-8");
    return null;
  }


  /**
   * 准备支付，显示支付二维码
   * 
   * @return
   */
  @Action("/application/pay/ajaxpre")
  public String preparePay() {
    Map<String, String> result = new HashMap<String, String>();
    String codeUrl = "";
    String dealResult = "success";
    try {
      Vo.setNotifyUrl(wechatPayNotifyUrl);
      Vo.setPsnId(SecurityUtils.getCurrentUserId());
      codeUrl = kpiValidateWeChatPayService.weChatPayUnifiedOrder(Vo);
    } catch (Exception e) {
      logger.error("支付准备，调用微信统一下单接口和本地下单处理异常", e);
      dealResult = "error";
    }
    result.put("codeUrl", codeUrl);
    result.put("result", dealResult);
    result.put("orderNum", Vo.getOrderNum());
    result.put("curDate", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
    Struts2Utils.renderJson(result, "encoding:utf-8");
    return null;
  }


  @Override
  public void prepare() throws Exception {
    if (Vo == null) {
      Vo = new KpiValidateVipVo();
    }
  }


  @Override
  public KpiValidateVipVo getModel() {
    return Vo;
  }

}
