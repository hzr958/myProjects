package com.smate.sie.center.task.quartz;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.task.base.TaskAbstract;
import com.smate.sie.center.task.model.SiePatFullTextRefresh;
import com.smate.sie.center.task.service.SiePatFulltextToImageService;

/**
 * 定时任务：生成SIE专利的全文的缩略图
 * 
 * @author 叶星源
 */
public class SiePatFulltextToImageTask extends TaskAbstract {

  private Logger logger = LoggerFactory.getLogger(getClass());

  private static final int BATCH_SIZE = 100;// 一次最多更新数量

  @Autowired
  private SiePatFulltextToImageService siePatFulltextToImageService;

  public SiePatFulltextToImageTask() {
    super();
  }

  public SiePatFulltextToImageTask(String beanName) {
    super(beanName);
  }

  public void doRun() throws Exception {
    if (!super.isAllowExecution()) {
      return;
    }
    try {
      while (true) {
        // 获取需要更新的数据
        List<SiePatFullTextRefresh> list = this.siePatFulltextToImageService.getNeedRefreshData(BATCH_SIZE);
        if (list == null || list.size() == 0) {
          return;
        }
        for (SiePatFullTextRefresh siePatFullTextRefresh : list) {
          try {
            siePatFulltextToImageService.refreshData(siePatFullTextRefresh);
          } catch (Exception e) {
            logger.error("成果全文转换图片刷新任务出现异常：", e);
            siePatFulltextToImageService.convertFail(siePatFullTextRefresh);
          }
        }
      }
    } catch (Exception e) {
      logger.error("成果全文转换图片刷新任务出现异常：", e);
    }

  }
}
