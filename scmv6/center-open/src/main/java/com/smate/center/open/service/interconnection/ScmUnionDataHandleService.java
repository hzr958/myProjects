package com.smate.center.open.service.interconnection;

import java.util.Map;


/**
 * 处理 互联互通接口
 * 
 * @author AiJiangBin
 *
 */
public interface ScmUnionDataHandleService {
  /**
   * 统一的 刷新时间 5 乘 60 秒
   */
  public static Integer REFRESH_TIME = 5 * 60;

  /**
   * 互联互通服务处理相关数据总入口
   * 
   * @param map
   * @return
   * @throws Exception
   */
  public String handleUnionData(Map<String, Object> dataParamet) throws Exception;


}
