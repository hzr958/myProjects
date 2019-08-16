package com.smate.web.v8pub.po.pdwh;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 基准库成果作者邮件信息表
 * 
 * @author YJ
 *
 *         2019年1月8日
 */
@Entity
@Table(name = "V_PUB_PDWH_MEMBER_EMAIL")
public class PdwhMemberEmailPO implements Serializable {

  private static final long serialVersionUID = -9013514439893930048L;

  @Id
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_PDWH_MEMBER_EMAIL_ID", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  @Column(name = "ID")
  private Long id; // 主键id

  @Column(name = "PDWH_PUB_ID")
  private Long pdwhPubId; // 基准库Id

  @Column(name = "EMAIL")
  private String email; // 邮件地址


  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getPdwhPubId() {
    return pdwhPubId;
  }

  public void setPdwhPubId(Long pdwhPubId) {
    this.pdwhPubId = pdwhPubId;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }


}
