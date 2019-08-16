package com.smate.web.management.model.mail;

import java.util.List;

import com.smate.core.base.utils.model.Page;

public class MailWhiteListForm {
  private List<MailWhitelist> mailWhitelist;
  private MailWhitelist mailWhite;
  private Page<MailWhitelist> page;
  private Long id;
  private String email; // 邮箱
  private int status;// 状态 0开启 1关闭

  public List<MailWhitelist> getMailWhitelist() {
    return mailWhitelist;
  }

  public void setMailWhitelist(List<MailWhitelist> mailWhitelist) {
    this.mailWhitelist = mailWhitelist;
  }

  public MailWhitelist getMailWhite() {
    if (mailWhite == null) {
      mailWhite = new MailWhitelist();
      mailWhite.setId(id);
      mailWhite.setEmail(email);
      mailWhite.setStatus(status);
    }
    return mailWhite;
  }

  public void setMailWhite(MailWhitelist mailWhite) {
    this.mailWhite = mailWhite;
  }

  public Page<MailWhitelist> getPage() {
    return page;
  }

  public void setPage(Page<MailWhitelist> page) {
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

}
