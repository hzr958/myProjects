package com.smate.center.batch.jobdetail.restful;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smate.center.batch.model.dynamic.InspgDynamicRefresh;
import com.smate.center.batch.service.dynamic.DynTaskService;


/**
 * 动态同步产生restful控制器
 * 
 * @author lxz
 * @since 6.0.1-snapshot
 * @version 6.0.1-snapshot
 */
@RestController
public class InspgDynamicRestFul {

  protected Logger logger = LoggerFactory.getLogger(getClass());
  @Resource(name = "dynTaskServiceImpl")
  private DynTaskService dynTaskService;

  @RequestMapping("/storedyninitdata")
  public int storeDynInitData(@RequestBody Map<String, Object> dynData) {
    try {
      build(dynData);
    } catch (Exception e) {
      logger.error("动态同步产生restful出错", e);
      return -1;
    }
    logger.info("动态同步产生restful开始");
    return 1;
  }

  /**
   * 构建对应的InspgDynamicRefresh对象
   * 
   * @param dynData
   * @author lxz
   */
  private void build(Map<String, Object> dynData) throws Exception {
    InspgDynamicRefresh refresh = new InspgDynamicRefresh();
    switch (Integer.valueOf((String) dynData.get("dynType"))) {
      case 101:
        // 新鲜事-附件
        refresh.setInspgId(Long.parseLong((String) dynData.get("inspgId")));
        refresh.setDynId(Long.parseLong((String) dynData.get("dynId")));
        refresh.setPsnId(Long.parseLong((String) dynData.get("createPsnId")));
        refresh.setDynType(Integer.valueOf((String) dynData.get("dynType")));
        refresh.setCreateTime(new Date(Long.parseLong((String) dynData.get("createTime"))));
        List<Map<String, String>> jsonList = (ArrayList<Map<String, String>>) dynData.get("detailList");
        List<Long> result = new ArrayList<Long>();
        for (Map<String, String> obj : jsonList) {
          result.add(Long.parseLong(obj.get("id")));
        }
        refresh.setFileIds(result);
        refresh.setContent("" + dynData.get("content"));
        dynTaskService.executeDyn(refresh);
        break;
      case 102:
        // 新鲜事-普通(链接)
        refresh.setInspgId(Long.parseLong((String) dynData.get("inspgId")));
        refresh.setDynId(Long.parseLong((String) dynData.get("dynId")));
        refresh.setPsnId(Long.parseLong((String) dynData.get("createPsnId")));
        refresh.setDynType(Integer.parseInt((String) dynData.get("dynType")));
        refresh.setCreateTime(new Date(Long.parseLong((String) dynData.get("createTime"))));
        String link = "&nbsp;&nbsp;<a class='Blue	' target='_blank'  href='" + dynData.get("linkUrl") + "'>"
            + dynData.get("linkUrl") + "</a>";
        refresh.setContent(dynData.get("content") + link);
        dynTaskService.executeDyn(refresh);
        break;
      default:
        break;
    }
  }

}
