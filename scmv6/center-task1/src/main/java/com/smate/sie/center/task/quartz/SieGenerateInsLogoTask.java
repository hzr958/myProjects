package com.smate.sie.center.task.quartz;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.task.base.TaskAbstract;
import com.smate.core.base.exception.ServiceException;
import com.smate.core.base.utils.model.Page;
import com.smate.core.base.utils.model.rol.Sie6InsPortal;
import com.smate.sie.center.task.service.SieGenerateInsLogoService;

/**
 * 批量生成单位默认LOGO
 * 
 * @author xr
 *
 */
public class SieGenerateInsLogoTask extends TaskAbstract {
  Logger logger = LoggerFactory.getLogger(getClass());
  private static int batchSize = 1000;
  @Autowired
  private SieGenerateInsLogoService sieGenerateInsLogoService;

  public SieGenerateInsLogoTask() {
    super();
  }

  public SieGenerateInsLogoTask(String beanName) {
    super(beanName);
  }


  public void doRun() throws Exception {
    if (!super.isAllowExecution()) {
      return;
    }

    while (true) {
      try {
        if (!super.isAllowExecution()) {
          break;
        }
        // 查询需要添加默认LOGO的单位域名
        Page<Sie6InsPortal> portalPage = new Page<Sie6InsPortal>(batchSize);
        portalPage = sieGenerateInsLogoService.findInsProtalByWithoutLogo(portalPage);
        if (portalPage == null || portalPage.getTotalCount() == 0) {
          return;
        }
        if (portalPage.getTotalCount() > 0) {
          if (!super.isAllowExecution()) {
            break;
          }
          Long tempTotalPage = portalPage.getTotalPages();
          for (int j = 1; j <= tempTotalPage; j++) {
            if (!super.isAllowExecution()) {
              break;
            }
            if (j > 1) {
              portalPage.setPageNo(j);
              portalPage = sieGenerateInsLogoService.findInsProtalByWithoutLogo(portalPage);
            }
            for (Sie6InsPortal insprotal : portalPage.getResult()) {
              if (!super.isAllowExecution()) {
                break;
              }
              // 生成单位logo
              sieGenerateInsLogoService.GenerateInsLogo(insprotal.getInsId());
            }
          }
        }
      } catch (Exception e) {
        logger.error("批量生成单位默认LOGO任务，任务执行失败", e);
        throw new ServiceException("批量生成单位默认LOGO任务，任务执行失败", e);
      }
    }


  }


}
