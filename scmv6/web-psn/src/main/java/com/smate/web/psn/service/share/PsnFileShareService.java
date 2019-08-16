package com.smate.web.psn.service.share;

import com.smate.core.base.utils.model.Page;
import com.smate.web.psn.model.share.FileShareForm;

/**
 * 个人文件分享
 * 
 * @author aijiangbin
 *
 */
public interface PsnFileShareService {

  /**
   * 查找文件分享列表
   * 
   * @param from
   */
  public void findEmailVileFiles(FileShareForm form, Page page) throws Exception;

}
