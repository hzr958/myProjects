package com.smate.center.batch.tasklet.pubfulltexttoimage;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.batch.model.pdwh.pub.PdwhPubFulltextImageRefresh;
import com.smate.center.batch.model.sns.pub.PubFulltextImageRefresh;
import com.smate.center.batch.service.pubfulltexttoimage.PubFullTextService;
import com.smate.center.batch.tasklet.base.BaseTasklet;
import com.smate.center.batch.tasklet.base.DataVerificationStatus;
import com.smate.core.base.utils.exception.BatchTaskException;

@Deprecated
/**
 * 
 * 该任务已经停止使用，图片处理已经迁移到ProduceThumbnailImageTasklet 和center-task
 * 
 * @author LIJUN
 * @date 2018年4月11日
 */
public class PubFullTextToImage extends BaseTasklet {

  @Autowired
  PubFullTextService pubFullTextService;

  @Override
  public DataVerificationStatus dataVerification(String withData) throws BatchTaskException {
    return DataVerificationStatus.TRUE;
  }

  /*
   * @Override public void taskExecution(Map jobContentMap) throws BatchTaskException { String
   * withData = String.valueOf(jobContentMap.get("msg_id")); Long pubId = Long.parseLong(withData);
   * logger.
   * debug("----------------------------------PubFullTextToImage----------------------------pubId = "
   * +pubId); }
   */

  @Override
  public void taskExecution(Map jobContentMap) throws BatchTaskException {
    String withData = String.valueOf(jobContentMap.get("msg_id"));
    String fromtype = String.valueOf(jobContentMap.get("from_type"));
    Long pubId = Long.parseLong(withData);
    // 生成pdwh全文图片（已经迁移到center-task处理pdwh全文图片生成任务 SCM-14948）
    if ("pdwh".equals(fromtype)) {
      PdwhPubFulltextImageRefresh pdwhImage = pubFullTextService.getNeedRefreshPdwhDataById(pubId);
      if (pdwhImage != null) {
        try {
          pubFullTextService.convertPdftoImg(pdwhImage);
          pubFullTextService.delPdwhPubFulltextImageRefresh(pdwhImage.getPubId());
        } catch (Exception e) {
          logger.error("pdwh成果全文转换图片刷新任务出现异常：", e);
          pdwhImage.setStatus(99);
          pdwhImage.setErrorMsg(this.getErrorMsg(e));
          pubFullTextService.savePdwhPubFulltextImageRefresh(pdwhImage);
          throw new BatchTaskException(e);
        }
      } else {
        throw new BatchTaskException("PDWH_PUB_FULLTEXT_IMG_REFRESH表中没有查询到pubId为 " + pubId + " 的记录！");
      }
    } else {
      // 老系统还有存在部分方法调用，需要修改，暂时先不执行

      // 刷新sns的全文图片
      PubFulltextImageRefresh one = pubFullTextService.getNeedRefreshDataById(pubId);

      if (one == null) {
        // 可能是其他文件，其他文件类型不需要在全文图片刷新表中获取，直接发送站内信与邮件;如果也不是其他邮件，则直接抛出错误
        if (pubFullTextService.isOtherTypeFt(pubId)) {
          pubFullTextService.sendFtRequestReplyMail(pubId);
          return;
        }

        throw new BatchTaskException("PUB_FULLTEXT表中没有查询到pubId为 " + pubId + " 的记录！");
      }

      try {
        pubFullTextService.refreshData(one);
        pubFullTextService.delPubFulltextImageRefresh(one.getPubId());
        pubFullTextService.sendFtRequestReplyMail(one.getPubId());

      } // catch(FileNotFoundException ex){
        // 处理FileNotFoundException，不能再refreshData里边进行，抛出的错误会导致回滚，删除相关事务无法提交报错
        // Transaction rolled back because it has been marked as
        // rollback-only
        // 暂时不作处理，只是删除相关数据。任务结果仍然标记成功
        // pubFullTextService.deletePubFulltextByPubId(pubId);
        // pubFullTextService.delPubFulltextImageRefresh(pubId);
        // }
      catch (Exception e) {
        logger.error("sns成果全文转换图片刷新任务出现异常：", e);
        one.setStatus(99);
        one.setErrorMsg(this.getErrorMsg(e));
        pubFullTextService.savePubFulltextImageRefresh(one);
        throw new BatchTaskException(e);
      }
    }

  }

  private String getErrorMsg(Exception e) {
    Writer result = new StringWriter();
    PrintWriter printWriter = new PrintWriter(result);
    e.printStackTrace(printWriter);
    String ErrorMsg = result.toString().length() > 600 ? result.toString().substring(600) : result.toString();
    return ErrorMsg;
  }

}
