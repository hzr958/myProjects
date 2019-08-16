package com.smate.web.v8pub.service.pubquery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.psn.service.psnpub.PsnPubService;
import com.smate.core.base.pub.vo.PubInfo;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.web.v8pub.consts.V8pubConst;
import com.smate.web.v8pub.po.PubPO;
import com.smate.web.v8pub.po.sns.PubSnsPO;
import com.smate.web.v8pub.service.query.PubQueryDTO;
import com.smate.web.v8pub.service.sns.PubSnsService;
import com.smate.web.v8pub.vo.PubListResult;

/**
 * center-open 系统查询成果列表
 * 
 * 删除的成果 默认也要返回
 * 
 * 获取基本信息
 * 
 * @param searchPsnId
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
public class CenterOpenPubListQueryServiceImpl extends AbstractPubQueryService {

  @Resource
  private PubSnsService pubSnsService;
  @Resource
  private PsnPubService psnPubService;

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
        pubInfo.setPubType(pubPO.getPubType());
        pubIdList.add(pubSnsPO.getPubId());
        if (pubQueryDTO.isQueryAll) {// 是否查询所有成果 true:查询所有
                                     // false:查询未被删除的成果
          int status = psnPubService.findPsnPubStatus(pubSnsPO.getPubId());
          pubInfo.setStatus(status);
        }
        list.add(pubInfo);
      }
      listResult.setResultList(list);
    }
    listResult.setTotalCount(
        pubQueryDTO.getTotalCount() == null ? 0 : NumberUtils.toInt(pubQueryDTO.getTotalCount().toString()));
    return listResult;
  }

}
