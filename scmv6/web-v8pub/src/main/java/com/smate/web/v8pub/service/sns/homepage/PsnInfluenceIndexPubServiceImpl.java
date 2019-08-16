package com.smate.web.v8pub.service.sns.homepage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smate.core.base.pub.dao.PubSnsPublicDAO;
import com.smate.core.base.pub.vo.PubInfo;
import com.smate.core.base.utils.common.HtmlUtils;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.v8pub.restTemp.service.PubRestemplateService;
import com.smate.web.v8pub.dao.sns.PubSnsDAO;
import com.smate.web.v8pub.po.sns.PubSnsPO;
import com.smate.web.v8pub.service.sns.PsnStatisticsService;
import com.smate.web.v8pub.vo.PsnInfluencePubVO;

@Service
@Transactional(rollbackOn = Exception.class)
public class PsnInfluenceIndexPubServiceImpl implements PsnInfluenceIndexPubService {
  @Autowired
  private PsnStatisticsService psnStatisticsService;
  @Autowired
  private PubSnsDAO pubSnsDAO;
  @Autowired
  private PubSnsPublicDAO pubSnsPublicDAO;
  @Autowired
  private PubRestemplateService pubRestemplateService;

  @Override
  public void pubHindexShow(PsnInfluencePubVO pubVo) throws Exception {
    if (pubVo.gethIndex() == null || pubVo.gethIndex() == 0) {
      Map<String, Object> hindexMap = pubSnsPublicDAO.findPsnHindex(pubVo.getPsnId());
      pubVo.sethIndex(NumberUtils.toInt(hindexMap.get("HINDEX").toString()));
    }
    List<PubSnsPO> pubHindexList = pubSnsDAO.findPubListForHindexImprove(pubVo);
    List<Long> exIds = new ArrayList<Long>();
    int max = 4;
    if (CollectionUtils.isNotEmpty(pubHindexList)) {
      int count = pubHindexList.size();
      if (count < max) {
        for (PubSnsPO pub : pubHindexList) {
          exIds.add(pub.getPubId());
        }
        List<PubSnsPO> pubHindexList2 = pubSnsDAO.findPubListByYearAndCite(pubVo, max - count, exIds);
        if (CollectionUtils.isNotEmpty(pubHindexList2)) {
          pubHindexList.addAll(pubHindexList2);
        }
      }
    } else {
      pubHindexList = pubSnsDAO.findPubListByYearAndCite2(pubVo, max);
    }
    // 组装成果数据-查看他人/本人 成果列表
    List<Long> pubIds = pubHindexList.stream().map(PubSnsPO::getPubId).collect(Collectors.toList());
    List<PubInfo> pubInfoList = restTemplatePost(pubIds, SecurityUtils.getCurrentUserId());
    pubVo.setPubHindexList(sortIndexPubInfo(pubInfoList, pubIds));
  }

  private List<PubInfo> sortIndexPubInfo(List<PubInfo> pubInfoList, List<Long> pubIds) {
    if (pubInfoList != null) {
      Collections.sort(pubInfoList, new Comparator<PubInfo>() {
        @Override
        public int compare(PubInfo p1, PubInfo p2) {
          int diff = p1.getCitedTimes() - p2.getCitedTimes();
          if (diff > 0) {
            return -1;
          } else if (diff < 0) {
            return 1;
          }
          return 0; // 相等为0
        }
      });
      for (PubInfo pub : pubInfoList) {
        pub.setAuthorNamesHtml(HtmlUtils.Html2Text(pub.getAuthorNames()));
      }
    }
    return pubInfoList;
  }

  /**
   * 查找个人代表成果
   * 
   * @param psnId
   * @return
   * @throws IOException
   * @throws JsonMappingException
   * @throws JsonParseException
   */
  private List<PubInfo> restTemplatePost(List<Long> pubIds, Long psnId) throws Exception {
    String pubJson = pubRestemplateService.pubListQueryByPubIds(pubIds, psnId);
    Map<String, Object> resultMap = JacksonUtils.jsonToMap(pubJson);
    List<PubInfo> list = new ArrayList<>();
    if (resultMap.get("resultList") != null) {
      list = new ObjectMapper().readValue(JacksonUtils.jsonObjectSerializer(resultMap.get("resultList")),
          new TypeReference<List<PubInfo>>() {});
    }
    return list;
  }
}
