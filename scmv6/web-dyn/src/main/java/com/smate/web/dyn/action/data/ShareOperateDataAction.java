package com.smate.web.dyn.action.data;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.struts2.convention.annotation.Action;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.struts2.Struts2Utils;
import com.smate.web.dyn.form.dynamic.DynamicForm;
import com.smate.web.dyn.form.dynamic.ShareOperateForm;
import com.smate.web.dyn.form.msg.MsgShowForm;
import com.smate.web.dyn.service.dynamic.DynamicService;
import com.smate.web.dyn.service.msg.ShowMsgService;

public class ShareOperateDataAction extends ActionSupport implements ModelDriven<ShareOperateForm>, Preparable {
  private static final long serialVersionUID = 5680698208517165621L;
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  private ShareOperateForm form;
  @Autowired
  private DynamicService dynamicService;
  @Resource
  private ShowMsgService showMsgService;

  /**
   * 更新资源的分享统计数
   * 
   * @return
   */
  @Action("/dyndata/share/updatesharestatic")
  public String updateShareStatic() {
    Map<String, String> resultMap = new HashMap<>();
    try {
      if (checkParam(form)) {
        DynamicForm dynForm = new DynamicForm();
        dynForm.setResId(form.getResId());
        dynForm.setResType(form.getResType());
        dynForm.setPlatform(form.getPlatform().toString());
        dynForm.setPsnId(NumberUtils.toLong(Des3Utils.decodeFromDes3(form.getDes3PsnId())));
        dynForm.setDynText(form.getShareText());
        dynamicService.updateResShareStatic(dynForm);
        resultMap.put("status", "success");
      } else {
        resultMap.put("status", "error");
        resultMap.put("errorMsg", "des3ResId或shareType不能为空");
      }
    } catch (Exception e) {
      resultMap.put("status", "error");
      logger.error("更新资源的分享统计数出错，resdes3Id={},resType={}", form.getDes3ResId(), form.getResType(), e);
    }
    Struts2Utils.renderJson(resultMap, "encoding:utf-8");
    return null;
  }

  /**
   * app站内信聊天检索会话列表
   * 
   * @return
   */
  @Action("/dyndata/msg/getsearchchatpsnlist")
  public String getSearchChatPsnList() {
    Map<String, Object> resultMap = new HashMap<>();
    try {
      MsgShowForm msgForm = new MsgShowForm();
      msgForm.setSearchKey(form.getSearchKey());
      msgForm.setPsnId(NumberUtils.toLong(Des3Utils.decodeFromDes3(form.getDes3PsnId())));
      showMsgService.getSearchChatPsnList(msgForm);
      resultMap.put("status", "success");
      resultMap.put("msgShowInfoList", msgForm.getMsgShowInfoList());
    } catch (Exception e) {
      resultMap.put("status", "error");
      logger.error("更新资源的分享统计数出错，resdes3Id={},resType={}", form.getDes3ResId(), form.getResType(), e);
    }
    Struts2Utils.renderJson(resultMap, "encoding:utf-8");
    return null;
  }

  /**
   * 参数校验
   * 
   * @param param
   * @return
   */
  private boolean checkParam(ShareOperateForm form) {
    String des3ResId = form.getDes3ResId();// 分享的资源id
    Integer shareType = form.getResType();// 分享的资源类型
    Integer platform = form.getPlatform();// 分享平台
    if (StringUtils.isNotBlank(des3ResId) && shareType != null && platform != null) {
      form.setResId(NumberUtils.toLong(Des3Utils.decodeFromDes3(des3ResId)));
      return true;
    }
    return false;
  }

  @Override
  public void prepare() throws Exception {
    if (form == null) {
      form = new ShareOperateForm();
    }

  }

  @Override
  public ShareOperateForm getModel() {
    return form;
  }

}
