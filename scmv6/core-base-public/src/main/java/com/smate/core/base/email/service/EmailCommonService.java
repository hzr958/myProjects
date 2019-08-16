package com.smate.core.base.email.service;

import java.util.Map;

import com.smate.core.base.pub.model.PubSnsPublicPO;
import com.smate.core.base.pub.model.Publication;
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
  String PC_OR_MB_TOKEN = "scmdistributionaddr";// 区别pc和mobile的token 后面短地址会根据这个token区分要划分的地址

  public final static String DEFAULT_PUBFULLTEXT_IMAGE = "/resscmwebsns/images_v5/images2016/file_img.jpg";

  // 有全文的默认图片
  public final static String DEFAULT_PUBFULLTEXT_IMAGE1 = "/resscmwebsns/images_v5/images2016/file_img1.jpg";

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

  String getFrdUrl(Long toPsnId) throws PublicException;

  String getFullTextImg(String fileId, Long pubId) throws PublicException;

  String getImpactsUrl(Long psnId, String locale) throws PublicException;

  String getEmailUrl(Long psnId) throws PublicException;

  String getMyPubUrl(Long psnId, String pubId) throws PublicException;

  String getFavPubsUrl(Long psnId) throws PublicException;

  String getFriendsPage(Long psnId, Long friendPsnId) throws PublicException;

  String getUrl(Long psnId, String optUrl) throws Exception;

  Map<String, Object> getNewPubAuthor(PubSnsPublicPO pub, Integer num) throws PublicException;

  String getNewFullTextImg(Long pubId) throws PublicException;

}
