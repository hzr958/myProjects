package com.smate.center.task.quartz.email;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.task.base.TaskAbstract;
import com.smate.center.task.exception.SingleTaskException;
import com.smate.center.task.model.grp.GrpBaseinfo;
import com.smate.center.task.service.group.SendGrpPubRcmdMailService;
import com.smate.center.task.service.sns.quartz.TaskMarkerService;
import com.smate.core.base.utils.cache.CacheService;

public class SendGroupPubRcmdMailTask extends TaskAbstract {
  private final Logger logger = LoggerFactory.getLogger(getClass());
  private final static Integer SIZE = 1000; // 每次刷新获取的个数
  @Autowired
  private SendGrpPubRcmdMailService SendGrpPubRcmdMailService;
  @Autowired
  private TaskMarkerService taskMarkerService;
  @Autowired
  private CacheService cacheService;
  public static String JT_GRP_PUB_RCMD_ID_CACHE = "jt_grp_pub_rcmd_id_cache";
  public static String JZ_GRP_PUB_RCMD_ID_CACHE = "jz_grp_pub_rcmd_id_cache";

  public SendGroupPubRcmdMailTask() {
    super();
  }

  public SendGroupPubRcmdMailTask(String beanName) {
    super(beanName);
  }

  public void doRun() throws SingleTaskException {

    if (!super.isAllowExecution()) {
      return;
    }
    // 是否移除pub_id缓存
    if (taskMarkerService.getApplicationQuartzSettingValue("SendGroupPubRcmdMailTask_removeJtGrpId") == 1) {
      cacheService.remove(JT_GRP_PUB_RCMD_ID_CACHE, "last_jtgrp_id");
    }
    // 是否移除pub_id缓存
    if (taskMarkerService.getApplicationQuartzSettingValue("SendGroupPubRcmdMailTask_removeJzGrpId") == 1) {
      cacheService.remove(JZ_GRP_PUB_RCMD_ID_CACHE, "last_jzgrp_id");
    }
    Long grpId = (Long) cacheService.get(JT_GRP_PUB_RCMD_ID_CACHE, "last_jtgrp_id");
    if (grpId == null) {
      grpId = 0L;
    }
    try {
      // 先取结题报告的群组
      List<GrpBaseinfo> jtGrpPubData = SendGrpPubRcmdMailService.getNeedSendMailJtData(grpId, SIZE);
      if (jtGrpPubData != null && jtGrpPubData.size() > 0) {
        for (GrpBaseinfo jtGrpBaseInfo : jtGrpPubData) {
          SendGrpPubRcmdMailService.sendMailToGroupOwner(jtGrpBaseInfo);
        }
        cacheService.put(JT_GRP_PUB_RCMD_ID_CACHE, 60 * 60 * 24, "last_jtgrp_id",
            jtGrpPubData.get(jtGrpPubData.size() - 1).getGrpId());
      } else {
        grpId = (Long) cacheService.get(JZ_GRP_PUB_RCMD_ID_CACHE, "last_jzgrp_id");
        if (grpId == null) {
          grpId = 0L;
        }
        List<GrpBaseinfo> jzGrpPubData = SendGrpPubRcmdMailService.getNeedSendMailJzData(grpId, SIZE);
        if (jzGrpPubData != null && jzGrpPubData.size() > 0) {
          for (GrpBaseinfo jzGrpBaseInfo : jzGrpPubData) {
            SendGrpPubRcmdMailService.sendMailToGroupOwner(jzGrpBaseInfo);
          }
          cacheService.put(JZ_GRP_PUB_RCMD_ID_CACHE, 60 * 60 * 24, "last_jzgrp_id",
              jzGrpPubData.get(jzGrpPubData.size() - 1).getGrpId());
        } else {
          this.closeOneTimeTask();
        }
      }
    } catch (Exception e) {
      logger.error("发送报告推荐成果邮件出错----", e);
    }
  }
}
