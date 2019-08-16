package com.smate.web.management.action.archiveFiles;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.smate.core.base.file.model.ArchiveFile;
import com.smate.core.base.file.model.FileDownload;
import com.smate.core.base.utils.file.FileService;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.struts2.Struts2Utils;
import com.smate.web.management.service.institution.ArchiveFilesService;

/**
 * 文件下载共用类.
 * 
 * @author lqh
 * 
 */
@Results({@Result(name = "success", type = "stream"),
    @Result(name = "fileNotExit", location = "/WEB-INF/content/archive-files/fileNotExit.jsp"),
    @Result(name = "redirectAction", location = "${forwardUrl}", type = "redirect")})
public class FileDownloadAction extends ActionSupport implements ModelDriven<FileDownload>, Preparable {

  /**
   * 
   */
  private static final long serialVersionUID = 7946357784811611528L;

  @Resource(name = "archiveFileService")
  private ArchiveFilesService archiveFilesService;

  private FileDownload fileDownload;

  @Autowired
  private FileService fileService;


  public InputStream getInputStream() throws Exception {

    return new FileInputStream(fileDownload.getPath());

  }

  @Action("/scmmanagement/archiveFiles/fileDownload")
  public String execute() throws Exception {

    if (fileDownload.getCurrentFileId() != null) {
      Integer nodeId = fileDownload.getNodeId();
      if (nodeId != null && !SecurityUtils.getCurrentAllNodeId().contains(nodeId)) {
        fileDownload.setForwardUrl("");
        return "redirectAction";
      }
      ArchiveFile af = archiveFilesService.getArchiveFiles(fileDownload.getCurrentFileId());
      if (af == null) {
        return "fileNotExit";
      }
      HttpServletResponse response = Struts2Utils.getResponse();
      HttpServletRequest request = Struts2Utils.getRequest();
      response.setCharacterEncoding("gbk");
      String dir = "bpoupfile";// 专门用来上传bpo文件路径 配置nginx
      if (af.getInsId() != null && af.getInsId() > 0) {
        dir = "rolfile";// 下载rol上传的文件
      }
      String url = "http://" + ((HttpServletRequest) Struts2Utils.getRequest()).getServerName();
      URL fileUrl = new URL(url + "/" + dir + fileService.getFilePath(af.getFilePath()));
      HttpURLConnection connection = (HttpURLConnection) fileUrl.openConnection();

      response.reset();
      String agent = (String) request.getHeader("USER-AGENT");
      if (agent.contains("MSIE 8.0")) {
        response.setContentType("application/octet-stream'");
      } else {
        response.setContentType(connection.getContentType());
      }
      if (agent != null && agent.indexOf("MSIE") == -1) {
        if (agent.indexOf("Windows NT") == -1 || agent.indexOf("Firefox") != -1) { // 解决IE11中文名乱码问题
          String enableFileName =
              "=?UTF-8?B?" + (new String(Base64.encodeBase64(af.getFileName().getBytes("UTF-8")))) + "?=";
          response.setHeader("Content-Disposition", "attachment; filename=" + enableFileName);
        } else {
          response.setHeader("Content-Disposition", "attachment;filename="
              + StringUtils.replace(java.net.URLEncoder.encode(af.getFileName(), "UTF-8"), "+", "%20"));
        }

      } else {
        response.setHeader("Content-Disposition", "attachment;filename="
            + StringUtils.replace(java.net.URLEncoder.encode(af.getFileName(), "UTF-8"), "+", "%20"));
      }

      response.setContentLength(connection.getContentLength());

      DataInputStream in = new DataInputStream(connection.getInputStream());
      OutputStream out = response.getOutputStream();
      BufferedInputStream buf = new BufferedInputStream(in);
      int len = 0;
      byte[] buffer = new byte[in.available()];
      while ((len = buf.read(buffer)) > 0) {
        out.write(buffer, 0, len);
      }
      out.flush();
      out.close();
      buf.close();
      return null;
    }

    return SUCCESS;
  }


  /*
   * public String getFileUrl() throws Exception {
   * 
   * String fileUrl = BaseFileLinkTag.getFileUrl(fileUrl, fileDownload.getFileId(),
   * fileDownload.getNodeId(), fileDownload.getInsId()); return fileUrl; }
   */


  @Override
  public FileDownload getModel() {
    return fileDownload;
  }

  @Override
  public void prepare() throws Exception {
    fileDownload = new FileDownload();

  }

}
