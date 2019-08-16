package com.smate.center.batch.tasklet.pub.img;

import java.util.Map;

import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.batch.model.pdwh.pub.PdwhPubFullTextPO;
import com.smate.center.batch.service.pub.FulltextPdfToImageService;
import com.smate.center.batch.tasklet.base.BaseTasklet;
import com.smate.center.batch.tasklet.base.DataVerificationStatus;
import com.smate.core.base.utils.exception.BatchTaskException;
import com.smate.core.base.utils.string.StringUtils;

public class PdwhPubFulltextToImageTasklet extends BaseTasklet {
  @Autowired
  private FulltextPdfToImageService fulltextPdfToImageService;

  @Override
  public DataVerificationStatus dataVerification(String msgId) throws BatchTaskException {
    if (StringUtils.isBlank(msgId) || NumberUtils.toLong(msgId) <= 0) {
      return DataVerificationStatus.FALSE;
    }
    return DataVerificationStatus.TRUE;
  }

  @Override
  public void taskExecution(Map jobContentMap) throws BatchTaskException {
    Long pdwhPubId = NumberUtils.toLong(jobContentMap.get("msg_id").toString());
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
