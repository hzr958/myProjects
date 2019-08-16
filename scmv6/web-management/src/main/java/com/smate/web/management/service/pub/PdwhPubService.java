package com.smate.web.management.service.pub;

import com.smate.web.management.model.pub.PubInfoForm;
import com.smate.web.management.model.pub.PubPdwhPO;

/**
 * 成果服务接口
 * 
 * @author yhx
 *
 */
public interface PdwhPubService {
  /**
   * 获取成果列表
   * 
   * @return
   */

  public void getPubList(PubInfoForm form);

  public PubPdwhPO get(Long pubId);

  /**
   * 删除基准库成果
   * 
   * @param des3PubId
   * @return
   */
  public String deletePub(String des3PubId) throws Exception;

  /**
   * 记录日志
   */
  public void savePubOperateLog(Long pubId, Long psnId, Long opType, String descMsg) throws Exception;
}
