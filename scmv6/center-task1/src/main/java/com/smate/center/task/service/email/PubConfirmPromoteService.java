package com.smate.center.task.service.email;

import java.util.List;

import com.smate.center.task.exception.ServiceException;
import com.smate.center.task.model.email.PubConfirmPromotePsn;


public interface PubConfirmPromoteService {

  // 指派过的人员标识
  final Integer IS_SEND = 1;
  // 未指派的人员标识
  final Integer IS_NOT_SEND = 0;
  // 重新发送
  final Integer RE_SEND = 3;
  String HTML_IMG = "html_img.jpg";
  String DOC_IMG = "doc_img.jpg";
  String txt_IMG = "txt_img.jpg";
  String ZIP_IMG = "zip_img.jpg";

  // 模板
  final String MAIL_TEMPLATE = "new_thesis_update";

  public List<PubConfirmPromotePsn> getPubConfirmPromotePsn(Integer size) throws ServiceException;

  void dealStatus(PubConfirmPromotePsn pcpp) throws ServiceException;

  void savePubConfirmPromotePsn(PubConfirmPromotePsn pcpp, Integer status) throws ServiceException;

  Long countNotSend() throws ServiceException;

}
