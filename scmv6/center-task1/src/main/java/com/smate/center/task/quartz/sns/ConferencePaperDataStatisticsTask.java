package com.smate.center.task.quartz.sns;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import com.smate.center.task.base.TaskAbstract;
import com.smate.center.task.exception.SingleTaskException;
import com.smate.center.task.model.sns.pub.PubTemp;
import com.smate.center.task.service.sns.pub.PubTempService;
import com.smate.core.base.utils.security.Des3Utils;

/**
 * @description 会议论文组织者数据统计定时任务
 * @author xiexing
 * @date 2019年2月28日
 */
public class ConferencePaperDataStatisticsTask extends TaskAbstract {
  private static final Logger logger = LoggerFactory.getLogger(ConferencePaperDataStatisticsTask.class);

  private static final String restfulUrl = "/data/pub/query/detail";

  @Value("${domainscm}")
  private String domainscm;

  @Autowired
  private PubTempService pubTempService;

  @Autowired
  private RestTemplate restTemplate;

  public ConferencePaperDataStatisticsTask() {
    super();
  }

  public ConferencePaperDataStatisticsTask(String beanName) {
    super(beanName);
  }


  public void doRun() throws SingleTaskException {
    if (!super.isAllowExecution()) {
      logger.info("=========ConferencePaperDataStatisticsTask 已关闭==========");
      return;
    }
    logger.info("=========ConferencePaperDataStatisticsTask 开始执行==========");
    try {
      while (true) {
        // 获取id
        List<Long> pubIds = pubTempService.getIds(500);

        if (pubIds != null && pubIds.size() > 0) {
          // 调接口查询详情
          if (CollectionUtils.isNotEmpty(pubIds)) {
            Map<String, Object> object = new HashMap<String, Object>();
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("serviceType", "snsPub");
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
            PubTemp pubTemp = new PubTemp();
            for (Long pubId : pubIds) {
              params.put("des3PubId", Des3Utils.encodeToDes3(String.valueOf(pubId)));
              HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<Map<String, Object>>(params, headers);
              object =
                  (Map<String, Object>) restTemplate.postForObject(domainscm + restfulUrl, requestEntity, Object.class);
              pubTemp.setPubId(pubId);
              Map<String, Object> resultMap = (Map<String, Object>) object.get("pubTypeInfo");
              String resultStr = String.valueOf(resultMap == null ? "" : resultMap.get("organizer"));
              pubTemp.setOrganizer(resultStr);
              pubTemp.setStatus(1);
              pubTempService.update(pubTemp);
            }
          }
        } else {
          break;
        }
      }
    } catch (Exception e) {
      logger.error("获取会议论文统计数出错", e);
    }
  }
}
