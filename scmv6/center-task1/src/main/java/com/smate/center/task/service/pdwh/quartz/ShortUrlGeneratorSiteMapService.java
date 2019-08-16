package com.smate.center.task.service.pdwh.quartz;

public interface ShortUrlGeneratorSiteMapService {

  /**
   * 生成sitemap文件
   * 
   * @param size
   * @throws Exception
   */
  Integer generatorSiteMap(Integer startIndex, Integer size) throws Exception;

  /**
   * 写入index文件
   * 
   * @throws Exception
   */
  void writeIndexFile() throws Exception;

}
