package com.smate.sie.center.task.service.codec;

import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.jce.interfaces.ECPrivateKey;
import org.bouncycastle.jce.interfaces.ECPublicKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.irissz.codec.utils.encrypt.ECCUtil;
import com.smate.core.base.utils.json.JacksonUtils;

/**
 * open加密解密服务接口.
 * 
 * @author ztg
 *
 */
@Service("taskCodecService")
public class TaskCodecServiceImpl implements TaskCodecService, InitializingBean {

  protected static Logger logger = LoggerFactory.getLogger(TaskCodecServiceImpl.class);

  /**
   * 公钥.
   */
  public static final Map<String, ECPublicKey> TOKEN_PUBLIC_KEY = new HashMap<String, ECPublicKey>();
  /**
   * 私钥.
   */
  public static final Map<String, ECPrivateKey> TOKEN_PRIVATE_KEY = new HashMap<String, ECPrivateKey>();

  @Value("${third.publik.key}")
  private String thirdPublicKey;

  /**
   * 读取公私钥.
   */
  @Override
  public void afterPropertiesSet() throws Exception {
    loadPublicKey();
    loadPrivateKey();
  }

  /**
   * 公钥加载. 公钥必须是json格式的 {"token":"publicKey"}.
   */
  private void loadPublicKey() {
    if (StringUtils.isBlank(thirdPublicKey)) {
      logger.debug("没有要加载的公钥!");
      return;
    }
    Map<String, String> map = JacksonUtils.jsonMapUnSerializer(thirdPublicKey);
    if (map == null) {
      logger.debug("没有要加载的公钥!");
      return;
    }
    map.forEach((k, v) -> {
      try {
        TOKEN_PUBLIC_KEY.put(k, ECCUtil.string2PublicKey(v));
      } catch (Exception e) {
        logger.error("公钥加载出错! key:" + k + ",v=" + v, e);
      }
    });
  }

  /**
   * 私钥加载.必须是json格式的 {"token":"publicKey"}.
   */
  private void loadPrivateKey() {
    String privateKey = System.getenv("PRIVATE_KEY");
    if (StringUtils.isBlank(privateKey)) {
      logger.debug("没有要加载的私钥匙!");
      return;
    }
    Map<String, String> map = JacksonUtils.jsonMapUnSerializer(privateKey);
    if (map == null) {
      logger.debug("没有要加载的私钥匙!");
      return;
    }
    map.forEach((k, v) -> {
      try {
        TOKEN_PRIVATE_KEY.put(k, ECCUtil.string2PrivateKey(v));
      } catch (Exception e) {
        logger.error("私钥加载出错! key:" + k + ",v=" + v, e);
      }
    });
  }

  /**
   * 加密方法.
   * 
   * @param token 对应加密公钥
   * @param content 要加密的内容
   * @return
   */
  @Override
  public String encode(String token, String content) {

    try {
      return ECCUtil.publicEncrypt(content, TOKEN_PUBLIC_KEY.get(token));
    } catch (Exception e) {
      logger.error("加密失败!" + JacksonUtils.mapToJsonStr(TOKEN_PUBLIC_KEY), e);
    }
    return null;
  }

  /**
   * 解密方法.
   * 
   * @param token 对应解密私钥
   * @param content 要解密的内容
   * @return
   */
  @Override
  public String decode(String token, String content) {

    try {
      return ECCUtil.privateDecrypt(content, TOKEN_PRIVATE_KEY.get(token));
    } catch (Exception e) {
      logger.error("解密密失败!" + JacksonUtils.mapToJsonStr(TOKEN_PRIVATE_KEY), e);
    }
    return null;
  }


}
