package com.smate.center.task.quartz;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.task.base.TaskAbstract;
import com.smate.center.task.exception.SingleTaskException;
import com.smate.center.task.model.sns.quartz.GrpPubIndexUrl;
import com.smate.center.task.service.sns.quartz.ShortUrlInitService;
import com.smate.core.base.utils.constant.ShortUrlConst;

/**
 * SCM 短地址数据初始化任务
 * 
 * @author LJ
 *
 */
public class ShortUrlInitTask extends TaskAbstract {
  private final Logger logger = LoggerFactory.getLogger(getClass());
  private static int batchSize = 200;

  public ShortUrlInitTask() {
    super();
  }

  public ShortUrlInitTask(String beanName) {
    super(beanName);
  }

  @Autowired
  private ShortUrlInitService shortUrlInitService;

  public void doRun() throws SingleTaskException {

    if (!super.isAllowExecution()) {
      return;
    }
    logger.info("-----开始 短地址数据初始化任务----");
    this.insertInitData();
    this.startInitData();
  }

  /**
   * SCM-15804 任务自动 跑那些必须要短地址 但是没有产生短地址的数据
   */
  public void insertInitData() {
    shortUrlInitService.insertData();
  }

  public void startInitData() {

    List<Long> needInitId = null;
    // 初始化个人短地址
    while (true) {
      int index = 0;
      index++;// 分页取ID
      try {
        needInitId = shortUrlInitService.getNeedInitPsnId(index, batchSize);
        if (needInitId.size() == 0 || needInitId == null) {
          break;
        }
      } catch (Exception e) {
        logger.error("获取需要初始化的Psn短地址出错！", e);
      }
      this.initUrlData(needInitId, ShortUrlConst.P_TYPE);
    }
    // 初始化群组短地址
    while (true) {
      int index = 0;
      index++;// 分页取ID
      try {
        needInitId = shortUrlInitService.getNeedInitGroupId(index, batchSize);
        if (needInitId.size() == 0 || needInitId == null) {
          break;
        }
      } catch (Exception e) {
        logger.error("获取需要初始化的Grp短地址出错！", e);
      }
      this.initUrlData(needInitId, ShortUrlConst.G_TYPE);
    }
    // 初始化成果短地址S类型（基准库）
    while (true) {
      int index = 0;
      index++;// 分页取ID
      try {
        needInitId = shortUrlInitService.getNeedInitSPubId(index, batchSize);
        if (needInitId.size() == 0 || needInitId == null) {
          break;
        }
      } catch (Exception e) {
        logger.error("获取需要初始化的PDWHPub短地址出错！", e);
      }
      this.initUrlData(needInitId, ShortUrlConst.S_TYPE);

    }
    // 初始化成果短地址A类型（个人）
    while (true) {
      int index = 0;
      index++;// 分页取ID
      try {
        needInitId = shortUrlInitService.getNeedInitAPubId(index, batchSize);
        if (needInitId.size() == 0 || needInitId == null) {
          break;
        }
      } catch (Exception e) {
        logger.error("获取需要初始化的SNSPub短地址出错！", e);
      }
      this.initUrlData(needInitId, ShortUrlConst.A_TYPE);

    }
    // 初始化成果32位短地址A类型（个人）
    while (true) {
      int index = 0;
      index++;// 分页取ID
      try {
        needInitId = shortUrlInitService.getNeedInitATPubId(index, batchSize);
        if (needInitId.size() == 0 || needInitId == null) {
          break;
        }
      } catch (Exception e) {
        logger.error("获取需要初始化的SNSPub32位短地址出错！", e);
      }
      this.initUrlData(needInitId, ShortUrlConst.AT_TYPE);

    }
    // 初始化群组成果短地址B类型
    List<GrpPubIndexUrl> list = null;

    while (true) {
      int index = 0;
      index++;// 分页取ID
      try {
        list = shortUrlInitService.getNeedInitBPubId(index, batchSize);
        if (list.size() == 0 || list == null) {
          break;
        }
      } catch (Exception e) {
        logger.error("获取需要初始化的SNSPub短地址出错！", e);
      }
      this.initBpubUrlData(list, ShortUrlConst.B_TYPE);

    }

  }

  /**
   * 初始化短地址
   * 
   * @param needInitId
   * @param type
   */
  public void initBpubUrlData(List<GrpPubIndexUrl> list, String type) {
    for (GrpPubIndexUrl gpu : list) {
      try {
        shortUrlInitService.initUrlData(gpu.getGrpId(), gpu.getPubId(), type);

      } catch (Exception e) {
        logger.error("初始化群组成果短地址出错，type=" + type + "Id=" + gpu.getId(), e);
      }

    }
  }

  /**
   * 初始化短地址
   * 
   * @param needInitId
   * @param type
   */
  public void initUrlData(List<Long> needInitId, String type) {
    for (Long Id : needInitId) {
      try {
        shortUrlInitService.initUrlData(null, Id, type);

      } catch (Exception e) {
        logger.error("初始化短地址出错，type=" + type + "Id=" + Id, e);
      }

    }
  }

}
