package com.smate.web.prj.service.project.fileimport;

import com.smate.core.base.exception.ServiceException;
import com.smate.web.prj.form.PrjImportForm;
import com.smate.web.prj.form.fileimport.PrjInfoDTO;

import java.io.File;
import java.util.Map;

/**
 * 项目文件导入service
 */
public interface PrjFileImportService {

  /**
   *
   *  resutlMap.put("xmlData", "");   把文件提取成 xml
   *  resutlMap.put("warnmsg", "");   错误信息
   *  resutlMap.put("count", 0);      条数
   * @return
   */
  public Map<String ,Object> extractFileData(File file,String sourceType , String sourceFileFileName);

  /**
   * 调整 xml数据
   * @param form
   * @throws ServiceException
   */
  public void buildPendingImportPrjByXml(PrjImportForm form) throws ServiceException ;


  public void savePendingImportPrjs(PrjImportForm form) throws ServiceException ;

  /**
   * 保存项目传输的对象
   * @return
   * @throws ServiceException
   */
  public int  savePrjData(PrjInfoDTO prjInfoDTO  , Long psnId) throws ServiceException ;
}
