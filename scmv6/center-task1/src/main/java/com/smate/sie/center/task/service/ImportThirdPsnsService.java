package com.smate.sie.center.task.service;

import java.util.List;

import com.smate.core.base.exception.ServiceException;
import com.smate.sie.center.task.model.ImportThirdPsns;

/**
 * 第三方人员信息接口.
 * 
 * @author xys
 *
 */
public interface ImportThirdPsnsService {

  /**
   * 获取待导入数据.
   * 
   * @param maxSize
   * @return
   * @throws ServiceException
   */
  public List<ImportThirdPsns> getThirdPsnsNeedImport(int maxSize) throws ServiceException;

  /**
   * 保存第三方人员信息.
   * 
   * @param importThirdPsns
   * @throws ServiceException
   */
  public void saveImportThirdPsns(ImportThirdPsns importThirdPsns) throws ServiceException;
}
