package com.smate.web.group.service.grp.pdwh;

import com.smate.core.base.pub.enums.PubPdwhStatusEnum;

/**
 * @description 基准库成果是否存在查询服务类
 * @author xiexing
 * @date 2019年3月12日
 */
public interface PubPdwhService {
  /**
   * 校验基准库成果是否删除
   * 
   * @param pubId
   * @return
   */
  public boolean checkPdwhIsDel(Long pubId, PubPdwhStatusEnum status);
}
