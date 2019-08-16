package com.smate.web.v8pub.service.pdwh;

import com.smate.web.v8pub.exception.ServiceException;
import com.smate.web.v8pub.po.pdwh.PdwhPubSharePO;
import com.smate.web.v8pub.vo.pdwh.PdwhPubOperateVO;

/**
 * 基准库成果分享服务接口
 * 
 * @author aijiangbin
 * @date 2018年6月1日
 */
public interface PdwhPubShareService {

  public void save(PdwhPubSharePO pubShare) throws ServiceException;

  public void pdwhShareOpt(PdwhPubOperateVO pdwhPubOperateVO) throws ServiceException;

  public void pdwhShare(PdwhPubOperateVO pdwhPubOperateVO, Long pdwhPubId) throws ServiceException;
}
