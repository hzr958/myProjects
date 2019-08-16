package com.smate.web.psn.service.sciencearea;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 科技领域服务实现
 *
 * @author wsn
 * @createTime 2017年3月14日 下午6:30:45
 *
 */
@Service("ScienceAreaService")
@Transactional(rollbackFor = Exception.class)
public class ScienceAreaIdentificationServiceImpl implements ScienceAreaIdentificationService {

}
