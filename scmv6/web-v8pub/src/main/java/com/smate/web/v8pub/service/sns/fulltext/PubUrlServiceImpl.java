package com.smate.web.v8pub.service.sns.fulltext;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.psn.service.profile.PersonalService;
import com.smate.core.base.pub.po.dao.PubPdwhSnsRelationDAO;
import com.smate.core.base.pub.vo.PubDetailVO;
import com.smate.core.base.utils.common.URIencodeUtils;
import com.smate.core.base.utils.constant.ShortUrlConst;
import com.smate.core.base.utils.exception.SysServiceException;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.web.v8pub.dao.sns.ConstRefDbDao;
import com.smate.web.v8pub.dao.sns.InsRefDbDao;
import com.smate.web.v8pub.exception.ServiceException;
import com.smate.web.v8pub.service.pdwh.indexurl.PdwhPubIndexUrlService;
import com.smate.web.v8pub.vo.sns.ConstRefDb;
import com.smate.web.v8pub.vo.sns.InsRefDb;

@Service(value = "pubUrlService")
@Transactional(rollbackFor = Exception.class)
public class PubUrlServiceImpl implements PubUrlService {
  private Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private PersonalService personalService;
  @Value(value = "${cnkiInsideUrl}")
  private String cnkiInsideUrl;
  @Autowired
  private InsRefDbDao insRefDbDao;
  @Autowired
  private ConstRefDbDao constRefDbDao;
  @Autowired
  private PubPdwhSnsRelationDAO pubPdwhSnsRelationDAO;
  @Value("${domainscm}")
  private String domainscm;
  @Autowired
  private PdwhPubIndexUrlService pdwhPubIndexUrlService;

  private static final String META_URL = "metaUrl"; // 在IE、FF中设置meta
  private static final String LINK_REL = "linkRel"; // 针对chrome设置a标签的rel属性

  public void buildPubUrl(PubDetailVO pubDetailVO) {
    Long insId = personalService.getInsIdByPsnId(SecurityUtils.getCurrentUserId());
    String fulltextUrl = this.getFullTextUrl(pubDetailVO.getSrcFulltextUrl(), pubDetailVO.getTitle(),
        pubDetailVO.getSourceDbId(), pubDetailVO.getPubId(), insId);
    String sourceUrl = this.getFullTextUrl(pubDetailVO.getSourceUrl(), pubDetailVO.getTitle(),
        pubDetailVO.getSourceDbId(), pubDetailVO.getPubId(), insId);
    pubDetailVO.setInsFulltextUrl(fulltextUrl);
    pubDetailVO.setInsSourceUrl(sourceUrl);
  }

  private String getFullTextUrl(String url, String title, Integer sourceDbId, Long pubId, Long insId) {
    try {
      if (new Integer(4).equals(sourceDbId)) {
        if (StringUtils.isNotBlank(title)) {
          url = cnkiInsideUrl.replace("@@title@@", URIencodeUtils.encodeURIComponent(title));
        }
      }
      if (insId != null) {
        gererateTmpUrl(insId, sourceDbId, url);
      }
      return url;
    } catch (Exception e) {
      // 吃掉这个异常
      logger.error("=======pubId=" + pubId, e);
      return "";
      // throw new ServiceException(e);
    }
  }

  private String getFullTextUrl(PubDetailVO pubDetailVO, Long insId) {
    try {
      String url = pubDetailVO.getSrcFulltextUrl();
      if (4 == pubDetailVO.getSourceDbId()) {
        if (StringUtils.isNotBlank(pubDetailVO.getTitle())) {
          url = cnkiInsideUrl.replace("@@title@@", URIencodeUtils.encodeURIComponent(pubDetailVO.getTitle()));
        }
      }
      if (insId != null) {
        gererateTmpUrl(insId, pubDetailVO.getSourceDbId(), url);
      }
      return url;
    } catch (Exception e) {
      logger.error("=======getFullText=null,pubId=", pubDetailVO.getPubId());
      throw new ServiceException(e);
    }
  }

  /**
   * 返回当前查看类型可能需要使用的url.如果没有配置校外访问url，或校内url与原url域名不匹配，则只返回原url供打开.
   * 
   * @param insRefDb
   * @param viewType
   * @return
   * @throws SysServiceException
   * @throws DaoException
   */
  private void gererateTmpUrl(Long insId, Integer sourceDbId, String url) throws SysServiceException {
    url = url.replace("apps.isiknowledge.com/", "apps.webofknowledge.com/");
    InsRefDb insRefDb = insRefDbDao.getURL(insId, Long.valueOf(sourceDbId));
    ConstRefDb constRefDb = constRefDbDao.getConstRefDbById(Long.valueOf(sourceDbId));
    if (insRefDb != null) {

      if (!"".equals(insRefDb.getActionUrl()) && StringUtils.isNotBlank(insRefDb.getFulltextUrlInside())
          && url.indexOf(insRefDb.getActionUrlInside()) >= 0) {
        if (StringUtils.isNotBlank(url) && StringUtils.isNotBlank(insRefDb.getActionUrl())
            && StringUtils.isNotBlank(insRefDb.getActionUrlInside())) {
          url = url.replace(insRefDb.getActionUrlInside(), insRefDb.getActionUrl());
        }
      }
    }
  }

  @Override
  public void builPubShortUrl(PubDetailVO pubDetailVO) {
    Long snsPubId = pubDetailVO.getPubId();
    pubDetailVO.setSeoIndexUrl(pubDetailVO.getPubIndexUrl());
    if (snsPubId != null) {// 是否有基准库的
      Long pdwhId = pubPdwhSnsRelationDAO.getPdwhPubIdBySnsPubId(snsPubId);
      if (pdwhId != null) {
        String pubIndex = pdwhPubIndexUrlService.getIndexUrlByPubId(pdwhId);
        if (StringUtils.isNotBlank(pubIndex)) {
          String pubShortIndex = this.domainscm + "/" + ShortUrlConst.S_TYPE + "/" + pubIndex;
          pubDetailVO.setSeoIndexUrl(pubShortIndex);
        }
      }
    }
  }

}
