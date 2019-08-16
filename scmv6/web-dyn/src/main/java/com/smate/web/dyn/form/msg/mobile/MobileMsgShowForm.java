package com.smate.web.dyn.form.msg.mobile;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import com.smate.core.base.utils.model.Page;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.string.ServiceUtil;
import com.smate.web.dyn.model.msg.MsgShowInfo;

/**
 * 移动端-消息form
 * 
 * @author lhd
 *
 */
public class MobileMsgShowForm {

  private Long psnId;// 人员id
  private String des3PsnId; // 人员加密id
  private Page page = new Page();// 分页实体
  private Integer mobilePageNo;// 移动端页码

  /**
   * ： 0=系统消息、1=请求添加好友消息、2=成果认领、3=全文认领、4=成果推荐、5=好友推荐、 6=基金推荐、7=站内信、8=请求加入群组消息、9=邀请加入群组消息、10=群组动向 ,
   * 11=请求全文消息
   */
  private String msgType;
  // 消息状态：0=未读、1=已读、2=删除
  private Integer status;
  private List<MsgShowInfo> msgShowInfoList;// 消息展示列表
  private Long msgRelationId;// MsgRelation主键ID
  private String msgRelationIds;// MsgRelation主键IDs

  public Long getPsnId() {
    if (psnId == null && StringUtils.isNotBlank(des3PsnId)) {
      psnId = NumberUtils.toLong(ServiceUtil.decodeFromDes3(des3PsnId));
    } else {
      psnId = SecurityUtils.getCurrentUserId();
    }
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public String getDes3PsnId() {
    return des3PsnId;
  }

  public void setDes3PsnId(String des3PsnId) {
    this.des3PsnId = des3PsnId;
  }

  public Page getPage() {
    return page;
  }

  public void setPage(Page page) {
    this.page = page;
  }

  public String getMsgType() {
    return msgType;
  }

  public void setMsgType(String msgType) {
    this.msgType = msgType;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public List<MsgShowInfo> getMsgShowInfoList() {
    if (CollectionUtils.isEmpty(msgShowInfoList)) {
      msgShowInfoList = new ArrayList<MsgShowInfo>();
    }
    return msgShowInfoList;
  }

  public void setMsgShowInfoList(List<MsgShowInfo> msgShowInfoList) {
    this.msgShowInfoList = msgShowInfoList;
  }

  public String getMsgRelationIds() {
    return msgRelationIds;
  }

  public void setMsgRelationIds(String msgRelationIds) {
    this.msgRelationIds = msgRelationIds;
  }

  public Integer getMobilePageNo() {
    return mobilePageNo;
  }

  public void setMobilePageNo(Integer mobilePageNo) {
    this.mobilePageNo = mobilePageNo;
  }

  public Long getMsgRelationId() {
    return msgRelationId;
  }

  public void setMsgRelationId(Long msgRelationId) {
    this.msgRelationId = msgRelationId;
  }
}
