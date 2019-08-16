package com.smate.web.psn.service.resume;

import java.io.File;

import com.smate.web.psn.exception.ServiceException;
import com.smate.web.psn.form.resume.PersonalResumeForm;

/**
 * 构建简历信息接口
 * 
 * @author wsn
 *
 */
public interface PsnResumeBuildService {

  // 根据模块ID构建对应模块信息
  public void buildPsnResumeInfo(PersonalResumeForm form) throws Exception;

  // 修改简历名称
  public void editCvName(PersonalResumeForm form) throws Exception;

  // 构建人员基础信息
  public void buildPsnBaseInfo(PersonalResumeForm form) throws Exception;

  // 修改人员基础信息
  public void editpsninfo(PersonalResumeForm form) throws Exception;

  // 获取人员基础信息
  public void getpsninfo(PersonalResumeForm form) throws Exception;

  // 构建人员工作信息
  public void buildPsnWorkInfo(PersonalResumeForm form) throws Exception;

  // 构建人员教育信息
  public void buildPsnEduInfo(PersonalResumeForm form) throws Exception;

  // 构建人员简介信息
  public void buildPsnBriefInfo(PersonalResumeForm form) throws Exception;

  // 构建人员证件信息
  public void buildPsnCredentialsInfo(PersonalResumeForm form) throws Exception;

  // 构建成果信息
  public void buildPsnResumePubInfo(PersonalResumeForm form) throws ServiceException;

  // 构建项目信息
  public void buildPsnResumePrjInfo(PersonalResumeForm form) throws Exception;

  // 创建简历
  public void createPsnResume(PersonalResumeForm form) throws Exception;

  // 扩展方法
  public void extend(PersonalResumeForm form) throws Exception;

  // 更新项目信息
  public void updateResumePrjInfo(PersonalResumeForm form) throws Exception;

  // 获取项目列表
  public void queryPrjInfo(PersonalResumeForm form) throws Exception;

  // 获取已导入项目列表
  public void getResumePrjList(PersonalResumeForm form) throws Exception;

  // 导出word格式简历
  public File exportCVToWord(PersonalResumeForm form) throws ServiceException;

  // 添加或修改教育经历
  public void updateEduInfo(PersonalResumeForm form) throws Exception;

  // 获取教育经历
  public void getEduInfo(PersonalResumeForm form) throws Exception;

  // 获取工作经历
  public void getWorkInfo(PersonalResumeForm form) throws Exception;

  // 删除教育经历
  public void deleteEduInfo(PersonalResumeForm form) throws Exception;

  // 删除工作经历
  public void deleteWorkInfo(PersonalResumeForm form) throws Exception;

  // 添加或修改工作经历
  public void updateWorkInfo(PersonalResumeForm form) throws Exception;
}
