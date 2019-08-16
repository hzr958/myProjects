package com.smate.center.open.service.wechat.log;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.open.dao.wechat.log.SmateWeChatLogDao;
import com.smate.center.open.model.wechat.log.SmateWeChatLog;
import com.smate.core.base.utils.dao.wechat.WeChatRelationDao;
import com.smate.core.base.utils.exception.SysServiceException;

/**
 * smate微信关系日志服务.
 * 
 * @author xys
 *
 */
@Service("smateWeChatLogService")
@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
public class SmateWeChatLogServiceImpl implements SmateWeChatLogService {

  @Autowired
  private WeChatRelationDao weChatRelationDao;
  @Autowired
  private SmateWeChatLogDao smateWeChatLogDao;

  @Override
  public void save(String weChatOpenId, Long smateOpenId, String type, int status, String weChatResult)
      throws SysServiceException {
    if (smateOpenId == null) {
      smateOpenId = weChatRelationDao.getSmateOpenId(weChatOpenId);
    }
    SmateWeChatLog smateWeChatLog =
        new SmateWeChatLog(weChatOpenId, smateOpenId, type, status, weChatResult, new Date());
    smateWeChatLogDao.save(smateWeChatLog);
  }

}
