package com.smate.web.v8pub.service.findduplicate.pub;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.core.base.utils.string.StringUtils;
import com.smate.web.v8pub.enums.PubHandlerStatusEnum;
import com.smate.web.v8pub.exception.DuplicateCheckParameterException;
import com.smate.web.v8pub.exception.ServiceException;
import com.smate.web.v8pub.service.findduplicate.DuplicateCheckHandlerBase;
import com.smate.web.v8pub.service.findduplicate.PubDuplicateDTO;
import com.smate.web.v8pub.service.pdwh.PdwhPubDuplicateService;

/**
 * 基准库查重处理
 * 
 * @author YJ
 *
 *         2018年8月18日
 */
public class PdwhPubDuplicateHandler extends DuplicateCheckHandlerBase {

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private PdwhPubDuplicateService pdwhPubDuplicateService;

  @Override
  public void checkParameter(PubDuplicateDTO dup) throws DuplicateCheckParameterException {

  }

  @Override
  public Map<String, String> handleDup(PubDuplicateDTO dup) throws ServiceException {
    Map<String, String> map = new HashMap<String, String>();
    try {
      List<Long> dupPubIds = getDupPub(dup);
      map.put("status", PubHandlerStatusEnum.SUCCESS.getValue());
      if (CollectionUtils.isNotEmpty(dupPubIds)) {
        String ids = dupPubIds.stream().map(pubId -> String.valueOf(pubId)).collect(Collectors.joining(","));
        map.put("msgList", ids);
        map.put("msg", String.valueOf(dupPubIds.get(0)));
      }
      return map;
    } catch (Exception e) {
      logger.error("基准库查重异常", e);
      throw new ServiceException(e);
    }
  }

  private List<Long> getDupPub(PubDuplicateDTO dup) {

    if (StringUtils.isNotBlank(dup.publicationOpenNo) || StringUtils.isNotBlank(dup.applicationNo)) {
      List<Long> dupPubIds = pdwhPubDuplicateService.dupByPatentInfo(dup.hashApplicationNo, dup.hashPublicationOpenNo);
      // 专利查重，只通过专利的申请号或者公开号进行查重，不走TPP的查重规则，此逻辑不允许随便改动
      if (CollectionUtils.isEmpty(dupPubIds)) {
        // 20190703 如果查重不到，再走一次hashTPP的查重
        dupPubIds = pdwhPubDuplicateService.dupByPubInfoNullAppNoAndOpenNo(dup.hashTP, dup.hashTPP);
      }
      return dupPubIds;
    }

    if (StringUtils.isNotBlank(dup.doi)) {
      // doi查重修改，第一步：筛选出表中有doi的数据，进行查重
      List<Long> dupPubIds = pdwhPubDuplicateService.dupByNotNullDoi(dup.hashDoi, dup.hashCleanDoi);
      if (CollectionUtils.isEmpty(dupPubIds)) {
        // 第二步：与表里面有doi的数据没有匹配上的话，再去除掉表中doi有的数据，通过hashTPP和hashTP进行查重
        dupPubIds = pdwhPubDuplicateService.dupByPubInfoNullDoi(dup.hashTP, dup.hashTPP);
      }
      // 成果有DOI只通过DOI进行查重，不走下面的查重规则，此逻辑不允许随便改动
      return dupPubIds;
    }

    // 标准类型，标准号standardNo必须要存在，因此只通过标准号进行查重
    if (StringUtils.isNotBlank(dup.standardNo)) {
      List<Long> dupPubIds = pdwhPubDuplicateService.dupByStandardNo(dup.hashStandardNo);
      if (CollectionUtils.isEmpty(dupPubIds)) {
        // 20190703 如果查重不到，再走一次hashTPP的查重
        dupPubIds = pdwhPubDuplicateService.dupByPubInfoNullStandardNo(dup.hashTP, dup.hashTPP);
      }
      return dupPubIds;
    }

    // 软件著作权类型，登记号registerNo必须要存在，因此只通过登记号进行查重
    if (StringUtils.isNotBlank(dup.registerNo)) {
      List<Long> dupPubIds = pdwhPubDuplicateService.dupByRegisterNo(dup.hashRegisterNo);
      if (CollectionUtils.isEmpty(dupPubIds)) {
        // 20190703 如果查重不到，再走一次hashTPP的查重
        dupPubIds = pdwhPubDuplicateService.dupByPubInfoNullRegisterNo(dup.hashTP, dup.hashTPP);
      }
      return dupPubIds;
    }

    // souceId：有souceId的跟有sourceI的查重，没有的跟TPP查重
    if (StringUtils.isNotBlank(dup.sourceId)) {
      List<Long> dupPubIds = pdwhPubDuplicateService.dupBySourceId(dup.hashSourceId);
      if (CollectionUtils.isEmpty(dupPubIds)) {
        // 20190703 如果查重不到，再走一次hashTPP的查重
        dupPubIds = pdwhPubDuplicateService.dupByPubInfoNullSourceId(dup.hashTP, dup.hashTPP);
      }
      return dupPubIds;
    }

    if (StringUtils.isNotEmpty(dup.hashTP)) {
      List<Long> dupPubIds = pdwhPubDuplicateService.dupByPubInfo(dup.hashTP, dup.hashTPP);
      return dupPubIds;
    }

    return null;
  }

}
