package com.smate.web.v8pub.po.sns;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.smate.core.base.file.enums.FileTypeEnum;
import com.smate.core.base.file.enums.converter.FileTypeEnumAttributeConverter;

/**
 * 文件下载记录实体类
 * 
 * @author ChuanjieHou
 * @date 2017年9月14日
 */
@Entity
@Table(name = "V_FILE_DOWNLOAD_RECORD")
public class FileDownloadRecord {
  /**
   * 文件下载记录逻辑id
   */
  @Id
  @Column(name = "ID")
  @SequenceGenerator(sequenceName = "SEQ_V_FILE_DOWNLOAD_RECORD", name = "seq_file_download_record")
  @GeneratedValue(generator = "seq_file_download_record")
  private Long id;

  /**
   * 文件id
   */
  @Column(name = "FILE_ID")
  private Long fileId;

  /**
   * 文件类型<br>
   * 
   * <pre>
   * FileTypeEnum.PSN             :   个人文件类型 <br>
   * FileTypeEnum.GROUP           :   群组文件类型 <br>
   * FileTypeEnum.SNS_FULLTEXT    :   个人成果全文类型 <br>
   * FileTypeEnum.PDWH_FULLTEXT   :   基准库成果全文类型
   * </pre>
   */
  @Convert(converter = FileTypeEnumAttributeConverter.class)
  @Column(name = "FILE_TYPE")
  private FileTypeEnum fileType;

  /**
   * 下载人员id
   */
  @Column(name = "DOWNLOAD_PSN_ID")
  private Long downloadPsnId;

  @Column(name = "OWNER_PSN_ID")
  private Long ownerPsnId;

  /**
   * 文件下载时间
   */
  @Column(name = "DOWNLOAD_DATE")
  private Date downloadDate;

  /**
   * 文件下载的客户端访问请求来源（Reffer）
   */
  @Column(name = "DOWNLOAD_SOURCE")
  private String downloadSource;

  /**
   * 文件下载的客户端IP地址
   */
  @Column(name = "DOWNLOAD_IP")
  private String downloadIp;

  /**
   * 文件下载记录逻辑id
   */
  public Long getId() {
    return id;
  }

  /**
   * 文件下载记录逻辑id
   */
  public void setId(Long id) {
    this.id = id;
  }

  /**
   * 文件id
   */
  public Long getFileId() {
    return fileId;
  }

  /**
   * 文件id
   */
  public void setFileId(Long fileId) {
    this.fileId = fileId;
  }

  /**
   * 文件类型: <br>
   * 
   * <pre>
   * FileTypeEnum.PSN             :   个人文件类型 <br>
   * FileTypeEnum.GROUP           :   群组文件类型 <br>
   * FileTypeEnum.SNS_FULLTEXT    :   个人成果全文类型 <br>
   * FileTypeEnum.PDWH_FULLTEXT   :   基准库成果全文类型
   * </pre>
   */
  public FileTypeEnum getFileType() {
    return fileType;
  }

  /**
   * 文件类型: <br>
   * 
   * <pre>
   * FileTypeEnum.PSN             :   个人文件类型 <br>
   * FileTypeEnum.GROUP           :   群组文件类型 <br>
   * FileTypeEnum.SNS_FULLTEXT    :   个人成果全文类型 <br>
   * FileTypeEnum.PDWH_FULLTEXT   :   基准库成果全文类型
   * </pre>
   */
  public void setFileType(FileTypeEnum fileType) {
    this.fileType = fileType;
  }

  /**
   * 下载人员id
   */
  public Long getDownloadPsnId() {
    return downloadPsnId;
  }

  /**
   * 下载人员id
   */
  public void setDownloadPsnId(Long downloadPsnId) {
    this.downloadPsnId = downloadPsnId;
  }

  /**
   * 文件下载时间
   */
  public Date getDownloadDate() {
    return downloadDate;
  }

  /**
   * 文件下载时间
   */
  public void setDownloadDate(Date downloadDate) {
    this.downloadDate = downloadDate;
  }

  /**
   * 文件下载的客户端访问请求来源（Reffer）
   */
  public String getDownloadSource() {
    return downloadSource;
  }

  /**
   * 文件下载的客户端访问请求来源（Reffer）
   */
  public void setDownloadSource(String downloadSource) {
    this.downloadSource = downloadSource;
  }

  /**
   * 文件下载的客户端IP地址
   */
  public String getDownloadIp() {
    return downloadIp;
  }

  /**
   * 文件下载的客户端IP地址
   */
  public void setDownloadIp(String downloadIp) {
    this.downloadIp = downloadIp;
  }

  /**
   * @return ownerPsnId 文件资源所有者id
   */
  public Long getOwnerPsnId() {
    return ownerPsnId;
  }

  /**
   * @param ownerPsnId 要设置的文件资源所有者id
   */
  public void setOwnerPsnId(Long ownerPsnId) {
    this.ownerPsnId = ownerPsnId;
  }

}
