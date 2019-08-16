package com.smate.web.v8pub.service.pdwh;

import com.smate.web.v8pub.exception.ServiceException;
import com.smate.web.v8pub.po.pdwh.PdwhPubViewPO;
import com.smate.web.v8pub.vo.pdwh.PdwhPubOperateVO;

/**
 * 基准库成果查看服务接口
 * 
 * @author aijiangbin
 * @date 2018年6月1日
 */
public interface PdwhPubViewService {

  /**
   * 保存成果查看记录
   * 
   * @param pubViewPO
   * @throws ServiceException
   */
  public void save(PdwhPubViewPO pubViewPO) throws ServiceException;

  public void pdwhViewOpt(PdwhPubOperateVO pdwhPubOperateVO) throws ServiceException;

}
