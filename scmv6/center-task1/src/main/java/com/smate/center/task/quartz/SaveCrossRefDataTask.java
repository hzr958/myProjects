package com.smate.center.task.quartz;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

import com.smate.center.task.base.TaskAbstract;
import com.smate.center.task.exception.TaskException;
import com.smate.center.task.model.common.CrossrefYearCount;
import com.smate.center.task.service.publicpub.SaveCrossRefDataService;
import com.smate.center.task.service.sns.quartz.TaskMarkerService;
import com.smate.core.base.utils.cache.CacheService;
import com.smate.core.base.utils.json.JacksonUtils;

public class SaveCrossRefDataTask extends TaskAbstract {
  private final Logger logger = LoggerFactory.getLogger(getClass());
  private static BufferedWriter bw;
  @Resource(name = "restTemplate")
  private RestTemplate restTemplate;
  @Autowired
  private CacheService cacheService;
  @Autowired
  private TaskMarkerService taskMarkerService;
  @Value("${crossref.file.root}")
  private String dir;
  @Autowired
  private SaveCrossRefDataService saveCrossRefDataService;

  public SaveCrossRefDataTask() {
    super();
  }

  public SaveCrossRefDataTask(String beanName) {
    super(beanName);
  }

  @SuppressWarnings("unchecked")
  public void doRun() throws TaskException {
    if (!super.isAllowExecution()) {
      logger.info("=========SaveCrossRefDataTask已关闭==========");
      return;
    }

    // 是否移除pub_id缓存
    if (taskMarkerService.getApplicationQuartzSettingValue("SaveCrossRefDataTask_removeCursor") == 1) {
      cacheService.remove("crossRef_cursor", "next_cursor");
    }

    try {
      super.closeOneTimeTask();
      // 设置请求头部
      HttpHeaders headers = new HttpHeaders();
      headers.add("X-Rate-Limit-Limit", "3000");
      headers.add("X-Rate-Limit-Interval", "60s");
      headers.add("Authorization",
          "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwOi8vY3Jvc3NyZWYub3JnLyIsImF1ZCI6Im1kcGx1cyIsImp0aSI6ImU0YjgxNjdlLTc0NjYtNGVkYi1iNmQ2LTZkOWIxYTViZDFmNCJ9.DUvXAqZBvRueARmtXGKm1P18BJ2glU7B0bs9cVTBQwc");
      HttpEntity<Object> entity = new HttpEntity<Object>(headers);
      List<CrossrefYearCount> yearCountList = saveCrossRefDataService.getYearCount();
      String nextCursor = null;
      for (CrossrefYearCount crossrefYearCount : yearCountList) {
        nextCursor = (String) cacheService.get("crossRef_cursor", "next_cursor");
        if (nextCursor == null) {
          nextCursor = "*";
        }
        Long year = crossrefYearCount.getYear();// 年份
        Long totalCount = crossrefYearCount.getCount();// 总数
        for (int i = 0; i <= totalCount / 1000 + 1; i++) {
          Map<String, Object> newDataMap = new HashMap<String, Object>();
          List<Map<String, Object>> newItemsList = new ArrayList<Map<String, Object>>();
          // 获取详情
          StringBuffer SERVER_URL = new StringBuffer();
          SERVER_URL.append("https://api.crossref.org/works");
          SERVER_URL.append("?rows=50");
          SERVER_URL.append("&mailto=linglingzhang@irissz.com");
          SERVER_URL.append("&sort=published&order=asc");
          SERVER_URL.append("&filter=from-pub-date:" + year + ",until-pub-date:" + year);
          SERVER_URL.append("&cursor=" + nextCursor);
          Map resultMap = (Map) restTemplate.getForObject(SERVER_URL.toString(), Object.class, entity);
          if ("ok".equals(resultMap.get("status"))) {
            Map messageMap = (Map) resultMap.get("message");
            if (messageMap.get("next-cursor") != null) {
              nextCursor = messageMap.get("next-cursor").toString();
              this.cacheService.put("crossRef_cursor", 60 * 60 * 24, "next_cursor", nextCursor);
            }
            List<Map<String, Object>> itemsMapList = (List<Map<String, Object>>) messageMap.get("items");
            if (itemsMapList != null && itemsMapList.size() > 0) {
              for (Map<String, Object> map : itemsMapList) {
                newItemsList.add(map);
              }
            } else {
              saveCrossRefDataService.updateYearCountStatus(year);
              cacheService.remove("crossRef_cursor", "next_cursor");
              nextCursor = (String) cacheService.get("crossRef_cursor", "next_cursor");
              if (nextCursor == null) {
                nextCursor = "*";
              }
              break;
            }
            logger.info("crossRef获取数据----------------------当前获取数据总量" + year + ":" + newItemsList.size());
          } else {
            logger.error(
                "crossRef获取数据失败--------------------" + year + ":" + resultMap.get("status") + resultMap.get("message"));
          }
          newDataMap.put("total_count", newItemsList.size());
          newDataMap.put("items", newItemsList);
          // 写入文件
          String fileName = "crossref_" + year + "_" + i + ".txt";
          File file = new File(dir + year + "/" + fileName);
          File fileParent = file.getParentFile();
          if (!fileParent.exists()) {
            fileParent.mkdir();
          }
          file.createNewFile();
          bw = new BufferedWriter(new FileWriter(file, true));
          bw.write(JacksonUtils.mapToJsonStr(newDataMap));
          bw.flush();
          bw.close();
        }
      }
    } catch (IOException e) {
      logger.error("crossRef获取数据失败");
    }

  }
}
