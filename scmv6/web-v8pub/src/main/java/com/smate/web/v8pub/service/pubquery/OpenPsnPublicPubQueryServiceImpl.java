package com.smate.web.v8pub.service.pubquery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.pub.vo.PubInfo;
import com.smate.web.v8pub.consts.V8pubConst;
import com.smate.web.v8pub.dom.PatentInfoBean;
import com.smate.web.v8pub.dom.PubSituationBean;
import com.smate.web.v8pub.dom.SoftwareCopyrightBean;
import com.smate.web.v8pub.dom.StandardInfoBean;
import com.smate.web.v8pub.dom.sns.PubSnsDetailDOM;
import com.smate.web.v8pub.po.PubPO;
import com.smate.web.v8pub.po.sns.PubSnsPO;
import com.smate.web.v8pub.service.query.PubQueryDTO;
import com.smate.web.v8pub.service.sns.PubSnsDetailService;
import com.smate.web.v8pub.service.sns.PubSnsService;
import com.smate.web.v8pub.vo.PubListResult;

@Transactional(rollbackFor = Exception.class)
public class OpenPsnPublicPubQueryServiceImpl extends AbstractPubQueryService {

  @Resource
  private PubSnsService pubSnsService;
  @Resource
  private PubSnsDetailService pubSnsDetailService;

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
    pubSnsService.queryPsnPublicPubForOpen(pubQueryDTO);

  }

  @Override
  public PubListResult assembleData(PubQueryDTO pubQueryDTO) {
    PubListResult listResult = new PubListResult();
    List<PubPO> pubList = pubQueryDTO.getPubList();
    if (pubList != null && pubList.size() > 0) {
      List<PubInfo> list = new ArrayList<>(pubList.size() + 1);
      for (PubPO pubPO : pubList) {
        PubSnsPO pubSnsPO = (PubSnsPO) pubPO;
        PubSnsDetailDOM detailDOM = pubSnsDetailService.getByPubId(pubSnsPO.getPubId());
        PubInfo pubInfo = new PubInfo();
        pubInfo.setPubId(pubSnsPO.getPubId());
        pubInfo.setTitle(pubPO.getTitle());
        pubInfo.setPubType(pubPO.getPubType());
        pubInfo.setPublishYear(pubPO.getPublishYear());
        pubInfo.setBriefDesc(pubPO.getBriefDesc());
        pubInfo.setAuthorNames(pubPO.getAuthorNames());
        pubInfo.setDOI(detailDOM.getDoi());
        pubInfo.setSrcDbId(detailDOM.getSrcDbId());
        if (pubPO.getPubType() == 5) {
          PatentInfoBean typeInfo = (PatentInfoBean) detailDOM.getTypeInfo();
          pubInfo.setApplicationNo(typeInfo.getApplicationNo());
          pubInfo.setPublicationOpenNo(typeInfo.getPublicationOpenNo());
        }
        if (pubPO.getPubType() == 12) {
          StandardInfoBean typeInfo = (StandardInfoBean) detailDOM.getTypeInfo();
          pubInfo.setStandardNo(typeInfo.getStandardNo());
        }
        if (pubPO.getPubType() == 13) {
          SoftwareCopyrightBean typeInfo = (SoftwareCopyrightBean) detailDOM.getTypeInfo();
          pubInfo.setRegisterNo(typeInfo.getRegisterNo());
        }
        // 将sourceId过去
        Set<PubSituationBean> sitList = detailDOM.getSituations();
        String sourceId = "";
        if (CollectionUtils.isNotEmpty(sitList)) {
          for (PubSituationBean sitBean : sitList) {
            if (sitBean.getSrcDbId().equals(detailDOM.getSrcDbId() + "")) {
              sourceId = sitBean.getSrcId();
            }
          }
        }
        pubInfo.setSourceId(sourceId);
        list.add(pubInfo);
      }
      listResult.setResultList(list);
      listResult.setTotalCount(NumberUtils.toInt(pubQueryDTO.getTotalCount().toString()));
    }
    if (pubQueryDTO.getIsAll() == 1) {
      long pubCount = pubSnsService.getPsnNotExistsResumePubCount(pubQueryDTO.getSearchPsnId());
      int sumCount =
          NumberUtils.toInt(pubQueryDTO.getTotalCount().toString()) + NumberUtils.toInt(String.valueOf(pubCount));
      listResult.setTotalCount(sumCount);
    }
    return listResult;
  }

}
