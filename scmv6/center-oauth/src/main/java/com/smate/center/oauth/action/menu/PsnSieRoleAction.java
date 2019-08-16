package com.smate.center.oauth.action.menu;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.math.NumberUtils;
import org.apache.struts2.convention.annotation.Action;
import org.codehaus.plexus.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.smate.center.oauth.model.profile.PsnSieRoleForm;
import com.smate.center.oauth.service.security.UserRoleService;
import com.smate.core.base.utils.common.CommonUtils;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.struts2.Struts2Utils;

/**
 * SIE人员角色处理 由于不想在每个web项目都加上SIE的新的数据源， 所以在oauth这边处理SIE人员角色， 供菜单显示SIE相关信息
 * 
 * @author wsn
 * @date 2018年6月7日
 */
public class PsnSieRoleAction extends ActionSupport implements ModelDriven<PsnSieRoleForm>, Preparable {

  private static final long serialVersionUID = -2984831592171035720L;

  private PsnSieRoleForm form;
  @Autowired
  private UserRoleService userRoleService;

  @Override
  public void prepare() throws Exception {
    if (form == null) {
      form = new PsnSieRoleForm();
    }
  }

  @Override
  public PsnSieRoleForm getModel() {
    return form;
  }

  /**
   * 查询人员在某个机构是否有多个角色
   */
  @Action("/oauth/sierole/ajaxjudge")
  public String judgePsnHasMultiRole() {
    Map<String, String> result = new HashMap<String, String>();
    try {
      String msg = checkParams(form);
      if (StringUtils.isNotBlank(msg)) {
        result.put("result", "error");
        result.put("msg", msg);
      } else {

        // 根据sys去SIE新库或旧库查询人员角色信息
        form.setRolMultiRole(userRoleService.HasMultiRoleInSie(form));
        result.put("result", "success");
        result.put("hasMultiRole", form.getRolMultiRole() ? "1" : "0");
      }
    } catch (Exception e) {
      result.put("result", "error");
      result.put("msg", "service exception");
    }
    Struts2Utils.renderJson(result, "encoding: utf-8");
    return null;
  }

  /**
   * 校验参数 只能传加密参数
   * 
   * @param form
   * @return
   */
  private String checkParams(PsnSieRoleForm form) {
    String msg = null;
    // 校验人员ID是否有传
    if (StringUtils.isNotBlank(form.getDes3PsnId())) {
      Long psnId = NumberUtils.toLong(Des3Utils.decodeFromDes3(form.getDes3PsnId()));
      if (CommonUtils.compareLongValue(psnId, 0L)) {
        msg = "psnId equals 0";
        return msg;
      } else {
        form.setPsnId(psnId);
      }
    } else {
      msg = "psnId is null";
      return msg;
    }
    // 校验单位ID是否有传
    if (StringUtils.isNotBlank(form.getDes3InsId())) {
      Long insId = NumberUtils.toLong(Des3Utils.decodeFromDes3(form.getDes3InsId()));
      if (CommonUtils.compareLongValue(insId, 0L)) {
        msg = "insId equals 0";
      } else {
        form.setInsId(insId);
      }
    } else {
      msg = "insId is null";
    }
    return msg;
  }

}
