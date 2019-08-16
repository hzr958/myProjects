package com.smate.web.group.action.grp.pub;

import java.util.HashMap;
import java.util.Map;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.smate.core.base.pub.enums.PubPdwhStatusEnum;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.string.StringUtils;
import com.smate.core.base.utils.struts2.Struts2Utils;
import com.smate.web.group.action.grp.form.GrpPubRcmdForm;
import com.smate.web.group.service.grp.member.GrpRoleService;
import com.smate.web.group.service.grp.pdwh.PubPdwhService;
import com.smate.web.group.service.grp.pub.GrpPubRcmdService;
import com.smate.web.group.service.grp.pub.GrpPubsService;

/**
 * 群组成果推荐
 * 
 * @author tsz
 *
 */
@SuppressWarnings("serial")
@Results({

    @Result(name = "pubrcmdlist", location = "/WEB-INF/jsp/grp/pub/grp_pub_rcmd_list.jsp"),
    @Result(name = "pubrcmdlistnew", location = "/WEB-INF/jsp/grp/pub/grp_pub_rcmd_list_new.jsp"),
    @Result(name = "pubrcmdone", location = "/WEB-INF/jsp/grp/pub/grp_pub_rcmd_one.jsp")})
public class GrpPubRcmdAction extends ActionSupport implements ModelDriven<GrpPubRcmdForm>, Preparable {

  protected Logger logger = LoggerFactory.getLogger(getClass());

  private GrpPubRcmdForm form;

  @Autowired
  private GrpPubRcmdService grpPubRcmdService;
  @Autowired
  private GrpRoleService grpRoleService;
  @Autowired
  private GrpPubsService grpPubsService;
  @Autowired
  private PubPdwhService pubPdwhService;

  /**
   * 获取推荐群组成果数据
   * 
   * @return
   */
  @Action(value = "/groupweb/grppub/ajaxpubrcmd")
  public String ajaxGetGrpPubRcmdList() {
    try {
      int role = grpRoleService.getGrpRole(form.getPsnId(), form.getGrpId());
      if (role == 1 || role == 2) {
        if (form.getShowType() == 2) {
          grpPubRcmdService.getAllRcmdPub(form);
          return "pubrcmdlist";
        } else {
          grpPubRcmdService.getOneRcmdPub(form);
          return "pubrcmdone";
        }

      }
    } catch (Exception e) {
      logger.error("获取推荐群组成果数据异常 psnId=" + form.getPsnId() + "; grpId=" + form.getGrpId(), e);
    }
    return null;
  }

  @Action(value = "/groupweb/grppub/ajaxpubrcmdnew")
  public String ajaxGetGrpPubRcmdListNew() {
    // String grpIdStr = Struts2Utils.getRequest().getParameter("grpId");
    Long grpId = form.getGrpId();
    Long psnId = SecurityUtils.getCurrentUserId();
    try {
      int role = grpRoleService.getGrpRole(psnId, grpId);
      if (role == 1 || role == 2) {
        grpPubRcmdService.getAllRcmdPub(form);
        return "pubrcmdlistnew";
      }
    } catch (Exception e) {
      logger.error("获取推荐群组成果数据异常 psnId=" + form.getPsnId() + "; grpId=" + form.getGrpId(), e);
    }
    return null;
  }

  @Action(value = "/groupweb/grppub/ajaxrejectpubrcmd")
  public String ajaxRejectGrpPubRcmd() {
    Map<String, String> map = new HashMap<String, String>();
    boolean iszhCN = LocaleContextHolder.getLocale().toString().equals("zh_CN");
    try {
      Long grpId = form.getGrpId();
      Long pubId = form.getPubId();
      Long psnId = SecurityUtils.getCurrentUserId();
      int role = grpRoleService.getGrpRole(psnId, grpId);
      if (role == 1 || role == 2) {
        grpPubRcmdService.rejectGrpPubRcmd(grpId, pubId, psnId);
        map.put("result", "success");
        map.put("msg", iszhCN ? "操作成功" : "Operated successfully.");
      } else {
        map.put("result", "error");
        map.put("msg", iszhCN ? "没有权限" : "You are not eligible to operate publication.");
      }
    } catch (Exception e) {
      map.put("result", "error");
      map.put("msg", iszhCN ? "网络异常" : "System error occured, please try again later.");
      logger.error("确认推荐成果数据异常 psnId=" + form.getPsnId() + "; grpId=" + form.getGrpId() + "; pubId=" + form.getPubId(),
          e);
    }
    Struts2Utils.renderJson(map, "encoding:UTF-8");
    return null;
  }

  @Action(value = "/groupweb/grppub/ajaxacceptpubrcmd")
  public String ajaxAcceptGrpPubRcmd() {
    Map<String, String> map = new HashMap<String, String>();
    boolean iszhCN = LocaleContextHolder.getLocale().toString().equals("zh_CN");
    try {
      Long grpId = form.getGrpId();
      Long pubId = form.getPubId();
      Long psnId = SecurityUtils.getCurrentUserId();
      if ((psnId == null || psnId == 0L) && StringUtils.isNotBlank(form.getDes3PsnId())) {
        psnId = NumberUtils.parseLong(Des3Utils.decodeFromDes3(form.getDes3PsnId()));
      }
      int role = grpRoleService.getGrpRole(psnId, grpId);
      if (role == 1 || role == 2) {
        if (checkPdwhIsDel(pubId)) {
          Long newPubId = grpPubRcmdService.acceptGrpPubRcmd(grpId, pubId, psnId);
          Long grpPubCounts = grpPubsService.getGroupPubCounts(grpId);
          map.put("result", "success");
          map.put("newPubId", String.valueOf(newPubId));// -1重复成果统计不用增加,0没有成果,其他统计数据加1
          map.put("grpPubCounts", String.valueOf(grpPubCounts));
          if (grpPubCounts > 1) {
            map.put("msgPub", iszhCN ? "成果" : "Publications");
          } else {
            map.put("msgPub", iszhCN ? "成果" : "Publication");
          }
          map.put("msg", iszhCN ? "操作成功" : "Operated successfully.");
        } else {
          map.put("result", "not exist");
          map.put("msg", iszhCN ? "成果不存在" : "Publication is not exist.");
        }
      } else {
        map.put("result", "error");
        map.put("msg", iszhCN ? "没有权限" : "You are not eligible to operate publication.");
      }
    } catch (Exception e) {
      map.put("result", "error");
      map.put("msg", iszhCN ? "网络异常" : "System error occured, please try again later.");
      logger.error("忽略推荐成果数据异常 psnId=" + form.getPsnId() + "; grpId=" + form.getGrpId() + "; pubId=" + form.getPubId(),
          e);
    }
    Struts2Utils.renderJson(map, "encoding:UTF-8");
    return null;
  }

  /**
   * 认领成果时判断基准库成果是否删除
   * 
   * @param pubId
   * @return
   */
  public boolean checkPdwhIsDel(Long pubId) {
    if (NumberUtils.isNotNullOrZero(pubId)) {
      return pubPdwhService.checkPdwhIsDel(pubId, PubPdwhStatusEnum.DEFAULT);
    }
    return false;
  }

  /**
   * 确认或忽略推荐群组成果数据
   * 
   * @return
   */
  @Action(value = "/groupweb/grppub/ajaxoptionpubrcmd")
  public String ajaxOptionGrpPubRcmd() {
    Map<String, String> map = new HashMap<String, String>();
    boolean iszhCN = LocaleContextHolder.getLocale().toString().equals("zh_CN");
    try {
      int role = grpRoleService.getGrpRole(form.getPsnId(), form.getGrpId());
      if (role == 1 || role == 2) {
        grpPubRcmdService.optionGrpPubRcmd(form);
        map.put("result", "success");
        map.put("msg", iszhCN ? "操作成功" : "Operated successfully.");
      } else {
        map.put("result", "error");
        map.put("msg", iszhCN ? "没有权限" : "You are not eligible to operate publication.");
      }
    } catch (Exception e) {
      map.put("result", "error");
      map.put("msg", iszhCN ? "网络异常" : "System error occured, please try again later.");
      logger.error("确认或者忽略推荐成果数据异常 psnId=" + form.getPsnId() + "; grpId=" + form.getGrpId(), e);
    }
    Struts2Utils.renderJson(map, "encoding:UTF-8");
    return null;
  }

  @Override
  public void prepare() throws Exception {
    if (form == null) {
      form = new GrpPubRcmdForm();
    }
  }

  @Override
  public GrpPubRcmdForm getModel() {

    return form;
  }

}
