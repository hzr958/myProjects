package com.smate.center.batch.model.rol.pub;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 期刊-开放存储类型.
 * 
 * @author xys
 * 
 */
@Entity
@Table(name = "JNL_OA_TYPE")
public class JnlOAType implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 945626899468073119L;

  @Id
  @Column(name = "JID")
  private Long jid;// 期刊id
  @Column(name = "OA_TYPE_ID")
  private Long oaTypeId;// 开放存储类型id
  @Column(name = "ROMEO_COLOUR_ZH")
  private String romeoColourZh;// 开放存储类型（中文）
  @Column(name = "ROMEO_COLOUR_EN")
  private String romeoColourEn;// 开放存储类型（英文）

  public JnlOAType() {
    super();
  }

  public JnlOAType(Long jid) {
    super();
    this.jid = jid;
  }

  public Long getJid() {
    return jid;
  }

  public void setJid(Long jid) {
    this.jid = jid;
  }

  public Long getOaTypeId() {
    return oaTypeId;
  }

  public void setOaTypeId(Long oaTypeId) {
    this.oaTypeId = oaTypeId;
  }

  public String getRomeoColourZh() {
    return romeoColourZh;
  }

  public void setRomeoColourZh(String romeoColourZh) {
    this.romeoColourZh = romeoColourZh;
  }

  public String getRomeoColourEn() {
    return romeoColourEn;
  }

  public void setRomeoColourEn(String romeoColourEn) {
    this.romeoColourEn = romeoColourEn;
  }

}
