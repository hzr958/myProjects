package com.smate.web.file.action.upload;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.smate.core.base.file.enums.FileTypeEnum;
import com.smate.core.base.file.service.FileDownloadUrlService;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.struts2.Struts2Utils;
import com.smate.web.file.form.FileUploadForm;
import com.smate.web.file.model.FileInfo;
import com.smate.web.file.service.upload.FileUploadService;

/**
 * 文件上传 支持多个文件上传
 * 
 * @author tsz
 *
 */
@Results({@Result(name = "main", location = "/WEB-INF/jsp/pdf_reader_main.jsp")})
public class AppFileUploadAction extends ActionSupport implements Preparable, ModelDriven<FileUploadForm> {

  protected final Logger logger = LoggerFactory.getLogger(getClass());
  private static final long serialVersionUID = -384378994398035107L;
  private FileUploadForm form;
  @Autowired
  private FileUploadService fileUploadService;
  @Autowired
  private FileDownloadUrlService fileDownUrlService;
  @Value("${domainscm}")
  private String domainscm;
  // 文件根路径
  @Value("${file.root}")
  private String rootPath;

  /**
   * 文件上传 支持多个文件 在提交数据的时候够着就好，一个文件标签选多个文件，或者 多个文件标签 每个标签选一个文件都可
   * 
   * @return
   * @throws Exception
   */
  @Action("/fileweb/upload/app")
  public void fileUpload() throws Exception {
    // 上传APP安装文件
    Map<String, Object> result = uploadAppFile();
    // 构建返回值
    Map<String, Object> map = buildResponseFileData(result);
    // 响应请求，返回数据
    this.responseReq(map);
  }

  private Map<String, String> buildExtentInfo(FileInfo fileInfo) {
    Map<String, String> info = new HashMap<String, String>();
    info.put("des3FileId", Des3Utils.encodeToDes3(fileInfo.getArchiveFile().getFileId().toString()));
    info.put("fileName", fileInfo.getArchiveFile().getFileName());
    info.put("fileUrl", fileInfo.getArchiveFile().getFileUrl());
    info.put("fileType", fileInfo.getArchiveFile().getFileType());
    info.put("fileSize", fileInfo.getArchiveFile().getFileSize().toString());
    String downloadUrl =
        fileDownUrlService.getShortDownloadUrl(FileTypeEnum.ARCHIVE_FILE, fileInfo.getArchiveFile().getFileId());
    info.put("downloadUrl", downloadUrl);
    return info;
  }

  private FileInfo buildFileInfo(FileUploadForm form, int index) {
    FileInfo fileInfo = new FileInfo();
    File file = form.getFiledata()[index];
    fileInfo.setFile(file);
    String fileName = form.getFiledataFileName()[index];
    fileInfo.setFileName(fileName);
    String fileType = form.getFiledataContentType()[index];
    fileInfo.setFileContentType(fileType);
    fileInfo.setFileDealType(form.getFileDealType());
    fileInfo.setRootPath(rootPath);
    fileInfo.setCreatePsnId(SecurityUtils.getCurrentUserId());
    fileInfo.setFileDesc(form.getFileDesc());
    fileInfo.setFileForm(form.getFileFrom());
    return fileInfo;
  }

  /**
   * 响应请求，返回数据
   * 
   * @throws IOException
   */
  private void responseReq(Map<String, Object> map) throws IOException {
    HttpServletResponse response = Struts2Utils.getResponse();
    String str = Struts2Utils.getHttpReferer();
    if (StringUtils.isNotBlank(str)) {
      URL url = new URL(str);
      String domain = url.getProtocol() + "://" + url.getHost();
      response.addHeader("Access-Control-Allow-Origin", domain);
      response.addHeader("Access-Control-Allow-Credentials", "true");
    }
    response.setHeader("Charset", "UTF-8");
    response.setContentType("text/html;charset=UTF-8");
    response.getWriter().print(JacksonUtils.jsonObjectSerializer(map));
  }

  /**
   * 上传APP安装文件
   * 
   * @return
   */
  private Map<String, Object> uploadAppFile() {
    Map<String, Object> result = new HashMap<String, Object>();
    int successFiles = 0; // 上传成功个数
    int failFiles = 0; // 上传失败个数
    StringBuilder failFileNames = new StringBuilder();
    StringBuilder successDes3FileIds = new StringBuilder();
    List<Map<String, String>> extendFileInfo = new ArrayList<Map<String, String>>();
    // 文件上传
    FileInfo fileInfo = this.buildFileInfo(form, 0);
    Boolean saveFileResult = true;
    try {
      saveFileResult = fileUploadService.saveFile(fileInfo);
      if (saveFileResult) {
        // 为了安全，所以加了密。
        successDes3FileIds.append(Des3Utils.encodeToDes3(fileInfo.getArchiveFile().getFileId().toString()) + ",");
        successFiles++;
        extendFileInfo.add(buildExtentInfo(fileInfo));
      } else {// 记录失败记录
        failFiles++;
        failFileNames.append(form.getFiledataFileName()[0] + "，").append(fileInfo.getResultMsg());
      }
    } catch (Exception e) {
      failFiles++;
      failFileNames.append(form.getFiledataFileName()[0] + "上传异常");
    }
    result.put("successFiles", successFiles);
    result.put("failFiles", failFiles);
    result.put("failFileNames", failFileNames);
    result.put("successDes3FileIds", successDes3FileIds);
    result.put("extendFileInfo", extendFileInfo);
    return result;
  }

  /**
   * 构建返回值
   * 
   * @return
   */
  @SuppressWarnings("unchecked")
  private Map<String, Object> buildResponseFileData(Map<String, Object> result) {
    int successFiles = NumberUtils.toInt(Objects.toString(result.get("successFiles"), "0"), 0); // 上传成功个数
    int failFiles = NumberUtils.toInt(Objects.toString(result.get("failFiles"), "0"), 0); // 上传失败个数
    String failFileNames = Objects.toString(result.get("failFileNames"), "");// 上传失败文件名称
    String successDes3FileIds = Objects.toString(result.get("successDes3FileIds"), "");// 上传成功文件ID
    List<Map<String, String>> extendFileInfo = (ArrayList<Map<String, String>>) result.get("extendFileInfo");// 上传成功的文件信息
    String successDes3fids = "";
    if (StringUtils.isNotBlank(successDes3FileIds) && successDes3FileIds.lastIndexOf(",") > -1) {
      successDes3fids = successDes3FileIds.substring(0, successDes3FileIds.lastIndexOf(","));
    }
    HashMap<String, Object> map = new HashMap<String, Object>();
    if (successFiles == 1) {
      map.put("fileName", form.getFiledataFileName()[0]);
      map.put("result", "success");
      map.put("fileId", Des3Utils.decodeFromDes3(successDes3fids));
    } else {
      map.put("result", "error");
      map.put("msg", "系统出现异常");
    }
    map.put("fileDesc", form.getFileDesc());
    map.put("createTime", DateFormatUtils.format(new Date(), "yyyy/MM/dd"));
    map.put("successDes3FileIds", successDes3fids);
    map.put("successFiles", successFiles + "");
    map.put("failFiles", failFiles + "");
    map.put("failFileNames", failFileNames.toString());
    map.put("extendFileInfo", extendFileInfo);
    map.put("files", "1");
    return map;
  }

  @Override
  public FileUploadForm getModel() {
    return form;
  }

  @Override
  public void prepare() throws Exception {
    if (form == null) {
      form = new FileUploadForm();
    }

  }

  public FileUploadForm getForm() {
    return form;
  }

  public void setForm(FileUploadForm form) {
    this.form = form;
  }

  public String getRootPath() {
    return rootPath;
  }

  public void setRootPath(String rootPath) {
    this.rootPath = rootPath;
  }
}
