package com.smate.core.base.psn.service.psnname;

import java.util.Map;
import java.util.Set;

import com.smate.core.base.utils.model.security.Person;

/**
 * 人员名字转换为拼音服务
 * 
 * @author SYL
 * @date 2019-6-24
 *
 */
public interface PsnNameToPinyinService {

  /**
   * 名字
   */
  public static final String FIRSTNAME = "firstName";
  /**
   * 姓氏
   */
  public static final String LASTNAME = "lastName";
  /**
   * 全称
   */
  public static final String FULLNAME = "fullName";
  /**
   * 简称
   */
  public static final String INITNAME = "initName";
  /**
   * 前缀名
   */
  public static final String PREFIXNAME = "prefixName";

  /**
   * 将人名的姓和名提取出来，并进行转换为对应的拼音（包含姓名多音字和复姓识别），返回转换后的中文拼音首字母大写
   * 
   * @param psnName
   * @return
   */
  Map<String, String> parseNameToPinyin(String psnName);

  /**
   * 将人名的姓和名提取出来，并进行转换为对应的拼音（包含姓名多音字和复姓识别），返回转换后的中文拼音首字母大写
   * 
   * @param psnName
   * @param fxLength 姓氏的字符长度
   * @return
   */
  Map<String, String> parseNameToPinyin(String psnName, int fxLength);

  /**
   * 将人员中文的姓转换为拼音（包含姓氏多音字识别）,每个拼音首字母大写
   * 
   * @param lastName
   * @return
   */
  String parseLastNameToPinyin(String lastName);

  /**
   * 根据人员中文姓名生成人员的全称\简称\前缀名(fullname,initname,prefixname) eg:马建,段文杰,欧阳向远
   * 对于多音字中文名称，会优先选择与Person表中匹配的人员拼音
   * 
   * @param zhName
   * @return Map<String, Set<String>>
   *
   */
  Map<String, Set<String>> generalPsnPmName(Person person);

  /**
   * 将人名进行姓氏和名字拆分
   * 
   * @param psnName
   * @return
   */
  Map<String, String> parseZhfirstAndLast(String psnName);
}
