package com.smate.web.v8pub.service.pubquery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.pub.vo.PubInfo;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.web.v8pub.consts.V8pubConst;
import com.smate.web.v8pub.dao.sns.group.GroupPubsDAO;
import com.smate.web.v8pub.po.PubPO;
import com.smate.web.v8pub.po.sns.PubSnsPO;
import com.smate.web.v8pub.service.query.PubQueryDTO;
import com.smate.web.v8pub.service.sns.PubSnsService;
import com.smate.web.v8pub.vo.PubListResult;

/**
 * 通过pubId 查询SNS 库的群组的成果
 * 
 * @author aijiangbin
 * @date 2018年8月14日
 */
@Transactional(rollbackFor = Exception.class)
public class GrpPubQueryByPubIdServiceImpl extends AbstractPubQueryService {

  @Resource
  private PubSnsService pubSnsService;
  @Resource
  private GroupPubsDAO groupPubsDAO;

  @Override
  public Map<String, Object> checkData(PubQueryDTO pubQueryDTO) {
    Map<String, Object> map = new HashMap<>();
    if (pubQueryDTO.getSearchPubId() == null || pubQueryDTO.getSearchPubId() == 0L) {
      map.put(V8pubConst.RESULT, V8pubConst.ERROR);
      map.put(V8pubConst.ERROR_MSG, "查询的成果的searchPubId不能为空");
      return map;
    }
    if (pubQueryDTO.getSearchGrpId() == null || pubQueryDTO.getSearchGrpId() == 0L) {
      map.put(V8pubConst.RESULT, V8pubConst.ERROR);
      map.put(V8pubConst.ERROR_MSG, "查询的成果的searchGrpId不能为空");
      return map;
    }
    return null;
  }

  @Override
  public void queryPubs(PubQueryDTO pubQueryDTO) {
    PubSnsPO pubSnsPO = pubSnsService.get(pubQueryDTO.getSearchPubId());
    List<PubPO> pubList = new ArrayList<>();
    pubQueryDTO.setPubList(pubList);
    if (pubSnsPO != null) {
      pubList.add(pubSnsPO);
    }

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
        pubInfo.setTitle(pubPO.getTitle());
        pubInfo.setBriefDesc(pubSnsPO.getBriefDesc());
        pubInfo.setAuthorNames(pubSnsPO.getAuthorNames());
        pubIdList.add(pubSnsPO.getPubId());

        Integer status = groupPubsDAO.findGrpPubsStatus(pubSnsPO.getPubId(), pubQueryDTO.getSearchGrpId());
        pubInfo.setPubHasDel(status == 1 ? true : false);
        list.add(pubInfo);
      }
      // 成果短地址
      buildGrpPubIndexUrl(list, pubQueryDTO.getSearchGrpId());
      // 全文
      Long psnId = pubQueryDTO.getPsnId();
      if (!NumberUtils.isNullOrZero(psnId)) {
        buildSnsPubFulltext(list, pubIdList, psnId);
      } else {
        buildSnsPubFulltext(list, pubIdList);
      }
      listResult.setResultList(list);
    }

    return listResult;
  }

}
