package com.smate.sie.web.application.action.validate;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.smate.core.base.file.model.FileDownload;
import com.smate.core.base.file.model.Sie6ArchiveFile;
import com.smate.core.base.file.service.Sie6ArchiveFileService;
import com.smate.core.base.utils.file.FileService;
import com.smate.core.base.utils.mobile.SmateMobileUtils;
import com.smate.core.base.utils.struts2.Struts2Utils;

public class SieFileDownloadAction extends ActionSupport implements ModelDriven<FileDownload>, Preparable {

  private static final long serialVersionUID = -7063550880400652494L;

  private FileDownload fileDownload;

  private String fileName;
  private String number;
  @Value("${domainscm}")
  private String domainscm;
  @Autowired
  private Sie6ArchiveFileService sie6ArchiveFileService;
  @Autowired
  private FileService fileService;

  @Action("/application/validate/fileDownload")
  public String FileDownload() throws Exception {
    if (getModel().getCurrentFileId() != null) {
      Sie6ArchiveFile sieFile = sie6ArchiveFileService.getArchiveFile(getModel().getCurrentFileId());
      if (sieFile == null) {
        return "fileNotExit";
      }
      HttpServletResponse response = Struts2Utils.getResponse();
      HttpServletRequest request = Struts2Utils.getRequest();
      response.setCharacterEncoding("gbk");
      URL fileUrl = new URL(domainscm + "/" + "siedownvarifyfile" + fileService.getFilePath(sieFile.getFilePath()));
      HttpURLConnection connection = (HttpURLConnection) fileUrl.openConnection();
      response.reset();
      String agent = (String) request.getHeader("USER-AGENT");
      if (agent.contains("MSIE 8.0")) {
        response.setContentType("application/octet-stream'");
      } else {
        if (SmateMobileUtils.isMobileBrowser(agent)) {
          if (SmateMobileUtils.isIphone(agent)) {
            response.setContentType(connection.getContentType());
          } else {
            response.setContentType("application/octet-stream");
          }
        } else {
          response.setContentType(connection.getContentType());
        }
      }
      if (agent != null && agent.indexOf("MSIE") == -1) {
        if (SmateMobileUtils.isMobileBrowser(agent)) {
          response.setHeader("Content-Disposition", "attachment;filename="
              + StringUtils.replace(java.net.URLEncoder.encode(sieFile.getFileName(), "UTF-8"), "+", "%20"));
        } else {
          if (agent.indexOf("Windows NT") == -1 || agent.indexOf("Firefox") != -1) { // 解决IE11中文名乱码问题
            String enableFileName =
                "=?UTF-8?B?" + (new String(Base64.encodeBase64(sieFile.getFileName().getBytes("UTF-8")))) + "?=";
            response.setHeader("Content-Disposition", "attachment; filename=" + enableFileName);
          } else {
            response.setHeader("Content-Disposition", "attachment;filename="
                + StringUtils.replace(java.net.URLEncoder.encode(sieFile.getFileName(), "UTF-8"), "+", "%20"));
          }
        }
      } else {
        response.setHeader("Content-Disposition", "attachment;filename="
            + StringUtils.replace(java.net.URLEncoder.encode(sieFile.getFileName(), "UTF-8"), "+", "%20"));
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
    return null;
  }

  @Override
  public void prepare() throws Exception {
    fileDownload = new FileDownload();
  }

  @Override
  public FileDownload getModel() {
    return fileDownload;
  }

  public FileDownload getFileDownload() {
    return fileDownload;
  }

  public void setFileDownload(FileDownload fileDownload) {
    this.fileDownload = fileDownload;
  }

  public String getFileName() {
    return fileName;
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  public String getNumber() {
    return number;
  }

  public void setNumber(String number) {
    this.number = number;
  }

}
