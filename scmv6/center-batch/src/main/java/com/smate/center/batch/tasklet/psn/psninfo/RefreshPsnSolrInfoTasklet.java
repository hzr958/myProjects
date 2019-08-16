package com.smate.center.batch.tasklet.psn.psninfo;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.batch.service.user.UserService;
import com.smate.center.batch.tasklet.base.BaseTasklet;
import com.smate.center.batch.tasklet.base.DataVerificationStatus;
import com.smate.core.base.utils.exception.BatchTaskException;

/**
 * 人员注册 后台数据处理
 * 
 * @author tsz
 *
 */
public class RefreshPsnSolrInfoTasklet extends BaseTasklet {

  private static final String DELETE_OPERATE_TYPE = "deleteSolrInfo";
  private static final String REFRESH_OPERATE_TYPE = "refreshSolrInfo";
  @Autowired
  private UserService userService;

  @Override
  public DataVerificationStatus dataVerification(String withData) throws BatchTaskException {
    return DataVerificationStatus.TRUE;
  }

  @Override
  public void taskExecution(Map jobContentMap) throws BatchTaskException {
    String psnIdStr = String.valueOf(jobContentMap.get("msg_id"));
    try {
      String operateType = String.valueOf(jobContentMap.get("operate_type"));
      Long psnId = Long.parseLong(psnIdStr);
      if (DELETE_OPERATE_TYPE.equalsIgnoreCase(operateType)) {
        userService.deleteSolrPsnInfo(psnId);
      } else if (REFRESH_OPERATE_TYPE.equalsIgnoreCase(operateType)) {
        userService.updateSolrPsnInfo(psnId);
      }
    } catch (Exception e) {
      super.logger.error("更新人员solr数据异常" + psnIdStr, e);
      throw new BatchTaskException("更新人员solr数据异常" + psnIdStr, e);
    }
  }

}
