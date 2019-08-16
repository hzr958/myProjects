package com.smate.center.task.quartz;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.task.base.TaskAbstract;
import com.smate.center.task.exception.ServiceException;
import com.smate.center.task.model.sns.psn.ETemplateInfluenceCount;
import com.smate.center.task.service.statistics.InfluenceStatisticalService;
import com.smate.center.task.service.statistics.PsnInfluenceService;
import com.smate.core.base.psn.model.PsnStatistics;

/**
 * 科研影响力任务类
 * 
 * @author hht
 * @Time 2018年10月24日 上午11:52:05
 */
public class InfluenceStatisticalTask extends TaskAbstract {
  private final Logger logger = LoggerFactory.getLogger(getClass());
  private Integer size = 0;
  private boolean isContinue = true;

  // 注入需要的service
  // 新系统的人员影响力服务类
  @Autowired
  private PsnInfluenceService psnInfluenceService;
  @Autowired
  private InfluenceStatisticalService influenceStatisticalService;

  public InfluenceStatisticalTask() {
    super();
  }

  public InfluenceStatisticalTask(String beanName) {
    super(beanName);
  }

  /**
   * 具体业务实现
   */
  public void doRun() {
    if (!super.isAllowExecution()) {
      logger.info("=========InfluenceStatisticalTask已关闭==========");
      return;
    }

    try {
      // 先判断上月是否已发送邮件
      Long c = psnInfluenceService.checkLastMonthHadSend();
      logger.debug("是否可以进入到if里" + (c == 0L) + " ，c=" + c);

      if (c == 0L) {

        // 初始化数据，将存在的记录统计数全置0
        this.initDate();
        logger.debug("------------------------------每周影响力统计,已经完成了初始化动作------------------------");
        // 新增成果数
        this.publicationDateDeal();
        // 成果引用数
        this.pubCitedTimesDeal();
        // 下载成果数
        this.downloadDateDeal();
        // 阅读数
        // this.readStatisticsDateDeal();
        this.vistStatisticsDateDeal();
        // 赞
        this.awardStatisticsDateDeal();
        // 分享
        this.shareStatisticsDateDeal();
        // 评论数
        // this.commendStatisticsDateDeal();
        // 发送邮件
        logger.debug("--------------新增成果，阅读数，下载数，赞数，分享阅读评论数已经得到啦--------------");
        while (true) {
          size = 0;
          List<ETemplateInfluenceCount> influenceCounts = psnInfluenceService.findETemplateInfluenceCounts(size);
          if (CollectionUtils.isNotEmpty(influenceCounts)) {
            // 整理hindex
            logger.debug("-------------influenceCounts.size=--------------" + influenceCounts.size());
            this.hindexDateDeal(influenceCounts);
            for (ETemplateInfluenceCount influence : influenceCounts) {
              // 如果论文，赞，引用，下载，阅读,分享总数任意为0,则处理
              psnInfluenceService.dealCount(Long.valueOf(influence.getPsnId()), influence);
              if (influence.getMonthAwardCount() == 0 && influence.getMonthReadCount() == 0
                  && influence.getMonthCitedTimesCount() == 0 && influence.getMonthShareCount() == 0) {
                // 数据量不足，不发送置状态为2
                influence.setStatus(2);
              } else {
                influence.setStatus(influenceStatisticalService.sendMail(influence));
              }
              influence.setCreateDate(new Date());
              psnInfluenceService.updateInfluence(influence);
            }
          } else {
            return;
          }
          size += 1;
        }
      } else {
        logger.error("xx");
      }
    } catch (Exception e) {
      logger.error("科研影响力近期统计分析任务", e);
    }
  }


  /**
   * 初始化数据
   */
  private void initDate() throws ServiceException {
    isContinue = true;
    size = 0;
    while (isContinue) {
      List<ETemplateInfluenceCount> eicList = psnInfluenceService.findETemplateInfluenceCount(size);
      if (CollectionUtils.isNotEmpty(eicList)) {
        for (ETemplateInfluenceCount influenCount : eicList) {
          influenCount.setStatus(0);
          influenCount.setMonthAwardCount(0L);
          influenCount.setAwardCount(0L);
          influenCount.setMonthDownloadCount(0L);
          influenCount.setDownloadCount(0L);
          influenCount.setMonthPubCount(0L);
          influenCount.setPubCount(0L);
          influenCount.setMonthReadCount(0L);
          influenCount.setReadCount(0L);
          influenCount.setMonthCitedTimesCount(0L);
          influenCount.setCitedTimesCount(0L);
          influenCount.setMonthShareCount(0L);
          influenCount.setShareCount(0L);
          influenCount.setHindex(0);
          psnInfluenceService.updateInfluence(influenCount);
          logger.debug("初始化表ETEMPLATE_INFLUENCE_COUNT");
        }
      } else {
        isContinue = false;
      }
    }
  }

  @SuppressWarnings("rawtypes")
  private void publicationDateDeal() throws ServiceException {
    isContinue = true;
    size = 0;
    while (isContinue) {
      List<Map> monthPubs = psnInfluenceService.getLastMonthPsnPubs(size);
      if (CollectionUtils.isNotEmpty(monthPubs)) {
        psnInfluenceService.findPubStatistics(monthPubs);
        size += 1;
      } else {
        isContinue = false;
      }
    }
  }

  @SuppressWarnings("rawtypes")
  private void pubCitedTimesDeal() throws ServiceException {
    isContinue = true;
    size = 0;
    while (isContinue) {
      List<Map> monthPubsCitedTimes = psnInfluenceService.getLastMonthPsnCitedTimes(size);
      if (CollectionUtils.isNotEmpty(monthPubsCitedTimes)) {
        psnInfluenceService.findCitedTimesStatistics(monthPubsCitedTimes);
        size += 1;
      } else {
        isContinue = false;
      }
    }
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  private void downloadDateDeal() throws ServiceException {
    isContinue = true;
    size = 0;

    while (isContinue) {
      List<Map> dcRecordList = psnInfluenceService.findDownloadCollectStatistics(size);
      if (CollectionUtils.isNotEmpty(dcRecordList)) {
        psnInfluenceService.findDownloadCollectStatistics(dcRecordList);
        size += 1;
      } else {
        isContinue = false;
      }
    }
  }

  @SuppressWarnings("rawtypes")
  private void vistStatisticsDateDeal() throws ServiceException {
    isContinue = true;
    size = 0;
    while (isContinue) {
      List<Map> psnStatistics = psnInfluenceService.findVistPsn(size);
      if (CollectionUtils.isNotEmpty(psnStatistics)) {
        psnInfluenceService.findReadStatistics(psnStatistics);
        size += 1;
      } else {
        isContinue = false;
      }
    }
  }

  @SuppressWarnings("rawtypes")
  private void awardStatisticsDateDeal() throws ServiceException {
    isContinue = true;
    size = 0;
    while (isContinue) {
      List<Map> awardList = psnInfluenceService.findAwardPsn(size);
      if (CollectionUtils.isNotEmpty(awardList)) {
        psnInfluenceService.findAwardStatistics(awardList);
        size += 1;
      } else {
        isContinue = false;
      }
    }
  }

  @SuppressWarnings("rawtypes")
  private void shareStatisticsDateDeal() throws ServiceException {
    isContinue = true;
    size = 0;
    while (isContinue) {
      List<Map> shareList = psnInfluenceService.findSharePsn(size);
      if (CollectionUtils.isNotEmpty(shareList)) {
        psnInfluenceService.findShareStatistics(shareList);
        size += 1;
      } else {
        isContinue = false;
      }
    }
  }

  private void hindexDateDeal(List<ETemplateInfluenceCount> influenceCounts) throws ServiceException {

    // 取psnid
    List<Long> psnIdList = new ArrayList<Long>();
    for (ETemplateInfluenceCount eiCount : influenceCounts) {
      psnIdList.add(eiCount.getPsnId());
    }
    // 取hindex
    List<PsnStatistics> pstList = psnInfluenceService.getHindexByPsnIds(psnIdList);
    // hindex封装成Map
    // Map<Long,Integer> hindexMap = this.handleHindex(pstList);
    if (CollectionUtils.isNotEmpty(pstList)) {
      // 将hindex保存
      psnInfluenceService.findHindexStatistics(pstList);
      // 赋值
      /*
       * for(ETemplateInfluenceCount eiCount:influenceCounts){
       * eiCount.setHindex(hindexMap.get(eiCount.getPsnId())); }
       */
    }

  }

}
