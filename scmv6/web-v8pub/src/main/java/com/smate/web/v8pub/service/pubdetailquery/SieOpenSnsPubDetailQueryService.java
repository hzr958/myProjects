package com.smate.web.v8pub.service.pubdetailquery;


import com.smate.core.base.exception.ServiceException;
import com.smate.core.base.pub.dao.PubIndexUrlDao;
import com.smate.core.base.pub.model.PubIndexUrl;
import com.smate.core.base.pub.po.dao.PubPdwhSnsRelationDAO;
import com.smate.core.base.pub.vo.PubDetailVO;
import com.smate.core.base.utils.constant.ShortUrlConst;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.web.v8pub.dao.sns.CategoryMapBaseDao;
import com.smate.web.v8pub.dom.PatentInfoBean;
import com.smate.web.v8pub.dom.sns.PubSnsDetailDOM;
import com.smate.web.v8pub.dto.PubFulltextDTO;
import com.smate.web.v8pub.dto.ScienceAreaDTO;
import com.smate.web.v8pub.po.sns.CategoryMapBase;
import com.smate.web.v8pub.po.sns.PubFullTextPO;
import com.smate.web.v8pub.po.sns.PubScienceAreaPO;
import com.smate.web.v8pub.po.sns.PubSnsPO;
import com.smate.web.v8pub.service.sns.PubFullTextService;
import com.smate.web.v8pub.service.sns.PubScienceAreaService;
import com.smate.web.v8pub.service.sns.PubSnsDetailService;
import com.smate.web.v8pub.service.sns.PubSnsService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * open系统sie个人库群组成果的基本信息 详情查询
 * 
 * @author aijiangbin
 * @date 2018年8月14日
 */

@Transactional(rollbackFor = Exception.class)
public class SieOpenSnsPubDetailQueryService extends AbstractPubDetailQueryService implements PubDetailQueryService {
  protected Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private PubSnsDetailService pubSnsDetailService;
  @Autowired
  private PubFullTextService pubFullTextService;
  @Autowired
  private PubSnsService pubSnsService;
  @Autowired
  private PubScienceAreaService pubScienceAreaService;
  @Autowired
  private PubIndexUrlDao pubIndexUrlDao;
  @Autowired
  private CategoryMapBaseDao categoryMapBaseDao;
  private PubDetailQueryService pubDetailQueryService;
  @Value("${domainscm}")
  private String domainscm;
  @Autowired
  private PubPdwhSnsRelationDAO pubPdwhSnsRelationDAO;

  public PubDetailQueryService getPubDetailQueryService() {
    return pubDetailQueryService;
  }

  public void setPubDetailQueryService(PubDetailQueryService pubDetailQueryService) {
    this.pubDetailQueryService = pubDetailQueryService;
  }

  @Override
  public PubDetailVO queryPubDetail(Long pubId, Long grpId) throws ServiceException {
    PubSnsPO pub = pubSnsService.get(pubId);
    if(pub == null){
      return null;
    }
    if(pub.getRecordFrom() != null && pub.getRecordFrom().getValue() == 3){  // 从基准库中获取成果详情哦
      Long pdwhPubId = pubPdwhSnsRelationDAO.getPdwhPubIdBySnsPubId(pubId);
      if(pdwhPubId !=null){
        return pubDetailQueryService.queryPubDetail(pdwhPubId,grpId);
      }
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
    pubDetailVO.setSourceDbId(detailDOM.getSrcDbId());
    pubDetailVO.setOrganization(detailDOM.getOrganization());
    pubDetailVO.setAuthorNames(detailDOM.getAuthorNames());
    pubDetailVO.setAuthorName(detailDOM.getAuthorNames());
    pubDetailVO.setGmtCreate(pub.getGmtCreate());
    pubDetailVO.setGmtModified(pub.getGmtModified());
    PubIndexUrl pubIndexUrl = pubIndexUrlDao.get(pubDetailVO.getPubId());
    pubDetailVO.setPubIndexUrl(
        pubIndexUrl == null ? "" : domainscm + "/" + ShortUrlConst.A_TYPE + "/" + pubIndexUrl.getPubIndexUrl());
    pubDetailVO.setRecordFrom(pub.getRecordFrom());
    pubDetailVO.setUpdateMark(pub.getUpdateMark());
    pubDetailVO.setPubOwnerPsnId(pubSnsService.queryPubPsnId(pubId));
    // 获取申请状态
    if (detailDOM.getTypeInfo() instanceof PatentInfoBean) {
      PatentInfoBean typeInfo = (PatentInfoBean) detailDOM.getTypeInfo();
      pubDetailVO.setApplicationStatus(typeInfo.getStatus());;
      pubDetailVO.setApplicationDate(typeInfo.getApplicationDate());
      // 获取科技领域
      List<PubScienceAreaPO> scienceAreaList = pubScienceAreaService.getScienceAreaList(pubId);
      if (scienceAreaList != null) {
        List<ScienceAreaDTO> scienceAreas = new ArrayList<>();
        for (PubScienceAreaPO pubScienceAreaPO : scienceAreaList) {
          CategoryMapBase categoryMapBase =
              categoryMapBaseDao.getCategoryMapBase(NumberUtils.toInt(pubScienceAreaPO.getScienceAreaId().toString()));
          ScienceAreaDTO scienceArea = new ScienceAreaDTO(pubScienceAreaPO.getScienceAreaId(),
              categoryMapBase.getCategoryZh(), categoryMapBase.getCategoryEn());
          scienceAreas.add(scienceArea);
        }
        pubDetailVO.setScienceAreas(scienceAreas);;
      }
    }
    buildCountryRegionId(pubDetailVO);

    buildPubMembersInfo(pubDetailVO, detailDOM);

    buildPubSituation(pubDetailVO, detailDOM);// 构建收录来源

    buildPubFulltext(pubDetailVO, detailDOM);

    buildSnsPubImpactFactors(pubDetailVO, detailDOM,pub.getPublishYear());

    pubDetailVO.setHCP(detailDOM.isHCP() ? 1 : 0);
    pubDetailVO.setHP(detailDOM.isHP() ? 1 : 0);
    pubDetailVO.setOA(detailDOM.getOA());
    return pubDetailVO;
  }

  /**
   * 构建全文
   *
   * @param pubDetailVO
   * @param detailDOM
   */
  @SuppressWarnings("rawtypes")
  public void buildPubFulltext(PubDetailVO pubDetailVO, PubSnsDetailDOM detailDOM) {
    // 全文逻辑id 主键唯一的
    PubFulltextDTO fullText = new PubFulltextDTO();
    PubFullTextPO fullTextPO = pubFullTextService.get(detailDOM.getPubId());
    if (fullTextPO != null) {
      String des3fileId = Des3Utils.encodeToDes3(Objects.toString(fullTextPO.getFileId()));
      fullText.setDes3fileId(des3fileId);
      fullText.setFileName(fullTextPO.getFileName());
      fullText.setPermission(fullTextPO.getPermission());
      if (StringUtils.isNotBlank(fullTextPO.getThumbnailPath())) {
        fullText.setThumbnailPath(this.domainscm + fullTextPO.getThumbnailPath());
      }
      pubDetailVO.setFullText(fullText);

    }
  }



}
