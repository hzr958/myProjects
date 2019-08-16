package com.smate.web.psn.model.resume;



import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 用户联系信息查看配置表.
 * 
 * @author liqinghua
 * 
 */
@Entity
@Table(name = "PSN_CONTACT_CONFIG")
public class ContactConfig implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 5454223242877847043L;

  private ContactConfigKey id;

  // email是否可看0/1
  private Integer email = 0;
  // 手机是否可看0/1
  private Integer tel = 0;
  // msn是否可看0/1
  private Integer msnNo = 0;
  // qq是否可看0/1
  private Integer qqNo = 0;
  // mobile是否可看0/1
  private Integer mobile = 0;
  // skype是否可看0/1
  private Integer skype = 0;

  public ContactConfig() {
    super();
  }

  public ContactConfig(Long tmpId, Long psnId, Integer email, Integer tel, Integer msnNo, Integer qqNo, Integer mobile,
      Integer skype) {
    super();
    this.id = new ContactConfigKey(tmpId, psnId);
    this.email = email;
    this.tel = tel;
    this.msnNo = msnNo;
    this.qqNo = qqNo;
    this.mobile = mobile;
    this.skype = skype;
  }

  public void setKey(Long tmpId, Long psnId) {
    this.id = new ContactConfigKey(tmpId, psnId);
  }

  @EmbeddedId
  public ContactConfigKey getId() {
    return id;
  }

  @Column(name = "TEL")
  public Integer getTel() {
    return tel;
  }

  @Column(name = "MSN_NO")
  public Integer getMsnNo() {
    return msnNo;
  }

  @Column(name = "QQ_NO")
  public Integer getQqNo() {
    return qqNo;
  }

  @Column(name = "MOBILE")
  public Integer getMobile() {
    return mobile;
  }

  public void setId(ContactConfigKey id) {
    this.id = id;
  }

  public void setTel(Integer tel) {
    this.tel = tel;
  }

  public void setMsnNo(Integer msnNo) {
    this.msnNo = msnNo;
  }

  public void setQqNo(Integer qqNo) {
    this.qqNo = qqNo;
  }

  @Column(name = "EMAIL")
  public Integer getEmail() {
    return email;
  }

  public void setEmail(Integer email) {
    this.email = email;
  }

  public void setMobile(Integer mobile) {
    this.mobile = mobile;
  }

  @Column(name = "SKYPE")
  public Integer getSkype() {
    return skype;
  }

  public void setSkype(Integer skype) {
    this.skype = skype;
  }

}
