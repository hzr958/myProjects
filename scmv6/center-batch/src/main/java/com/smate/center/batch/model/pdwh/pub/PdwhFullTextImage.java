package com.smate.center.batch.model.pdwh.pub;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 基准库成果全文图片信息表
 * 
 * @author LJ
 *
 *         2017年9月12日
 */
@Entity
@Table(name = "PDWH_FULLTEXT_IMG")
public class PdwhFullTextImage implements Serializable {
  private static final long serialVersionUID = 159565850155455205L;
  private Long pubId;// 基准库成果Id
  private Long fileId;// 全文文件Id
  private Integer imagePageIndex;// pdf转图片的页数
  private String imagePath;// 图片路径
  private String fileExtend;// 全文文件扩展名
  private Date updateTime;// 最后更新时间

  @Id
  @Column(name = "PUB_ID")
  public Long getPubId() {
    return pubId;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  @Column(name = "FILE_ID")
  public Long getFileId() {
    return fileId;
  }

  public void setFileId(Long fileId) {
    this.fileId = fileId;
  }

  @Column(name = "FULLTEXT_IMAGE_PAGE_INDEX")
  public Integer getImagePageIndex() {
    return imagePageIndex;
  }

  public void setImagePageIndex(Integer imagePageIndex) {
    this.imagePageIndex = imagePageIndex;
  }

  @Column(name = "FULLTEXT_IMAGE_PATH")
  public String getImagePath() {
    return imagePath;
  }

  public void setImagePath(String imagePath) {
    this.imagePath = imagePath;
  }

  @Column(name = "FULLTEXT_FILE_EXT")
  public String getFileExtend() {
    return fileExtend;
  }

  public void setFileExtend(String fileExtend) {
    this.fileExtend = fileExtend;
  }

  @Column(name = "UPDATE_TIME")
  public Date getUpdateTime() {
    return updateTime;
  }

  public void setUpdateTime(Date updateTime) {
    this.updateTime = updateTime;
  }

}
