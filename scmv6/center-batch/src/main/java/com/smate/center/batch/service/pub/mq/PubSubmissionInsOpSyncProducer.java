package com.smate.center.batch.service.pub.mq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.core.base.utils.security.SecurityUtils;

/**
 * 成果提交 申请单位把指定成果退回.
 * 
 * @author LY
 * 
 */
@Component("pubSubmissionInsOpSyncProducer")
public class PubSubmissionInsOpSyncProducer {

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  private String queueName = "pubSubmissionInsOpSync";

  /**
   * 单位批准成果提交.
   * 
   * @throws ServiceException
   */
  public void aproveSubmitPub(Long psnId, Long insId, Long snsPubId) throws ServiceException {
    try {
      // 批准成果1，拒绝成果2，拒绝申请撤销3，同意申请撤销4
      PubSubmissionInsSyncMessage msg = new PubSubmissionInsSyncMessage(psnId, insId, snsPubId, 1);

    } catch (Exception e) {
      logger.error("单位批准成果提交错误.", e);
      throw new ServiceException(e);
    }
  }

  /**
   * 单位拒绝成果提交.
   * 
   * @throws ServiceException
   */
  public void rejectSubmitPub(Long psnId, Long insId, Long snsPubId) throws ServiceException {
    try {

      // 批准成果1，拒绝成果2，拒绝申请撤销3，同意申请撤销4
      PubSubmissionInsSyncMessage msg = new PubSubmissionInsSyncMessage(psnId, insId, snsPubId, 2);
    } catch (Exception e) {
      logger.error("单位拒绝成果提交错误.", e);
      throw new ServiceException(e);
    }
  }

  /**
   * 单位批准申请撤销成果.
   * 
   * @throws ServiceException
   */
  public void aproveWithdraw(Long psnId, Long insId, Long snsPubId) throws ServiceException {
    try {

      // 批准成果1，拒绝成果2，拒绝申请撤销3，同意申请撤销4
      PubSubmissionInsSyncMessage msg = new PubSubmissionInsSyncMessage(psnId, insId, snsPubId, 4);

    } catch (Exception e) {
      logger.error(" 单位批准申请撤销成果错误.", e);
      throw new ServiceException(e);
    }
  }

  /**
   * 单位拒绝申请撤销成果.
   * 
   * @throws ServiceException
   */
  public void rejectWithdraw(Long psnId, Long insId, Long snsPubId) throws ServiceException {
    try {
      // 批准成果1，拒绝成果2，拒绝申请撤销3，同意申请撤销4
      PubSubmissionInsSyncMessage msg = new PubSubmissionInsSyncMessage(psnId, insId, snsPubId, 3);

    } catch (Exception e) {
      logger.error(" 单位拒绝申请撤销成果错误.", e);
      throw new ServiceException(e);
    }
  }

}
