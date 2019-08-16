package com.smate.center.batch.service.pub;

import com.smate.center.batch.model.sns.pub.PubSimple;

/**
 * 成果High处理器
 * 
 * @author lxz
 * 
 * @since 6.0.1
 * @version 6.0.1
 * @param pubSimple
 */
public interface PubEditHighProcessService {


  public Integer run(PubSimple pubSimple);
}
