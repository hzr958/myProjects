package com.smate.web.v8pub.service.findduplicate.pub;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.codehaus.plexus.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.core.base.pub.util.DisposeDes3IdUtils;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.web.v8pub.enums.PubHandlerStatusEnum;
import com.smate.web.v8pub.exception.DuplicateCheckParameterException;
import com.smate.web.v8pub.exception.ServiceException;
import com.smate.web.v8pub.po.sns.group.GroupPubPO;
import com.smate.web.v8pub.service.findduplicate.DuplicateCheckHandlerBase;
import com.smate.web.v8pub.service.findduplicate.PubDuplicateDTO;
import com.smate.web.v8pub.service.sns.PubDuplicateService;

/**
 * 群组成果查重处理 原逻辑暂时没有群组成果查重
 * 
 * @author YJ
 *
 *         2018年8月18日
 */
public class GroupPubDuplicateHandler extends DuplicateCheckHandlerBase {

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private PubDuplicateService pubDuplicateService;

  @Override
  public void checkParameter(PubDuplicateDTO dup) throws DuplicateCheckParameterException {
    // TODO Auto-generated method stub

  }

  @Override
  public Map<String, String> handleDup(PubDuplicateDTO dup) throws ServiceException {

    Map<String, String> map = new HashMap<String, String>();
    try {
      List<Long> ids = new ArrayList<>();
      dup.psnId = DisposeDes3IdUtils.disposeDes3Id(dup.psnId, dup.des3PsnId);
      if (!NumberUtils.isNullOrZero(dup.psnId)) {
        Set<Long> dupPubIds = getDupPub(dup);
        if (CollectionUtils.isNotEmpty(dupPubIds)) {
          for (Long pubId : dupPubIds) {
            GroupPubPO groupPubPO = pubDuplicateService.getGroupDupPub(dup.groupId, pubId);
            if (groupPubPO != null) {
              map.put("msg", String.valueOf(pubId));
              ids.add(pubId);
            }
          }
        }
        if (CollectionUtils.isNotEmpty(ids)) {
          String list = ids.stream().map(pubId -> String.valueOf(pubId)).collect(Collectors.joining(","));
          map.put("msgList", list);
        }
        map.put("status", PubHandlerStatusEnum.SUCCESS.getValue());
      }
      return map;
    } catch (Exception e) {
      logger.error("群组成果查重失败！", e);
      throw new ServiceException(e);
    }
  }

  private Set<Long> getDupPub(PubDuplicateDTO dup) {
    Set<Long> dupPubIds = new HashSet<>();
    if (StringUtils.isNotBlank(dup.publicationOpenNo) || StringUtils.isNotBlank(dup.applicationNo)) {
      List<Long> dupPatents = pubDuplicateService.dupByPatentInfo(dup.hashApplicationNo, dup.hashPublicationOpenNo);
      // 专利查重，只通过专利的申请号或者公开号进行查重，不走TPP的查重规则，此逻辑不允许随便改动
      dupPubIds.addAll(dupPatents);
      return dupPubIds;
    }

    if (NumberUtils.isNotNullOrZero(dup.hashDoi)) {
      List<Long> dupDois = pubDuplicateService.dupByDoi(dup.hashDoi);
      if (CollectionUtils.isNotEmpty(dupDois)) {
        dupPubIds.addAll(dupDois);
      }
    }

    if (NumberUtils.isNotNullOrZero(dup.hashSourceId)) {
      List<Long> dupSourceIds = pubDuplicateService.dupBySourceId(dup.hashSourceId);
      if (CollectionUtils.isNotEmpty(dupSourceIds)) {
        dupPubIds.addAll(dupSourceIds);
      }
    }

    if (StringUtils.isNotEmpty(dup.hashTP)) {
      List<Long> dupInfos = pubDuplicateService.dupByPubInfo(dup.hashTP, dup.hashTPP);
      if (CollectionUtils.isNotEmpty(dupInfos)) {
        dupPubIds.addAll(dupInfos);
      }
    }

    return dupPubIds;

  }
}
