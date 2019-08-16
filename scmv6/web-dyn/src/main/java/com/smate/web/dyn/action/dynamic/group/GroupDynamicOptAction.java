package com.smate.web.dyn.action.dynamic.group;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.smate.core.base.utils.struts2.Struts2Utils;
import com.smate.web.dyn.form.dynamic.group.GroupDynOptForm;
import com.smate.web.dyn.service.dynamic.group.GroupDynamicOptService;

public class GroupDynamicOptAction extends ActionSupport implements Preparable, ModelDriven<GroupDynOptForm> {

  /**
   * 
   */
  private static final long serialVersionUID = -5050867127561568019L;

  protected final Logger logger = LoggerFactory.getLogger(getClass());
  private GroupDynOptForm form;

  @Resource
  private GroupDynamicOptService groupDynamicOptService;

  /**
   * 删除群组动态
   * 
   * @return
   */
  @Action("/dynweb/dynamic/groupdyn/ajaxdeldyn")
  public String ajaxDelDyn() {

    return null;
  }

  /**
   * 赞操作 （赞动态/取消动态）
   * 
   * @return
   */
  @Action("/dynweb/dynamic/groupdyn/ajaxaward")
  public String ajaxAwardDyn() {
    Map<String, Object> map = new HashMap<String, Object>();
    if (form.getDynId() != null && form.getDynId() != 0L && form.getPsnId() != 0L) {
      try {
        map = groupDynamicOptService.groupDynAward(form);
        map.put("result", "success");
      } catch (Exception e) {
        map.put("result", "error");
        logger.error("群组动态赞操作失败！dynId=" + form.getDynId() + e.toString());
      }

    } else {
      map.put("result", "error");
    }
    Struts2Utils.renderJson(map, "encoding:UTF-8");
    return null;
  }

  /**
   * 评论操作
   * 
   * @return
   */
  @Action("/dynweb/dynamic/groupdyn/ajaxcomment")
  public String ajaxCommentDyn() {
    Map<String, Object> map = new HashMap<String, Object>();
    if (form.getDynId() != null && form.getDynId() != 0L && form.getPsnId() != 0L
        && (StringUtils.isNotBlank(form.getCommentContent()) || form.getCommentResId() != null)) {
      try {
        groupDynamicOptService.groupDynComment(form);
        map.put("result", "success");
      } catch (Exception e) {
        map.put("result", "error");
        logger.error("群组动态评论操作失败！dynId=" + form.getDynId(), e);
      }
    } else {
      map.put("result", "error");
    }
    Struts2Utils.renderJson(map, "encoding:UTF-8");
    return null;
  }

  /**
   * 分享操作
   * 
   * @return
   */
  @Action("/dynweb/dynamic/groupdyn/ajaxshare")
  public String ajaxShareDyn() {
    Map<String, Object> map = new HashMap<String, Object>();
    if (form.getDynId() != null && form.getDynId() != 0L) {
      try {
        groupDynamicOptService.groupDynShare(form);
        map.put("result", "success");
      } catch (Exception e) {
        map.put("result", "error");
        logger.error("群组动态分享操作失败！dynId=" + form.getDynId() + e.toString());
      }
    } else {
      map.put("result", "error");
    }
    Struts2Utils.renderJson(map, "encoding:UTF-8");
    return null;
  }

  @Override
  public GroupDynOptForm getModel() {
    return form;
  }

  @Override
  public void prepare() throws Exception {
    if (form == null) {
      form = new GroupDynOptForm();
    }
  }

}
