package com.smate.center.task.quartz.institution;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.task.base.TaskAbstract;
import com.smate.center.task.model.rol.quartz.InstitutionRol;
import com.smate.center.task.service.institution.UpdateInsInfoFromSieService;
import com.smate.core.base.utils.cache.CacheService;

/**
 * 从SIE库更新SNS库的institution表和V_INS_STATISTICS表 方法作废，改用视图
 * 
 * @author wsn
 *
 */
@Deprecated
public class UpdateInsInfoFromSIETask extends TaskAbstract {

  protected Logger logger = LoggerFactory.getLogger(getClass());
  private final static Integer size = 500;
  private static Long startInsId = 0L;
  private static Long endInsId = 999999999L;
  @Autowired
  private UpdateInsInfoFromSieService updateInsInfoFromSieService;
  @Autowired
  private CacheService cacheService;

  public UpdateInsInfoFromSIETask() {
    super();
  }

  public UpdateInsInfoFromSIETask(String beanName) {
    super(beanName);
  }

  public void doRun() throws Exception {
    try {
      logger.info("---------------------------执行机构信息更新任务-----------------------------------");
      if (!super.isAllowExecution()) {
        return;
      }
      int i = 1;
      while (true) {
        Long lastInsId = (Long) cacheService.get("update_ins_info_insId", "last_ins_id");
        if (lastInsId == null) {
          lastInsId = startInsId;
        }
        List<InstitutionRol> rolInsList = updateInsInfoFromSieService.findInstitutionRolList(size, lastInsId, endInsId);
        if (CollectionUtils.isNotEmpty(rolInsList)) {
          Integer lastIndex = rolInsList.size() - 1;
          InstitutionRol lastId = rolInsList.get(lastIndex);
          this.cacheService.put("update_ins_info_insId", 60 * 60 * 24, "last_ins_id", lastId.getId());
          for (InstitutionRol insRol : rolInsList) {
            try {
              updateInsInfoFromSieService.updateInsInfoFromROL(insRol);
            } catch (Exception e) {
              logger.error("更新机构信息出错, insId = " + insRol.getId(), e);
            }
          }
        } else {
          logger.info("----------------------------------更新完成-------------------------------------");
          return;
        }
        logger.info("----------------------------------更新了-------------------------------------" + size * i);
        i++;
      }
    } catch (Exception e) {
      logger.error("更新机构信息任务出错 ", e);
    }
  }

}
