package com.smate.center.batch.service.pub;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 成果、文献统计服务.
 * 
 * @author liqinghua
 * 
 */
public interface PublicationStatisticsService extends Serializable {

  /**
   * 清理成果所有类别统计数缓存.
   * 
   * @param psnId
   * @param articleType @
   */
  public void clearPubTypeStatistics(Long psnId, Integer articleType);

  /**
   * 清理成果所有发布年份统计数缓存.
   * 
   * @param psnId
   * @param pubYear @
   */
  public void clearPubYearStatistic(Long psnId, Integer articleType);

  /**
   * 清理成果所有文件夹统计数缓存.
   * 
   * @param psnId
   */
  public void clearPubFolderStatistic(Long psnId, Integer articleType);

  /**
   * 清理成果所有收录情况统计数缓存.
   * 
   * @param psnId
   */
  public void clearPubListStatistic(Long psnId, Integer articleType);

  /**
   * 清理成果所有群组统计数缓存.
   * 
   * @param psnId
   */
  public void clearPubGroupStatistic(Long psnId, Integer articleType);

  /**
   * 清理所有成果统计数.
   * 
   * @param psnId
   * @param articleType @
   */
  public void clearAllPubStatistic(Long psnId, Integer articleType);

  /**
   * 获取指定成果、文献类别统计数.
   * 
   * @param psnId
   * @param articleType
   * @param pubType
   * @return @
   */
  public Map<String, Integer> getPubTypeStatistics(Long psnId, Integer articleType, List<Integer> pubTypes);

  /**
   * 获取成果发表年度统计数.
   * 
   * @param psnId
   * @param articleType
   * @param pubyears
   * @return @
   */
  public Map<String, Integer> getPubYearStatistics(Long psnId, Integer articleType, List<Integer> pubyears);

  /**
   * 获取成果、文献文件夹统计数.
   * 
   * @param psnId
   * @param articleType
   * @param folderIds
   * @return @
   */
  public Map<String, Integer> getPubFolderStatistics(Long psnId, Integer articleType, List<Long> folderIds);

  /**
   * 获取成果、文献收录情况统计数.
   * 
   * @param psnId
   * @param articleType
   * @param pubLists
   * @return @
   */
  public Map<String, Integer> getPubListStatistics(Long psnId, Integer articleType, List<String> pubLists);

  /**
   * 获取成果、文献群组统计数.
   * 
   * @param psnId
   * @param articleType
   * @param folderIds
   * @return @
   */
  public Map<String, Integer> getPubGroupStatistics(Long psnId, Integer articleType, List<Long> groupIds);
}
