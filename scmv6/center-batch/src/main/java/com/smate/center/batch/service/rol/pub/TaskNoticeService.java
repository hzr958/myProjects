package com.smate.center.batch.service.rol.pub;

import java.io.Serializable;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.rol.pub.ClearTaskNoticeEvent;
import com.smate.center.batch.model.rol.pub.ClearTaskNoticeUserInfo;


/**
 * 单位成果数据状态服务信息.
 * 
 * @author liqinghua
 * 
 */
public interface TaskNoticeService extends Serializable {

  /**
   * 获取成果KPI统计不完善的成果统计数.
   * 
   * @return
   * @throws ServiceException
   */
  public Long getPubKpiUnValidNum() throws ServiceException;

  /**
   * 获取需要合并的成果统计数.
   * 
   * @return
   * @throws ServiceException
   */
  public Long getNeedMergePubNum() throws ServiceException;

  /**
   * 获取等待确认成果人员数.
   * 
   * @return
   * @throws ServiceException
   */
  public Long getNeedConfirmPubPsnNum() throws ServiceException;

  /**
   * 获取等待审核的单位人员数.
   * 
   * @return
   * @throws ServiceException
   */
  public Long getNeedApprovePsnNum() throws ServiceException;

  /**
   * 获取未分配部门人员数.
   * 
   * @return
   * @throws ServiceException
   */
  public Long getNoUnitPsnNum() throws ServiceException;

  /**
   * 清理消息统计数.
   * 
   * @param event
   * @throws ServiceException
   */
  public void clearTaskNotice(ClearTaskNoticeEvent event) throws ServiceException;

  /**
   * 清理消息统计数.
   * 
   * @param event
   * @throws ServiceException
   */
  public void clearTaskNotice(ClearTaskNoticeEvent event, ClearTaskNoticeUserInfo userInfo) throws ServiceException;

}
