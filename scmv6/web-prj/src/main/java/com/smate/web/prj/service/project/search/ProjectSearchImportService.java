package com.smate.web.prj.service.project.search;

import java.util.List;

import com.smate.core.base.exception.ServiceException;
import com.smate.core.base.psn.model.WorkHistory;
import com.smate.web.prj.form.PrjImportForm;

/**
 * 检索导入项目接口.
 * 
 * @author wsn
 * @date Dec 14, 2018
 */
public interface ProjectSearchImportService {

  /**
   * 初始化进入联邦检索页面所需信息.
   * 
   * @param form 不能为{@code null}
   */
  void initSearchInfo(PrjImportForm form) throws ServiceException;

  /**
   * .获取人员所有的单位信息.
   * 
   * @param psnId 不能为{@code null}
   * @return
   */
  List<WorkHistory> findPsnAllInsInfo(Long psnId);

  /**
   * .初始化待导入项目XML.
   * 
   * @param form 不能为{@code null}
   */
  void buildPendingImportPrjByXml(PrjImportForm form) throws ServiceException;


  /**
   * .保存待导入项目.
   * 
   * @param form 不能为{@code null}
   */
  void savePendingImportPrjs(PrjImportForm form) throws ServiceException;
}
