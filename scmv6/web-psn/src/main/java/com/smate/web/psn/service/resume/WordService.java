package com.smate.web.psn.service.resume;

import java.io.File;

import com.smate.web.psn.form.resume.PersonalResumeForm;

/**
 * 导出word简历服务
 * 
 * @author lhd
 *
 */
public interface WordService {

  /**
   * 导出word简历
   * 
   * @param form
   * @return
   * @throws Exception
   */
  File exportWordFile(PersonalResumeForm form) throws Exception;

}
