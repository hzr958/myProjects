package com.smate.web.psn.service.thrid.account;

import com.smate.core.base.utils.dao.security.UserDao;
import com.smate.core.base.utils.model.cas.security.User;
import com.smate.core.base.utils.string.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.utils.dao.wechat.WeChatRelationDao;
import com.smate.core.base.utils.model.wechat.WeChatRelation;
import com.smate.core.base.utils.url.HttpRequestUtils;
import com.smate.web.psn.dao.open.OpenUserUnionDao;
import com.smate.web.psn.dao.third.user.SysThirdUserDao;
import com.smate.web.psn.form.PsnSettingForm;
import com.smate.web.psn.model.third.user.SysThirdUser;

/**
 * 
 * @author aijiangbin
 *
 */
@Service("thirdAccountRelationService")
@Transactional(rollbackFor = Exception.class)
public class ThirdAccountRelationServiceImpl implements ThirdAccountRelationService {

  @Autowired
  private SysThirdUserDao sysThirdUserDao;
  @Autowired
  private WeChatRelationDao weChatRelationDao;
  @Autowired
  private OpenUserUnionDao openUserUnionDao;
  @Autowired
  private UserDao userDao;

  /**
   * 得到第三方关联信息
   */
  @Override
  public void getThirdAccountBindInfo(PsnSettingForm form) throws Exception {
    SysThirdUser thirdUser = sysThirdUserDao.find(SysThirdUser.TYPE_QQ, form.getPsnId());
    if (thirdUser != null) {
      form.setBindQQ(true);
      form.setNickNameQQ(thirdUser.getNickName());
    }
    Long openId = openUserUnionDao.getOpenIdByPsnId(form.getPsnId());
    WeChatRelation weChatRelation = weChatRelationDao.getBySmateOpenId(openId);
    if (weChatRelation != null) {
      form.setBindWX(true);
      form.setNickNameWC(weChatRelation.getNickName());
    }
    User user = userDao.get(form.getPsnId());
    if(user !=null && StringUtils.isNotBlank(user.getMobileNumber())){
      form.setBindMobile(true);
      form.setMobileNum(user.getMobileNumber());
    }
  }

  /**
   * 取消QQ绑定
   * 
   * @param form
   */
  @Override
  public void cancelQQBind(PsnSettingForm form) throws Exception {
    sysThirdUserDao.delete(SysThirdUser.TYPE_QQ, form.getPsnId());

  }

  /**
   * 取消微信绑定
   * 
   * @param form
   */
  @Override
  public void cancelWCBind(PsnSettingForm form) throws Exception {
    Long openId = openUserUnionDao.getOpenIdByPsnId(form.getPsnId());
    weChatRelationDao.cancelBindBySmateId(openId);
  }

  public void getQQUnionID(PsnSettingForm form) throws Exception {
    Long openId = openUserUnionDao.getOpenIdByPsnId(form.getPsnId());
    String result = HttpRequestUtils.sendGet("https://graph.qq.com/oauth2.0/get_unionid",
        "openid=" + openId + "&client_id=101190413");
    String unionId = result.substring(result.indexOf("UID"), result.lastIndexOf('"'));

  }

}
