package com.smate.web.v8pub.service.findduplicate.pub;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.core.base.utils.pubHash.PubHashUtils;
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
public class VerifyPdwhPubDuplicateHandler extends DuplicateCheckHandlerBase {

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
        if (dupPubIds.size() > 0) {
          String ids = dupPubIds.stream().map(pubId -> String.valueOf(pubId)).collect(Collectors.joining(","));
          map.put("msgList", ids);
        }
        map.put("msg", String.valueOf(dupPubIds.get(0)));
      }
      return map;
    } catch (Exception e) {
      logger.error("基准库查重异常", e);
      throw new ServiceException(e);
    }
  }

  private List<Long> getDupPub(PubDuplicateDTO dup) {
    if (StringUtils.isNotBlank(dup.doi)) {
      List<Long> dupPubIds = pdwhPubDuplicateService.dupByNotNullDoi(dup.hashDoi, dup.hashCleanDoi);
      if (CollectionUtils.isNotEmpty(dupPubIds)) {
        return dupPubIds;
      }
    }
    if (StringUtils.isNotEmpty(dup.hashT)) {
      List<Long> dupPubIds = pdwhPubDuplicateService.dupByPubInfo(dup.hashT);
      if (CollectionUtils.isNotEmpty(dupPubIds)) {
        return dupPubIds;
      }
    }

    return null;
  }

  public static void main(String[] args) {
    Long titleHash = PubHashUtils.cleanTitleHash("肿瘤干细胞与肿瘤转移");
    System.out.println(titleHash);
  }

}
