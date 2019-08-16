package com.smate.center.task.quartz.pdwh;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.task.base.TaskAbstract;
import com.smate.center.task.service.pdwh.quartz.ShortUrlGeneratorSiteMapService;

/**
 * 基准库成果短地址生成sitemap文件任务
 * 
 * @author hht
 * @Time 2019年2月22日 下午3:27:29
 */
public class ShortUrlGeneratorSiteMapTask extends TaskAbstract {
  private final Logger logger = LoggerFactory.getLogger(getClass());
  private final static Integer SIZE = 50000; // 每次处理的个数
  @Autowired
  private ShortUrlGeneratorSiteMapService shortUrlGeneratorSiteMapService;

  public ShortUrlGeneratorSiteMapTask() {
    super();
  }

  public ShortUrlGeneratorSiteMapTask(String beanName) {
    super(beanName);
  }

  public void doRun() {

    if (!super.isAllowExecution()) {
      logger.info("=========ShortUrlGeneratorSiteMapTask已关闭==========");
      return;
    }
    // 关闭一次性任务
    Integer index = 0;
    while (true) {
      try {
        index = shortUrlGeneratorSiteMapService.generatorSiteMap(index, SIZE);
      } catch (Exception e) {
        logger.error("短地址生成sitemap任务出错", e);
      }
      if (index == 0) {
        return;// 终止
      }
      try {
        shortUrlGeneratorSiteMapService.writeIndexFile();
      } catch (Exception e) {
        logger.error("生成index文件出错", e);
      }
    }
  }
}
