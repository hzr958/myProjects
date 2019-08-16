package com.smate.web.management.action;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.smate.core.base.utils.string.MapBuilder;
import com.smate.core.base.utils.struts2.Struts2Utils;
import com.smate.web.management.model.institution.bpo.FileUploadSimple;
import com.smate.web.management.service.institution.ApproveService;
import com.smate.web.management.service.institution.ArchiveFilesService;
import com.smate.web.management.service.institution.InstitutionBpoService;

public class FileUploadSimpleAction extends ActionSupport implements ModelDriven<FileUploadSimple>, Preparable {

  /**
   * 
   */
  private static final long serialVersionUID = 5985209298415824925L;
  Logger logger = LoggerFactory.getLogger(getClass());
  private FileUploadSimple fileUploadSimple;
  private String pageType;
  @Autowired
  private InstitutionBpoService institutionBpoService;
  @Autowired
  private ApproveService approveService;
  @Resource(name = "archiveFileService")
  private ArchiveFilesService archiveFilesService;

  @Override
  public void prepare() throws Exception {
    fileUploadSimple = new FileUploadSimple();
  }

  @Override
  public FileUploadSimple getModel() {
    return fileUploadSimple;
  }

  /**
   * 上传传真文件
   * 
   * @return
   * @throws Exception
   */
  @Action("/scmmanagement/archiveFiles/ajaxSimpleUploadFax")
  public String ajaxUploadFaxFile() throws Exception {
    try {
      if ("edit".equals(pageType)) {
        fileUploadSimple = institutionBpoService.uploadAndSaveInsFax(fileUploadSimple);
      } else {
        fileUploadSimple = approveService.uploadAndSaveFaxAttachment(fileUploadSimple);
      }
      if (StringUtils.isBlank(fileUploadSimple.getSaveResult())) {
        fileUploadSimple
            .setSaveResult(MapBuilder.getInstance().put("result", "success").put("msg", "上传和保存单位传真成功").getJson());
      }
    } catch (Exception e) {
      logger.error("上传和保存单位传真时系统出现异常：", e);
      fileUploadSimple
          .setSaveResult(MapBuilder.getInstance().put("result", "error").put("msg", "上传和保存单位传真失败").getJson());
    }

    HttpServletResponse response = Struts2Utils.getResponse();
    response.setHeader("Charset", "UTF-8");
    response.setContentType("text/html;charset=UTF-8");
    response.getWriter().print(fileUploadSimple.getSaveResult());
    return null;
  }

  /**
   * 上传单位Logo.
   * 
   * @return
   * @throws Exception
   */
  @Action("/scmmanagement/archiveFiles/ajaxUploadLogo")
  public String ajaxUploadInsLogoFile() throws Exception {
    fileUploadSimple.setLimitLength("100KB");
    fileUploadSimple.setAllowType("*.jpg,*.jpeg,*.gif,*.png,*.bmp");
    if ("edit".equals(pageType)) {
      try {
        fileUploadSimple = institutionBpoService.uploadAndSaveInsLogo(fileUploadSimple);
      } catch (Exception e) {
        logger.error("上传和保存单位Log时系统出现异常：", e);
        fileUploadSimple.setSaveResult(MapBuilder.getInstance().put("result", "error").put("msg", "上传和保存单位Logo失败")
            .put("path", fileUploadSimple.getArchiveFile().getFilePath()).getJson());
      }
    } else {
      fileUploadSimple = approveService.uploadAndSaveInsLog(fileUploadSimple);
      if (StringUtils.isBlank(fileUploadSimple.getSaveResult())) {
        fileUploadSimple.setSaveResult(MapBuilder.getInstance().put("result", "success").put("msg", "上传和保存单位Logo成功")
            .put("path", fileUploadSimple.getArchiveFile().getFilePath()).getJson());
      }
    }
    HttpServletResponse response = Struts2Utils.getResponse();
    response.setHeader("Charset", "UTF-8");
    response.setContentType("text/html;charset=UTF-8");
    response.getWriter().print(fileUploadSimple.getSaveResult());
    return null;
  }

  /**
   * 处理AJAX上传.
   * 
   * @return
   * @throws Exception
   */
  @Action("/scmmanagement/archiveFiles/ajaxUploadFund")
  public String ajaxUploadFund() throws Exception {
    /* fileUploadSimple.setLimitSize(10L); */
    fileUploadSimple = archiveFilesService.saveArchiveFundFiles(fileUploadSimple);
    HttpServletResponse response = Struts2Utils.getResponse();
    response.setHeader("Charset", "UTF-8");
    response.setContentType("text/html;charset=UTF-8");
    String jsonStr = fileUploadSimple.getSaveResult();
    response.getWriter().print(jsonStr);
    return null;
  }

  public String getPageType() {
    return pageType;
  }

  public void setPageType(String pageType) {
    this.pageType = pageType;
  }

}
