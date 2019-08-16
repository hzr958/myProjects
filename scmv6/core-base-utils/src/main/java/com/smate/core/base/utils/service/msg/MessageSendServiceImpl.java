package com.smate.core.base.utils.service.msg;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.utils.dao.msg.MessageLogDao;
import com.smate.core.base.utils.model.msg.MessageLog;

@Service("messageSendService")
@Transactional(rollbackFor = Exception.class)
public class MessageSendServiceImpl implements MessageSendService {
  private final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  MessageLogDao messageLogDao;
  private String serviceURL = "http://sms.shwsms.com/httpInterfaceSubmitAction.do";

  private String username = "cui";

  private String password = "cui";

  private String pwd = "";// 密码

  public MessageSendServiceImpl() throws UnsupportedEncodingException {
    this.pwd = getMD5(this.password);
  }

  /*
   * 构造函数
   */
  @SuppressWarnings("static-access")
  public MessageSendServiceImpl(String sn, String password) throws UnsupportedEncodingException {
    this.password = password;
    this.pwd = this.getMD5(sn + password);
  }

  /*
   * 方法名称：getMD5 功 能：字符串MD5加密 参 数：待转换字符串 返 回 值：加密之后字符串
   */
  public static String getMD5(String sourceStr) throws UnsupportedEncodingException {
    String resultStr = "";
    try {
      byte[] temp = sourceStr.getBytes();
      MessageDigest md5 = MessageDigest.getInstance("MD5");
      md5.update(temp);
      byte[] b = md5.digest();
      for (int i = 0; i < b.length; i++) {
        char[] digit = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        char[] ob = new char[2];
        ob[0] = digit[(b[i] >>> 4) & 0X0F];
        ob[1] = digit[b[i] & 0X0F];
        resultStr += new String(ob);
      }
      return resultStr;
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
      return null;
    }
  }

  /*
   * 方法名称：mt 功 能：发送短信 参 数：mobile,content,ext,stime,rrid(手机号，内容，扩展码，定时时间，唯一标识) 返 回
   * 值：唯一标识，如果不填写rrid将返回系统生成的
   */
  public String mt(String mobile, String content, String ext, String stime, String rrid) {

    if (StringUtils.isBlank(mobile) || StringUtils.isBlank(content)) {
      return "999";// 手机或短信为空将不能发送短信通知
    }

    URL url;
    InputStreamReader isr = null;
    try {

      String ctt = URLEncoder.encode(content, "UTF-8");
      StringBuffer urlBuf = new StringBuffer();

      urlBuf.append(this.serviceURL).append("?account=").append(this.username).append("&password=").append(this.pwd)
          .append("&mobile=").append(mobile).append("&content=").append(ctt);

      url = new URL(urlBuf.toString());

      URLConnection connection = url.openConnection();
      HttpURLConnection httpconn = (HttpURLConnection) connection;
      httpconn.setDoInput(true);
      httpconn.setDoOutput(true);
      isr = new InputStreamReader(httpconn.getInputStream());
      BufferedReader in = new BufferedReader(isr);
      String inputLine;
      if (in.ready()) {
        inputLine = in.readLine();
        if (inputLine.equals("111")) {
          if (in.ready()) {
            return in.readLine(); // 发送成功，获得下行标识码
          } else {
            throw new Exception("获取下行标识失败！！！！！");
          }
        } else {
          return inputLine; // 发送失败
        }
      } else {
        throw new Exception("SMS SERVER RESPONSE Fail!!!!");
      }

    } catch (Exception e) {
      e.printStackTrace();
      return "998";
    } finally {
      try {
        if (isr != null)
          isr.close();

      } catch (IOException e) {
        e.printStackTrace();
      }

    }

  }

  /**
   * 调用发送信息(HTTP接口).
   * 
   * @param destId 目的号码
   * @param content 短息内容
   * @return 返回发送结果码
   * @throws Exception
   */
  @Override
  public String sendMessage(String destId, String content) throws Exception {
    MessageLog msg = new MessageLog();
    Long retVal = new Long(mt(destId, content, "", "", "9999"));
    msg.setSmsType(1000l);// 注册短信
    msg.setContent(content);
    msg.setSmsTo(destId);
    msg.setSendTime(new Date());
    try {
      if (retVal > 999) {
        logger.info("短信下行消息标识 [" + retVal + "]，" + "目的号码 [" + destId + "]，" + "短信内容 [" + content + "]");
      } else {
        String key = "error_" + retVal;
        msg.setStatus(2);
        msg.setErrormsg(key);
        throw new Exception("短信发送异常");
      }

    } catch (Exception e) {
      this.logger.error("短信发送异常", e);
      String errorMsg = e.getMessage();
      if (errorMsg != null && errorMsg.length() > 2000) {
        errorMsg = errorMsg.substring(0, 1500);
      }
      msg.setStatus(2);
      msg.setErrormsg(errorMsg);
    }
    msg.setStatus(1);
    messageLogDao.save(msg);
    return retVal.toString();
  }

  /**
   * test
   * 
   * @param args
   * @throws Exception
   */
  public static void main(String[] args) throws Exception {
    MessageSendServiceImpl m = new MessageSendServiceImpl();
    m.sendMessage("17620351122", "【科研之友】测试短信666666");
  }

}
