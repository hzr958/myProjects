package com.smate.center.open.service.data.wechat;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.open.consts.OpenConsts;
import com.smate.center.open.consts.OpenMsgCodeConsts;
import com.smate.center.open.dao.wechat.WeChatMessagePublicDao;
import com.smate.center.open.exception.OpenSerSaveWechatPublicMsgException;
import com.smate.center.open.model.wechat.WeChatMessagePublic;
import com.smate.center.open.service.data.ThirdDataTypeBase;

/**
 * 开放数据 保存 公共微信消息 实现
 * 
 * @author tsz
 * 
 * @since 6.0.1
 * @version 6.0.1
 */

@Transactional(rollbackFor = Exception.class)
public class ThirdWeChatPublicMsgImpl extends ThirdDataTypeBase {

  protected Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private WeChatMessagePublicDao weChatMessagePublicDao;

  /**
   * 校验参数 主要是校验业务参数
   */
  @Override
  public Map<String, Object> doVerify(Map<String, Object> paramet) {
    Map<String, Object> temp = new HashMap<String, Object>();
    Object data = paramet.get(OpenConsts.MAP_DATA);
    Object openId = paramet.get(OpenConsts.MAP_OPENID);
    if (!OpenConsts.SYSTEM_OPENID.toString().equals(openId.toString())) {
      logger.error("群发消息  openId不正确");
      temp = super.errorMap(OpenMsgCodeConsts.SCM_212, paramet, "");
      return temp;
    }
    if (data == null || data.toString().length() == 0) {
      logger.error("微信消息内容 不能为空");
      temp = super.errorMap(OpenMsgCodeConsts.SCM_210, paramet, "");
      return temp;
    }
    // TODO 等群发消息 放开后 需要判断参数
    temp.put(OpenConsts.RESULT_STATUS, OpenConsts.RESULT_STATUS_SUCCESS);
    return temp;
  }

  /**
   * 处理数据
   */
  @Override
  public Map<String, Object> doHandler(Map<String, Object> paramet) {
    try {
      WeChatMessagePublic temp = new WeChatMessagePublic();
      temp.setContent(paramet.get(OpenConsts.MAP_DATA).toString());
      temp.setCreateTime(new Date());
      temp.setOpenId(Long.parseLong(paramet.get(OpenConsts.MAP_OPENID).toString()));
      temp.setStatus(0);
      temp.setToken(paramet.get(OpenConsts.MAP_TOKEN).toString());
      weChatMessagePublicDao.save(temp);
      return super.successMap("保存数据成功", null);
    } catch (Exception e) {
      logger.error("保存群发微信消息出错" + paramet.toString());
      throw new OpenSerSaveWechatPublicMsgException(e);
    }
  }

}
