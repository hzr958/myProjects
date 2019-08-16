package com.smate.web.dyn.service.msg;

import com.smate.web.dyn.form.msg.MsgShowForm;
import com.smate.web.dyn.form.msg.mobile.MobileMsgShowForm;
import com.smate.web.dyn.model.msg.MsgRelation;

/**
 * 消息构建显示服务接口
 * 
 * @author zzx
 *
 */
public interface BuildMsgInfoService {

  /**
   * 构建数据显示对象
   * 
   * @param form
   */
  void buildMsgShowInfo(MsgShowForm form, MsgRelation m) throws Exception;

  /**
   * 移动端-构建数据显示对象
   * 
   * @param form
   * @param msgRelation
   */
  void buildMobileMsgShowInfo(MobileMsgShowForm form, MsgRelation msgRelation) throws Exception;
}
