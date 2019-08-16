package com.smate.web.v8pub.service.pubquery;

import com.smate.core.base.psn.service.psnpub.PsnPubService;
import com.smate.core.base.pub.vo.PubInfo;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.web.v8pub.consts.V8pubConst;
import com.smate.web.v8pub.po.PubPO;
import com.smate.web.v8pub.po.sns.PubSnsPO;
import com.smate.web.v8pub.service.query.PubQueryDTO;
import com.smate.web.v8pub.service.sns.PubSnsService;
import com.smate.web.v8pub.vo.PubListResult;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 通过pubIds 和 psnId 查询最近更新的的一条成果成果
 * 
 * @author aijiangbin
 * @date 2018年8月20日
 */
@Transactional(rollbackFor = Exception.class)
public class QueryLastUpdatePubByPubIdsServiceImpl extends AbstractPubQueryService {

  @Resource
  private PubSnsService pubSnsService;
  @Resource
  private PsnPubService psnPubService;

  @Override
  public Map<String, Object> checkData(PubQueryDTO pubQueryDTO) {
    Map<String, Object> map = new HashMap<>();
    if (pubQueryDTO.getSearchPubIdList() == null || pubQueryDTO.getSearchPubIdList().size() == 0) {
      map.put(V8pubConst.RESULT, V8pubConst.ERROR);
      map.put(V8pubConst.ERROR_MSG, "查询的成果的searchPubIdList不能为空");
      return map;
    }
    if (pubQueryDTO.getSearchPsnId() == null && pubQueryDTO.getSearchPsnId() == 0L) {
      map.put(V8pubConst.RESULT, V8pubConst.ERROR);
      map.put(V8pubConst.ERROR_MSG, "查询的成果的searchPsnId不能为空");
      return map;
    }
    return null;
  }

  @Override
  public void queryPubs(PubQueryDTO pubQueryDTO) {
    // 如果ids 集合大于1000 需要截断
    if (pubQueryDTO.getSearchPubIdList().size() > 1000) {
      List<Long> subList = pubQueryDTO.getSearchPubIdList().subList(0, 1000);
      pubQueryDTO.setSearchPubIdList(subList);
    }
    pubSnsService.queryLastUpdatePubByPubIds(pubQueryDTO);
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
        pubInfo.setDes3PubId(Des3Utils.encodeToDes3(pubInfo.getPubId().toString()));
        pubInfo.setTitle(pubPO.getTitle());
        pubIdList.add(pubSnsPO.getPubId());
        pubInfo.setBriefDesc(pubSnsPO.getBriefDesc());
        pubInfo.setGmtModified(pubSnsPO.getGmtModified());
        pubInfo.setAuthorNames(pubSnsPO.getAuthorNames());
        list.add(pubInfo);
      }
      listResult.setResultList(list);
    }

    return listResult;
  }

}
