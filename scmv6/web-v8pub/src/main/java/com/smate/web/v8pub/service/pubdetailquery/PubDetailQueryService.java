package com.smate.web.v8pub.service.pubdetailquery;
/**
 * 成果详情查询接口
 * 
 * @author aijiangbin
 * @date 2018年8月3日
 */

import com.smate.core.base.exception.ServiceException;
import com.smate.core.base.pub.vo.PubDetailVO;

public interface PubDetailQueryService {

  /**
   * 
   * @param pubId
   * @param grpId 群组成果需要的参数
   * @return
   */
  public PubDetailVO queryPubDetail(Long pubId, Long grpId) throws ServiceException;

}
