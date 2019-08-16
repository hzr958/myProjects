package com.smate.center.batch.service.psn.register;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.constant.PubMatchedScoreConstants;
import com.smate.center.batch.model.pdwh.pub.cnki.CnkiPubMatchAuthor;
import com.smate.center.batch.service.pdwh.pubmatch.CnkiPubMatchService;


/**
 * CNKI新注册用户匹配成果的逻辑.
 * 
 */
@Service("registerCnkiPubMatchService")
@Transactional(rollbackFor = Exception.class)
public class RegisterCnkiPubMatchServiceImpl implements PubRegisterMatchService {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private CnkiPubMatchService cnkiPubMatchService;

  @Override
  public void registerUserMatchPub(Map<String, Object> params) {
    logger.info("cnkiPubMatchDecorator");
    String psnId = Objects.toString(params.get(PARAM_PSN_ID), "");
    String fullName = Objects.toString(params.get(PARAM_ZHNAME), "");
    String insId = Objects.toString(params.get(PARAM_INS_ID), "");
    List<Long> pubInsList = null;
    if (StringUtils.isNotBlank(insId) && !"".equalsIgnoreCase(insId)) {
      if (StringUtils.isNotBlank(fullName)) {
        List<String> nameList = new ArrayList<String>();
        nameList.add(fullName);
        pubInsList = cnkiPubMatchService.getCnkiPubAssignId(Long.valueOf(insId), nameList,
            PubMatchedScoreConstants.MATCH_LIMIT_SIZE);
      }
    }
    if (CollectionUtils.isNotEmpty(pubInsList) && StringUtils.isNotBlank(fullName)) {
      List<Long> matchedPubList = new ArrayList<Long>();
      for (Long pubId : pubInsList) {
        // 获取人员姓名匹配的成果列表.
        List<CnkiPubMatchAuthor> authorList = cnkiPubMatchService.getCnkiPubMatchAuthorList(pubId, fullName);
        if (CollectionUtils.isNotEmpty(authorList)) {
          for (CnkiPubMatchAuthor author : authorList) {
            cnkiPubMatchService.initCnkiPubMatchAssign(author, Long.valueOf(psnId));
          }
          matchedPubList.add(pubId);
        }
      }
      // 排除用户已确认或已拒绝的成果.
      cnkiPubMatchService.excludeMatchedPub(Long.valueOf(psnId), matchedPubList);
    }
  }


}
