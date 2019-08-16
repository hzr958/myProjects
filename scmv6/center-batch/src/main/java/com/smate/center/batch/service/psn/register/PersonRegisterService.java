package com.smate.center.batch.service.psn.register;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.psn.register.PersonRegister;


/**
 * 人员注册服务接口.
 * 
 * @author tsz
 * 
 */
public interface PersonRegisterService {

  /**
   * 个人注册.
   * 
   * @param person
   * @throws ServiceException
   * @throws Exception
   */
  Long saveR(PersonRegister person) throws Exception;

  /**
   * 整理人员注册时的有效信息并发送至基准库端.
   * 
   * @param person
   */
  public void matchPdwhPub(PersonRegister person);

  public PersonRegister getPersonRegisterInfo(Long psnId);

  /**
   * 人员注册时，psnHtml列表需要处理的数据
   * 
   * @param zk
   * @since 2014/09/02
   * @param person
   * @param result
   */
  public void psnHtmlNeedData(PersonRegister person, Long result) throws ServiceException;

  /**
   * 初始化人员基金推荐条件
   * 
   * @param psnId
   * @throws ServiceException
   */
  public void initPsnFundRecommend(Long psnId) throws ServiceException;
}
