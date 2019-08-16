package com.smate.center.batch.service.group;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smate.center.batch.dao.sns.pub.InviteUrlValueDao;
import com.smate.center.batch.exception.pub.DaoException;
import com.smate.center.batch.model.sns.pub.InviteUrlValue;

/**
 * 邀请信息处理实现类
 * 
 * @author zzx
 *
 */
@Service("inviteUrlValueService")
public class InviteUrlValueServiceImpl implements InviteUrlValueService {
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private InviteUrlValueDao inviteUrlValueDao;

  @Override
  public InviteUrlValue findInviteUrlValueByRefId(Long id) throws Exception {
    try {
      return inviteUrlValueDao.findInviteUrlValueByRefId(id);
    } catch (DaoException e) {
      logger.error("查找邀请链接参数值出错", e);
      throw new Exception(e);
    }
  }

  @Override
  public Long saveInviteUrlValue(InviteUrlValue inviteUrlValue) throws Exception {
    inviteUrlValueDao.save(inviteUrlValue);
    return inviteUrlValue.getId();
  }

}
