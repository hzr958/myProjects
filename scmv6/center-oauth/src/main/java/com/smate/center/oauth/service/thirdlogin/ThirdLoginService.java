package com.smate.center.oauth.service.thirdlogin;

import java.util.List;

import com.smate.center.oauth.exception.ServiceException;
import com.smate.center.oauth.model.thirduser.SysThirdUser;

/**
 * 第三方登录服务
 * 
 * @author Scy
 * 
 */

public interface ThirdLoginService {

  /**
   * 保存第三方登录
   * 
   * @param psnId
   * @param type
   * @param thirdId
   * @throws ServiceException
   */
  public void saveThirdLogin(Long psnId, Integer type, String thirdId) throws ServiceException;

  /**
   * 根据第三方ID获取psnId
   * 
   * @param type
   * @param thirdId
   * @return
   * @throws ServiceException
   */
  public Long findPsnId(Integer type, String thirdId) throws ServiceException;

  /**
   * 保存第三方登录
   * 
   * @param psnId
   * @param type
   * @param thirdId
   * @throws ServiceException
   */
  public void saveThirdLogin(Long psnId, Integer type, String thirdId, String nickName, String qqUnionId)
      throws ServiceException;

  /**
   * 保存QQ第三方登录，使用QQ unionID关联
   * 
   * @param psnId
   * @param type
   * @param thirdId
   * @throws ServiceException
   */
  public void saveThirdLogin2(Long psnId, Integer type, String thirdId, String qqUnionId, String nickName)
      throws ServiceException;

  /**
   * 更新昵称
   * 
   * @param thirdId
   * @param type
   * @param nickName
   * @throws ServiceException
   */
  public void updateNickname(Integer type, String thirdId, String nickName) throws ServiceException;

  public void delete(Long psnId, String qqOpenId, int type) throws ServiceException;

  /**
   * 查找psnId 绑定的账号
   * 
   * @param type
   * @param psnId
   * @return
   * @throws ServiceException
   */
  public SysThirdUser findSysThirdUser(Integer type, Long psnId) throws ServiceException;

  // ---------------------------------------------
  /**
   * 更新UnionID
   * 
   * @param type
   * @param thirdId
   * @param unionId
   * @throws ServiceException
   */
  public void updateUnionID(Integer type, String thirdId, String unionId) throws ServiceException;

  /**
   * 根据类型查询
   * 
   * @param type
   * @throws ServiceException
   */
  public List<String> findByType(Integer type) throws ServiceException;

  /**
   * 根据第三方unionId获取psnId
   * 
   * @param type
   * @param thirdId
   * @return
   * @throws ServiceException
   */
  public Long findUnionId(Integer type, String unionId) throws ServiceException;

  /**
   * 保存第三方账号（QQ\微博）记录
   * 
   * @param psnId
   * @param type
   * @param thirdId
   * @param qqUnionId
   * @param nickName
   * @throws ServiceException
   */
  public void saveThirdSysUserNew(Long psnId, Integer type, String thirdId, String unionId, String nickName)
      throws ServiceException;

}
