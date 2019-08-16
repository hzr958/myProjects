package com.smate.web.dyn.service.share;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.web.dyn.model.share.SmateShareForm;

/**
 * 分享文件到动态
 * 
 * @author wsn
 * @date May 24, 2019
 */
@Service("shareFileToDyn")
@Transactional(rollbackFor = Exception.class)
public class ShareSnsPubToDynService extends ShareToDynService {

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  @Override
  boolean checkParams(SmateShareForm form) {
    // TODO Auto-generated method stub
    return true;
  }

  @Override
  void shareRes(SmateShareForm form) {
    // TODO Auto-generated method stub

  }

}
