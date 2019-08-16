package com.smate.web.file.form;

import com.smate.core.base.file.model.ArchiveFile;
import com.smate.core.base.utils.json.JacksonUtils;

/**
 * 下载文件资源类
 *
 * @author houchuanjie
 * @date 2018年3月7日 下午3:00:52
 * @param <T> 具体业务文件模型类，如PsnFile/GroupFile等
 */
public class DownloadFileRes {
  /**
   * 具体业务文件对象的id
   */
  private Long id;
  /**
   * 具体业务文件对象
   */
  private Object specificFile;
  /**
   * 具体业务文件对象类型
   */
  private Class specificFileClass;
  /**
   * 对应的附件对象
   */
  private ArchiveFile archiveFile;
  /**
   * 该文件资源的真实下载url
   */
  private String url;
  /**
   * 该文件资源的真实名称
   */
  private String name;
  /**
   * 该文件资源的所属人员id
   */
  private Long ownerPsnId;

  /**
   * @param id
   * @param specificFile
   */
  public DownloadFileRes(Long id, Object specificFile) {
    super();
    this.id = id;
    this.specificFile = specificFile;
  }

  /**
   * @param id
   * @param specificFile
   * @param archiveFile
   */
  public DownloadFileRes(Long id, Object specificFile, ArchiveFile archiveFile) {
    super();
    this.id = id;
    this.specificFile = specificFile;
    this.archiveFile = archiveFile;
  }

  /**
   * @return id
   */
  public Long getId() {
    return id;
  }

  /**
   * 业务文件对象的id
   * 
   * @param id 要设置的 id
   */
  public void setId(Long id) {
    this.id = id;
  }

  /**
   * 业务文件对象
   * 
   * @return specificFile
   */
  public Object getSpecificFile() {
    return specificFile;
  }

  /**
   * 设置业务文件
   * 
   * @param specificFile 要设置的 specificFile
   */
  public void setSpecificFile(Object specificFile) {
    this.specificFile = specificFile;
  }

  /**
   * 附件对象
   * 
   * @return archiveFile
   */
  public ArchiveFile getArchiveFile() {
    return archiveFile;
  }

  /**
   * 设置附件对象
   * 
   * @param archiveFile 要设置的 archiveFile
   */
  public void setArchiveFile(ArchiveFile archiveFile) {
    this.archiveFile = archiveFile;
  }

  /**
   * 获取该文件资源的真实下载地址
   * 
   * @return url
   */
  public String getUrl() {
    return url;
  }

  /**
   * 设置该文件资源的真实下载地址
   * 
   * @param url 要设置的 url
   */
  public void setUrl(String url) {
    this.url = url;
  }

  /**
   * 获取该文件资源的名称
   * 
   * @return name
   */
  public String getName() {
    return name;
  }

  /**
   * 设置该文件资源的名称
   * 
   * @param name 要设置的 name
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * 获取该文件资源的所属人id
   * 
   * @return ownerPsnId
   */
  public Long getOwnerPsnId() {
    return ownerPsnId;
  }

  /**
   * 设置该文件资源的所属人id
   * 
   * @param ownerPsnId 要设置的 ownerPsnId
   */
  public void setOwnerPsnId(Long ownerPsnId) {
    this.ownerPsnId = ownerPsnId;
  }

  @Override
  public String toString() {
    return JacksonUtils.jsonObjectSerializer(this);
  }
}
