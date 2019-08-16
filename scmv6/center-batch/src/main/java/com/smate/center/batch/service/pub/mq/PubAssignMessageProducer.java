package com.smate.center.batch.service.pub.mq;

import com.smate.center.batch.connector.dao.exception.CreateBatchJobException;
import com.smate.center.batch.connector.enums.BatchOpenCodeEnum;
import com.smate.center.batch.connector.enums.BatchWeightEnum;
import com.smate.center.batch.connector.factory.BatchJobsFactory;
import com.smate.center.batch.connector.model.job.BatchJobs;
import com.smate.center.batch.connector.service.job.BatchJobsService;
import com.smate.center.batch.connector.util.BatchJobUtil;
import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.core.base.utils.constant.ServiceConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.annotation.Resource;

/**
 * 指派队列消息.
 * 
 * @author yamingd
 * 
 */
@Component
public class PubAssignMessageProducer extends AbstractLocalQueneMessageProducer {

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  private final String cBeanName = "pubAssignMessageConsumer";

  @Resource(name = "batchJobsContextFactory")
  private BatchJobsFactory batchJobsFactory;

  @Autowired
  private BatchJobsService batchJobsService;

  /**
   * 按成果导入单位、导入批次(java)号指派人。(后台导入).
   * 
   * @param insId
   * @param batchNo
   * @param opPsnId
   */
  public void assignByIns(final Long insId, final Long batchNo, final Long opPsnId) throws Exception {

    Assert.notNull(insId, "assignByIns参数insId不能为空.");

    PubAssignMessage message = new PubAssignMessage(null, insId, null, PubAssignMessageModeEnum.byIns,
        PubAssignMessageKindEnum.AddNewPub, opPsnId, batchNo);

    this.sendMessage(message);
  }

  /**
   * 按新增成果指派人（在线导入）.
   * 
   * @param insId
   * @param opPsnId
   * @param pubId
   */
  public void assignByPub(final Long insId, final Long opPsnId, final Long pubId, Integer importType) throws Exception {

    Assert.notNull(pubId, "assignByPub参数pubId不能为空.");

    PubAssignMessage message = new PubAssignMessage(pubId, insId, null, PubAssignMessageModeEnum.byPub,
        PubAssignMessageKindEnum.AddNewPub, opPsnId, null);
    if (importType == null) {
      importType = 0;
    }
    message.setImportType(importType);
    // 后台导入，等级设置成最低
    if (importType == 1) {
      message.setPriority(1);
    }
    this.sendMessage(message);
    // 在v_batch_jobs中插入记录
    Long msgId = message.getMsgId();
    this.saveToBatchJobs(msgId);

  }

  /**
   * 按人指派单位库成果（新增人）.
   * 
   * @param insId
   * @param opPsnId
   * @param psnId
   * @param kind
   */
  public void assignByPsn(final Long insId, final Long opPsnId, final Long psnId, int kind) throws Exception {

    boolean valid = kind == PubAssignMessageKindEnum.AddNewPsn || kind == PubAssignMessageKindEnum.ApproveNewPsn
        || kind == PubAssignMessageKindEnum.DeletePsn || kind == PubAssignMessageKindEnum.UpdatePsn
        || kind == PubAssignMessageKindEnum.CONFIRM_PUB;

    Assert.isTrue(valid, "assignByPsn参数kind的值不正确：kind=" + kind);

    // 基金委单位，直接忽略
    for (Long nsfcInsId : ServiceConstants.NSFC_INS_IDS) {
      if (nsfcInsId.equals(insId)) {
        return;
      }
    }
    PubAssignMessage message =
        new PubAssignMessage(null, insId, psnId, PubAssignMessageModeEnum.byPsn, kind, opPsnId, null);
    // 人员确定成果信息，重新匹配成果优先级设置成最低.
    if (kind == PubAssignMessageKindEnum.CONFIRM_PUB) {
      message.setPriority(1);
    }

    this.sendMessage(message);
  }

  @Override
  public String getCbeanName() {
    return cBeanName;
  }

  private void saveToBatchJobs(Long msgId) throws ServiceException {
    try {

      BatchJobs job = batchJobsFactory.createBatchJob(BatchOpenCodeEnum.PUB_ASSIGN_FOR_ROL1,
          BatchJobUtil.getContext(msgId + ""), BatchWeightEnum.D.toString());
      batchJobsService.saveJob(job);
    } catch (CreateBatchJobException e) {
      throw new ServiceException(e);
    }

  }

}
