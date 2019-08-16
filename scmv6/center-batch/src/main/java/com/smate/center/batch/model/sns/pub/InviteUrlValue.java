package com.smate.center.batch.model.sns.pub;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


/**
 * 邀请链接信息类
 * 
 * @author zzx
 *
 */
@Entity
@Table(name = "INVITE_URL_VALUE")
public class InviteUrlValue implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 9075801971420391436L;
  /**
   * 
   */
  // 主键
  private Long id;
  // 引用ID（例如：群组ID，标识当前记录记录的是哪一个群组邀请的参数值）
  private Long reference_id;
  // 参数值，以json的形式存贮
  private String value;

  public InviteUrlValue() {
    super();
  }

  public InviteUrlValue(Long id, Long referenceId, String value) {
    super();
    this.id = id;
    reference_id = referenceId;
    this.value = value;
  }

  /**
   * @return the id
   */
  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_INVITE_URL_VALUE", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getId() {
    return id;
  }

  /**
   * @param id the id to set
   */
  public void setId(Long id) {
    this.id = id;
  }

  /**
   * @return the reference_id
   */
  @Column(name = "REFERENCE_ID")
  public Long getReference_id() {
    return reference_id;
  }

  /**
   * @param referenceId the reference_id to set
   */
  public void setReference_id(Long referenceId) {
    reference_id = referenceId;
  }

  /**
   * @return the value
   */
  @Column(name = "VALUE")
  public String getValue() {
    return value;
  }

  /**
   * @param value the value to set
   */
  public void setValue(String value) {
    this.value = value;
  }

}
