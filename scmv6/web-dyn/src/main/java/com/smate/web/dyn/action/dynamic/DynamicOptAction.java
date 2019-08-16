package com.smate.web.dyn.action.dynamic;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.struts2.Struts2Utils;
import com.smate.web.dyn.form.dynamic.DynamicForm;
import com.smate.web.dyn.service.dynamic.*;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Actions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

/**
 * 动态操作Action
 * 
 * @author zk
 *
 */
public class DynamicOptAction extends ActionSupport implements ModelDriven<DynamicForm>, Preparable {

  private static final long serialVersionUID = -303698274498430054L;
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  private DynamicForm form;
  @Autowired
  private DynamicService dynamicService;
  @Autowired
  private DynamicAwardService dynamicAwardService;
  @Autowired
  private DynamicReplyService dynamicReplyService;
  @Autowired
  private DynamicQuickShareService dynamicQuickShareService;
  @Autowired
  private DynamicShareService dynamicShareService;

  /**
   * 资源分享到站外
   * 
   * @return
   */
  @SuppressWarnings("rawtypes")
  @Actions({@Action("/dynweb/dynamic/ajaxgetsharetxt"), @Action("/dynweb/outside/ajaxgetsharetxt")})
  public String getShareTxt() {
    Map<String, Object> map = new HashMap<String, Object>();
    try {
      dynamicService.getShareTxt(form);
      map.put("shareTxt", JacksonUtils.jsonObjectSerializer(form.getResultMap()));
      map.put("result", "success");
    } catch (Exception e) {
      map.put("result", "error");
      logger.error("资源分享到站外出错", e);
    }
    Struts2Utils.renderJson(map, "encoding:utf-8");
    return null;
  }

  /**
   * 分享到站外，ajax添加分享记录
   * 
   * @return
   * @throws Exception
   */
  @Action("/dynamic/ajaxAddShareCounts")
  public String ajaxAddShareCounts() throws Exception {
    dynamicShareService.ajaxAddResShareCounts(form);
    return null;
  }

  /**
   * 删除动态
   * 
   * @return
   */
  @SuppressWarnings("rawtypes")
  @Action("/dynweb/dynamic/ajaxdeletedyn")
  public String deleteDyn() {
    Map<String, Object> map = new HashMap<String, Object>();
    try {
      if (form.getPsnId() != null && form.getPsnId() != 0L && form.getDynId() != null) {
        dynamicService.deleteDyn(form);
        map.put("result", "success");
      } else {
        map.put("result", "error");
      }
    } catch (Exception e) {
      map.put("result", "error");
      logger.error("删除动态异常：psnId=" + form.getPsnId() + " dynId=" + form.getDynId() + e.toString());
    }
    Struts2Utils.renderJson(map, "encoding:utf-8");
    return null;
  }

  /**
   * 屏蔽个人动态
   */
  @SuppressWarnings("rawtypes")
  @Action("/dynweb/dynamic/ajaxskippsndyn")
  public String skipPsnDyn() {
    Map map = new HashMap();
    try {
      dynamicService.skipPsnDyn(form);
    } catch (Exception e) {

    }
    Struts2Utils.renderJson(map, "utf-8");
    return null;
  }

  /**
   * 屏蔽此类动态
   * 
   * @return
   */
  @SuppressWarnings("rawtypes")
  @Action("/dynweb/dynamic/ajaxskiptypedyn")
  public String skipTypeDyn() {
    Map map = new HashMap();
    try {
      dynamicService.skipTypeDyn(form);
    } catch (Exception e) {

    }
    Struts2Utils.renderJson(map, "utf-8");
    return null;
  }

  /**
   * 评论动态
   * 
   * @return
   */
  @Action("/dynweb/dynamic/ajaxreplydyn")
  public String replyDyn() {
    Map<String, Object> map = new HashMap<String, Object>();
    try {
      form.setLocale(this.getLocale().toString());
      dynamicReplyService.replyDyn(form);
      map.put("dynReplayInfo", form.getDynReplayInfoList().get(0));
      map.put("result", "success");
    } catch (Exception e) {
      map.put("result", "fail");
      logger.error("评论动态出错", e);
    }
    Struts2Utils.renderJson(map, "encoding:UTF-8");
    return null;
  }

  /**
   * 快速分享
   * 
   */
  @Action("/dynweb/dynamic/ajaxquickshare")
  public String ajaxquickShareDyn() {
    Map<String, Object> map = new HashMap<String, Object>();
    try {
      dynamicQuickShareService.quickShare(form);
      map.put("result", "success");
    } catch (Exception e) {
      logger.error("快速分出错,dynId=" + form.getParentDynId(), e);
      map.put("result", "fail");
    }
    Struts2Utils.renderJson(map, "encoding:UTF-8");
    return null;
  }

  @Override
  public void prepare() throws Exception {
    if (form == null) {
      form = new DynamicForm();
    }
    form.setPsnId(SecurityUtils.getCurrentUserId());
  }

  @Override
  public DynamicForm getModel() {
    return form;
  }

}
