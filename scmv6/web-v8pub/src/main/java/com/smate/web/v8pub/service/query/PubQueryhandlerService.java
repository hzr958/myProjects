package com.smate.web.v8pub.service.query;

import com.smate.web.v8pub.vo.PubListResult;

/**
 * 成果查询处理类
 * 
 * @author aijiangbin
 * @date 2018年7月17日
 */
public interface PubQueryhandlerService {

  /**
   * 查询成果入口
   * 
   * @param pubQueryDTO
   * @return
   */
  public PubListResult queryPub(PubQueryDTO pubQueryDTO);

}
