package com.smate.web.group.action.grp.label;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
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
import com.smate.core.base.utils.struts2.Struts2Utils;
import com.smate.web.group.action.grp.form.GrpLabelForm;
import com.smate.web.group.model.grp.label.GrpLabel;
import com.smate.web.group.service.grp.label.GrpLabelService;
import com.smate.web.group.service.grp.member.GrpRoleService;

/**
 * 群组标签action
 * 
 * @author AiJiangBin
 *
 */
@Results({@Result(name = "has_ivite_grp_list", location = "/WEB-INF/jsp/grp/grpmain/has_ivite_grp_list.jsp"),})
public class GrpLabelAction extends ActionSupport implements ModelDriven<GrpLabelForm>, Preparable {


  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  protected final Logger LOGGER = LoggerFactory.getLogger(getClass());
  private GrpLabelForm form;
  @Autowired
  private GrpRoleService grpRoleService;
  @Autowired
  private GrpLabelService grpLabelService;



  /**
   * 新增群组标签 1=成功 ，2=群组标签存在20个 , 6=已经存在标签， 7=缺少必要参数 8=没权限 ， 9=异常，
   * 
   * @return
   */
  @Action("/groupweb/grplabel/ajaxcreatelabel")
  public String ajaxCreateLabel() {
    Map<String, Object> resultMap = new HashMap<>();
    try {
      if (checkGrpIdAndPsnId() && StringUtils.isNotBlank(form.getLabelName())) {
        Integer role = grpRoleService.getGrpRole(form.getPsnId(), form.getGrpId());
        if (role == 1 || role == 2 || role == 3) {
          List<GrpLabel> list = grpLabelService.getAllGrpLabel(form);
          if (list == null || list.size() < 20) {
            GrpLabel label = grpLabelService.getLabelByGrpIdAndLabel(form);
            if (label == null) {
              grpLabelService.createNewLabel(form);
              resultMap.put("result", 1);
              resultMap.put("des3BaseId", form.getDes3LabelId());
            } else {
              resultMap.put("result", 6);
            }
          } else {
            resultMap.put("result", 2);
          }
        } else {
          resultMap.put("result", 8);
        }
      } else {
        resultMap.put("result", 7);
      }
    } catch (Exception e) {
      resultMap.put("result", 9);
      LOGGER.error("新增群组标签异常，form = " + form, e);
    }
    Struts2Utils.renderJson(resultMap, "encoding:UTF-8");
    return null;
  }



  /**
   * 删除标签 1=成功 ， 5 == 标签不存在 ， 7=缺少必要参数 8=没权限 ， 9=异常，
   * 
   * @return
   */
  @Action("/groupweb/grplabel/ajaxdellabel")
  public String ajaxDelLabel() {
    Map<String, Object> resultMap = new HashMap<>();
    try {
      if (checkGrpIdAndPsnId() && form.getLabelId() != null && form.getLabelId() != 0L) {
        Integer role = grpRoleService.getGrpRole(form.getPsnId(), form.getGrpId());
        GrpLabel grpLabel = grpLabelService.getGrplabelById(form);
        if (grpLabel != null) {
          if (role == 1 || role == 2 || grpLabel.getCreatePsnId().longValue() == form.getPsnId().longValue()) {
            grpLabelService.delGrplabel(form);
            resultMap.put("result", 1);
          } else {
            resultMap.put("result", 8);
          }
        } else {
          resultMap.put("result", 5);
        }
      } else {
        resultMap.put("result", 7);
      }
    } catch (Exception e) {
      resultMap.put("result", 9);
      LOGGER.error("新增群组标签异常，form = " + form, e);
    }
    Struts2Utils.renderJson(resultMap, "encoding:UTF-8");
    return null;
  }



  /**
   * 检查群组id 和 psnId
   * 
   * @return
   */
  private Boolean checkGrpIdAndPsnId() {
    if (form.getGrpId() != null && form.getGrpId() != 0L && form.getPsnId() != null && form.getPsnId() != 0L) {
      return true;
    }
    return false;
  }



  @Override
  public void prepare() throws Exception {
    if (form == null) {
      form = new GrpLabelForm();
    }

  }

  @Override
  public GrpLabelForm getModel() {
    return form;
  }

}
