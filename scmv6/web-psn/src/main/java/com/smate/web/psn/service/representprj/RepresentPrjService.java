package com.smate.web.psn.service.representprj;

import java.util.List;

import com.smate.core.base.project.model.Project;
import com.smate.core.base.utils.model.Page;
import com.smate.web.psn.exception.PsnException;
import com.smate.web.psn.model.homepage.PersonProfileForm;

/**
 * 人员代表性项目服务接口
 *
 * @author wsn
 * @createTime 2017年3月14日 下午8:29:37
 *
 */
public interface RepresentPrjService {

  /**
   * 获取人员代表性项目信息
   * 
   * @param psnId
   * @param status
   * @return
   */
  List<Project> findPsnRepresentPrjInfoList(Long psnId, Integer status, boolean isMyself);

  /**
   * 查找人员公开项目列表
   * 
   * @param psnId
   * @param status
   * @return
   */
  List<Project> findPsnOpenPrjList(Long psnId, Long cnfId, Page page, Integer anyUser, String queryString);

  /**
   * 查找人员公开项目列表
   * 
   * @param psnId
   * @param status
   * @return
   */
  PersonProfileForm findPsnOpenPrjListByForm(PersonProfileForm form);

  /**
   * 保存人员代表性项目
   * 
   * @param psnId
   * @param pubIds
   * @return
   */
  List<Project> savePsnRepresentPrj(Long psnId, String prjIds);

  /**
   * 构建代表性项目信息
   * 
   * @param form
   * @return
   */
  PersonProfileForm buildPsnRepresentPrjInfo(PersonProfileForm form);

  /**
   * 构建移动端代表性项目信息
   * 
   * @param psnId
   * @param isMyself
   * @return
   * @throws PsnException
   */
  public List<Project> buildMobilePsnRepresentPrjInfo(Long psnId, boolean isMyself) throws PsnException;
}
