package com.smate.center.batch.service.psn;

import java.io.Serializable;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.core.base.psn.model.PsnProfileUrl;

/**
 * 个人主页URL接口.
 * 
 * @author zhuangyanming
 * 
 */
public interface PsnProfileUrlService extends Serializable {
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
   * 查找个人主页url.
   * 
   * @param psnId
   * @return
   * @throws ServiceException
   */
  PsnProfileUrl findPsnProfileUrlById(Long psnId) throws ServiceException;

  /**
   * 删除个人主页url.
   * 
   * @param psnId
   * @throws ServiceException
   */
  void delPsnProfileUrl(Long psnId) throws ServiceException;

  /**
   * 保存个人主页url.
   * 
   * @param url
   * @param psnId
   * @return
   * @throws ServiceException
   */
  int setPsnProfileUrl(String url, Long psnId) throws ServiceException;

  /**
   * 查找个人主页的URL.
   * 
   * @param psnId
   * @return
   * @throws ServiceException
   */
  String findUrl(Long psnId) throws ServiceException;

  /**
   * 查找个人主页的人员ID.
   * 
   * @param url
   * @return
   * @throws ServiceException
   */
  Long findPsn(String url) throws ServiceException;

  /**
   * 查找或创建个人主页url.
   * 
   * @param cname
   * @param psnId
   * @return
   * @throws ServiceException
   */
  String findAndCreateUrl(String cname, Long psnId) throws ServiceException;

}
