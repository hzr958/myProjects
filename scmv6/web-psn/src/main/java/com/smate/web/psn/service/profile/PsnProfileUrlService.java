package com.smate.web.psn.service.profile;

import com.smate.web.psn.exception.PsnException;
import com.smate.web.psn.model.homepage.PersonProfileForm;


/**
 * 个人主页URL服务接口
 * 
 * @author Administrator
 *
 */
public interface PsnProfileUrlService {

  /**
   * 个人主页最长url为20字符.
   */
  int MAX_URL_LEN = 20;
  int MAX_URL_LOOP = 100;
  // 随机字符串长度
  int RAND_URL_LEN = 10;
  // 随机字母和数字
  String RAND_URL_CHAR = "abcdefghswvmnopqyzjk0123456789";
  String FIRST_NAME = "firstName";
  String LAST_NAME = "lastName";


  /**
   * 查找个人主页URL
   * 
   * @param psnId
   * @return
   * @throws ServiceException
   */
  String findUrl(Long psnId) throws PsnException;

  /**
   * 保存个人主页url.
   * 
   * @param url
   * @param psnId
   * @return
   * @throws ServiceException
   */
  int setPsnProfileUrl(String url, Long psnId) throws PsnException;

  /**
   * 查找或创建人员主页公开url
   * 
   * @param cname
   * @param psnId
   * @return
   * @throws ServiceException
   */
  String findAndCreateUrl(String cname, Long psnId) throws PsnException;

  /**
   * 保存人员短地址
   * 
   * @param form
   * @throws PsnException
   */
  void savePsnShortUrl(PersonProfileForm form) throws PsnException;

}
