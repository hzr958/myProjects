package com.smate.center.task.quartz;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.task.base.TaskAbstract;
import com.smate.center.task.exception.SingleTaskException;
import com.smate.center.task.single.service.pub.PublicationIndexService;


/**
 * 检索提示solr索引
 * 
 * @author lj
 *
 */
public class SuggestStrIndexTask extends TaskAbstract {
  private final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  PublicationIndexService publicationIndexService;

  public static String INDEX_TYPE_PUB = "publication_index";

  public SuggestStrIndexTask() {
    super();
  }

  public SuggestStrIndexTask(String beanName) {
    super(beanName);
  }

  public void doRun() throws SingleTaskException {

    if (!super.isAllowExecution()) {
      logger.info("=========SuggestStrIndexTask开关关闭==========");
      return;
    }

    logger.info("=========SuggestStrIndexTask开始运行==========");
    // 地名
    List<BigDecimal> InsSuggestId = this.publicationIndexService.getSuggestStrToHandleList(2);
    // 人员
    List<BigDecimal> UserSuggestId = this.publicationIndexService.getSuggestStrToHandleList(1);
    if (CollectionUtils.isEmpty(UserSuggestId) && CollectionUtils.isEmpty(InsSuggestId)) {
      logger.info("=========SuggestStrIndexTask执行完毕==========");
      return;
    }
    if (CollectionUtils.isNotEmpty(UserSuggestId)) {
      for (BigDecimal psnIdDe : UserSuggestId) {
        Long psnId = psnIdDe.longValue();
        try {
          this.publicationIndexService.indexSuggestInfo(psnId, 1);
          this.publicationIndexService.updateSuggestStrStatus(psnId, 1, 1);
        } catch (Exception e) {
          logger.error("SuggestStrIndexTask,运行异常", e);
          this.publicationIndexService.updateSuggestStrStatus(psnId, 1, 3);
        }
      }
    }

    if (CollectionUtils.isNotEmpty(InsSuggestId)) {
      for (BigDecimal insIdDe : InsSuggestId) {
        Long insId = insIdDe.longValue();
        try {
          this.publicationIndexService.indexSuggestInfo(insId, 2);
          this.publicationIndexService.updateSuggestStrStatus(insId, 2, 1);
        } catch (Exception e) {
          logger.error("SuggestStrIndexTask,运行异常", e);
          this.publicationIndexService.updateSuggestStrStatus(insId, 2, 3);
        }
      }
    }

  }

}
