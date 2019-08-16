package com.smate.sie.center.task.service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.exception.ServiceException;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.sie.core.base.utils.dao.statistics.KpiImpactBaseIndexDao;
import com.smate.sie.core.base.utils.dao.statistics.KpiImpactExtendIndexOneDao;
import com.smate.sie.core.base.utils.dao.statistics.KpiImpactExtendViewIPDao;
import com.smate.sie.core.base.utils.model.statistics.ImpactConsts;
import com.smate.sie.core.base.utils.model.statistics.KpiImpactExtendIndexOne;

/**
 * 单位分析，主页社交化数据拓展表1表服务
 * 
 * @author hd
 *
 */
@Service("sieImpactIndexCalAspectsService")
@Transactional(rollbackFor = Exception.class)
public class SieImpactIndexCalAspectsServiceImpl implements SieImpactIndexCalAspectsService {
  Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private KpiImpactExtendIndexOneDao kpiImpactExtendIndexOneDao;
  @Autowired
  private KpiImpactBaseIndexDao kpiImpactBaseIndexDao;

  @Autowired
  private KpiImpactExtendViewIPDao kpiImpactExtendViewIPDao;

  @Override
  public void calIndexByAspects(Long insId, Date date) throws ServiceException {
    try {
      if (insId == null || date == null) {
        return;
      }
      Calendar c = getLastMonth(date);
      if (c != null) {
        /* 获取上月天数 */
        int days = c.getActualMaximum(Calendar.DAY_OF_MONTH);
        /* 循环计算每一天的数据 */
        for (int i = 1; i <= days; i++) {
          c.set(Calendar.DAY_OF_MONTH, i);
          Date stDate = c.getTime();
          /* 阅读 */
          calIndexByItem(insId, stDate);
          /* 2日期 */
          calIndexByDate(insId, stDate);
        }

      }
    } catch (Exception e) {
      logger.error("SieImpactExtendTask: 分析阅读数行为异常！insId：{}  ;date:{} ", new Object[] {insId, date, e});
      throw new ServiceException("SieImpactExtendTask: 分析阅读数行为异常！insId：" + insId + " ;date:" + date, e);
    }

  }

  @SuppressWarnings("rawtypes")
  @Override
  public void calIndexByItem(Long insId, Date stDate) throws ServiceException {
    String item = null;
    try {
      for (Integer calType : ImpactConsts.INDEX_CAL_TYPE_LIST) {
        item = getCalIndexItem(calType);
        if (item == null) {
          continue;
        }
        List<Map<String, Object>> itemList = kpiImpactBaseIndexDao.getCountByItems(stDate, insId, item);
        if (itemList != null && itemList.size() > 0) {
          for (Map temp : itemList) {
            String itemname = temp.get(item).toString();
            Long count = NumberUtils.parseLong(temp.get("count").toString(), 0L);
            KpiImpactExtendIndexOne record = new KpiImpactExtendIndexOne(stDate, calType, itemname, count, insId);
            kpiImpactExtendIndexOneDao.save(record);
          }
        }
      }
    } catch (Exception e) {
      logger.error("SieImpactExtendTask: 分析主页阅读数行为异常！insId：{}  ;stDate:{} ;calType:{}",
          new Object[] {insId, stDate, item, e});
      throw new ServiceException(
          "SieImpactExtendTask: 分析主页阅读数行为异常！insId：" + insId + " ;stDate:" + stDate + " ;calType:" + item, e);
    }

  }

  @Override
  public void calIndexByDate(Long insId, Date stDate) throws ServiceException {
    try {
      Long count = kpiImpactBaseIndexDao.getCountByDate(stDate, insId);
      KpiImpactExtendIndexOne record =
          new KpiImpactExtendIndexOne(stDate, ImpactConsts.IMPACT_INDEX_CAL_DATE, "日期", count, insId);
      kpiImpactExtendIndexOneDao.save(record);
    } catch (Exception e) {
      logger.error("SieImpactExtendTask: 日期-分析主页阅读数行为异常！insId：{}  ;stDate:{} ;calType:{}",
          new Object[] {insId, stDate, "st_date", e});
      throw new ServiceException(
          "SieImpactExtendTask: 日期-分析主页阅读数行为异常！insId：" + insId + " ;stDate:" + stDate + " ;calType:" + "st_date", e);
    }


  }

  @Override
  public void delTwoMonBeforeData(Long insId, Date date) throws ServiceException {
    try {
      Date beginTime = getFirstTimeOfMonth(-2, date);
      kpiImpactExtendIndexOneDao.delTwoMonBeforeData(insId, beginTime);

    } catch (Exception e) {
      logger.error("SieImpactExtendTask: 删除KPI_IMPACT_EXTEND_INDEX*1表两个月之前的数据异常！insId：{} ", new Object[] {insId, e});
      throw new ServiceException("SieImpactExtendTask: 删除KPI_IMPACT_EXTEND_INDEX*1表两个月之前的数据异常！insId：" + insId, e);
    }

  }


  /**
   * 获取每个月的第一天
   * 
   * @param increment
   * @return
   */
  private Date getFirstTimeOfMonth(Integer increment, Date date) {
    // SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Calendar c = Calendar.getInstance();
    c.setTime(date);
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


  private Calendar getLastMonth(Date date) {
    Calendar c = Calendar.getInstance();
    c.setTime(new Date());
    c.add(Calendar.MONTH, -1);
    c.set(Calendar.DAY_OF_MONTH, 1);
    // 将小时至0
    c.set(Calendar.HOUR_OF_DAY, 0);
    // 将分钟至0
    c.set(Calendar.MINUTE, 0);
    // 将秒至0
    c.set(Calendar.SECOND, 0);
    // 将毫秒至0
    c.set(Calendar.MILLISECOND, 0);
    return c;
  }

  private String getCalIndexItem(Integer calType) {
    switch (calType) {
      case 1:
        return ImpactConsts.FIELD_IP;
    }
    return null;
  }

  @Override
  public void delTwoMonBeforeIPData(Long insId, Date date) {
    try {
      Calendar c = getBeforeMonth(-2, date);
      if (c != null) {
        String time = String.valueOf(c.get(Calendar.YEAR)) + String.valueOf(c.get(Calendar.MONTH) + 1);
        kpiImpactExtendViewIPDao.delTwoMonBeforeData(insId, time);
      }

    } catch (Exception e) {
      logger.error("SieImpactExtendTask: 删除KPI_IMPACT_EXTEND_VIEW_IP表两个月之前的数据异常！insId：{} ", new Object[] {insId, e});
      throw new ServiceException("SieImpactExtendTask: 删除KPI_IMPACT_EXTEND_VIEW_IP表两个月之前的数据异常！insId：" + insId, e);
    }
  }


  private Calendar getBeforeMonth(Integer increment, Date date) {
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
    return c;
  }

  @Override
  public boolean checkRepeatByIndex(Long insId, Date startDate, Date endDate) throws ServiceException {
    boolean flag = false;
    Long extendIndexCount = kpiImpactExtendIndexOneDao.getCountByDateTime(insId, startDate, endDate);
    if (extendIndexCount > 0) {
      flag = true;
    }
    return flag;
  }
}
