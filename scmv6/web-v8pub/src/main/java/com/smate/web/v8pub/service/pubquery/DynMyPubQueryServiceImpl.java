package com.smate.web.v8pub.service.pubquery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.pub.dao.PubIndexUrlDao;
import com.smate.core.base.pub.model.PubIndexUrl;
import com.smate.core.base.pub.vo.PubInfo;
import com.smate.core.base.utils.constant.ShortUrlConst;
import com.smate.web.v8pub.consts.V8pubConst;
import com.smate.web.v8pub.po.PubPO;
import com.smate.web.v8pub.po.sns.PubSnsPO;
import com.smate.web.v8pub.service.query.PubQueryDTO;
import com.smate.web.v8pub.service.sns.GroupPubService;
import com.smate.web.v8pub.service.sns.PubSnsService;
import com.smate.web.v8pub.vo.PubListResult;

/**
 * 个人动态，群组动态 我的成果列表查询服务
 * 
 * @author aijiangbin
 * @date 2018年7月17日
 */

@Transactional(rollbackFor = Exception.class)
public class DynMyPubQueryServiceImpl extends AbstractPubQueryService {

  @Resource
  private PubSnsService pubSnsService;
  @Resource
  private PubIndexUrlDao pubIndexUrlDao;
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
    return null;
  }

  @Override
  public void queryPubs(PubQueryDTO pubQueryDTO) {
    pubSnsService.queryDynMyPub(pubQueryDTO);
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
        pubInfo.setBriefDesc(pubPO.getBriefDesc());
        pubInfo.setAuthorNames(pubPO.getAuthorNames());
        if (pubSnsService.queryPubPsnId(pubSnsPO.getPubId()) != null) {
          pubInfo.setOwnerPsnId(pubSnsService.queryPubPsnId(pubSnsPO.getPubId()));
        } else if (groupPubService.getByPubId(pubSnsPO.getPubId()) != null) {
          pubInfo.setOwnerPsnId(groupPubService.getByPubId(pubSnsPO.getPubId()).getOwnerPsnId());
        }
        pubIdList.add(pubSnsPO.getPubId());
        list.add(pubInfo);
      }
      // 成果短地址
      buildPubIndexUrl(list, pubIdList);
      listResult.setResultList(list);
      listResult.setTotalCount(NumberUtils.toInt(pubQueryDTO.getTotalCount().toString()));
    }

    return listResult;
  }

  public void buildPubIndexUrl(List<PubInfo> list, List<Long> pubIdList) {
    if (list != null && list.size() > 0 && pubIdList != null && pubIdList.size() > 0) {
      List<PubIndexUrl> urlList = pubIndexUrlDao.findPubIndexUrl(pubIdList);
      for (PubInfo info : list) {
        for (PubIndexUrl pubIndexUrl : urlList) {
          if (pubIndexUrl.getPubId().longValue() == info.getPubId().longValue()) {
            String indexUrl = this.domainscm + "/" + ShortUrlConst.A_TYPE + "/" + pubIndexUrl.getPubIndexUrl();
            info.setPubIndexUrl(indexUrl);
            break;
          }
        }

      }
    }
  }

}
