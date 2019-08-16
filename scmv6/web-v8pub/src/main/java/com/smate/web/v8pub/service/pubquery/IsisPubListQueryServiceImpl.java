package com.smate.web.v8pub.service.pubquery;

import com.smate.core.base.psn.service.psnpub.PsnPubService;
import com.smate.core.base.pub.vo.PubDetailVO;
import com.smate.web.v8pub.consts.V8pubConst;
import com.smate.web.v8pub.dao.sns.pubtype.ConstPubTypeDao;
import com.smate.web.v8pub.dom.sns.PubSnsDetailDOM;
import com.smate.web.v8pub.po.PubPO;
import com.smate.web.v8pub.po.pubType.ConstPubType;
import com.smate.web.v8pub.po.sns.PubSnsPO;
import com.smate.web.v8pub.service.query.PubQueryDTO;
import com.smate.web.v8pub.service.sns.PubSnsDetailService;
import com.smate.web.v8pub.service.sns.PubSnsService;
import com.smate.web.v8pub.vo.PubListResult;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * center-open 系统查询成果列表
 * 
 * 删除的成果 默认也要返回
 * 
 * 获取基本信息
 * 
 *
 * @author aijiangbin
 * @date 2018年8月15日
 */

@Transactional(rollbackFor = Exception.class)
public class IsisPubListQueryServiceImpl extends AbstractPubQueryService {
  private org.slf4j.Logger logger = LoggerFactory.getLogger(getClass());

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
    if (pubQueryDTO.getSearchPsnId() == null || pubQueryDTO.getSearchPsnId() == 0L) {
      map.put(V8pubConst.RESULT, V8pubConst.ERROR);
      map.put(V8pubConst.ERROR_MSG, "查询的人员psnId不能为空");
      return map;
    }
    return null;
  }

  @Override
  public void queryPubs(PubQueryDTO pubQueryDTO) {
    pubSnsService.queryPubListForOpen(pubQueryDTO);

  }

  @Override
  public PubListResult assembleData(PubQueryDTO pubQueryDTO) {
    PubListResult listResult = new PubListResult();
    List<PubPO> pubList = pubQueryDTO.getPubList();
    if (pubList != null && pubList.size() > 0) {
      List<PubDetailVO> list = new ArrayList<>(pubQueryDTO.getPageSize() * 2 + 1);
      for (PubPO pubPO : pubList) {
        PubSnsPO pubSnsPO = (PubSnsPO) pubPO;

        PubDetailVO pubDetailVO = new PubDetailVO();
        PubSnsDetailDOM detailDOM = pubSnsDetailService.getByPubId(pubSnsPO.getPubId());
        if (detailDOM == null) {
          logger.error("个人成果详情为空detailDOM，pubId="+pubSnsPO.getPubId());
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
        pubDetailVO.setPublishDate(detailDOM.getPublishDate());
        pubDetailVO.setPubOwnerPsnId(pubSnsService.queryPubPsnId(detailDOM.getPubId()));
        pubDetailVO.setCountryId(detailDOM.getCountryId());
        pubDetailVO.setSourceDbId(detailDOM.getSrcDbId());
        pubDetailVO.setPublishDate(detailDOM.getPublishDate());
        pubDetailVO.setPublishYear(pubSnsPO.getPublishYear());
        pubDetailVO.setPublishMonth(pubSnsPO.getPublishMonth());
        pubDetailVO.setPublishDay(pubSnsPO.getPublishDay());
        pubDetailVO.setFundInfo(detailDOM.getFundInfo());
        pubDetailVO.setCitations(detailDOM.getCitations());
        pubDetailVO.setDoi(detailDOM.getDoi());
        pubDetailVO.setSrcFulltextUrl(detailDOM.getSrcFulltextUrl());
        pubDetailVO.setOrganization(detailDOM.getOrganization());
        pubDetailVO.setAuthorNames(detailDOM.getAuthorNames());
        pubDetailVO.setAuthorName(detailDOM.getAuthorNames());


        buildCountryRegionId(pubDetailVO);

        buildPubMembersInfo(pubDetailVO, detailDOM);

        buildSnsPubIndexUrl(pubDetailVO, detailDOM);

        pubDetailVO.setSrcFulltextUrl(detailDOM.getSrcFulltextUrl());

        buildPubFulltext(pubDetailVO, detailDOM);

        buildPubSituation(pubDetailVO, detailDOM);// 构建收录来源

        int status = psnPubService.findPsnPubStatus(pubSnsPO.getPubId());
        pubDetailVO.setStatus(status);
        list.add(pubDetailVO);
      }
      listResult.setPubDetailVOList(list);
    }
    listResult.setTotalCount(
        pubQueryDTO.getTotalCount() == null ? 0 : NumberUtils.toInt(pubQueryDTO.getTotalCount().toString()));
    return listResult;
  }

}
