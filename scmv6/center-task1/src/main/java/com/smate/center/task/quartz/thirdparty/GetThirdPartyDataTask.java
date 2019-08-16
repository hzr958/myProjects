package com.smate.center.task.quartz.thirdparty;

import com.smate.center.task.base.TaskAbstract;
import com.smate.center.task.exception.SingleTaskException;
import com.smate.center.task.model.thirdparty.ThirdSources;
import com.smate.center.task.model.thirdparty.ThirdSourcesErrorLog;
import com.smate.center.task.model.thirdparty.ThirdSourcesType;
import com.smate.center.task.service.thirdparty.ThirdPartyDataHandleService;
import com.smate.center.task.service.thirdparty.source.ThirdSourcesErrorLogService;
import com.smate.center.task.service.thirdparty.source.ThirdSourcesRequestService;
import com.smate.center.task.service.thirdparty.source.ThirdSourcesService;
import com.smate.core.base.utils.json.JacksonUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 获取第三方业务数据接口调用任务 .
 *
 * @author tsz
 *
 */
public class GetThirdPartyDataTask extends TaskAbstract {
  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private ThirdSourcesService thirdSourcesService;
  @Autowired
  private ThirdSourcesRequestService thirdSourcesRequestService;
  @Autowired
  private ThirdPartyDataHandleService thirdPartyDataHandleService;
  @Autowired
  private ThirdSourcesErrorLogService thirdSourcesErrorLogService;

  public GetThirdPartyDataTask() {
    super();
  }

  public GetThirdPartyDataTask(String beanName) {
    super(beanName);
  }

  public void doRun() throws SingleTaskException {
    if (!super.isAllowExecution()) {
      logger.info("=========GetThirdPartyDataTask 已关闭==========");
      return;
    }
    logger.info("=========GetThirdPartyDataTask开始运行==========");
    try {
      // 获取调用要调用的接口 list
      List<ThirdSources> tsList = thirdSourcesService.getSourcesList();
      if (CollectionUtils.isEmpty(tsList)) {
        logger.info("=========没有需要调用的接口 退出任务==========");
        return;
      }
      // 循环调用接口
      for (ThirdSources ts : tsList) {
        // 注意每个系统可能会提供多种类别信息
        List<ThirdSourcesType> tsTypeList = ts.getTsType();
        if (CollectionUtils.isEmpty(tsTypeList)) {
          continue;
        }
        for (ThirdSourcesType tst : tsTypeList) {
          // 里面记录请求记录 已经请求返回结果 不抛出异常
          List<Map<String, Object>> resultList = thirdSourcesRequestService.postUrl(ts, tst);
          if (CollectionUtils.isEmpty(resultList)) {
            continue;
          }
          for (Map<String, Object> result : resultList) {
            try {
              // 得到数据解析保存
              thirdPartyDataHandleService.handle(ts.getToken(), tst.getType().toString(), result);
            } catch (Exception e) {
              logger.error("接口响应数据 单条记录处理失败 数据:" + JacksonUtils.mapToJsonStr(result), e);
              // 获取异常 增加错误记录
              // 对应的异常 对应的处理方式
              // 异常不在抛出
              ThirdSourcesErrorLog log = new ThirdSourcesErrorLog();
              log.setCreateDate(new Date());
              log.setErrorData(JacksonUtils.mapToJsonStr(result));
              log.setErrorMsg(e.getMessage());
              log.setSourceId(tst.getSourceId());
              log.setType(tst.getType());
              thirdSourcesErrorLogService.saveLog(log);
            }
          }
        }
      }
    } catch (Exception e) {
      logger.error("GetThirdPartyDataTask,运行异常", e);
    }
  }

}
