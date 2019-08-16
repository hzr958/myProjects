package com.smate.web.psn.service.resume;

import com.smate.web.psn.exception.PsnException;
import com.smate.web.psn.form.resume.PersonalResumeForm;

/**
 * 简历基础服务接口,
 * 
 * 定义除了各个具体类型简历公共的接口之外的一些接口
 * 
 * @author wsn
 *
 */
public interface PsnResumeBuilder {

  /**
   * 初始化人员简历服务，根据简历类型获取对应的服务
   * 
   * @param serviceType
   * @return
   * @throws PsnException
   */
  public PsnResumeBuildService initPsnResumeService(String serviceType) throws PsnException;

  /**
   * 获取人员简历列表
   * 
   * @param form
   * @throws PsnException
   */
  public void findPsnResumeList(PersonalResumeForm form) throws PsnException;

  /**
   * 删除简历
   * 
   * @param resumeId
   * @throws PsnException
   */
  public void deletePsnResume(Long resumeId) throws PsnException;

  /**
   * 保存简历模块
   * 
   * @param form
   * @throws PsnException
   */
  public void savePsnResumeModule(PersonalResumeForm form) throws PsnException;

  /**
   * 校验成果信息，如是否是成果拥有者等
   * 
   * @param form
   * @throws PsnException
   */
  public boolean checkPubInfo(PersonalResumeForm form) throws PsnException;

  /**
   * 判断是否是简历拥有者
   */
  public boolean isPsnResumeOwner(Long resumeId) throws PsnException;

  /**
   * 初始化简历详情所需的信息
   * 
   * @param form
   * @throws PsnException
   */
  public void initPsnCVInfo(PersonalResumeForm form) throws PsnException;
}
