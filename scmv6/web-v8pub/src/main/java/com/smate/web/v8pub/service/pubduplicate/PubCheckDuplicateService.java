package com.smate.web.v8pub.service.pubduplicate;

import com.smate.web.v8pub.dom.sns.PubSnsDetailDOM;
import com.smate.web.v8pub.exception.ServiceException;
import com.smate.web.v8pub.vo.sns.PubDupResultVO;

/**
 * 专门提供成果查重的服务
 * 
 * @author YJ
 *
 *         2019年7月18日
 */
public interface PubCheckDuplicateService {

  /**
   * 个人库成果查重接口
   * 
   * @param pubSnsDOM
   * @param psnId 需要查重者的psnId
   * @return 查重结果json返回
   * @throws ServiceException
   */
  PubDupResultVO checkSnsPubDuplicate(PubSnsDetailDOM pubSnsDOM, Long psnId) throws ServiceException;

}
