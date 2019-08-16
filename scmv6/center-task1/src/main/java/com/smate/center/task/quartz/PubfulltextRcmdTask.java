package com.smate.center.task.quartz;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.task.base.TaskAbstract;
import com.smate.center.task.exception.SingleTaskException;
import com.smate.center.task.model.publicpub.PubFulltextUploadLog;
import com.smate.center.task.model.sns.quartz.PubFulltextPsnRcmd;
import com.smate.center.task.service.publicpub.PubfulltextRcmdService;
import com.smate.center.task.v8pub.sns.po.PubSnsPO;

/**
 * 全文推荐任务
 * 
 * @author zll
 *
 */
public class PubfulltextRcmdTask extends TaskAbstract {
  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private PubfulltextRcmdService pubfulltextRcmdService;

  public PubfulltextRcmdTask() {
    super();
  }

  public PubfulltextRcmdTask(String beanName) {
    super(beanName);
  }

  public void doRun() throws SingleTaskException {

    if (!super.isAllowExecution()) {
      return;
    }
    List<PubFulltextUploadLog> list = pubfulltextRcmdService.getNeedRcmdData();
    if (CollectionUtils.isNotEmpty(list)) {
      for (PubFulltextUploadLog uploadLog : list) {
        try {
          Long pdwhPubId = null;
          // 处理个人成果全文
          if (uploadLog.getSnsPubId() != null) {
            // 判断是否是隐私全文、全文是否删除，成果是否已删除,是否是群组成果
            PubSnsPO snsPub = pubfulltextRcmdService.getSnsPubById(uploadLog.getSnsPubId());
            if (uploadLog.getIsPrivacy() != 2 && uploadLog.getIsDelete() == 0 && snsPub != null) {
              // 1：如果自己上传全文前有给这条成果推荐全文，需要删除推荐给这条成果的记录
              PubFulltextPsnRcmd rcmd = pubfulltextRcmdService.getFulltextRcmd(uploadLog.getSnsPubId());
              if (rcmd != null) {
                pubfulltextRcmdService.deleteFulltextRcmd(uploadLog.getSnsPubId());
              }
              // 根据个人成果与基准库成果关联关系找到该条成果关联的基准库成果
              pdwhPubId = pubfulltextRcmdService.getPdwhPubId(uploadLog.getSnsPubId());
              if (pdwhPubId != null) {
                // 根据基准库找到其他关联到的个人成果
                List<Long> snsPubIds = pubfulltextRcmdService.getSnsPubId(pdwhPubId, uploadLog.getSnsPubId());
                if (CollectionUtils.isNotEmpty(snsPubIds)) {
                  pubfulltextRcmdService.savePubFulltextPsnRcmd(snsPubIds, uploadLog.getSnsPubId(), 0, uploadLog);
                }
              }
            } else {
              // 隐私文件或者文件已删除 需要删除所有的推荐记录
              pubfulltextRcmdService.deleteFulltextPsnRcmd(uploadLog.getSnsPubId());
            }
          } else if (uploadLog.getPdwhPubId() != null) {
            // 根据基准库找到其他关联到的个人成果
            List<Long> snsPubIds = pubfulltextRcmdService.getSnsPubId(uploadLog.getPdwhPubId(), 0L);
            if (CollectionUtils.isNotEmpty(snsPubIds)) {
              pubfulltextRcmdService.savePubFulltextPsnRcmd(snsPubIds, uploadLog.getPdwhPubId(), 1, uploadLog);
            }
          }
          pubfulltextRcmdService.updateLogStatus(uploadLog.getId(), 1);
        } catch (Exception e) {
          logger.error("全文推荐任务处理出错", e);
          pubfulltextRcmdService.updateLogStatus(uploadLog.getId(), 9);
        }
      }

    }

  }
}
