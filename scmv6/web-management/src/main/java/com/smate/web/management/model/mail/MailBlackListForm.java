package com.smate.web.management.model.mail;

import java.util.Date;
import java.util.List;

import com.smate.core.base.utils.model.Page;

public class MailBlackListForm {
  private List<MailBlacklist> mailBlacklist;
  private MailBlacklist mailBlack;
  private Page<MailBlacklist> page;
  private Long id;
  private String email; // 拉黑的邮箱
  private int status;// 状态 0开启 1关闭
  private int type; // 0 邮箱类 1 域名类
  private Date updateDate; // 创建更新时间
  private String msg; // 描述

  public List<MailBlacklist> getMailBlacklist() {
    return mailBlacklist;
  }

  public void setMailBlacklist(List<MailBlacklist> mailBlacklist) {
    this.mailBlacklist = mailBlacklist;
  }

  public MailBlacklist getMailBlack() {
    if (mailBlack == null) {
      mailBlack = new MailBlacklist();
      mailBlack.setId(id);
      mailBlack.setEmail(email);
      mailBlack.setStatus(status);
      mailBlack.setType(type);
      mailBlack.setUpdateDate(updateDate);
      mailBlack.setMsg(msg);
    }
    return mailBlack;
  }

  public void setMailBlack(MailBlacklist mailBlack) {
    this.mailBlack = mailBlack;
  }

  public Page<MailBlacklist> getPage() {
    return page;
  }

  public void setPage(Page<MailBlacklist> page) {
    this.page = page;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public int getStatus() {
    return status;
  }

  public void setStatus(int status) {
    this.status = status;
  }

  public int getType() {
    return type;
  }

  public void setType(int type) {
    this.type = type;
  }

  public Date getUpdateDate() {
    return updateDate;
  }

  public void setUpdateDate(Date updateDate) {
    this.updateDate = updateDate;
  }

  public String getMsg() {
    return msg;
  }

  public void setMsg(String msg) {
    this.msg = msg;
  }


}
