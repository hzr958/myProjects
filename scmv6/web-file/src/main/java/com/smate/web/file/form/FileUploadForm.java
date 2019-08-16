package com.smate.web.file.form;

import java.io.File;

import com.smate.web.file.enums.UploadFileTypeEnum;

/**
 * 
 * @author tsz
 *
 */
public class FileUploadForm {

  private File[] filedata = new File[] {}; // 文件上传对象

  private String[] filedataContentType = new String[] {}; // 文件类型
  private String[] filedataFileName = new String[] {}; // 文件名字

  private String fileDealType; // 文件处理类型 必须有

  private Integer type; // 上传的文件类型

  private String imgData; // 图片编码

  private String fileUniqueKey; // 唯一文件 的路径key

  private String fileDesc;

  private String fileFrom = "sns"; // 文件来源 默认是科研之友来的

  public FileUploadForm() {
    super();
  }

  public File[] getFiledata() {
    return filedata;
  }

  public void setFiledata(File[] filedata) {
    this.filedata = filedata;
  }

  public String[] getFiledataContentType() {
    return filedataContentType;
  }

  public void setFiledataContentType(String[] filedataContentType) {
    this.filedataContentType = filedataContentType;
  }

  public String[] getFiledataFileName() {
    return filedataFileName;
  }

  public void setFiledataFileName(String[] filedataFileName) {
    this.filedataFileName = filedataFileName;
  }

  public String getFileDealType() {
    return fileDealType;
  }

  public void setFileDealType(String fileDealType) {
    this.fileDealType = fileDealType;
  }

  public String getImgData() {
    return imgData;
  }

  public void setImgData(String imgData) {
    this.imgData = imgData;
  }

  public String getFileUniqueKey() {
    return fileUniqueKey;
  }

  public void setFileUniqueKey(String fileUniqueKey) {
    this.fileUniqueKey = fileUniqueKey;
  }

  public Integer getType() {
    return this.type;
  }

  /**
   * 返回文件上传类型对应的枚举值
   * 
   * @return type
   */
  public UploadFileTypeEnum getUploadFileType() {
    return UploadFileTypeEnum.valueOf(type);
  }

  /**
   * @param type 要设置的 type
   */
  public void setType(Integer type) {
    this.type = type;
  }

  /**
   * 校验参数
   * 
   * @author houchuanjie
   * @date 2018年3月9日 下午5:27:44
   * @return
   */
  public boolean validate() {
    try {
      UploadFileTypeEnum fileUploadType = this.getUploadFileType();
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  /**
   * @return fileDesc
   */
  public String getFileDesc() {
    return fileDesc;
  }

  /**
   * @param fileDesc 要设置的 fileDesc
   */
  public void setFileDesc(String fileDesc) {
    this.fileDesc = fileDesc;
  }

  public String getFileFrom() {
    return fileFrom;
  }

  public void setFileFrom(String fileFrom) {
    this.fileFrom = fileFrom;
  }
}
