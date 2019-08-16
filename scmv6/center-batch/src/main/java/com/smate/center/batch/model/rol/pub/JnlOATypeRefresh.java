package com.smate.center.batch.model.rol.pub;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 期刊-开放存储类型刷新表.
 * 
 * @author xys
 * 
 */
@Entity
@Table(name = "JNL_OA_TYPE_REFRESH")
public class JnlOATypeRefresh implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 8252503853197801903L;

  @Id
  @Column(name = "JID")
  private Long jid;// 期刊id
  @Column(name = "STATUS")
  private Integer status = 0;// 0-待刷新，1-刷新成功，99-刷新失败

  public JnlOATypeRefresh() {
    super();
  }

  public JnlOATypeRefresh(Long jid) {
    super();
    this.jid = jid;
  }

  public Long getJid() {
    return jid;
  }

  public void setJid(Long jid) {
    this.jid = jid;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

}
