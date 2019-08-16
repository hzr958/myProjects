package com.smate.web.v8pub.service.pubdetailquery;

import java.text.SimpleDateFormat;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.exception.ServiceException;
import com.smate.core.base.pub.dao.PubIndexUrlDao;
import com.smate.core.base.pub.model.PubIndexUrl;
import com.smate.core.base.pub.util.PubParamUtils;
import com.smate.core.base.pub.vo.PubDetailVO;
import com.smate.core.base.utils.constant.ShortUrlConst;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.web.v8pub.dom.sns.PubSnsDetailDOM;
import com.smate.web.v8pub.po.sns.PubSnsPO;
import com.smate.web.v8pub.service.sns.GroupPubService;
import com.smate.web.v8pub.service.sns.PubSnsDetailService;
import com.smate.web.v8pub.service.sns.PubSnsService;

/**
 * 个人库成果编辑详情查询
 * 
 * @author aijiangbin
 * @date 2018年8月3日
 */

@Transactional(rollbackFor = Exception.class)
public class SnsEditPubDetailQueryService extends AbstractPubDetailQueryService implements PubDetailQueryService {
  protected Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private PubSnsDetailService pubSnsDetailService;

  @Autowired
  private PubSnsService pubSnsService;
  @Autowired
  private GroupPubService groupPubService;
  @Autowired
  private PubIndexUrlDao pubIndexUrlDao;
  @Value("${domainscm}")
  private String domainscm;

  @Override
  public PubDetailVO<?> queryPubDetail(Long pubId, Long grpId) throws ServiceException {
    PubSnsDetailDOM detailDOM = pubSnsDetailService.getByPubId(pubId);
    if (detailDOM == null) {
      return null;
    }
    PubDetailVO<?> pubDetailVO = buildPubTypeInfo(detailDOM);
    pubDetailVO.setPubId(pubId);
    pubDetailVO.setDes3PubId(Des3Utils.encodeToDes3(Objects.toString(pubId)));
    pubDetailVO.setPubType(detailDOM.getPubType());
    pubDetailVO.setTitle(detailDOM.getTitle());
    String summary = PubParamUtils.dealWithSpace(detailDOM.getSummary());
    pubDetailVO.setSummary(summary);
    pubDetailVO.setBriefDesc(detailDOM.getBriefDesc());
    String key = detailDOM.getKeywords();
    pubDetailVO.setKeywords(key);
    pubDetailVO.setSourceId(detailDOM.getSourceId());
    pubDetailVO.setPublishDate(detailDOM.getPublishDate());
    pubDetailVO.setCountryId(detailDOM.getCountryId());
    pubDetailVO.setFundInfo(detailDOM.getFundInfo());
    pubDetailVO.setCitations(detailDOM.getCitations());
    pubDetailVO.setDoi(detailDOM.getDoi());
    pubDetailVO.setSrcFulltextUrl(detailDOM.getSrcFulltextUrl());
    // 之后只要保留一个
    pubDetailVO.setSrcDbId(detailDOM.getSrcDbId());
    pubDetailVO.setSourceDbId(detailDOM.getSrcDbId());
    pubDetailVO.setOrganization(detailDOM.getOrganization());
    pubDetailVO.setAuthorNames(detailDOM.getAuthorNames());
    pubDetailVO.setAuthorName(detailDOM.getAuthorNames());
    pubDetailVO.setSourceUrl(detailDOM.getSourceUrl());
    PubSnsPO pub = pubSnsService.get(pubId);
    pubDetailVO.setPublishYear(pub.getPublishYear());
    if (pub.getGmtModified() != null) {
      pubDetailVO.setModifyDate(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(pub.getGmtModified()));
    }
    pubDetailVO.setUpdateMark(pub.getUpdateMark());
    pubDetailVO.setGmtCreate(pub.getGmtCreate());
    pubDetailVO.setGmtModified(pub.getGmtModified());
    if (groupPubService.getByPubId(pubId) != null) {
      pubDetailVO.setPsnId(groupPubService.getByPubId(pubId).getOwnerPsnId());
    } else if (pubSnsService.queryPubPsnId(pubId) != null) {
      pubDetailVO.setPsnId(pubSnsService.queryPubPsnId(pubId));
    }
    pubDetailVO.setPubOwnerPsnId(pubDetailVO.getPsnId());
    PubIndexUrl pubIndexUrl = pubIndexUrlDao.get(pubDetailVO.getPubId());
    pubDetailVO.setPubIndexUrl(
        pubIndexUrl == null ? "" : domainscm + "/" + ShortUrlConst.A_TYPE + "/" + pubIndexUrl.getPubIndexUrl());
    pubDetailVO.setPermission(pubSnsService.queryPubPermission(pubDetailVO.getPsnId(), pubId));
    pubDetailVO.setRecordFrom(pub.getRecordFrom());
    pubDetailVO.setUpdateMark(pub.getUpdateMark());

    buildCountryRegionId(pubDetailVO);

    buildPubMembersInfo(pubDetailVO, detailDOM);

    buildPubSituation(pubDetailVO, detailDOM);// 构建收录来源

    buildScienciArea(pubDetailVO, detailDOM);

    buildIndustry(pubDetailVO, detailDOM);

    buildPubFulltext(pubDetailVO, detailDOM);

    buildPubAccessory(pubDetailVO, detailDOM);// 构建成果附件

    buildSnsPubImpactFactors(pubDetailVO, detailDOM, pub.getPublishYear());

    pubDetailVO.setHCP(detailDOM.isHCP() ? 1 : 0);
    pubDetailVO.setHP(detailDOM.isHP() ? 1 : 0);
    pubDetailVO.setOA(detailDOM.getOA());

    return pubDetailVO;
  }
}
