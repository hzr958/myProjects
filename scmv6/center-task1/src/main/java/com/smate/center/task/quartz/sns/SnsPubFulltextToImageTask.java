package com.smate.center.task.quartz.sns;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.task.base.TaskAbstract;
import com.smate.center.task.exception.SingleTaskException;
import com.smate.center.task.service.pdwh.quartz.FulltextPdfToImageService;
import com.smate.center.task.v8pub.sns.po.PubFullTextPO;

public class SnsPubFulltextToImageTask extends TaskAbstract {
  private final Logger logger = LoggerFactory.getLogger(getClass());
  private final static Integer SIZE = 200; // 每次处理的个数
  @Autowired
  private FulltextPdfToImageService fulltextPdfToImageService;

  public SnsPubFulltextToImageTask() {
    super();
  }

  public SnsPubFulltextToImageTask(String beanName) {
    super(beanName);
  }

  public void doRun() throws SingleTaskException {
    if (!super.isAllowExecution()) {
      return;
    }
    // 取基准库全文
    List<Long> snsPubIds = fulltextPdfToImageService.getSnsToImageData(SIZE);
    if (snsPubIds != null && snsPubIds.size() > 0) {
      for (Long snsPubId : snsPubIds) {
        try {
          // 1.先删除可能存在的旧文件图片
          PubFullTextPO pubFulltext = fulltextPdfToImageService.getSnsPubFulltext(snsPubId);
          if (StringUtils.isNotBlank(pubFulltext.getThumbnailPath())) {
            fulltextPdfToImageService.deleteFileByPath(pubFulltext.getThumbnailPath());
          }
          // 基准库全文转图片
          fulltextPdfToImageService.ConvertSnsPubFulltextPdfToimage(pubFulltext);
        } catch (Exception e) {
          fulltextPdfToImageService.updateSnsToImageStatus(snsPubId, 2, "V_PUB_FULLTEXT没有全文，不需要处理");
          logger.error("个人库pdf转图片离线任务处理时出现异常！该记录id={}", snsPubId);
        }
      }

    }
  }

}
