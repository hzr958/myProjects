package com.smate.center.batch.quartz;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.sns.nsfc.NsfcPubOtherInfoRefresh;
import com.smate.center.batch.service.nsfc.NsfcPubOtherInfoRefreshService;
import com.smate.core.base.utils.exception.BatchTaskException;

/**
 * 成果其他信息刷新任务.
 * 
 * @author xys
 *
 */
public class NsfcPubOtherInfoRefreshTask {
  private final Logger logger = LoggerFactory.getLogger(getClass());

  private final static Integer SIZE = 500; // 每次刷新获取的个数
  @Autowired
  private NsfcPubOtherInfoRefreshService nsfcPubOtherInfoRefreshService;

  public void run() throws BatchTaskException {
    if (isRun() == false) {
      return;
    }

    try {
      doRun();
    } catch (BatchTaskException e) {
      logger.error("NsfcPubOtherInfoRefreshTask-run(),运行异常", e);
      throw new BatchTaskException(e);
    }
  }

  public void doRun() throws BatchTaskException {
    try {
      List<NsfcPubOtherInfoRefresh> list =
          this.nsfcPubOtherInfoRefreshService.getNsfcPubOtherInfoNeedRefreshBatch(SIZE);
      if (!CollectionUtils.isEmpty(list)) {
        for (NsfcPubOtherInfoRefresh nsfcPubOtherInfoRefresh : list) {
          try {
            this.nsfcPubOtherInfoRefreshService.updateNsfcPubOtherInfo(nsfcPubOtherInfoRefresh);
            nsfcPubOtherInfoRefresh.setStatus(1);
            nsfcPubOtherInfoRefresh.setErrorInfo(null);
            this.nsfcPubOtherInfoRefreshService.saveNsfcPubOtherInfoRefresh(nsfcPubOtherInfoRefresh);
          } catch (Exception e) {
            nsfcPubOtherInfoRefresh.setStatus(99);
            nsfcPubOtherInfoRefresh
                .setErrorInfo(e.toString().length() > 3000 ? e.toString().substring(0, 3000) : e.toString());
            this.nsfcPubOtherInfoRefreshService.saveNsfcPubOtherInfoRefresh(nsfcPubOtherInfoRefresh);
            logger.error("NsfcPubOtherInfoRefreshTask刷新出错咯,pubId:{}", nsfcPubOtherInfoRefresh.getPubId(), e);
            throw new ServiceException("NsfcPubOtherInfoRefreshTask刷新出错咯,pubId=" + nsfcPubOtherInfoRefresh.getPubId(),
                e);
          }
        }
      }
    } catch (Exception e) {
      logger.error("NsfcPubOtherInfoRefreshTask-doRun(),运行异常", e);
    }
  }

  public boolean isRun() throws BatchTaskException {
    // 任务开关控制逻辑
    return true;
  }
}
