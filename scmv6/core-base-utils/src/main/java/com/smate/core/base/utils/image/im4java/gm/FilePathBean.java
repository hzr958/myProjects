package com.smate.core.base.utils.image.im4java.gm;

/**
 * 文件缩略批处理类
 * 
 * @author LIJUN
 * @date 2018年4月10日
 */
public class FilePathBean {
  private Long id;
  private Long fileId;// 文件id
  // pdf/图片名
  private String fileName;

  // 图片源路径
  private String fileFullPath;

  // 图片压缩后路径
  private String fileFullToPath;

  // 压缩高度
  private String height;

  // 压缩宽度
  private String width;

  // 压缩质量
  private String quality;

  public FilePathBean() {
    super();
  }

  public FilePathBean(Long id, Long fileId, String fileFullPath, String fileFullToPath) {
    super();
    this.id = id;
    this.fileId = fileId;
    this.fileFullPath = fileFullPath;
    this.fileFullToPath = fileFullToPath;
  }

  public FilePathBean(Long id, Long fileId, String fileName, String fileFullPath, String fileFullToPath, String height,
      String width, String quality) {
    super();
    this.id = id;
    this.fileId = fileId;
    this.fileName = fileName;
    this.fileFullPath = fileFullPath;
    this.fileFullToPath = fileFullToPath;
    this.height = height;
    this.width = width;
    this.quality = quality;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getFileId() {
    return fileId;
  }

  public void setFileId(Long fileId) {
    this.fileId = fileId;
  }

  public String getFileName() {
    return fileName;
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  public String getFileFullPath() {
    return fileFullPath;
  }

  public void setFileFullPath(String fileFullPath) {
    this.fileFullPath = fileFullPath;
  }

  public String getFileFullToPath() {
    return fileFullToPath;
  }

  public void setFileFullToPath(String fileFullToPath) {
    this.fileFullToPath = fileFullToPath;
  }

  public String getHeight() {
    return height;
  }

  public void setHeight(String height) {
    this.height = height;
  }

  public String getWidth() {
    return width;
  }

  public void setWidth(String width) {
    this.width = width;
  }

  public String getQuality() {
    return quality;
  }

  public void setQuality(String quality) {
    this.quality = quality;
  }

}
