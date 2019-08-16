package com.smate.sie.center.task.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
import com.smate.core.base.utils.exception.SysServiceException;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.sie.core.base.utils.dao.statistics.KpiImpactBaseAwardDao;
import com.smate.sie.core.base.utils.dao.statistics.KpiImpactBaseDownloadDao;
import com.smate.sie.core.base.utils.dao.statistics.KpiImpactBaseShareDao;
import com.smate.sie.core.base.utils.dao.statistics.KpiImpactBaseViewDao;
import com.smate.sie.core.base.utils.dao.statistics.KpiImpactExtendAwardOneDao;
import com.smate.sie.core.base.utils.dao.statistics.KpiImpactExtendDownloadOneDao;
import com.smate.sie.core.base.utils.dao.statistics.KpiImpactExtendShareOneDao;
import com.smate.sie.core.base.utils.dao.statistics.KpiImpactExtendViewIPDao;
import com.smate.sie.core.base.utils.dao.statistics.KpiImpactExtendViewOneDao;
import com.smate.sie.core.base.utils.model.statistics.ImpactConsts;
import com.smate.sie.core.base.utils.model.statistics.KpiImpactExtendAwardOne;
import com.smate.sie.core.base.utils.model.statistics.KpiImpactExtendDownloadOne;
import com.smate.sie.core.base.utils.model.statistics.KpiImpactExtendShareOne;
import com.smate.sie.core.base.utils.model.statistics.KpiImpactExtendViewIP;
import com.smate.sie.core.base.utils.model.statistics.KpiImpactExtendViewOne;

/**
 * 单位分析，社交化数据拓展表1表服务
 * 
 * @author hd
 *
 */
@Service("sieImpactCalAspectsService")
@Transactional(rollbackFor = Exception.class)
public class SieImpactCalAspectsServiceImpl implements SieImpactCalAspectsService {
  Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private KpiImpactExtendViewOneDao kpiImpactExtendViewOneDao;

  @Autowired
  private KpiImpactExtendViewIPDao kpiImpactExtendViewIPDao;
  @Autowired
  private KpiImpactExtendShareOneDao kpiImpactExtendShareOneDao;
  @Autowired
  private KpiImpactExtendDownloadOneDao kpiImpactExtendDownloadOneDao;
  @Autowired
  private KpiImpactExtendAwardOneDao kpiImpactExtendAwardOneDao;
  @Autowired
  private KpiImpactBaseViewDao kpiImpactBaseViewDao;
  @Autowired
  private KpiImpactBaseShareDao kpiImpactBaseShareDao;
  @Autowired
  private KpiImpactBaseDownloadDao kpiImpactBaseDownloadDao;
  @Autowired
  private KpiImpactBaseAwardDao kpiImpactBaseAwardDao;

  @Override
  public void calViewByAspects(Long insId, Date date) throws ServiceException {
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
          /* 1国家，2省份，3城市，4单位，5研究领域，6职称 */
          /* 成果 */
          calViewByItem(insId, stDate, ImpactConsts.SIE_PUB);
          /* 项目 */
          calViewByItem(insId, stDate, ImpactConsts.SIE_PATENT);
          /* 专利 */
          calViewByItem(insId, stDate, ImpactConsts.SIE_PRJ);
          /* 7日期 */
          calViewByDate(insId, stDate);
        }

      }
    } catch (Exception e) {
      logger.error("SieImpactExtendTask: 分析阅读数行为异常！insId：{}  ;date:{} ", new Object[] {insId, date, e});
      throw new ServiceException("SieImpactExtendTask: 分析阅读数行为异常！insId：" + insId + " ;date:" + date, e);
    }
  }

  /**
   * 统计某个insId,在指定stDate日期，keyType(数据类型：1项目2论文3专利)的不同统计维度(方法内item变量)的View(阅读)记录
   */
  @SuppressWarnings("rawtypes")
  @Override
  public void calViewByItem(Long insId, Date stDate, Integer keyType) throws ServiceException {
    String item = null;
    try {
      for (Integer calType : ImpactConsts.CAL_TYPE_LIST) {
        item = getCalItem(calType);
        if (item == null) {
          continue;
        }
        List<Map<String, Object>> itemList = kpiImpactBaseViewDao.getCountByItems(stDate, keyType, insId, item);
        if (itemList != null && itemList.size() > 0) {
          for (Map temp : itemList) {
            String itemname = temp.get(item).toString();
            Long count = NumberUtils.parseLong(temp.get("count").toString(), 0L);
            KpiImpactExtendViewOne record =
                new KpiImpactExtendViewOne(stDate, calType, itemname, count, insId, keyType);
            kpiImpactExtendViewOneDao.save(record);
          }
        }
      }
    } catch (Exception e) {
      logger.error("SieImpactExtendTask: 分析阅读数行为异常！insId：{} ;keyType:{} ;stDate:{} ;calType:{}",
          new Object[] {insId, keyType, stDate, item, e});
      throw new ServiceException("SieImpactExtendTask: 分析阅读数行为异常！insId：" + insId + " ;keyType:" + keyType + " ;stDate:"
          + stDate + " ;calType:" + item, e);
    }

  }


  /**
   * 统计某个单位的某天的记录，分不同keyType( 数据类型：1项目2论文3专利)
   */
  @Override
  public void calViewByDate(Long insId, Date stDate) throws ServiceException {
    Integer type = null;
    try {
      for (Integer keyType : ImpactConsts.DATA_TYPE_LIST) {
        if (keyType == null) {
          continue;
        }
        type = keyType;
        Long count = kpiImpactBaseViewDao.getCountByDate(stDate, keyType, insId);
        KpiImpactExtendViewOne record =
            new KpiImpactExtendViewOne(stDate, ImpactConsts.IMPACT_CAL_DATE, "日期", count, insId, keyType);
        kpiImpactExtendViewOneDao.save(record);
      }
    } catch (Exception e) {
      logger.error("SieImpactExtendTask: 日期-分析阅读数行为异常！insId：{} ;keyType:{} ;stDate:{} ;calType:{}",
          new Object[] {insId, type, stDate, "st_date", e});
      throw new ServiceException("SieImpactExtendTask: 日期-分析阅读数行为异常！insId：" + insId + " ;keyType:" + type + " ;stDate:"
          + stDate + " ;calType:" + "st_date", e);
    }

  }

  @Override
  public void calAwardByAspects(Long insId, Date date) throws ServiceException {
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
          /* 1国家，2省份，3城市，4单位，5研究领域，6职称 */
          /* 成果 */
          calAwardByItem(insId, stDate, ImpactConsts.SIE_PUB);
          /* 项目 */
          calAwardByItem(insId, stDate, ImpactConsts.SIE_PATENT);
          /* 专利 */
          calAwardByItem(insId, stDate, ImpactConsts.SIE_PRJ);
          /* 7日期 */
          calAwardByDate(insId, stDate);
        }

      }
    } catch (Exception e) {
      logger.error("SieImpactExtendTask: 分析阅读数行为异常！insId：{}  ;date:{} ", new Object[] {insId, date, e});
      throw new ServiceException("SieImpactExtendTask: 分析阅读数行为异常！insId：" + insId + " ;date:" + date, e);
    }

  }

  @SuppressWarnings("rawtypes")
  @Override
  public void calAwardByItem(Long insId, Date stDate, Integer keyType) throws ServiceException {
    String item = null;
    try {
      for (Integer calType : ImpactConsts.CAL_TYPE_LIST) {
        item = getCalItem(calType);
        if (item == null) {
          continue;
        }
        List<Map<String, Object>> itemList = kpiImpactBaseAwardDao.getCountByItems(stDate, keyType, insId, item);
        if (itemList != null && itemList.size() > 0) {
          for (Map temp : itemList) {
            String itemname = temp.get(item).toString();
            Long count = NumberUtils.parseLong(temp.get("count").toString(), 0L);
            KpiImpactExtendAwardOne record =
                new KpiImpactExtendAwardOne(stDate, calType, itemname, count, insId, keyType);
            kpiImpactExtendAwardOneDao.save(record);
          }
        }
      }
    } catch (Exception e) {
      logger.error("SieImpactExtendTask: 分析赞数行为异常！insId：{} ;keyType:{} ;stDate:{} ;calType:{}",
          new Object[] {insId, keyType, stDate, item, e});
      throw new ServiceException("SieImpactExtendTask: 分析赞数行为异常！insId：" + insId + " ;keyType:" + keyType + " ;stDate:"
          + stDate + " ;calType:" + item, e);
    }

  }

  @Override
  public void calAwardByDate(Long insId, Date stDate) throws ServiceException {
    Integer type = null;
    try {
      for (Integer keyType : ImpactConsts.DATA_TYPE_LIST) {
        if (keyType == null) {
          continue;
        }
        type = keyType;
        Long count = kpiImpactBaseAwardDao.getCountByDate(stDate, keyType, insId);
        KpiImpactExtendAwardOne record =
            new KpiImpactExtendAwardOne(stDate, ImpactConsts.IMPACT_CAL_DATE, "日期", count, insId, keyType);
        kpiImpactExtendAwardOneDao.save(record);
      }
    } catch (Exception e) {
      logger.error("SieImpactExtendTask: 日期-分析赞数行为异常！insId：{} ;keyType:{} ;stDate:{} ;calType:{}",
          new Object[] {insId, type, stDate, "st_date", e});
      throw new ServiceException("SieImpactExtendTask: 日期-分析赞数行为异常！insId：" + insId + " ;keyType:" + type + " ;stDate:"
          + stDate + " ;calType:" + "st_date", e);
    }

  }

  @Override
  public void calDownLoadByAspects(Long insId, Date date) throws ServiceException {
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
          /* 1国家，2省份，3城市，4单位，5研究领域，6职称 */
          /* 成果 */
          calDownLoadByItem(insId, stDate, ImpactConsts.SIE_PUB);
          /* 项目 */
          calDownLoadByItem(insId, stDate, ImpactConsts.SIE_PATENT);
          /* 专利 */
          calDownLoadByItem(insId, stDate, ImpactConsts.SIE_PRJ);
          /* 7日期 */
          calDownLoadByDate(insId, stDate);
        }

      }
    } catch (Exception e) {
      logger.error("SieImpactExtendTask: 分析阅读数行为异常！insId：{}  ;date:{} ", new Object[] {insId, date, e});
      throw new ServiceException("SieImpactExtendTask: 分析阅读数行为异常！insId：" + insId + " ;date:" + date, e);
    }

  }

  @SuppressWarnings("rawtypes")
  @Override
  public void calDownLoadByItem(Long insId, Date stDate, Integer keyType) throws ServiceException {
    String item = null;
    try {
      for (Integer calType : ImpactConsts.CAL_TYPE_LIST) {
        item = getCalItem(calType);
        if (item == null) {
          continue;
        }
        List<Map<String, Object>> itemList = kpiImpactBaseDownloadDao.getCountByItems(stDate, keyType, insId, item);
        if (itemList != null && itemList.size() > 0) {
          for (Map temp : itemList) {
            String itemname = temp.get(item).toString();
            Long count = NumberUtils.parseLong(temp.get("count").toString(), 0L);
            KpiImpactExtendDownloadOne record =
                new KpiImpactExtendDownloadOne(stDate, calType, itemname, count, insId, keyType);
            kpiImpactExtendDownloadOneDao.save(record);
          }
        }
      }
    } catch (Exception e) {
      logger.error("SieImpactExtendTask: 分析下载数行为异常！insId：{} ;keyType:{} ;stDate:{} ;calType:{}",
          new Object[] {insId, keyType, stDate, item, e});
      throw new ServiceException("SieImpactExtendTask: 分析下载数行为异常！insId：" + insId + " ;keyType:" + keyType + " ;stDate:"
          + stDate + " ;calType:" + item, e);
    }

  }

  @Override
  public void calDownLoadByDate(Long insId, Date stDate) throws ServiceException {
    Integer type = null;
    try {
      for (Integer keyType : ImpactConsts.DATA_TYPE_LIST) {
        if (keyType == null) {
          continue;
        }
        type = keyType;
        Long count = kpiImpactBaseDownloadDao.getCountByDate(stDate, keyType, insId);
        KpiImpactExtendDownloadOne record =
            new KpiImpactExtendDownloadOne(stDate, ImpactConsts.IMPACT_CAL_DATE, "日期", count, insId, keyType);
        kpiImpactExtendDownloadOneDao.save(record);
      }
    } catch (Exception e) {
      logger.error("SieImpactExtendTask: 日期-分析下载数行为异常！insId：{} ;keyType:{} ;stDate:{} ;calType:{}",
          new Object[] {insId, type, stDate, "st_date", e});
      throw new ServiceException("SieImpactExtendTask: 日期-分析下载数行为异常！insId：" + insId + " ;keyType:" + type + " ;stDate:"
          + stDate + " ;calType:" + "st_date", e);
    }

  }

  @Override
  public void calShareByAspects(Long insId, Date date) throws ServiceException {
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
          /* 1国家，2省份，3城市，4单位，5研究领域，6职称 */
          /* 成果 */
          calShareByItem(insId, stDate, ImpactConsts.SIE_PUB);
          /* 项目 */
          calShareByItem(insId, stDate, ImpactConsts.SIE_PATENT);
          /* 专利 */
          calShareByItem(insId, stDate, ImpactConsts.SIE_PRJ);
          /* 7日期 */
          calShareByDate(insId, stDate);
        }

      }
    } catch (Exception e) {
      logger.error("SieImpactExtendTask: 分析阅读数行为异常！insId：{}  ;date:{} ", new Object[] {insId, date, e});
      throw new ServiceException("SieImpactExtendTask: 分析阅读数行为异常！insId：" + insId + " ;date:" + date, e);
    }

  }

  @SuppressWarnings("rawtypes")
  @Override
  public void calShareByItem(Long insId, Date stDate, Integer keyType) throws ServiceException {
    String item = null;
    try {
      for (Integer calType : ImpactConsts.CAL_TYPE_LIST) {
        item = getCalItem(calType);
        if (item == null) {
          continue;
        }
        List<Map<String, Object>> itemList = kpiImpactBaseShareDao.getCountByItems(stDate, keyType, insId, item);
        if (itemList != null && itemList.size() > 0) {
          for (Map temp : itemList) {
            String itemname = temp.get(item).toString();
            Long count = NumberUtils.parseLong(temp.get("count").toString(), 0L);
            KpiImpactExtendShareOne record =
                new KpiImpactExtendShareOne(stDate, calType, itemname, count, insId, keyType);
            kpiImpactExtendShareOneDao.save(record);
          }
        }
      }
    } catch (Exception e) {
      logger.error("SieImpactExtendTask: 分析分享数行为异常！insId：{} ;keyType:{} ;stDate:{} ;calType:{}",
          new Object[] {insId, keyType, stDate, item, e});
      throw new ServiceException("SieImpactExtendTask: 分析分享数行为异常！insId：" + insId + " ;keyType:" + keyType + " ;stDate:"
          + stDate + " ;calType:" + item, e);
    }

  }

  @Override
  public void calShareByDate(Long insId, Date stDate) throws ServiceException {
    Integer type = null;
    try {
      for (Integer keyType : ImpactConsts.DATA_TYPE_LIST) {
        if (keyType == null) {
          continue;
        }
        type = keyType;
        Long count = kpiImpactBaseShareDao.getCountByDate(stDate, keyType, insId);
        KpiImpactExtendShareOne record =
            new KpiImpactExtendShareOne(stDate, ImpactConsts.IMPACT_CAL_DATE, "日期", count, insId, keyType);
        kpiImpactExtendShareOneDao.save(record);
      }
    } catch (Exception e) {
      logger.error("SieImpactExtendTask: 日期-分析分享数行为异常！insId：{} ;keyType:{} ;stDate:{} ;calType:{}",
          new Object[] {insId, type, stDate, "st_date", e});
      throw new ServiceException("SieImpactExtendTask: 日期-分析分享数行为异常！insId：" + insId + " ;keyType:" + type + " ;stDate:"
          + stDate + " ;calType:" + "st_date", e);
    }

  }


  @Override
  public void delTwoMonBeforeData(Long insId, Date date) throws ServiceException {
    try {
      Date beginTime = getFirstTimeOfMonth(-2, date);
      kpiImpactExtendViewOneDao.delTwoMonBeforeData(insId, beginTime);
      kpiImpactExtendShareOneDao.delTwoMonBeforeData(insId, beginTime);
      kpiImpactExtendDownloadOneDao.delTwoMonBeforeData(insId, beginTime);
      kpiImpactExtendAwardOneDao.delTwoMonBeforeData(insId, beginTime);

    } catch (Exception e) {
      logger.error("SieImpactExtendTask: 删除KPI_IMPACT_EXTEND_*1表两个月之前的数据异常！insId：{} ", new Object[] {insId, e});
      throw new ServiceException("SieImpactExtendTask: 删除KPI_IMPACT_EXTEND_*1表两个月之前的数据异常！insId：" + insId, e);
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

  private String getCalItem(Integer calType) {
    switch (calType) {
      case 1:
        return ImpactConsts.FIELD_COUNTRY;
      case 2:
        return ImpactConsts.FIELD_PROV;
      case 3:
        return ImpactConsts.FIELD_CITY;
      case 4:
        return ImpactConsts.FIELD_PSNINSNAME;
      case 5:
        return ImpactConsts.FIELD_PSNSUB;
      case 6:
        return ImpactConsts.FIELD_PSNPOS;
    }
    return null;
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

  /**
   * 获取参数date所在月份
   * 
   * @param date
   * @return
   * @throws ParseException
   * @throws SysServiceException
   */
  private String getMonth(Date date) throws ParseException, SysServiceException {
    String dateStr = null;
    if (date != null) {
      SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
      dateStr = sdf.format(date);
    } else {
      throw new SysServiceException("SieImpactExtendTask 获取yyyyMM 时间格式串异常");
    }
    return dateStr;
  }

  public static void main(String[] args) {
    // SimpleDateFormat format = new SimpleDateFormat("yyyyMM");
    // // 2018-12-06 15:45:59.0
    // Calendar c = Calendar.getInstance();
    // c.setTime(new Date());
    // c.add(Calendar.MONTH, -2);
    // c.set(Calendar.DAY_OF_MONTH, 1);
    // // 将小时至0
    // c.set(Calendar.HOUR_OF_DAY, 0);
    // // 将分钟至0
    // c.set(Calendar.MINUTE, 0);
    // // 将秒至0
    // c.set(Calendar.SECOND, 0);
    // // 将毫秒至0
    // c.set(Calendar.MILLISECOND, 0);
    // System.out.println(format.format(c.getTime()));
    // System.out.println(c.get(Calendar.YEAR));
    // System.out.println(c.get(Calendar.MONTH));
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
    // Date date = new SieImpactCalAspectsServiceImpl().getFirstTimeOfMonth(-2, new Date());
    // System.out.println(sdf.format(date));
    // Date date2 = new SieImpactCalAspectsServiceImpl().getLastMonth(new Date()).getTime();
    // System.out.println(sdf.format(date2));
    // new SieImpactCalAspectsServiceImpl().calViewIpCntByAspects(857L, new Date());
    Calendar c = new SieImpactCalAspectsServiceImpl().getLastMonth(new Date());
    System.out.println();
  }



  @Override
  public void calViewIpCntByAspects(Long insId, Date date) {


    try {
      if (insId == null || date == null) {
        return;
      }
      /* 成果访客数 */
      calViewIpCntByItem(insId, date, ImpactConsts.ST_IP_BATCH_ONE, ImpactConsts.SIE_PUB);
      calViewIpCntByItem(insId, date, ImpactConsts.ST_IP_BATCH_TWO, ImpactConsts.SIE_PUB);

      /* 项目访客数 */
      calViewIpCntByItem(insId, date, ImpactConsts.ST_IP_BATCH_ONE, ImpactConsts.SIE_PRJ);
      calViewIpCntByItem(insId, date, ImpactConsts.ST_IP_BATCH_TWO, ImpactConsts.SIE_PRJ);

      /* 专利访客数 */
      calViewIpCntByItem(insId, date, ImpactConsts.ST_IP_BATCH_ONE, ImpactConsts.SIE_PATENT);
      calViewIpCntByItem(insId, date, ImpactConsts.ST_IP_BATCH_TWO, ImpactConsts.SIE_PATENT);

    } catch (Exception e) {
      logger.error("SieImpactExtendTask: 分析访客数数行为异常！insId：{}  ;date:{} ", new Object[] {insId, date, e});
      throw new ServiceException("SieImpactExtendTask: 分析访客数行为异常！insId：" + insId + " ;date:" + date, e);
    }

  }

  @Override
  public void calViewIpCntByItem(Long insId, Date date, Integer batch, Integer keyType)
      throws ServiceException, ParseException {
    String item = null;
    if (batch == null) {
      return;
    }

    Date beginTime;
    Date endTime;
    String stMonth = null;

    // 当前月的第一天
    endTime = getFirstTimeOfMonth(0, date);
    if (batch == ImpactConsts.ST_IP_BATCH_ONE) {
      // 当前月的前第一个月第一天
      beginTime = getFirstTimeOfMonth(-1, date);
    } else if (batch == ImpactConsts.ST_IP_BATCH_TWO) {
      // 当前月的前第二个月第一天
      beginTime = getFirstTimeOfMonth(-2, date);
    } else {
      return;
    }

    try {
      for (Integer calType : ImpactConsts.CAL_TYPE_LIST) {
        item = getCalItem(calType);
        if (item == null) {
          continue;
        }
        stMonth = getMonth(beginTime);

        List<Map<String, Object>> itemList =
            kpiImpactBaseViewDao.getIPCountByItems(beginTime, endTime, keyType, insId, item);
        if (itemList != null && itemList.size() > 0) {
          for (Map temp : itemList) {
            String itemname = temp.get(item).toString();
            Long count = NumberUtils.parseLong(temp.get("count").toString(), 0L);
            KpiImpactExtendViewIP record =
                new KpiImpactExtendViewIP(stMonth, batch, calType, itemname, null, count, insId, keyType);
            kpiImpactExtendViewIPDao.save(record);
          }
        }
      }
    } catch (Exception e) {
      logger.error("SieImpactExtendTask: 分析访客数行为异常！insId：{} ;keyType:{} ;beginTime:{};endTime:{};calType:{}",
          new Object[] {insId, keyType, beginTime, endTime, item, e});
      throw new ServiceException("SieImpactExtendTask: 分析阅读数行为异常！insId：" + insId + " ;keyType:" + keyType
          + " ;beginTime:" + beginTime + " ;endTime:" + endTime + " ;calType:" + item, e);
    }
  }

  @Override
  public boolean checkRepeatWithAspects(Long insId, Date startDate, Date endDate) throws ServiceException {
    boolean flag = false; // 默认单位在扩展表不存在上月数据
    try {
      Long extendViewCount = kpiImpactExtendViewOneDao.getCountByDateTime(insId, startDate, endDate);
      Long extendShareCount = kpiImpactExtendShareOneDao.getCountByDateTime(insId, startDate, endDate);
      Long extendAwardCount = kpiImpactExtendAwardOneDao.getCountByDateTime(insId, startDate, endDate);
      Long extendDownLoadCount = kpiImpactExtendDownloadOneDao.getCountByDateTime(insId, startDate, endDate);
      Date beginTime = getFirstTimeOfMonth(-1, new Date());
      String stMonth = getMonth(beginTime);
      Long extendIpCount = kpiImpactExtendViewIPDao.getCountByDateTime(insId, stMonth);
      if (extendViewCount > 0 || extendShareCount > 0 || extendAwardCount > 0 || extendDownLoadCount > 0
          || extendIpCount > 0) {
        flag = true;
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return flag;
  }
}
