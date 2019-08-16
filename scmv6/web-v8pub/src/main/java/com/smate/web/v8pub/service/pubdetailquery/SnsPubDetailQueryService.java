package com.smate.web.v8pub.service.pubdetailquery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.exception.ServiceException;
import com.smate.core.base.psn.dao.psncnf.PsnConfigPubDao;
import com.smate.core.base.psn.model.psncnf.PsnConfigPub;
import com.smate.core.base.psn.service.psnpub.PsnPubService;
import com.smate.core.base.pub.po.dao.PubPdwhSnsRelationDAO;
import com.smate.core.base.pub.vo.PubDetailVO;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.web.v8pub.dao.sns.pubtype.ConstPubTypeDao;
import com.smate.web.v8pub.dom.sns.PubSnsDetailDOM;
import com.smate.web.v8pub.po.pdwh.PdwhPubStatisticsPO;
import com.smate.web.v8pub.po.pubType.ConstPubType;
import com.smate.web.v8pub.po.sns.PubSnsPO;
import com.smate.web.v8pub.po.sns.PubStatisticsPO;
import com.smate.web.v8pub.service.pdwh.PdwhPubStatisticsService;
import com.smate.web.v8pub.service.sns.PubSnsDetailService;
import com.smate.web.v8pub.service.sns.PubSnsService;
import com.smate.web.v8pub.service.sns.PubStatisticsService;

/**
 * 个人库成果详情查询
 * 
 * @author aijiangbin
 * @date 2018年8月6日
 */
@Transactional(rollbackFor = Exception.class)
public class SnsPubDetailQueryService extends AbstractPubDetailQueryService implements PubDetailQueryService {
  protected Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private PubSnsDetailService pubSnsDetailService;
  @Autowired
  private ConstPubTypeDao constPubTypeDao;
  @Autowired
  private PubStatisticsService newPubStatisticsService;
  @Autowired
  private PsnPubService psnPubService;
  @Autowired
  private PubSnsService pubSnsService;
  @Autowired
  private PubPdwhSnsRelationDAO pubPdwhSnsRelationDAO;
  @Autowired
  private PdwhPubStatisticsService newPdwhPubStatisticsService;
  @Autowired
  private PsnConfigPubDao psnConfigPubDao;

  @SuppressWarnings("rawtypes")
  @Override
  public PubDetailVO queryPubDetail(Long pubId, Long grpId) throws ServiceException {
    PubDetailVO pubDetailVO = new PubDetailVO();
    PubSnsDetailDOM detailDOM = pubSnsDetailService.getByPubId(pubId);
    if (detailDOM == null) {
      return null;
    }
    pubDetailVO = buildPubTypeInfo(detailDOM);
    pubDetailVO.setPubId(pubId);
    pubDetailVO.setDes3PubId(Des3Utils.encodeToDes3(String.valueOf(pubId)));
    pubDetailVO.setPubType(detailDOM.getPubType());
    ConstPubType constPubType = constPubTypeDao.get(detailDOM.getPubType());
    pubDetailVO.setTypeName(constPubType != null ? constPubType.getZhName() : "");
    pubDetailVO.setTitle(detailDOM.getTitle());
    pubDetailVO.setSummary(detailDOM.getSummary());
    pubDetailVO.setBriefDesc(detailDOM.getBriefDesc());
    pubDetailVO.setSourceDbId(detailDOM.getSrcDbId());
    PubSnsPO pubSnsPO = pubSnsService.get(detailDOM.getPubId());
    if (pubSnsPO != null) {
      pubDetailVO.setGmtCreate(pubSnsPO.getGmtCreate());
      pubDetailVO.setGmtModified(pubSnsPO.getGmtModified());
      pubDetailVO.setCitations(pubSnsPO.getCitations());
      pubDetailVO.setPublishYear(pubSnsPO.getPublishYear());
    }
    String key = detailDOM.getKeywords();
    pubDetailVO.setKeywords(pubSnsDetailService.splitPubKeywords(key));
    pubDetailVO.setCitations(detailDOM.getCitations());
    pubDetailVO.setAuthorNames(pubSnsDetailService.buildPubAuthorNames(pubId));
    pubDetailVO.setPublishDate(detailDOM.getPublishDate());


    pubDetailVO.setDoi(detailDOM.getDoi());

    pubDetailVO.setPubOwnerPsnId(psnPubService.getPubOwnerId(pubId));
    buildPubMembersInfo(pubDetailVO, detailDOM);

    buildSnsPubIndexUrl(pubDetailVO, detailDOM);

    pubDetailVO.setSrcFulltextUrl(detailDOM.getSrcFulltextUrl());
    pubDetailVO.setSourceUrl(detailDOM.getSourceUrl());

    // buildPubFulltext(pubDetailVO, detailDOM); 逻辑已调整,SCM-23246

    buildPubAccessory(pubDetailVO, detailDOM);

    buildPubStatistics(pubDetailVO, pubId);

    buildPubSituation(pubDetailVO, detailDOM);// 构建收录来源

    pubDetailVO.setHCP(detailDOM.isHCP() ? 1 : 0);
    pubDetailVO.setHP(detailDOM.isHP() ? 1 : 0);
    pubDetailVO.setOA(detailDOM.getOA());
    pubDetailVO.setUpdateMark(buildUpdateMark(pubId));

    return pubDetailVO;
  }

  private Integer buildUpdateMark(Long pubId) {
    if (NumberUtils.isNotNullOrZero(pubId)) {
      PubSnsPO pubSnsPO = pubSnsService.get(pubId);
      if (pubSnsPO != null) {
        return pubSnsPO.getUpdateMark();
      }
    }
    return null;
  }

  public PubDetailVO queryPubDetail(Long pubId, Long grpId, Long psnId) throws ServiceException {
    PubDetailVO pubDetailVO = new PubDetailVO();
    PubSnsDetailDOM detailDOM = pubSnsDetailService.getByPubId(pubId);
    if (detailDOM == null) {
      return null;
    }
    pubDetailVO = buildPubTypeInfo(detailDOM);
    pubDetailVO.setPubId(pubId);
    pubDetailVO.setDes3PubId(Des3Utils.encodeToDes3(String.valueOf(pubId)));
    pubDetailVO.setPubType(detailDOM.getPubType());
    ConstPubType constPubType = constPubTypeDao.get(detailDOM.getPubType());
    pubDetailVO.setTypeName(constPubType != null ? constPubType.getZhName() : "");
    pubDetailVO.setTitle(detailDOM.getTitle());
    pubDetailVO.setSummary(detailDOM.getSummary());
    pubDetailVO.setBriefDesc(detailDOM.getBriefDesc());
    pubDetailVO.setSourceDbId(detailDOM.getSrcDbId());
    PubSnsPO pubSnsPO = pubSnsService.get(detailDOM.getPubId());
    if (pubSnsPO != null) {
      pubDetailVO.setGmtCreate(pubSnsPO.getGmtCreate());
      pubDetailVO.setGmtModified(pubSnsPO.getGmtModified());
      pubDetailVO.setCitations(pubSnsPO.getCitations());
    }
    String key = detailDOM.getKeywords();
    pubDetailVO.setKeywords(pubSnsDetailService.splitPubKeywords(key));
    pubDetailVO.setCitations(detailDOM.getCitations());
    pubDetailVO.setAuthorNames(pubSnsDetailService.buildPubAuthorNames(pubId));
    pubDetailVO.setPublishDate(detailDOM.getPublishDate());
    pubDetailVO.setDoi(detailDOM.getDoi());

    pubDetailVO.setPubOwnerPsnId(psnPubService.getPubOwnerId(pubId));
    buildPubMembersInfo(pubDetailVO, detailDOM);

    buildSnsPubIndexUrl(pubDetailVO, detailDOM);

    pubDetailVO.setSrcFulltextUrl(detailDOM.getSrcFulltextUrl());
    pubDetailVO.setSourceUrl(detailDOM.getSourceUrl());

    buildPubFulltext(pubDetailVO, detailDOM);

    buildPubAccessory(pubDetailVO, detailDOM);

    buildPubStatistics(pubDetailVO, pubId);

    buildPubSituation(pubDetailVO, detailDOM);// 构建收录来源

    pubDetailVO.setHCP(detailDOM.isHCP() ? 1 : 0);
    pubDetailVO.setHP(detailDOM.isHP() ? 1 : 0);
    pubDetailVO.setOA(detailDOM.getOA());

    return pubDetailVO;
  }

  /**
   * 构建阅读数、引用数
   * 
   * @param pubDetailVO
   * @param pubId
   */
  @SuppressWarnings("rawtypes")
  private void buildPubStatistics(PubDetailVO pubDetailVO, Long pubId) {
    PubStatisticsPO statistics = newPubStatisticsService.get(pubId);
    // SCM-23420 数据来源调整 跟基准库关联的成果， 就公用基准库的数据
    // SCM-24461
    boolean flag = false;
    PsnConfigPub psnConfigPub = psnConfigPubDao.getPsnConfigPubByPubId(pubId);
    if (psnConfigPub != null && psnConfigPub.getAnyUser() < 7) {
      flag = true;
    }
    Long pdwhPubId = pubPdwhSnsRelationDAO.getPdwhPubIdBySnsPubId(pubId);
    if (pdwhPubId != null && pdwhPubId > 0L && !flag) {
      buildSnsPdwhRelationReadCount(pubDetailVO, pdwhPubId);// 阅读数
    } else {
      buildSnsReadCount(pubDetailVO, statistics);
    }
    pubDetailVO.setRefCount(statistics == null || statistics.getRefCount() == null ? 0 : statistics.getRefCount());
  }

  public void buildSnsReadCount(PubDetailVO pubDetailVO, PubStatisticsPO statistics) {
    if (statistics != null) {
      pubDetailVO.setReadCount(statistics.getReadCount() == null ? 0 : statistics.getReadCount());
    } else {
      pubDetailVO.setReadCount(0);
    }
  }

  public void buildSnsPdwhRelationReadCount(PubDetailVO pubDetailVO, Long pdwhPubId) {
    PdwhPubStatisticsPO sta = newPdwhPubStatisticsService.get(pdwhPubId);
    Long count = newPubStatisticsService.getSnsPubReadCounts(pdwhPubId);
    int readCount = Integer.parseInt(count.toString());
    if (sta != null) {
      pubDetailVO.setReadCount(sta.getReadCount() == null ? 0 + readCount : sta.getReadCount() + readCount);
    } else {
      pubDetailVO.setReadCount(readCount);
    }
  }

}
