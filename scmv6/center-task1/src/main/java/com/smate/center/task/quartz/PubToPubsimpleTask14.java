package com.smate.center.task.quartz;

import java.util.Date;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.task.base.TaskAbstract;
import com.smate.center.task.exception.SingleTaskException;
import com.smate.center.task.model.pdwh.quartz.PubFundingInfo;
import com.smate.center.task.model.sns.quartz.GroupFundInfo;
import com.smate.center.task.service.group.GrpService;
import com.smate.center.task.service.sns.quartz.TaskMarkerService;
import com.smate.center.task.single.service.pub.ScmPubXmlService;
import com.smate.core.base.utils.cache.CacheService;

@Deprecated
public class PubToPubsimpleTask14 extends TaskAbstract {
  private final Logger logger = LoggerFactory.getLogger(getClass());

  private final static Integer SIZE = 1000; // 每次刷新获取的个数

  /*
   * private Long startGroupId = 100000001291565L;
   * 
   * private Long endGroupId = 100000001425420L;
   */

  private Long startGroupId;

  private Long endGroupId;

  public Long getStartGroupId() {
    return startGroupId;
  }

  public void setStartGroupId(Long startGroupId) {
    this.startGroupId = startGroupId;
  }

  public Long getEndGroupId() {
    return endGroupId;
  }

  public void setEndGroupId(Long endGroupId) {
    this.endGroupId = endGroupId;
  }

  public PubToPubsimpleTask14() {
    super();
  }

  public PubToPubsimpleTask14(String beanName) {
    super(beanName);
  }

  @Autowired
  private ScmPubXmlService scmPubXmlService;

  @Autowired
  private TaskMarkerService taskMarkerService;

  @Autowired
  private GrpService groupService;

  @Autowired
  private CacheService cacheService;

  public void doRun() throws SingleTaskException {
    if (!super.isAllowExecution()) {
      logger.info("=========PubToPubSimpleTask14已关闭==========");
      return;
    }

    try {

      logger.debug("===========================================PubToPubSimpleTask14=========开始1");

      List<GroupFundInfo> groupList = this.groupService.getGroupFundInfo(SIZE, startGroupId, endGroupId);

      if (CollectionUtils.isEmpty(groupList)) {
        logger.info(
            "===========================================PubToPubSimpleTask14  没有获取到pubSimple表数据!!!!============, time = "
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
            Long pubId = pubFundingInfo.getPubId();
            Integer dbId = pubFundingInfo.getDbId();
            /* this.groupService.importPdwhPubIntoGroup(groupId, psnId, pubId, dbId); */
          }

          this.groupService.saveOpResult(groupFundInfo, 1);
        } catch (Exception e) {

          this.groupService.saveOpResult(groupFundInfo, 9);
          logger.error("PubToPubSimpleTask14出错============== groupId = " + groupFundInfo.getGroupId(), e);
        }
      }

    } catch (Exception e) {
      logger.error("pubToPubsimpleTask14,运行异常", e);
    }

  }

}
