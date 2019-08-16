package com.smate.center.task.quartz.email;

import java.util.List;
import java.util.Locale;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.task.base.TaskAbstract;
import com.smate.center.task.service.email.BaseRaRecmdService;
import com.smate.center.task.service.email.EmailInfoBaseMethod;
import com.smate.center.task.service.fund.PsnFundRcmdMailService;
import com.smate.center.task.service.sns.quartz.PsnKwRmcService;
import com.smate.center.task.single.service.pub.KeywordIdentificationService;
import com.smate.core.base.utils.model.security.Person;

/**
 * 推荐研究领域的推荐任务.
 * 
 * @author mjg
 * 
 */
public class BaseRaRecmdTask extends TaskAbstract {

  // 一次最多操作的数据.
  private final static Integer MAX_SIZE = 1;
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private BaseRaRecmdService basePubRecmdService;
  @Autowired
  private PsnKwRmcService psnKwRmcService;
  @Autowired
  private PsnFundRcmdMailService psnFundRcmdMailService;
  @Autowired
  private KeywordIdentificationService keywordIdentificationService;
  private static boolean isRun = true;

  public BaseRaRecmdTask() {}

  public BaseRaRecmdTask(String beanName) {
    super(beanName);
  }

  public Boolean run() {
    Long startPsnId = 0L;
    try {

      while (isRun) {
        // 获取有推荐基金的人员ID.
        List<Long> reKwPsnIdList = psnKwRmcService.getPsnIdList(startPsnId, MAX_SIZE);
        if (CollectionUtils.isEmpty(reKwPsnIdList)) {
          break;
        }
        // 遍历人员ID，获取推荐的研究领域记录.
        for (int i = 0; i < reKwPsnIdList.size(); i++) {
          Long iPsnId = reKwPsnIdList.get(i);
          List<String> rKwList = keywordIdentificationService.recommendKw(iPsnId, Locale.CHINESE);
          if (CollectionUtils.isNotEmpty(rKwList)) {
            // 获取收件人的人员信息.
            Person person = psnFundRcmdMailService.getPsnInfo(iPsnId);
            // 构建邮件信息.
            try {
              basePubRecmdService.buildMailLogEntity(person, rKwList);
            } catch (Exception e) {
              logger.error("BasePubRecmdTask-推荐研究领域的推荐邮件记录出错：psnId-" + iPsnId, EmailInfoBaseMethod.getErrorMsg(e));
              continue;
            }
          }

        }
        startPsnId = reKwPsnIdList.get(reKwPsnIdList.size() - 1);
        isRun = false;
      }
    } catch (Exception e) {
      logger.error(" 推荐研究领域的推荐任务失败.", e);
    } finally {
      // emailSendTaskDBService.updateNextDate(BpoEtemplateConstant.RESEACHAREA_RECMD_CODE);
    }
    return true;
  }

}
