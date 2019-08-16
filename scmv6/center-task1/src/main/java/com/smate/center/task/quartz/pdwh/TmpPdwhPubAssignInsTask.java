package com.smate.center.task.quartz.pdwh;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.task.base.TaskAbstract;
import com.smate.center.task.exception.SingleTaskException;
import com.smate.center.task.model.sns.pub.PubAssginMatchContext;
import com.smate.center.task.service.pdwh.quartz.PdwhPublicationService;
import com.smate.center.task.service.sns.pubAssign.handler.PdwhPubAssignMatchService;
import com.smate.web.v8pub.dom.pdwh.PubPdwhDetailDOM;

public class TmpPdwhPubAssignInsTask extends TaskAbstract {
  private final Logger logger = LoggerFactory.getLogger(getClass());
  private final static Integer SIZE = 500; // 每次处理的个数
  private Long startHandleId;
  private Long endHandleId;

  @Autowired
  private PdwhPublicationService pdwhPublicationService;
  @Resource(name = "pdwhPubAssignMatchEmailHandler")
  private PdwhPubAssignMatchService pdwhPubAssignMatchService;

  public TmpPdwhPubAssignInsTask() {
    super();
  }

  public TmpPdwhPubAssignInsTask(String beanName) {
    super(beanName);
  }

  public void doRun() throws SingleTaskException {
    if (!super.isAllowExecution()) {
      logger.info("=========PdwhPubAssignTask已关闭==========");
      return;
    }
    try {
      List<Long> pdwhPubIds = pdwhPublicationService.getTmpBatchhandleIdList(startHandleId, endHandleId);
      if (CollectionUtils.isEmpty(pdwhPubIds)) {
        logger.info("=========成果数据已指派完==========");
        return;
      }
      for (Long pdwhPubId : pdwhPubIds) {
        PubAssginMatchContext context = new PubAssginMatchContext();
        try {
          PubPdwhDetailDOM detailDom = pdwhPublicationService.getNeedAssignPub(pdwhPubId);
          if (detailDom == null) {
            continue;
          }

          if (StringUtils.isNotBlank(detailDom.getPublishDate())
              && detailDom.getPublishDate().matches("\\s*\\d{4}.*")) {
            context.setPubyear(
                Long.valueOf(StringUtils.substring(StringUtils.trimToEmpty(detailDom.getPublishDate()), 0, 4)));
          } else {
            context.setPubyear(null);
          }
          List<String> emailList = pdwhPublicationService.getPubMemberEmailList(pdwhPubId);
          if (CollectionUtils.isNotEmpty(emailList)) {
            context.setEmailList(emailList);
          }
          context.setKeywords(detailDom.getKeywords());
          context.setPdwhPubId(pdwhPubId);
          pdwhPubAssignMatchService.handler(context, null);
        } catch (Exception e) {
          pdwhPublicationService.updateTaskStatus(pdwhPubId, 9, e.getMessage());
          logger.error("=========PdwhPubAssignTask从xml取关键词，Email出错，pubId:" + pdwhPubId, e);
        }
        pdwhPublicationService.updateTaskStatus(pdwhPubId, 1, "处理成功");
      }
    } catch (Exception e) {
      logger.error("=========PdwhPubAssignTask出错===========", e);
    }
  }

  public Long getStartHandleId() {
    return startHandleId;
  }

  public void setStartHandleId(Long startHandleId) {
    this.startHandleId = startHandleId;
  }

  public Long getEndHandleId() {
    return endHandleId;
  }

  public void setEndHandleId(Long endHandleId) {
    this.endHandleId = endHandleId;
  }
}
