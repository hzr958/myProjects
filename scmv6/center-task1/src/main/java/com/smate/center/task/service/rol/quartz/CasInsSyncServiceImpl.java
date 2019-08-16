package com.smate.center.task.service.rol.quartz;

import java.util.UUID;

import org.acegisecurity.providers.encoding.PasswordEncoder;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.task.exception.ServiceException;
import com.smate.center.task.model.bpo.InsInfo;
import com.smate.core.base.utils.dao.security.UserDao;
import com.smate.core.base.utils.model.cas.security.User;

/**
 * 
 * @author hd
 *
 */
@Service("casInsSyncService")
@Transactional(rollbackFor = Exception.class)
public class CasInsSyncServiceImpl implements CasInsSyncService {
  Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private UserDao userDao;
  @Autowired
  private PasswordEncoder passwordEncoder;

  @Override
  public User findUserByLoginName(String email) {
    if (StringUtils.isEmpty(email)) {
      logger.error("同步单位信息不正确，email不能为空");
      throw new ServiceException("同步单位信息不正确，email不能为空");
    }

    try {
      return userDao.getUserByUsername(email);
    } catch (Exception e) {
      logger.error("同步单位信息不正确，查询User失败", e);
      throw new ServiceException("查询email对应的user失败", e);
    }
  }

  @Override
  public Long addSysUser(InsInfo info) throws ServiceException {
    try {
      Long psnId = info.getPsnId();
      if (StringUtils.isEmpty(info.getContactEmail()) || psnId == null) {
        logger.error("同步单位信息不正确，创建登陆账号，psnId和email不能为空");
        throw new ServiceException("同步单位信息不正确，创建登陆账号，psnId和email不能为空");
      }

      // 创建随机密码
      String uuid = UUID.randomUUID().toString();
      String pass = uuid.substring(uuid.length() - 8);
      String password = passwordEncoder.encodePassword(pass, null);
      User u = new User();
      u.setId(psnId);
      u.setPassword(password);
      u.setLoginName(info.getContactEmail());
      u.setEmail(info.getContactEmail());
      u.setEnabled(true);
      u.setNodeId(1);
      userDao.save(u);
      return psnId;
    } catch (Exception e) {
      logger.error("基金同步信息不正确，增加人员登陆账号失败", e);
      throw new ServiceException("sys_user增加人员登陆账号失败", e);
    }
  }

  // 清除cas端脏数据.
  @Override
  public void deleteCas(Long psnId) {
    try {
      userDao.delete(psnId);
    } catch (Exception e) {
      logger.error("基金同步信息不正确，清除sys_user失败,psnId=" + psnId, e);
    }
  }

}
