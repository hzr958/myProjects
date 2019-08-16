package com.smate.sie.core.base.utils.pay.wechat;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

/**
 * 微信支付工具类
 * 
 * @author wsn
 * @date Feb 26, 2019
 */
public class WeChatPayUtils {

  /**
   * 获取微信支付通知结果字符串
   * 
   * @param request
   * @return
   * @throws Exception
   */
  public static String getWeChatPayReturnStr(HttpServletRequest request) throws Exception {
    String wechatReturnStr = "";
    try {
      InputStream inStream = request.getInputStream();
      int _buffer_size = 1024;
      if (inStream != null) {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] tempBytes = new byte[_buffer_size];
        int count = -1;
        while ((count = inStream.read(tempBytes, 0, _buffer_size)) != -1) {
          outStream.write(tempBytes, 0, count);
        }
        tempBytes = null;
        outStream.flush();
        // 将流转换成字符串
        wechatReturnStr = new String(outStream.toByteArray(), "UTF-8");
      }
    } catch (Exception e) {
      throw new Exception(e);
    }
    return wechatReturnStr;
  }


  /**
   * 调用微信订单查询接口检查订单是否已支付
   * 
   * @param wxPay
   * @param returnParams
   * @return
   * @throws Exception
   */
  public static boolean checkWxOrderHasPaied(WXPay wxPay, Map<String, String> returnParams) throws Exception {
    Map<String, String> orderStatusReturn = wxPay.orderQuery(returnParams);
    return WXPayConstants.SUCCESS.equalsIgnoreCase(orderStatusReturn.get("return_code"))
        && WXPayConstants.SUCCESS.equalsIgnoreCase(orderStatusReturn.get("result_code"))
        && WXPayConstants.SUCCESS.equalsIgnoreCase(orderStatusReturn.get("trade_state"));
  }

}
