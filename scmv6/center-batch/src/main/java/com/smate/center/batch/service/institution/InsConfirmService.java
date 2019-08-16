package com.smate.center.batch.service.institution;


public interface InsConfirmService {

  /**
   * 查找单位确认成果状态
   * 
   * @param insId
   * @return
   * @throws ServiceException
   */
  public Integer findInsConfirmStatus(Long insId);
}
