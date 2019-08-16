package com.smate.web.prj.action.project;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.alibaba.fastjson.JSONArray;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.smate.core.base.utils.cache.CacheService;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.struts2.Struts2Utils;
import com.smate.web.prj.form.PrjImportForm;
import com.smate.web.prj.form.fileimport.PrjInfoDTO;
import com.smate.web.prj.service.project.fileimport.PrjFileImportService;

/**
 * 文件导入项目Action.
 * 
 * @author ai jiangbin
 * @date 2019-06-13
 */
@Results({@Result(name = "import_prj_page", location = "/WEB-INF/jsp/prj/fileimport/exportFile.jsp"),
    @Result(name = "import_prj_list", location = "/WEB-INF/jsp/prj/fileimport/collect_imp_prj_fileList.jsp"),
    @Result(name = "import_prj_result", location = "/WEB-INF/jsp/prj/fileimport/collect_imp_prj_fielResult.jsp")})
public class PrjFileImportAction extends ActionSupport implements Preparable, ModelDriven<PrjImportForm> {

  private final Logger logger = LoggerFactory.getLogger(getClass());
  private PrjImportForm form;
  @Autowired
  private PrjFileImportService prjFileImportService;
  @Autowired
  private CacheService cacheService;
  @Value("${domain.scm.http}")
  private String snsDomain;
  private File sourceFile; // 导入的文件流
  private String fileType; // 导入的文件类型 SCMIRIS SCMEXCEL
  private String sourceFileFileName;
  private String fileName;

  /**
   * 进入文件导入项目页面.
   * 
   * @return
   */
  @Action("/prjweb/fileimport")
  public String showPrjFileImportPage() {

    return "import_prj_page";
  }


  /**
   * 显示待导入的项目列表.
   * 
   * @return
   */
  @Action("/prjweb/fileimport/list")
  public String initImportPrjList() {
    try {
      form.setPsnId(SecurityUtils.getCurrentUserId());
      Map<String, Object> map = prjFileImportService.extractFileData(sourceFile, fileType, sourceFileFileName);
      List<PrjInfoDTO> prjInfos = map.get("list") != null ? (List<PrjInfoDTO>) map.get("list") : null;
      logger.debug("传入的项目json内容：" + JacksonUtils.listToJsonStr(prjInfos));
      if (CollectionUtils.isNotEmpty(prjInfos)) {
        form.setFilePrjInfos(prjInfos);
        // 重新处理下待导入项目的xml
        prjFileImportService.buildPendingImportPrjByXml(form);
      } else {
        form.setErrorMsg("-1");
        return showPrjFileImportPage();
      }
    } catch (Exception e) {
      logger.error("初始化待导入项目数据出错， psnId={}, json={}", form.getPsnId(),
          JacksonUtils.listToJsonStr(form.getFilePrjInfos()), e);
      form.setErrorMsg("构建待导入项目信息出错");
    }
    return "import_prj_list";
  }

  /**
   * 保存检索导入的项目.
   * 
   * @return
   */
  @Action("/prjweb/fileimport/save")
  public String saveImportPrjs() {
    try {
      form.setPsnId(SecurityUtils.getCurrentUserId());
      if (StringUtils.isNotBlank(form.getPrjJsonParams())) {
        JSONArray jsonArray = JSONArray.parseArray(form.getPrjJsonParams());
        form.setPrjJsonList(jsonArray);
      }
      prjFileImportService.savePendingImportPrjs(form);
    } catch (Exception e) {
      logger.error("文件导入项目保存出错, psnId={}", form.getPsnId(), e);
    }
    return "import_prj_result";
  }

  /**
   * 保存检索导入的项目.
   *
   * @return
   */
  @Action("/prjdata/save/data")
  public String savePrjData() {
    Map<String, String> map = new HashMap<String, String>();
    try {
      if (StringUtils.isNotBlank(form.getPrjData()) && NumberUtils.isNotNullOrZero(form.getPsnId())) {
        PrjInfoDTO prjInfoDTO = JacksonUtils.jsonObject(form.getPrjData(), PrjInfoDTO.class);
        if (prjInfoDTO != null) {
          int importSuccessNum = prjFileImportService.savePrjData(prjInfoDTO, form.getPsnId());
          map.put("result", "success");
          map.put("importSuccessNum", importSuccessNum + "");
        } else {
          map.put("result", "error");
        }
      } else {
        map.put("result", "error");
      }
    } catch (Exception e) {
      map.put("result", "error");
      logger.error("项目保存出错, getPrjData={}", form.getPrjData(), e);
    }
    Struts2Utils.renderJson(map, "encoding:utf-8");
    return null;
  }


  /**
   * 删除缓存的待导入项目信息.
   * 
   * @return
   */
  @Action("/prjweb/cache/ajaxremove")
  public String removeCahcePendingImportPrj() {
    Map<String, String> map = new HashMap<String, String>();
    try {
      cacheService.remove("searchImportPrjCache", form.getCacheKey());
      map.put("result", "success");
    } catch (Exception e) {
      logger.error("删除掉缓存的待导入成果出错, psnId ={} , cacheKey = {}", SecurityUtils.getCurrentUserId(), form.getCacheKey(), e);
      map.put("result", "error");
    }
    Struts2Utils.renderJson(map, "encoding:utf-8");
    return null;
  }



  @Override
  public PrjImportForm getModel() {
    return form;
  }

  @Override
  public void prepare() throws Exception {
    if (form == null) {
      form = new PrjImportForm();
    }
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

  public String getFileName() {
    return fileName;
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }


}
