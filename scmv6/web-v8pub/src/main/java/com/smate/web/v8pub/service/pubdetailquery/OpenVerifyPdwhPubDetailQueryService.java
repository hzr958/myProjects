package com.smate.web.v8pub.service.pubdetailquery;

import com.smate.core.base.exception.ServiceException;
import com.smate.core.base.pub.vo.PubDetailVO;
import com.smate.web.v8pub.dom.pdwh.PubPdwhDetailDOM;
import com.smate.web.v8pub.dto.PubFulltextDTO;
import com.smate.web.v8pub.po.pdwh.PdwhPubFullTextPO;
import com.smate.web.v8pub.po.pdwh.PubPdwhPO;
import com.smate.web.v8pub.service.pdwh.PdwhPubFullTextService;
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
 * @author aijiangbin
 * @date 2018年8月6日
 */
@Transactional(rollbackFor = Exception.class)
public class OpenVerifyPdwhPubDetailQueryService extends AbstractPubDetailQueryService
    implements PubDetailQueryService {
  protected Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private PubPdwhDetailService pubPdwhDetailService;
  @Autowired
  private PubPdwhService pubPdwhService;
  @Autowired
  private PdwhPubFullTextService pdwhPubFullTextService;

  @Override
  public PubDetailVO queryPubDetail(Long pubId, Long grpId) throws ServiceException {
    PubPdwhDetailDOM detailDOM = pubPdwhDetailService.getByPubId(pubId);
    if (detailDOM == null) {
      return null;
    }
    PubDetailVO pubDetailVO = buildPubTypeInfo(detailDOM);
    PubPdwhPO pubPdwhPO = pubPdwhService.get(detailDOM.getPubId());

    pubDetailVO.setTitle(detailDOM.getTitle());
    pubDetailVO.setPubId(detailDOM.getPubId());
    pubDetailVO.setPubType(detailDOM.getPubType());
    pubDetailVO.setPublishYear(pubPdwhPO.getPublishYear());
    pubDetailVO.setAuthorNames(detailDOM.getAuthorNames());
    pubDetailVO.setDoi(detailDOM.getDoi());
    pubDetailVO.setFundInfo(detailDOM.getFundInfo());
    pubDetailVO.setSrcDbId(detailDOM.getSrcDbId());
    buildPdwhPubImpactFactors(pubDetailVO, detailDOM,pubPdwhPO.getPublishYear());

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
