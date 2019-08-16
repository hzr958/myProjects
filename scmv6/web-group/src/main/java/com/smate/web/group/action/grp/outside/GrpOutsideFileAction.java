package com.smate.web.group.action.grp.outside;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.smate.core.base.utils.struts2.Struts2Utils;
import com.smate.web.group.action.grp.form.GrpFileForm;
import com.smate.web.group.service.grp.file.GrpFileService;
import com.smate.web.group.service.grp.manage.GrpBaseService;
import com.smate.web.group.service.grp.member.GrpRoleService;

/**
 * 群组文件action
 * 
 * @author AiJiangBin
 *
 */

@Results({@Result(name = "grp_outside_file_list", location = "/WEB-INF/jsp/grpoutside/file/grp_outside_file_list.jsp"),
    @Result(name = "grp_outside_file_main", location = "/WEB-INF/jsp/grpoutside/file/grp_outside_file_main.jsp")})
public class GrpOutsideFileAction extends ActionSupport implements ModelDriven<GrpFileForm>, Preparable {

  /**
   * 
   */
  private static final long serialVersionUID = -2578811131482973656L;
  protected final Logger logger = LoggerFactory.getLogger(getClass());

  private GrpFileForm grpFileForm;
  @Resource
  private GrpRoleService grpRoleService;
  @Resource
  private GrpFileService grpFileService;
  @Resource
  private GrpBaseService grpBaseService;

  @Autowired
  private RestTemplate restTemplate;
  @Value("${groupDyn.restful.url}")
  private String groupDynRestfulUrl;

  /**
   * 文件模块
   * 
   * @return
   */
  @Action("/groupweb/grpinfo/outside/ajaxgrpfilemain")
  public String ajaxGrpFileMain() {
    try {
      grpFileForm.setGrpCategory(grpBaseService.getGrpCategory(grpFileForm.getGrpId()));
      int role = grpRoleService.getGrpRole(grpFileForm.getPsnId(), grpFileForm.getGrpId());
      grpFileForm.setGrpRole(role);
    } catch (Exception e) {
      logger.error("进入群组文件出错+ grpId=" + grpFileForm.getGrpId() + e.toString());
    }
    return "grp_outside_file_main";

  }

  /**
   * 群组文件列表
   * 
   * @return
   */
  @Action("/groupweb/grpinfo/outside/ajaxgrpfilelist")
  public String ajaxGrpFileList() {
    try {

      if (checkGrpId()) {
        // 处理特殊字符串
        grpFileForm.setSearchKey(StringEscapeUtils.unescapeHtml4(grpFileForm.getSearchKey()));
        // TODO 获取群的类别
        grpFileForm.setGrpCategory(grpBaseService.getGrpCategory(grpFileForm.getGrpId()));
        int role = grpRoleService.getGrpRole(grpFileForm.getPsnId(), grpFileForm.getGrpId());
        grpFileForm.setGrpRole(role);
        grpFileService.findGrpFileList(grpFileForm);
      }
    } catch (Exception e) {
      logger.error("获取群组文件列表出错+ grpId=" + grpFileForm.getGrpId() + e.toString());
    }
    return "grp_outside_file_list";
  }



  /**
   * 检查群组id不为空
   * 
   * @return
   */
  private boolean checkGrpId() {
    if (grpFileForm.getGrpId() == null || grpFileForm.getGrpId() == 0L) {
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
    if (checkGrpId() && (grpFileForm.getPsnId() != null && grpFileForm.getPsnId() != 0L)) {
      return true;
    }
    return false;
  }

  /**
   * 检查群组id不为空人员id群组文件文件id,不为空
   * 
   * @return
   */
  private boolean checkGrpIdPsnIdGrpFileId() {
    if (checkGrpIdPsnId() && (grpFileForm.getGrpFileIdList() != null && grpFileForm.getGrpFileIdList().size() > 0)) {
      return true;
    }
    return false;
  }


  /**
   * 检查群组id不为空人员id群组文件文件id,不为空
   * 
   * @return
   */
  private boolean checkPsnIdGrpFileId() {
    if ((grpFileForm.getPsnId() != null && grpFileForm.getPsnId() != 0L)
        && (grpFileForm.getGrpFileIdList() != null && grpFileForm.getGrpFileIdList().size() > 0)) {
      return true;
    }
    return false;
  }



  /**
   * 检查我的文件判断是否有权限编辑
   * 
   * @return
   */
  @Action("/groupweb/grpinfo/outside/ajaxcheckmygrpfile")
  public String ajaxCheckMyGrpFile() {
    Map<String, String> map = new HashMap<String, String>();
    try {

      if (checkGrpIdPsnIdGrpFileId()) {
        if (grpFileService.checkFilePermit(grpFileForm)) {
          map.put("status", "success");
        } else {
          map.put("status", "noPermit");
        }
      } else {
        map.put("status", "error");
      }
    } catch (Exception e) {
      logger.error("检查文件权限，异常psnId=" + grpFileForm.getPsnId() + e.toString());
      map.put("status", "error");
    }
    Struts2Utils.renderJson(map, "encoding:UTF-8");
    return null;
  }

  /**
   * 收藏文件grpId,fileId
   * 
   * 
   * @return
   */
  @Action("/groupweb/grpinfo/outside/ajaxcollectgrpfile")
  public String ajaxCollectGrpFile() {

    Map<String, String> map = new HashMap<String, String>();
    try {
      if (checkPsnIdGrpFileId()) {
        grpFileService.collectGrpFile(grpFileForm);
        map.put("count", grpFileForm.getBatchCount().toString());
        map.put("status", "success");
      } else {
        map.put("status", "error");
      }
    } catch (Exception e) {
      logger.error("文件编辑权限，异常psnId=" + grpFileForm.getPsnId() + e.toString());
      map.put("status", "error");
    }
    Struts2Utils.renderJson(map, "encoding:UTF-8");
    return null;
  }



  @Override
  public GrpFileForm getModel() {

    return grpFileForm;
  }

  @Override
  public void prepare() throws Exception {
    if (grpFileForm == null) {
      grpFileForm = new GrpFileForm();
    }
  }

}
