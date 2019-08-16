package com.smate.web.v8pub.service.pubdetailquery;

import com.smate.core.base.exception.ServiceException;
import com.smate.core.base.pub.enums.PubSnsRecordFromEnum;
import com.smate.core.base.pub.vo.PubDetailVO;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.web.v8pub.dao.sns.pubtype.ConstPubTypeDao;
import com.smate.web.v8pub.dom.pdwh.PubPdwhDetailDOM;
import com.smate.web.v8pub.dto.PubFulltextDTO;
import com.smate.web.v8pub.po.pdwh.PdwhMemberEmailPO;
import com.smate.web.v8pub.po.pdwh.PdwhPubFullTextPO;
import com.smate.web.v8pub.po.pdwh.PubPdwhPO;
import com.smate.web.v8pub.po.pubType.ConstPubType;
import com.smate.web.v8pub.service.pdwh.PdwhMemberEmailService;
import com.smate.web.v8pub.service.pdwh.PdwhPubFullTextService;
import com.smate.web.v8pub.service.pdwh.PubPdwhDetailService;
import com.smate.web.v8pub.service.pdwh.PubPdwhService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 基准库成果详情查询
 * 
 * @author aijiangbin
 * @date 2018年8月6日
 */
@Transactional(rollbackFor = Exception.class)
public class OpenPdwhPubDetailQueryService extends AbstractPubDetailQueryService implements PubDetailQueryService {
  protected Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private PubPdwhDetailService pubPdwhDetailService;
  @Autowired
  private PubPdwhService pubPdwhService;
  @Autowired
  private ConstPubTypeDao constPubTypeDao;
  @Autowired
  private PdwhPubFullTextService pdwhPubFullTextService;
  @Autowired
  private PdwhMemberEmailService  pdwhMemberEmailService;

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
    pubDetailVO.setStatus(pubPdwhPO.getStatus().getValue());
    pubDetailVO.setUpdateMark(pubPdwhPO.getUpdateMark());
    pubDetailVO.setGmtCreate(pubPdwhPO.getGmtCreate());
    pubDetailVO.setGmtModified(pubPdwhPO.getGmtModified());
    pubDetailVO.setKeywords(key);
    pubDetailVO.setPublishDate(detailDOM.getPublishDate());
    pubDetailVO.setCountryId(detailDOM.getCountryId());
    pubDetailVO.setCitations(detailDOM.getCitations());
    pubDetailVO.setAuthorNames(detailDOM.getAuthorNames());
    pubDetailVO.setDoi(detailDOM.getDoi());
    pubDetailVO.setFundInfo(detailDOM.getFundInfo());
    pubDetailVO.setSrcFulltextUrl(detailDOM.getSrcFulltextUrl());
    pubDetailVO.setSourceUrl(detailDOM.getSourceUrl());
    pubDetailVO.setOrganization(detailDOM.getOrganization());
    pubDetailVO.setRecordFrom(PubSnsRecordFromEnum.IMPORT_FROM_PDWH);
    buildPubMembersInfo(pubDetailVO, detailDOM);

    buildOriginalEmail(pubDetailVO);

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
   * 构建原始邮件
   * @param pubDetailVO
   */
  public void buildOriginalEmail(PubDetailVO pubDetailVO){
    List<PdwhMemberEmailPO> memEmailList = pdwhMemberEmailService.findByPubId(pubDetailVO.getPubId());
    if(CollectionUtils.isNotEmpty(memEmailList)){
      String oriEmail = "";
      for(PdwhMemberEmailPO emailPO : memEmailList){
        if(StringUtils.isNotBlank(emailPO.getEmail())){
          oriEmail = oriEmail + ";" + emailPO.getEmail();
        }
      }
      if(StringUtils.isNotBlank(oriEmail)){
        pubDetailVO.setOriginalMail(oriEmail.substring(1,oriEmail.length()));
      }
    }
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
