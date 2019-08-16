package com.smate.center.batch.quartz;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.batch.model.pdwh.pub.PubPdwhPO;
import com.smate.center.batch.model.sns.pub.GroupFundInfo;
import com.smate.center.batch.model.sns.pub.GrpPubs;
import com.smate.center.batch.model.sns.pub.PubDuplicatePO;
import com.smate.center.batch.model.tmp.pdwh.PubFundingInfo;
import com.smate.center.batch.service.TaskMarkerService;
import com.smate.center.batch.service.pdwh.pubimport.PdwhPublicationService;
import com.smate.center.batch.service.pub.GroupService;
import com.smate.center.batch.service.pub.GrpPubsService;
import com.smate.center.batch.service.pub.PubDupService;
import com.smate.center.batch.service.pub.PubSimpleService;
import com.smate.center.batch.service.pubfulltexttoimage.ScmPubXmlService;
import com.smate.core.base.utils.cache.CacheService;
import com.smate.core.base.utils.exception.BatchTaskException;
import com.smate.core.base.utils.pubHash.PubHashUtils;

public class PubToPubsimpleTask14 {
  private final Logger logger = LoggerFactory.getLogger(getClass());

  private final static Integer SIZE = 1000; // 每次刷新获取的个数

  private Long startGroupId = 100000001311679L;

  private Long endGroupId = 110000000087300L;

  @Autowired
  private ScmPubXmlService scmPubXmlService;

  @Autowired
  private TaskMarkerService taskMarkerService;

  @Autowired
  private GroupService groupService;

  @Autowired
  private CacheService cacheService;
  @Autowired
  private GrpPubsService grpPubsService;
  @Autowired
  private PdwhPublicationService pdwhPublicationService;
  @Autowired
  private PubSimpleService pubSimpleService;
  @Autowired
  private PubDupService pubDupService;

  public void run() throws BatchTaskException {
    logger.debug("====================================pubToPubsimpleTask__14===开始运行");
    if (isRun() == false) {
      logger.debug("====================================pubToPubsimpleTask__14===开关关闭");
      return;
    } else {
      try {
        doRun();
      } catch (BatchTaskException e) {
        logger.error("pubToPubsimpleTask__14,运行异常", e);
        throw new BatchTaskException(e);
      }
    }
  }

  public void doRun() throws BatchTaskException {

    try {

      logger.debug("===========================================BatchPubToPubSimpleTask14=========开始1");

      List<GroupFundInfo> groupList = this.groupService.getGroupFundInfo(SIZE, startGroupId, endGroupId);

      if (CollectionUtils.isEmpty(groupList)) {
        logger.info(
            "===========================================BatchPubToPubSimpleTask14  没有获取到pubSimple表数据!!!!============, time = "
                + new Date());
        return;
      }

      for (GroupFundInfo groupFundInfo : groupList) {
        try {
          Long groupId = groupFundInfo.getGroupId();
          Long psnId = groupFundInfo.getPsnId();
          String groupFund = StringUtils.trimToEmpty(groupFundInfo.getFundInfo());
          if (StringUtils.isNotEmpty(groupFund)) {
            groupFund = groupFund.toLowerCase();
          } else {
            continue;
          }

          List<PubFundingInfo> pubFundingInfoList = this.groupService.getPubFundingInfoByFundingNo(groupFund);

          for (PubFundingInfo pubFundingInfo : pubFundingInfoList) {
            Boolean isSave = true;
            Long pubId = pubFundingInfo.getPubId();
            Integer dbId = pubFundingInfo.getDbId();
            List<GrpPubs> GrpPubsList = new ArrayList<GrpPubs>();
            GrpPubsList = grpPubsService.getGrpPubsList(groupId);
            PubPdwhPO pdwhPub = pdwhPublicationService.getNewPdwhPubById(pubId);
            if (pdwhPub == null) {
              continue;
            }
            String pdwhTitleHashCode =
                PubHashUtils.getTitleInfoHash(pdwhPub.getTitle(), pdwhPub.getPubType(), pdwhPub.getPublishYear());
            for (GrpPubs grpPubs : GrpPubsList) {
              PubDuplicatePO pubDuplicatePO = pubDupService.getPubDuplicatePO(grpPubs.getPubId());
              if (pdwhTitleHashCode.equals(pubDuplicatePO.getHashTP())
                  || pdwhTitleHashCode.equals(pubDuplicatePO.getHashTPP())) {// 采用新的计算方式
                isSave = false;
                break;
              }
            }
            if (isSave) {
              this.groupService.insertIntoRcmdPdwh(groupId, pubId, pubFundingInfo.getPublishYear());
            }
          }

          this.groupService.saveOpResult(groupFundInfo, 1);
        } catch (Exception e) {

          this.groupService.saveOpResult(groupFundInfo, 9);
          logger.error("BatchPubToPubSimpleTask14出错============== groupId = " + groupFundInfo.getGroupId(), e);
        }
      }

    } catch (Exception e) {
      logger.error("pubToPubsimpleTask14,运行异常", e);
    }

  }

  public boolean isRun() throws BatchTaskException {
    // 任务开关控制逻辑
    // return true;
    return taskMarkerService.getApplicationQuartzSettingValue("PubToPubsimpleTask14") == 1;
  }
}
