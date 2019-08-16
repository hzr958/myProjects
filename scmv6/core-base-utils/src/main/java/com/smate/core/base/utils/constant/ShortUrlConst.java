package com.smate.core.base.utils.constant;

import java.util.ArrayList;
import java.util.List;

/**
 * 短地址需要用到的常量
 * 
 * @author AiJiangBin
 *
 */
public class ShortUrlConst {

  /**
   * 创建人的psnid集合
   */
  public static List<String> createPsnIdList = new ArrayList<String>();
  static {
    createPsnIdList.add("0"); // 系统用户
    createPsnIdList.add("1"); // 匿名用户
  }

  public static String A_TYPE = "A"; // 个人成果类型 A
  public static String AT_TYPE = "AT"; // 个人成果类型 AT(产生的是32位长的短地址)
  public static String G_TYPE = "G"; // 群组站外短地址类型
  public static String S_TYPE = "S"; // 基准成果类型 S
  public static String SI_TYPE = "SI"; // 基准成果站内类型 SI
  public static String P_TYPE = "P"; // 个人站外短地址类型
  public static String B_TYPE = "B"; // 群组成果站外短地址类型
  public static String F_TYPE = "F"; // 个人文件，短地址

  public static String CREATE_PSN_ID = "createPsnId"; // 创建人0系统用户；1匿名用户
  public static String SHORT_URL_PARAMET = "shortUrlParamet"; // url
  // 的参数;json格式
  public static String TYPE = "type";// 创建短地址的类型
  public static String DES3_GRP_ID = "des3GrpId"; // 加密的群组id
  public static String DES3_PSN_ID = "des3PsnId"; // 加密的人员Id
  public static String SHORT_URL = "shortUrl"; // 短地址

}
