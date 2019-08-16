package com.smate.core.base.utils.service.msg;

import com.smate.core.base.utils.dao.msg.MessageLogDao;
import com.smate.core.base.utils.dao.msg.MobileWhitelistDao;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.model.msg.MessageLog;
import com.smate.core.base.utils.model.msg.MobileWhitelist;
import com.smate.core.base.utils.security.SecurityUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/*
 * 微网信云短信服务接口.
 * 
 * @author ab
 */
@Service("mobileMessageWwxyService")
@Transactional(rollbackFor = Exception.class)
public class MobileMessageWwxyServiceImpl implements MobileMessageWwxyService {


  private String serviceURL = "http://sms.uninets.com.cn/Modules/Interface/http/Iservicesenc.aspx?flag=sendsms";

  private String username = "scm";

  private String password = "scm";

  // 加密后内容
  private String req = "";
  // Secret
  private String pwdKey = "dd1611024a316ae7ed4dacde2b3501bf";

  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  MessageLogDao messageLogDao;
  @Autowired
  MobileWhitelistDao mobileWhitelistDao;
  @Value("${domainscm}")
  private String domainscm;
  @Resource(name = "restTemplate")
  protected RestTemplate restTemplate;

  /*
   * @Autowired protected ResourceBundleMessageSource messageSource;
   */

  /**
   * 加密
   *
   * @param _strQ 待加密字符串
   * @param secret 密码
   * @return 加密后字符串
   * @throws Exception
   */
  public static String encryption(String _strQ, String secret) throws Exception {
    String StrKey = secret.substring(0, 8);
    String strIV = secret.substring(8, 16);
    // 创建密钥
    DESKeySpec ks = new DESKeySpec(StrKey.getBytes("UTF-8"));
    SecretKeyFactory skf = SecretKeyFactory.getInstance("DES");
    SecretKey sk = skf.generateSecret(ks);
    Cipher cip = Cipher.getInstance("DES/CBC/PKCS5Padding");// Cipher.getInstance("DES");
    // 加密
    IvParameterSpec iv = new IvParameterSpec(strIV.getBytes("UTF-8"));
    cip.init(Cipher.ENCRYPT_MODE, sk, iv);// IV的方式
    return encodeBase64(cip.doFinal(_strQ.getBytes("UTF-8"))).replace("+", "@");
  }

  /**
   * 解密
   *
   * @param _strQ 待解密字符串
   * @param secret 密码
   * @return 解密后字符串
   * @throws Exception
   */
  public static String decryption(String _strQ, String secret) throws Exception {

    String StrKey = secret.substring(0, 8);
    String strIV = secret.substring(8, 16);
    byte[] bt = decodeBase64(_strQ.replace("@", "+"));
    // 创建密钥
    DESKeySpec ks = new DESKeySpec(StrKey.getBytes("UTF-8"));
    SecretKeyFactory skf = SecretKeyFactory.getInstance("DES");
    SecretKey sk = skf.generateSecret(ks);
    Cipher cip = Cipher.getInstance("DES/CBC/PKCS5Padding");// Cipher.getInstance("DES");
    // 解密
    IvParameterSpec iv = new IvParameterSpec(strIV.getBytes("UTF-8"));
    cip.init(Cipher.DECRYPT_MODE, sk, iv);// IV的方式
    return new String(cip.doFinal(bt));
  }

  /***
   * encode by Base64
   */
  public static String encodeBase64(byte[] input) throws Exception {
    Class clazz = Class.forName("com.sun.org.apache.xerces.internal.impl.dv.util.Base64");
    Method mainMethod = clazz.getMethod("encode", byte[].class);
    mainMethod.setAccessible(true);
    Object retObj = mainMethod.invoke(null, new Object[] {input});
    return (String) retObj;
  }

  /***
   * decode by Base64
   */
  public static byte[] decodeBase64(String input) throws Exception {
    Class clazz = Class.forName("com.sun.org.apache.xerces.internal.impl.dv.util.Base64");
    Method mainMethod = clazz.getMethod("decode", String.class);
    mainMethod.setAccessible(true);
    Object retObj = mainMethod.invoke(null, input);
    return (byte[]) retObj;
  }

  /*
   * 方法名称：mt 功 能：发送短信 参 数：mobile,content,ext,stime,rrid(手机号，内容，扩展码，定时时间，唯一标识) 返 回
   * 值：唯一标识，如果不填写rrid将返回系统生成的
   */
  public String mt(String mobile, String content, String ext, String stime, String rrid) {

    if (StringUtils.isBlank(mobile) || StringUtils.isBlank(content)) {
      return "-999,手机或短信内容为空";// 手机或短信为空将不能发送短信通知
    }
    URL url;
    InputStreamReader isr = null;
    try {

      StringBuffer urlBuf = new StringBuffer();
      req = "{\"c\":\"" + content + "\",\"p\":\"" + mobile + "\"}";
      urlBuf.append(this.serviceURL).append("&loginname=").append(this.username).append("&password=")
          .append(this.password).append("&req=").append(encryption(req, pwdKey));
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
        return inputLine;
      } else {
        throw new Exception("SMS SERVER RESPONSE Fail!!!!");
      }

    } catch (Exception e) {
      e.printStackTrace();
      return "-998,短信发送出现异常";
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
   * rrid:9999
   * 
   * @param message
   * @return
   * @throws Exception
   */
  private String sendMessage(MobileMessageForm message) throws Exception {
    if (!checkMobile(message.destId)) {
      return "-999,手机号不在白名单中，发送失败。";
    }
    String retVal = mt(message.getDestId(), message.getContent(), "", "", null);
    if (StringUtils.isBlank(retVal)) {
      return null;
    }
    String[] str = retVal.split(",");
    if (str == null) {
      return null;
    }
    try {
      if (str[0].equals("0")) {
        logger.info("短信下行消息标识 [" + str[1] + "]，" + "目的号码 [" + message.destId + "]，" + "短信内容 [" + message.content + "]");
        /*
         * if (smsCode != null && smsLogService != null) { smsLogService.addMgsIdentity(smsCode, str[1]); }
         */
      } else {
        String key = str[1];
        // String errMsg = messageSource.getMessage(key, null, key, LocaleContextHolder.getLocale());
        throw new Exception("短信发送异常：" + key);
      }

    } catch (Exception e) {
      this.logger.error("短信发送异常", e);
      String errorMsg = e.getMessage();
      if (errorMsg != null && errorMsg.length() > 2000) {
        errorMsg = errorMsg.substring(0, 1500);
        logger.error("短信发送异常：errorMsg=" + errorMsg);
      }
      throw new Exception("短信发送异常：" + e);

    }
    return retVal;
  }

  /**
   * 构建注册的手机信息
   * 
   * @param code
   * @return
   */
  public static String buildRegMessage(String code) {
    if (StringUtils.isBlank(code))
      return "";
    return "【科研之友】科研之友注册验证码 " + code + " ，本验证码3分钟内有效。（请确保是本人操作且为本人手机，否则请忽略此短信）";
  }

  /**
   * 构建登录的手机信息
   * 
   * @param code
   * @return
   */
  public static String buildLoginMessage(String code) {
    if (StringUtils.isBlank(code))
      return "";
    return "【科研之友】科研之友登录验证码 " + code + " ，本验证码3分钟内有效。（请确保是本人操作且为本人手机，否则请忽略此短信）";
  }

  @Override
  public String initSendMessage(MobileMessageForm message) {
    MessageLog log = new MessageLog();
    try {
      log.setSmsType(message.getSmsType());
      log.setSmsTo(message.getDestId());
      log.setContent(message.getContent());
      log.setSendTime(new Date());
      Long currentUserId = SecurityUtils.getCurrentUserId();
      if (message.getPsnId() != null && message.getPsnId() != 0L) {
        log.setProduceLogPsnId(currentUserId);
      }
      String result = sendMessage(message);
      if (StringUtils.isBlank(result)) {
        log.setErrormsg("发送失败");
      } else {
        String[] str = result.split(",");
        if (str[0].equals("0")) {
          log.setStatus(SUCCESS);
        } else {
          log.setStatus(FAIL);
          log.setErrormsg(str[1]);
        }
      }
    } catch (Exception e) {
      logger.error("发送消息异常：destID：" + message.getDestId(), e);
      log.setErrormsg(e.toString());
    } finally {
      messageLogDao.save(log);
    }
    return "";
  }

  @Override
  public Boolean isSendMessageTheDay(String mobile, Long type) {
    if (StringUtils.isBlank(mobile) || type == null) {
      return false;
    }
    int sendCount = messageLogDao.getSendCount(mobile, type);
    if (sendCount > 0) {
      return true;
    }
    return false;
  }

  @Override
  public MessageLog findLastestLogByTime(Long psnId, Long type) {
    MessageLog messageLog = messageLogDao.getMessageLog(psnId, type);
    if (messageLog == null) {
      return null;
    }
    return messageLog;
  }

  /**
   * 判断手机号
   * 
   * @param mobile
   * @return
   */
  public boolean checkMobile(String mobile) {
    if (StringUtils.isBlank(mobile)) {
      return false;
    }
    String run_env = System.getenv("RUN_ENV");
    if ("run".equalsIgnoreCase(run_env)) {
      return true;
    }
    MobileWhitelist mobileWhitelist = mobileWhitelistDao.getByMobile(mobile);
    if (mobileWhitelist != null) {
      return true;
    }
    return false;
  }

  public static void main(String[] args) throws Exception {
    MobileMessageWwxyServiceImpl m = new MobileMessageWwxyServiceImpl();
    MobileMessageForm form = new MobileMessageForm();
    form.setSmsCode(0L);
    form.setContent("【科研之友】验证码:658741，10分钟内有效。");
    form.setDestId("13265547075");
    m.sendMessage(form);
  }

  @Override
  public String ipCheck() {
    String ipCheck = "2";// 1.手机验证 2.只能邮箱验证
    // 设置请求头部
    Map<String, Object> map = new HashMap<>();
    String SERVER_URL = domainscm + "/oauth/ip";
    HttpHeaders requestHeaders = new HttpHeaders();
    requestHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
    HttpEntity<String> requestEntity = new HttpEntity<String>(JacksonUtils.jsonObjectSerializer(map), requestHeaders);
    Map<String, Object> object =
        (Map<String, Object>) restTemplate.postForObject(SERVER_URL, requestEntity, Object.class);
    if (object != null && object.get("status").equals("success")) {
      ipCheck = String.valueOf(object.get("result"));
    }
    return ipCheck;

  }

  /**
   * 验证码是否有效
   * @param mobile
   * @param code
   * @return
   */
  @Override
  public Boolean checkCodeValide(String mobile, String code) {
    MessageLog messageLog = messageLogDao.findMessageLogNew(mobile, MobileMessageWwxyService.REG_TYPE);
    if(messageLog != null){
      if((new Date().getTime() - messageLog.getSendTime().getTime()) >3*60*1000){
        return messageLog.getContent().contains(code);
      }
    }
    return false;
  }

}
