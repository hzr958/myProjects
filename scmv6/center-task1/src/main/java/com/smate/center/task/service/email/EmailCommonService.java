package com.smate.center.task.service.email;

import java.util.Map;

import com.smate.center.task.model.sns.quartz.Publication;
import com.smate.core.base.utils.exception.PublicException;
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

  String getPsnNameByEmailLangage(Person person, String emailLangage) throws PublicException;

  String getValidatedViewPsnUrl(String casUrl, Long tempPsnId, Long viewPsnId, String frdDomain, String languageVersion)
      throws PublicException;

  String getPsnName(Person person) throws PublicException;

  Integer isIsi(Integer sourceDbid) throws PublicException;

  String getPubTitle(Publication pub, String emailLanguage) throws PublicException;

  String getPubBrief(Publication pub, String emailLanguage) throws PublicException;

  String getPubDetail(Long psnId, Long pubId, Integer nodeId) throws PublicException;

  String getMsgFullTextUrl(Long psnId) throws PublicException;

  Map<String, Object> getPubAuthor(Publication pub, Integer num) throws PublicException;

  String getFrdUrl(Long psnId, Long toPsnId, String casUrl, Integer isAddFrd) throws PublicException;

  String getFullTextImg(String fileId, Long pubId) throws PublicException;

  String getImpactsUrl(Long psnId, String locale) throws PublicException;

  String getEmailUrl(Long psnId) throws PublicException;

  String getMyPubUrl(Long psnId, String pubId) throws PublicException;

  String getFavPubsUrl(Long psnId) throws PublicException;

  String getFriendsPage(Long psnId, Long friendPsnId) throws PublicException;

  String getUrl(Long psnId, String optUrl) throws Exception;

}
