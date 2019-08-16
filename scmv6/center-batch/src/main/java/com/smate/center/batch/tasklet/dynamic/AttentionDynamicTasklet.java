package com.smate.center.batch.tasklet.dynamic;

import java.util.Map;

import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.batch.dao.sns.pub.DynamicDao;
import com.smate.center.batch.dao.sns.pub.DynamicMsgDao;
import com.smate.center.batch.service.dynamic.AttentionDynamicService;
import com.smate.center.batch.tasklet.base.BaseTasklet;
import com.smate.center.batch.tasklet.base.DataVerificationStatus;
import com.smate.core.base.utils.exception.BatchTaskException;

/**
 * 关注，添加动态和删除动态任务
 * 
 * @author aijiangbin
 *
 */
public class AttentionDynamicTasklet extends BaseTasklet {

  protected Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private AttentionDynamicService attentionDynamicService;
  @Autowired
  private DynamicMsgDao dynamicMsgDao;

  @Override
  public void taskExecution(Map jobContentMap) throws BatchTaskException {
    Object psnIdObj = jobContentMap.get("psnId");
    Object attPsnIdObj = jobContentMap.get("attPsnId");
    Object statusObj = jobContentMap.get("status");
    if (psnIdObj == null || !NumberUtils.isNumber(psnIdObj.toString()) || attPsnIdObj == null
        || !NumberUtils.isNumber(attPsnIdObj.toString()) || statusObj == null
        || !NumberUtils.isNumber(statusObj.toString())) {
      logger.error("处理关注成果动态异常，确认必要参数");
      return;
    }

    AttentionDynamicForm form = new AttentionDynamicForm();
    form.setPsnId(Long.valueOf(psnIdObj.toString()));
    form.setAttPsnId(Long.valueOf(attPsnIdObj.toString()));
    form.setStatus(Integer.parseInt(statusObj.toString()));

    try {
      attentionDynamicService.getDynamicMsgCount(form);
      if (form.getTotalPage() > 0) {
        for (int i = 0; i < form.getTotalPage(); i++) {
          form.setPageNo(i + 1);
          attentionDynamicService.dealAttentionDynamic(form);
        }
        logger.debug("处理关注动成员态成功");
      }


    } catch (Exception e) {
      logger.error("处理关注成员动态异常", e);
      throw new BatchTaskException(e);
    }
  }

  @Override
  public DataVerificationStatus dataVerification(String withData) throws BatchTaskException {

    return DataVerificationStatus.TRUE;
  }
}
