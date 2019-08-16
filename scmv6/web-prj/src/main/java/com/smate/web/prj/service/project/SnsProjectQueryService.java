package com.smate.web.prj.service.project;

import java.util.List;
import java.util.Map;

import org.hibernate.service.spi.ServiceException;

import com.smate.core.base.project.model.Project;
import com.smate.core.base.utils.model.Page;
import com.smate.web.prj.form.ProjectForm;
import com.smate.web.prj.form.ProjectQueryForm;
import com.smate.web.prj.form.wechat.PrjWeChatForm;

/**
 * 项目列表
 * 
 * @author zx
 *
 */
public interface SnsProjectQueryService {
  /**
   * 获取项目列表. query 查询项目的条件
   */

  /*
   * public Page<Project> queryOutput(ProjectQueryForm query, Page<Project> page) throws
   * ServiceException;
   */

  public void queryOutput(ProjectQueryForm query, Page<Project> page) throws ServiceException;

  /*
   * 群组和项目关系列表
   */
  public void prjGroupRelation(ProjectQueryForm form, List<ProjectForm> prjForm) throws ServiceException;

  /**
   * 未上传全文提醒
   */
  public Integer findNoFullTextPrj() throws ServiceException;

  /**
   * 查找项目和群组关系
   */
  public Long findGroupIdByPrjId(Long prjId) throws ServiceException;

  /**
   * 创建项目和群组的关系
   */
  public String GreatePrjGrpRelation(Long groupId, Long prjId) throws ServiceException;

  /**
   * 根据prjId获取全文权限
   * 
   * @param prjId
   * @return
   * @throws ServiceException
   */
  public String queryPrjFulltextAuthority(Long prjId) throws ServiceException;

  /**
   * 删除项目
   * 
   * @param prjIds
   * @throws ServiceException
   */
  public void deleteProject(String prjIds) throws ServiceException;

  /**
   * 站外站外查看项目主页
   */
  public void queryOutSidePrj(ProjectQueryForm query, Page page) throws ServiceException;

  /**
   * 查询资助机构
   */
  void queryAgencyName(ProjectQueryForm query) throws Exception;

  /**
   * 资助机构和年份的回显数据
   */

  public String getRecommendAgencyYear(ProjectQueryForm query) throws Exception;

  /**
   * 
   * @Title: queryPrjNumAmount
   * @Description: TODO(查询项目总数和资金总数)
   * @param @return
   * @param @throws ServiceException 入参
   * @return Map<String,Object> 返回类型
   */
  public Map<String, Object> queryPrjNumAmount(ProjectQueryForm query) throws ServiceException;

  /**
   * 
   * @Title: queryPrjNumAmount
   * @Description: TODO(查询项目总数和资金总数)
   * @param @return
   * @param @throws ServiceException 入参
   * @return Map<String,Object> 返回类型
   */
  public Map<String, Object> queryPrjNumAmount() throws ServiceException;

  /**
   * 移动端项目查询
   * 
   * @param form
   * @throws ServiceException
   */
  public void queryPrjList(PrjWeChatForm form) throws ServiceException;

  /**
   * 检查项目是否存在
   * 
   * @param form
   * @return
   * @throws ServiceException
   */
  public Boolean checkPrjExist(ProjectQueryForm form) throws ServiceException;

  /**
   * 通过项目id获取项目详情
   * 
   * @param prjId
   * @return
   * @throws ServiceException
   */
  public Project getById(Long prjId) throws ServiceException;
}
