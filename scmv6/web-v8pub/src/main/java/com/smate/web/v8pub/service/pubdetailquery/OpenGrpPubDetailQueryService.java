package com.smate.web.v8pub.service.pubdetailquery;


import com.smate.core.base.exception.ServiceException;
import com.smate.core.base.psn.service.psnpub.PsnPubService;
import com.smate.core.base.pub.vo.PubDetailVO;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.web.v8pub.dom.sns.PubSnsDetailDOM;
import com.smate.web.v8pub.po.sns.PubSnsPO;
import com.smate.web.v8pub.po.sns.group.GroupPubPO;
import com.smate.web.v8pub.service.sns.GroupPubService;
import com.smate.web.v8pub.service.sns.PubFullTextService;
import com.smate.web.v8pub.service.sns.PubSnsDetailService;
import com.smate.web.v8pub.service.sns.PubSnsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

/**
 * open系统个人库成果 详情查询
 * 
 * @author aijiangbin
 * @date 2018年8月14日
 */

@Transactional(rollbackFor = Exception.class)
public class OpenGrpPubDetailQueryService extends AbstractPubDetailQueryService implements PubDetailQueryService {
  protected Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private PubSnsDetailService pubSnsDetailService;
  @Autowired
  private PubFullTextService pubFullTextService;
  @Autowired
  private PubSnsService pubSnsService;
  @Autowired
  private PsnPubService psnPubService;
  @Autowired
  private GroupPubService groupPubService;

  @Override
  public PubDetailVO queryPubDetail(Long pubId, Long grpId) throws ServiceException {


    GroupPubPO groupPubPO = groupPubService.getByGrpIdAndPubId(grpId, pubId);
    if (groupPubPO == null) {
      return null;
    }
    PubSnsDetailDOM detailDOM = pubSnsDetailService.getByPubId(pubId);
    if (detailDOM == null) {
      return null;
    }
    PubDetailVO pubDetailVO = buildPubTypeInfo(detailDOM);
    pubDetailVO.setPubId(pubId);
    pubDetailVO.setDes3PubId(Des3Utils.encodeToDes3(Objects.toString(pubId)));
    pubDetailVO.setPubType(detailDOM.getPubType());
    pubDetailVO.setTitle(detailDOM.getTitle());
    pubDetailVO.setSummary(detailDOM.getSummary());
    pubDetailVO.setBriefDesc(detailDOM.getBriefDesc());
    String key = detailDOM.getKeywords();
    pubDetailVO.setKeywords(key);
    pubDetailVO.setPublishDate(detailDOM.getPublishDate());
    pubDetailVO.setCountryId(detailDOM.getCountryId());
    pubDetailVO.setFundInfo(detailDOM.getFundInfo());
    pubDetailVO.setCitations(detailDOM.getCitations());
    pubDetailVO.setDoi(detailDOM.getDoi());
    pubDetailVO.setSrcFulltextUrl(detailDOM.getSrcFulltextUrl());
    pubDetailVO.setOrganization(detailDOM.getOrganization());

    // 成果拥有者
    Long pubOwnerId = psnPubService.getPubOwnerId(pubId);
    pubDetailVO.setPubOwnerPsnId(pubOwnerId);
    PubSnsPO pub = pubSnsService.get(pubId);

    pubDetailVO.setRecordFrom(pub.getRecordFrom());
    pubDetailVO.setHCP(detailDOM.isHCP() ? 1 : 0);
    pubDetailVO.setHP(detailDOM.isHP() ? 1 : 0);
    pubDetailVO.setOA(detailDOM.getOA());

    return pubDetailVO;
  }



}
