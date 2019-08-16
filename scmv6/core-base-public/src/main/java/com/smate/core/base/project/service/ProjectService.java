package com.smate.core.base.project.service;

import com.smate.core.base.exception.ServiceException;
import com.smate.core.base.project.model.Project;

/**
 * 项目服务接口
 *
 * @author houchuanjie
 * @date 2018年3月22日 下午5:19:48
 */
public interface ProjectService {

  /**
   * 建立项目-群组关系
   * 
   * @param prjId
   * @throws ServiceException
   */
  public Project getProject(Long prjId) throws ServiceException;

  Long createPrjId() throws ServiceException;

  /**
   * 保存项目
   * 
   * @author houchuanjie
   * @date 2018年3月23日 上午9:45:41
   * @param project
   */
  public void saveProject(Project project) throws ServiceException;

}
