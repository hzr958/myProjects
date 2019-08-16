package com.smate.sie.center.task.service;

import java.util.Map;

/***
 * 
 * @author yxy
 * @Date 20190221
 */
public interface SieSynSnsDynMsgService {

  /**
   * 处理业务,根据参数进行分页查询，并返回查询后的参数
   */
  Map<String, Long> dealWithBusiness(Map<String, Long> paramMap);

}
