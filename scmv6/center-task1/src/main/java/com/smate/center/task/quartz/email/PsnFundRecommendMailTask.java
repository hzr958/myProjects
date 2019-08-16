package com.smate.center.task.quartz.email;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.task.base.TaskAbstract;
import com.smate.center.task.exception.SingleTaskException;
import com.smate.center.task.model.fund.sns.PsnFundRecommend;
import com.smate.center.task.service.fund.PsnFundRcmdMailService;
import com.smate.core.base.utils.model.security.Person;

public class PsnFundRecommendMailTask extends TaskAbstract {
  private final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private PsnFundRcmdMailService psnFundRcmdMailService;

  public PsnFundRecommendMailTask() {
    super();
  }

  public PsnFundRecommendMailTask(String beanName) {
    super(beanName);
  }

  public void doRun() throws SingleTaskException {
    if (!super.isAllowExecution()) {
      logger.info("=========PsnFundRecommendMailTask已关闭==========");
      return;
    }

    try {
      List<Long> fundIds = psnFundRcmdMailService.getFundList();
      if (fundIds != null && fundIds.size() > 0) {
        // 根据基金信息查询人员发送邮件
        List<Long> psnIdList = psnFundRcmdMailService.getPsnIdsByFund(fundIds);
        if (psnIdList != null && psnIdList.size() > 0) {
          for (Long psnId : psnIdList) {
            List<PsnFundRecommend> reFundList = psnFundRcmdMailService.getPsnFundRecommendList(psnId, fundIds);
            // 获取收件人的人员信息.
            Person person = psnFundRcmdMailService.getPsnInfo(psnId);
            // 构建邮件信息.
            if (person != null && StringUtils.isNotBlank(person.getEmail())) {
              // 一个收件箱一周只能收到一封基金推荐的邮件 将邮件模板表的LIMIT_STATUS设置为2
              psnFundRcmdMailService.buildRcmdMailInfo(person, reFundList);
              /*
               * 这段代码先注释掉，生产机消息推送模板还没审核成功 // 基金推荐微信消息推送 Long openId =
               * psnFundRcmdMailService.getUserOpenId(person.getPersonId(), "00000000"); if (openId != null) { if
               * (psnFundRcmdMailService.getDataByOpenId(openId, "00000000")) {
               * psnFundRcmdMailService.saveWeChatMessagePsn(person, reFundList.get(0), openId,
               * reFundList.size()); } }
               */
            }
            psnFundRcmdMailService.updateSendmailStatus(psnId, fundIds);
          }
        }
      }
    } catch (Exception e) {
      logger.error("人员基金邮件发送失败", e);
    }
  }

}
