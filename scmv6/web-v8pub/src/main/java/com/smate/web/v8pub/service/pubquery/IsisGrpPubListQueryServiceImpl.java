package com.smate.web.v8pub.service.pubquery;

import com.smate.core.base.psn.service.psnpub.PsnPubService;
import com.smate.core.base.pub.vo.PubDetailVO;
import com.smate.core.base.utils.constant.ShortUrlConst;
import com.smate.web.v8pub.consts.V8pubConst;
import com.smate.web.v8pub.dao.sns.group.GrpPubIndexUrlDAO;
import com.smate.web.v8pub.dao.sns.pubtype.ConstPubTypeDao;
import com.smate.web.v8pub.dom.sns.PubSnsDetailDOM;
import com.smate.web.v8pub.po.PubPO;
import com.smate.web.v8pub.po.pubType.ConstPubType;
import com.smate.web.v8pub.po.sns.PubSnsPO;
import com.smate.web.v8pub.po.sns.group.GroupPubPO;
import com.smate.web.v8pub.po.sns.group.GrpPubIndexUrlPO;
import com.smate.web.v8pub.service.query.PubQueryDTO;
import com.smate.web.v8pub.service.sns.GroupPubService;
import com.smate.web.v8pub.service.sns.PubSnsDetailService;
import com.smate.web.v8pub.service.sns.PubSnsService;
import com.smate.web.v8pub.vo.PubListResult;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * center-open 系统查询群组成果列表
 * 
 * 删除的成果 默认也要返回****************************别忘记
 * 
 * 获取基本信息
 * 
 *
 * 
 * @author aijiangbin
 * @date 2018年8月15日
 */

@Transactional(rollbackFor = Exception.class)
public class IsisGrpPubListQueryServiceImpl extends AbstractPubQueryService {


  @Resource
  private GroupPubService groupPubService;
  @Resource
  private GrpPubIndexUrlDAO grpPubIndexUrlDAO;
  @Autowired
  private PubSnsDetailService pubSnsDetailService;
  @Autowired
  private ConstPubTypeDao constPubTypeDao;
  @Autowired
  private PsnPubService psnPubService;
  @Autowired
  private PubSnsService pubSnsService;

  @Override
  public Map<String, Object> checkData(PubQueryDTO pubQueryDTO) {
    Map<String, Object> map = new HashMap<>();
    if (pubQueryDTO.getSearchGrpId() == null || pubQueryDTO.getSearchGrpId() == 0L) {
      map.put(V8pubConst.RESULT, V8pubConst.ERROR);
      map.put(V8pubConst.ERROR_MSG, "查询的群组grpId不能为空");
      return map;
    }
    return null;
  }

  @Override
  public void queryPubs(PubQueryDTO pubQueryDTO) {
    pubSnsService.queryGrpPubListForOpen(pubQueryDTO);

  }

  @Override
  public PubListResult assembleData(PubQueryDTO pubQueryDTO) {
    PubListResult listResult = new PubListResult();
    List<Long> pubIdList = new ArrayList<>();
    List<PubPO> pubList = pubQueryDTO.getPubList();
    if (pubList != null && pubList.size() > 0) {
      List<PubDetailVO> list = new ArrayList<>(pubQueryDTO.getPageSize() * 2 + 1);
      for (PubPO pubPO : pubList) {
        PubSnsPO pubSnsPO = (PubSnsPO) pubPO;
        PubDetailVO pubDetailVO = new PubDetailVO();
        PubSnsDetailDOM detailDOM = pubSnsDetailService.getByPubId(pubSnsPO.getPubId());
        if (detailDOM == null) {
          continue;
        }
        pubDetailVO = buildPubTypeInfo(detailDOM);
        pubDetailVO.setPubId(pubSnsPO.getPubId());
        pubDetailVO.setUpdateMark(pubSnsPO.getUpdateMark());
        pubDetailVO.setPubType(detailDOM.getPubType());
        ConstPubType constPubType = constPubTypeDao.get(detailDOM.getPubType());
        pubDetailVO.setTypeName(constPubType != null ? constPubType.getZhName() : "");
        pubDetailVO.setTitle(detailDOM.getTitle());
        pubDetailVO.setSummary(detailDOM.getSummary());
        pubDetailVO.setBriefDesc(detailDOM.getBriefDesc());
        if (pubSnsPO != null) {
          pubDetailVO.setGmtCreate(pubSnsPO.getGmtCreate());
          pubDetailVO.setGmtModified(pubSnsPO.getGmtModified());
          pubDetailVO.setCitations(pubSnsPO.getCitations());
          pubDetailVO.setRecordFrom(pubSnsPO.getRecordFrom());
          pubDetailVO.setUpdateMark(pubSnsPO.getUpdateMark());
        }
        String key = detailDOM.getKeywords();
        pubDetailVO.setKeywords(key);
        pubDetailVO.setAuthorNames(pubSnsPO.getAuthorNames());
        pubDetailVO.setPublishDate(detailDOM.getPublishDate());
        pubDetailVO.setPubOwnerPsnId(pubSnsService.queryPubPsnId(detailDOM.getPubId()));
        pubDetailVO.setFundInfo(detailDOM.getFundInfo());
        pubDetailVO.setSourceDbId(detailDOM.getSrcDbId());
        pubDetailVO.setCitations(detailDOM.getCitations());
        pubDetailVO.setSrcFulltextUrl(detailDOM.getSrcFulltextUrl());
        pubDetailVO.setOrganization(detailDOM.getOrganization());
        pubDetailVO.setCountryId(detailDOM.getCountryId());
        pubDetailVO.setSourceDbId(detailDOM.getSrcDbId());
        pubDetailVO.setDoi(detailDOM.getDoi());
        pubDetailVO.setAuthorName(detailDOM.getAuthorNames());
        pubDetailVO.setPublishYear(pubSnsPO.getPublishYear());
        pubDetailVO.setPublishMonth(pubSnsPO.getPublishMonth());
        pubDetailVO.setPublishDay(pubSnsPO.getPublishDay());

        buildCountryRegionId(pubDetailVO);

        buildPubMembersInfo(pubDetailVO, detailDOM);

        pubDetailVO.setSrcFulltextUrl(detailDOM.getSrcFulltextUrl());

        buildPubFulltext(pubDetailVO, detailDOM);

        buildPubSituation(pubDetailVO, detailDOM);// 构建收录来源

        GroupPubPO groupPubPO = groupPubService.getGrpPub(pubQueryDTO.getSearchGrpId(), pubSnsPO.getPubId());
        pubDetailVO.setStatus(groupPubPO.getStatus());
        GrpPubIndexUrlPO pubIndexUrlPO =
            grpPubIndexUrlDAO.findByGrpIdAndPubId(pubQueryDTO.getSearchGrpId(), pubSnsPO.getPubId());
        if (pubIndexUrlPO != null) {
          pubDetailVO
              .setPubIndexUrl(this.domainscm + "/" + ShortUrlConst.B_TYPE + "/" + pubIndexUrlPO.getPubIndexUrl());
        }
        pubDetailVO.setRelevance(groupPubPO.getRelevance());
        pubDetailVO.setLabeled(groupPubPO.getLabeled());
        pubIdList.add(pubSnsPO.getPubId());
        list.add(pubDetailVO);
      }

      listResult.setPubDetailVOList(list);
      listResult.setTotalCount(NumberUtils.toInt(pubQueryDTO.getTotalCount().toString()));
    }

    return listResult;
  }

}
