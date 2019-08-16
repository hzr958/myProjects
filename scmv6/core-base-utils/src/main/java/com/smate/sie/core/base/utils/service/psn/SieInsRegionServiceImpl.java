package com.smate.sie.core.base.utils.service.psn;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.utils.exception.SysServiceException;
import com.smate.sie.core.base.utils.dao.ins.SieInsRegionDao;

/**
 * 单位地区 seivice
 * 
 * @author hd
 *
 */
@Service("sieInsRegionService")
@Transactional(rollbackFor = Exception.class)
public class SieInsRegionServiceImpl implements SieInsRegionService {



}
