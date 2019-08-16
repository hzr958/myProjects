package com.smate.center.open.model.prj;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 第三方项目成员中间表
 * 
 * @author LXZ SEQ_V_OPEN_PROJECT_MEMBER
 * @since 6.0.1
 * @version 6.0.1
 * @return
 */
@Entity
@Table(name = "V_OPEN_PROJECT_MEMBER")
public class OpenPrjMember implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -5151481990408761350L;
  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_V_OPEN_PROJECT_MEMBER", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  private Long id;// ID
  @Column(name = "open_prj_id")
  private Long openPrjId;// OpenProject.id
  @Column(name = "member_name")
  private String name;// 成员名称
  @Column(name = "email")
  private String email;// 邮箱
  @Column(name = "ins_name")
  private String insName;// 单位名称
  @Column(name = "seq_no")
  private Integer seqNo;
  @Column(name = "NOTIFY_AUTHOR") // 项目负责人 0/1
  private Integer notifyAuthor;

  public OpenPrjMember(String name, String email, String insName, Integer seqNo, Integer notifyAuthor) {
    super();
    this.name = name;
    this.email = email;
    this.insName = insName;
    this.seqNo = seqNo;
    this.notifyAuthor = notifyAuthor;
  }

  public OpenPrjMember() {
    super();
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getOpenPrjId() {
    return openPrjId;
  }

  public void setOpenPrjId(Long openPrjId) {
    this.openPrjId = openPrjId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getInsName() {
    return insName;
  }

  public void setInsName(String insName) {
    this.insName = insName;
  }

  public Integer getSeqNo() {
    return seqNo;
  }

  public void setSeqNo(Integer seqNo) {
    this.seqNo = seqNo;
  }

  public Integer getNotifyAuthor() {
    return notifyAuthor;
  }

  public void setNotifyAuthor(Integer notifyAuthor) {
    this.notifyAuthor = notifyAuthor;
  }


}
