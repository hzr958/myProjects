package com.smate.web.v8pub.service.pubquery;

import com.smate.core.base.psn.service.psnpub.PsnPubService;
import com.smate.core.base.pub.vo.PubInfo;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.web.v8pub.consts.V8pubConst;
import com.smate.web.v8pub.po.PubPO;
import com.smate.web.v8pub.po.sns.PubKeywordsPO;
import com.smate.web.v8pub.po.sns.PubSnsPO;
import com.smate.web.v8pub.po.sns.group.GroupPubPO;
import com.smate.web.v8pub.service.query.PubQueryDTO;
import com.smate.web.v8pub.service.sns.GroupPubService;
import com.smate.web.v8pub.service.sns.PubKeywordsService;
import com.smate.web.v8pub.service.sns.PubSnsService;
import com.smate.web.v8pub.vo.PubListResult;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 通过pubId 查询SNS 库的成果
 * 
 * @author aijiangbin
 * @date 2018年8月14日
 */
@Transactional(rollbackFor = Exception.class)
public class PubQueryByPubIdServiceImpl extends AbstractPubQueryService {

  @Resource
  private PubSnsService pubSnsService;
  @Resource
  private PsnPubService psnPubService;
  @Autowired
  private GroupPubService groupPubService;
  @Resource
  private PubKeywordsService pubKeywordsService;

  @Override
  public Map<String, Object> checkData(PubQueryDTO pubQueryDTO) {
    Map<String, Object> map = new HashMap<>();
    if (pubQueryDTO.getSearchPubId() == null || pubQueryDTO.getSearchPubId() == 0L) {
      map.put(V8pubConst.RESULT, V8pubConst.ERROR);
      map.put(V8pubConst.ERROR_MSG, "查询的成果的searchPubId不能为空");
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
        pubIdList.add(pubSnsPO.getPubId());
        pubInfo.setBriefDesc(pubSnsPO.getBriefDesc());
        pubInfo.setAuthorNames(pubSnsPO.getAuthorNames());
        pubInfo.setGmtModified(pubSnsPO.getGmtModified());
        pubInfo.setGmtModifiedStr(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(pubSnsPO.getGmtModified()));
        setOwnerId(pubSnsPO, pubInfo);
        buildPubKeyWords(pubSnsPO, pubInfo);
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
    }

    return listResult;
  }

  private void setOwnerId(PubSnsPO pubSnsPO, PubInfo pubInfo) {
    Long pubOwnerId = psnPubService.getPubOwnerId(pubSnsPO.getPubId());
    int pubStatus = 1;
    if (NumberUtils.isNullOrZero(pubOwnerId)) {
      GroupPubPO groupPub = groupPubService.getByPubId(pubSnsPO.getPubId());
      if (groupPub != null) {
        pubOwnerId = groupPub.getOwnerPsnId();
        pubStatus = groupPub.getStatus();
      }
    } else {
      pubStatus = psnPubService.findPsnPubStatus(pubSnsPO.getPubId());
    }
    pubInfo.setStatus(pubStatus);
    pubInfo.setOwnerPsnId(pubOwnerId);
  }

  private void buildPubKeyWords(PubSnsPO pubSnsPO, PubInfo pubInfo) {
    List<PubKeywordsPO> keyWords = pubKeywordsService.getByPubId(pubSnsPO.getPubId());
    if (CollectionUtils.isNotEmpty(keyWords)) {
      String pubKeyWords = "";
      for (PubKeywordsPO pubKeywordsPO : keyWords) {
        pubKeyWords = pubKeyWords + pubKeywordsPO.getPubKeyword() + ";";
      }
      pubKeyWords = pubKeyWords.substring(0, pubKeyWords.length() - 1);
      pubInfo.setPubkeyWords(pubKeyWords);
    }
  }

}
