package com.smate.center.batch.model.sns.nsfc;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "tmp_dest")
public class TmpDest implements Serializable {
  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_TMP_TASK_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  private Long Id;
  @Column(name = "app_id")
  private String appId;
  @Column(name = "app_dir")
  private String appdir;
  @Column(name = "keyword")
  private String kws;

  public String getAppId() {
    return appId;
  }

  public void setAppId(String appId) {
    this.appId = appId;
  }

  public String getAppdir() {
    return appdir;
  }

  public void setAppdir(String appdir) {
    this.appdir = appdir;
  }

  public String getKws() {
    return kws;
  }

  public void setKws(String kws) {
    this.kws = kws;
  }

  public TmpDest() {
    super();
    // TODO Auto-generated constructor stub
  }

  public TmpDest(String appId, String appdir, String kws) {
    super();
    this.appId = appId;
    this.appdir = appdir;
    this.kws = kws;
  }

  public Long getId() {
    return Id;
  }

  public void setId(Long id) {
    Id = id;
  }

  public TmpDest(Long id, String appId, String appdir, String kws) {
    super();
    Id = id;
    this.appId = appId;
    this.appdir = appdir;
    this.kws = kws;
  }

}

