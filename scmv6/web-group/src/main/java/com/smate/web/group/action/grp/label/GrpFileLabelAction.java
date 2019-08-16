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

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.smate.core.base.utils.struts2.Struts2Utils;
import com.smate.web.group.action.grp.form.GrpLabelForm;
import com.smate.web.group.action.grp.form.GrpLabelShowInfo;
import com.smate.web.group.model.grp.label.GrpFileLabel;
import com.smate.web.group.model.grp.label.GrpLabel;
import com.smate.web.group.service.grp.label.GrpFileLabelService;
import com.smate.web.group.service.grp.label.GrpLabelService;
import com.smate.web.group.service.grp.member.GrpRoleService;

/**
 * 群组文件标签action
 * 
 * @author AiJiangBin
 *
 */
@Results({@Result(name = "grp_file_label_list", location = "/WEB-INF/jsp/grp/grpfile/grp_file_label_list.jsp"),
    @Result(name = "grp_file_label_list_exclude_owner",
        location = "/WEB-INF/jsp/grp/grpfile/grp_file_label_list_exclude_owner.jsp"),
    @Result(name = "grp_file_label_list_for_upload_file",
        location = "/WEB-INF/jsp/grp/grpfile/grp_file_label_list_for_upload_file.jsp"),})
public class GrpFileLabelAction extends ActionSupport implements ModelDriven<GrpLabelForm>, Preparable {


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
  @Autowired
  private GrpFileLabelService grpFileLabelService;



  /**
   * 群组文件关联标签 1=成功 ,3=文件最多关联10个标签 , 4, 标签已经添加 5 == 标签不存在 , 7=缺少必要参数 8=没权限 ， 9=异常，
   * 
   * @return
   */
  @Action("/groupweb/grpfilelabel/ajaxaddfilelabel")
  public String ajaxAddFileLabel() {
    Map<String, Object> resultMap = new HashMap<>();
    try {
      if (checkGrpIdAndPsnId() && form.getLabelId() != null && form.getLabelId() != 0L && form.getGrpFileId() != null
          && form.getGrpFileId() != 0L) {
        Integer role = grpRoleService.getGrpRole(form.getPsnId(), form.getGrpId());
        if (role == 1 || role == 2 || role == 3) {
          GrpLabel grpLabel = grpLabelService.getGrplabelById(form);
          if (grpLabel != null) {
            // 文件最多关联10个标签
            List<GrpFileLabel> list = grpFileLabelService.findLabelByFileId(form);
            if (list == null || list.size() < 10) {
              grpFileLabelService.addFileLabel(form);
              resultMap.put("result", form.getResult());
              resultMap.put("des3filelabelid", form.getDes3FileLabelId());
            } else {
              resultMap.put("result", 3);
            }

          } else {
            resultMap.put("result", 5);
          }

        } else {
          resultMap.put("result", 8);
        }
      } else {
        resultMap.put("result", 7);
      }
    } catch (Exception e) {
      resultMap.put("result", 9);
      LOGGER.error("新增群组文件关联标签异常，form = " + form, e);
    }
    Struts2Utils.renderJson(resultMap, "encoding:UTF-8");
    return null;
  }


  /**
   * 删除群组文件标签关联 1=成功 ， 5 == 标签不存在 , 7=缺少必要参数 8=没权限 ， 9=异常，
   * 
   * @return
   */
  @Action("/groupweb/grpfilelabel/ajaxdelfilelabel")
  public String ajaxDelFileLabel() {
    Map<String, Object> resultMap = new HashMap<>();
    try {
      if (checkGrpIdAndPsnId() && form.getFileLabelId() != null && form.getFileLabelId() != 0L
          && form.getGrpFileId() != null && form.getGrpFileId() != 0L) {
        Integer role = grpRoleService.getGrpRole(form.getPsnId(), form.getGrpId());
        GrpFileLabel fileLabel = grpFileLabelService.getFileLabelById(form);
        if (fileLabel != null) {
          if (role == 1 || role == 2 || fileLabel.getCreatePsnId().longValue() == form.getPsnId().longValue()) {
            grpFileLabelService.delFileLabelById(form);
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
      LOGGER.error("删除群组文件标签关联异常，form = " + form, e);
    }
    Struts2Utils.renderJson(resultMap, "encoding:UTF-8");
    return null;
  }


  /**
   * 得到文件所有的标签
   * 
   * @return
   */
  @Action("/groupweb/grplabel/ajaxgetallfilelabel")
  public String ajaxGetAllFileLabel() {
    try {
      if (form.getGrpId() != null && form.getGrpId() != 0L) {
        List<GrpLabelShowInfo> list = grpFileLabelService.getAllFileLabel(form);
        form.setGrpLabelShowInfoList(list);
      }
    } catch (Exception e) {
      LOGGER.error("得到文件所有的标签异常，form = " + form, e);
    }
    return "grp_file_label_list";
  }

  /**
   * 得到所有的标签,排除文件已有的标签
   * 
   * @return
   */
  @Action("/groupweb/grplabel/ajaxgetalllabelexcludeowner")
  public String ajaxGetAllLabelExcludeOwner() {
    try {
      if (form.getGrpId() != null && form.getGrpId() != 0L && form.getGrpFileId() != null
          && form.getGrpFileId() != 0L) {
        List<GrpLabelShowInfo> list = grpFileLabelService.getAllLabelExcludeOwner(form);
        form.setGrpLabelShowInfoList(list);
      }
    } catch (Exception e) {
      LOGGER.error("得到所有的标签,排除文件已有的标签异常，form = " + form, e);
    }
    return "grp_file_label_list_exclude_owner";
  }

  /**
   * 得到所有的标签,上传文件使用
   * 
   * @return
   */
  @Action("/groupweb/grplabel/ajaxgetalllabelforuploadfile")
  public String ajaxGetAllLabelForUploadFile() {
    try {
      if (form.getGrpId() != null && form.getGrpId() != 0L) {
        List<GrpLabelShowInfo> list = grpFileLabelService.getAllFileLabel(form);
        form.setGrpLabelShowInfoList(list);
      }
    } catch (Exception e) {
      LOGGER.error("得到所有的标签,上传文件使用，form = " + form, e);
    }
    return "grp_file_label_list_for_upload_file";
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
