package com.smate.web.dyn.action.dynamic.grp.data;

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
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.security.TheadLocalPsnId;
import com.smate.core.base.utils.struts2.Struts2Utils;
import com.smate.web.dyn.form.dynamic.group.GroupDynOptForm;
import com.smate.web.dyn.service.dynamic.group.GroupDynamicOptService;

public class GroupDynamicOptDataAction extends ActionSupport implements Preparable, ModelDriven<GroupDynOptForm> {

  /**
   * 
   */
  private static final long serialVersionUID = -5050867127561568019L;

  protected final Logger logger = LoggerFactory.getLogger(getClass());
  private GroupDynOptForm form;

  @Resource
  private GroupDynamicOptService groupDynamicOptService;

  /**
   * 赞操作 （赞动态/取消动态）
   * 
   * @return
   */
  @Action("/dyndata/grpdyn/award")
  public void ajaxAwardDyn() {
    Map<String, Object> map = new HashMap<String, Object>();
    Long currentPsnId = NumberUtils.toLong(Des3Utils.decodeFromDes3(form.getDes3PsnId()), 0L);
    if (NumberUtils.isNotNullOrZero(form.getDynId()) && NumberUtils.isNotNullOrZero(currentPsnId)) {
      try {
        form.setPsnId(currentPsnId);
        TheadLocalPsnId.setPsnId(currentPsnId);
        map = groupDynamicOptService.groupDynAward(form);
        map.put("result", "success");
      } catch (Exception e) {
        map.put("result", "error");
        logger.error("群组动态赞操作失败！dynId={}", form.getDynId(), e);
      }
    } else {
      map.put("result", "error");
    }
    Struts2Utils.renderJson(map, "encoding:UTF-8");
  }


  /**
   * 评论群组动态
   * 
   * @return
   */
  @Action("/dyndata/grpdyn/docomment")
  public void commentGrpDyn() {
    Map<String, Object> map = new HashMap<String, Object>();
    Long currentPsnId = NumberUtils.toLong(Des3Utils.decodeFromDes3(form.getDes3PsnId()), 0L);
    String status = "error";
    try {
      if (NumberUtils.isNotNullOrZero(form.getDynId()) && NumberUtils.isNotNullOrZero(currentPsnId)
          && (StringUtils.isNotBlank(form.getCommentContent())
              || NumberUtils.isNotNullOrZero(form.getCommentResId()))) {
        form.setPsnId(currentPsnId);
        TheadLocalPsnId.setPsnId(currentPsnId);
        groupDynamicOptService.groupDynComment(form);
        status = "success";
      }
    } catch (Exception e) {
      logger.error("群组动态评论操作失败！dynId={}, psnId={}", form.getDynId(), currentPsnId, e);
    }
    map.put("commentCount", form.getCommentCount() != null ? form.getCommentCount() : 0);
    map.put("result", status);
    Struts2Utils.renderJson(map, "encoding:UTF-8");
  }


  /**
   * 记录群组分享操作
   * 
   * @return
   */
  @Action("/dyndata/grpdyn/share")
  public String ajaxShareDyn() {
    Map<String, Object> map = new HashMap<String, Object>();
    String status = "error";
    Long psnId = NumberUtils.toLong(Des3Utils.decodeFromDes3(form.getDes3PsnId()), 0L);
    if (NumberUtils.isNotNullOrZero(form.getDynId()) && NumberUtils.isNotNullOrZero(psnId)) {
      try {
        form.setPsnId(psnId);
        groupDynamicOptService.groupDynShare(form);
        status = "success";
      } catch (Exception e) {
        logger.error("群组动态分享操作失败！dynId=" + form.getDynId() + e.toString());
      }
    }
    map.put("status", status);
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
