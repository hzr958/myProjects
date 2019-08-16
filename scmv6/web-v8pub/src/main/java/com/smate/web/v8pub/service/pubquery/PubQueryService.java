package com.smate.web.v8pub.service.pubquery;

import com.smate.web.v8pub.service.query.PubQueryDTO;
import com.smate.web.v8pub.vo.PubListResult;

/**
 * 成果查询接口
 * 
 * @author aijiangbin
 * @date 2018年7月17日
 */
public interface PubQueryService {

  public PubListResult handleData(PubQueryDTO pubQueryDTO);

}
