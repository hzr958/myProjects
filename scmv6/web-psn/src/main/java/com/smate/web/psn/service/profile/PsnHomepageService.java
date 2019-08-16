package com.smate.web.psn.service.profile;

import java.util.Map;

import com.smate.web.psn.exception.PsnException;
import com.smate.web.psn.model.homepage.PersonProfileForm;

/**
 * 人员主页服务接口
 *
 * @author wsn
 * @createTime 2017年7月21日 上午11:20:13
 *
 */
public interface PsnHomepageService {

  /**
   * 获取人员姓名
   * 
   * @param form
   * @return
   * @throws PsnException
   */
  public PersonProfileForm getPsnNameInfo(PersonProfileForm form) throws PsnException;

  /**
   * 拆分中文的姓和名
   * 
   * @param cname
   * @return
   * @throws PsnException
   */
  public Map<String, String> splitCName(String cname) throws PsnException;

  /**
   * 汉字转拼音
   * 
   * @param word
   * @return
   * @throws PsnException
   */
  public String parseWordToPinYin(String word, String wordType, Integer length) throws PsnException;

  public String registerParseWordToPinYin(String word, Integer length) throws PsnException;

  /**
   * 查找人员账号
   * 
   * @param psnId
   * @return
   * @throws PsnException
   */
  String findPsnAccount(Long psnId) throws PsnException;
}
