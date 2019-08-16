package com.smate.core.base.pay.service.wechatpay;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.smate.core.base.exception.ServiceException;
import com.smate.core.base.pay.model.PayBaseVo;
import com.smate.sie.core.base.utils.pay.ali.AlipayConfig;

/**
 * 支付宝支付基础服务
 * 
 * @author wsn
 * @date Mar 5, 2019
 */
public abstract class AlipayPayBaseService {


  /**
   * 构建支付宝支付所要提交的表单字符串，并进行一些具体业务处理，用PrintWriter输出后自动提交跳转支付宝支付页面
   * 
   * @param vo
   * @return
   * @throws ServiceException
   */
  public String alipayPostFormStr(PayBaseVo vo) throws ServiceException {
    String formStr = "";
    try {
      // 1. 用AlipayConfig初始化一个AlipayClient对象
      // 获得初始化的AlipayClient
      AlipayClient alipayClient =
          new DefaultAlipayClient(AlipayConfig.gatewayUrl, AlipayConfig.app_id, AlipayConfig.merchant_private_key,
              "json", AlipayConfig.charset, AlipayConfig.alipay_public_key, AlipayConfig.sign_type);
      // 2. 调用buildAlipayUnifiedOrderReqData(BusinessVO vo)，构建付费产品相关的一些参数，返回reqData（需要具体的业务逻辑Service去具体实现）
      Map<String, String> reqData = this.buildAlipayUnifiedOrderReqData(vo);
      // 3. new一个AlipayTradePagePayRequest对象alipayRequest，设置请求参数
      AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
      alipayRequest.setReturnUrl(reqData.get("return_url"));
      alipayRequest.setNotifyUrl(reqData.get("notify_url"));
      alipayRequest.setBizContent("{\"out_trade_no\":\"" + reqData.get("out_trade_no") + "\"," + "\"total_amount\":\""
          + reqData.get("total_amount") + "\"," + "\"subject\":\"" + reqData.get("subject") + "\"," + "\"body\":\""
          + reqData.get("body") + "\"," + "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");
      // 4. 调用alipayClient.pageExecute(alipayRequest).getBody()，获取拼接成的form单html数据
      formStr = alipayClient.pageExecute(alipayRequest).getBody();
      // 5. 调用dealWithUnifiedOrderSuccessBusiness(BusinessVO vo)，处理具体的业务逻辑（需要具体的业务逻辑Service去具体实现）
      this.dealWithUnifiedOrderSuccessBusiness(vo);
    } catch (Exception e) {
      // 6. 若是产生异常，调用dealWithUnifiedOrderFailed(PayBaseVo vo)处理（需要具体的业务逻辑Service去具体实现）
      this.dealWithUnifiedOrderFailed(vo);
      throw new ServiceException(e);
    }
    return formStr;
  }



  /**
   * 支付宝支付通知回调处理
   * 
   * @param request
   * @throws ServiceException
   */
  public String dealWithAlipayCallBack(HttpServletRequest request, String callBackType) throws ServiceException {
    String dealStatus = "success";
    try {
      // 1.解析request参数，调用AlipaySignature.rsaCheckV1验证签名，可参考aliPayReturnSignVerified
      Map<String, String> returnParams = this.aliPayReturnMap(request);
      boolean signVerified = AlipaySignature.rsaCheckV1(returnParams, AlipayConfig.alipay_public_key,
          AlipayConfig.charset, AlipayConfig.sign_type);
      boolean syncCallbackTradeSuccess = true;
      // 2.签名验证成功时
      if (signVerified) {
        // 2.1 异步通知，检查交易状态
        if ("syncCallback".equals(callBackType)) {
          String trade_status = returnParams.get("trade_status");
          syncCallbackTradeSuccess = "TRADE_FINISHED".equals(trade_status) || "TRADE_SUCCESS".equals(trade_status);
        }
        // 2.2 调用Alipay接口，查询订单状态
        boolean alipayOrderHasPaied =
            this.queryAlipayOrderStatus(returnParams.get("out_trade_no"), returnParams.get("trade_no"));
        // 2.3 调用checkLocalePayStatus(String orderNum)，检查本地记录的订单状态是否还未支付 (需要具体的业务逻辑Service去具体实现)
        boolean localeOrderHasPaied = this.checkLocaleOrderHasPaied(returnParams.get("out_trade_no"));
        // 2.4 调用checkOrderTotalAmount(String orderNum, String totalAmount)，检查订单金额是否正确
        boolean checkOrderTotalFee =
            this.checkOrderTotalAmount(returnParams.get("out_trade_no"), returnParams.get("total_amount"));
        // 2.5 如果上面检查都通过，调用dealWithPaySuccessBusiness(Map<String, String> returnParams)，处理支付成功的具体的业务逻辑
        if (syncCallbackTradeSuccess && alipayOrderHasPaied && checkOrderTotalFee && !localeOrderHasPaied) {
          this.dealWithPaySuccessBusiness(returnParams);
        }
      }
      // 3.如果签名验证失败或交易失败，调用dealWithPayFailedBusiness(returnParams)，处理支付失败逻辑
      if (!(signVerified && syncCallbackTradeSuccess)) {
        this.dealWithPayFailedBusiness(returnParams);
        dealStatus = "failure";
      }
    } catch (Exception e) {
      // 4.如果出现异常情况，调用dealWithCallbackException(Map<String, String> returnParams)处理
      this.dealWithCallbackException(request);
      dealStatus = "failure";
      throw new ServiceException(e);
    }
    return dealStatus;
  }


  /**
   * 解析支付宝支付返回的参数成Map
   * 
   * @param request
   * @return
   * @throws ServiceException
   */
  protected Map<String, String> aliPayReturnMap(HttpServletRequest request) throws ServiceException {
    Map<String, String> params = new HashMap<String, String>();
    try {
      Map<String, String[]> requestParams = request.getParameterMap();
      for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext();) {
        String name = (String) iter.next();
        String[] values = (String[]) requestParams.get(name);
        String valueStr = "";
        for (int i = 0; i < values.length; i++) {
          valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
        }
        // 乱码解决，这段代码在出现乱码时使用
        valueStr = new String(valueStr);
        params.put(name, valueStr);
      }
    } catch (Exception e) {
      throw new ServiceException(e);
    }
    return params;
  }


  /**
   * 调用支付宝订单查询接口查询订单是否已支付
   * 
   * @param orderNum
   * @param alipayTradeNO
   * @return
   * @throws AlipayApiException
   */
  protected boolean queryAlipayOrderStatus(String orderNum, String alipayTradeNO) throws AlipayApiException {
    AlipayClient alipayClient =
        new DefaultAlipayClient(AlipayConfig.gatewayUrl, AlipayConfig.app_id, AlipayConfig.merchant_private_key, "json",
            AlipayConfig.charset, AlipayConfig.alipay_public_key, AlipayConfig.sign_type);
    AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
    request.setBizContent("{\"out_trade_no\":\"" + orderNum + "\",\"trade_no\":\"" + alipayTradeNO + "\"}");
    AlipayTradeQueryResponse response = alipayClient.execute(request);
    if (response.isSuccess() && ("TRADE_FINISHED".equalsIgnoreCase(response.getTradeStatus())
        || "TRADE_SUCCESS".equals(response.getTradeStatus()))) {
      return true;
    }
    return false;
  }


  /**
   * 抽象接口，构建付费产品相关的一些参数，用来构建支付宝统一下单接口form用
   * 
   * @param vo
   * @return
   * @throws ServiceException
   */
  public abstract Map<String, String> buildAlipayUnifiedOrderReqData(PayBaseVo vo) throws ServiceException;

  /**
   * 抽象接口，调用微信统一下单接口后，获取到微信支付链接，进行本地业务处理
   * 
   * @param vo
   * @return
   * @throws ServiceException
   */
  public abstract void dealWithUnifiedOrderSuccessBusiness(PayBaseVo vo) throws ServiceException;

  /**
   * 抽象接口，支付前的准备处理中出现异常的情况
   * 
   * @param vo
   */
  public abstract void dealWithUnifiedOrderFailed(PayBaseVo vo);

  /**
   * 抽象接口，检查对应的订单在本地数据库中是否已支付
   * 
   * @param orderNum
   * @return
   */
  public abstract boolean checkLocaleOrderHasPaied(String orderNum);

  /**
   * 抽象接口，检查对应的订单的金额是否正确
   * 
   * @param orderNum
   * @param totalAmount
   * @return
   */
  public abstract boolean checkOrderTotalAmount(String orderNum, String totalAmount);

  /**
   * 抽象接口，处理支付成功的具体的业务逻辑
   * 
   * @param returnParams
   */
  public abstract void dealWithPaySuccessBusiness(Map<String, String> returnParams);

  /**
   * 抽象接口，处理支付失败的具体业务逻辑
   * 
   * @param returnParams
   */
  public abstract void dealWithPayFailedBusiness(Map<String, String> returnParams);

  /**
   * 抽象接口，处理回调中的异常情况
   * 
   * @param returnParams
   */
  public abstract void dealWithCallbackException(HttpServletRequest request);
}
