package com.smate.web.dyn.form.dynamic.group;

import java.util.Date;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.smate.core.base.utils.security.Des3Utils;

/**
 * 群组动态显示类-只用于页面展示数据
 * 
 * @author zzx
 *
 */
public class GroupDynShowInfo {
  // 最新一条评论内容
  private String commentContent;
  private Date commentDate;
  // person
  private String firstName;// 用户名
  private String lastName;// 用户姓氏
  private String name;// 用户中文明
  private String ename;// 用户英文名
  private String avatars;// 头像地址
  // groupDynamicContent
  private String dynContent;
  // GroupDynamicMsg
  private Long dynId;// 动态id
  private String des3DynId;
  private Long groupId;// 群组id
  private Long producer;// 创建人ID
  private String dynType;// 动态类型
  private String dynTmp;// 动态模版
  private Date createDate;// 创建时间
  private Date updateDate;// 最后更新时间
  private Integer status;// 状态 正常0， 删除99
  private Integer relDealStatus;// 关系处理状态 0 未处理 1 已经处理
  private Long resId;// 资源Id
  private String des3ResId;
  private String resType;// 资源类型
  private Long sameFlag;// 动态来源（父级动态id）
  private String extend;// 预留字段
  // GroupDynamicStatistic
  private Integer awardCount;// 赞次数
  private Integer commentCount;// 评论次数
  private Integer shareCount;// 分享次数
  private Integer awardstatus;// 是否已经赞过 1：可以点赞 0：已经赞了
  // 自定义属性
  private String commentDateForShow;// 携带的评论的时间
  private String dynDateForShow;// 动态的创建时间

  private Integer isCanDel;// 是否可以被删除：1=可以；0=不可以
  private Boolean hasCollenciton = false;// 已经收藏 ，默认是 没有

  private String resFullTextFileId; // 资源的全文文件id
  private String resFullTextImage; // 资源的全文图片
  private Integer resPremission; // 资源下载权限
  private Map<String, Object> jsonDynInfo; // json格式的群组动态信息
  private int resNotExists = 0; // 资源是否还存在， 1（不存在），0（存在）

  public Long getDynId() {
    return dynId;
  }

  public void setDynId(Long dynId) {
    this.dynId = dynId;
  }

  public Long getGroupId() {
    return groupId;
  }

  public void setGroupId(Long groupId) {
    this.groupId = groupId;
  }

  public Long getProducer() {
    return producer;
  }

  public void setProducer(Long producer) {
    this.producer = producer;
  }

  public String getDynType() {
    return dynType;
  }

  public void setDynType(String dynType) {
    this.dynType = dynType;
  }

  public String getDynTmp() {
    return dynTmp;
  }

  public void setDynTmp(String dynTmp) {
    this.dynTmp = dynTmp;
  }

  public Date getCreateDate() {
    return createDate;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

  public Date getUpdateDate() {
    return updateDate;
  }

  public void setUpdateDate(Date updateDate) {
    this.updateDate = updateDate;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public Integer getRelDealStatus() {
    return relDealStatus;
  }

  public void setRelDealStatus(Integer relDealStatus) {
    this.relDealStatus = relDealStatus;
  }

  public Long getResId() {
    return resId;
  }

  public void setResId(Long resId) {
    this.resId = resId;
  }

  public String getResType() {
    return resType;
  }

  public void setResType(String resType) {
    this.resType = resType;
  }

  public Long getSameFlag() {
    return sameFlag;
  }

  public void setSameFlag(Long sameFlag) {
    this.sameFlag = sameFlag;
  }

  public String getExtend() {
    return extend;
  }

  public void setExtend(String extend) {
    this.extend = extend;
  }

  public Integer getAwardCount() {
    return awardCount;
  }

  public void setAwardCount(Integer awardCount) {
    this.awardCount = awardCount;
  }

  public Integer getCommentCount() {
    return commentCount;
  }

  public void setCommentCount(Integer commentCount) {
    this.commentCount = commentCount;
  }

  public Integer getShareCount() {
    return shareCount;
  }

  public void setShareCount(Integer shareCount) {
    this.shareCount = shareCount;
  }

  public String getDynContent() {
    return dynContent;
  }

  public void setDynContent(String dynContent) {
    this.dynContent = dynContent;
  }

  public String getCommentContent() {
    return commentContent;
  }

  public void setCommentContent(String commentContent) {
    this.commentContent = commentContent;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getEname() {
    return ename;
  }

  public void setEname(String ename) {
    this.ename = ename;
  }

  public String getAvatars() {
    return avatars;
  }

  public void setAvatars(String avatars) {
    this.avatars = avatars;
  }

  public Date getCommentDate() {
    return commentDate;
  }

  public void setCommentDate(Date commentDate) {
    this.commentDate = commentDate;
  }

  public String getCommentDateForShow() {
    return commentDateForShow;
  }

  public void setCommentDateForShow(String commentDateForShow) {
    this.commentDateForShow = commentDateForShow;
  }

  public String getDynDateForShow() {
    return dynDateForShow;
  }

  public void setDynDateForShow(String dynDateForShow) {
    this.dynDateForShow = dynDateForShow;
  }

  public Integer getAwardstatus() {
    return awardstatus;
  }

  public void setAwardstatus(Integer awardstatus) {
    this.awardstatus = awardstatus;
  }

  public String getDes3ResId() {
    if (StringUtils.isBlank(des3ResId) && resId != null) {
      return Des3Utils.encodeToDes3(resId.toString());
    }
    return des3ResId;
  }

  public void setDes3ResId(String des3ResId) {
    this.des3ResId = des3ResId;
  }

  public String getDes3DynId() {
    if (StringUtils.isBlank(des3DynId)) {
      this.des3DynId = Des3Utils.encodeToDes3(this.dynId.toString());
    }
    return des3DynId;
  }

  public void setDes3DynId(String des3DynId) {
    this.des3DynId = des3DynId;
  }

  public Integer getIsCanDel() {
    return isCanDel;
  }

  public void setIsCanDel(Integer isCanDel) {
    this.isCanDel = isCanDel;
  }

  public Boolean getHasCollenciton() {
    return hasCollenciton;
  }

  public void setHasCollenciton(Boolean hasCollenciton) {
    this.hasCollenciton = hasCollenciton;
  }

  public String getResFullTextFileId() {
    return resFullTextFileId;
  }

  public void setResFullTextFileId(String resFullTextFileId) {
    this.resFullTextFileId = resFullTextFileId;
  }

  public String getResFullTextImage() {
    return resFullTextImage;
  }

  public void setResFullTextImage(String resFullTextImage) {
    this.resFullTextImage = resFullTextImage;
  }

  public Integer getResPremission() {
    return resPremission;
  }

  public void setResPremission(Integer resPremission) {
    this.resPremission = resPremission;
  }

  public Map<String, Object> getJsonDynInfo() {
    return jsonDynInfo;
  }

  public void setJsonDynInfo(Map<String, Object> jsonDynInfo) {
    this.jsonDynInfo = jsonDynInfo;
  }

  public int getResNotExists() {
    return resNotExists;
  }

  public void setResNotExists(Integer resNotExists) {
    this.resNotExists = resNotExists;
  }



}
