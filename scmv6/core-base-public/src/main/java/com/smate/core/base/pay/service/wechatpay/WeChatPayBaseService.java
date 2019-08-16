package com.smate.core.base.pay.service.wechatpay;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

import com.smate.core.base.exception.ServiceException;
import com.smate.core.base.pay.model.PayBaseVo;
import com.smate.sie.core.base.utils.pay.wechat.WXPay;
import com.smate.sie.core.base.utils.pay.wechat.WXPayConfigImpl;
import com.smate.sie.core.base.utils.pay.wechat.WXPayConstants;
import com.smate.sie.core.base.utils.pay.wechat.WXPayUtil;

/**
 * 微信支付基础服务
 * 
 * @author wsn
 * @date Mar 5, 2019
 */
public abstract class WeChatPayBaseService {


  /**
   * 调用微信统一下单接口，获取微信支付二维码，并进行一些具体业务处理
   * 
   * @param config
   * @param vo
   * @return
   * @throws ServiceException
   */
  public String weChatPayUnifiedOrder(PayBaseVo vo) throws ServiceException {
    String codeUrl = "";
    try {
      // 1. 用config初始化一个WXPay对象
      WXPay wxPay = new WXPay(new WXPayConfigImpl());
      // 2. 调用buildWeChatUnifiedOrderReqData(PayBaseVo vo)，构建付费产品相关的一些参数，返回reqData（需要具体的业务逻辑Service去具体实现）
      Map<String, String> reqData = this.buildWeChatUnifiedOrderReqData(vo);
      // 3. 调用WXPay.unifiedOrder(reqData)调用统一下单接口
      Map<String, String> unifiedOrderReturn = wxPay.unifiedOrder(reqData);
      // 4. 获取微信支付链接code_url
      if (unifiedOrderReturn.containsKey("code_url")) {
        codeUrl = unifiedOrderReturn.get("code_url");
      }
      // 5. 调用dealWithUnifiedOrderSuccessBusiness(PayBaseVo vo, Map<String, String>
      // unifiedOrderReturn)，处理具体的业务逻辑（需要具体的业务逻辑Service去具体实现）
      if (StringUtils.isNotBlank(codeUrl)) {
        this.dealWithUnifiedOrderSuccessBusiness(vo, unifiedOrderReturn);
      }
    } catch (Exception e) {
      // 6. 若是产生异常，调用dealWithUnifiedOrderFailed(PayBaseVo vo)处理（需要具体的业务逻辑Service去具体实现）
      this.dealWithUnifiedOrderFailed(vo);
      throw new ServiceException(e);
    }
    return codeUrl;
  }



  /**
   * 微信支付通知回调处理
   * 
   * @param request
   * @throws ServiceException
   */
  public void dealWithWeChatPayCallBack(HttpServletRequest request) throws ServiceException {
    try {
      // 1.解析request参数，获取微信端返回的xml数据，可参考getWeChatPayReturn(HttpServletRequest request)
      String returnStr = WXPayUtil.getWeChatPayReturnStr(request);
      // 2.用config初始化一个WXPay对象
      WXPay wxPay = new WXPay(new WXPayConfigImpl());
      // 3.调用WXPay.processResponseXml(String xmlStr)将微信返回数据转化为Map<String, String>
      Map<String, String> returnParams = wxPay.processResponseXml(returnStr);
      // 4.判断return_code和result_code都是success,判断是否支付成功
      if (WXPayConstants.SUCCESS.equals(returnParams.get("return_code"))
          && WXPayConstants.SUCCESS.equals(returnParams.get("result_code"))) {
        // 4.1 避免结果出现差异，安全起见,调用微信订单查询接口，查询订单状态是否是已支付
        Map<String, String> orderQueryReqData = new HashMap<String, String>();
        orderQueryReqData.put("out_trade_no", returnParams.get("out_trade_no"));
        boolean wxOrderHasPaied = WXPayUtil.checkWxOrderHasPaied(wxPay, orderQueryReqData);
        // 4.2 调用checkLocalePayStatus(String orderNum)，检查本地记录的订单状态是否还未支付 (需要具体的业务逻辑Service去具体实现)
        boolean localeOrderHasPaied = this.checkLocaleOrderHasPaied(returnParams.get("out_trade_no"));
        // 4.3 调用checkOrderTotalAmount(String orderNum, String totalAmount)，检查订单金额是否正确
        boolean checkOrderTotalFee =
            this.checkOrderTotalAmount(returnParams.get("out_trade_no"), returnParams.get("total_fee"));
        // 4.4 如果上面检查都通过，调用dealWithPaySuccessBusiness(Map<String, String> returnParams)，处理支付成功的具体的业务逻辑
        if (wxOrderHasPaied && checkOrderTotalFee && !localeOrderHasPaied) {
          this.dealWithPaySuccessBusiness(returnParams);
        }
      } else {
        // 5. 调用dealWithPayFailedBusiness(Map<String, String> returnParams)，处理支付失败逻辑
        this.dealWithPayFailedBusiness(returnParams);
      }
    } catch (Exception e) {
      // 6.如果出现异常情况，调用dealWithCallbackException(Map<String, String> returnParams)处理
      this.dealWithCallbackException(request);
      throw new ServiceException(e);
    }
  }


  /**
   * 抽象接口，构建付费产品相关的一些参数，用来调用微信统一下单接口用
   * 
   * @param vo
   * @return
   * @throws ServiceException
   */
  public abstract Map<String, String> buildWeChatUnifiedOrderReqData(PayBaseVo vo) throws ServiceException;

  /**
   * 抽象接口，调用微信统一下单接口后，获取到微信支付链接，进行本地业务处理
   * 
   * @param vo
   * @param unifiedOrderReturn
   * @return
   * @throws ServiceException
   */
  public abstract void dealWithUnifiedOrderSuccessBusiness(PayBaseVo vo, Map<String, String> unifiedOrderReturn)
      throws ServiceException;

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
