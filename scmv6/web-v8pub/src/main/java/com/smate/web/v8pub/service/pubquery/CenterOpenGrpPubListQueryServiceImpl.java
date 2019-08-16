package com.smate.web.v8pub.service.pubquery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.smate.core.base.utils.constant.ShortUrlConst;
import com.smate.web.v8pub.dao.sns.group.GrpPubIndexUrlDAO;
import com.smate.web.v8pub.po.sns.group.GrpPubIndexUrlPO;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.pub.vo.PubInfo;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.web.v8pub.consts.V8pubConst;
import com.smate.web.v8pub.po.PubPO;
import com.smate.web.v8pub.po.sns.PubSnsPO;
import com.smate.web.v8pub.po.sns.group.GroupPubPO;
import com.smate.web.v8pub.service.query.PubQueryDTO;
import com.smate.web.v8pub.service.sns.GroupPubService;
import com.smate.web.v8pub.service.sns.PubSnsService;
import com.smate.web.v8pub.vo.PubListResult;

/**
 * center-open 系统查询群组成果列表
 * 
 * 删除的成果 默认也要返回****************************别忘记
 * 
 * 获取基本信息
 * 
 * @param searchGrpId
 * @param pubType 4,3,5
 * @param pageNo 默认1
 * @param pageSize 默认10
 * @param pubUpdateDate
 * 
 * 
 * @author aijiangbin
 * @date 2018年8月15日
 */

@Transactional(rollbackFor = Exception.class)
public class CenterOpenGrpPubListQueryServiceImpl extends AbstractPubQueryService {

  @Resource
  private PubSnsService pubSnsService;
  @Resource
  private GroupPubService groupPubService;
  @Resource
  private GrpPubIndexUrlDAO grpPubIndexUrlDAO;

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
      List<PubInfo> list = new ArrayList<>(pubList.size() + 1);
      for (PubPO pubPO : pubList) {
        PubSnsPO pubSnsPO = (PubSnsPO) pubPO;
        PubInfo pubInfo = new PubInfo();
        pubInfo.setPubId(pubSnsPO.getPubId());
        pubInfo.setDes3PubId(Des3Utils.encodeToDes3(pubSnsPO.getPubId().toString()));
        pubInfo.setTitle(pubPO.getTitle());
        pubInfo.setBriefDesc(pubPO.getBriefDesc());
        pubInfo.setAuthorNames(pubPO.getAuthorNames());
        GroupPubPO groupPubPO = groupPubService.getGrpPub(pubQueryDTO.getSearchGrpId(), pubSnsPO.getPubId());
        pubInfo.setStatus(groupPubPO.getStatus());
        GrpPubIndexUrlPO pubIndexUrlPO =
            grpPubIndexUrlDAO.findByGrpIdAndPubId(pubQueryDTO.getSearchGrpId(), pubSnsPO.getPubId());
        if (pubIndexUrlPO != null) {
          pubInfo.setPubIndexUrl(this.domainscm + "/" + ShortUrlConst.B_TYPE + "/" + pubIndexUrlPO.getPubIndexUrl());
        }
        pubInfo.setRelevance(groupPubPO.getRelevance());
        pubInfo.setLabeled(groupPubPO.getLabeled());
        pubIdList.add(pubSnsPO.getPubId());
        list.add(pubInfo);
      }
      /**
       * 构建群组成果短地址在 pubInfo里面，所以传递list对象
       */
      buildGrpPubIndexUrl(list, pubQueryDTO.getSearchGrpId());
      listResult.setResultList(list);
      listResult.setTotalCount(NumberUtils.toInt(pubQueryDTO.getTotalCount().toString()));
    }

    return listResult;
  }

}
