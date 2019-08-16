package com.smate.core.base.consts.service;

import java.util.List;

/**
 * 2019-6-14
 * 
 * @author SYL
 *
 */
public interface ConstPsnLastNamePyService {

  /**
   * 根据中文姓氏查找对应的读音拼音
   * 
   * @param zhWord
   * @return
   */
  List<String> findPinyinByZhWord(String zhWord);

  /**
   * 根据中文姓氏查找对应的读音拼音，按id升序（获取第一个）
   * 
   * @param zhWord
   * @return
   */
  String findFirstPinyinByZhWord(String zhWord);

}
