package com.smate.sie.center.task.quartz;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.task.base.TaskAbstract;
import com.smate.sie.center.task.model.ImportInsDataError;
import com.smate.sie.center.task.model.ImportInsDataInfo;
import com.smate.sie.center.task.service.ImportInsDataErrorService;
import com.smate.sie.center.task.service.ImportInsService;

/**
 * 批量创建单位任务
 * 
 * @author hd
 *
 */
public class SieAddInsByBacthTask extends TaskAbstract {

  Logger logger = LoggerFactory.getLogger(getClass());
  private static int batchSize = 100;
  @Autowired
  private ImportInsService importInsService;
  @Autowired
  private ImportInsDataErrorService importInsDataErrorService;

  public SieAddInsByBacthTask() {
    super();
  }

  public SieAddInsByBacthTask(String beanName) {
    super(beanName);
  }


  public void doRun() throws Exception {
    if (!super.isAllowExecution()) {
      return;
    }
    while (true) {
      List<ImportInsDataInfo> insList = importInsService.findSyncInsList(batchSize);
      if (insList == null || insList.size() == 0) {
        return;
      }
      for (ImportInsDataInfo tmpInsInfo : insList) {
        try {
          importInsService.doInsCreate(tmpInsInfo);
          // 成功
          tmpInsInfo.setSynFlag(1L);
          importInsService.updateTmpInsInfo(tmpInsInfo);
        } catch (Exception e) {
          tmpInsInfo.setSynFlag(2L);
          tmpInsInfo.setSynMsg(StringUtils.substring(e.getMessage(), 0, 2000));
          // 失败
          importInsService.updateTmpInsInfo(tmpInsInfo);
          Long psnId = importInsService.findPersonId(tmpInsInfo.getContactEmail());
          if (psnId != null) {
            try {
              ImportInsDataError error = new ImportInsDataError();
              error.setErrDate(new Date());
              error.setErrMsg("token :" + tmpInsInfo.getId().getToken() + "  org_code : "
                  + tmpInsInfo.getId().getOrgCode() + "   错误日志： " + e.getMessage() + "---" + e.toString());
              error.setPsnId(psnId);
              importInsDataErrorService.saveObject(error);
            } catch (Exception e2) {
              logger.error("保存错误日志出错", e2);
            }
          }
          logger.error("批量创建单位任务出错", e);
        }
      }
    }
  }

}
