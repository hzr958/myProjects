package com.smate.web.v8pub.service.pubdetailquery;

import java.util.Map;

import com.smate.core.base.pub.vo.PubDetailVO;


/**
 * 成果详处理类
 * 
 * @author aijiangbin
 * @date 2018年8月3日
 */
public interface PubDetailHandleService {


  public Map<String, String> checkParmas(Map<String, Object> params);

  public PubDetailVO queryPubDetail(Map<String, Object> params);

}
