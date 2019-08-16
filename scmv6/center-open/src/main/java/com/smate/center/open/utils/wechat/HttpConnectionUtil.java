package com.smate.center.open.utils.wechat;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;

import com.smate.core.base.utils.constant.wechat.WeChatConstant;


/**
 * http请求工具类
 * 
 * @author zk
 * @since 6.0.1
 */
public class HttpConnectionUtil {

  public static String httpConnection(String requestUrl, String requestMethod, String outputStr) {
    StringBuffer buffer = new StringBuffer();
    try {
      URL url = new URL(requestUrl);
      HttpURLConnection httpUrlConn = (HttpURLConnection) url.openConnection();
      httpUrlConn.setDoOutput(true);
      httpUrlConn.setDoInput(true);
      httpUrlConn.setUseCaches(false);
      // 设置请求方式（GET/POST）
      httpUrlConn.setRequestMethod(requestMethod);
      if (WeChatConstant.REQ_METHOD_GET.equalsIgnoreCase(requestMethod)) {
        httpUrlConn.connect();
      }
      // 当有数据需要提交时
      if (null != outputStr) {
        OutputStream outputStream = httpUrlConn.getOutputStream();
        // 注意编码格式，防止中文乱码
        outputStream.write(outputStr.getBytes("UTF-8"));
        outputStream.close();
      }
      // 将返回的输入流转换成字符串
      InputStream inputStream = httpUrlConn.getInputStream();
      InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
      BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

      String str = null;
      while ((str = bufferedReader.readLine()) != null) {
        buffer.append(str);
      }
      bufferedReader.close();
      inputStreamReader.close();
      // 释放资源
      inputStream.close();
      inputStream = null;
      httpUrlConn.disconnect();

    } catch (ConnectException ce) {
      ce.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return buffer.toString();

  }
}
