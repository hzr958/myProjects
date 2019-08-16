package com.smate.web.group.action.grp.file;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
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
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.struts2.Struts2Utils;
import com.smate.web.group.action.grp.form.GrpFileForm;
import com.smate.web.group.model.grp.file.GrpFile;
import com.smate.web.group.service.grp.file.GrpFileService;
import com.smate.web.group.service.grp.manage.GrpBaseService;
import com.smate.web.group.service.grp.member.GrpRoleService;

/**
 * 群组文件action
 * 
 * @author AiJiangBin
 *
 */

@Results({@Result(name = "grp_file_list", location = "/WEB-INF/jsp/grp/grpfile/grp_file_list.jsp"),
    @Result(name = "grp_member_file_info_list", location = "/WEB-INF/jsp/grp/grpfile/grp_member_file_info_list.jsp"),
    @Result(name = "grp_file_main", location = "/WEB-INF/jsp/grp/grpfile/grp_file_main.jsp")})
public class GrpFileAction extends ActionSupport implements ModelDriven<GrpFileForm>, Preparable {

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
  @Value("${domainscm}")
  private String domainscm;

  /**
   * 文件模块
   * 
   * @return
   */
  @Action("/groupweb/grpfile/ajaxgrpfilemain")
  public String ajaxGrpFileMain() {
    try {
      grpFileForm.setGrpCategory(grpBaseService.getGrpCategory(grpFileForm.getGrpId()));
      int role = grpRoleService.getGrpRole(grpFileForm.getPsnId(), grpFileForm.getGrpId());
      grpFileForm.setGrpRole(role);
    } catch (Exception e) {
      logger.error("进入群组文件出错+ grpId=" + grpFileForm.getGrpId() + e.toString());
    }
    return "grp_file_main";

  }

  /**
   * 群组文件列表
   * 
   * @return
   */
  @Action("/groupweb/grpfile/ajaxgrpfilelist")
  public String ajaxGrpFileList() {
    try {

      if (checkGrpId()) {
        // 处理特殊字符串
        grpFileForm.setSearchKey(StringEscapeUtils.unescapeHtml4(grpFileForm.getSearchKey()));
        // TODO 获取群的类别
        grpFileForm.setGrpCategory(grpBaseService.getGrpCategory(grpFileForm.getGrpId()));
        // // 1=群组拥有者,2=管理员,3=组员
        int role = grpRoleService.getGrpRole(grpFileForm.getPsnId(), grpFileForm.getGrpId());
        grpFileForm.setGrpRole(role);
        grpFileService.findGrpFileList(grpFileForm);
      }
    } catch (Exception e) {
      logger.error("获取群组文件列表出错+ grpId=" + grpFileForm.getGrpId() + e.toString());
    }
    return "grp_file_list";
  }

  /**
   * 上传文件 ,保存文件
   * 
   * @return
   */
  @Action("/groupweb/grpfile/ajaxSaveUploadGrpFile")
  public String ajaxSaveUploadGrpFile() {
    Map<String, String> map = new HashMap<String, String>(1);
    try {
      if (checkGrpIdPsnId() && grpFileForm.getArchiveFileId() != null) {
        grpFileService.saveUploadGrpFile(grpFileForm);
        map.put("status", "success");
        if (grpFileForm.getGrpFileId() != null) {
          produceGrpDyn(grpFileForm);
        }
      } else {
        map.put("status", "error");
      }
    } catch (Exception e) {
      logger.error("获取群组文件列表出错+ grpId=" + grpFileForm.getGrpId() + e.toString());
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
    boolean flag = checkGrpId() && (grpFileForm.getPsnId() != null && grpFileForm.getPsnId() != 0L);
    if (flag) {
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
    boolean flag =
        checkGrpIdPsnId() && (grpFileForm.getGrpFileIdList() != null && grpFileForm.getGrpFileIdList().size() > 0);
    if (flag) {
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
    boolean flag = (grpFileForm.getPsnId() != null && grpFileForm.getPsnId() != 0L)
        && (grpFileForm.getGrpFileIdList() != null && grpFileForm.getGrpFileIdList().size() > 0);
    if (flag) {
      return true;
    }
    return false;
  }

  /**
   * 添加我的文件 stationFile ids
   * 
   * @return
   */
  @Action("/groupweb/grpfile/ajaxgrpaddmyfile")
  public String ajaxGrpAddMyFile() {
    Map<String, String> map = new HashMap<String, String>();

    if (checkGrpId() && grpFileForm.getStationFileIdList() != null && grpFileForm.getStationFileIdList().size() > 0) {
      try {
        grpFileForm.setGrpRole(grpRoleService.getGrpRole(grpFileForm.getPsnId(), grpFileForm.getGrpId()));
        if (grpFileForm.getGrpRole() == 1 || grpFileForm.getGrpRole() == 2 || grpFileForm.getGrpRole() == 3) {
          grpFileService.addMyFileForGrp(grpFileForm);
          map.put("status", "success");
          for (int i = 0; i < grpFileForm.getGrpFileIdList().size(); i++) {
            grpFileForm.setGrpFileId(grpFileForm.getGrpFileIdList().get(i));
            produceGrpDyn(grpFileForm);
          }
        } else {
          map.put("status", "noPermit");
        }

      } catch (Exception e) {
        logger.error("添加文件出现，异常 psnId =" + grpFileForm.getPsnId() + e.toString());
        map.put("status", "error");
      }

    } else {
      map.put("status", "paramError");
    }
    Struts2Utils.renderJson(map, "encoding:UTF-8");
    return null;
  }

  /**
   * 组员文件成员列表信息
   * 
   * @return
   */
  @Action("/groupweb/grpfile/ajaxgrpfilememberlist")
  public String ajaxGrpMemberList() {
    if (checkGrpId()) {
      try {
        grpFileForm.setGrpRole(grpRoleService.getGrpRole(grpFileForm.getPsnId(), grpFileForm.getGrpId()));
        grpFileService.findGrpFileMember(grpFileForm);
      } catch (Exception e) {
        logger.error("添加文件出现，异常 psnId =" + grpFileForm.getPsnId() + e.toString());
      }
    }
    return "grp_member_file_info_list";
  }

  /**
   * 检查我的文件判断是否有权限编辑
   * 
   * @return
   */
  @Action("/groupweb/grpfile/ajaxcheckmygrpfile")
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
   * 编辑群组文件
   * 
   * @return
   */
  @Action("/groupweb/grpfile/ajaxeditmygrpfile")
  public String ajaxEditMyGrpFile() {
    Map<String, String> map = new HashMap<String, String>();
    try {
      if (checkGrpIdPsnIdGrpFileId()) {
        if (grpFileService.checkFilePermit(grpFileForm)) {
          grpFileService.editGrpFile(grpFileForm);
          map.put("status", "success");
        } else {
          map.put("status", "noPermit");
        }
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

  /**
   * 删除群组文件
   * 
   * @return
   */
  @Action("/groupweb/grpfile/ajaxdelgrpfile")
  public String ajaxDelMyGrpFile() {
    Map<String, Object> map = new HashMap<String, Object>();
    try {
      if (checkGrpIdPsnIdGrpFileId()) {
        grpFileService.deleteGrpFile(grpFileForm);
        map.put("count", grpFileForm.getBatchCount().toString());
        if (grpFileForm.getBatchCount() > 0) {
          map.put("delDes3FileIds", grpFileForm.getDelDes3FileIds());
          map.put("status", "success");
        } else {
          map.put("status", "noPermit");
        }
      } else {
        map.put("status", "error");
      }
    } catch (Exception e) {
      logger.error("文件编辑权限，异常psnId=" + grpFileForm.getPsnId() + e.toString());
      map.put("status", "error");
    }
    Struts2Utils.renderJson(JacksonUtils.mapToJsonStr(map), "encoding:UTF-8");
    return null;
  }

  /**
   * 收藏文件grpId,fileId
   * 
   * 
   * @return
   */
  @Action("/groupweb/grpfile/ajaxcollectgrpfile")
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

  /**
   * 课程群组，标记作业和课件
   * 
   * @return
   */
  @Action("/groupweb/grpfile/ajaxflaggrpfiletype")
  public String ajaxFlagGrpFileType() {
    Map<String, String> map = new HashMap<String, String>();
    try {
      if (checkGrpIdPsnIdGrpFileId() && grpFileForm.getGrpFileType() != null) {
        int role = grpRoleService.getGrpRole(grpFileForm.getPsnId(), grpFileForm.getGrpId());
        if (role == 1 || role == 2) {
          grpFileService.flagGrpFileType(grpFileForm);
          map.put("status", "success");
        } else {
          map.put("status", "noPermit");
        }
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

  /**
   * 产生动态
   * 
   * @param form
   */
  private void produceGrpDyn(GrpFileForm form) {
    try {
      // 发一个群组动态
      Map<String, Object> map = new HashMap<String, Object>();
      map.put("groupId", form.getGrpId());
      map.put("psnId", form.getPsnId());
      map.put("resType", "grpfile");
      map.put("resId", form.getGrpFileId());
      if (form.getGrpFileType() == 1) {
        map.put("tempType", "GRP_ADDWORK"); // 作业
      } else if (form.getGrpFileType() == 2) {
        map.put("tempType", "GRP_ADDCOURSE"); // 课件
      } else {
        map.put("tempType", "GRP_ADDFILE");
      }
      restTemplate.postForObject(this.groupDynRestfulUrl, map, Object.class);
    } catch (Exception e) {
      logger.error("添加文件，产生动态异常，异常psnId=" + grpFileForm.getPsnId() + e.toString());
    }

  }

  /**
   * 下载文件得到， archivefileid
   * 
   * @see
   * @return
   */
  @Deprecated
  @Action("/groupweb/grpfile/ajaxgetdes3archivefileid")
  public String ajaxGetDes3ArchiveFileId() {
    Map<String, String> map = new HashMap<String, String>();
    try {
      if (grpFileForm.getGrpFileId() != null && grpFileForm.getGrpFileId() != 0L) {

        GrpFile grpFile = grpFileService.findGrpFile(grpFileForm);
        if (grpFile != null) {
          map.put("des3ArchiveFileId", Des3Utils.encodeToDes3(grpFile.getArchiveFileId().toString()));
          map.put("status", "success");
        }
      } else {
        map.put("status", "error");
      }
    } catch (Exception e) {
      logger.error("获取群组文件，异常psnId=" + grpFileForm.getPsnId(), e.toString());
      map.put("status", "error");
    }
    Struts2Utils.renderJson(map, "encoding:UTF-8");
    return null;
  }

  // 分享群组文件

  /**
   * 文件批量分享
   * 
   * @return
   */
  @Action("/groupweb/grpfile/ajaxshareGrpfiles")
  public String shareFileAll() {
    Map<String, Object> map = new HashMap<String, Object>();
    try {
      if (grpFileForm.getPsnId() != null && grpFileForm.getPsnId() != 0L && grpFileForm.getGrpFileIdList() != null
          && grpFileForm.getGrpFileIdList().size() > 0 && (StringUtils.isNoneBlank(grpFileForm.getDes3ReceiverIds())
              || StringUtils.isNotBlank(grpFileForm.getReceiverEmails()))) {
        // 群外也可以分享，下载文件，不要权限控制
        if (StringUtils.isNoneBlank(grpFileForm.getDes3ReceiverIds())) {
          grpFileService.shareGrpFiles(grpFileForm);
        }
        if (StringUtils.isNotBlank(grpFileForm.getReceiverEmails())) {
          grpFileService.shareGrpFilesByEmail(grpFileForm);
        }
        map.put("result", "success");
      } else {
        map.put("result", "error");
      }
    } catch (Exception e) {
      logger.error("群组文件分享，异常psnId=" + grpFileForm.getPsnId(), e);
      map.put("result", "error");
    }
    Struts2Utils.renderJson(map, "encoding:UTF-8");
    return null;
  }

  /**
   * 检查群组文件是否被删除
   * 
   * @return
   */
  @Action("/groupweb/grpfile/ajaxCheckGrpFile")
  public String ajaxCheckGrpFile() {
    Map<String, String> map = new HashMap<String, String>(1);
    try {
      if (grpFileForm.getDes3GrpFileId() != null) {
        if (!grpFileService.checkExitGrpFile(grpFileForm.getGrpFileId())) {
          map.put("status", "isDel");
        } else {
          map.put("status", "success");
        }
      } else {
        map.put("status", "error");
      }
    } catch (Exception e) {
      logger.error("检查群组文件是否被删除出错+ grpFileId=" + grpFileForm.getGrpFileId() + e.toString());
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
