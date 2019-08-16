package com.smate.sie.center.task.service;

import java.util.Calendar;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.exception.ServiceException;

/**
 * 单位分析，拓展表服务实现
 * 
 * @author hd
 *
 */
@Service("sieImpactExtendService")
@Transactional(rollbackFor = Exception.class)
public class SieImpactExtendServiceImpl implements SieImpactExtendService {
  Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private SieImpactCalAspectsService sieImpactCalAspectsService;
  @Autowired
  private SieImpactCalDataService sieImpactCalDataService;
  @Autowired
  private SieImpactIndexCalAspectsService sieImpactIndexCalAspectsService;

  @Override
  public void doCalculate(Long insId, Date date) throws ServiceException {
    try {
      /* KPI_IMPACT_EXTEND_***1表 */
      calByAspects(insId, date);
      /* KPI_IMPACT_EXTEND_***2表 */
      calByData(insId, date);

      /* KPI_IMPACT_EXTEND_VIEW_IP */
      calByAspectsWithIP(insId, date);
      /* 删除两个月之前的数据 */
      sieImpactCalAspectsService.delTwoMonBeforeData(insId, date);
      sieImpactCalDataService.delTwoMonBeforeData(insId, date);
      sieImpactIndexCalAspectsService.delTwoMonBeforeData(insId, date);
      sieImpactIndexCalAspectsService.delTwoMonBeforeIPData(insId, date);
    } catch (Exception e) {
      logger.error("SieImpactExtendTask: 分析社交化行为异常！insId：{} ;date:{} ", new Object[] {insId, date, e});
      throw new ServiceException("SieImpactExtendTask:分析社交化行为异常！insId：" + insId + " ;date:" + date, e);
    }


  }

  @Override
  public void calByAspectsWithIP(Long insId, Date date) {
    /* 访客数 */
    sieImpactCalAspectsService.calViewIpCntByAspects(insId, date);
  }

  @Override
  public void calByAspects(Long insId, Date date) throws ServiceException {
    try {
      /* 阅读 */
      sieImpactCalAspectsService.calViewByAspects(insId, date);
      /* 赞 */
      sieImpactCalAspectsService.calAwardByAspects(insId, date);
      /* 下载 */
      sieImpactCalAspectsService.calDownLoadByAspects(insId, date);
      /* 分享 */
      sieImpactCalAspectsService.calShareByAspects(insId, date);
      /* 主页访问 */
      sieImpactIndexCalAspectsService.calIndexByAspects(insId, date);

    } catch (Exception e) {
      logger.error("SieImpactExtendTask: KPI_IMPACT_EXTEND_***1表，分析社交化行为异常！insId：{} ;date:{} ",
          new Object[] {insId, date, e});
      throw new ServiceException("SieImpactExtendTask:分析社交化行为异常！insId：" + insId + " ;date:" + date, e);
    }

  }

  @Override
  public void calByData(Long insId, Date date) throws ServiceException {
    try {
      /* 阅读 */
      sieImpactCalDataService.calViewByData(insId, date);
      /* 赞 */
      sieImpactCalDataService.calAwardByData(insId, date);
      /* 下载 */
      sieImpactCalDataService.calDownloadByData(insId, date);
      /* 分享 */
      sieImpactCalDataService.calShareByData(insId, date);

    } catch (Exception e) {
      logger.error("SieImpactExtendTask: KPI_IMPACT_EXTEND_***2表，分析社交化行为异常！insId：{} ;date:{} ",
          new Object[] {insId, date, e});
      throw new ServiceException("SieImpactExtendTask:分析社交化行为异常！insId：" + insId + " ;date:" + date, e);
    }
  }

  @Override
  public boolean checkRepeat(Long insId) throws ServiceException {
    boolean flag = false; // 默认单位在扩展表不存在上月数据
    Date beginTime = getFirstTimeOfMonth(-1);
    Date endTime = getFirstTimeOfMonth(0);
    flag = sieImpactCalAspectsService.checkRepeatWithAspects(insId, beginTime, endTime);
    if (!flag) {
      flag = sieImpactCalDataService.checkRepeatWithData(insId);
    }
    if (!flag) {
      flag = sieImpactIndexCalAspectsService.checkRepeatByIndex(insId, beginTime, endTime);
    }
    return flag;
  }

  /**
   * 获取每个月的第一天
   * 
   * @param increment
   * @return
   */
  private Date getFirstTimeOfMonth(Integer increment) {
    // SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Calendar c = Calendar.getInstance();
    c.setTime(new Date());
    c.add(Calendar.MONTH, increment);
    c.set(Calendar.DAY_OF_MONTH, 1);
    // 将小时至0
    c.set(Calendar.HOUR_OF_DAY, 0);
    // 将分钟至0
    c.set(Calendar.MINUTE, 0);
    // 将秒至0
    c.set(Calendar.SECOND, 0);
    // 将毫秒至0
    c.set(Calendar.MILLISECOND, 0);
    Date m = c.getTime();
    return m;
  }
}
