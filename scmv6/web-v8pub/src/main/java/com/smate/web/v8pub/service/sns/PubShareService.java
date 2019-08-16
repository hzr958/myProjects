package com.smate.web.v8pub.service.sns;

import com.smate.web.v8pub.exception.ServiceException;
import com.smate.web.v8pub.po.sns.PubSharePO;
import com.smate.web.v8pub.vo.PubShareVo;
import com.smate.web.v8pub.vo.sns.PubOperateVO;

/**
 * 成果分享接口
 * 
 * @author aijiangbin
 * @date 2018年6月1日
 */
public interface PubShareService {

  public void save(PubSharePO pubShare) throws ServiceException;

  public void shareOpt(PubOperateVO pubOperateVO) throws ServiceException;

  public void sendShareEmail(PubShareVo pubShareVo, String des3PsnId, int i) throws Exception;

  public void updateSnsShareStatistics(PubOperateVO pubOperateVO, Long snsPubId) throws Exception;
}
