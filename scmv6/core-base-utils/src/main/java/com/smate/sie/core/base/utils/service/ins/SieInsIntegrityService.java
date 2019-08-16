package com.smate.sie.core.base.utils.service.ins;

import com.smate.core.base.utils.exception.SysServiceException;
import com.smate.core.base.utils.model.consts.Sie6Institution;

/**
 * 单位信息完整度计算服务
 * 
 * @author ztg
 *
 */
public interface SieInsIntegrityService {

  void saveIntegrity(Sie6Institution ins) throws SysServiceException;
}
