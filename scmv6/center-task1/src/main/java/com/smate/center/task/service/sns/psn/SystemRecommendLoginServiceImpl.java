package com.smate.center.task.service.sns.psn;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smate.center.task.exception.ServiceException;
import com.smate.center.task.model.sns.psn.FriendSysRecommend;
import com.smate.center.task.model.sns.psn.FriendSysRecommendLog;
import com.smate.center.task.model.sns.psn.FriendSysRecommendScore;
import com.smate.center.task.model.sns.psn.PsnKnowCopartner;
import com.smate.center.task.model.sns.quartz.Friend;
import com.smate.center.task.model.sns.quartz.Publication;
import com.smate.center.task.single.service.person.EducationHistoryService;
import com.smate.center.task.single.service.person.PersonManager;
import com.smate.center.task.single.service.person.WorkHistoryService;
import com.smate.center.task.single.service.pub.PublicationService;
import com.smate.core.base.psn.model.EducationHistory;
import com.smate.core.base.psn.model.WorkHistory;
import com.smate.core.base.utils.model.Page;
import com.smate.core.base.utils.model.security.Person;

/**
 * @author lichangwen
 * 
 */
@Service("systemRecommendLoginService")
public class SystemRecommendLoginServiceImpl implements SystemRecommendLoginService {

  /**
   * 
   */
  private static final long serialVersionUID = -8241310275400220606L;

  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private FriendService friendService;
  @Autowired
  private FriendWebService friendWebService;
  @Autowired
  private WorkHistoryService workHistoryService;
  @Autowired
  private EducationHistoryService educationHistoryService;
  @Autowired
  private PersonManager personManager;
  @Autowired
  private RecommendService recommendService;
  @Autowired
  private PublicationService publicationService;

  @Override
  public void timingRecommendLogin(List<Long> psnIds) throws ServiceException {
    // 推荐计分权重：合作者*50%+同事*10%+同学*10%+好友的好友*15%+领域*15%
    int batchSize = 1000;
    FriendSysRecommendLog recommendLog = new FriendSysRecommendLog();
    recommendLog.setStartPsnId(psnIds.get(0));
    recommendLog.setLastPsnId(psnIds.get(psnIds.size() - 1));
    recommendLog.setStartDate(new Date());
    recommendLog = recommendService.saveFriendSysRecommendLog(recommendLog);
    // 工作经历智能推荐好友得分
    String workLog = this.autoRecommendFriendByWork(psnIds, batchSize);
    // 教育经历智能推荐好友得分
    String eduLog = this.autoRecommendFriendByEdu(psnIds, batchSize);
    // 好友的好友得分
    String forfLog = this.autoRecommendFriendByOfFriend(psnIds);
    // 成果合作者得分
    String pubLog = this.autoRecommendFriendByPublication(psnIds);
    // 项目合作者得分
    String prjLog = this.autoRecommendFriendByProject(psnIds);
    // 领域得分，一个学科相同得20分
    String disLog = this.autoRecommendFriendByDis(psnIds, batchSize);
    // 推荐总分
    String recLog = this.autoRecommendFriendByPerson(batchSize);
    recommendLog.setWorkMatch(workLog);
    recommendLog.setEduMatch(eduLog);
    recommendLog.setForfMatch(forfLog);
    recommendLog.setPubMatch(pubLog);
    recommendLog.setPrjMatch(prjLog);
    recommendLog.setDisMatch(disLog);
    recommendLog.setRecommendMatch(recLog);
    recommendLog.setLastDate(new Date());
    recommendService.saveFriendSysRecommendLog(recommendLog);
  }

  private String autoRecommendFriendByWork(List<Long> psnIds, int batchSize) throws ServiceException {
    String log = "totalCount:";
    List<WorkHistory> workList = workHistoryService.findWorkByAutoRecommend(psnIds);
    if (CollectionUtils.isEmpty(workList)) {
      log += 0;
      return log;
    }
    log += workList.size();
    int matchCount = 0;
    for (WorkHistory psn : workList) {
      if (psn.getInsId() == null)
        continue;
      Long psnId = psn.getPsnId();
      Page<WorkHistory> tempPage = new Page<WorkHistory>(batchSize);
      tempPage = workHistoryService.findWork(psn.getInsId(), psnId, tempPage);
      if (tempPage.getTotalCount() <= 0)
        continue;
      Long tempTotalPage = tempPage.getTotalPages();
      for (int j = 1; j <= tempTotalPage; j++) {
        if (j > 1) {
          tempPage.setPageNo(j);
          tempPage = workHistoryService.findWork(psn.getInsId(), psnId, tempPage);
        }
        for (WorkHistory temp : tempPage.getResult()) {
          if (temp.getPsnId().intValue() == psnId.intValue())
            continue;
          double workScore = RecommendAlgorithmUtils.workSecore(psn, temp);
          if (workScore == 0)
            continue;
          recommendService.saveRecommendScore(
              new FriendSysRecommendScore(psnId, temp.getPsnId(), workScore * 0.1, RecommendType.RECOMMEND_WORK));
          matchCount++;
        }
      }
    }
    log += ",matchCount:" + matchCount;
    return log;
  }

  private String autoRecommendFriendByEdu(List<Long> psnIds, int batchSize) throws ServiceException {
    String log = "totalCount:";
    List<EducationHistory> eduList = educationHistoryService.findEduByAutoRecommend(psnIds);
    if (CollectionUtils.isEmpty(eduList)) {
      log += 0;
      return log;
    }
    log += eduList.size();
    int matchCount = 0;
    for (EducationHistory psn : eduList) {
      if (psn.getInsId() == null)
        continue;
      Long psnId = psn.getPsnId();
      Page<EducationHistory> tempPage = new Page<EducationHistory>(batchSize);
      tempPage = educationHistoryService.findEducationHistory(psn.getInsId(), psnId, tempPage);
      if (tempPage.getTotalCount() <= 0)
        continue;
      Long tempTotalPage = tempPage.getTotalPages();
      for (int j = 1; j <= tempTotalPage; j++) {
        if (j > 1) {
          tempPage.setPageNo(j);
          tempPage = educationHistoryService.findEducationHistory(psn.getInsId(), psnId, tempPage);
        }
        for (EducationHistory temp : tempPage.getResult()) {
          if (temp.getPsnId().intValue() == psnId.intValue())
            continue;
          double eduScore = RecommendAlgorithmUtils.eduSecore(psn, temp);
          if (eduScore == 0)
            continue;
          recommendService.saveRecommendScore(
              new FriendSysRecommendScore(psnId, temp.getPsnId(), eduScore * 0.1, RecommendType.RECOMMEND_EDU));
          matchCount++;
        }
      }
    }
    log += ",matchCount:" + matchCount;
    return log;
  }

  private String autoRecommendFriendByOfFriend(List<Long> psnIds) throws ServiceException {
    String log = "totalCount:";
    List<Friend> psnList = friendService.findFriendByAutoRecommend(psnIds);
    if (CollectionUtils.isEmpty(psnList)) {
      log += 0;
      return log;
    }
    log += psnList.size();
    int matchCount = 0;
    for (Friend psn : psnList) {
      Long psnId = psn.getPsnId();
      Integer friendNode = Integer.valueOf(psn.getFriendNode());
      Long friendPsnId = psn.getFriendPsnId();
      List<Friend> tempList = getFriendService(friendNode).findRecommendFriend(psnId, friendPsnId);
      if (CollectionUtils.isEmpty(tempList))
        continue;
      for (Friend temp : tempList) {
        if (temp.getFriendPsnId().intValue() == psnId.intValue())
          continue;
        int matchPsnFriendCount = friendWebService.getMatchPsnFriendsCount(psnId, temp.getFriendPsnId());
        recommendService.saveRecommendScore(new FriendSysRecommendScore(psnId, temp.getFriendPsnId(),
            RecommendAlgorithmUtils.friendOfFriendSecore(matchPsnFriendCount) * 0.15,
            RecommendType.RECOMMEND_OF_FRIEND));
        matchCount++;
      }
    }
    log += ",matchCount:" + matchCount;
    return log;
  }

  @SuppressWarnings("rawtypes")
  private String autoRecommendFriendByPublication(List<Long> psnIds) throws ServiceException {
    String log = "totalCount:";
    log += psnIds.size();
    int matchCount = 0;
    for (Long psnId : psnIds) {
      List<Publication> pubList = publicationService.findPubByPnsId(psnId);
      if (CollectionUtils.isEmpty(pubList))
        continue;
      Person person = personManager.getPerson(psnId);
      if (person != null) {
        for (Publication pub : pubList) {
          if (pub.getZhTitleHash() == null && pub.getEnTitleHash() == null)
            continue;
          List<Map<String, Object>> tempPubPsnIds = null;
          Integer recommendType = null;
          Double refScore = 0.0;
          tempPubPsnIds = publicationService.matchPubPsnIds(pub);
          if (CollectionUtils.isEmpty(tempPubPsnIds))
            continue;
          // 排除成果合作者的成果合作者中没有包含当前人员的
          for (int i = tempPubPsnIds.size() - 1; i >= 0; i--) {
            Long tmpPsnId = Long.valueOf(String.valueOf(((Map) tempPubPsnIds.get(i)).get("psnId")));
            String lastName = person.getLastName();
            String firstName = person.getFirstName();
            String likeName = "";
            if (StringUtils.isNotBlank(lastName) && StringUtils.isNotBlank(firstName)) {
              likeName = likeName + "%" + firstName.substring(0, 1) + "%".toLowerCase();
            }
            int result = publicationService.pubMatchName(tmpPsnId, person.getName(), likeName);
            if (result < 1)
              tempPubPsnIds.remove(i);
          }
          if (CollectionUtils.isEmpty(tempPubPsnIds))
            continue;
          recommendType = RecommendType.RECOMMEND_PUBLICATION;
          for (Map<String, Object> map : tempPubPsnIds) {
            try {
              Long tempPsnId = Long.valueOf(String.valueOf(map.get("psnId")));
              Integer count = Integer.valueOf(String.valueOf(map.get("count")));
              if (tempPsnId.intValue() == psnId.intValue())
                continue;
              refScore = Double.valueOf(count > 2 ? 100 : 80);
              recommendService
                  .saveRecommendScore(new FriendSysRecommendScore(psnId, tempPsnId, refScore * 0.5, recommendType));
              matchCount++;
            } catch (Exception e) {
              logger.error(
                  "保存成果合作者分数出错 ：psnId:" + psnId + "tempPsnId:" + Long.valueOf(String.valueOf(map.get("psnId"))), e);
            }
          }
        }
      }
    }
    log += ",matchCount:" + matchCount;
    return log;
  }

  private String autoRecommendFriendByProject(List<Long> psnIds) throws ServiceException {
    String log = "totalCount:";
    List<Long> psnIdList = recommendService.findGroupInvitePsnByPrj(psnIds);
    if (CollectionUtils.isEmpty(psnIdList)) {
      log += 0;
      return log;
    }
    log += psnIdList.size();
    int matchCount = 0;
    for (Long psnId : psnIdList) {
      List<Long> prjGroupIds = recommendService.findPrjGroupIdsByPsnId(psnId);
      if (CollectionUtils.isEmpty(prjGroupIds))
        continue;
      List<Map<String, Object>> tempPsnIdList = recommendService.findGroupInvitePsnIdsByMap(prjGroupIds, psnId);
      if (CollectionUtils.isEmpty(tempPsnIdList))
        continue;
      for (Map<String, Object> map : tempPsnIdList) {
        try {
          Long tempPsnId = Long.valueOf(String.valueOf(map.get("psnId")));
          Integer count = Integer.valueOf(String.valueOf(map.get("count")));
          if (tempPsnId.intValue() == psnId.intValue())
            continue;
          recommendService.saveRecommendScore(new FriendSysRecommendScore(psnId, tempPsnId,
              Double.valueOf(count > 1 ? 100 : 80) * 0.5, RecommendType.RECOMMEND_PROJECT));
          matchCount++;
        } catch (Exception e) {
          logger.error("保存项目合作者分数出错 ：psnId:" + psnId + "tempPsnId:" + Long.valueOf(String.valueOf(map.get("psnId"))),
              e);
        }
      }
    }
    log += ",matchCount:" + matchCount;
    return log;
  }

  public String autoRecommendFriendByDis(List<Long> psnIds, int batchSize) throws ServiceException {
    String log = "totalCount:";
    List<Long> psnIdList = personManager.findPsnDisciplineByAutoRecommend(psnIds);
    if (CollectionUtils.isEmpty(psnIdList)) {
      log += 0;
      return log;
    }
    log += psnIdList.size();
    int matchCount = 0;
    for (Long psnId : psnIdList) {
      Page tempPsnPage = new Page(batchSize);
      tempPsnPage = recommendService.findFriendDisByPsnId(psnId, tempPsnPage);
      if (tempPsnPage.getTotalCount() <= 0)
        continue;
      Long tempPsnTotalPage = tempPsnPage.getTotalPages();
      for (int j = 1; j <= tempPsnTotalPage; j++) {
        if (j > 1) {
          tempPsnPage.setPageNo(j);
          tempPsnPage = recommendService.findFriendDisByPsnId(psnId, tempPsnPage);
        }
        List tempList = tempPsnPage.getResult();
        for (Object obj : tempList) {
          Map map = (Map) obj;
          Long tempPsnId = Long.valueOf(String.valueOf(map.get("PSN_ID")));
          Integer tempCount = Integer.valueOf(String.valueOf(map.get("COUNT(T2.PSN_ID)")));
          if (tempPsnId.intValue() == psnId.intValue())
            continue;
          recommendService.saveRecommendScore(new FriendSysRecommendScore(psnId, tempPsnId,
              Double.valueOf((tempCount * 30) > 100 ? 100 : (tempCount * 30)) * 0.15, RecommendType.RECOMMEND_FIELD));
          matchCount++;
        }
      }
    }
    log += ",matchCount:" + matchCount;
    return log;
  }

  private String autoRecommendFriendByPerson(int batchSize) throws ServiceException {
    String log = "totalCount:";
    Page<Long> psnIdPage = new Page<Long>(batchSize);
    psnIdPage = recommendService.findRecommendScoreByPsnId(psnIdPage);
    if (psnIdPage.getTotalCount() <= 0) {
      log += 0;
      return log;
    }
    log += psnIdPage.getTotalCount();
    int updateCount = 0;
    int saveCount = 0;
    Long psnIdTotalPage = psnIdPage.getTotalPages();
    for (int i = 1; i <= psnIdTotalPage; i++) {
      if (i > 1) {
        psnIdPage.setPageNo(i);
        psnIdPage = recommendService.findRecommendScoreByPsnId(psnIdPage);
      }
      List<Long> psnIdList = psnIdPage.getResult();
      for (Long psnId : psnIdList) {

        if (getPersonManager(psnId) == null)
          continue;
        Page<FriendSysRecommendScore> tempPage = new Page<FriendSysRecommendScore>(batchSize);
        tempPage = recommendService.findRecommendScore(psnId, tempPage);
        if (tempPage.getTotalCount() <= 0)
          continue;
        recommendService.delRecommendByPsnId(psnId);// 删除推荐表中的旧数据
        recommendService.delPsnKnowCopartnerByPsnId(psnId);
        Long tempTotalPage = tempPage.getTotalPages();
        for (int j = 1; j <= tempTotalPage; j++) {
          if (j > 1) {
            tempPage.setPageNo(j);
            tempPage = recommendService.findRecommendScore(psnId, tempPage);
          }
          List<FriendSysRecommendScore> tempList = tempPage.getResult();
          for (FriendSysRecommendScore temp : tempList) {
            try {
              Long tempPsnId = temp.getRecommendPsnId();
              if (psnId.intValue() == tempPsnId.intValue())
                continue;
              String type = String.valueOf(temp.getRecommendType());
              double total = temp.getRecommendScore();
              List<FriendSysRecommendScore> scoreList =
                  recommendService.findScore(psnId, tempPsnId, temp.getRecommendType());
              if (CollectionUtils.isNotEmpty(scoreList)) {
                for (FriendSysRecommendScore recScore : scoreList) {
                  total += recScore.getRecommendScore();
                  type += "," + recScore.getRecommendType();
                }
              }
              boolean isMax = recommendService.isMax(psnId);
              FriendSysRecommend newRecommend = new FriendSysRecommend();
              newRecommend.setRecommendType(type);
              newRecommend.setScore(total);// 推荐人员总得分

              boolean isAddMaxRec = false;
              if (isMax) {
                FriendSysRecommend recommendMin = recommendService.getRecommendMin(psnId);
                if (recommendMin != null && recommendMin.getScore() < newRecommend.getScore()) {
                  recommendService.delFriendSysRecommend(recommendMin.getId());
                  isAddMaxRec = true;
                }
              }
              Integer tempNode = personManager.getPsnByNode(tempPsnId);
              newRecommend = this.psnToSysRecommend(newRecommend, psnId, tempNode, tempPsnId);
              if (newRecommend == null)
                continue;
              if (!isMax || isAddMaxRec) {
                if (tempNode != null) {
                  FriendSysRecommend toRecommend = recommendService.getFriendSysRecommend(psnId, tempPsnId);
                  if (toRecommend == null) {
                    recommendService.save(newRecommend);
                    saveCount++;
                  } else {
                    recommendService.save(this.recommendToRecommend(newRecommend, toRecommend));
                    updateCount++;
                  }
                }
              }
              matchCpt(psnId, temp, newRecommend);
            } catch (Exception e) {
              logger.error("保存推荐总分出错，psnId：" + psnId + " tempPsnId:" + temp.getRecommendPsnId(), e);
            }
          }
        }

      }
    }
    log += ",updateCount:" + updateCount + ",addCount:" + saveCount;
    return log;
  }

  private void matchCpt(Long psnId, FriendSysRecommendScore temp, FriendSysRecommend newRecommend)
      throws ServiceException {
    String type = String.valueOf(temp.getRecommendType());
    if ("4".equals(type) || "5".equals(type)) {
      PsnKnowCopartner cpt = recommendService.getPsnKnowCopartner(psnId, temp.getRecommendPsnId());
      if (cpt == null) {
        cpt = new PsnKnowCopartner();
        cpt.setPsnId(psnId);
        cpt.setCptPsnId(temp.getRecommendPsnId());
        cpt.setCptName(newRecommend.getTempPsnName());
        cpt.setCptFirstName(newRecommend.getTempPsnFirstName());
        cpt.setCptLastName(newRecommend.getTempPsnLastName());
        cpt.setCptHeadUrl(newRecommend.getTempPsnHeadUrl());
        cpt.setCptViewTitel(newRecommend.getTempPsnTitel());
        cpt.setCptTypes(type);
        if ("4".equals(type))
          cpt.setPubScore(temp.getRecommendScore());
        if ("5".equals(type))
          cpt.setPrjScore(temp.getRecommendScore());
      } else {
        String cpttypes = cpt.getCptTypes();
        if (cpttypes.indexOf(type) == -1) {
          cpt.setCptTypes(cpttypes + "," + type);
        }
      }
      boolean isFriend = friendService.isPsnFirend(psnId, temp.getRecommendPsnId());
      if (isFriend)
        cpt.setIsFriend(1);
      recommendService.savePsnKnowCopartner(cpt);
    }
  }

  private FriendSysRecommend psnToSysRecommend(FriendSysRecommend newRecommend, Long psnId, Integer tempNode,
      Long tempPsnId) {
    try {
      Person tempPerson = null;
      tempPerson = personManager.getPersonByRecommend(tempPsnId);
      if (tempPerson == null)
        return null;
      newRecommend.setPsnId(psnId);
      newRecommend.setTempPsnId(tempPerson.getPersonId());
      newRecommend.setTempPsnName(tempPerson.getName());
      newRecommend.setTempPsnFirstName(tempPerson.getFirstName());
      newRecommend.setTempPsnLastName(tempPerson.getLastName());
      newRecommend.setTempPsnHeadUrl(tempPerson.getAvatars());
      newRecommend.setTempPsnTitel(tempPerson.getTitolo());
      return newRecommend;
    } catch (ServiceException e) {
      logger.error("根据psnId:{},tempNode:{},tempPsnId:{}查询person出错", new Object[] {psnId, tempNode, tempPsnId}, e);
      return null;
    }
  }

  private FriendSysRecommend recommendToRecommend(FriendSysRecommend newRecommend, FriendSysRecommend toRecommend) {
    toRecommend.setRecommendType(newRecommend.getRecommendType());
    toRecommend.setScore(newRecommend.getScore());
    toRecommend.setTempPsnName(newRecommend.getTempPsnName());
    toRecommend.setTempPsnFirstName(newRecommend.getTempPsnFirstName());
    toRecommend.setTempPsnLastName(newRecommend.getTempPsnLastName());
    toRecommend.setTempPsnHeadUrl(newRecommend.getTempPsnHeadUrl());
    toRecommend.setTempPsnTitel(newRecommend.getTempPsnTitel());
    return toRecommend;
  }

  public RecommendService getRecommendService(Integer nodeId) throws ServiceException {
    return recommendService;
  }

  public RecommendService getRecommendService(Long psnId) throws ServiceException {
    return recommendService;
  }

  private PersonManager getPersonManager(Integer nodeId) throws ServiceException {
    return personManager;
  }

  private PersonManager getPersonManager(Long psnId) throws ServiceException {
    return personManager;
  }

  private FriendService getFriendService(Integer nodeId) throws ServiceException {
    return friendService;
  }

}
