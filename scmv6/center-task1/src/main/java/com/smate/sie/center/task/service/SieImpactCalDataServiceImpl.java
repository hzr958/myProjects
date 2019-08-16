package com.smate.sie.center.task.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
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
import com.smate.sie.core.base.utils.dao.statistics.KpiImpactExtendAwardTwoDao;
import com.smate.sie.core.base.utils.dao.statistics.KpiImpactExtendDownloadTwoDao;
import com.smate.sie.core.base.utils.dao.statistics.KpiImpactExtendShareTwoDao;
import com.smate.sie.core.base.utils.dao.statistics.KpiImpactExtendViewTwoDao;
import com.smate.sie.core.base.utils.model.statistics.ImpactConsts;
import com.smate.sie.core.base.utils.model.statistics.KpiImpactBaseAward;
import com.smate.sie.core.base.utils.model.statistics.KpiImpactBaseDownload;
import com.smate.sie.core.base.utils.model.statistics.KpiImpactBaseShare;
import com.smate.sie.core.base.utils.model.statistics.KpiImpactBaseView;
import com.smate.sie.core.base.utils.model.statistics.KpiImpactExtendAwardTwo;
import com.smate.sie.core.base.utils.model.statistics.KpiImpactExtendDownloadTwo;
import com.smate.sie.core.base.utils.model.statistics.KpiImpactExtendShareTwo;
import com.smate.sie.core.base.utils.model.statistics.KpiImpactExtendViewTwo;

/**
 * 单位分析，社交化数据拓展表2表服务
 * 
 * @author hd
 *
 */
@Service("sieImpactCalDataService")
@Transactional(rollbackFor = Exception.class)
public class SieImpactCalDataServiceImpl implements SieImpactCalDataService {
  Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private KpiImpactExtendViewTwoDao kpiImpactExtendViewTwoDao;
  @Autowired
  private KpiImpactExtendShareTwoDao kpiImpactExtendShareTwoDao;
  @Autowired
  private KpiImpactExtendDownloadTwoDao kpiImpactExtendDownloadTwoDao;
  @Autowired
  private KpiImpactExtendAwardTwoDao kpiImpactExtendAwardTwoDao;
  @Autowired
  private KpiImpactBaseViewDao kpiImpactBaseViewDao;
  @Autowired
  private KpiImpactBaseShareDao kpiImpactBaseShareDao;
  @Autowired
  private KpiImpactBaseDownloadDao kpiImpactBaseDownloadDao;
  @Autowired
  private KpiImpactBaseAwardDao kpiImpactBaseAwardDao;

  @SuppressWarnings("rawtypes")
  @Override
  public void calViewByData(Long insId, Date date) throws ServiceException {
    try {
      for (Integer keyType : ImpactConsts.DATA_TYPE_LIST) {
        if (keyType == null) {
          continue;
        }
        Date beginTime = getFirstTimeOfMonth(-1, date);
        Date endTime = getFirstTimeOfMonth(0, date);
        List<Map<String, Object>> itemList =
            kpiImpactBaseViewDao.getCountByDataAndMon(insId, keyType, beginTime, endTime);
        if (itemList != null && itemList.size() > 0) {
          for (Map temp : itemList) {
            Long keyCode = NumberUtils.parseLong(temp.get("item").toString(), null);
            Long count = NumberUtils.parseLong(temp.get("count").toString(), 0L);
            KpiImpactExtendViewTwo record = new KpiImpactExtendViewTwo(keyCode, keyType, count, insId);
            KpiImpactBaseView view =
                kpiImpactBaseViewDao.getByKeyCodeAndType(insId, keyType, keyCode, beginTime, endTime);
            if (view != null) {
              record.setTimeYear(view.getTiemYear());
              record.setTimeMon(view.getTimeMon());
              record.setTitle(view.getTitle());
            }
            if (record.getTimeYear() == null || record.getTimeMon() == null) {
              // 拆分记录时间
              Map<String, Integer> timemap = analyTime(beginTime);
              if (timemap != null) {
                record.setTimeYear(timemap.get(ImpactConsts.FIELD_YEAR));
                record.setTimeMon(timemap.get(ImpactConsts.FIELD_MONTH));
              }
            }
            record.setTime((record.getTimeYear() == null ? "" : record.getTimeYear().toString())
                + (record.getTimeMon() == null ? "" : String.format("%02d", record.getTimeMon())));
            kpiImpactExtendViewTwoDao.save(record);
          }
        }
      }

    } catch (Exception e) {
      logger.error("SieImpactExtendTask: KPI_IMPACT_EXTEND_VIEW2表，分析阅读数行为异常！insId：{} ;date:{} ",
          new Object[] {insId, date, e});
      throw new ServiceException(
          "SieImpactExtendTask: KPI_IMPACT_EXTEND_VIEW2表，分析阅读数行为异常！insId：" + insId + " ;date:" + date, e);
    }

  }

  @SuppressWarnings("rawtypes")
  @Override
  public void calAwardByData(Long insId, Date date) throws ServiceException {
    try {
      for (Integer keyType : ImpactConsts.DATA_TYPE_LIST) {
        if (keyType == null) {
          continue;
        }
        Date beginTime = getFirstTimeOfMonth(-1, date);
        Date endTime = getFirstTimeOfMonth(0, date);
        List<Map<String, Object>> itemList =
            kpiImpactBaseAwardDao.getCountByDataAndMon(insId, keyType, beginTime, endTime);
        if (itemList != null && itemList.size() > 0) {
          for (Map temp : itemList) {
            Long keyCode = NumberUtils.parseLong(temp.get("item").toString(), null);
            Long count = NumberUtils.parseLong(temp.get("count").toString(), 0L);
            KpiImpactExtendAwardTwo record = new KpiImpactExtendAwardTwo(keyCode, keyType, count, insId);
            KpiImpactBaseAward award =
                kpiImpactBaseAwardDao.getByKeyCodeAndType(insId, keyType, keyCode, beginTime, endTime);
            if (award != null) {
              record.setTimeYear(award.getTiemYear());
              record.setTimeMon(award.getTimeMon());
              record.setTitle(award.getTitle());
            }
            if (record.getTimeYear() == null || record.getTimeMon() == null) {
              // 拆分记录时间
              Map<String, Integer> timemap = analyTime(beginTime);
              if (timemap != null) {
                record.setTimeYear(timemap.get(ImpactConsts.FIELD_YEAR));
                record.setTimeMon(timemap.get(ImpactConsts.FIELD_MONTH));
              }
            }
            record.setTime((record.getTimeYear() == null ? "" : record.getTimeYear().toString())
                + (record.getTimeMon() == null ? "" : String.format("%02d", record.getTimeMon())));
            kpiImpactExtendAwardTwoDao.save(record);
          }
        }
      }

    } catch (Exception e) {
      logger.error("SieImpactExtendTask: KPI_IMPACT_EXTEND_AWARD2表，分析赞数行为异常！insId：{} ;date:{} ",
          new Object[] {insId, date, e});
      throw new ServiceException(
          "SieImpactExtendTask: KPI_IMPACT_EXTEND_AWARD2表，分析赞数行为异常！insId：" + insId + " ;date:" + date, e);
    }

  }

  @SuppressWarnings("rawtypes")
  @Override
  public void calDownloadByData(Long insId, Date date) throws ServiceException {
    try {
      for (Integer keyType : ImpactConsts.DATA_TYPE_LIST) {
        if (keyType == null) {
          continue;
        }
        Date beginTime = getFirstTimeOfMonth(-1, date);
        Date endTime = getFirstTimeOfMonth(0, date);
        List<Map<String, Object>> itemList =
            kpiImpactBaseDownloadDao.getCountByDataAndMon(insId, keyType, beginTime, endTime);
        if (itemList != null && itemList.size() > 0) {
          for (Map temp : itemList) {
            Long keyCode = NumberUtils.parseLong(temp.get("item").toString(), null);
            Long count = NumberUtils.parseLong(temp.get("count").toString(), 0L);
            KpiImpactExtendDownloadTwo record = new KpiImpactExtendDownloadTwo(keyCode, keyType, count, insId);
            KpiImpactBaseDownload download =
                kpiImpactBaseDownloadDao.getByKeyCodeAndType(insId, keyType, keyCode, beginTime, endTime);
            if (download != null) {
              record.setTimeYear(download.getTiemYear());
              record.setTimeMon(download.getTimeMon());
              record.setTitle(download.getTitle());
            }
            if (record.getTimeYear() == null || record.getTimeMon() == null) {
              // 拆分记录时间
              Map<String, Integer> timemap = analyTime(beginTime);
              if (timemap != null) {
                record.setTimeYear(timemap.get(ImpactConsts.FIELD_YEAR));
                record.setTimeMon(timemap.get(ImpactConsts.FIELD_MONTH));
              }
            }
            record.setTime((record.getTimeYear() == null ? "" : record.getTimeYear().toString())
                + (record.getTimeMon() == null ? "" : String.format("%02d", record.getTimeMon())));
            kpiImpactExtendDownloadTwoDao.save(record);
          }
        }
      }

    } catch (Exception e) {
      logger.error("SieImpactExtendTask: KPI_IMPACT_EXTEND_DOWNLOAD2表，分析下载数行为异常！insId：{} ;date:{} ",
          new Object[] {insId, date, e});
      throw new ServiceException(
          "SieImpactExtendTask: KPI_IMPACT_EXTEND_DOWNLOAD2表，分析下载数行为异常！insId：" + insId + " ;date:" + date, e);
    }

  }

  @SuppressWarnings("rawtypes")
  @Override
  public void calShareByData(Long insId, Date date) throws ServiceException {
    try {
      for (Integer keyType : ImpactConsts.DATA_TYPE_LIST) {
        if (keyType == null) {
          continue;
        }
        Date beginTime = getFirstTimeOfMonth(-1, date);
        Date endTime = getFirstTimeOfMonth(0, date);
        List<Map<String, Object>> itemList =
            kpiImpactBaseShareDao.getCountByDataAndMon(insId, keyType, beginTime, endTime);
        if (itemList != null && itemList.size() > 0) {
          for (Map temp : itemList) {
            Long keyCode = NumberUtils.parseLong(temp.get("item").toString(), null);
            Long count = NumberUtils.parseLong(temp.get("count").toString(), 0L);
            KpiImpactExtendShareTwo record = new KpiImpactExtendShareTwo(keyCode, keyType, count, insId);
            KpiImpactBaseShare share =
                kpiImpactBaseShareDao.getByKeyCodeAndType(insId, keyType, keyCode, beginTime, endTime);
            if (share != null) {
              record.setTimeYear(share.getTiemYear());
              record.setTimeMon(share.getTimeMon());
              record.setTitle(share.getTitle());
            }
            if (record.getTimeYear() == null || record.getTimeMon() == null) {
              // 拆分记录时间
              Map<String, Integer> timemap = analyTime(beginTime);
              if (timemap != null) {
                record.setTimeYear(timemap.get(ImpactConsts.FIELD_YEAR));
                record.setTimeMon(timemap.get(ImpactConsts.FIELD_MONTH));
              }
            }
            record.setTime((record.getTimeYear() == null ? "" : record.getTimeYear().toString())
                + (record.getTimeMon() == null ? "" : String.format("%02d", record.getTimeMon())));
            kpiImpactExtendShareTwoDao.save(record);
          }
        }
      }

    } catch (Exception e) {
      logger.error("SieImpactExtendTask: KPI_IMPACT_EXTEND_SHARE2表，分析分享数行为异常！insId：{} ;date:{} ",
          new Object[] {insId, date, e});
      throw new ServiceException(
          "SieImpactExtendTask: KPI_IMPACT_EXTEND_SHARE2表，分析分享数行为异常！insId：" + insId + " ;date:" + date, e);
    }

  }

  /**
   * 拆分出日期的年、月、日
   * 
   * @param dateRecords
   * @return
   * @throws ServiceException
   */
  private Map<String, Integer> analyTime(Date dateRecords) throws ServiceException {
    Map<String, Integer> m = null;
    try {
      if (dateRecords == null) {
        return null;
      }
      m = new HashMap<String, Integer>();
      Calendar c = Calendar.getInstance();
      c.setTime(dateRecords);
      m.put(ImpactConsts.FIELD_YEAR, c.get(Calendar.YEAR));
      m.put(ImpactConsts.FIELD_MONTH, c.get(Calendar.MONTH) + 1);
      m.put(ImpactConsts.FIELD_DAY, c.get(Calendar.DAY_OF_MONTH));
      return m;
    } catch (Exception e) {
      logger.error("SieImpactExtendTask任务拆分时间异常！time : {}", new Object[] {dateRecords, e});
      throw new ServiceException("SieImpactExtendTask任务拆分时间异常！time : " + dateRecords, e);
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

  @Override
  public void delTwoMonBeforeData(Long insId, Date date) throws ServiceException {
    try {
      Calendar c = getBeforeMonth(-2, date);
      if (c != null) {
        String time = String.valueOf(c.get(Calendar.YEAR)) + String.valueOf(c.get(Calendar.MONTH) + 1);
        kpiImpactExtendViewTwoDao.delTwoMonBeforeData(insId, time);
        kpiImpactExtendShareTwoDao.delTwoMonBeforeData(insId, time);
        kpiImpactExtendDownloadTwoDao.delTwoMonBeforeData(insId, time);
        kpiImpactExtendAwardTwoDao.delTwoMonBeforeData(insId, time);
      }

    } catch (Exception e) {
      logger.error("SieImpactExtendTask: 删除KPI_IMPACT_EXTEND_*2表两个月之前的数据异常！insId：{} ", new Object[] {insId, e});
      throw new ServiceException("SieImpactExtendTask: 删除KPI_IMPACT_EXTEND_*2表两个月之前的数据异常！insId：" + insId, e);
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
  public boolean checkRepeatWithData(Long insId) throws ServiceException {
    boolean flag = false;
    try {
      Date beginTime = getFirstTimeOfMonth(-1, new Date());
      String stMonth = getMonth(beginTime);
      Long extendViewCount = kpiImpactExtendViewTwoDao.getCountByDateTime(insId, stMonth);
      Long extendAwardCount = kpiImpactExtendAwardTwoDao.getCountByDateTime(insId, stMonth);
      Long extendDownLoadCount = kpiImpactExtendDownloadTwoDao.getCountByDateTime(insId, stMonth);
      Long extendShareCount = kpiImpactExtendShareTwoDao.getCountByDateTime(insId, stMonth);
      if (extendViewCount > 0 || extendAwardCount > 0 || extendDownLoadCount > 0 || extendShareCount > 0) {
        flag = true;
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return flag;
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

}
