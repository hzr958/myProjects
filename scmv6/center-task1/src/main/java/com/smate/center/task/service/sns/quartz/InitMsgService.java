package com.smate.center.task.service.sns.quartz;

import java.util.List;

import com.smate.center.task.model.sns.msg.InitMsgForm;

public interface InitMsgService {

  List<InitMsgForm> getList(Integer batchSize);

  void doInitMsg(InitMsgForm i);
}
