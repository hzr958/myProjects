package com.smate.web.v8pub.service.pubquery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.pub.enums.PubSnsRecordFromEnum;
import com.smate.core.base.pub.vo.PubInfo;
import com.smate.web.v8pub.consts.V8pubConst;
import com.smate.web.v8pub.po.PubPO;
import com.smate.web.v8pub.po.sns.PubSnsPO;
import com.smate.web.v8pub.service.query.PubQueryDTO;
import com.smate.web.v8pub.service.sns.PubSnsService;
import com.smate.web.v8pub.vo.PubListResult;

/**
 * 群组首页的成果列表 需要的参数：searchGrpId searchPrjPub=true 表示查询的是项目成果
 * 
 * @author aijiangbin
 * @date 2018年7月24日
 */

@Transactional(rollbackFor = Exception.class)
public class GrpHomepagePubQueryServiceImpl extends AbstractPubQueryService {

  @Resource
  private PubSnsService pubSnsService;

  @Override
  public Map<String, Object> checkData(PubQueryDTO pubQueryDTO) {
    Map<String, Object> map = new HashMap<>();
    if (pubQueryDTO.getSearchGrpId() == null || pubQueryDTO.getSearchGrpId() == 0L) {
      map.put(V8pubConst.RESULT, V8pubConst.ERROR);
      map.put(V8pubConst.ERROR_MSG, "群组ID不能为空");
      return map;
    }
    return null;
  }

  @Override
  public void queryPubs(PubQueryDTO pubQueryDTO) {
    pubQueryDTO.setOrderBy("citations");
    pubQueryDTO.setPageNo(1);
    pubQueryDTO.setPageSize(5);
    pubQueryDTO.setSearchCount(false);
    pubSnsService.queryGrpPubList(pubQueryDTO);

  }

  @Override
  public PubListResult assembleData(PubQueryDTO pubQueryDTO) {
    PubListResult listResult = new PubListResult();
    List<PubPO> pubList = pubQueryDTO.getPubList();
    List<Long> pubIdList = new ArrayList<>();
    if (pubList != null && pubList.size() > 0) {
      List<PubInfo> list = new ArrayList<>(pubList.size() + 1);
      for (PubPO pubPO : pubList) {
        PubSnsPO pubSnsPO = (PubSnsPO) pubPO;
        PubInfo pubInfo = new PubInfo();
        pubInfo.setPubId(pubSnsPO.getPubId());
        pubInfo.setTitle(pubSnsPO.getTitle());
        pubInfo.setBriefDesc(pubSnsPO.getBriefDesc());
        pubInfo.setAuthorNames(pubSnsPO.getAuthorNames());
        PubSnsRecordFromEnum recordFrom = pubSnsPO.getRecordFrom();
        pubInfo.setRecordFrom(recordFrom.getValue());
        pubIdList.add(pubSnsPO.getPubId());
        list.add(pubInfo);
      }
      listResult.setResultList(list);
      listResult.setTotalCount(list.size());
    }

    return listResult;
  }

}
