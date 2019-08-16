package com.smate.web.psn.service.resume;

import java.io.File;

import javax.annotation.Resource;

import org.springframework.transaction.annotation.Transactional;

import com.smate.web.psn.exception.ServiceException;
import com.smate.web.psn.form.resume.PersonalResumeForm;
import com.smate.web.psn.model.consts.ResumeModuleConsts;

/**
 * 一般简历信息构建
 * 
 * @author wsn
 *
 */
@Transactional(rollbackFor = Exception.class)
public class NormalResumeBuildServiceImpl implements PsnResumeBuildService {
  @Resource(name = "normalWordService")
  private WordService wordService;

  @Override
  public void buildPsnResumeInfo(PersonalResumeForm form) throws Exception {
    if (form.getModuleId() != null) {
      switch (form.getModuleId()) {
        case ResumeModuleConsts.BASE_INFO:
          this.buildPsnBaseInfo(form);
          break;
        case ResumeModuleConsts.EDU_INFO:
          this.buildPsnEduInfo(form);
          break;
        case ResumeModuleConsts.WORK_INFO:
          this.buildPsnWorkInfo(form);
          break;
        case ResumeModuleConsts.PRJ_INFO:
          this.buildPsnResumePrjInfo(form);
          break;
        case ResumeModuleConsts.REPRESENT_PUB_INFO:
        case ResumeModuleConsts.OTHER_PUB_INFO:
          this.buildPsnResumePubInfo(form);
          break;
        default:
          break;
      }
    }
  }

  @Override
  public void buildPsnBaseInfo(PersonalResumeForm form) throws Exception {

  }

  @Override
  public void buildPsnWorkInfo(PersonalResumeForm form) throws Exception {

  }

  @Override
  public void buildPsnEduInfo(PersonalResumeForm form) throws Exception {

  }

  @Override
  public void buildPsnBriefInfo(PersonalResumeForm form) throws Exception {
    // TODO Auto-generated method stub

  }

  @Override
  public void buildPsnCredentialsInfo(PersonalResumeForm form) throws Exception {
    // TODO Auto-generated method stub

  }

  @Override
  public void buildPsnResumePubInfo(PersonalResumeForm form) throws ServiceException {
    // TODO Auto-generated method stub

  }

  @Override
  public void buildPsnResumePrjInfo(PersonalResumeForm form) throws Exception {
    // TODO Auto-generated method stub

  }

  @Override
  public void extend(PersonalResumeForm form) throws Exception {
    // TODO Auto-generated method stub

  }

  @Override
  public void createPsnResume(PersonalResumeForm form) throws Exception {

  }

  @Override
  public void updateResumePrjInfo(PersonalResumeForm form) throws Exception {
    // TODO Auto-generated method stub

  }

  @Override
  public void queryPrjInfo(PersonalResumeForm form) throws Exception {
    // TODO Auto-generated method stub

  }

  @Override
  public void getResumePrjList(PersonalResumeForm form) throws Exception {
    // TODO Auto-generated method stub

  }

  @Override
  public File exportCVToWord(PersonalResumeForm form) throws ServiceException {
    try {
      return wordService.exportWordFile(form);
    } catch (Exception e) {
      throw new ServiceException(e);
    }
  }

  @Override
  public void updateEduInfo(PersonalResumeForm form) throws Exception {
    // TODO Auto-generated method stub

  }

  @Override
  public void updateWorkInfo(PersonalResumeForm form) throws Exception {
    // TODO Auto-generated method stub

  }

  @Override
  public void getEduInfo(PersonalResumeForm form) throws Exception {
    // TODO Auto-generated method stub

  }

  @Override
  public void deleteEduInfo(PersonalResumeForm form) throws Exception {
    // TODO Auto-generated method stub

  }

  @Override
  public void getWorkInfo(PersonalResumeForm form) throws Exception {
    // TODO Auto-generated method stub

  }

  @Override
  public void deleteWorkInfo(PersonalResumeForm form) throws Exception {
    // TODO Auto-generated method stub

  }

  @Override
  public void editpsninfo(PersonalResumeForm form) throws Exception {
    // TODO Auto-generated method stub

  }

  @Override
  public void getpsninfo(PersonalResumeForm form) throws Exception {
    // TODO Auto-generated method stub

  }

  @Override
  public void editCvName(PersonalResumeForm form) throws Exception {
    // TODO Auto-generated method stub

  }
}
