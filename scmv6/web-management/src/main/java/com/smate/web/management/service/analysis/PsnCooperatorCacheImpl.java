package com.smate.web.management.service.analysis;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.smate.core.base.psn.dao.PersonProfileDao;
import com.smate.core.base.psn.dao.PsnStatisticsDao;
import com.smate.core.base.psn.model.PsnStatistics;
import com.smate.core.base.solr.service.SolrIndexService;
import com.smate.core.base.utils.cache.CacheService;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.model.Page;
import com.smate.core.base.utils.model.security.Person;
import com.smate.web.management.dao.analysis.sns.CooperatorMayRecommendDao;
import com.smate.web.management.dao.analysis.sns.KwRmcGroupDao;
import com.smate.web.management.dao.analysis.sns.PublicationDao;
import com.smate.web.management.dao.analysis.sns.PublicationQueryDao;
import com.smate.web.management.dao.analysis.sns.ReadStatisticsDao;
import com.smate.web.management.model.analysis.DaoException;
import com.smate.web.management.model.analysis.PsnCmdForm;
import com.smate.web.management.model.analysis.sns.CooperatorMayRecommend;
import com.smate.web.management.model.analysis.sns.RecommendScore;

/**
 * 基金、论文合作者推荐：查询缓存.
 * 
 * @author zhuangyanming
 * 
 */
@Service("psnCooperatorCache")
@Transactional(rollbackFor = Exception.class)
public class PsnCooperatorCacheImpl extends PsnCmdServiceUtil implements PsnCooperatorCache {
  protected Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private KwRmcGroupDao kwRmcGroupDao;

  @Autowired
  private CooperatorMayRecommendDao cooperatorMayRecommendDao;

  @Autowired
  private PersonProfileDao personProfileDao;

  @Autowired
  private CacheService cacheService;

  @Autowired
  private PsnCooperatorTraversal psnCooperatorTraversal;

  @Autowired
  private PsnCooperatorService psnCooperatorService;

  @Autowired
  private PersonManager personManager;

  @Autowired
  private ReadStatisticsDao readStatisticsDao;

  @Autowired
  private PublicationDao publicationDao;

  @Autowired
  private PublicationQueryDao publicationQueryDao;

  @Autowired
  private PsnStatisticsDao psnStatisticsDao;

  @Autowired
  private FriendService friendService;

  @Autowired
  private SolrIndexService solrIndexService;

  @Override
  public List<LinkedHashMap<String, Object>> getKeywordCountByYear(PsnCmdForm form) throws Exception {
    if (form != null && form.getKeywordJson() != null) {
      List<Map<String, String>> kwList = JacksonUtils.jsonListUnSerializer(form.getKeywordJson());
      if (kwList != null) {
        Calendar calendar = Calendar.getInstance();
        Integer thisYear = calendar.get(Calendar.YEAR);
        Assert.isTrue(thisYear > 2000);
        Integer[] yearList = {thisYear - 5, thisYear - 4, thisYear - 3, thisYear - 2, thisYear - 1};
        List<String> strList = this.kwSplit(kwList);

        if (strList != null && strList.size() > 0) {
          List<LinkedHashMap<String, Object>> rsList = new ArrayList<LinkedHashMap<String, Object>>();
          for (String kw : strList) {
            LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
            ArrayList<Long> countByYears = new ArrayList<Long>();
            for (Integer year : yearList) {
              Long count = solrIndexService.queryPubCount(kw, year);
              countByYears.add(count);
            }
            map.put("name", kw);
            map.put("type", "line");
            map.put("stack", kw);
            map.put("areaStyle", "{normal: {}}");
            map.put("data", countByYears);
            rsList.add(map);
          }
          return rsList;
        }
      }
    }

    return null;
  }

  @Override
  public List<String> getKeywordsList(PsnCmdForm form) throws Exception {
    if (form != null && form.getKeywordJson() != null) {
      List<Map<String, String>> kwList = JacksonUtils.jsonListUnSerializer(form.getKeywordJson());
      if (kwList != null) {
        List<String> strList = this.kwSplit(kwList);
        return strList;
      }
    }

    return null;
  }

  @Override
  public List<LinkedHashMap<String, String>> getKeywordCount(PsnCmdForm form) throws Exception {
    if (form != null && form.getKeywordJson() != null) {
      List<Map<String, String>> kwList = JacksonUtils.jsonListUnSerializer(form.getKeywordJson());
      if (kwList != null) {
        List<String> strList = this.kwSplit(kwList);
        if (strList != null && strList.size() > 0) {
          List<LinkedHashMap<String, String>> rsList = new ArrayList<LinkedHashMap<String, String>>();
          for (String kw : strList) {
            LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
            Long count = solrIndexService.queryPubCount(kw);
            map.put("value", String.valueOf(count == null ? 0L : count));
            map.put("name", kw);
            rsList.add(map);
          }
          return rsList;
        }
      }
    }

    return null;
  }

  // 基金、论文推荐合作者分页列表
  @SuppressWarnings("unchecked")
  @Override
  public Page<PsnCmdForm> getCooperatorByPage(PsnCmdForm form, Page<PsnCmdForm> page, Long psnId) throws Exception {

    String md5Key = SnsPageAndFormStringUtil.get(SnsServiceConstants.SNS_KEY_PSN_COOPERATOR, page, form, psnId);
    form.setPsnId(psnId);
    // Page pageCache = (Page)cacheService.get(
    // SnsServiceConstants.SNS_TYPE_PSN,md5Key);
    Page pageCache = null;
    if (pageCache != null) {
      return pageCache;
    } else {
      if (form != null && form.getKeywordJson() != null) {

        List<Map<String, String>> kwList = JacksonUtils.jsonListUnSerializer(form.getKeywordJson());

        if (kwList != null) {
          List<String> strList = this.kwSplit(kwList);
          if (strList != null && strList.size() > 0) {

            // 查询Hash
            String key = DigestUtils.shaHex(CACHE_NAME + psnId + ":" + StringUtils.join(strList, "###"));

            // List<PsnCmdForm> cmdList = (List<PsnCmdForm>) cacheService.get(CACHE_NAME, key);
            List<PsnCmdForm> cmdList = null;
            if (cmdList == null) {// 未缓存时，读取数据库，并加入缓存
              RecommendScore[] rsArr = this.match(strList, psnId);
              cmdList = this.render(rsArr, psnId);// 渲染结果集，最大30条数据一次取出
              if (cmdList != null) {
                cacheService.put(CACHE_NAME, EXP, key, (ArrayList<PsnCmdForm>) cmdList);
              }
            }

            if (cmdList != null) {
              // 每页10条控制
              page.setPageSize(EACH_FETCH_SIZE);
              page.setTotalCount(cmdList.size());

              Integer end = Math.min(page.getPageNo() * page.getPageSize(), cmdList.size());
              List<PsnCmdForm> arrayList = new ArrayList<PsnCmdForm>();
              // 数据不多，为简便：第１次返回10条，第2次返回20条，...
              if (end > 0) {
                arrayList.addAll(cmdList.subList(0, end));
              }
              page.setResult(arrayList);
            }
          }

        }
      }
      cacheService.put(SnsServiceConstants.SNS_TYPE_PSN, cacheService.EXP_MIN_30, md5Key, page);
    }
    return page;
  }

  /**
   * 根据计分结果，渲染结果集.
   * 
   * @param rsArr
   * @param psnId
   * @return
   * @throws ServiceException
   */
  private List<PsnCmdForm> render(RecommendScore[] rsArr, Long psnId) throws Exception {
    if (rsArr != null && rsArr.length > 0) {
      List<Long> psnIdList = new ArrayList<Long>();
      for (int i = 0; i < rsArr.length && i < MAX_FETCH_SIZE; i++) {
        psnIdList.add(rsArr[i].getCoPsnId());
      }
      try {
        List<Person> psnList = personProfileDao.queryCooperatorBatch(psnIdList);
        // List转Map
        Map<Long, Person> psnMap = new HashMap<Long, Person>();
        if (psnList != null && psnList.size() > 0) {
          for (Person person : psnList) {
            psnMap.put(person.getPersonId(), person);
          }
        }

        List<PsnCmdForm> cmdList = new ArrayList<PsnCmdForm>();
        // for (Long coPsnId : psnIdList) {
        // 临时拟定得分顺序，给推荐人计算星级，临时解决方案0.15,.0.4,0.3,0.15
        int length = psnIdList.size();
        int four = (int) (length * 0.15) + 1;
        int three = (int) (length * 0.4) + 1 + four;
        int two = (int) (length * 0.3) + 1 + three;

        for (int i = 1; i <= length; i++) {
          Long coPsnId = psnIdList.get(i - 1);
          Person person = psnMap.get(coPsnId);
          if (person == null)
            continue;
          // 修正增加了获取人员显示名称和头衔的逻辑_MJG_SCM-5707.
          String psnViewName =
              personManager.getPsnViewName(person.getName(), person.getFirstName(), person.getLastName());
          // String psnViewTitolo = personManager.getPsnViewTitolo(person);
          String psnViewTitolo = personManager.getPsnViewTitoloApplication(person);
          person.setViewName(psnViewName);
          person.setViewTitolo(psnViewTitolo);
          person.setTitolo(psnViewTitolo);
          if (length <= 2) {
            if (i == 1) {
              person.setScoreLevel(1);
            }
            if (i == 2) {
              person.setScoreLevel(2);
            }
          } else {
            if (i <= four) {
              person.setScoreLevel(1);
            } else if (i > four && i <= three) {
              person.setScoreLevel(2);
            } else if (i > three && i <= two) {
              person.setScoreLevel(3);
            } else {
              person.setScoreLevel(4);
            }
          }

          if (person != null && person.getPersonId() > 0) {
            // 填充人员基本推荐信息.
            PsnCmdForm psnCmd = this.getPsnCmdBaseInfo(person, psnId);
            cmdList.add(psnCmd);
          }
        }
        return cmdList;
      } catch (DaoException e) {
        logger.error("基金、论文合作者推荐批量获取合作者信息失败，person:psnIds={}", StringUtils.join(psnIdList, ","), e);
      }
    }
    return null;
  }

  public static void main(String[] args) {
    for (int length = 1; length < 31; length++) {
      int four = (int) (length * 0.15) + 1;
      int three = (int) (length * 0.4) + 1 + four;
      int two = (int) (length * 0.3) + 1 + three;
      System.out.println("length :" + length + "___" + four + "--" + three + "--" + two);

    }
  }

  // 界面拆分关键词与关键词/同义词/翻译词比较,
  // 表：scholar2.psn_kw_rmc_group(kw_txt,gid),scholar2.psn_kw_rmc_gid(psn_id,kw_gid)
  // 渲染合作者人员列表

  private RecommendScore[] match(List<String> strList, Long psnId) throws Exception {

    // select count(1) from psn_kw_rmc_gid t1 where t1.psn_id<>:1 and
    // t1.psn_id=:2 and t1.kw_gid in(:3);
    if (strList == null || strList.size() == 0) {// 无关键词时，不需要继续比较
      return null;
    }
    // 查找关键词分组id
    List<Long> gids = kwRmcGroupDao.getKwRmcGids(strList);
    try {

      psnCooperatorService.delTmpCoKwRecord(psnId);// 删除合作者数据(关键词临时记录)

      Map<Long, RecommendScore> rsPreMap = this.preMatch(psnId, gids);// 加载预先计算人员的总分，最多30人

      Set<Long> coPsnIds = rsPreMap == null || rsPreMap.size() == 0 ? null : rsPreMap.keySet();
      Integer minKw = this.minKwScore(rsPreMap);// 关键词匹配必须的最小分值

      int firstResult = 0;
      // 实时计算部分：计算关键词匹配上，且满足必要条件的人员（仅计算关键词匹配数量较多的人员）
      while (true) {

        Map<Long, RecommendScore> rsMap = cooperatorMayRecommendDao.findKwRecommendScoreList(psnId, gids, coPsnIds,
            minKw, firstResult, MAX_KWPSN_FETCH_SIZE);
        if (rsMap == null || rsMap.size() == 0) {
          break;
        }

        firstResult += MAX_KWPSN_FETCH_SIZE;

        boolean isNotNeedMore = psnCooperatorTraversal.psnCooperatorRun(psnId, rsMap);
        if (isNotNeedMore || rsMap.size() < MAX_KWPSN_FETCH_SIZE) {
          break;
        }
      }

      Map<Long, RecommendScore> rsNextMap = this.nextMatch(psnId);// 加载实时计算人员的总分

      Map<Long, RecommendScore> rsMap = new HashMap<Long, RecommendScore>();
      // 合并预先计算和实时计算的数据
      if (rsPreMap != null && rsPreMap.size() > 0) {
        rsMap.putAll(rsPreMap);
      }
      if (rsNextMap != null && rsNextMap.size() > 0) {
        rsMap.putAll(rsNextMap);
      }

      if (rsMap.size() > 0) {
        RecommendScore[] arr = new RecommendScore[rsMap.size()];
        // 转化为数组
        rsMap.values().toArray(arr);
        // 推荐分数排序
        Arrays.sort(arr);
        return arr;
      }
    } catch (DaoException e) {
      logger.error("基金、论文合作者推荐失败，psn_kw_rmc_group,psn_kw_rmc_gid:psnId={},kwTxts={}", psnId,
          StringUtils.join(strList, ","), e);
    }
    return null;

  }

  // 加载预先计算人员的总分
  private Map<Long, RecommendScore> preMatch(Long psnId, List<Long> gids) throws Exception {
    try {
      Map<Long, RecommendScore> rsMap = cooperatorMayRecommendDao.findRecommendScoreList(psnId, gids);
      if (rsMap == null) {
        return null;
      }
      Set<Long> coPsnIds = rsMap.keySet();
      // 加载可预先计算的推荐分数
      List<CooperatorMayRecommend> cmrList = cooperatorMayRecommendDao.findRecommendList(psnId, coPsnIds);
      if (cmrList != null) {
        for (CooperatorMayRecommend cmr : cmrList) {
          RecommendScore rs = rsMap.get(cmr.getCoPsnId());
          if (rs != null) {
            rs.setQualityScore(cmr.getCoQuality());
            rs.getDegreeScore().setFriendScore(cmr.getCoDegree());
            rs.getRelevanceScore().setDeptScore(cmr.getCoDept());
            rs.getRelevanceScore().setJnlScore(cmr.getCoJnl());
            rs.getRelevanceScore().setTaughtScore(cmr.getCoTaught());
          }
        }
      }
      return rsMap;
    } catch (DaoException e) {
      logger.error("可能合作者推荐加载预先计算人员的总分失败，cooperator_may_run:psn_id={}", psnId, e);
      throw new Exception(e);
    }

  }

  // 加载实时计算人员的总分
  private Map<Long, RecommendScore> nextMatch(Long psnId) throws Exception {
    try {
      Map<Long, RecommendScore> rsMap = cooperatorMayRecommendDao.findKwRecommendScoreList(psnId);
      if (rsMap == null) {
        return null;
      }
      Set<Long> coPsnIds = rsMap.keySet();
      // 加载可预先计算的推荐分数
      List<CooperatorMayRecommend> cmrList = cooperatorMayRecommendDao.findRecommendList(psnId, coPsnIds);
      if (cmrList != null) {
        for (CooperatorMayRecommend cmr : cmrList) {
          RecommendScore rs = rsMap.get(cmr.getCoPsnId());
          if (rs != null) {
            rs.setQualityScore(cmr.getCoQuality());
            rs.getDegreeScore().setFriendScore(cmr.getCoDegree());
            rs.getRelevanceScore().setDeptScore(cmr.getCoDept());
            rs.getRelevanceScore().setJnlScore(cmr.getCoJnl());
            rs.getRelevanceScore().setTaughtScore(cmr.getCoTaught());
            rs.getRelevanceScore().setKwScore(cmr.getTmpCoKw());
          }
        }
      }
      return rsMap;
    } catch (DaoException e) {
      logger.error("可能合作者推荐加载实时计算人员的总分失败，cooperator_may_run:psn_id={}", psnId, e);
      throw new Exception(e);
    }

  }

  // 关键词匹配必须的最小分值(可能带来误差)
  private Integer minKwScore(Map<Long, RecommendScore> rsMap) {
    if (rsMap == null || rsMap.size() == 0) {
      return 0;
    }
    Double minKw = Double.MAX_VALUE;
    for (RecommendScore rs : rsMap.values()) {
      minKw = Math.min(minKw, rs.getTotalScore() / PsnCooperatorCache.MAX_CONST_VAL);
    }

    return minKw.intValue();

  }

  // 抽取关键词字符串列表
  private List<String> kwSplit(List<Map<String, String>> kwList) {
    if (kwList != null) {
      List<String> strList = new ArrayList<String>();
      for (int i = 0; i < kwList.size(); i++) {
        if (strList.size() == MAX_KW) {
          break;
        }
        String kwStr = kwList.get(i).get("keyword");
        if (kwStr != null) {
          strList.add(kwStr.trim().toLowerCase());
        }
      }
      return strList;
    }
    return null;
  }

  /**
   * 填充人员基本推荐信息.
   * 
   * @param person
   * @param currentPsnId
   * @return
   * @throws ServiceException
   */
  protected PsnCmdForm getPsnCmdBaseInfo(Person person, Long currentPsnId) throws Exception {
    return this.getPsnCmdBaseInfo(person, currentPsnId, null);
  }

  /**
   * 填充人员基本推荐信息.
   * 
   * @param person
   * @param currentPsnId
   * @param insId
   * @return
   * @throws ServiceException
   */
  protected PsnCmdForm getPsnCmdBaseInfo(Person person, Long currentPsnId, Long insId) throws Exception {
    PsnCmdForm psnCmd = null;
    try {
      psnCmd = new PsnCmdForm();
      Long psnId = person.getPersonId();
      psnCmd.setPsnId(psnId);
      psnCmd.setName(person.getName());
      psnCmd.setFirstName(person.getFirstName());
      psnCmd.setLastName(person.getLastName());
      // 修正了人员显示名称，头衔_MJG_SCM-5707.
      psnCmd.setPsnViewName(person.getViewName());
      psnCmd.setTitolo(person.getViewTitolo());
      psnCmd.setInsName(person.getInsName());
      psnCmd.setAvatars(person.getAvatars());
      // 判断是否好友
      boolean isFriend = this.friendService.isPsnFirend(psnId, currentPsnId);
      psnCmd.setIsFriend(isFriend ? 1 : 0);
      psnCmd.setReadCount(readStatisticsDao.countReadByPsnId(psnId, DynamicConstant.RES_TYPE_PUB));
      psnCmd.setPubCount(publicationDao.getTotalPubsByPsnId(psnId));
      psnCmd.setCiteCount(publicationQueryDao.queryPubsCiteTimesByPsnId(psnId));
      // 添加H index指数
      PsnStatistics ps = psnStatisticsDao.get(psnId);
      psnCmd.sethIndex(ps == null ? 0 : ps.getHindex());
      // 添加sorcelevel评分
      psnCmd.setScoreLevel(person.getScoreLevel() == null ? 1 : person.getScoreLevel());// 默认为1

    } catch (DaoException e) {
      logger.error("获取人员基本推荐信息出现异常psnId=" + person.getPersonId(), e);
    }
    return psnCmd;
  }
}
