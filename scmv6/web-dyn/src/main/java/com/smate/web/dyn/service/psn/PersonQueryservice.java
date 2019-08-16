package com.smate.web.dyn.service.psn;

import java.util.Map;

import com.smate.core.base.utils.exception.DynException;
import com.smate.core.base.utils.model.security.Person;

/**
 * 人员查询服务接口
 * 
 * @author zk
 *
 */
public interface PersonQueryservice {

  /**
   * 查询人员单位和职称
   * 
   * @param psnId
   * @return
   * @throws DynException
   */
  Person findPersonInsAndPos(Long psnId) throws DynException;

  /**
   * 获取人员姓名信息
   * 
   * @param psnId
   * @return
   * @throws Exception
   */
  Person findPsnName(Long psnId) throws DynException;

  /**
   * 获取人员基础信息
   * 
   * @param psnId
   * @return
   * @throws DynException
   */
  public Person findPerson(Long psnId) throws DynException;

  /**
   * 获取人员姓名
   * 
   * @param person
   * @param locale
   * @return
   */
  String getPsnName(Person person, String locale);

  /**
   * 获取人员基础信息
   * 
   * @param psnId
   * @return
   * @throws DynException
   */
  public Person findPersonBase(Long psnId) throws DynException;

  /**
   * 获取人员头像
   * 
   * @param psnId
   * @return
   * @throws DynException
   */
  public String findAvatarsById(Long psnId) throws DynException;

}
