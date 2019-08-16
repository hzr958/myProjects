package com.smate.center.batch.service.mail;

import com.smate.center.batch.exception.pub.ServiceException;

public interface EtemplateDealUrlMethod {

  // 是否为加好友类型
  public final Integer isAddFrd = 1;
  public final Integer isNotAddFrd = 0;

  // 个人主页成果锚点
  public final String pub = "publication_box";
  // 个人主页工作锚点
  public final String work = "work_box";

  String getFrdUrl(String email2log, Long psnId, Long toPsnId, String casUrl, Integer isAddFrd) throws ServiceException;

  String getPubDetail(String email2log, Long psnId, Long pubId, Integer nodeId, String casUrl) throws ServiceException;

  public String getFrdSrc(String email2log, Long psnId, Long frdPsnId, String casUrl, String src)
      throws ServiceException;

  String getViewUrl(String email2log, String viewStr, Long psnId, String casUrl, Integer menuId)
      throws ServiceException;

  String getScreenMailUrl() throws ServiceException;

  String getMailViewUrl(Long mailId) throws ServiceException;

  String getPsnDynUrl(String email2log, Long psnId, String casUrl) throws ServiceException;

  String getPsnUrl(String email2log, Long psnId, String casUrl) throws ServiceException;
}
