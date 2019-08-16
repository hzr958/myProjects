package com.smate.web.v8pub.service.findduplicate.pub;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
import com.smate.web.v8pub.service.findduplicate.DuplicateCheckHandlerBase;
import com.smate.web.v8pub.service.findduplicate.PubDuplicateDTO;
import com.smate.web.v8pub.service.sns.PubDuplicateService;

/**
 * 个人库成果查重处理
 * 
 * @author YJ
 *
 *         2018年8月18日
 */
public class PsnPubDuplicateHandler extends DuplicateCheckHandlerBase {

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private PubDuplicateService pubDuplicateService;

  @Override
  public void checkParameter(PubDuplicateDTO dup) throws DuplicateCheckParameterException {


  }

  @Override
  public Map<String, String> handleDup(PubDuplicateDTO dup) throws ServiceException {
    Map<String, String> map = new HashMap<String, String>();
    try {
      dup.psnId = DisposeDes3IdUtils.disposeDes3Id(dup.psnId, dup.des3PsnId);
      dup.pubId = DisposeDes3IdUtils.disposeDes3Id(dup.pubId, dup.des3PubId);
      if (!NumberUtils.isNullOrZero(dup.psnId)) {
        Set<Long> dupPubIds = getDupPub(dup);
        if (CollectionUtils.isNotEmpty(dupPubIds)) {
          map.put("msg", dupPubIds.iterator().next() + "");
          String list = dupPubIds.stream().map(pubId -> String.valueOf(pubId)).collect(Collectors.joining(","));
          map.put("msgList", list);
        }
        map.put("status", PubHandlerStatusEnum.SUCCESS.getValue());
      }
      return map;
    } catch (Exception e) {
      logger.error("个人成果查重失败！", e);
      throw new ServiceException(e);
    }
  }

  private Set<Long> getDupPub(PubDuplicateDTO dup) {
    Set<Long> dupPubIds = new HashSet<>();
    if (StringUtils.isNotBlank(dup.publicationOpenNo) || StringUtils.isNotBlank(dup.applicationNo)) {
      List<Long> dupPatents =
          pubDuplicateService.dupByPatentInfo(dup.hashApplicationNo, dup.hashPublicationOpenNo, dup.psnId, dup.pubId);
      // 专利查重，只通过专利的申请号或者公开号进行查重，不走TPP的查重规则，此逻辑不允许随便改动
      if (CollectionUtils.isEmpty(dupPatents)) {
        // 20190701 新增专利号和申请号查重不成功，再通过hashTPP进行查重的逻辑
        dupPatents = pubDuplicateService.dupByPubInfoNullAppNoAndOpenNo(dup.hashTP, dup.hashTPP, dup.psnId, dup.pubId);
      }
      dupPubIds.addAll(Optional.ofNullable(dupPatents).orElse(new ArrayList<Long>()));
      return dupPubIds;
    }

    // 标准类型，标准号standardNo必须要存在，因此只通过标准号进行查重
    if (StringUtils.isNotBlank(dup.standardNo)) {
      List<Long> dupStandards = pubDuplicateService.dupByStandardNo(dup.hashStandardNo, dup.psnId, dup.pubId);
      if (CollectionUtils.isEmpty(dupStandards)) {
        // 20190703 标准号查重不成功，再通过hashTPP进行查重的逻辑
        dupStandards = pubDuplicateService.dupByPubInfoNullStandardNo(dup.hashTP, dup.hashTPP, dup.psnId, dup.pubId);
      }
      dupPubIds.addAll(Optional.ofNullable(dupStandards).orElse(new ArrayList<Long>()));
      return dupPubIds;
    }

    // 软件著作权类型，登记号registerNo必须要存在，因此只通过登记号进行查重
    if (StringUtils.isNotBlank(dup.registerNo)) {
      List<Long> dupSoftwares = pubDuplicateService.dupByRegisterNo(dup.hashRegisterNo, dup.psnId, dup.pubId);
      if (CollectionUtils.isEmpty(dupSoftwares)) {
        // 20190703 登记号查重不成功，再通过hashTPP进行查重的逻辑
        dupSoftwares = pubDuplicateService.dupByPubInfoNullRegisterNo(dup.hashTP, dup.hashTPP, dup.psnId, dup.pubId);
      }
      dupPubIds.addAll(Optional.ofNullable(dupSoftwares).orElse(new ArrayList<Long>()));
      return dupPubIds;
    }

    if (StringUtils.isNotBlank(dup.doi)) {
      List<Long> dupDois = pubDuplicateService.dupByDoi(dup.hashDoi, dup.hashCleanDoi, dup.psnId, dup.pubId);
      dupPubIds.addAll(Optional.ofNullable(dupDois).orElse(new ArrayList<Long>()));
    }

    if (StringUtils.isNotBlank(dup.sourceId)) {
      List<Long> dupSourceIds = pubDuplicateService.dupBySourceId(dup.hashSourceId, dup.psnId, dup.pubId);
      dupPubIds.addAll(Optional.ofNullable(dupSourceIds).orElse(new ArrayList<Long>()));
    }

    if (StringUtils.isNotEmpty(dup.hashTP)) {
      List<Long> dupInfos = pubDuplicateService.dupByPubInfo(dup.hashTP, dup.hashTPP, dup.psnId, dup.pubId);
      dupPubIds.addAll(Optional.ofNullable(dupInfos).orElse(new ArrayList<Long>()));
    }

    return dupPubIds;
  }
  
}
