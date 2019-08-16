package com.smate.core.base.utils.tags;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.apache.commons.lang3.StringUtils;

/**
 * 通过文件后缀解析文件类型的样式CLASS.
 * 
 * @author liqinghua
 * 
 */
public class FileTypeTag extends BodyTagSupport {

  /**
   * 
   */
  private static final long serialVersionUID = -226914969057361309L;
  private String fileExt = "";

  public int doStartTag() throws JspException {

    String cssClass = "file";
    if (StringUtils.isNotBlank(fileExt)) {
      fileExt = fileExt.toLowerCase();
      if (fileExt.indexOf("xls") > -1 || fileExt.indexOf("xlsx") > -1) {
        cssClass = "xls";
      } else if (fileExt.indexOf("ppt") > -1 || fileExt.indexOf("pptx") > -1) {
        cssClass = "ppt";
      } else if (fileExt.indexOf("doc") > -1 || fileExt.indexOf("docx") > -1) {
        cssClass = "doc";
      } else if (fileExt.indexOf("rar") > -1 || fileExt.indexOf("zip") > -1) {
        cssClass = "rar";
      } else if (fileExt.indexOf("txt") > -1) {
        cssClass = "txt";
      } else if (fileExt.indexOf("pdf") > -1) {
        cssClass = "pdf";
      } else if (fileExt.indexOf("jpg") > -1 || fileExt.indexOf("jpeg") > -1 || fileExt.indexOf("png") > -1
          || fileExt.indexOf("gif") > -1 || fileExt.indexOf("bmp") > -1 || fileExt.indexOf("jpe") > -1
          || fileExt.indexOf("jfif") > -1 || fileExt.indexOf("dib") > -1 || fileExt.indexOf("tif") > -1
          || fileExt.indexOf("tiff") > -1) {
        cssClass = "imgIc";
      }
    }

    try {
      pageContext.getOut().write(cssClass);
    } catch (IOException e) {

    }
    return SKIP_BODY;
  }

  public String getFileExt() {
    return fileExt;
  }

  public void setFileExt(String fileExt) {
    this.fileExt = fileExt;
  }

}
