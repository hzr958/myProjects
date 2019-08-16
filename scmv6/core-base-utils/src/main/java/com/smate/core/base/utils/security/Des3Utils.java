package com.smate.core.base.utils.security;

import org.acegisecurity.util.EncryptionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.smate.core.base.utils.constant.ServiceConstants;
import com.smate.core.base.utils.string.ServiceUtil;

public class Des3Utils {
  private static final Log LOGGER = LogFactory.getLog(ServiceUtil.class);

  /*
   * 针对URL要求的 des3加密.
   * 
   * @param str
   * 
   * @return
   */
  public static String encodeToDes3(String str) {

    return Des3Utils.encodeToDes3(str, ServiceConstants.ENCRYPT_KEY);
  }

  /**
   * 针对URL要求的des3解密 .
   * 
   * @param str
   * @return
   */
  public static String decodeFromDes3(String str) {

    return Des3Utils.decodeFromDes3(str, ServiceConstants.ENCRYPT_KEY);
  }

  /**
   * 针对URL要求的 des3加密，特殊使用，例如特殊的查看权限.
   * 
   * @param str
   * @return
   */
  public static String specEncodeToDes3(String str) {

    return Des3Utils.encodeToDes3(str, ServiceConstants.SPE_ENCRYPT_KEY);
  }

  /**
   * 针对URL要求的des3解密，特殊使用，例如特殊的查看权限.
   * 
   * @param str
   * @return
   */
  public static String specDecodeFromDes3(String str) {

    return Des3Utils.decodeFromDes3(str, ServiceConstants.SPE_ENCRYPT_KEY);
  }

  /**
   * 针对egrant成果url的des3解密.
   * 
   * @param str
   * @return
   */
  public static String egrantDecodeFromDes3(String str) {
    return Des3Utils.decodeFromDes3(str, ServiceConstants.EGRANT_ENCRYPT_KEY);
  }

  /**
   * 针对ISIS关键词确认url的des3解密.
   * 
   * @param str
   * @return
   */
  public static String IsisDecodeFromDes3(String str) {
    return Des3Utils.decodeFromDes3(str, ServiceConstants.ISIS_ENCRYPT_KEY);
  }

  /**
   * 针对URL要求的 des3加密，指定加密KEY.
   * 
   * @param str
   * @param encryptKey
   * @return
   */
  public static String encodeToDes3(String str, String encryptKey) {

    try {
      if (StringUtils.isBlank(str))
        return null;
      return java.net.URLEncoder.encode(EncryptionUtils.encrypt(encryptKey, str), "utf-8");
    } catch (Exception e) {
      LOGGER.warn("des3加密失败:" + str);
      return null;
    }
  }

  /**
   * 针对URL要求的des3解密 ，指定加密KEY.
   * 
   * @param str
   * @param encryptKey
   * @return
   */
  public static String decodeFromDes3(String str, String encryptKey) {

    try {
      if (StringUtils.isBlank(str))
        return null;
      String tmp = null;
      tmp = str.replace("+", "%2B");
      tmp = tmp.replace("=", "%3D");
      tmp = tmp.replace("%25", "%");
      return EncryptionUtils.decrypt(encryptKey, java.net.URLDecoder.decode(tmp, "utf-8"));

    } catch (Exception e) {
      LOGGER.warn("des3解密失败:" + str);
      return null;
    }
  }

  public static void main(String args[]) {
    String str = "JvUzHyT7+GKY/U9/UfPfWA==";
    System.out.println(Des3Utils.decodeFromDes3(str));
  }

}
