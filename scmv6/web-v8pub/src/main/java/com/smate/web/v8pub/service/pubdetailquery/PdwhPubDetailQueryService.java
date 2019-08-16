package com.smate.web.v8pub.service.pubdetailquery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.exception.ServiceException;
import com.smate.core.base.pub.vo.PubDetailVO;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.web.v8pub.dao.pdwh.PdwhPubSituationDAO;
import com.smate.web.v8pub.dao.sns.pubtype.ConstPubTypeDao;
import com.smate.web.v8pub.dom.pdwh.PubPdwhDetailDOM;
import com.smate.web.v8pub.po.pdwh.PubPdwhPO;
import com.smate.web.v8pub.po.pubType.ConstPubType;
import com.smate.web.v8pub.service.pdwh.PubPdwhDetailService;
import com.smate.web.v8pub.service.pdwh.PubPdwhService;

/**
 * 基准库成果详情查询
 * 
 * @author aijiangbin
 * @date 2018年8月6日
 */
@Transactional(rollbackFor = Exception.class)
public class PdwhPubDetailQueryService extends AbstractPubDetailQueryService implements PubDetailQueryService {
  protected Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private PubPdwhDetailService pubPdwhDetailService;
  @Autowired
  private PubPdwhService pubPdwhService;
  @Autowired
  private ConstPubTypeDao constPubTypeDao;
  @Autowired
  private PdwhPubSituationDAO PdwhPubSituationDao;

  @Override
  public PubDetailVO queryPubDetail(Long pubId, Long grpId) throws ServiceException {
    PubPdwhDetailDOM detailDOM = pubPdwhDetailService.getByPubId(pubId);
    if (detailDOM == null) {
      return null;
    }
    PubDetailVO pubDetailVO = buildPubTypeInfo(detailDOM);
    pubDetailVO.setPubId(pubId);
    pubDetailVO.setDes3PubId(Des3Utils.encodeToDes3(String.valueOf(pubId)));
    pubDetailVO.setPubType(detailDOM.getPubType());
    ConstPubType constPubType = constPubTypeDao.get(detailDOM.getPubType());
    pubDetailVO.setTypeName(constPubType != null ? constPubType.getZhName() : "");
    pubDetailVO.setTitle(detailDOM.getTitle());
    pubDetailVO.setSummary(detailDOM.getSummary());
    pubDetailVO.setBriefDesc(detailDOM.getBriefDesc());
    pubDetailVO.setSourceDbId(detailDOM.getSrcDbId());
    String key = detailDOM.getKeywords();
    PubPdwhPO pubPdwhPO = pubPdwhService.get(detailDOM.getPubId());
    pubDetailVO.setPublishYear(pubPdwhPO.getPublishYear());
    pubDetailVO.setUpdateMark(pubPdwhPO.getUpdateMark());
    pubDetailVO.setGmtCreate(pubPdwhPO.getGmtCreate());
    pubDetailVO.setGmtModified(pubPdwhPO.getGmtModified());
    pubDetailVO.setKeywords(pubPdwhDetailService.splitPubKeywords(key));
    pubDetailVO.setPublishDate(detailDOM.getPublishDate());
    pubDetailVO.setCountryId(detailDOM.getCountryId());
    pubDetailVO.setCitations(detailDOM.getCitations());
    pubDetailVO.setAuthorNames(detailDOM.getAuthorNames());
    pubDetailVO.setDoi(detailDOM.getDoi());
    pubDetailVO.setFundInfo(detailDOM.getFundInfo());
    pubDetailVO.setSrcFulltextUrl(detailDOM.getSrcFulltextUrl());
    pubDetailVO.setSourceUrl(detailDOM.getSourceUrl());
    pubDetailVO.setOrganization(detailDOM.getOrganization());
    pubDetailVO.setSrcDbId(detailDOM.getSrcDbId());
    pubDetailVO.setSourceId(detailDOM.getSourceId());
    buildPubMembersInfo(pubDetailVO, detailDOM);

    buildPubFulltext(pubDetailVO, detailDOM);

    buildPubSituation(pubDetailVO, detailDOM);

    buildPdwhPubIndex(pubDetailVO, detailDOM);

    buildPdwhPubImpactFactors(pubDetailVO, detailDOM, pubPdwhPO.getPublishYear());

    pubDetailVO.setHCP(detailDOM.isHCP() ? 1 : 0);
    pubDetailVO.setHP(detailDOM.isHP() ? 1 : 0);
    pubDetailVO.setOA(detailDOM.getOA());

    return pubDetailVO;
  }

}
