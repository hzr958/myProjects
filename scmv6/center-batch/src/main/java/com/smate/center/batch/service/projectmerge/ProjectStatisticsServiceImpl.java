package com.smate.center.batch.service.projectmerge;

import net.sf.ehcache.Ehcache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.oldXml.prj.ProjectStatisticsEnum;

/**
 * 项目统计数service.
 * 
 * @author liqinghua
 * 
 */
@Service("projectStatisticsService")
@Transactional(rollbackFor = Exception.class)
public class ProjectStatisticsServiceImpl implements ProjectStatisticsService {

  private static final long serialVersionUID = -2240251352104608196L;
  private static final String CACHE_KEY_PATTERN = "prjstatistics_%s_%s";
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  // 统计数缓存
  private Ehcache statisticsCache;

  @Override
  public void clearPrjYearStatistic(Long psnId) {
    this.removeCache(psnId, ProjectStatisticsEnum.PRJ_YEAR);
  }

  @Override
  public void clearPrjFolderStatistic(Long psnId) {
    this.removeCache(psnId, ProjectStatisticsEnum.PRJ_FOLDER);
  }

  @Override
  public void clearPrjGroupStatistic(Long psnId) {
    this.removeCache(psnId, ProjectStatisticsEnum.PRJ_GROUP);
  }

  @Override
  public void clearAllPrjStatistic(Long psnId) {

    this.clearPrjYearStatistic(psnId);
    this.clearPrjFolderStatistic(psnId);
    this.clearPrjGroupStatistic(psnId);
  }

  /**
   * 获取缓存KEY.
   * 
   * @param psnId
   * @param articleType
   * @param type
   * @return
   */
  private String getCacheKey(Long psnId, int type) {
    return String.format(CACHE_KEY_PATTERN, psnId, type);
  }

  /**
   * 删除缓存.
   * 
   * @param psnId
   * @param articleType
   * @param type
   */
  private void removeCache(Long psnId, int type) {
    String cacheKey = getCacheKey(psnId, type);
    this.statisticsCache.remove(cacheKey);
  }



}
