package com.smate.center.batch.service.mail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.service.user.UserService;
import com.smate.core.base.utils.model.cas.security.User;

/**
 * 接收人获取辅助接口实现.
 * 
 * @author pwl
 * 
 */
@Service("receiverService")
@Transactional(rollbackFor = Exception.class)
public class ReceiverServiceImpl implements ReceiverService {

  /**
   * 
   */
  private static final long serialVersionUID = 3688715437283133491L;
  private static Logger logger = LoggerFactory.getLogger(ReceiverServiceImpl.class);
  @Autowired
  private PersonEmailManager personEmailManager;
  @Autowired
  private UserService userService;

  @SuppressWarnings("rawtypes")
  @Override
  public Map<String, List> getReceivePsnIdAndEmail(String receiversStr) throws ServiceException {
    // 匹配到系统用户保存psnId
    List<Long> psnIdList = new ArrayList<Long>();
    // 没有匹配到系统用户则保存email
    List<String> emailList = new ArrayList<String>();
    try {
      String[] receiverStrArry = receiversStr.split(",");
      for (String receiverStr : receiverStrArry) {
        Long psnId = null;
        String email = null;
        if (StringUtils.isNotBlank(receiverStr) && receiverStr.matches("^\\d+$")) {
          psnId = NumberUtils.createLong(receiverStr);
        } else {
          if (StringUtils.indexOf(receiverStr, "@") > -1) { // 直接输入邮件
            email = receiverStr;
            // 根据用户录入的邮件查找相应的用户列表
            List<User> userList = userService.findUserByLoginNameOrEmail(email);
            if (CollectionUtils.isNotEmpty(userList)) {
              int verifyEmailCount = 0;
              for (User user : userList) {
                // 查看该人该邮件是否已经经过确认
                boolean isVerify = personEmailManager.isPsnVerified(user.getId(), email);
                if (isVerify) {
                  psnId = user.getId();
                  verifyEmailCount++;
                }
              }

              // 如果不止一个用户确认这个邮箱，则表示没有匹配到用户并将psnId置为null
              if (verifyEmailCount != 1) {
                psnId = null;
              }
            }
          }
        }

        if (psnId != null) {
          psnIdList.add(psnId);
        } else if (StringUtils.isNotBlank(email)) {
          emailList.add(email);
        }
      }
    } catch (Exception e) {
      logger.error("分享资源构造资源接收人出现异常：", e);
      throw new ServiceException(e);
    }

    Map<String, List> returnMap = new HashMap<String, List>();
    returnMap.put("psnIdList", psnIdList);
    returnMap.put("emailList", emailList);
    return returnMap;
  }

}
