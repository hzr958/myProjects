package com.smate.center.task.quartz;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.task.base.TaskAbstract;
import com.smate.center.task.exception.SingleTaskException;
import com.smate.center.task.service.pdwh.quartz.FulltextPdfToImageService;
import com.smate.center.task.v8pub.pdwh.po.PdwhPubFullTextPO;

public class PdwhPubFulltextToImageTask extends TaskAbstract {
  private final Logger logger = LoggerFactory.getLogger(getClass());
  private final static Integer SIZE = 200; // 每次处理的个数
  @Autowired
  private FulltextPdfToImageService fulltextPdfToImageService;

  public PdwhPubFulltextToImageTask() {
    super();
  }

  public PdwhPubFulltextToImageTask(String beanName) {
    super(beanName);
  }

  public void doRun() throws SingleTaskException {
    if (!super.isAllowExecution()) {
      return;
    }
    // 取基准库全文
    List<Long> pdwhPubIds = fulltextPdfToImageService.getPdwhToImageData(SIZE);
    for (Long pdwhPubId : pdwhPubIds) {
      try {
        // 1.先删除可能存在的旧文件图片
        PdwhPubFullTextPO pubFulltext = fulltextPdfToImageService.getPdwhPubFulltext(pdwhPubId);
        if (StringUtils.isNotBlank(pubFulltext.getThumbnailPath())) {
          fulltextPdfToImageService.deleteFileByPath(pubFulltext.getThumbnailPath());
        }
        // 基准库全文转图片
        fulltextPdfToImageService.ConvertPdwhPubFulltextPdfToimage(pubFulltext);
      } catch (Exception e) {
        fulltextPdfToImageService.updatePdwhToImageStatus(pdwhPubId, 2, "V_PDWH_FULLTEXT没有全文,不需要处理");
        logger.error("基准库pdf转图片离线任务处理时出现异常！该记录id={}", pdwhPubId);
      }
    }

  }

}
