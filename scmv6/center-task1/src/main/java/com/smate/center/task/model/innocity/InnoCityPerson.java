package com.smate.center.task.model.innocity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;


/**
 * 人员主表.
 * 
 * @author lqh
 * 
 */
@Entity
@Table(name = "PERSON")
public class InnoCityPerson implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -5428688672834684956L;
  @Id
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_PERSON", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  @Column(name = "PSN_ID")
  private Long psnId;
  @Column(name = "FIRST_NAME")
  private String firstName;
  @Column(name = "LAST_NAME")
  private String lastName;
  @Column(name = "NAME")
  private String name;// 中文名称
  @Column(name = "ENAME")
  private String ename;// lastName+" "+firstName
  @Column(name = "EMAIL")
  private String email;// 邮件
  @Column(name = "POSITION")
  private String position;// 职称
  @Column(name = "INS_NAME")
  private String insName;// 机构名称
  @Column(name = "AVATARS")
  private String avatars;// 头像、机构logo图片地址(个人注册用本地默认头像地址，sns同步后用sns上传的地址)
  @Column(name = "SCM_TITOLO")
  private String scmTitolo;// 科研之友设置的头衔
  @Column(name = "SCM_HTTP")
  private String scmHttp;// 科研之友主页网址
  @Column(name = "SCM_PSN_ID")
  private Long scmPsnId;// 关联科研之友PSN_ID
  @Column(name = "CREATE_DATE")
  private Date createDate;//
  @Column(name = "SEQ_NO")
  private Integer seqNo;// 排序字段
  @Column(name = "IS_SUPER_ADMIN")
  private Integer isSuperAdmin;// 1-超级管理员
  @Column(name = "MOBILEPHONE_NUM")
  private String mobilephoneNum; // 人员手机号码
  @Column(name = "TEMP_FLAG")
  private int tempFlag = 0; // 临时用户标识
  @Column(name = "INC_PSN_ID")
  private Long incPsnId; // 关联创新城psnId
  @Transient
  private String classifys;// 人员领域分类
  @Transient
  private String classifyNames;// 人员领域分类名称
  @Transient
  private String des3Id;
  @Transient
  private String psnShowName;// 人员姓名(页面显示用).
  @Transient
  private String psnShowTitolo;// 人员职称(页面显示用).
  @Transient
  private String password;
  @Transient
  private String department;
  @Transient
  private String fromYear;
  @Transient
  private String fromYonth;
  @Transient
  private int isAward;
  @Transient
  private Long connectId; // 专家活动展会对接
  @Transient
  private Long messageCount;
  @Transient
  private Long collectStatus; // 该成果的征集状态（用于展会征集）

  public InnoCityPerson() {
    super();
  }

  public InnoCityPerson(Long psnId, String name, String email, String mobilephoneNum) {
    super();
    this.psnId = psnId;
    this.name = name;
    this.email = email;
    this.mobilephoneNum = mobilephoneNum;
  }

  public InnoCityPerson(Long psnId, String firstName, String lastName, String name, String ename, Long scmPsnId,
      String scmHttp, Integer isSuperAdmin) {
    super();
    this.psnId = psnId;
    this.firstName = firstName;
    this.lastName = lastName;
    this.name = name;
    this.ename = ename;
    this.scmPsnId = scmPsnId;
    this.scmHttp = scmHttp;
    this.isSuperAdmin = isSuperAdmin;
  }

  public InnoCityPerson(Long psnId, String firstName, String lastName, String name, String ename) {
    super();
    this.psnId = psnId;
    this.firstName = firstName;
    this.lastName = lastName;
    this.name = name;
    this.ename = ename;
  }

  public InnoCityPerson(String firstName, String lastName, String name, String ename, Integer isSuperAdmin,
      String mobilephoneNum) {
    super();
    this.firstName = firstName;
    this.lastName = lastName;
    this.name = name;
    this.ename = ename;
    this.isSuperAdmin = isSuperAdmin;
    this.mobilephoneNum = mobilephoneNum;
  }

  public InnoCityPerson(Long psnId, String firstName, String lastName, String name, String ename, String email) {
    super();
    this.psnId = psnId;
    this.firstName = firstName;
    this.lastName = lastName;
    this.name = name;
    this.ename = firstName + " " + lastName;
    this.email = email;
  }

  public InnoCityPerson(Long psnId, Long scmPsnId, String firstName, String lastName, String name, String ename,
      String avatars, String scmTitolo, String position, String insName) {
    super();
    this.psnId = psnId;
    this.scmPsnId = scmPsnId;
    this.firstName = firstName;
    this.lastName = lastName;
    this.name = name;
    this.ename = ename;
    this.avatars = avatars;
    this.scmTitolo = scmTitolo;
    this.position = position;
    this.insName = insName;
  }

  public InnoCityPerson(Long psnId, Long scmPsnId, String firstName, String lastName, String name, String ename,
      String avatars, String scmTitolo, String position, String insName, String scmHttp) {
    super();
    this.psnId = psnId;
    this.scmPsnId = scmPsnId;
    this.firstName = firstName;
    this.lastName = lastName;
    this.name = name;
    this.ename = ename;
    this.avatars = avatars;
    this.scmTitolo = scmTitolo;
    this.position = position;
    this.insName = insName;
    this.scmHttp = scmHttp;
  }

  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public String getFirstName() {
    return firstName;
  }


  public String getLastName() {
    return lastName;
  }


  public String getName() {
    return name;
  }

  public String getEname() {
    return ename;
  }



  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public void setName(String name) {
    this.name = name;
  }


  public void setEname(String ename) {
    this.ename = ename;
  }


  public String getEmail() {
    return email;
  }


  public void setEmail(String email) {
    this.email = email;
  }

  public String getPosition() {
    return position;
  }


  public void setPosition(String position) {
    this.position = position;
  }


  public String getAvatars() {
    return avatars;
  }


  public void setAvatars(String avatars) {
    this.avatars = avatars;
  }


  public String getInsName() {
    return insName;
  }


  public void setInsName(String insName) {
    this.insName = insName;
  }


  public String getScmTitolo() {
    return scmTitolo;
  }


  public void setScmTitolo(String scmTitolo) {
    this.scmTitolo = scmTitolo;
  }


  public String getScmHttp() {
    return scmHttp;
  }


  public void setScmHttp(String scmHttp) {
    this.scmHttp = scmHttp;
  }


  public Long getScmPsnId() {
    return scmPsnId;
  }


  public void setScmPsnId(Long scmPsnId) {
    this.scmPsnId = scmPsnId;
  }


  public Date getCreateDate() {
    return createDate;
  }


  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

  public String getPsnShowName() {
    return psnShowName;
  }


  public void setPsnShowName(String psnShowName) {
    this.psnShowName = psnShowName;
  }

  public String getPsnShowTitolo() {
    return psnShowTitolo;
  }

  public void setPsnShowTitolo(String psnShowTitolo) {
    this.psnShowTitolo = psnShowTitolo;
  }

  public String getClassifys() {
    return classifys;
  }

  public void setClassifys(String classifys) {
    this.classifys = classifys;
  }


  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getDepartment() {
    return department;
  }

  public void setDepartment(String department) {
    this.department = department;
  }

  public String getFromYear() {
    return fromYear;
  }

  public void setFromYear(String fromYear) {
    this.fromYear = fromYear;
  }

  public String getFromYonth() {
    return fromYonth;
  }

  public void setFromYonth(String fromYonth) {
    this.fromYonth = fromYonth;
  }

  public Integer getSeqNo() {
    return seqNo;
  }

  public Integer getIsSuperAdmin() {
    return isSuperAdmin;
  }

  public void setIsSuperAdmin(Integer isSuperAdmin) {
    this.isSuperAdmin = isSuperAdmin;
  }

  public void setSeqNo(Integer seqNo) {
    this.seqNo = seqNo;
  }


  public String getClassifyNames() {
    return classifyNames;
  }

  public void setClassifyNames(String classifyNames) {
    this.classifyNames = classifyNames;
  }

  public String getMobilephoneNum() {
    return mobilephoneNum;
  }

  public void setMobilephoneNum(String mobilephoneNum) {
    this.mobilephoneNum = mobilephoneNum;
  }

  public int getTempFlag() {
    if (Integer.valueOf(tempFlag) == null) {
      tempFlag = 0;
    }
    return tempFlag;
  }

  public void setTempFlag(int tempFlag) {
    this.tempFlag = tempFlag;
  }

  public int getIsAward() {
    return isAward;
  }

  public void setIsAward(int isAward) {
    this.isAward = isAward;
  }

  public Long getConnectId() {
    return connectId;
  }

  public void setConnectId(Long connectId) {
    this.connectId = connectId;
  }

  public Long getIncPsnId() {
    return incPsnId;
  }

  public void setIncPsnId(Long incPsnId) {
    this.incPsnId = incPsnId;
  }

  public Long getCollectStatus() {
    return collectStatus;
  }

  public void setCollectStatus(Long collectStatus) {
    this.collectStatus = collectStatus;
  }

  public Long getMessageCount() {
    return messageCount;
  }

  public void setMessageCount(Long messageCount) {
    this.messageCount = messageCount;
  }

}
