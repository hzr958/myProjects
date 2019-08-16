package com.smate.center.batch.service.projectmerge;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smate.center.batch.constant.BatchConfConstant;
import com.smate.center.batch.context.OpenProjectContext;
import com.smate.center.batch.handler.OpenProjectHandler;
import com.smate.center.batch.model.sns.prj.OpenProject;

/**
 * Open 系统 - 第三方项目合并任务入口
 * 
 * @author LXZ
 * 
 * @since 6.0.1
 * @version 6.0.1
 */
@Service("openProjectProcessorService")
public class OpenProjectProcessorServiceImpl implements OpenProjectProcessorService {
  protected Logger logger = LoggerFactory.getLogger(getClass());

  private static final Integer PROJECT_SIZE = 1;
  private static final Integer TASK_STATUS_WAIT = 0;
  private static final Integer TASK_STATUS_REPEAT = 1;
  private static final Integer TASK_STATUS_SUCCESS = 2;
  private static final Integer TASK_STATUS_FAIL = 99;

  @Autowired
  private OpenProjectService openProjectService;
  @Autowired
  private OpenProjectHandler openProjectHandler;

  /**
   * 任务处理入口
   * 
   * @author LXZ
   * 
   * @since 6.0.1
   * @version 6.0.1
   * @throws Exception
   */
  public Integer run(OpenProject prj) {
    try {
      // 根据传入的ID去取OpenProject对象,这里需要修改
      OpenProjectContext context = null;// context
      OpenProject project = prj;
      if (excludeRepeat(project)) {
        // repeat
        openProjectService.changeStatus(project, TASK_STATUS_REPEAT);
      } else {
        // not repeat
        context = openProjectService.init();
        try {
          context.setOpenProject(project);
          openProjectHandler.doHandler(context, project);
        } catch (Exception ex) {
          openProjectService.changeStatus(project, TASK_STATUS_FAIL);
          logger.error("Open 系统 - 第三方项目合并任务合并出错.openProjectId:" + project.getId(), ex);
          throw new Exception(ex);
        }
        openProjectService.changeStatus(project, TASK_STATUS_SUCCESS);
      }
    } catch (Exception e) {
      logger.error("Open 系统 - 第三方项目合并任务入口出错", e);
      return BatchConfConstant.JOB_ERROR;
    }
    return BatchConfConstant.JOB_SUCCESS;

  }

  /**
   * 查重项目 重复的不进行处理 逻辑: 一: OpenProject表 第三方系统id+token相同 return 重复 二: 1,本机构批准号 存在于系统 return 重复
   * -数据不接收了,废弃 2,资助机构批准号 存在与系统 return 重复 3,(资助机构名称==资助机构名称) 并且 (项目名称==项目名称) return 重复
   * 
   * @author LXZ 重复 return true 不重复 return false
   * @since 6.0.1
   * @version 6.0.1
   * @return
   * @throws Exception
   */
  public boolean excludeRepeat(OpenProject project) throws Exception {

    return openProjectService.repeatProject(project);
  }
}
