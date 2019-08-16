package com.smate.web.v8pub.vo.sns;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import com.smate.web.v8pub.enums.PubHandlerStatusEnum;

/**
 * 成果查重结果返回对象
 * 
 * @author YJ
 *
 *         2019年7月18日
 */
public class PubDupResultVO {

  private String msg; // 查重的结果，msgList中随意一条
  private Long dupPubId;// 查重的结果，msg转化而来的结果
  private String msgList;// 查重的结果，查重的所有结果
  private List<Long> dupIdList;// 对msgList进行拆分的结果
  private String status;// 查重的状态位


  public String getMsg() {
    return msg;
  }

  public void setMsg(String msg) {
    this.msg = msg;
  }

  public String getMsgList() {
    if (PubHandlerStatusEnum.SUCCESS.getValue().equals(status)) {
      return null;
    }
    return msgList;
  }

  public void setMsgList(String msgList) {
    this.msgList = msgList;
  }

  public List<Long> getDupIdList() {
    // 从msgList中构建出来
    if (PubHandlerStatusEnum.SUCCESS.getValue().equals(status)) {
      return null;
    }
    if (StringUtils.isNotBlank(msgList)) {
      dupIdList = new ArrayList<>();
      String[] msgs = msgList.split(",");
      if (msgs != null && msgs.length > 0) {
        for (String string : msgs) {
          dupIdList.add(NumberUtils.toLong(string));
        }
      }
    }
    return null;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public Long getDupPubId() {
    if (!PubHandlerStatusEnum.SUCCESS.getValue().equals(status)) {
      return null;
    }
    return NumberUtils.toLong(msg);
  }

}
