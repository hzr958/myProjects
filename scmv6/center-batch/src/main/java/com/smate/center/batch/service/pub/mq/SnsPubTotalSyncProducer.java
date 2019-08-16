package com.smate.center.batch.service.pub.mq;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.service.psn.PsnInsService;
import com.smate.center.batch.service.pub.PublicationService;
import com.smate.center.batch.service.pub.pubsubmission.PubSubmissionService;
import com.smate.center.batch.service.rol.pub.InsPortalManager;

/**
 * 个人成果数量同步到单位.
 * 
 * @author liqinghua
 * 
 */
@Component("snsPubTotalSyncProducer")
public class SnsPubTotalSyncProducer {

  /**
   * 
   */
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private InsPortalManager insPortalManager;
  @Autowired
  private PsnInsService psnInsService;
  @Autowired
  private PublicationService publicationService;
  @Autowired
  private PubSubmissionService pubSubmissionService;
  @Autowired
  private SnsPubTotalSyncConsumer snsPubTotalSyncConsumer;

  /**
   * 发送个人成果数量同步到单位.
   * 
   * @param insId
   * @param psnId
   * @param total
   * @throws ServiceException
   */
  public void sendSnsPubTotal(Long psnId) throws ServiceException {
    Assert.notNull(psnId, "psnId不能为空");

    try {
      List<Long> insIds = psnInsService.findAllowSubmitInsIdsByPsnId(psnId);
      if (insIds != null && insIds.size() > 0) {
        Long total = this.publicationService.getTotalPubsByPsnId(psnId);
        for (Long insId : insIds) {
          Long submitTotal = this.pubSubmissionService.getSubmitTotal(insId, psnId);
          this.sendSnsPubTotal(insId, psnId, total, submitTotal);
        }
      }
    } catch (Exception e) {
      logger.error("发送个人成果数量同步到单位", e);
      throw new ServiceException(e);
    }
  }

  /**
   * 发送个人成果数量同步到单位.
   * 
   * @param insId
   * @param psnId
   * @param total
   * @throws ServiceException
   */
  public void sendSnsPubTotal(Long insId, Long psnId) throws ServiceException {
    Assert.notNull(insId, "insId不能为空");
    Assert.notNull(psnId, "psnId不能为空");
    try {
      Long total = this.publicationService.getTotalPubsByPsnId(psnId);
      Long submitTotal = this.pubSubmissionService.getSubmitTotal(insId, psnId);
      this.sendSnsPubTotal(insId, psnId, total, submitTotal);
      logger.debug("发送个人成果数量同步到单位insId:{},psnId:{},total:{}", new Object[] {insId, psnId, total});
    } catch (Exception e) {
      logger.error("发送个人成果数量同步到单位", e);
      throw new ServiceException(e);
    }
  }

  /**
   * 发送个人成果数量同步到单位.
   * 
   * @param insId
   * @param psnId
   * @param total
   * @throws ServiceException
   */
  public void sendSnsPubTotal(Long insId, Long psnId, Long total, Long submitTotal) throws ServiceException {
    /*
     * SCM-17208,和强哥确认过，同步sie功能注释 Assert.notNull(insId, "insId不能为空"); Assert.notNull(psnId,
     * "psnId不能为空"); Assert.notNull(total, "total不能为空"); try { Integer fromNodeId =
     * ServiceConstants.SCHOLAR_NODE_ID_1; Integer toNodeId = insPortalManager.getInsNodeId(insId); if
     * (toNodeId != null) { //FIXME 2015-10-29 改造 -done SnsPubTotalSyncMessage msg = new
     * SnsPubTotalSyncMessage(psnId, insId, total, submitTotal, fromNodeId);
     * snsPubTotalSyncConsumer.receive(msg);; logger.debug("发送个人成果数量同步到单位insId:{},psnId:{},total:{}",
     * new Object[] { insId, psnId, total }); } } catch (Exception e) {
     * logger.error("发送个人成果数量同步到单位insId:{},psnId:{},total:{}", new Object[] { insId, psnId, total }, e);
     * throw new ServiceException(e); }
     */}

}
