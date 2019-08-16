package com.smate.web.prj.service.project;

import java.util.Map;

import com.smate.core.base.exception.NoPermissionException;
import com.smate.core.base.exception.ServiceException;
import com.smate.web.prj.exception.ProjectNotExistException;
import com.smate.web.prj.form.ProjectOptForm;

/**
 * 项目操作服务类接口
 * 
 * @author houchuanjie
 * @date 2018年3月15日 下午6:11:45
 */
public interface SnsPrjOptService {

  /**
   * 构建编辑form表单参数
   * 
   * @author houchuanjie
   * @date 2018年3月19日 下午5:11:27
   * @param prjOptForm
   * @throws ProjectNotExistException
   * @throws NoPermissionException
   * @throws ServiceException
   * @throws ProjectNotFoundException
   */
  void bulidEditFormData(ProjectOptForm prjOptForm)
      throws ProjectNotExistException, ServiceException, NoPermissionException;

  /**
   * 项目详情 - 添加赞
   * 
   * @param form
   * @throws Exception
   */
  String prjAddAward(ProjectOptForm form) throws Exception;

  /**
   * 项目详情 - 取消赞
   * 
   * @param form
   * @throws Exception
   */
  String prjCancelAward(ProjectOptForm form) throws Exception;

  /**
   * 保存编辑后的form表单结果
   * 
   * @author houchuanjie
   * @date 2018年3月21日 下午4:53:17
   * @param form
   * @param paramsMap 客户端发送的参数集合
   * @throws ServiceException
   * @throws ProjectNotExistException
   * @throws NoPermissionException
   */
  void saveEditFormData(ProjectOptForm form, Map paramsMap)
      throws ProjectNotExistException, ServiceException, NoPermissionException;

  void bulidEnterFormData(ProjectOptForm form) throws Exception;

  /**
   * 在center-batch那边更新人员solr信息
   * 
   * @param psnId
   */
  void refreshPsnSolrInfoByTask(Long currentUserId);

  /**
   * 在center-batch那边删除人员solr信息
   * 
   * @param psnId
   */
  void deletePsnSolrInfoByTask(Long currentUserId);


}
