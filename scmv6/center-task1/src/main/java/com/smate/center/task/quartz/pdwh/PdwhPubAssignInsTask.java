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
import com.smate.center.task.model.pdwh.pub.PdwhPubAuthorSnsPsnRecord;
import com.smate.center.task.model.sns.pub.PubAssginMatchContext;
import com.smate.center.task.service.pdwh.quartz.PdwhPublicationService;
import com.smate.center.task.service.sns.pubAssign.handler.PdwhPubAssignMatchService;
import com.smate.center.task.service.sns.pubAssign.handler.PdwhPubAssignService;
import com.smate.web.v8pub.dom.pdwh.PubPdwhDetailDOM;

public class PdwhPubAssignInsTask extends TaskAbstract {
  private final Logger logger = LoggerFactory.getLogger(getClass());
  private final static Integer SIZE = 1000; // 每次处理的个数

  @Autowired
  private PdwhPublicationService pdwhPublicationService;
  @Resource(name = "pdwhPubAssignMatchEmailHandler")
  private PdwhPubAssignMatchService pdwhPubAssignMatchService;
  @Autowired
  private PdwhPubAssignService pdwhPubAssignService;

  public PdwhPubAssignInsTask() {
    super();
  }

  public PdwhPubAssignInsTask(String beanName) {
    super(beanName);
  }

  public void doRun() throws SingleTaskException {
    if (!super.isAllowExecution()) {
      logger.info("=========PdwhPubAssignTask已关闭==========");
      return;
    }
    try {
      List<Long> pdwhPubIds = pdwhPublicationService.getbatchhandleIdList(SIZE);
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
          List<PdwhPubAuthorSnsPsnRecord> PsnRecordList = pdwhPubAssignService.getPsnRecordByPubId(pdwhPubId);
          if (CollectionUtils.isNotEmpty(PsnRecordList) && PsnRecordList.size() > 1000) {
            pdwhPublicationService.updateTaskStatus(pdwhPubId, 8, "记录数太多");
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
}
