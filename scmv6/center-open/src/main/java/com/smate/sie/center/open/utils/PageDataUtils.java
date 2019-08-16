package com.smate.sie.center.open.utils;

import java.util.Map;

import com.smate.center.open.consts.OpenConsts;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.number.NumberUtils;

/**
 * 
 * open接口分页参数工具
 * 
 * @author XR
 * @date 2019-08-13
 * @description 1.分页参数不传 给默认值 2.分页参数类型错误 给默认值 --默认值(page:1 size:10)
 */

public class PageDataUtils {

  /**
   * 传入转换为Map<String,Object> data(接口data参数map)数据进行矫正
   * 
   * @param paramet
   * @return
   * @description 3.此方法只适用于open接口
   */
  public static void correctPageData(Map<String, Object> paramet) {
    Object data = paramet.get(OpenConsts.MAP_DATA);
    Map<String, Object> dataMap = JacksonUtils.jsonToMap(data.toString());
    if (dataMap != null) {
      if (!NumberUtils.isDigits(dataMap.get("page_no") + "")) {
        paramet.put("page_no", 1);
      } else {
        paramet.put("page_no", dataMap.get("page_no"));
      }
      if (!NumberUtils.isDigits(dataMap.get("page_size") + "")) {
        paramet.put("page_size", 10);
      } else {
        paramet.put("page_size", dataMap.get("page_size"));
      }
    }
  }
}
