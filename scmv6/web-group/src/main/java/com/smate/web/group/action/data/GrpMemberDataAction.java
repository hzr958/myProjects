package com.smate.web.group.action.data;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.struts2.convention.annotation.Action;
import org.codehaus.plexus.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.smate.core.base.utils.model.Page;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.struts2.Struts2Utils;
import com.smate.web.group.action.grp.form.GrpMemberForm;
import com.smate.web.group.constant.grp.GrpConstant;
import com.smate.web.group.service.grp.member.GrpMemberOptService;
import com.smate.web.group.service.grp.member.GrpMemberShowService;
import com.smate.web.group.service.grp.member.GrpRoleService;
import com.smate.web.group.service.grp.rcmd.GrpRcmdService;

/**
 * 群组成员相关数据服务
 * 
 * @author wsn
 * @date May 9, 2019
 */
public class GrpMemberDataAction extends ActionSupport implements ModelDriven<GrpMemberForm>, Preparable {

  protected Logger logger = LoggerFactory.getLogger(getClass());
  private GrpMemberForm form;
  @Autowired
  private GrpRoleService grpRoleService;
  @Autowired
  private GrpMemberShowService grpMemberShowService;
  @Autowired
  private GrpMemberOptService grpMemberOptService;
  @Autowired
  private GrpRcmdService grpRcmdService;


  /**
   * 显示群组成员列表
   * 
   * @return
   */
  @Action("/grpdata/member/list")
  public String showGrpMembers() {
    Map<String, Object> result = new HashMap<String, Object>();
    try {
      grpMemberShowService.getGrpMemberListForShow(form);
      result.put("status", "success");
      result.put("infoList", form.getPsnInfoList());
      result.put("totalCount", form.getPsnCount());
    } catch (Exception e) {
      logger.error("显示群组成员列表出错grpId=" + form.getGrpId(), e);
      result.put("status", "error");
    }
    Struts2Utils.renderJson(result, "encoding:utf-8");
    return null;
  }


  /**
   * 显示群组申请人员列表
   * 
   * @return
   */
  @Action("/grpdata/member/apply")
  public String showGrpProposers() {
    Map<String, Object> result = new HashMap<String, Object>();
    try {
      Integer role = grpRoleService.getGrpRole(form.getPsnId(), form.getGrpId());
      if (role == GrpConstant.GRP_ROLE_OWNER || role == GrpConstant.GRP_ROLE_ADMIN) {
        grpMemberShowService.getGrpProposerListForShow(form);
      }
      result.put("status", "success");
      result.put("infoList", form.getPsnInfoList());
      result.put("totalCount", form.getPsnCount());
    } catch (Exception e) {
      logger.error("显示群组申请人员列表出错grpId=" + form.getGrpId(), e);
      result.put("status", "error");
    }
    Struts2Utils.renderJson(result, "encoding:utf-8");
    return null;
  }


  /**
   * 处理群组申请（接受/忽略）
   * 
   * @return
   */
  @Action("/grpdata/apply/deal")
  public String disposeGrpApplication() {
    Map<String, String> map = new HashMap<String, String>();
    try {
      Integer role = grpRoleService.getGrpRole(form.getPsnId(), form.getGrpId());
      if (role == GrpConstant.GRP_ROLE_OWNER || role == GrpConstant.GRP_ROLE_ADMIN) {
        grpMemberOptService.disposeGrpApplication(form);
        grpMemberOptService.updateGrpMemberCount(form.getGrpId());
        map.put("result", "success");
      } else {
        map.put("result", "error");
      }
    } catch (Exception e) {
      map.put("result", "error");
      logger.error("处理群组申请（接受/忽略）出错grpId={},psnId={}", form.getGrpId(), form.getTargetPsnId(), e);
    }
    Struts2Utils.renderJson(map, "encoding:UTF-8");
    return null;
  }



  /**
   * 退出群组
   * 
   * @return
   */
  @Action("/grpdata/opt/quit")
  public void autoExitGrp() {
    Map<String, String> map = new HashMap<String, String>();
    String status = "error";
    Long psnId = NumberUtils.parseLong(Des3Utils.decodeFromDes3(form.getDes3PsnId()), 0L);
    try {
      if (NumberUtils.isNotNullOrZero(psnId) && StringUtils.isNotBlank(form.getDes3GrpId())) {
        form.setPsnId(psnId);
        Integer role = grpRoleService.getGrpRole(form.getPsnId(), form.getGrpId());
        if (role == GrpConstant.GRP_ROLE_OWNER) {
          map.put("errorMsg", "请先变更群组拥有者");
        } else if (role == 2 || role == 3) {
          form.setTargetPsnId(form.getPsnId());
          grpMemberOptService.delGrpMember(form);
          grpMemberOptService.updateGrpMemberCount(form.getGrpId());
          status = "success";
        } else {
          map.put("errorMsg", "不是该群组成员");
        }
      }
    } catch (Exception e) {
      map.put("errorMsg", "has a exception");
      logger.error("退出群组出错grpId={}, psnId={}", form.getGrpId(), form.getTargetPsnId(), e);
    }
    map.put("result", status);
    Struts2Utils.renderJson(map, "encoding:UTF-8");
  }

  @Override
  public void prepare() throws Exception {
    if (form == null) {
      form = new GrpMemberForm();
    }
    if (form.getPage() == null) {
      form.setPage(new Page());
    }
    LocaleContextHolder.setLocale(Locale.SIMPLIFIED_CHINESE);
  }

  @Override
  public GrpMemberForm getModel() {
    return form;
  }
}
