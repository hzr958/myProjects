package com.smate.web.prj.service.simplesns;

import java.util.Map;

import com.smate.web.prj.form.ProjectForm;

/**
 * 
 * 
 * @author zx 保存项目的上传全文
 *
 */
public interface PrjSimpleSnsSaveService {

  /**
   * 保存项目上传文件信息
   */
  public Map<String, String> uploadPubXmlFulltext(ProjectForm form) throws Exception;
}
