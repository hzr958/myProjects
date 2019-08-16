package com.smate.web.v8pub.service.pubquery;

import com.smate.core.base.psn.service.psnpub.PsnPubService;
import com.smate.core.base.pub.vo.PubInfo;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.web.v8pub.consts.V8pubConst;
import com.smate.web.v8pub.po.PubPO;
import com.smate.web.v8pub.po.sns.PubSnsPO;
import com.smate.web.v8pub.service.query.PubQueryDTO;
import com.smate.web.v8pub.service.sns.GroupPubService;
import com.smate.web.v8pub.service.sns.PubSnsService;
import com.smate.web.v8pub.vo.PubListResult;
import org.apache.commons.lang.StringUtils;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 群组导入成员成果列表
 * 
 * @author aijiangbin
 * @date 2018年7月17日
 */
@Transactional(rollbackFor = Exception.class)
public class ImportMemberPubListQueryServiceImpl extends AbstractPubQueryService {
  @Resource
  private PubSnsService pubSnsService;
  @Resource
  private PsnPubService psnPubService;
  @Resource
  private GroupPubService groupPubService;

  @Override
  public Map<String, Object> checkData(PubQueryDTO pubQueryDTO) {
    Map<String, Object> map = new HashMap<>();
    if (pubQueryDTO.getSearchPsnId() == null || pubQueryDTO.getSearchPsnId() == 0L) {
      map.put(V8pubConst.RESULT, V8pubConst.ERROR);
      map.put(V8pubConst.ERROR_MSG, "查询的人员psnId不能为空");
      return map;
    }
    if (pubQueryDTO.getSearchGrpId() == null || pubQueryDTO.getSearchGrpId() == 0L) {
      map.put(V8pubConst.RESULT, V8pubConst.ERROR);
      map.put(V8pubConst.ERROR_MSG, "查询的群组的grpId不能为空");
      return map;
    }
    return null;
  }

  @Override
  public void queryPubs(PubQueryDTO pubQueryDTO) {
    // 这是是公开的
    // pubQueryDTO.setSearchSelfPub(false);
    if(StringUtils.isBlank(pubQueryDTO.getOrderBy())){
      pubQueryDTO.setOrderBy("importMemberPubOrder");
    }
    pubSnsService.queryPubList(pubQueryDTO);
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
        pubInfo.setTitle(pubPO.getTitle());
        pubInfo.setBriefDesc(pubPO.getBriefDesc());
        pubInfo.setAuthorNames(pubPO.getAuthorNames());
        // 是否存在群组成果
        pubInfo.setExistGrpPub(groupPubService.existGrpPub(pubQueryDTO.getSearchGrpId(), pubSnsPO.getPubId()));
        pubInfo.setOwnerPsnId(psnPubService.getPubOwnerId(pubSnsPO.getPubId()));
        pubInfo.setExistRepGrpPub(pubSnsService.existRepGrpPub(pubPO.getTitle(), pubQueryDTO.getSearchGrpId()));// 是否存在重复的成果,1=是
        pubInfo.setUpdateMark(pubPO.getUpdateMark());
        pubIdList.add(pubInfo.getPubId());
        list.add(pubInfo);
      }
      // 成果短地址
      buildSnsPubIndexUrl(list, pubIdList);
      // 全文
      Long psnId = pubQueryDTO.getPsnId();
      if (!NumberUtils.isNullOrZero(psnId)) {
        buildSnsPubFulltext(list, pubIdList, psnId);
      } else {
        buildSnsPubFulltext(list, pubIdList);
      }
      listResult.setResultList(list);
      listResult.setTotalCount(NumberUtils.toInt(pubQueryDTO.getTotalCount().toString()));
    }
    return listResult;
  }
}
