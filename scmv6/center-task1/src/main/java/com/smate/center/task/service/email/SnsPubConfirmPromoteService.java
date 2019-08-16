package com.smate.center.task.service.email;

import java.util.List;
import java.util.Map;

import com.smate.center.task.exception.ServiceException;
import com.smate.center.task.model.rcmd.quartz.PublicationConfirm;


/**
 * 成果认领邮件推广服务
 * 
 * @author zk
 */

public interface SnsPubConfirmPromoteService {

  // 获取三条
  final Integer SIZE = 3;


  Map getPubTitlePlus(Long psnId) throws ServiceException;

  Map getPubTitlePlusByScore(Long psnId) throws ServiceException;

}
