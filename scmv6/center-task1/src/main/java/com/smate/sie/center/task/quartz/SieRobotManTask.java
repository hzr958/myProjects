package com.smate.sie.center.task.quartz;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.task.base.TaskAbstract;
import com.smate.sie.center.task.model.SieRobotMan;
import com.smate.sie.center.task.model.SieRobotManConfig;
import com.smate.sie.center.task.model.SieRobotManIns;
import com.smate.sie.center.task.service.SieRobotManControlService;
import com.smate.sie.center.task.service.SieRobotManService;

/**
 * SIE单位分析，随机访问机器人
 * 
 * @author 叶星源
 * @Date 20181225
 */
public class SieRobotManTask extends TaskAbstract {
  private Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private SieRobotManService sieRobotManService;
  @Autowired
  private SieRobotManControlService sieRobotManControlService;

  public SieRobotManTask() {
    super();
  }

  public SieRobotManTask(String beanName) {
    super(beanName);
  }

  public void doRun() throws Exception {
    if (!super.isAllowExecution()) {
      return;
    }
    try {
      doRunOnTheBusiness();
    } catch (Exception e) {
      logger.error("社交机器人运行异常：", e);
    }
  }

  /**
   * 业务操作情况
   */
  private void doRunOnTheBusiness() {
    try {
      // 获取robot_man_ins表中需执行的机构列表信息
      List<SieRobotManIns> sieRobotManInslist = sieRobotManService.getSubInsList();
      // 先判断是否有需要执行随机访问的机构
      if (sieRobotManInslist == null) {
        logger.info("待执行机器人的机构列表无数据，请检查社交机器人需访问的机构表数据！");;
        return;
      }
      Long insId = null;
      Integer configId = null;
      for (SieRobotManIns sieRobotManIns : sieRobotManInslist) {
        insId = sieRobotManIns.getId();
        configId = sieRobotManIns.getConfigId();
        SieRobotManConfig sieRobotManConfig = sieRobotManService.getNormalReflush(configId);
        if (robotIsAllowExecution(configId)) {
          for (SieRobotMan man : sieRobotManService.getSieRobotMan(sieRobotManConfig)) {
            // 该机器人随机访问该机构
            sieRobotManControlService.interviewIns(configId, insId, man);
            // 该机器人随机访问该机构下的项目
            sieRobotManControlService.interviewProject(configId, insId, man);
            // 该机器人随机访问该机构下的专利
            sieRobotManControlService.interviewPatent(configId, insId, man);
            // 该机器人随机访问该机构下的成果
            sieRobotManControlService.interviewPublication(configId, insId, man);
            logger.info("run to robbot one : robbot task end.");
          }
        }
      }
      logger.info("run end to robbot one : robbot task end.");
    } catch (Exception e) {
      logger.error("社交机器人执行操作出异常：", e);
    }
  }

  /*
   * public void doRun() throws Exception { if (!super.isAllowExecution()) { return; } try {
   * SieRobotManConfig sieRobotManConfig = sieRobotManService.getNormalReflush(); if
   * (sieRobotManConfig != null) { doRunOnTheBusiness(sieRobotManConfig); } } catch (Exception e) {
   * logger.error("社交机器人运行异常：", e); } }
   * 
   *//**
      * 业务操作情况
      *//*
         * private void doRunOnTheBusiness(SieRobotManConfig sieRobotManConfig) { try { Set<Long> insSet =
         * sieRobotManService.getSubIns(sieRobotManConfig); // 开刀的地方 for (Long insId : insSet) { if
         * (robotIsAllowExecution()) { // 开刀的地方 for (SieRobotMan man :
         * sieRobotManService.getSieRobotMan(sieRobotManConfig)) { // 该机器人随机访问该机构
         * sieRobotManControlService.interviewIns(insId, man); // 该机器人随机访问该机构下的项目
         * sieRobotManControlService.interviewProject(insId, man); // 该机器人随机访问该机构下的专利
         * sieRobotManControlService.interviewPatent(insId, man); // 该机器人随机访问该机构下的成果
         * sieRobotManControlService.interviewPublication(insId, man);
         * logger.info("run to robbot one : robbot task end."); } } }
         * logger.info("run end to robbot one : robbot task end."); } catch (Exception e) {
         * logger.error("社交机器人执行操作出异常：", e); } }
         */

  /**
   * 是否需要继续运行
   */
  private boolean robotIsAllowExecution(Integer configId) {
    boolean result = false;
    if (configId != null) {
      SieRobotManConfig sieRobotManConfig = sieRobotManService.getNormalReflush(configId);
      if (sieRobotManConfig != null) {
        result = true;
      }
    }
    return result;
  }

}
