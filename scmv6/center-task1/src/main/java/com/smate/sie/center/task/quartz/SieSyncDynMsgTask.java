package com.smate.sie.center.task.quartz;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.task.base.TaskAbstract;
import com.smate.sie.center.task.service.SieSynSnsDynMsgService;

/**
 * 同步Sns动态信息到Sie库中
 * 
 * @author 叶星源
 * @Date 201902
 */
public class SieSyncDynMsgTask extends TaskAbstract {

  private Logger logger = LoggerFactory.getLogger(getClass());
  private static final long BATCH_SIZE = 100;// 一次最多更新数量

  @Autowired
  private SieSynSnsDynMsgService sieSynSnsDynMsgService;

  public SieSyncDynMsgTask() {
    super();
  }

  public SieSyncDynMsgTask(String beanName) {
    super(beanName);
  }

  public void doRun() throws Exception {
    if (!super.isAllowExecution()) {
      return;
    }
    try {
      Map<String, Long> paramMap = new HashMap<String, Long>();
      paramMap.put("first", 1L);
      paramMap.put("last", BATCH_SIZE);
      theRecursiveMothed(paramMap);
    } catch (Exception e) {
      logger.error("同步Sns动态信息到Sie库中任务运行异常：", e);
    }
  }

  // 递归调用业务功能
  private Map<String, Long> theRecursiveMothed(Map<String, Long> paramMap) {
    // 结束条件一：任务已关闭,或参数为空
    if (paramMap == null) {
      return null;
    }
    Long tempLong = getLast(paramMap);
    // 结束条件二：获取到了最后一页的数据量，则不再进行处理
    if (tempLong == null || (tempLong != null && !tempLong.equals(BATCH_SIZE))) {
      return null;
    } else {
      // 正常业务
      paramMap = sieSynSnsDynMsgService.dealWithBusiness(paramMap);
    }
    return theRecursiveMothed(paramMap);
  }

  /**
   * 获取每页内容容量
   */
  private Long getLast(Map<String, Long> paramMap) {
    Object tempObj = paramMap.get("last");
    return tempObj != null && tempObj instanceof Long ? Long.valueOf(tempObj.toString())
        : tempObj != null && tempObj instanceof Integer ? Integer.valueOf(tempObj.toString()) : null;
  }

}
