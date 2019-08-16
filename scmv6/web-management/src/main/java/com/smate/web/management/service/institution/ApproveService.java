package com.smate.web.management.service.institution;

import com.smate.web.management.model.institution.bpo.FileUploadSimple;

public interface ApproveService {

  /**
   * 上传并保存单位的传真附件
   * 
   * @param name
   * @throws ServiceException
   * @throws Exception
   */
  public FileUploadSimple uploadAndSaveFaxAttachment(FileUploadSimple fileUploadSimple) throws Exception;

  public FileUploadSimple uploadAndSaveInsLog(FileUploadSimple fileUploadSimple) throws Exception;

}
