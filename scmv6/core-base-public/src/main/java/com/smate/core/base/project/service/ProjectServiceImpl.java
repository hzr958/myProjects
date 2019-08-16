package com.smate.core.base.project.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.smate.core.base.exception.ServiceException;
import com.smate.core.base.project.dao.ProjectDao;
import com.smate.core.base.project.model.Project;

/**
 * 项目服务接口实现类
 *
 * @author houchuanjie
 * @date 2018年3月22日 下午5:20:38
 */
@Service("projectService")
@Transactional(rollbackFor = Exception.class)
public class ProjectServiceImpl implements ProjectService {
  private static final long serialVersionUID = 6471488923584834859L;
  private Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private ProjectDao projectDao;

  @Override
  public Project getProject(Long prjId) throws ServiceException {
    try {
      return projectDao.findPrjById(prjId);
    } catch (Exception e) {
      logger.error("获取项目出错！prjId={}", prjId, e);
      throw new ServiceException(e);
    }
  }

  @Override
  public Long createPrjId() throws ServiceException {
    return projectDao.createPrjId();
  }

  @Override
  public void saveProject(Project project) throws ServiceException {
    Assert.notNull(project, "待保存的project对象不能为空");
    try {
      projectDao.save(project);
    } catch (Exception e) {
      logger.error("保存项目信息出错！prj={}", project, e);
      throw new ServiceException(e);
    }
  }

}
