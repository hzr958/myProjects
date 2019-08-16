package com.smate.web.v8pub.service.sns;

import com.smate.web.v8pub.exception.ServiceException;
import com.smate.web.v8pub.po.sns.PubViewPO;
import com.smate.web.v8pub.vo.sns.PubOperateVO;

/**
 * 成果查看接口
 * 
 * @author aijiangbin
 * @date 2018年6月1日
 */
public interface PubViewService {

  /**
   * 保存成果查看记录
   * 
   * @param pubViewPO
   * @throws ServiceException
   */
  public void save(PubViewPO pubViewPO) throws ServiceException;

  public void viewOpt(PubOperateVO pubOperateVO) throws ServiceException;

}
