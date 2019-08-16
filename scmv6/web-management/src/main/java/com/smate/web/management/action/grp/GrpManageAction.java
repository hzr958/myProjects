package com.smate.web.management.action.grp;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.smate.core.base.utils.cache.CacheService;
import com.smate.core.base.utils.string.StringUtils;
import com.smate.core.base.utils.struts2.Struts2Utils;
import com.smate.web.management.model.grp.GrpItemInfo;
import com.smate.web.management.model.grp.GrpManageForm;
import com.smate.web.management.service.grp.GrpManageService;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 邮件管理Action
 * 
 * @author zzx
 *
 */
@Results({@Result(name = "grp_manage_main", location = "/WEB-INF/grp_management/grp_manage_main.jsp"),
    @Result(name = "grp_list", location = "/WEB-INF/grp_management/grp_list.jsp"),
    @Result(name = "import_grp_page", location = "/WEB-INF/grp_management/export_grp.jsp"),
    @Result(name = "export_grp_list", location = "/WEB-INF/grp_management/collect_imp_grp_fileList.jsp"),
    @Result(name = "import_grp_result", location = "/WEB-INF/grp_management/collect_imp_grp_fielResult.jsp"),
    @Result(name = "pdwh_pub_list", location = "/WEB-INF/grp_management/pdwh_pub_list.jsp"),
   })
public class GrpManageAction extends ActionSupport implements ModelDriven<GrpManageForm>, Preparable {
  private static final long serialVersionUID = 1L;



  protected final Logger logger = LoggerFactory.getLogger(getClass());
  private GrpManageForm form;
  @Autowired
  private GrpManageService grpManageService;
  @Autowired
  private CacheService cacheService;
  private File sourceFile; // 导入的文件流
  private String  fileType; //  导入的文件类型  SCMIRIS  SCMEXCEL
  private String sourceFileFileName;

  /**
   * 群组管理主页面
   * 
   * @return
   */
  @Action("/scmmanagement/grpmanage/main")
  public String showGrpmanageMain() {
    try {

    } catch (Exception e) {
      logger.error("群组管理主页面", e);
    }
    return "grp_manage_main";
  }


  /**
   * 群组管理主页面
   *
   * @return
   */
  @Action("/scmmanagement/grpmanage/grplist")
  public String showGrpList() {
    try {
      grpManageService.findGrpList(form);
    } catch (Exception e) {
      logger.error("群组管理主页面列表出错", e);
    }
    return "grp_list";
  }

  /**
   * 进入文件导入群组页面.
   *
   * @return
   */
  @Action("/scmmanagement/grpmanage/importgrp")
  public String showImportGrpPage() {

    return "import_grp_page";
  }


  /**
   * 显示待导入的群组列表.
   *
   * @return
   */
  @Action("/scmmanagement/grpmanage/exportgrplist")
  public String initImportGrpList() {
    try {
      Map<String, Object> map =  grpManageService.extractFileData(sourceFile,fileType,sourceFileFileName) ;
      if (map.get("count") != null && Integer.parseInt(map.get("count").toString()) >0) {
        grpManageService.buildPendingGrpInfos(form , (List<GrpItemInfo>) map.get("list"));
      }
    } catch (Exception e) {
      logger.error("初始化待导入群组数据出错， psnId={}", form.getPsnId() , e);
    }
    return "export_grp_list";
  }


  /**
   * 保存检索导入的项目.
   *
   * @return
   */
  @Action("/scmmanagement/grpmanage/saveimportgrp")
  public String saveImportGrps() {
    try {
      grpManageService.savePendingGrpInfos(form);
    } catch (Exception e) {
      logger.error("文件导入群组保存出错, psnId={}", form.getPsnId(), e);
    }
    return "import_grp_result";
  }


  /**
   * 保存检索导入的项目.
   *
   * @return
   */
  @Action("/scmmanagement/grpmanage/searchpdwhpub")
  public String searchPdwhPub() {
    try {
      if(StringUtils.isNotBlank(form.getSearchKey())){
        grpManageService.findPdwhPub(form);
      }
    } catch (Exception e) {
      logger.error("检索基准库成果异常, psnId={}", form.getPsnId(), e);
    }
    return "pdwh_pub_list";
  }

  /**
   * 保存检索导入的项目.
   *
   * @return
   */
  @Action("/scmmanagement/grpmanage/savepdwhpubtogrp")
  public String savePdwhPubToGrp() {
    Map map = new HashMap();
    try {
      if(StringUtils.isNotBlank(form.getDesGrpIds()) && StringUtils.isNotBlank(form.getPdwhPubIds())){
        grpManageService.importPdwhPubToGrp(form);
        map.put("result","success");
        map.put("count",form.getCount());
      }else{
        map.put("result","error");
      }
    } catch (Exception e) {
      map.put("result","error");
      logger.error("检索基准库成果异常, psnId={}", form.getPsnId(), e);
    }
    Struts2Utils.renderJson(map, "encoding:UTF-8");
    return null;
  }

  @Override
  public GrpManageForm getModel() {
    if(form == null){
      form = new GrpManageForm();
    }
    return form;
  }

  public File getSourceFile() {
    return sourceFile;
  }

  public void setSourceFile(File sourceFile) {
    this.sourceFile = sourceFile;
  }

  public String getFileType() {
    return fileType;
  }

  public void setFileType(String fileType) {
    this.fileType = fileType;
  }

  public String getSourceFileFileName() {
    return sourceFileFileName;
  }

  public void setSourceFileFileName(String sourceFileFileName) {
    this.sourceFileFileName = sourceFileFileName;
  }



  @Override
  public void prepare() throws Exception {

  }

}
