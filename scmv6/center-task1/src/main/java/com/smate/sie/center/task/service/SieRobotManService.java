package com.smate.sie.center.task.service;

import java.util.List;

import com.smate.sie.center.task.model.SieRobotMan;
import com.smate.sie.center.task.model.SieRobotManConfig;
import com.smate.sie.center.task.model.SieRobotManIns;

/**
 * 机器人相关配置信息
 * 
 * @author 叶星源
 */
public interface SieRobotManService {

  /**
   * 失败后的信息记录
   */
  /* void errorInfo(SieRobotManConfig sieRobotManReflush); */

  /**
   * 初始化配置信息
   */
  SieRobotManConfig getNormalReflush(Integer configId);

  /**
   * 初始化机器人配置，设置ip和ip地址，以及机器人访问各条数据时的操作数据。
   */
  List<SieRobotMan> getSieRobotMan(SieRobotManConfig sieRobotManReflush);

  /**
   * 获取需要访问单位的基本信息
   */
  List<SieRobotManIns> getSubInsList();
}
