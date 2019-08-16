package com.smate.center.batch.chain.pubassign.task;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections4.MapUtils;

import com.smate.center.batch.model.rol.pub.PubAssginMatchContext;
import com.smate.center.batch.model.rol.pub.PubAssignScoreDetail;
import com.smate.center.batch.model.rol.pub.PubAssignScoreMap;

/**
 * PubAssignScoreMap工具类.
 * 
 * @author liqinghua
 * 
 */
public class PubAssignScoreMapUtils {

  /**
   * 查找MAP中的PubAssignScoreDetail.
   * 
   * @param scoreMap
   * @param psnId
   * @return
   */
  public static List<PubAssignScoreDetail> findPubAssignScoreDetail(Map<Integer, PubAssignScoreMap> scoreMap,
      Long psnId) {

    List<PubAssignScoreDetail> detailList = new ArrayList<PubAssignScoreDetail>();
    Iterator<Integer> iter = scoreMap.keySet().iterator();
    while (iter.hasNext()) {
      PubAssignScoreDetail detail = scoreMap.get(iter.next()).getDetailMap().get(psnId);
      if (detail != null) {
        detailList.add(detail);
      }
    }

    return detailList;
  }

  /**
   * 设置用户匹配上的关键词分数.
   * 
   * @param scoreMap
   * @param score
   */
  public static void setPsnKeyWordScore(Map<Integer, PubAssignScoreMap> pubAssignScoreMap, Long psnId, Float score,
      Float maxValue) {

    PubAssignScoreMapUtils.setPsnScore(pubAssignScoreMap, psnId, score, maxValue, ScoreType.keyWord);

  }

  /**
   * 设置用户匹配上的期刊分数.
   * 
   * @param scoreMap
   * @param score
   */
  public static void setPsnJournalScore(Map<Integer, PubAssignScoreMap> pubAssignScoreMap, Long psnId, Float score,
      Float maxValue) {

    PubAssignScoreMapUtils.setPsnScore(pubAssignScoreMap, psnId, score, maxValue, ScoreType.journal);
  }

  /**
   * 设置用户匹配上的会议分数.
   * 
   * @param scoreMap
   * @param score
   */
  public static void setPsnConferenceScore(Map<Integer, PubAssignScoreMap> pubAssignScoreMap, Long psnId, Float score,
      Float maxValue) {

    PubAssignScoreMapUtils.setPsnScore(pubAssignScoreMap, psnId, score, maxValue, ScoreType.conference);
  }

  /**
   * 设置用户匹配上的合作者分数.
   * 
   * @param scoreMap
   * @param score
   */
  public static void setPsnCoauthorScore(Map<Integer, PubAssignScoreMap> pubAssignScoreMap, Long psnId, Float score,
      Float maxValue) {

    PubAssignScoreMapUtils.setPsnScore(pubAssignScoreMap, psnId, score, maxValue, ScoreType.coauthor);
  }

  /**
   * 设置用户匹配上的成果发布年份分数.
   * 
   * @param scoreMap
   * @param score
   */
  public static void setPsnPubyearScore(Map<Integer, PubAssignScoreMap> pubAssignScoreMap, Long psnId, Float score,
      Float maxValue) {

    PubAssignScoreMapUtils.setPsnScore(pubAssignScoreMap, psnId, score, maxValue, ScoreType.pubyear);
  }

  /**
   * 计算所有人员总分.
   * 
   * @param pubAssignScoreMap
   * @param context
   */
  public static void calculateDetailTotalScore(Map<Integer, PubAssignScoreMap> pubAssignScoreMap,
      PubAssginMatchContext context) {

    if (MapUtils.isEmpty(pubAssignScoreMap)) {
      return;
    }
    // 查找人员，分别加上匹配上的分数
    Iterator<Integer> iter = pubAssignScoreMap.keySet().iterator();
    while (iter.hasNext()) {
      Integer seqNo = iter.next();
      PubAssignScoreMap scoreMap = pubAssignScoreMap.get(seqNo);
      if (scoreMap == null) {
        continue;
      }
      Map<Long, PubAssignScoreDetail> scoreDetailMap = scoreMap.getDetailMap();
      if (MapUtils.isEmpty(scoreDetailMap)) {
        continue;
      }
      Iterator<Long> iterDetail = scoreDetailMap.keySet().iterator();
      while (iterDetail.hasNext()) {
        PubAssignScoreDetail detail = scoreDetailMap.get(iterDetail.next());
        calculateDetailTotalScore(context, detail);
      }
    }
  }

  private static void calculateDetailTotalScore(PubAssginMatchContext context, PubAssignScoreDetail detail) {
    Float total = 0f;
    if (context.isCnkiImport()) {// cnki
      total = detail.getName() + detail.getConame() + detail.getConference() + detail.getDept() + detail.getJournal()
          + detail.getKeywords() + detail.getPubYear() + detail.getInst();
    } else if (context.isIsiImport()) {// isi
      total = +detail.getEmail() + detail.getInitName() + detail.getNameWord() + detail.getFullName()
          + detail.getKeywords() + detail.getJournal() + detail.getConference() + detail.getConame()
          + detail.getPubYear() + detail.getDept() + detail.getInst();
    } else if (context.isScopusImport()) {// scopus
      total =
          +detail.getEmail() + detail.getInitName() + detail.getFullName() + detail.getKeywords() + detail.getJournal()
              + detail.getConference() + detail.getConame() + detail.getPubYear() + detail.getDept() + detail.getInst();
    } else if (context.isCniprImport()) {// cnipr
      total = detail.getName() + detail.getConame() + detail.getDept() + detail.getJournal() + detail.getKeywords()
          + detail.getPubYear() + detail.getInst();
    } else if (context.isCnkiPatImport()) {// cnkipat
      total = detail.getName() + detail.getConame() + detail.getDept() + detail.getJournal() + detail.getKeywords()
          + detail.getPubYear() + detail.getInst();
    } else if (context.isPubMedImport()) {// pubMed
      total = +detail.getEmail() + detail.getInitName() + detail.getNameWord() + detail.getFullName()
          + detail.getKeywords() + detail.getJournal() + detail.getConference() + detail.getConame()
          + detail.getPubYear() + detail.getDept() + detail.getInst();
    } else if (context.isEiImport()) {// EI
      total = +detail.getEmail() + detail.getInitName() + detail.getNameWord() + detail.getFullName()
          + detail.getKeywords() + detail.getJournal() + detail.getConference() + detail.getConame()
          + detail.getPubYear() + detail.getDept() + detail.getInst();
    }
    total = formatTotleScore(total);
    detail.setTotal(total);
  }

  /**
   * 删除分数较低的匹配结果.
   * 
   * @param pubAssignScoreMap
   * @param context
   */
  public static void removeUnBoundScore(Map<Integer, PubAssignScoreMap> pubAssignScoreMap,
      PubAssginMatchContext context) {

    if (MapUtils.isEmpty(pubAssignScoreMap)) {
      return;
    }
    // 查找人员，分别加上匹配上的分数
    Iterator<Integer> iter = new HashSet<Integer>(pubAssignScoreMap.keySet()).iterator();
    List<Integer> removeSeqNo = new ArrayList<Integer>();
    while (iter.hasNext()) {
      Integer seqNo = iter.next();
      PubAssignScoreMap scoreMap = pubAssignScoreMap.get(seqNo);
      if (scoreMap == null) {
        continue;
      }
      List<Long> removePsnIds = new ArrayList<Long>();
      Map<Long, PubAssignScoreDetail> scoreDetailMap = scoreMap.getDetailMap();
      if (MapUtils.isEmpty(scoreDetailMap)) {
        continue;
      }
      Iterator<Long> iterDetail = new HashSet<Long>(scoreDetailMap.keySet()).iterator();
      while (iterDetail.hasNext()) {
        Long psnId = iterDetail.next();
        PubAssignScoreDetail detail = scoreDetailMap.get(psnId);
        // 必须大于阀值，否则删除
        if (detail.getTotal() <= context.getSettingPubAssignMatchScore().getMatchBound()) {
          removePsnIds.add(psnId);
        }
      }
      // 循环删除
      for (Long psnId : removePsnIds) {
        scoreDetailMap.remove(psnId);
      }
      // 如果所有用户都不合格，作者匹配结果
      if (MapUtils.isEmpty(scoreDetailMap)) {
        removeSeqNo.add(seqNo);
      }
    }
    // 删除为空的seqNo
    if (removeSeqNo.size() > 0) {
      for (Integer seqNo : removeSeqNo) {
        pubAssignScoreMap.remove(seqNo);
      }
    }
  }

  /**
   * 获取每个作者匹配的最高分数.
   * 
   * @param pubAssignScoreMap
   * @return
   */
  public static Map<Integer, Float> getSeqMaxScore(Map<Integer, PubAssignScoreMap> pubAssignScoreMap) {

    Map<Integer, Float> map = new HashMap<Integer, Float>();
    // 查找人员，分别加上匹配上的分数
    Iterator<Integer> iter = pubAssignScoreMap.keySet().iterator();
    while (iter.hasNext()) {
      Integer seqNo = iter.next();
      PubAssignScoreMap scoreMap = pubAssignScoreMap.get(seqNo);
      if (scoreMap == null) {
        continue;
      }
      Map<Long, PubAssignScoreDetail> scoreDetailMap = scoreMap.getDetailMap();
      if (MapUtils.isEmpty(scoreDetailMap)) {
        continue;
      }
      // 找出最大分数用户
      Float maxScore = 0f;
      Iterator<Long> iterDetail = scoreDetailMap.keySet().iterator();
      while (iterDetail.hasNext()) {
        PubAssignScoreDetail detail = scoreDetailMap.get(iterDetail.next());
        if (detail.getTotal() > maxScore) {
          maxScore = detail.getTotal();
        }
      }
      map.put(seqNo, maxScore);
    }
    return map;
  }

  /**
   * 过滤成果匹配结果，将分数差距太大的人员移除(每个成果作者匹配到的人员，分数与最大分数差距大于等于50分的删除).
   * 
   * @param pubAssignScoreMap
   * @param context
   */
  public static void removeLowerPsnScore(Map<Integer, PubAssignScoreMap> pubAssignScoreMap,
      PubAssginMatchContext context) {

    if (MapUtils.isEmpty(pubAssignScoreMap)) {
      return;
    }

    // 找出每个作者的最大匹配分数
    Map<Integer, Float> maxScoreMap = getSeqMaxScore(pubAssignScoreMap);

    // 查找人员，分别加上匹配上的分数
    Iterator<Integer> iter = pubAssignScoreMap.keySet().iterator();
    while (iter.hasNext()) {
      Integer seqNo = iter.next();
      PubAssignScoreMap scoreMap = pubAssignScoreMap.get(seqNo);
      if (scoreMap == null) {
        continue;
      }
      Map<Long, PubAssignScoreDetail> scoreDetailMap = scoreMap.getDetailMap();
      if (MapUtils.isEmpty(scoreDetailMap)) {
        continue;
      }
      // 最大分数
      Float maxScore = maxScoreMap.get(seqNo);
      // 删除分数较低用户
      Set<Long> scoreDetailMapKeySet = new HashSet<Long>(scoreDetailMap.keySet());
      Iterator<Long> iterDetail = scoreDetailMapKeySet.iterator();
      while (iterDetail.hasNext()) {
        Long psnId = iterDetail.next();
        PubAssignScoreDetail detail = scoreDetailMap.get(psnId);
        if ((maxScore - detail.getTotal()) >= 50) {
          scoreDetailMap.remove(psnId);
        }
      }
    }
  }

  /**
   * 获取匹配上成果的每个人的最高分.
   * 
   * @param pubAssignScoreMap
   * @return key:psnid object[0:seqno,1:maxscore]
   */
  public static Map<Long, Object[]> getPsnMaxScore(Map<Integer, PubAssignScoreMap> pubAssignScoreMap) {

    Map<Long, Object[]> map = new HashMap<Long, Object[]>();
    Iterator<Integer> iter = pubAssignScoreMap.keySet().iterator();
    while (iter.hasNext()) {
      Integer seqNo = iter.next();
      PubAssignScoreMap scoreMap = pubAssignScoreMap.get(seqNo);
      if (scoreMap == null) {
        continue;
      }
      Map<Long, PubAssignScoreDetail> scoreDetailMap = scoreMap.getDetailMap();
      if (MapUtils.isEmpty(scoreDetailMap)) {
        continue;
      }
      Iterator<Long> detailIter = scoreDetailMap.keySet().iterator();
      while (detailIter.hasNext()) {
        Long psnId = detailIter.next();
        PubAssignScoreDetail detail = scoreDetailMap.get(psnId);
        Object[] obj = map.get(psnId);
        if (obj == null) {
          map.put(psnId, new Object[] {detail.getSeqNo(), detail.getTotal()});
          // 分值更高，替换
        } else if (((Float) obj[1]) < detail.getTotal()) {
          map.put(psnId, new Object[] {detail.getSeqNo(), detail.getTotal()});
        }
      }
    }
    return map;
  }

  /**
   * 一条成果，一个人可能匹配上多个作者，需要将同一个人的最高分保留，其他删除.
   * 
   * @param pubAssignScoreMap
   * @param context
   */
  public static void removeDupPsnScore(Map<Integer, PubAssignScoreMap> pubAssignScoreMap,
      PubAssginMatchContext context) {

    if (MapUtils.isEmpty(pubAssignScoreMap)) {
      return;
    }

    Map<Long, Object[]> maxScoreMap = getPsnMaxScore(pubAssignScoreMap);
    // 查找人员，分别加上匹配上的分数
    Iterator<Integer> iter = pubAssignScoreMap.keySet().iterator();
    while (iter.hasNext()) {
      Integer seqNo = iter.next();
      PubAssignScoreMap scoreMap = pubAssignScoreMap.get(seqNo);
      if (scoreMap == null) {
        continue;
      }
      Map<Long, PubAssignScoreDetail> scoreDetailMap = scoreMap.getDetailMap();
      if (MapUtils.isEmpty(scoreDetailMap)) {
        continue;
      }
      List<Long> removePsnIds = new ArrayList<Long>();
      Iterator<Long> maxScoreIter = maxScoreMap.keySet().iterator();
      while (maxScoreIter.hasNext()) {
        Long psnId = maxScoreIter.next();
        Integer maxSeqNo = (Integer) maxScoreMap.get(psnId)[0];
        PubAssignScoreDetail detail = scoreDetailMap.get(psnId);
        if (detail == null) {
          continue;
          // 序号不一样，删除
        } else if (!seqNo.equals(maxSeqNo)) {
          removePsnIds.add(psnId);
        }
      }
      for (Long psnId : removePsnIds) {
        scoreDetailMap.remove(psnId);
      }
    }
  }

  /**
   * 过滤成果匹配结果，将分数差距太大的人员移除(每个成果作者匹配到的人员，分数与最大分数差距大于等于50分的删除).
   * 
   * @param pubAssignScoreMap
   * @param context
   */
  public static List<PubAssignScoreDetail> getMatchedPsnDetail(Map<Integer, PubAssignScoreMap> pubAssignScoreMap,
      PubAssginMatchContext context) {

    List<PubAssignScoreDetail> detailList = new ArrayList<PubAssignScoreDetail>();
    // 查找人员，分别加上匹配上的分数
    Iterator<Integer> iter = pubAssignScoreMap.keySet().iterator();
    while (iter.hasNext()) {
      Integer seqNo = iter.next();
      PubAssignScoreMap scoreMap = pubAssignScoreMap.get(seqNo);
      if (scoreMap == null) {
        continue;
      }
      Map<Long, PubAssignScoreDetail> scoreDetailMap = scoreMap.getDetailMap();
      if (MapUtils.isEmpty(scoreDetailMap)) {
        continue;
      }
      detailList.addAll(scoreDetailMap.values());
    }
    return detailList;
  }

  /**
   * 格式化分数.
   * 
   * @param total
   * @return
   */
  public static Float formatTotleScore(Float total) {
    // 格式化结果
    DecimalFormat df = new DecimalFormat("#.00");
    total = Float.valueOf(df.format(total));
    return total;
  }

  /**
   * 设置用户匹配上的分数.
   * 
   * @param scoreMap
   * @param score
   */
  private static void setPsnScore(Map<Integer, PubAssignScoreMap> pubAssignScoreMap, Long psnId, Float score,
      Float maxValue, ScoreType type) {

    // 查找人员，分别加上匹配上的分数
    Iterator<Integer> iter = pubAssignScoreMap.keySet().iterator();
    while (iter.hasNext()) {
      Integer seqNo = iter.next();
      PubAssignScoreMap scoreMap = pubAssignScoreMap.get(seqNo);
      if (scoreMap == null) {
        continue;
      }
      PubAssignScoreDetail scoreDetail = scoreMap.getDetailMap().get(psnId);
      if (scoreDetail == null) {
        continue;
      }
      if (type.equals(ScoreType.keyWord)) {
        scoreDetail.setKeywords(score);
      } else if (type.equals(ScoreType.journal)) {
        scoreDetail.setJournal(score);
      } else if (type.equals(ScoreType.conference)) {
        scoreDetail.setConference(score);
      } else if (type.equals(ScoreType.coauthor)) {
        // 合作者分name匹配,email匹配
        score = Math.min(score + scoreDetail.getConame(), maxValue);
        scoreDetail.setConame(score);
      } else if (type.equals(ScoreType.pubyear)) {
        scoreDetail.setPubYear(score);
      }
    }
  }

  /**
   * 分数类型，内部用.
   * 
   * @author liqinghua
   * 
   */
  private enum ScoreType {

    /**
     * 关键词.
     */
    keyWord,
    /**
     * 期刊.
     */
    journal,
    /**
     * 会议.
     */
    conference,
    /**
     * 合作者.
     */
    coauthor,
    /**
     * 成果年份.
     */
    pubyear
  }
}
