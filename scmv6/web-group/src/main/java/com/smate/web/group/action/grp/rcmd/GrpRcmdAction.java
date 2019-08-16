package com.smate.web.group.action.grp.rcmd;

import java.util.HashMap;
import java.util.Map;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.smate.core.base.utils.struts2.Struts2Utils;
import com.smate.web.group.action.grp.form.GrpBaseForm;
import com.smate.web.group.service.grp.rcmd.GrpRcmdService;

/**
 * 群组推荐
 * 
 * @author AiJiangBin
 *
 */
@Results({@Result(name = "rcmd_grp_main", location = "/WEB-INF/jsp/grp/grpmain/rcmd_grp_main.jsp"),
    @Result(name = "rcmd_grp_statistics", location = "/WEB-INF/jsp/grp/grpmain/rcmd_grp_statistics.jsp"),
    @Result(name = "rcmd_grp_list", location = "/WEB-INF/jsp/grp/grpmain/rcmd_grp_list.jsp")})
public class GrpRcmdAction extends ActionSupport implements ModelDriven<GrpBaseForm>, Preparable {

  protected final Logger logger = LoggerFactory.getLogger(getClass());
  /**
   * 
   */
  private static final long serialVersionUID = -6418647129836290933L;
  private GrpBaseForm form;

  @Autowired
  private GrpRcmdService grpRcmdService;


  /**
   * 群组推荐页面
   * 
   * @return
   */
  @Action("/groupweb/rcmdgrp/ajaxrcmdgrpmain")
  public String showRcmdGrp() {

    return "rcmd_grp_main";
  }

  /**
   * 群组推荐 列表
   * 
   * @return
   */
  @Action("/groupweb/rcmdgrp/ajaxrcmdgrplist")
  public String ajaxGetRcmdGrpList() {
    try {
      if (form.getPsnId() != null && form.getPsnId() != 0L) {
        grpRcmdService.getRcmdGrpList(form);
      }
    } catch (Exception e) {
      logger.error("群组推荐列表 ，操作 异常：psnId=" + form.getPsnId() + e.toString());
    }

    return "rcmd_grp_list";
  }

  /**
   * 群组推荐 列表
   * 
   * @return
   */
  @Action("/groupweb/rcmdgrp/ajaxdatarcmdgrplist")
  public void ajaxGetDataRcmdGrpList() {
    Map<String, Object> result = new HashMap<String, Object>();
    try {
      if (form.getPsnId() != null && form.getPsnId() != 0L) {
        grpRcmdService.getRcmdGrpList(form);
      }
      // 获取数据
      result.put("grpInfoList", form.getGrpShowInfoList());
      result.put("status", "success");
    } catch (Exception e) {
      result.put("status", "error");
      result.put("grpInfoList", null);
      logger.error("群组推荐列表 ，操作 异常：psnId=" + form.getPsnId() + e.toString());
    }

    Struts2Utils.renderJsonNoNull(result, "encoding:utf-8");
  }

  /**
   * 群组推荐 列表统计数
   * 
   * @return
   */
  @Action("/groupweb/rcmdgrp/ajaxrcmdgrpliststatistics")
  public String ajaxGetRcmdGrpListStatistics() {
    try {
      if (form.getPsnId() != null && form.getPsnId() != 0L) {
        grpRcmdService.getRcmdGrpListStatistics(form);

      }
    } catch (Exception e) {
      logger.error("群组推荐 统计数 ，操作 异常：psnId=" + form.getPsnId() + e.toString());
    }
    Struts2Utils.renderJson(form.getResult(), "encoding:UTF-8");
    return null;
  }

  /**
   * 群组推荐后操作（申请 ， 忽略）
   * 
   * @return
   */
  @Action("/groupweb/rcmdgrp/ajaxoptionrcmdgrp")
  public String ajaxOptionRcmdGrp() {
    Map<String, String> map = new HashMap<String, String>();
    try {
      if (checkGrpIdPsnId() && form.getRcmdStatus() != null) {
        grpRcmdService.optionRcmdGrp(form.getPsnId(), form.getGrpId(), form.getRcmdStatus());
        map.put("status", "success");
      } else {
        map.put("status", "error");
      }
    } catch (Exception e) {
      logger.error("群组推荐 ，操作 异常：psnId=" + form.getPsnId() + e.toString());
      map.put("status", "error");
    }
    Struts2Utils.renderJson(map, "encoding:UTF-8");
    return null;
  }


  /**
   * 检查群组id不为空
   * 
   * @return
   */
  private boolean checkGrpId() {
    if (form.getGrpId() == null || form.getGrpId() == 0L) {
      return false;
    }
    return true;
  }

  /**
   * 检查群组id，空人员id不为
   * 
   * @return
   */
  private boolean checkGrpIdPsnId() {
    if (checkGrpId() && (form.getPsnId() != null && form.getPsnId() != 0L)) {
      return true;
    }
    return false;
  }

  @Override
  public void prepare() throws Exception {
    if (form == null) {
      form = new GrpBaseForm();
    }

  }

  @Override
  public GrpBaseForm getModel() {
    return form;
  }

}
