package com.smate.center.task.quartz.group;

import java.util.Calendar;
import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.task.base.TaskAbstract;
import com.smate.center.task.exception.SingleTaskException;
import com.smate.center.task.model.grp.GrpBaseinfo;
import com.smate.center.task.model.grp.GrpPubInit;
import com.smate.center.task.service.group.GrpService;

public class InstrestGrpAddPubTask extends TaskAbstract {
  protected Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private GrpService grpService;

  public InstrestGrpAddPubTask() {

  }

  public InstrestGrpAddPubTask(String beanName) {
    super(beanName);
  }

  public void doRun() throws SingleTaskException {
    if (!super.isAllowExecution()) {
      logger.info("=========InstrestGrpAddPubTask 已关闭==========");
      return;
    }
    List<GrpBaseinfo> GrpBaseinfoList = grpService.getInstrestGroup();
    try {
      for (GrpBaseinfo grpBaseInfo : GrpBaseinfoList) {
        int grpPubYearNum = (int) Math.ceil((new Random().nextInt(50 - 30) + 30) / 5);// 每个年份初始化成果数
        for (int i = 1; i < 6; i++) {// 近5年成果
          int year = Calendar.getInstance().get(Calendar.YEAR);// 获取当前年份
          List<GrpPubInit> grpPubInitList = grpService.getGrpPubInit(grpBaseInfo.getGrpId(), year - i, grpPubYearNum);
          for (GrpPubInit grpPubInit : grpPubInitList) {
            grpService.importPdwhPubIntoGroup(grpPubInit, grpBaseInfo.getOwnerPsnId());
          }
        }
      }
      super.closeOneTimeTask();
    } catch (Exception e) {
      logger.error("InstrestGrpAddPubTask出错------", e);
    }
  }
}
