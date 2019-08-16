package com.smate.sie.center.task.quartz;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.task.base.TaskAbstract;
import com.smate.sie.center.task.model.PubFullTextRefresh;
import com.smate.sie.center.task.service.SiePubFulltextToImageService;

/**
 * 定时任务：生成SIE成果的全文的缩略图
 * 
 * @author 叶星源
 */
public class SiePubFulltextToImageTask extends TaskAbstract {

  private Logger logger = LoggerFactory.getLogger(getClass());

  private static final int BATCH_SIZE = 100;// 一次最多更新数量

  @Autowired
  private SiePubFulltextToImageService siePubFulltextToImageService;

  public SiePubFulltextToImageTask() {
    super();
  }

  public SiePubFulltextToImageTask(String beanName) {
    super(beanName);
  }

  public void doRun() throws Exception {
    if (!super.isAllowExecution()) {
      return;
    }
    try {
      while (true) {
        // 获取需要更新的数据
        List<PubFullTextRefresh> list = this.siePubFulltextToImageService.getNeedRefreshData(BATCH_SIZE);
        if (list == null || list.size() == 0) {
          return;
        }
        for (PubFullTextRefresh pubFullTextRefresh : list) {
          try {
            siePubFulltextToImageService.refreshData(pubFullTextRefresh);
            siePubFulltextToImageService.delPubFulltextImageRefresh(pubFullTextRefresh.getPubId());
          } catch (Exception e) {
            logger.error("成果全文转换图片刷新任务出现异常：", e);
            pubFullTextRefresh.setStatus(99);
            siePubFulltextToImageService.savePubFulltextImageRefresh(pubFullTextRefresh);
          }
        }
      }
    } catch (Exception e) {
      logger.error("成果全文转换图片刷新任务出现异常：", e);
    }

  }
}
