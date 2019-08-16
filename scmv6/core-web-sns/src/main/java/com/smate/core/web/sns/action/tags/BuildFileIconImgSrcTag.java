package com.smate.core.web.sns.action.tags;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.apache.commons.lang3.StringUtils;

/**
 * 重新构建文件图标图片地址
 * 
 * @author wsn
 * @date May 16, 2019
 */
public class BuildFileIconImgSrcTag extends BodyTagSupport {

  private String fileType; // 文件类型
  private String defaultSrc; // 默认图片地址

  public int doStartTag() throws JspException {
    String src = "";
    src = buildSrc(fileType, StringUtils.trimToEmpty(defaultSrc));
    try {
      pageContext.getOut().write(src);
    } catch (IOException e) {
    }
    return EVAL_BODY_BUFFERED;
  }

  private String buildSrc(String fileType, String defaultSrc) {
    String iconSrc = defaultSrc;
    if (StringUtils.isNotBlank(fileType)) {
      switch (fileType.toLowerCase()) {
        case "txt":
          iconSrc = "/resmod/smate-pc/img/fileicon_txt.png";
          break;
        case "ppt":
        case "pptx":
          iconSrc = "/resmod/smate-pc/img/fileicon_ppt.png";
          break;
        case "doc":
        case "docx":
          iconSrc = "/resmod/smate-pc/img/fileicon_doc.png";
          break;
        case "rar":
        case "zip":
          iconSrc = "/resmod/smate-pc/img/fileicon_zip.png";
          break;
        case "xls":
        case "xlsx":
          iconSrc = "/resmod/smate-pc/img/fileicon_xls.png";
          break;
        case "pdf":
          iconSrc = "/resmod/smate-pc/img/fileicon_pdf.png";
          break;
        default:
          break;
      }
    }
    if (StringUtils.isBlank(iconSrc)) {
      iconSrc = "/resmod/smate-pc/img/fileicon_default.png";
    }
    return iconSrc;
  }

  public String getFileType() {
    return fileType;
  }

  public void setFileType(String fileType) {
    this.fileType = fileType;
  }

  public String getDefaultSrc() {
    return defaultSrc;
  }

  public void setDefaultSrc(String defaultSrc) {
    this.defaultSrc = defaultSrc;
  }



}
