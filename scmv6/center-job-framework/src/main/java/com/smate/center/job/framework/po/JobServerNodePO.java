package com.smate.center.job.framework.po;

import com.smate.center.job.framework.support.JobConfig;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 任务服务器节点实体类
 *
 * @author houchuanjie
 * @date 2018/04/02 15:34
 */
@Entity
@Table(name = "V_JOB_SERVER_NODE")
public class JobServerNodePO implements Serializable {

  private static final long serialVersionUID = 1437810963355385645L;

  @Id
  @SequenceGenerator(name = "id_seq", sequenceName = "SEQ_V_JOB_SERVER_NODE")
  @GeneratedValue(generator = "id_seq")
  @Column(name = "ID")
  private Integer id;

  @Column(name = "NAME", unique = true)
  private String name;

  @Column(name = "HOST")
  private String host;

  /**
   * 线程池最大线程数
   */
  @Column(name = "MAX_POOL_SIZE")
  private Integer maxPoolSize;

  /**
   * 线程池核心线程数
   */
  @Column(name = "CORE_POOL_SIZE")
  private Integer corePoolSize;

  /**
   * 线程最大空闲时间，单位：秒
   */
  @Column(name = "KEEP_ALIVE_SECONDS")
  private Integer keepAliveSeconds;
  /**
   * 线程池队列大小
   */
  @Column(name = "QUEUE_CAPACITY")
  private Integer queueCapacity;

  @Column(name = "GMT_CREATE")
  private Date gmtCreate;

  @Column(name = "GMT_MODIFIED")
  private Date gmtModified;

  /**
   * 可用线程数
   */
  @Transient
  private Integer availablePoolSize = 0;

  /**
   * 可用队列大小
   */
  @Transient
  private Integer availableQueueSize = 0;

  public JobServerNodePO() {
  }

  public JobServerNodePO(String name, String host) {
    this.name = name;
    this.host = host;
    this.corePoolSize = JobConfig.DEFAULT_CORE_POOL_SIZE;
    this.maxPoolSize = JobConfig.DEFAULT_MAX_POOL_SIZE;
    this.queueCapacity = JobConfig.DEFAULT_QUEUE_CAPACITY;
    this.keepAliveSeconds = JobConfig.DEFAULT_KEEP_ALIVE_SECONDS;
    this.availablePoolSize = this.corePoolSize;
    this.availableQueueSize = this.queueCapacity;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  /**
   * 服务器名称
   *
   * @return
   */
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  /**
   * 服务器ip
   *
   * @return
   */
  public String getHost() {
    return host;
  }

  public void setHost(String host) {
    this.host = host;
  }

  /**
   * 创建时间
   *
   * @return
   */
  public Date getGmtCreate() {
    return gmtCreate;
  }

  public void setGmtCreate(Date gmtCreate) {
    this.gmtCreate = gmtCreate;
  }

  public Integer getMaxPoolSize() {
    return maxPoolSize;
  }

  public void setMaxPoolSize(Integer maxPoolSize) {
    this.maxPoolSize = maxPoolSize;
  }

  public Integer getCorePoolSize() {
    return corePoolSize;
  }

  public void setCorePoolSize(Integer corePoolSize) {
    this.corePoolSize = corePoolSize;
  }

  public Integer getKeepAliveSeconds() {
    return keepAliveSeconds;
  }

  public void setKeepAliveSeconds(Integer keepAliveSeconds) {
    this.keepAliveSeconds = keepAliveSeconds;
  }

  public Integer getQueueCapacity() {
    return queueCapacity;
  }

  public void setQueueCapacity(Integer queueCapacity) {
    this.queueCapacity = queueCapacity;
  }

  /**
   * 修改时间
   *
   * @return
   */
  public Date getGmtModified() {
    return gmtModified;
  }

  public void setGmtModified(Date gmtModified) {
    this.gmtModified = gmtModified;
  }

  public Integer getAvailablePoolSize() {
    return availablePoolSize;
  }

  public void setAvailablePoolSize(Integer availablePoolSize) {
    this.availablePoolSize = availablePoolSize;
  }

  public Integer getAvailableQueueSize() {
    return availableQueueSize;
  }

  public void setAvailableQueueSize(Integer availableQueueSize) {
    this.availableQueueSize = availableQueueSize;
  }

  @Override
  public String toString() {
    return "JobServerNodePO{" + "id=" + id + ", name='" + name + '\'' + ", host='" + host + '\''
        + ", maxPoolSize="
        + maxPoolSize + ", corePoolSize=" + corePoolSize + ", keepAliveSeconds=" + keepAliveSeconds
        + ", queueCapacity=" + queueCapacity + ", gmtCreate=" + gmtCreate + ", gmtModified="
        + gmtModified
        + ", availablePoolSize=" + availablePoolSize + ", availableQueueSize=" + availableQueueSize
        + '}';
  }

  @Override
  public int hashCode() {
    int result = this.id.hashCode();
    result = 31 * result + 17 * name.hashCode();
    return result;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (Objects.isNull(o) || getClass() != o.getClass()) {
      return false;
    }

    JobServerNodePO that = (JobServerNodePO) o;

    if (Objects.isNull(that.id) || !Objects.equals(id, that.id)) {
      return false;
    }
    if (Objects.isNull(that.name) || !Objects.equals(name, that.name)) {
      return false;
    }
    return true;
  }
}
