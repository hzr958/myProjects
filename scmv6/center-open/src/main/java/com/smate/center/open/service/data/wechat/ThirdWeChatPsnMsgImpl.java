package com.smate.center.open.service.data.wechat;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.open.consts.OpenConsts;
import com.smate.center.open.consts.OpenMsgCodeConsts;
import com.smate.center.open.dao.wechat.WeChatMessagePsnDao;
import com.smate.center.open.dao.wechat.template.SmateWeChatTemplateDao;
import com.smate.center.open.exception.OpenSerSaveWechatPsnMsgException;
import com.smate.center.open.model.wechat.WeChatMessagePsn;
import com.smate.center.open.model.wechat.template.SmateWeChatTemplate;
import com.smate.center.open.service.data.ThirdDataTypeBase;
import com.smate.core.base.utils.dao.wechat.WeChatRelationDao;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.model.wechat.WeChatRelation;

/**
 * 开放数据 保存 个人微信消息 实现
 * 
 * @author tsz
 * 
 * @since 6.0.1
 * @version 6.0.1
 */

@Transactional(rollbackFor = Exception.class)
public class ThirdWeChatPsnMsgImpl extends ThirdDataTypeBase {

  protected Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private WeChatMessagePsnDao weChatMessagePsnDao;
  @Autowired
  private SmateWeChatTemplateDao smateWeChatTemplateDao;
  @Autowired
  private WeChatRelationDao weChatRelationDao;

  /**
   * 校验参数 主要是校验业务参数 //需要抽取 多个模板的时候由不同服务 提供验证方法
   */
  @Override
  public Map<String, Object> doVerify(Map<String, Object> paramet) {
    Map<String, Object> temp = new HashMap<String, Object>();
    Object data = paramet.get(OpenConsts.MAP_DATA);
    if (data == null || data.toString().length() == 0) {
      logger.error("微信个人消息数据 不 能为空");
      temp = super.errorMap(OpenMsgCodeConsts.SCM_211, paramet, "");
      return temp;
    }
    @SuppressWarnings("unchecked")
    Map<String, Object> dataMap = JacksonUtils.jsonToMap(data.toString());
    if (dataMap == null) {
      logger.error("微信消息数据 参数 格式不正确 不能转换成map");
      temp = super.errorMap(OpenMsgCodeConsts.SCM_213, paramet, "");
      return temp;
    }
    if (dataMap.get(OpenConsts.WECHAT_DATA_FIRST) == null
        || dataMap.get(OpenConsts.WECHAT_DATA_FIRST).toString().length() == 0) {
      logger.error("微信消息  用户尊称提示不能为空");
      temp = super.errorMap(OpenMsgCodeConsts.SCM_221, paramet, "");
      return temp;
    }
    if (dataMap.get(OpenConsts.WECHAT_DATA_REMARK) == null
        || dataMap.get(OpenConsts.WECHAT_DATA_REMARK).toString().length() == 0) {
      logger.error("微信消息  补充说明不能为空");
      temp = super.errorMap(OpenMsgCodeConsts.SCM_222, paramet, "");
      return temp;
    }
    if (dataMap.get(OpenConsts.WECHAT_DATA_KEYWORD2) == null
        || dataMap.get(OpenConsts.WECHAT_DATA_KEYWORD2).toString().length() == 0) {
      logger.error("微信消息  消息内容不能为空");
      temp = super.errorMap(OpenMsgCodeConsts.SCM_214, paramet, "");
      return temp;
    }
    if (dataMap.get(OpenConsts.WECHAT_DATA_SMATE_TEMP_ID) == null
        || dataMap.get(OpenConsts.WECHAT_DATA_SMATE_TEMP_ID).toString().length() == 0) {
      logger.error("微信消息  消息类型不能为空");
      temp = super.errorMap(OpenMsgCodeConsts.SCM_215, paramet, "");
      return temp;
    }
    if (dataMap.get(OpenConsts.WECHAT_DATA_KEYWORD1) == null
        || dataMap.get(OpenConsts.WECHAT_DATA_KEYWORD1).toString().length() == 0) {
      logger.error("微信消息 消息发送机构 不能为空");
      temp = super.errorMap(OpenMsgCodeConsts.SCM_216, paramet, "");
      return temp;
    }
    if (dataMap.get(OpenConsts.WECHAT_DATA_KEYWORD3) == null
        || dataMap.get(OpenConsts.WECHAT_DATA_KEYWORD3).toString().length() == 0) {
      logger.error("微信消息 消息时间描述不能为空");
      temp = super.errorMap(OpenMsgCodeConsts.SCM_217, paramet, "");
      return temp;
    }
    int tempLength = dataMap.get(OpenConsts.WECHAT_DATA_REMARK).toString().length()
        + dataMap.get(OpenConsts.WECHAT_DATA_FIRST).toString().length()
        + dataMap.get(OpenConsts.WECHAT_DATA_KEYWORD2).toString().length()
        + dataMap.get(OpenConsts.WECHAT_DATA_KEYWORD1).toString().length()
        + dataMap.get(OpenConsts.WECHAT_DATA_KEYWORD3).toString().length();
    if (tempLength > OpenConsts.WECHAT_MAX_LENGTH || tempLength < OpenConsts.WECHAT_MIN_LENGTH) {
      logger.error("微信消息 显示内容长度不正确");
      temp = super.errorMap(OpenMsgCodeConsts.SCM_218, paramet, "");
      return temp;
    }
    if (!NumberUtils.isNumber(dataMap.get(OpenConsts.WECHAT_DATA_SMATE_TEMP_ID).toString())) {
      logger.error("微信消息  消息类型格式不 正确");
      temp = super.errorMap(OpenMsgCodeConsts.SCM_219, paramet, "");
      return temp;
    }
    // 判断 openId 是不是 已经绑定 微信用户
    WeChatRelation wechatTemp =
        weChatRelationDao.getBySmateOpenId(Long.parseLong(paramet.get(OpenConsts.MAP_OPENID).toString()));
    if (wechatTemp == null) {
      logger.error("微信消息  openId没有绑定微信用户");
      temp = super.errorMap(OpenMsgCodeConsts.SCM_223, paramet, "");
      return temp;
    }
    SmateWeChatTemplate template =
        smateWeChatTemplateDao.get(Long.parseLong(dataMap.get(OpenConsts.WECHAT_DATA_SMATE_TEMP_ID).toString()));
    if (template == null) {
      logger.error("微信消息  没有找到 对应的消息类型");
      temp = super.errorMap(OpenMsgCodeConsts.SCM_220, paramet, "");
      return temp;
    }
    // 参数验证通过
    temp.put(OpenConsts.RESULT_STATUS, OpenConsts.RESULT_STATUS_SUCCESS);
    return temp;
  }

  /**
   * 处理数据
   */
  @Override
  public Map<String, Object> doHandler(Map<String, Object> paramet) {
    try {
      WeChatMessagePsn temp = new WeChatMessagePsn();
      temp.setContent(paramet.get(OpenConsts.MAP_DATA).toString());
      temp.setCreateTime(new Date());
      temp.setOpenId(Long.parseLong(paramet.get(OpenConsts.MAP_OPENID).toString()));
      temp.setStatus(0);
      temp.setToken(paramet.get(OpenConsts.MAP_TOKEN).toString());
      weChatMessagePsnDao.save(temp);
      return super.successMap("保存数据成功", null);
    } catch (Exception e) {
      logger.error("保存个人微信消息出错" + paramet.toString());
      throw new OpenSerSaveWechatPsnMsgException(e);
    }
  }

}
