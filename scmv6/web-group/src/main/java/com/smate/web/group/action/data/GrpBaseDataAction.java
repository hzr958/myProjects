package com.smate.web.group.action.data;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import org.apache.struts2.convention.annotation.Action;
import org.codehaus.plexus.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.smate.core.base.utils.model.Page;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.struts2.Struts2Utils;
import com.smate.web.group.action.grp.form.GrpMainForm;
import com.smate.web.group.model.grp.grpbase.GrpBaseinfo;
import com.smate.web.group.service.grp.manage.GrpBaseService;
import com.smate.web.group.service.grp.member.GrpMemberOptService;
import com.smate.web.group.service.grp.member.GrpRoleService;

public class GrpBaseDataAction extends ActionSupport implements ModelDriven<GrpMainForm>, Preparable {
  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  protected final Logger LOGGER = LoggerFactory.getLogger(getClass());
  private GrpMainForm form;
  @Autowired
  private GrpBaseService grpBaseService;
  @Autowired
  private GrpMemberOptService grpMemberOptService;
  @Autowired
  private GrpRoleService grpRoleService;
  @Value("${domainscm}")
  private String domainscm;



  /**
   * 获取群组详情
   * 
   * @return
   */
  @Action("/grpdata/info/base")
  public void grpBaseInfo() {
    Map<String, Object> result = new HashMap<String, Object>();
    try {
      Long grpId = form.getGrpId();
      Long psnId = form.getPsnId();
      if (NumberUtils.isNotNullOrZero(grpId)) {
        // 先判断人员是否有权限访问当前群组
        int psnRole = grpRoleService.getGrpRole(psnId, grpId);
        form.setRole(psnRole);
        GrpBaseinfo currGrpBaseInfo = grpBaseService.getCurrGrp(grpId);
        boolean isNotExists = Objects.isNull(currGrpBaseInfo);
        boolean isDeleted = !isNotExists && !"01".equals(currGrpBaseInfo.getStatus());
        boolean hasNoPermission =
            !isDeleted && "P".equals(currGrpBaseInfo.getOpenType()) && psnRole != 1 && psnRole != 2 && psnRole != 3;
        Integer psnCount = grpBaseService.getProposerCount(form.getGrpId());// 群组申请人数
        // 不存在或已删除状态
        if (isNotExists || isDeleted) {
          result.put("isExist", "0");
        }
        // 没权限访问
        if (hasNoPermission) {
          result.put("isPrivate", "1");
        } else {
          // 更新访问时间
          grpMemberOptService.updateVisitDate(form.getPsnId(), form.getGrpId());
          // 获取群组详情信息
          grpBaseService.getGrpInfo(form);
          result.put("status", "success");
          result.put("grpInfo", form.getGrpShowInfo());
          result.put("proposerCount", psnCount);
          result.put("grpCategory", form.getGrpShowInfo().getGrpBaseInfo().getGrpCategory());
        }
      } else {
        result.put("isExist", "0");
      }
    } catch (Exception e) {
      LOGGER.error("进入群组详情出错grpId=" + form.getGrpId(), e);
      result.put("status", "error");
    }
    Struts2Utils.renderJson(result, "encoding: UTF-8");
  }


  /**
   * 群组置顶设置
   * 
   * @return
   */
  @Action("/grpdata/opt/sticky")
  public void ajaxMygrpSetTop() {
    Map<String, String> map = new HashMap<String, String>();
    String status = "error";
    Long psnId = NumberUtils.parseLong(Des3Utils.decodeFromDes3(form.getDes3PsnId()), 0L);
    try {
      if (NumberUtils.isNotNullOrZero(psnId) && StringUtils.isNotBlank(form.getDes3GrpId())) {
        form.setPsnId(psnId);
        grpBaseService.setGrpTop(form);
        status = "success";
      }
    } catch (Exception e) {
      LOGGER.error("群组置顶设置grpId={}", form.getGrpId(), e);
    }
    map.put("result", status);
    Struts2Utils.renderJson(map, "encoding:UTF-8");
  }

  @Override
  public void prepare() throws Exception {
    if (form == null) {
      form = new GrpMainForm();
    }
    if (form.getPage() == null) {
      form.setPage(new Page());
    }
    LocaleContextHolder.setLocale(Locale.SIMPLIFIED_CHINESE);
  }

  @Override
  public GrpMainForm getModel() {
    return form;
  }

}
