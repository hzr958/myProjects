package com.smate.center.oauth.service.thirdlogin;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.oauth.dao.thirduser.SysThirdUserDao;
import com.smate.center.oauth.exception.ServiceException;
import com.smate.center.oauth.model.thirduser.SysThirdUser;

/**
 * 第三方登录服务
 * 
 * @author Scy
 * 
 */
@Transactional(rollbackFor = Exception.class)
@Service
public class ThirdLoginServiceImpl implements ThirdLoginService {

  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private SysThirdUserDao sysThirdUserDao;

  @Override
  public void saveThirdLogin(Long psnId, Integer type, String thirdId) throws ServiceException {
    try {
      Long psnIdTemp = this.findPsnId(type, thirdId);
      if (psnIdTemp != null) {
        return;
      }
      SysThirdUser sysThirdUser = new SysThirdUser(psnId, type, thirdId, new Date());
      this.sysThirdUserDao.save(sysThirdUser);
    } catch (Exception e) {
      logger.error("保存第三方登录信息的时候出错，psnId={}，type={}，thirdId={}", psnId, type, thirdId, e);
      throw new ServiceException("保存第三方登录信息的时候出错，psnId=" + psnId + "，type=" + type + "，thirdId=" + thirdId, e);
    }

  }

  @Override
  public void saveThirdLogin(Long psnId, Integer type, String thirdId, String nickName, String qqUnionId)
      throws ServiceException {
    try {
      Long psnIdTemp = this.findPsnId(type, thirdId);
      if (psnIdTemp != null) {
        return;
      }
      SysThirdUser sysThirdUser = new SysThirdUser(psnId, type, thirdId, new Date(), nickName, qqUnionId);
      this.sysThirdUserDao.save(sysThirdUser);
    } catch (Exception e) {
      logger.error("保存第三方登录信息的时候出错，psnId={}，type={}，thirdId={}，qqUnionId={}", psnId, type, thirdId, qqUnionId, e);
      throw new ServiceException(
          "保存第三方登录信息的时候出错，psnId=" + psnId + "，type=" + type + "，thirdId=" + thirdId + "，qqUnionId=" + qqUnionId, e);
    }

  }

  @Override
  public Long findPsnId(Integer type, String thirdId) throws ServiceException {
    try {
      return this.sysThirdUserDao.findPsnId(type, thirdId);
    } catch (Exception e) {
      logger.error("根据第三方ID查找PsnId出错，type=" + type + "，thirdId=" + thirdId, e);
      throw new ServiceException("根据第三方ID查找PsnId出错，type=" + type + "，thirdId=" + thirdId, e);
    }
  }

  @Override
  public Long findUnionId(Integer type, String unionId) throws ServiceException {
    try {
      return this.sysThirdUserDao.findUnionID(type, unionId);
    } catch (Exception e) {
      logger.error("根据第三方unionId查找PsnId出错，type=" + type + "，unionId=" + unionId, e);
      throw new ServiceException("根据第三方unionId查找PsnId出错，type=" + type + "，unionId=" + unionId, e);
    }
  }

  @Override
  public void updateNickname(Integer type, String thirdId, String nickName) throws ServiceException {
    try {
      this.sysThirdUserDao.updateNickname(type, thirdId, nickName);
    } catch (Exception e) {
      logger.error("根据第三方用户的昵称异常，type=" + type + "，thirdId=" + thirdId + "，nickName=" + nickName, e);
      throw new ServiceException("根据第三方用户的昵称异常，type=" + type + "，thirdId=" + thirdId + "，nickName=" + nickName, e);
    }

  }

  @Override
  public void delete(Long psnId, String unionId, int type) throws ServiceException {
    sysThirdUserDao.deleteBy(psnId, unionId, type);
  }

  @Override
  public SysThirdUser findSysThirdUser(Integer type, Long psnId) throws ServiceException {
    SysThirdUser sysThirdUser = sysThirdUserDao.findByTypeAndPsnId(type, psnId);
    return sysThirdUser;
  }

  // -------------------------------------
  @Override
  public void updateUnionID(Integer type, String thirdId, String unionId) throws ServiceException {
    try {

      this.sysThirdUserDao.updateUnionID(type, thirdId, unionId);

    } catch (Exception e) {
      logger.error("根据第三方用户的昵称异常，type=" + type + "，thirdId=" + thirdId + "，unionId=" + unionId, e);
      throw new ServiceException("根据第三方用户的昵称异常，type=" + type + "，thirdId=" + thirdId + "，unionId=" + unionId, e);
    }

  }

  @Override
  public List<String> findByType(Integer type) throws ServiceException {
    return sysThirdUserDao.findByType(type);
  }

  @Override
  public void saveThirdLogin2(Long psnId, Integer type, String thirdId, String qqUnionId, String nickName)
      throws ServiceException {
    try {
      Long psnIdTemp = this.findPsnId(type, thirdId);
      if (psnIdTemp != null) {
        return;
      }
      // SysThirdUser sysThirdUser = new SysThirdUser(psnId, type,
      // thirdId, new Date());
      SysThirdUser sysThirdUser = new SysThirdUser(psnId, type, thirdId, new Date(), nickName, qqUnionId);
      this.sysThirdUserDao.save(sysThirdUser);
    } catch (Exception e) {
      logger.error("保存第三方登录信息的时候出错，psnId={}，type={}，thirdId={},qqUnionId", psnId, type, thirdId, qqUnionId, e);
      throw new ServiceException(
          "保存第三方登录信息的时候出错，psnId=" + psnId + "，type=" + type + "，thirdId=" + thirdId + "，qqUnionId=" + qqUnionId, e);
    }
  }

  @Override
  public void saveThirdSysUserNew(Long psnId, Integer type, String thirdId, String unionId, String nickName)
      throws ServiceException {
    Long psnIdTemp = sysThirdUserDao.findUnionID(type, unionId);
    if (psnIdTemp != null) {
      return;
    }
    SysThirdUser sysThirdUser = new SysThirdUser(psnId, type, thirdId, new Date(), nickName, unionId);
    sysThirdUserDao.save(sysThirdUser);
  }

}
