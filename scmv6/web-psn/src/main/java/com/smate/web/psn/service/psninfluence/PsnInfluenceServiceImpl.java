package com.smate.web.psn.service.psninfluence;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.consts.dao.ConstRegionDao;
import com.smate.core.base.consts.dao.InstitutionDao;
import com.smate.core.base.consts.model.ConstRegion;
import com.smate.core.base.consts.model.Institution;
import com.smate.core.base.psn.dao.PsnStatisticsDao;
import com.smate.core.base.psn.model.PsnStatistics;
import com.smate.core.base.pub.dao.PubSnsPublicDAO;
import com.smate.core.base.pub.po.dao.PubPdwhSnsRelationDAO;
import com.smate.core.base.statistics.dao.DownloadCollectStatisticsDao;
import com.smate.core.base.utils.common.CommonUtils;
import com.smate.core.base.utils.common.LocaleTextUtils;
import com.smate.web.psn.dao.project.ProjectStatisticsDao;
import com.smate.web.psn.dao.pub.PublicationDao;
import com.smate.web.psn.dao.statistics.AwardStatisticsDao;
import com.smate.web.psn.dao.statistics.ShareStatisticsDao;
import com.smate.web.psn.dao.statistics.VistStatisticsDao;
import com.smate.web.psn.exception.PsnException;
import com.smate.web.psn.exception.ServiceException;
import com.smate.web.psn.model.influence.InfluenceForm;
import com.smate.web.psn.model.influence.PsnVisitLineInfo;
import com.smate.web.psn.model.influence.VisitMapData;
import com.smate.web.psn.v8pub.dao.pdwh.pub.PubPdwhPoDao;
import com.smate.web.psn.v8pub.dao.sns.pub.PubShareDAO;

@Service("psnInfluenceService")
@Transactional(rollbackFor = Exception.class)
public class PsnInfluenceServiceImpl implements PsnInfluenceService {

  protected Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private PsnStatisticsDao psnStatisticsDao;
  @Autowired
  private DownloadCollectStatisticsDao downloadCollectStatisticsDao;
  @Autowired
  private AwardStatisticsDao awardStatisticsDao;
  @Autowired
  private ShareStatisticsDao shareStatisticsDao;
  @Autowired
  private VistStatisticsDao vistStatisticsDao;
  @Autowired
  private PublicationDao publicationDao;
  @Autowired
  private InstitutionDao institutionDao;
  @Autowired
  private ConstRegionDao constRegionDao;
  @Autowired
  private PubSnsPublicDAO pubSnsPublicDAO;
  @Autowired
  private PubPdwhSnsRelationDAO pubPdwhSnsRelationDAO;
  @Autowired
  private PubPdwhPoDao pubPdwhPoDao;
  @Autowired
  private PubShareDAO pubShareDAO;
  @Autowired
  private ProjectStatisticsDao projectStatisticsDao;

  @Override
  public InfluenceForm findPsnStatistics(InfluenceForm form) throws PsnException {
    try {
      Long psnId = form.getPsnId();
      // 从psn_statistics表获取hindex、引用数、访问数
      PsnStatistics sta = psnStatisticsDao.getPsnStatisticsForInfluence(psnId);
      Long pubAwardSum = 0L;
      if (sta != null) {
        form.setCitedSum(sta.getCitedSum() != null ? sta.getCitedSum() : 0);
        form.setHindex(sta.getHindex() != null ? sta.getHindex() : 0);
        form.setVisitSum(sta.getVisitSum() != null ? sta.getVisitSum() : 0);
        form.setFriendSum(sta.getFrdSum() != null ? sta.getFrdSum() : 0);
        pubAwardSum = sta.getPubAwardSum() != null ? sta.getPubAwardSum() : 0L;
      }
      // 下载数
      Long downloadSum = downloadCollectStatisticsDao.countPsnResourceDownload(psnId);
      form.setDownLoadSum(downloadSum == null ? 0 : downloadSum);
      // 赞统计数
      Long awardSum = awardStatisticsDao.countPsnAwardSum(psnId);
      // 分享统计数
      Long shareSum = shareStatisticsDao.countPsnShareSum(psnId);
      Long pubShareSum = pubShareDAO.countPsnPubShareSum(psnId);
      Long prjShareSum = projectStatisticsDao.countPsnPrjShareSum(psnId);
      form.setAwardAndShareSum(awardSum + shareSum + pubAwardSum + pubShareSum + prjShareSum);
    } catch (Exception e) {
      logger.error("获取人员影响力页面统计数据出错， PsnId = " + form.getPsnId(), e);
      throw new PsnException(e);
    }
    return form;
  }

  @Override
  public InfluenceForm findPsnResReadTrend(InfluenceForm form) throws PsnException {
    try {
      // 按周统计阅读数
      List<Map<String, Object>> visitInfo = vistStatisticsDao.findPsnVisitGroupByWeek(form.getPsnId());
      // 查询最近7周的时间点
      List timeInfo = vistStatisticsDao.findVisitTrendTime();
      StringBuffer xAxisData = new StringBuffer();
      StringBuffer yAxisData = new StringBuffer();
      Long maxCount = 0L;
      for (int i = 0; i < timeInfo.size(); i++) {
        Long count = 0L;
        String time = timeInfo.get(i).toString();
        for (int j = 0; j < visitInfo.size(); j++) {
          if (time.equals(visitInfo.get(j).get("WEEKTIME").toString())) {
            count = NumberUtils.toLong(visitInfo.get(j).get("VISITSUM").toString());
          }
        }
        xAxisData.append(time.substring(0, 10));
        yAxisData.append(count);
        if (count > maxCount) {
          maxCount = count;
        }
        if (i < timeInfo.size() - 1) {
          xAxisData.append(",");
          yAxisData.append(",");
        }
      }
      // 获取X轴和Y轴数据，供画图用
      form.setXAxisData(xAxisData.toString());
      form.setYAxisData(yAxisData.toString());
      form.setMaxVisitCount(maxCount);
    } catch (Exception e) {
      logger.error("获取人员影响力页面阅读趋势数据出错， PsnId = " + form.getPsnId(), e);
      throw new PsnException(e);
    }
    return form;
  }

  @Override
  public InfluenceForm findPsnHindexInfo(InfluenceForm form) throws PsnException {
    try {
      // 先获取人员H-index值---可以从前台传过来
      Integer hindex = form.getHindex();
      if (hindex != null && hindex.intValue() == 0) {
        Map<String, Object> hindexMap = pubSnsPublicDAO.findPsnHindex(form.getPsnId());
        hindex = NumberUtils.toInt(hindexMap.get("HINDEX").toString());
        form.setHindex(hindex);
      }
      // 再从H-index处往后找10条成果，往前找所有的成果引用信息用来画图
      List<Integer> pubCitedInfo = new ArrayList<Integer>();
      pubCitedInfo = pubSnsPublicDAO.findPubCitedInfo(form.getPsnId(), hindex + 10);
      form.setPubCitedInfo(pubCitedInfo);
      // 构建X、Y轴数据
      StringBuffer xAxisData = new StringBuffer();
      StringBuffer yAxisData = new StringBuffer();
      for (int i = 0; !pubCitedInfo.isEmpty() && i < pubCitedInfo.size(); i++) {
        xAxisData.append(i + 1);
        yAxisData.append(pubCitedInfo.get(i));
        if (i < pubCitedInfo.size() - 1) {
          xAxisData.append(",");
          yAxisData.append(",");
        }
      }
      if (hindex > 0) {
        form.setyHindex(pubCitedInfo.get(hindex - 1));
        form.setxHinex(hindex - 1);
      }
      form.setXAxisData(xAxisData.toString());
      form.setYAxisData(yAxisData.toString());
    } catch (Exception e) {
      logger.error("获取人员影响力页面Hindex图数据出错， PsnId = " + form.getPsnId(), e);
      throw new PsnException(e);
    }
    return form;
  }

  /**
   * 获取X轴和Y轴数据
   * 
   * @param visitInfo
   * @param axis
   * @return
   */
  private String getLineTrendXandYInfo(List<PsnVisitLineInfo> visitInfo, String axis) {
    StringBuffer data = new StringBuffer();
    if ("X".equals(axis)) {
      for (PsnVisitLineInfo info : visitInfo) {
        data.append(info.getCountDate() + ",");
      }
    }
    if ("Y".equals(axis)) {
      for (PsnVisitLineInfo info : visitInfo) {
        data.append(info.getVisitSum() + ",");
      }
    }
    if (data.length() > 0) {
      data.substring(1);
    }
    return data.toString();
  }

  @Override
  public Map<String, Object> getVisitMapData(Long psnId, Map<String, Object> resultMap) throws ServiceException {
    try {
      List<VisitMapData> vistMapList = new ArrayList<VisitMapData>();
      List<Object> resList = vistStatisticsDao.getVistNumByPsnId(psnId);// 获取阅读省份统计数
      List<ConstRegion> chinaRegion = constRegionDao.getChinaRegion(false);
      Long maxNum = 0L;
      if (resList != null) {
        VisitMapData visit = null;
        for (Iterator iterator = resList.iterator(); iterator.hasNext();) {
          boolean isSearch = false;
          visit = new VisitMapData();
          Object[] rows = (Object[]) iterator.next();
          Long regionId = NumberUtils.toLong(Objects.toString(rows[0]));
          String zhName = (String) rows[1];
          String enName = (String) rows[2];
          Long visitCount = NumberUtils.toLong(Objects.toString(rows[3]));
          ConstRegion region = constRegionDao.findSuperRegionById(regionId);
          if (region != null && !"156".equals(Objects.toString(region.getId()))) {
            regionId = region.getId();
            zhName = region.getZhName();
            enName = region.getEnName();
          }
          for (VisitMapData visitData : vistMapList) {// 省下的地区统计到省
            if (visitData.getProvinceName().equals(zhName)) {
              isSearch = true;
              visitCount = visitData.getVisiCount() + visitCount;
              visitData.setVisiCount(visitCount);
            }
          }
          if (!isSearch) {// 不是省下的地区按原先的添加
            visit.setProvinceName(zhName);
            visit.setProvinceEnName(enName);
            visit.setVisiCount(visitCount);
            vistMapList.add(visit);
          }
          if (visitCount > maxNum) {// 阅读最多的省份
            maxNum = visitCount;
          }
        }
      }
      resultMap.put("maxNum", maxNum);// 阅读最多的省份
      resultMap.put("dataList", vistMapList);
      resultMap.put("chinaRegion", chinaRegion);// 用于国际化
    } catch (Exception e) {
      logger.error("获取影响力阅读人数记录出错，psnId=" + psnId, e);
      throw new ServiceException(e);
    }
    return resultMap;
  }

  @Override
  public void getVisitIns(InfluenceForm form) throws ServiceException {
    List<Map<String, String>> visitInsList = new ArrayList<Map<String, String>>();
    try {
      List<Map<String, Object>> resList = vistStatisticsDao.getVisitIns(form.getPsnId());// 获取阅读省份统计数
      Map<String, String> itemMap = null;
      Locale locale = LocaleContextHolder.getLocale();
      if (resList != null) {
        for (Map item : resList) {
          itemMap = new HashMap<String, String>();
          String insId = Objects.toString(item.get("INSID"));
          Institution ins = institutionDao.findInsName(NumberUtils.toLong(insId));// 获取单位中英文名称
          if (ins != null) {
            String count = Objects.toString(item.get("COUNT"));
            itemMap.put("name", LocaleTextUtils.getLocaleText(locale, ins.getZhName(), ins.getEnName()));
            itemMap.put("num", count);
            visitInsList.add(itemMap);
          }
        }
        if (visitInsList != null && visitInsList.size() > 4) {
          visitInsList = visitInsList.subList(0, 4);
        }
        form.setVisitInsList(visitInsList);
      }
    } catch (Exception e) {
      logger.error("获取影响力单位分布记录记录出错，psnId=" + form.getPsnId(), e);
      throw new ServiceException(e);
    }

  }

  @Override
  public void getVisitPos(InfluenceForm form) throws ServiceException {
    List<Map<String, String>> visitPosList = new ArrayList<Map<String, String>>();
    try {
      List<Map<String, Object>> resList = vistStatisticsDao.getVisitPos(form.getPsnId());// 获取阅读省份统计数
      Map<String, String> itemMap = null;
      Long otherNum = 0L;
      Locale locale = LocaleContextHolder.getLocale();
      if (resList != null) {
        int primaryCount = 0;
        int seniorCount = 0;
        int intermediateCount = 0;
        for (Map item : resList) {
          String grade = Objects.toString(item.get("GRADE"));
          String count = Objects.toString(item.get("COUNT"));
          if (StringUtils.isNotBlank(count) && !"0".equals(count)) {
            if (grade.contains("1") || grade.contains("2")) {
              primaryCount += NumberUtils.toInt(count, 0);
            } else if (grade.contains("3")) {
              intermediateCount += NumberUtils.toInt(count, 0);
            } else if (grade.contains("4")) {
              seniorCount += NumberUtils.toInt(count, 0);
            } else {
              otherNum += NumberUtils.toLong(count);
            }
          }
        }
        if (primaryCount > 0) {
          itemMap = new HashMap<String, String>();
          itemMap.put("name", LocaleTextUtils.getLocaleText(locale, "高级", "Primary"));
          itemMap.put("num", Objects.toString(primaryCount, "0"));
          visitPosList.add(itemMap);
        }
        if (intermediateCount > 0) {
          itemMap = new HashMap<String, String>();
          itemMap.put("name", LocaleTextUtils.getLocaleText(locale, "中级", "Intermediate"));
          itemMap.put("num", Objects.toString(intermediateCount, "0"));
          visitPosList.add(itemMap);
        }
        if (seniorCount > 0) {
          itemMap = new HashMap<String, String>();
          itemMap.put("name", LocaleTextUtils.getLocaleText(locale, "初级", "Senior"));
          itemMap.put("num", Objects.toString(seniorCount, "0"));
          visitPosList.add(itemMap);
        }
        if (otherNum > 0) {
          itemMap = new HashMap<String, String>();
          itemMap.put("name", LocaleTextUtils.getLocaleText(locale, "其他", "Other"));
          itemMap.put("num", otherNum.toString());
          visitPosList.add(itemMap);
        }
      }
      form.setVisitPosList(visitPosList);
    } catch (Exception e) {
      logger.error("获取影响力职称分布记录记录出错，psnId=" + form.getPsnId(), e);
      throw new ServiceException(e);
    }
  }

  @Override
  public InfluenceForm findPsnHindexRanking(InfluenceForm form) throws PsnException {
    try {
      // 从psn_statistics表获取H-index
      PsnStatistics sta = psnStatisticsDao.getPsnStatisticsForInfluence(form.getPsnId());
      if (sta != null) {
        form.setHindex(sta.getHindex() != null ? sta.getHindex() : 0);
      }
      Long hindexRanking = psnStatisticsDao.findPsnHindexRanking(form.getPsnId(), form.getHindex());
      form.setHindexRanking(hindexRanking + 1);
    } catch (Exception e) {
      logger.error("获取人员hindex在好友中的排名出错， PsnId = " + form.getPsnId(), e);
      throw new PsnException(e);
    }
    return null;
  }

  @Override
  public InfluenceForm findPsnVisitSumRanking(InfluenceForm form) throws PsnException {
    try {
      // 从psn_statistics表获取访问数
      PsnStatistics sta = psnStatisticsDao.getPsnStatisticsForInfluence(form.getPsnId());
      if (sta != null) {
        form.setVisitSum(sta.getVisitSum() != null ? sta.getVisitSum() : 0);
      }
      Long visitSumRanking = psnStatisticsDao.findPsnVisitSumRanking(form.getPsnId(), form.getVisitSum());
      form.setVisitSumRanking(visitSumRanking + 1);
    } catch (Exception e) {
      logger.error("获取人员访问数在好友中的排名出错， PsnId = " + form.getPsnId(), e);
      throw new PsnException(e);
    }
    return form;
  }

  @Override
  public void findPubCiteTrend(InfluenceForm form) throws PsnException {
    List<Map<String, Object>> citeTrendList = new ArrayList<>();
    buildRank(form);// 构建总引用次数,好友总数,排名
    List<Long> pdwhPubIds = pubPdwhSnsRelationDAO.findPdwhPubIdByPsnId(form.getPsnId());
    Long yMaxVal = 0L;
    Long yMinVal = 0L;
    List<Map<String, Object>> pubCitedMap = pubPdwhPoDao.findPubCite(pdwhPubIds);
    // 构建测试数据
    // pubCitedMap = buildTestInfo();
    Calendar now = Calendar.getInstance();
    int currentYear = now.get(Calendar.YEAR);
    StringBuilder xAxisData = new StringBuilder();
    StringBuilder yAxisData = new StringBuilder();
    if (CollectionUtils.isNotEmpty(pubCitedMap)) {
      for (int i = currentYear - 7; i <= currentYear; i++) {
        Long count = 0L;
        for (Map<String, Object> pubCitedItem : pubCitedMap) {
          Integer year = (Integer) pubCitedItem.get("pubYear");
          if (year != null && year.intValue() <= i) {
            count += (Long) pubCitedItem.get("count");
          }
        }
        if (count > 0) {
          xAxisData.append(i + ",");
          yAxisData.append(count + ",");
        }
        // 第一个年份count值是最小值
        if (i == currentYear - 7) {
          yMinVal = count;
        }
        // 最后一个年份count值是最大值
        if (i == currentYear) {
          yMaxVal = count;
        }
      }
      xAxisData.deleteCharAt(xAxisData.length() - 1);
      yAxisData.deleteCharAt(yAxisData.length() - 1);
    }
    if (StringUtils.isBlank(xAxisData.toString())) {
      form.setHasCiteThead("no");
    }
    form.setXAxisData(xAxisData.toString());
    form.setYAxisData(yAxisData.toString());
    form.setyMaxVal(yMaxVal);
    form.setyMinVal(yMinVal);
  }

  /**
   * 构建测试数据
   * 
   * @return
   */
  protected List<Map<String, Object>> buildTestInfo() {
    List<Map<String, Object>> pubCitedMapList = new ArrayList<Map<String, Object>>();
    Integer[] arr = new Integer[] {1997, 1998, 1999, 2000, 2001, 2004, 2005, 2006, 2007, 2008, 2009, 2010, 2011, 2012,
        2013, 2014, 2015, 2016};
    Long[] countArr = new Long[] {15L, 30L, 24L, 242L, 1L, 25L, 25L, 50L, 247L, 11L, 173L, 15L, 273L, 174L, 157L, 17L,
        107L, 28L, 10L};
    for (int i = 0; i < arr.length; i++) {
      Map<String, Object> item = new HashMap<String, Object>();
      item.put("pubYear", arr[i]);
      item.put("count", countArr[i]);
      pubCitedMapList.add(item);
    }
    return pubCitedMapList;
  }

  protected void buildRank(InfluenceForm form) {
    PsnStatistics ps = psnStatisticsDao.get(form.getPsnId());
    if (ps != null) {
      form.setCitedSum(ps.getCitedSum());// 总引用次数
      form.setFriendSum(ps.getFrdSum());// 好友总数
      Long citeRank = psnStatisticsDao.findCiteSumRank(form.getPsnId(), form.getCitedSum());
      citeRank = citeRank == null ? 0L : citeRank;
      form.setCiteRank(citeRank.intValue() + 1);
    }
  }

  @Override
  public void updatePsnHindex(InfluenceForm form) throws PsnException {
    try {
      PsnStatistics pst = psnStatisticsDao.get(form.getPsnId());
      Map<String, Object> hindexMap = publicationDao.findPsnHindex(form.getPsnId());
      form.setHindex(NumberUtils.toInt(hindexMap.get("HINDEX").toString()));
      if (pst != null && !CommonUtils.compareIntegerValue(pst.getHindex(), form.getHindex())) {
        pst.setHindex(form.getHindex());
        psnStatisticsDao.save(pst);
      }
    } catch (Exception e) {
      logger.error("检查更新人员H-index值出错", e);
      throw new PsnException(e);
    }
  }
}
