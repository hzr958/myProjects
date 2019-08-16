package com.smate.center.batch.service.pub.pubsubmission;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.dao.sns.pub.PubSubmissionDao;
import com.smate.center.batch.exception.pub.DaoException;
import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.sns.pub.PublicationSubmmission;
import com.smate.center.batch.service.pub.mq.PubSubmissionInsSyncMessage;
import com.smate.center.batch.service.pub.mq.SnsPubTotalSyncProducer;

/**
 * 成果提交..
 * 
 * @author LY
 * 
 */
@Service("pubSubmissionService")
@Transactional(rollbackFor = Exception.class)
public class PubSubmissionServiceImpl implements PubSubmissionService {
  private static Logger logger = LoggerFactory.getLogger(PubSubmissionServiceImpl.class);
  @Autowired
  private PubSubmissionDao pubSubmissionDao;
  @Autowired
  private SnsPubTotalSyncProducer snsPubTotalSyncProducer;

  @Override
  public Long getSubmitTotal(Long insId, Long psnId) throws ServiceException {
    try {
      return this.pubSubmissionDao.getSubmitTotal(insId, psnId);
    } catch (Exception e) {
      logger.error("获取已提交成果数错误", e);
      throw new ServiceException(e);
    }
  }


  @Override
  public void receiverPubSubmissionSyncMessage(PubSubmissionInsSyncMessage message) throws ServiceException {
    // 单位：批准成果1，拒绝成果2，拒绝申请撤销3，同意申请撤销4
    Integer status = null;
    if (message.getStatus().intValue() == 1) {
      status = 2;
    } else if (message.getStatus().intValue() == 2) {
      status = 3;
    } else if (message.getStatus().intValue() == 4) {
      status = 0;
      PublicationSubmmission submission = null;
      try {
        submission = this.pubSubmissionDao.getByUqId(message.getInsId(), message.getPubId());
      } catch (DaoException e) {
        logger.error("查找提交的成果失败" + e);
      }
      if (submission != null) {
        this.pubSubmissionDao.delete(submission);
      }
    }
    if (status != null) {
      this.pubSubmissionDao.updatePubSubmissionState(String.valueOf(message.getPubId()), message.getInsId(),
          message.getPsnId(), status);
      // 状态不是批准状态或等待审核状态，同步提交个数到单位
      if (status != 2 && status != 1) {
        snsPubTotalSyncProducer.sendSnsPubTotal(message.getInsId(), message.getPsnId());
      }
    }
  }
}
