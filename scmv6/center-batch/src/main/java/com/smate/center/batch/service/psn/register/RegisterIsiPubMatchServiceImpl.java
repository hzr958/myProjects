package com.smate.center.batch.service.psn.register;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.constant.PubMatchedScoreConstants;
import com.smate.center.batch.model.pdwh.pub.isi.IsiPubMatchAssign;
import com.smate.center.batch.model.pdwh.pub.isi.IsiPubMatchAuthor;
import com.smate.center.batch.service.pdwh.isipub.IsiPubMatchService;
import com.smate.center.batch.util.pub.PsnPmIsiNameUtils;


/**
 * ISI新注册用户匹配成果的逻辑.
 * 
 */
@Service("registerIsiPubMatchService")
@Transactional(rollbackFor = Exception.class)
public class RegisterIsiPubMatchServiceImpl implements PubRegisterMatchService {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private IsiPubMatchService isiPubMatchService;

  @Override
  public void registerUserMatchPub(Map<String, Object> params) {
    logger.info("isiPubMatchDecorator");
    String psnId = Objects.toString(params.get(PARAM_PSN_ID), "");
    // 用户参数Email.
    String psnEmail = Objects.toString(params.get(PARAM_EMAIL), "");
    String firstName = Objects.toString(params.get(PARAM_FIRST_NAME), "");
    String lastName = Objects.toString(params.get(PARAM_LAST_NAME), "");
    String otherName = Objects.toString(params.get(PARAM_OTHER_NAME), "");
    String insId = Objects.toString(params.get(PARAM_INS_ID), "");
    // String initName = null;
    // isi用户名匹配前缀.
    // Set<String> prefixNames =
    // PsnPmIsiNameUtils.buildPrefixName(firstName, lastName,
    // otherName);
    // isi用户简名组合.
    // Set<String> initNames =
    // PsnPmIsiNameUtils.buildInitName(firstName, lastName, otherName);

    // isi用户全称组合.
    Set<String> fullNames = PsnPmIsiNameUtils.buildFullName(firstName, lastName, otherName);
    List<String> psnEnNameList = this.getPsnEnList(fullNames);
    // 该单位的所有成果.
    List<Long> insPubList = null;
    if (StringUtils.isNotBlank(insId) && !StringUtils.equalsIgnoreCase("", insId)
        && CollectionUtils.isNotEmpty(psnEnNameList)) {
      // insPubList =
      // isiPubMatchService.getIsiPubAssignIdList(Long.valueOf(insId));
      insPubList = isiPubMatchService.getMatchAuPubList(Long.valueOf(insId), psnEnNameList,
          PubMatchedScoreConstants.MATCH_LIMIT_SIZE);
    }
    // 获取邮件匹配的成果ID.
    List<Long> pubIdList = null;
    if (StringUtils.isNotBlank(psnEmail)) {
      pubIdList = isiPubMatchService.getPubIdListByEmail(psnEmail);
    }
    if (CollectionUtils.isNotEmpty(pubIdList)) {
      // 遍历匹配到的成果ID.
      for (Long pubId : pubIdList) {
        // 初始化匹配结果记录.
        IsiPubMatchAssign assign = isiPubMatchService.initIsiPubMatchAssign(pubId, Long.valueOf(psnId));
        assign.setEmail(1);// 1-邮件匹配上.
        if (CollectionUtils.isNotEmpty(insPubList) && insPubList.contains(pubId)) {
          // 获取成果的作者列表.
          List<IsiPubMatchAuthor> authorList = isiPubMatchService.getIsiPubMatchAuthorList(pubId);
          // 过滤匹配到的成果,并返回成果作者.
          IsiPubMatchAuthor author = this.filterMatchedPub(fullNames, authorList);
          if (author != null) {
            assign.setScore(PubMatchedScoreConstants.EXACT_FNAME_EMAIL_SCORE);
            // 补充人员信息.
            isiPubMatchService.supplePsnPmInfo(pubId, Long.valueOf(psnId), author, authorList);
          }
        }
      }
    }
    // 如果邮件未匹配到成果或匹配到的成果数小与 7 条，则用名称和单位进行匹配.
    if (CollectionUtils.isEmpty(pubIdList) || (CollectionUtils.isNotEmpty(pubIdList)
        && pubIdList.size() < PubMatchedScoreConstants.MATCH_LIMIT_SIZE.intValue())) {
      // 根据名称匹配单位内的成果.
      List<Long> nameMatchedList = this.addNameMatchPub(Long.valueOf(psnId), insPubList, fullNames);
      if (CollectionUtils.isNotEmpty(nameMatchedList)) {
        pubIdList.addAll(nameMatchedList);
      }
    }
    // 排除用户已确认或已拒绝的成果.
    isiPubMatchService.excludeMatchedPub(psnId, pubIdList);
  }

  @SuppressWarnings("rawtypes")
  private List<String> getPsnEnList(Set<String> fullNames) {
    if (fullNames != null && fullNames.size() > 0) {
      List<String> enNameList = new ArrayList<String>();
      Iterator iterator = fullNames.iterator();
      while (iterator.hasNext()) {
        String iFName = (String) iterator.next();
        if (StringUtils.isNotBlank(iFName)) {
          enNameList.add(iFName);
        }
      }
    }
    return null;
  }

  /**
   * 根据名称匹配单位内的成果.
   * 
   * @param pubInsList
   * @param fullNames
   */
  private List<Long> addNameMatchPub(Long psnId, List<Long> pubInsList, Set<String> fullNames) {
    List<Long> matchedPubList = null;
    if (CollectionUtils.isNotEmpty(pubInsList)) {
      matchedPubList = new ArrayList<Long>();
      for (Long pubId : pubInsList) {
        // 获取成果的作者列表.
        List<IsiPubMatchAuthor> authorList = isiPubMatchService.getIsiPubMatchAuthorList(pubId);
        if (CollectionUtils.isNotEmpty(authorList)) {
          // 过滤匹配到的成果,并返回成果作者.
          IsiPubMatchAuthor author = this.filterMatchedPub(fullNames, authorList);
          if (author != null) {
            // 初始化匹配结果记录.
            IsiPubMatchAssign assign = isiPubMatchService.initIsiPubMatchAssign(pubId, Long.valueOf(psnId));
            assign = this.filterNameInfo(assign, author);
            // 补充人员信息.
            isiPubMatchService.supplePsnPmInfo(pubId, Long.valueOf(psnId), author, authorList);
          }
        }
      }
    }
    return matchedPubList;
  }

  /**
   * 补充名称匹配上的匹配结果信息.
   * 
   * @param assign
   * @param author
   * @return
   */
  private IsiPubMatchAssign filterNameInfo(IsiPubMatchAssign assign, IsiPubMatchAuthor author) {
    assign.setfName(1);
    assign.setAthSeq(author.getSeqNo());
    assign.setAthId(author.getId());
    assign.setAthPos(author.getAuthorPos());
    assign.setScore(PubMatchedScoreConstants.FNAME_SCORE);
    isiPubMatchService.saveIsiPubMatchAssign(assign);
    return assign;
  }

  /**
   * 过滤匹配到的成果.
   * 
   * @param fullNames 人名全称.
   * @param authorList
   * @return
   */
  private IsiPubMatchAuthor filterMatchedPub(Set<String> fullNames, List<IsiPubMatchAuthor> authorList) {
    IsiPubMatchAuthor result = null;
    if (CollectionUtils.isNotEmpty(authorList)) {
      // 遍历成果作者列表.
      for (IsiPubMatchAuthor author : authorList) {
        String authorFullName = author.getFullName();
        // 全称匹配上.
        if (this.isMatchFNameFlag(fullNames, authorFullName)) {
          result = author;
          break;
        }
      }
    }
    return result;
  }

  /**
   * 判断全称是否和成果作者全称匹配.
   * 
   * @param fullNames
   * @param authorFullName
   * @return true-匹配上；false-未匹配上.
   */
  @SuppressWarnings("rawtypes")
  private boolean isMatchFNameFlag(Set<String> fullNames, String authorFullName) {
    boolean isMatchFlag = false;
    if (fullNames != null && fullNames.size() > 0) {
      Iterator iterator = fullNames.iterator();
      while (iterator.hasNext()) {
        String iFName = (String) iterator.next();
        if (StringUtils.isNotBlank(iFName) && StringUtils.isNotBlank(authorFullName)
            && StringUtils.equalsIgnoreCase(iFName.trim(), authorFullName.trim())) {
          isMatchFlag = true;
          break;
        }
      }
    }
    return isMatchFlag;
  }


}
