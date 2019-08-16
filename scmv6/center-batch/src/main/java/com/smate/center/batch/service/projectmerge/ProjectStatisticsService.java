package com.smate.center.batch.service.projectmerge;

import java.io.Serializable;

/**
 * 项目统计数service.
 * 
 * @author liqinghua
 * 
 */
public interface ProjectStatisticsService extends Serializable {

  /**
   * 清理项目所有发布年份统计数缓存.
   * 
   * @param psnId
   * @param pubYear
   */
  public void clearPrjYearStatistic(Long psnId);

  /**
   * 清理项目所有文件夹统计数缓存.
   * 
   * @param psnId
   */
  public void clearPrjFolderStatistic(Long psnId);

  /**
   * 清理项目所有群组统计数缓存.
   * 
   * @param psnId
   */
  public void clearPrjGroupStatistic(Long psnId);

  /**
   * 清理所有项目统计数.
   * 
   * @param psnId
   */
  public void clearAllPrjStatistic(Long psnId);

}
