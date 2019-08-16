package com.smate.web.v8pub.service.pubdetailquery;

import com.smate.core.base.exception.ServiceException;
import com.smate.core.base.pub.vo.PubDetailVO;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.web.v8pub.dom.pdwh.PubPdwhDetailDOM;
import com.smate.web.v8pub.dto.PubFulltextDTO;
import com.smate.web.v8pub.po.pdwh.PdwhPubFullTextPO;
import com.smate.web.v8pub.po.pdwh.PubPdwhPO;
import com.smate.web.v8pub.po.sns.PubAssignLogPO;
import com.smate.web.v8pub.service.pdwh.PdwhPubFullTextService;
import com.smate.web.v8pub.service.pdwh.PubAssignLogService;
import com.smate.web.v8pub.service.pdwh.PubPdwhDetailService;
import com.smate.web.v8pub.service.pdwh.PubPdwhService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * 基准库成果详情查询
 * 
 * 通过pubConfirmId 查询得到
 * 
 * 
 * @author aijiangbin
 * @date 2018年8月6日
 */
@Transactional(rollbackFor = Exception.class)
public class PdwhPubDetailByPubConfirmIdQueryService extends AbstractPubDetailQueryService
    implements PubDetailQueryService {
  protected Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private PubPdwhDetailService pubPdwhDetailService;
  @Autowired
  private PdwhPubFullTextService pdwhPubFullTextService;
  @Autowired
  private PubAssignLogService pubAssignLogService;
  @Autowired
  private PubPdwhService pubPdwhService;

  @Override
  public PubDetailVO queryPubDetail(Long pubConfirmId, Long grpId) throws ServiceException {
    PubAssignLogPO pubAssignLogPO = pubAssignLogService.findByPubConfirmId(pubConfirmId);
    if (pubAssignLogPO == null) {
      return null;
    }
    Long pubId = pubAssignLogPO.getPdwhPubId();
    PubPdwhDetailDOM detailDOM = pubPdwhDetailService.getByPubId(pubId);
    if (detailDOM == null) {
      return null;
    }
    PubDetailVO pubDetailVO = buildPubTypeInfo(detailDOM);
    pubDetailVO.setPubId(pubId);
    pubDetailVO.setDes3PubId(Des3Utils.encodeToDes3(String.valueOf(pubId)));
    pubDetailVO.setPubType(detailDOM.getPubType());
    pubDetailVO.setTitle(detailDOM.getTitle());
    pubDetailVO.setSummary(detailDOM.getSummary());
    pubDetailVO.setBriefDesc(detailDOM.getBriefDesc());
    pubDetailVO.setSourceDbId(detailDOM.getSrcDbId());
    PubPdwhPO pubPdwhPO = pubPdwhService.get(detailDOM.getPubId());
    pubDetailVO.setStatus(pubPdwhPO.getStatus().getValue());
    pubDetailVO.setGmtCreate(pubPdwhPO.getGmtCreate());
    pubDetailVO.setGmtModified(pubPdwhPO.getGmtModified());
    String key = detailDOM.getKeywords();
    pubDetailVO.setKeywords(key);
    pubDetailVO.setPublishDate(detailDOM.getPublishDate());
    pubDetailVO.setCountryId(detailDOM.getCountryId());
    pubDetailVO.setCitations(detailDOM.getCitations());
    pubDetailVO.setAuthorNames(detailDOM.getAuthorNames());
    pubDetailVO.setDoi(detailDOM.getDoi());
    pubDetailVO.setFundInfo(detailDOM.getFundInfo());
    pubDetailVO.setSrcFulltextUrl(detailDOM.getSrcFulltextUrl());
    pubDetailVO.setPubOwnerPsnId(pubAssignLogPO.getPsnId());
    pubDetailVO.setOrganization(detailDOM.getOrganization());
    buildPubMembersInfo(pubDetailVO, detailDOM);

    buildCountryRegionId(pubDetailVO);

    buildPubFulltext(pubDetailVO, detailDOM);

    buildPubSituation(pubDetailVO, detailDOM);

    buildPdwhPubIndex(pubDetailVO, detailDOM);

    buildPdwhPubImpactFactors(pubDetailVO, detailDOM,pubPdwhPO.getPublishYear());

    pubDetailVO.setHCP(detailDOM.isHCP() ? 1 : 0);
    pubDetailVO.setHP(detailDOM.isHP() ? 1 : 0);
    pubDetailVO.setOA(detailDOM.getOA());

    return pubDetailVO;
  }

  /**
   * 构建基准库全文
   *
   * @param pubDetailVO
   * @param detailDOM
   */
  @SuppressWarnings("rawtypes")
  public void buildPubFulltext(PubDetailVO pubDetailVO, PubPdwhDetailDOM detailDOM) {
    Long pdwhPubId = pubDetailVO.getPubId();
    PdwhPubFullTextPO fullTextPO = pdwhPubFullTextService.getPdwhPubfulltext(pdwhPubId);
    // 全文逻辑id 主键唯一的
    // Long fulltextId = detailDOM.getFulltextId();
    if (fullTextPO != null) {
      PubFulltextDTO fulltext = new PubFulltextDTO();
      fulltext.setFileId(fullTextPO.getFileId());
      fulltext.setFileName(fullTextPO.getFileName());
      pubDetailVO.setFullText(fulltext);
      if (StringUtils.isNotBlank(fullTextPO.getThumbnailPath())) {
        pubDetailVO.setFulltextImageUrl(this.domainscm + fullTextPO.getThumbnailPath());
        fulltext.setThumbnailPath(this.domainscm + fullTextPO.getThumbnailPath());
      }

    }
  }

}
