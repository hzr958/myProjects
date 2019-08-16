package com.smate.web.prj.service.project;

import java.util.Map;

import com.smate.web.prj.exception.PrjException;
import com.smate.web.prj.form.ProjectDetailsForm;
import com.smate.web.prj.model.common.PrjInfo;

/**
 * 项目详情服务接口
 * 
 * @author zzx
 *
 */
public interface SnsProjectDetailsService {
  /**
   * 显示项目详情页面
   * 
   * @param form
   * @throws Exception
   */
  void showPrjDetails(ProjectDetailsForm form) throws Exception;

  /**
   * 加载评论列表
   * 
   * @param form
   * @throws Exception
   */
  void showPrjComment(ProjectDetailsForm form) throws Exception;

  /**
   * 增加评论
   * 
   * @param form
   */
  void prjAddComment(ProjectDetailsForm form) throws Exception;

  /**
   * 判断是否登录
   * 
   * @param form
   */
  void checkCurrentPsnLogin(ProjectDetailsForm form, String domainscm) throws Exception;

  /**
   * 查找与群组相关连的项目的信息
   * 
   * @param psnId
   * @param grpId
   * @return
   * @throws PrjException
   */
  PrjInfo findGrpRelationPrjInfo(Long grpId) throws Exception;

  /**
   * 加载项目基本信息
   * 
   * @param form
   */
  void loadDetailProjectInfo(ProjectDetailsForm form) throws Exception;

  /**
   * 加载项目报告
   * 
   * @param form
   */
  void loadDetailProjectReport(ProjectDetailsForm form) throws Exception;

  /**
   * 加载项目成果
   * 
   * @param form
   */
  void loadDetailProjectPubinfo(ProjectDetailsForm form) throws Exception;

  /**
   * 加载项目经费
   * 
   * @param form
   */
  void loadDetailProjectExpenditure(ProjectDetailsForm form) throws Exception;

  /**
   * 加载项目经费-记一笔窗口
   * 
   * @param form
   */
  void loadProjectExpenditureAdd(ProjectDetailsForm form) throws Exception;

  /**
   * 加载项目列表
   * 
   * @param form
   */
  void loadDetailProjectReportList(ProjectDetailsForm form) throws Exception;

  /**
   * 加载项目列表条件
   * 
   * @param form
   */
  Map<String, Object> loadDetailProjectReportCount(ProjectDetailsForm form) throws Exception;

  /**
   * 上传全文 更新项目报告附件信息
   * 
   * @param reportId
   * @param fileId
   * @param psnId
   * @return
   * @throws Exception
   */
  public Map<String, String> uploadReportFile(Long reportId, Long fileId, Long psnId) throws Exception;

  /**
   * 保存一笔经费记录
   * 
   * @param form
   */
  void saveProjectExpenditure(ProjectDetailsForm form) throws Exception;

  /**
   * 加载项目经费附件
   * 
   * @param form
   * @throws Exception
   */
  void loadExpenAccessory(ProjectDetailsForm form) throws Exception;

  /**
   * 记录项目阅读记录
   * 
   * @param form
   * @throws Exception
   */
  void saveProjectView(ProjectDetailsForm form) throws Exception;

  /**
   * 加载项目成果列表
   * 
   * @param form
   */
  void loadDetailPrjPubList(ProjectDetailsForm form) throws Exception;

  /**
   * 加载项目列表条件
   * 
   * @param form
   */
  Map<String, Object> loadDetailPrjPubCount(ProjectDetailsForm form) throws Exception;

  /**
   * 加载项目支出记录
   * 
   * @param form
   * @throws Exception
   */
  void loadExpenRecord(ProjectDetailsForm form) throws Exception;

  /**
   * 删除指定支出记录
   * 
   * @param form
   */
  void deleteExpenRecord(ProjectDetailsForm form) throws Exception;

  /**
   * 加载项目成果列表成果匹配
   * 
   * @param form
   */
  void loadDetailPrjPubConfirm(ProjectDetailsForm form) throws Exception;

  /**
   * 项目成果列表成果匹配操作
   * 
   * @param form
   */
  Map<String, Object> ajaxPrjPubConfirmOpt(ProjectDetailsForm form) throws Exception;
}
