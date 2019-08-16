package com.smate.center.open.service.codec;

import com.irissz.codec.utils.encrypt.ECCUtil;
import com.smate.center.open.consts.OpenConsts;
import com.smate.center.open.exception.OpenDecodeException;
import com.smate.center.open.exception.OpenEncodeException;
import com.smate.core.base.utils.json.JacksonUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.jce.interfaces.ECPrivateKey;
import org.bouncycastle.jce.interfaces.ECPublicKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * open加密解密服务接口.
 * 
 * @author tsz
 *
 */
@Service("openCodecService")
public class OpenCodecServiceImpl implements OpenCodecService, InitializingBean {

  protected static Logger logger = LoggerFactory.getLogger(OpenCodecServiceImpl.class);
  /**
   * 返回结果需要加密的服务.
   */
  public static final String[] ENCODE_SERVICE_TYPE = {"jyh99kls", "lxj3219s", "siescget", "siescirv"};// "siescirv"

  /**
   * 请求参数需要解密的服务.
   */
  public static final String[] DECODE_SERVICE_TYPE = {"jyh99kls", "lxj3219s", "siescget", "siescirv"}; // "jyh99kls", //
                                                                                                       // "lxj3219s"
  // "siescirv"

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
   * 检查解密情况.
   * 
   * @param map
   * @throws Exception
   */
  @Override
  public void checkParameterDecode(Map<String, Object> map) {
    // 是否需要解密
    String serviceType = map.get(OpenConsts.MAP_TYPE).toString();
    if (!ArrayUtils.contains(DECODE_SERVICE_TYPE, serviceType)) {
      return;
    }else if(isDevEnv()){
      return;
    }
    // 如果serviceType 是以sie开头就token=11111111
    String token = OpenConsts.SMATE_TOKEN;
    if (serviceType.startsWith("sie")) {
      token = OpenConsts.SIE_TOKEN;
    }
    // 检查解密参数
    Object dataO = map.get(OpenConsts.MAP_DATA);
    String content = "";
    if (dataO != null) {
      String data = dataO.toString();
      Map<String, String> tempMap = new HashMap<String, String>();
      try {
        tempMap = JacksonUtils.json2Map(data);
      } catch (IOException e) {
        logger.error("参数获取失败", e);
      }
      content = tempMap.get("content");
    }
    // 解密
    try {
      String newDataStr = ECCUtil.privateDecrypt(content, TOKEN_PRIVATE_KEY.get(token));
      map.put(OpenConsts.MAP_DATA, newDataStr);
    } catch (Exception e) {
      logger.error("data 解密失败！按正常参数处理." + JacksonUtils.mapToJsonStr(map), e);
      throw new OpenDecodeException("data参数解密失败", e);
    }
  }

  /**
   * 检查加密情况.
   * 
   * @param map
   */
  @Override
  public void checkParameterEncode(Map<String, Object> map, Map<String, Object> resutlMap) {
    // 是否需要加密
    String serviceType = map.get(OpenConsts.MAP_TYPE).toString();
    String token = map.get(OpenConsts.MAP_TOKEN).toString();
    if (!ArrayUtils.contains(ENCODE_SERVICE_TYPE, serviceType)) {
      return;
    }else if(isDevEnv()){
      return;
    }
    // 检查加密参数
    if (resutlMap.get(OpenConsts.RESULT_DATA) == null) {
      // 没有返回值就不加密
      return;
    }

    // 加密
    try {
      String newDataStr = ECCUtil.publicEncrypt(
          JacksonUtils.jsonObjectSerializerNoNull(resutlMap.get(OpenConsts.RESULT_DATA)), TOKEN_PUBLIC_KEY.get(token));
      Map<String, String> tempMap = new HashMap<String, String>();
      tempMap.put("content", newDataStr);
      resutlMap.put(OpenConsts.RESULT_DATA, JacksonUtils.mapToJsonStr(tempMap));
    } catch (Exception e) {
      logger.error("返回数据加密失败！按正常参数处理." + JacksonUtils.mapToJsonStr(map) + JacksonUtils.mapToJsonStr(resutlMap), e);
      throw new OpenEncodeException("返回结果加密失败", e);
    }
  }

  /**
   * 加密方法.
   * 
   * @param token 对应加密公钥
   * @param content 要加密的内容
   * @return
   */
  public static String encode(String token, String content) {

    try {
      return ECCUtil.publicEncrypt(content, TOKEN_PUBLIC_KEY.get(token));
    } catch (Exception e) {
      logger.error("加密失败!" + JacksonUtils.mapToJsonStr(TOKEN_PUBLIC_KEY), e);
    }
    return null;
  }

  /**
   * 是否开发环境
   * @return
   */
  public boolean isDevEnv(){
    String runEnv = System.getenv("RUN_ENV");
    if("development".equalsIgnoreCase(runEnv)){
      return true ;
    }
    return false ;
  }
  /**
   * 解密方法.
   * 
   * @param token 对应解密私钥
   * @param content 要解密的内容
   * @return
   */
  public static String decode(String token, String content) {

    try {
      return ECCUtil.privateDecrypt(content, TOKEN_PRIVATE_KEY.get(token));
    } catch (Exception e) {
      logger.error("解密密失败!" + JacksonUtils.mapToJsonStr(TOKEN_PRIVATE_KEY), e);
    }
    return null;
  }

}
