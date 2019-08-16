package com.smate.web.dyn.service.dynamic.restful;

import java.util.Map;

import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.web.dyn.form.dynamic.DynamicForm;
import com.smate.web.dyn.service.dynamic.DynamicMsgService;
import com.smate.web.dyn.service.dynamic.DynamicRealtimeService;

/**
 * 动态生成 restful接口
 * 
 * @author tsz
 *
 */
@RestController
public class DynamicProduceController {

  protected Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private DynamicRealtimeService dynamicRealtimeService;
  @Autowired
  private DynamicMsgService dynamicMsgService;

  @RequestMapping(value = "/dynamicprodece", method = RequestMethod.POST)
  public String getScmOpenData(@RequestBody Map<String, Object> mapData) {
    try {
      if (!dynamicMsgService.getPubPermit(mapData)) {
        logger.info("--------------------B3动态的隐私成果不产生动态---------pubId=" + mapData.get("pubId").toString());
        return "";
      }
      int count = dynamicMsgService.getPubDynamicCount(
          NumberUtils.toLong(mapData.get("psnId") != null ? mapData.get("psnId").toString() : "0"),
          (Integer) mapData.get("resType"), mapData.get("dynType").toString());
      logger.info("--------------------------当天生成的动态时---------" + count
          + "--------条-----------------------------------------------------------");
      if (count > 0) {
        return "";
      }
      DynamicForm form = new DynamicForm();
      buildDynamicForm(form, mapData);
      if (checkMapData(mapData)) {
        dynamicRealtimeService.dynamicRealtime(form);
        return "success";
      } else {
        // 参数 不全
        return "error";
      }
    } catch (Exception e) {
      logger.error("restful生成动态出错" + e);
      return "error";
    }
  }

  /**
   * 构建form对象
   * 
   * @param form
   * @param mapData
   */
  private void buildDynamicForm(DynamicForm form, Map<String, Object> mapData) {
    String dynType = mapData.get("dynType").toString();
    if ("B3TEMP".equals(dynType)) {
      form.setPsnId(Long.valueOf(mapData.get("psnId").toString()));
      mapData.remove("psnId");
      form.setResType(Integer.valueOf(mapData.get("resType").toString()));
      mapData.remove("resType");
      form.setDynType(mapData.get("dynType").toString());
      form.setResId(NumberUtils.toLong(mapData.get("pubId").toString()));
      mapData.remove("dynType");
      form.setParamJson(JacksonUtils.mapToJsonStr(mapData));
    }

  }

  /**
   * 参数检查
   * 
   * @param mapData
   * @return
   */
  private boolean checkMapData(Map<String, Object> mapData) {
    return true;

  }

}
