package com.smate.center.batch.service.pub;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.dao.sns.pub.PublicationDao;
import com.smate.center.batch.enums.pub.PublicationStatisticsEnum;

/**
 * 成果、文献统计服务.
 * 
 * @author liqinghua
 * 
 */
@Service("publicationStatisticsService")
@Transactional(rollbackFor = Exception.class)
public class PublicationStatisticsServiceImpl implements PublicationStatisticsService {

  /**
   * 
   */
  private static final long serialVersionUID = 8041405456366700264L;
  private static final String CACHE_KEY_PATTERN = "pubstatistics_%s_%s_%s";
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  // 统计数缓存
  private Ehcache statisticsCache;
  @Autowired
  private PublicationDao publicationDao;

  @Override
  public void clearPubTypeStatistics(Long psnId, Integer articleType) {

    this.removeCache(psnId, articleType, PublicationStatisticsEnum.PUB_TYPE);
  }

  @Override
  public void clearPubYearStatistic(Long psnId, Integer articleType) {
    this.removeCache(psnId, articleType, PublicationStatisticsEnum.PUB_YEAR);
  }

  @Override
  public void clearPubFolderStatistic(Long psnId, Integer articleType) {
    this.removeCache(psnId, articleType, PublicationStatisticsEnum.PUB_FOLDER);
  }

  @Override
  public void clearPubListStatistic(Long psnId, Integer articleType) {
    this.removeCache(psnId, articleType, PublicationStatisticsEnum.PUB_LIST);
  }

  @Override
  public void clearPubGroupStatistic(Long psnId, Integer articleType) {
    this.removeCache(psnId, articleType, PublicationStatisticsEnum.PUB_GROUP);
  }

  @Override
  public void clearAllPubStatistic(Long psnId, Integer articleType) {

    this.clearPubFolderStatistic(psnId, articleType);
    this.clearPubListStatistic(psnId, articleType);
    this.clearPubYearStatistic(psnId, articleType);
    this.clearPubTypeStatistics(psnId, articleType);
    this.clearPubGroupStatistic(psnId, articleType);
  }

  /**
   * 获取缓存KEY.
   * 
   * @param psnId
   * @param articleType
   * @param type
   * @return
   */
  private String getCacheKey(Long psnId, Integer articleType, int type) {

    return String.format(CACHE_KEY_PATTERN, psnId, articleType, type);
  }

  /**
   * 将统计数加入缓存.
   * 
   * @param psnId
   * @param folderId
   * @param count
   */
  @SuppressWarnings("rawtypes")
  private void putCache(Long psnId, Integer articleType, int type, Map map) {
    String cacheKey = getCacheKey(psnId, articleType, type);
    Element element = new Element(cacheKey, map);
    this.statisticsCache.put(element);
  }

  /**
   * 删除缓存.
   * 
   * @param psnId
   * @param articleType
   * @param type
   */
  private void removeCache(Long psnId, Integer articleType, int type) {
    String cacheKey = getCacheKey(psnId, articleType, type);
    if (statisticsCache == null) {
      logger.error("清除个人群组成果统计缓存出错，psnId = " + psnId);
      return;
    }
    this.statisticsCache.remove(cacheKey);

  }

  /**
   * 获取缓存数据.
   * 
   * @param psnId
   * @param folderId
   * @return
   */
  @SuppressWarnings({"rawtypes"})
  private Map getCache(Long psnId, Integer articleType, int type) {
    String cacheKey = getCacheKey(psnId, articleType, type);
    Element element = statisticsCache.get(cacheKey);
    if (element != null) {
      return (Map) element.getValue();
    } else {
      return null;
    }
  }

  @SuppressWarnings("unchecked")
  @Override
  public Map<String, Integer> getPubTypeStatistics(Long psnId, Integer articleType, List<Integer> pubTypes) {

    Map<String, Integer> map =
        (Map<String, Integer>) this.getCache(psnId, articleType, PublicationStatisticsEnum.PUB_TYPE);
    if (map == null) {
      map = new HashMap<String, Integer>();
    }
    try {
      for (Integer pubType : pubTypes) {
        if (map.get(pubType.toString()) == null) {
          int count = this.publicationDao.getPubTypeNum(pubType, articleType, psnId);
          map.put(pubType.toString(), count);
        }
      }
    } catch (Exception e) {
      logger.error("获取指定成果、文献类别统计数", e);
      throw e;
    }
    this.putCache(psnId, articleType, PublicationStatisticsEnum.PUB_TYPE, map);
    return (Map<String, Integer>) map;
  }

  @SuppressWarnings("unchecked")
  @Override
  public Map<String, Integer> getPubYearStatistics(Long psnId, Integer articleType, List<Integer> pubyears) {
    Map<String, Integer> map =
        (Map<String, Integer>) this.getCache(psnId, articleType, PublicationStatisticsEnum.PUB_YEAR);
    if (map == null) {
      map = new HashMap<String, Integer>();
    }
    try {
      for (Integer pubyear : pubyears) {
        if (map.get(pubyear.toString()) == null) {
          int count = 0;
          // 未归类年份
          if (pubyear == -1) {
            count = this.publicationDao.getNoPubYearNum(articleType, psnId);
          } else {
            count = this.publicationDao.getPubYearNum(pubyear, articleType, psnId);
          }
          map.put(pubyear.toString(), count);
        }
      }
    } catch (Exception e) {
      logger.error("获取成果发表年度统计数", e);
      throw e;
    }
    this.putCache(psnId, articleType, PublicationStatisticsEnum.PUB_YEAR, map);
    return (Map<String, Integer>) map;
  }

  @SuppressWarnings("unchecked")
  @Override
  public Map<String, Integer> getPubFolderStatistics(Long psnId, Integer articleType, List<Long> folderIds) {
    Map<String, Integer> map =
        (Map<String, Integer>) this.getCache(psnId, articleType, PublicationStatisticsEnum.PUB_FOLDER);
    if (map == null) {
      map = new HashMap<String, Integer>();
    }
    try {
      for (Long folderId : folderIds) {
        if (map.get(folderId.toString()) == null) {
          int count = 0;
          // 未归类文件夹
          if (folderId == -1) {
            count = this.publicationDao.getNoFolderPubNum(psnId, articleType);
          } else {
            count = this.publicationDao.getFolderPubNum(folderId);
          }
          map.put(folderId.toString(), count);
        }
      }
    } catch (Exception e) {
      logger.error("获取成果、文献文件夹统计数", e);
      throw e;
    }
    this.putCache(psnId, articleType, PublicationStatisticsEnum.PUB_FOLDER, map);
    return (Map<String, Integer>) map;
  }

  @SuppressWarnings("unchecked")
  @Override
  public Map<String, Integer> getPubListStatistics(Long psnId, Integer articleType, List<String> pubLists) {

    Map<String, Integer> map =
        (Map<String, Integer>) this.getCache(psnId, articleType, PublicationStatisticsEnum.PUB_LIST);
    if (map == null) {
      map = new HashMap<String, Integer>();
    }
    try {
      for (String pubList : pubLists) {
        if (map.get(pubList.toString()) == null) {
          int count = this.publicationDao.getPubListNum(pubList, articleType, psnId);
          map.put(pubList, count);
        }
      }
    } catch (Exception e) {
      logger.error("获取成果、文献收录情况统计数", e);
      throw e;
    }
    this.putCache(psnId, articleType, PublicationStatisticsEnum.PUB_LIST, map);
    return (Map<String, Integer>) map;
  }

  @SuppressWarnings("unchecked")
  @Override
  public Map<String, Integer> getPubGroupStatistics(Long psnId, Integer articleType, List<Long> groupIds) {
    Map<String, Integer> map =
        (Map<String, Integer>) this.getCache(psnId, articleType, PublicationStatisticsEnum.PUB_GROUP);
    if (map == null) {
      map = new HashMap<String, Integer>();
    }
    try {
      for (Long groupId : groupIds) {

        if (map.get(groupId.toString()) == null) {
          int count = this.publicationDao.getGroupPubNum(groupId, articleType, psnId);
          map.put(groupId.toString(), count);
        }

      }
    } catch (Exception e) {
      logger.error("获取成果、文献群组统计数", e);
      throw e;
    }
    this.putCache(psnId, articleType, PublicationStatisticsEnum.PUB_GROUP, map);
    return (Map<String, Integer>) map;
  }

  public void setStatisticsCache(Ehcache statisticsCache) {
    this.statisticsCache = statisticsCache;
  }

}
