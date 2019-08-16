package com.smate.core.base.utils.wechat;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.servlet.http.HttpServletRequest;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.smate.core.base.utils.json.JacksonUtils;

/**
 * 消息工具类.
 * 
 * @author xys
 *
 */
public class MessageUtil {
  @SuppressWarnings("unchecked")
  public static Map<String, String> parseXml(HttpServletRequest request) throws Exception {
    // 将解析结果存储在HashMap中
    Map<String, String> map = new HashMap<String, String>();

    // 从request中取得输入流
    InputStream inputStream = request.getInputStream();
    // 读取输入流
    SAXReader reader = new SAXReader();
    Document document = reader.read(inputStream);
    // 得到xml根元素
    Element root = document.getRootElement();
    // 得到根元素的所有子节点
    List<Element> elementList = root.elements();

    // 遍历所有子节点
    for (Element e : elementList)
      map.put(e.getName(), e.getText());

    // 释放资源
    inputStream.close();
    inputStream = null;

    return map;
  }

  /**
   * 获取完整的request url.
   * 
   * @param reqUrl
   * @param appid
   * @param secret
   * @return
   */
  public static String getFullReqUrl(String reqUrl, String appid, String secret) {
    StringBuffer sb = new StringBuffer();
    sb.append(reqUrl);
    sb.append("?grant_type=client_credential");
    sb.append("&appid=");
    sb.append(appid);
    sb.append("&secret=");
    sb.append(secret);
    return sb.toString();
  }

  /**
   * 获取完整的request url.
   * 
   * @param reqUrl
   * @param accessToken
   * @return
   */
  public static String getFullReqUrl(String reqUrl, String accessToken) {
    StringBuffer sb = new StringBuffer();
    sb.append(reqUrl);
    sb.append("?access_token=");
    sb.append(accessToken);
    return sb.toString();
  }

  /**
   * 构建绑定消息内容.
   * 
   * @param bindUrl
   * @param openid
   * @param eventkey
   * @return
   */
  public static String bulidContentForBinding(String bindUrl, String openid, String eventkey) {
    StringBuffer sb = new StringBuffer();
    sb.append("您还没有绑定科研之友账号，轻松绑定之后即可接收最新科研动态；");
    sb.append("\n\n<a href=\"");
    sb.append(bindUrl);
    sb.append("?openid=");
    sb.append(openid);
    sb.append("&eventkey=");
    sb.append(eventkey);
    sb.append("\">点击这里，立即绑定</a>");
    return sb.toString();
  }

  /**
   * 构建绑定成功消息内容.
   * 
   * @return
   */
  public static String buildBindingSuccessContent() {
    StringBuffer sb = new StringBuffer();
    sb.append("恭喜您成功绑定科研之友，\n");
    return sb.toString();
  }

  /**
   * 获取网页授权url.
   * 
   * @param appid
   * @param redirectUri
   * @param scope
   * @param state
   * @return
   */
  public static String getOAuth2Url(String appid, String redirectUri, String scope, String state) {
    StringBuffer sb = new StringBuffer();
    sb.append("https://open.weixin.qq.com/connect/oauth2/authorize");
    sb.append("?appid=");
    sb.append(appid);
    sb.append("&redirect_uri=");
    sb.append(redirectUri);
    sb.append("&response_type=code");
    sb.append("&scope=");
    sb.append(scope == null ? "snsapi_base" : scope);
    sb.append("&state=");
    sb.append(state == null ? "1" : state);
    sb.append("#wechat_redirect");
    return sb.toString();
  }


  /**
   * 向微信端发送请求.
   * 
   * @param requestUrl
   * @param requestMethod
   * @param outputStr
   * @return
   */
  @SuppressWarnings("rawtypes")
  public static Map httpRequest(String requestUrl, String requestMethod, String outputStr) {
    Map map = null;
    StringBuffer buffer = new StringBuffer();
    try {

      URL url = new URL(requestUrl);
      HttpsURLConnection httpUrlConn = (HttpsURLConnection) url.openConnection();

      httpUrlConn.setDoOutput(true);
      httpUrlConn.setDoInput(true);
      httpUrlConn.setUseCaches(false);
      // 设置请求方式（GET/POST）
      httpUrlConn.setRequestMethod(requestMethod);

      if ("GET".equalsIgnoreCase(requestMethod))
        httpUrlConn.connect();

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
      map = JacksonUtils.jsonToMap(buffer.toString());
    } catch (ConnectException ce) {
      ce.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return map;
  }


}
