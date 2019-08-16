package com.smate.center.batch.chain.pubassign.task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.rol.pub.PubAssginMatchContext;
import com.smate.center.batch.model.rol.pub.PubAssignScoreDetail;
import com.smate.center.batch.model.rol.pub.PubAssignScoreMap;
import com.smate.center.batch.service.rol.pub.PubAssignEiMatchService;
import com.smate.center.batch.service.rol.pub.PubAssignMatchService;
import com.smate.center.batch.service.rol.pub.PubAssignPubMedMatchService;
import com.smate.center.batch.service.rol.pubassign.PsnPmService;

/**
 * ISI成果匹配-成果指派匹配用户名称简写匹配任务，序号为1.
 * 
 * @author liqinghua
 * 
 */
public class PubAssignMatchInitNameTask implements IPubAssignMatchTask {

  /**
   * 
   */
  private static final long serialVersionUID = -4338835072874243490L;
  private final String name = "PubAssignMatchInitNameTask";
  @Autowired
  private PubAssignMatchService pubAssignMatchService;
  @Autowired
  private PubAssignPubMedMatchService pubAssignPubMedMatchService;
  @Autowired
  private PubAssignEiMatchService pubAssignEiMatchService;
  @Autowired
  private PsnPmService psnPmService;

  @Override
  public String getName() {
    return name;
  }

  @Override
  public boolean can(PubAssginMatchContext context) {
    return context.hasMatchedPsn();
  }

  /**
   * <pre>
   * 此匹配分两步：
   * 1:先匹配用户简写比如li qh,li q h,li q-h；
   * 
   * 2:如果用户简写匹配上了，再匹配用户名（firstname）的单词相似度，匹配上一个单词加50分，最多100分.
   * </pre>
   */
  @Override
  public boolean run(PubAssginMatchContext context) throws Exception {

    List<Object[]> matchedPsns = getMatchPsns(context);
    // 如果未匹配到一个用户，直接返回
    if (CollectionUtils.isEmpty(matchedPsns)) {
      return true;
    }

    // 需要匹配姓名中单词的人员列表(单词加分部分)
    Map<Integer, List<Long>> matchNameWordMap = new HashMap<Integer, List<Long>>();
    // 存储匹配结果
    Map<Integer, PubAssignScoreMap> pubAssignScoreMap = context.getPubAssignScoreMap();
    for (Object[] matchedPsn : matchedPsns) {
      Integer seqNo = (Integer) matchedPsn[0];
      Long psnId = (Long) matchedPsn[1];
      PubAssignScoreMap scoreMap = pubAssignScoreMap.get(seqNo);
      if (scoreMap == null) {
        continue;
      }
      PubAssignScoreDetail scoreDetail = scoreMap.getDetailMap().get(psnId);
      if (scoreDetail == null) {
        continue;
      }
      // 作者fullname为空，取InitFullName的分数
      if (StringUtils.isBlank(scoreMap.getFullName())) {
        scoreDetail.setInitName(context.getSettingPubAssignMatchScore().getInitFullName());
      } else {
        scoreDetail.setInitName(context.getSettingPubAssignMatchScore().getInitName());
        List<Long> psnIdsList =
            matchNameWordMap.get(seqNo) == null ? new ArrayList<Long>() : matchNameWordMap.get(seqNo);
        psnIdsList.add(psnId);
        matchNameWordMap.put(seqNo, psnIdsList);
      }
      // seq_no为空的情况，如果对上了同一个人，则将email分数加过去同一个人
      PubAssignScoreDetail nullDetail = context.getNullSeqScoreMap().get(psnId);
      if (nullDetail != null) {
        scoreDetail.setEmail(nullDetail.getEmail());
      }
    }

    // 依据前面匹配到简称的人员，继续匹配全称的单词个数相似度加分.
    matchAuthorFullNameWords(context, matchNameWordMap, pubAssignScoreMap);

    return true;
  }

  /**
   * 获取匹配上的人员列表.
   * 
   * @param context
   * @return
   * @throws ServiceException
   */
  private List<Object[]> getMatchPsns(PubAssginMatchContext context) throws ServiceException {
    // 获取匹配上isi成果作者名称简写的单位人员
    List<Object[]> matchedPsns = null;
    if (context.isIsiImport()) {
      matchedPsns = pubAssignMatchService.getIsiInitNameMatchPubAuthor(context.getPubId(), context.getInsId(),
          context.getMatchedPsnIds());
    } else if (context.isPubMedImport()) {
      matchedPsns = pubAssignPubMedMatchService.getPubMedInitNameMatchPubAuthor(context.getPubId(), context.getInsId(),
          context.getMatchedPsnIds());
    } else if (context.isEiImport()) {
      matchedPsns = pubAssignEiMatchService.getEiInitNameMatchPubAuthor(context.getPubId(), context.getInsId(),
          context.getMatchedPsnIds());
    }

    return matchedPsns;
  }

  /**
   * 依据前面匹配到简称的人员，继续匹配全称的单词个数相似度加分.
   * 
   * @param context
   * @param matchNameWordMap
   * @param pubAssignScoreMap
   * @throws ServiceException
   */
  private void matchAuthorFullNameWords(PubAssginMatchContext context, Map<Integer, List<Long>> matchNameWordMap,
      Map<Integer, PubAssignScoreMap> pubAssignScoreMap) throws ServiceException {

    // 匹配作者名称单词
    if (MapUtils.isEmpty(matchNameWordMap)) {
      return;
    }
    // get psnIds
    Set<Long> psnIds = new HashSet<Long>();
    Iterator<Integer> iter = matchNameWordMap.keySet().iterator();
    while (iter.hasNext()) {
      psnIds.addAll(matchNameWordMap.get(iter.next()));
    }
    // 获取用户全称
    Map<Long, List<String>> fullNameMap = psnPmService.getUserFullName(psnIds);
    if (MapUtils.isEmpty(fullNameMap)) {
      return;
    }

    // 依据前面匹配到简称的人员，继续匹配全称的单词个数相似度加分
    Iterator<Integer> nameWordMapIter = matchNameWordMap.keySet().iterator();
    while (nameWordMapIter.hasNext()) {
      boolean isMatch = false;
      Integer seqNo = nameWordMapIter.next();
      PubAssignScoreMap scoreMap = pubAssignScoreMap.get(seqNo);
      // 作者fullname，截取firstname
      String authorFullName = scoreMap.getFullName();
      String authorFistName = StringUtils.substring(authorFullName, authorFullName.indexOf(" "));
      if (StringUtils.isBlank(authorFistName)) {
        continue;
      }
      // 每个作者匹配上简写的人员
      List<Long> matchNameWordPsnIds = matchNameWordMap.get(seqNo);
      if (CollectionUtils.isEmpty(matchNameWordPsnIds)) {
        continue;
      }
      for (Long matchNameWordPsnId : matchNameWordPsnIds) {
        if (isMatch) {
          break;
        }
        // 人员fullname
        List<String> psnFullNameList = fullNameMap.get(matchNameWordPsnId);
        if (CollectionUtils.isEmpty(psnFullNameList)) {
          continue;
        }
        for (String psnFullName : psnFullNameList) {
          // 如果有全名，但全名不匹配，则减500分
          if (authorFullName.equals(psnFullName)) {
            // 拆分对比
            String[] nameSplits = psnFullName.split("[\\s\\-\\._]{1,}");
            if (nameSplits.length < 2) {
              continue;
            }
            Float initFullNameScore = 0f;
            int nextStartIndex = 0;
            // 第一个是姓，忽略
            for (int i = 1; i < nameSplits.length; i++) {
              String nameSplit = nameSplits[i];
              // 按顺序比较
              int idx = authorFistName.indexOf(nameSplit, nextStartIndex);
              // 匹配上一个单词，加50分
              if (idx > -1) {
                nextStartIndex = idx + nameSplit.length();
                initFullNameScore += 50f;
              }
            }
            // 最多取100分
            initFullNameScore = Math.min(initFullNameScore, context.getSettingPubAssignMatchScore().getInitFullName());
            // 判断是否有更大值
            PubAssignScoreDetail scoreDetail = scoreMap.getDetailMap().get(matchNameWordPsnId);
            if (initFullNameScore > 0 && scoreDetail.getNameWord() < initFullNameScore) {
              // scoreDetail.setNameWord(initFullNameScore);根据匹配规则，huang xi 匹配到huang
              // xia的部分，被加了分数，存储到Pub_assign_score.Name_Word，影响了准确度。此处先设为0
              scoreDetail.setNameWord(0f);
              // 多个fullname匹配上了，就一层一层跳出，继续下一个
              isMatch = true;
              break;
            }
          } else {
            PubAssignScoreDetail scoreDetail = scoreMap.getDetailMap().get(matchNameWordPsnId);
            scoreDetail.setNameWord(-500f);

          }

        }
      }
    }
  }
}
