package com.smate.sie.center.task.service;

import java.util.List;
import java.util.Map;

import com.smate.sie.center.task.model.SieRobotMan;

/**
 * 机器人操作service
 * 
 * @author 叶星源
 */
public interface SieRobotManControlService {

  /**
   * 机器人对这个项目进行访问(阅读，分享，下载全文)
   * 
   * @param ins
   * @param man
   */
  List<Map<String, Object>> interviewProject(Integer configId, Long ins, SieRobotMan man);

  /**
   * 机器人对这个专利进行访问(阅读，分享，下载全文，引用)
   * 
   * @param ins
   * @param man
   */
  List<Map<String, Object>> interviewPatent(Integer configId, Long ins, SieRobotMan man);

  /**
   * 机器人对这个成果进行访问(阅读，分享，下载全文，引用)
   * 
   * @param ins
   * @param man
   */
  List<Map<String, Object>> interviewPublication(Integer configId, Long ins, SieRobotMan man);

  /**
   * 访问单位，记录单位访问记录，记录分享单位记录
   * 
   * @param ins
   * @param man
   */
  void interviewIns(Integer configId, Long insId, SieRobotMan man);

}
