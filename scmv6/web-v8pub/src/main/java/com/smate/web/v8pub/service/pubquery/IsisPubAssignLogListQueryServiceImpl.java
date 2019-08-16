package com.smate.web.v8pub.service.pubquery;

import com.smate.core.base.file.enums.FileTypeEnum;
import com.smate.core.base.file.service.FileDownloadUrlService;
import com.smate.core.base.pub.vo.PubDetailVO;
import com.smate.core.base.pub.vo.PubInfo;
import com.smate.core.base.utils.constant.ShortUrlConst;
import com.smate.web.v8pub.consts.V8pubConst;
import com.smate.web.v8pub.dao.pdwh.PdwhPubIndexUrlDao;
import com.smate.web.v8pub.dao.sns.pubtype.ConstPubTypeDao;
import com.smate.web.v8pub.dom.pdwh.PubPdwhDetailDOM;
import com.smate.web.v8pub.po.PubPO;
import com.smate.web.v8pub.po.pdwh.PdwhPubFullTextPO;
import com.smate.web.v8pub.po.pdwh.PdwhPubIndexUrl;
import com.smate.web.v8pub.po.pdwh.PubPdwhPO;
import com.smate.web.v8pub.po.pubType.ConstPubType;
import com.smate.web.v8pub.po.sns.PubAssignLogPO;
import com.smate.web.v8pub.service.pdwh.PdwhPubFullTextService;
import com.smate.web.v8pub.service.pdwh.PubAssignLogService;
import com.smate.web.v8pub.service.pdwh.PubPdwhDetailService;
import com.smate.web.v8pub.service.pdwh.PubPdwhService;
import com.smate.web.v8pub.service.query.PubQueryDTO;
import com.smate.web.v8pub.vo.PubListResult;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Transactional(rollbackFor = Exception.class)
public class IsisPubAssignLogListQueryServiceImpl extends AbstractPubQueryService {
  @Resource
  private PubAssignLogService pubAssignLogService;
  @Resource
  private PdwhPubFullTextService pdwhPubFullTextService;
  @Autowired
  private FileDownloadUrlService fileDownUrlService;
  @Autowired
  private PdwhPubIndexUrlDao pdwhPubIndexUrlDao;
  @Autowired
  private PubPdwhDetailService pubPdwhDetailService;
  @Autowired
  private PubPdwhService pubPdwhService;
  @Autowired
  private ConstPubTypeDao constPubTypeDao;

  @Override
  public Map<String, Object> checkData(PubQueryDTO pubQueryDTO) {
    Map<String, Object> map = new HashMap<>();
    if (pubQueryDTO.getSearchPsnId() == null || pubQueryDTO.getSearchPsnId() == 0L) {
      map.put(V8pubConst.RESULT, V8pubConst.ERROR);
      map.put(V8pubConst.ERROR_MSG, "查询的人员psnId不能为空");
      return map;
    }
    return null;
  }

  @Override
  public void queryPubs(PubQueryDTO pubQueryDTO) {
    pubAssignLogService.queryAllPubAssignListForOpen(pubQueryDTO);
    if (!CollectionUtils.isEmpty(pubQueryDTO.getPubIds())) {
      pubPdwhService.findAllByIds(pubQueryDTO);
    }

  }

  @Override
  public PubListResult assembleData(PubQueryDTO pubQueryDTO) {
    PubListResult listResult = new PubListResult();
    List<Long> pubIds = new ArrayList<>();
    List<PubPO> pubList = pubQueryDTO.getPubList();
    pubQueryDTO.setPubIds(pubIds);
    if (pubList != null && pubList.size() > 0) {
      List<PubDetailVO> list = new ArrayList<>(pubQueryDTO.getPageSize() * 2 + 1);
      for (PubPO pubPO : pubList) {
        PubPdwhPO pubPdwhPO = (PubPdwhPO) pubPO;
        pubIds.add(pubPdwhPO.getPubId());
        PubPdwhDetailDOM detailDOM = pubPdwhDetailService.getByPubId(pubPdwhPO.getPubId());
        if (detailDOM == null) {
          return null;
        }
        PubDetailVO pubDetailVO = buildPubTypeInfo(detailDOM);
        pubDetailVO.setPubId(pubPdwhPO.getPubId());
        pubDetailVO.setPubType(detailDOM.getPubType());
        ConstPubType constPubType = constPubTypeDao.get(detailDOM.getPubType());
        pubDetailVO.setTypeName(constPubType != null ? constPubType.getZhName() : "");
        pubDetailVO.setTitle(detailDOM.getTitle());
        pubDetailVO.setSummary(detailDOM.getSummary());
        pubDetailVO.setBriefDesc(detailDOM.getBriefDesc());
        pubDetailVO.setSourceDbId(detailDOM.getSrcDbId());
        String key = detailDOM.getKeywords();
        pubDetailVO.setUpdateMark(pubPdwhPO.getUpdateMark());
        pubDetailVO.setGmtCreate(pubPdwhPO.getGmtCreate());
        pubDetailVO.setGmtModified(pubPdwhPO.getGmtModified());
        pubDetailVO.setKeywords(key);
        pubDetailVO.setPublishDate(detailDOM.getPublishDate());
        pubDetailVO.setCountryId(detailDOM.getCountryId());
        pubDetailVO.setCitations(detailDOM.getCitations());
        pubDetailVO.setAuthorNames(detailDOM.getAuthorNames());
        pubDetailVO.setFundInfo(detailDOM.getFundInfo());
        pubDetailVO.setSrcFulltextUrl(detailDOM.getSrcFulltextUrl());
        pubDetailVO.setOrganization(detailDOM.getOrganization());
        // pubDetailVO.setPubOwnerPsnId(pubPdwhService.queryPubPsnId(detailDOM.getPubId()));
        buildPubMembersInfo(pubDetailVO, detailDOM);
        pubDetailVO.setCountryId(detailDOM.getCountryId());
        pubDetailVO.setSourceDbId(detailDOM.getSrcDbId());
        pubDetailVO.setDoi(detailDOM.getDoi());
        pubDetailVO.setAuthorName(detailDOM.getAuthorNames());
        pubDetailVO.setPublishYear(pubPdwhPO.getPublishYear());
        pubDetailVO.setPublishMonth(pubPdwhPO.getPublishMonth());
        pubDetailVO.setPublishDay(pubPdwhPO.getPublishDay());

        buildCountryRegionId(pubDetailVO);

        buildPubFulltext(pubDetailVO, detailDOM);

        buildPubSituation(pubDetailVO, detailDOM);

        buildPdwhPubIndex(pubDetailVO, detailDOM);

        list.add(pubDetailVO);
      }

      buildpubConfirmInfo(list, pubQueryDTO);
      listResult.setPubDetailVOList(list);
      listResult.setTotalCount(NumberUtils.toInt(pubQueryDTO.getTotalCount().toString()));
    }

    return listResult;
  }

  private void buildpubConfirmInfo(List<PubDetailVO> list, PubQueryDTO pubQueryDTO) {
    List<PubAssignLogPO> pubAssignList = pubAssignLogService.queryAllPubAssignLogByIds(pubQueryDTO);
    if (pubAssignList != null && pubAssignList.size() > 0) {
      for (PubDetailVO info : list) {
        for (PubAssignLogPO pubAssign : pubAssignList) {
          if (pubAssign.getPdwhPubId().longValue() == info.getPubId().longValue()) {
            info.setPubConfirmId(pubAssign.getId());
            Integer confirmResult = pubAssign.getConfirmResult();
            if ((pubAssign.getStatus() !=null && pubAssign.getStatus() ==1)
                || ( confirmResult != null && confirmResult != 0)) {
              info.setStatus(1);// 已经确认
                                // ，或者拒绝的成果直接返回，正常结果不存在该字段
            } else {
              info.setStatus(0);
            }
            break;
          }
        }
      }
    }
  }

  public void buildPdwhfulltext(List<PubInfo> list, PubQueryDTO pubQueryDTO) {
    List<PdwhPubFullTextPO> fulltextList = pdwhPubFullTextService.queryPdwhFullTextByIds(pubQueryDTO);
    if (fulltextList != null && fulltextList.size() > 0) {
      for (PubInfo info : list) {
        for (PdwhPubFullTextPO pdwhPubFullTextPO : fulltextList) {
          if (info.getPubId().longValue() == pdwhPubFullTextPO.getPdwhPubId().longValue()) {
            // 不是短地址链接
            String fullTextDownloadUrl = fileDownUrlService.getDownloadUrl(FileTypeEnum.PDWH_FULLTEXT, info.getPubId());
            info.setFullTextDownloadUrl(fullTextDownloadUrl);
            info.setFullTextFieId(pdwhPubFullTextPO.getFileId());
            String fullTextImgUrl =
                StringUtils.isNotBlank(pdwhPubFullTextPO.getThumbnailPath()) ? pdwhPubFullTextPO.getThumbnailPath()
                    : pdwhPubFullTextPO.getSourceFulltextUrl();
            info.setFullTextImgUrl(fullTextImgUrl);
            info.setHasFulltext(1);
            break;
          }
        }
        // 设置默认图片
        if (info.getHasFulltext().intValue() == 0) {
          info.setFullTextImgUrl(V8pubConst.PUB_DEFAULT_NOT_FULLTEXT_IMG_NEW);
        }

      }
    }
  }

  /**
   * 构建个基准库成果的短地址
   * 
   * @param list
   * @param pubIdList
   */
  public void buildPdwhPubIndexUrl(List<PubInfo> list, List<Long> pubIdList) {
    List<PdwhPubIndexUrl> urlList = pdwhPubIndexUrlDao.getByIds(pubIdList);
    if (urlList != null && list != null) {
      for (PubInfo info : list) {
        for (PdwhPubIndexUrl pubIndexUrl : urlList) {
          if (pubIndexUrl.getPubId().longValue() == info.getPubId().longValue()
              && StringUtils.isNotBlank(pubIndexUrl.getPubIndexUrl())) {
            String indexUrl = this.domainscm + "/" + ShortUrlConst.S_TYPE + "/" + pubIndexUrl.getPubIndexUrl();
            info.setPubIndexUrl(indexUrl);
            break;
          }
        }
        // 默认的url
        /*
         * if (StringUtils.isBlank(info.getPubIndexUrl())) { String url = this.domainscm +
         * "/pubweb/details/showpdwh?des3Id=" + Des3Utils.encodeToDes3(info.getPubId().toString());
         * info.setPubIndexUrl(url); }
         */

      }
    }
  }
}
