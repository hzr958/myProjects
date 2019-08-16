package com.smate.web.v8pub.service.pubquery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.file.service.FileDownloadUrlService;
import com.smate.core.base.pub.enums.PubSnsRecordFromEnum;
import com.smate.core.base.pub.vo.PubInfo;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.web.v8pub.consts.V8pubConst;
import com.smate.web.v8pub.po.PubPO;
import com.smate.web.v8pub.po.sns.PubSnsPO;
import com.smate.web.v8pub.po.sns.group.GroupPubPO;
import com.smate.web.v8pub.service.query.PubQueryDTO;
import com.smate.web.v8pub.service.sns.GroupPubService;
import com.smate.web.v8pub.service.sns.PubFullTextService;
import com.smate.web.v8pub.service.sns.PubSnsService;
import com.smate.web.v8pub.vo.PubListResult;

/**
 * 群组成果列表查询服务
 * 
 * @author aijiangbin
 * @date 2018年7月17日
 */

@Transactional(rollbackFor = Exception.class)
public class GrpPubListQueryServiceImpl extends AbstractPubQueryService {

  @Resource
  private PubSnsService pubSnsService;
  @Resource
  private GroupPubService groupPubService;

  @Resource
  private PubFullTextService pubFullTextService;
  // @Autowired
  // private PubFullTextReqBaseDao pubFTReqBaseDao;
  @Autowired
  private FileDownloadUrlService fileDownUrlService;

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
        pubInfo.setPublishYear(pubPO.getPublishYear());
        pubInfo.setAuthorNames(pubSnsPO.getAuthorNames());
        PubSnsRecordFromEnum recordFrom = pubSnsPO.getRecordFrom();
        pubInfo.setRecordFrom(recordFrom.getValue());
        // 成果拥有者？？？？TODO
        GroupPubPO groupPubPO = groupPubService.getByGrpIdAndPubId(pubQueryDTO.getSearchGrpId(), pubSnsPO.getPubId());
        pubInfo.setOwnerPsnId(groupPubPO.getOwnerPsnId());
        pubIdList.add(pubSnsPO.getPubId());
        pubInfo.setUpdateMark(pubSnsPO.getUpdateMark());
        list.add(pubInfo);
      }
      // 群组成果短地址
      buildGrpPubIndexUrl(list, pubQueryDTO.getSearchGrpId());
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
