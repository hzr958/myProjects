package com.smate.center.batch.service.mail;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.sns.pub.Publication;
import com.smate.core.base.utils.model.security.Person;


public interface EmailCommonService {

  // 默认语言
  String DEFAULT_LOCALE = "zh_CN";

  String ZH_LOCALE = "zh_CN";

  String EN_LOCALE = "en_US";
  // 编码
  String ENCODING = "utf-8";

  String BASE_URL = "https://www.scholarmate.com/resscmwebsns/images_v5/";

  String NO_FULLTEXT_IMG = "images2016/file_img.jpg";

  String HTML_IMG = "html_img.jpg";
  String DOC_IMG = "doc_img.jpg";
  String txt_IMG = "txt_img.jpg";
  String ZIP_IMG = "zip_img.jpg";

  String getPsnNameByEmailLangage(Person person, String emailLangage) throws ServiceException;

  String getValidatedViewPsnUrl(String casUrl, Long tempPsnId, Long viewPsnId, String frdDomain, String languageVersion)
      throws UnsupportedEncodingException;

  String getPsnName(Person person) throws ServiceException;

  Integer isIsi(Integer sourceDbid) throws ServiceException;

  String getPubTitle(Publication pub, String emailLanguage) throws ServiceException;

  String getPubBrief(Publication pub, String emailLanguage) throws ServiceException;

  String getPubDetail(Long psnId, Long pubId, Integer nodeId) throws ServiceException;

  String getMsgFullTextUrl(Long psnId) throws ServiceException;

  Map<String, Object> getPubAuthor(Publication pub, Integer num) throws ServiceException;

  String getFrdUrl(Long psnId, Long toPsnId, String casUrl, Integer isAddFrd) throws ServiceException;

  String getFullTextImg(String fileId, Long pubId) throws ServiceException;

  String getImpactsUrl(Long psnId, String locale) throws ServiceException;

  String getEmailUrl(Long psnId) throws ServiceException;

  String getMyPubUrl(Long psnId, String pubId) throws ServiceException;

}
