package com.smate.center.batch.service.user;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.core.base.utils.dao.security.SysUserLoginLogDao;

@Service("sysUserLoginLogService")
@Transactional(rollbackFor = Exception.class)
public class SysUserLoginLogServiceImpl implements SysUserLoginLogService {
  /**
   * 
   */
  private static final long serialVersionUID = 800869716178206951L;
  @Autowired
  public SysUserLoginLogDao sysUserLoginLogDao;

  @Override
  public Date findLastTimeByPsnId(Long psnId) throws ServiceException {
    try {
      return sysUserLoginLogDao.findLastTimeByPsnId(psnId);
    } catch (Exception e) {
      throw new ServiceException(e);
    }
  }

}
