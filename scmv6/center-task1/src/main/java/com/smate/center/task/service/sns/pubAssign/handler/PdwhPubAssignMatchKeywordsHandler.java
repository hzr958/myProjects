package com.smate.center.task.service.sns.pubAssign.handler;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.task.dao.sns.psn.PsnDisciplineKeyDao;
import com.smate.center.task.dao.sns.pub.PubAssignLogDao;
import com.smate.center.task.exception.ServiceException;
import com.smate.center.task.model.sns.pub.PubAssginMatchContext;
import com.smate.center.task.model.sns.pub.PubAssignLog;
import com.smate.core.base.utils.json.JacksonUtils;

@Service("pdwhPubAssignMatchKeywordsHandler")
@Transactional(rollbackFor = Exception.class)
public class PdwhPubAssignMatchKeywordsHandler implements PdwhPubAssignMatchService {
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private PdwhPubAssignService pdwhPubAssignService;
  @Autowired
  private PsnDisciplineKeyDao psnDisciplineKeyDao;
  @Autowired
  private PubAssignLogDao pubAssignLogDao;

  @Override
  public String handler(PubAssginMatchContext context, PubAssignLog assignLog) throws ServiceException {
    try {
      List<String> psnKeywords = psnDisciplineKeyDao.getPsnKeywords(context.getPsnId());
      int count = 0;
      for (String psnKeyword : psnKeywords) {
        if (context.getKeywords().contains(psnKeyword)) {
          count++;
        }
      }
      if (psnKeywords.size() > 0) {
        float matchFeq = (float) count / psnKeywords.size();
        if (matchFeq >= 0.50) {
          if (assignLog.getScore() == 120L) {
            assignLog.setScore(125L);
          } else if (assignLog.getScore() == 115L) {
            assignLog.setScore(120L);
          } else if (assignLog.getScore() == 110L) {
            assignLog.setScore(115L);
          } else if (assignLog.getScore() == 105L) {
            assignLog.setScore(110L);
          } else if (assignLog.getScore() == 100L) {
            assignLog.setScore(105L);
          } else if (assignLog.getScore() == 85L) {
            assignLog.setScore(90L);
          } else if (assignLog.getScore() == 80L) {
            assignLog.setScore(85L);
          } else if (assignLog.getScore() == 40L) {
            assignLog.setScore(45L);
          } else if (assignLog.getScore() == 35L) {
            assignLog.setScore(40L);
          } else if (assignLog.getScore() == 60L) {
            assignLog.setScore(65L);
          } else if (assignLog.getScore() == 30L) {
            assignLog.setScore(35L);
          } else if (assignLog.getScore() == 0 && assignLog.getFullNameMatch() == 1) {
            assignLog.setScore(40L);
          } else if (assignLog.getScore() == 0 && assignLog.getInitNameMatch() == 1) {
            assignLog.setScore(25L);
          }
          assignLog.setKeywordsMatch(1);
        } else {
          assignLog.setKeywordsMatch(0);
        }
      }
      String dupResult = pdwhPubAssignService.getPubDupucheckStatus(context.getPsnId(), context.getPdwhPubId());
      Map dupResultMap = JacksonUtils.jsonToMap(dupResult);
      if ("SUCCESS".equals(dupResultMap.get("status").toString())) {
        if (dupResultMap.get("msg") != null) {// 查重到了
          assignLog.setIsAutoConfirm(1);
          assignLog.setConfirmResult(1);
          String msg = dupResultMap.get("msgList").toString();
          String[] dupPubIds = msg.split(",");
          assignLog.setSnsPubId(Long.valueOf(dupPubIds[0]));
          assignLog.setConfirmDate(new Date());
          for (String dupPubId : dupPubIds) {
            pdwhPubAssignService.savePubPdwhSnsRelation(Long.valueOf(dupPubId), context.getPdwhPubId());
          }
          // 自动认领也要计算成果合作者
          pdwhPubAssignService.PsnPubCopartnerRcmd(context.getPsnId(), context.getPdwhPubId());
        }
        assignLog.setUpdateDate(new Date());
      }
      pubAssignLogDao.saveWithNewTransaction(assignLog);
      pdwhPubAssignService.dealpubAssignLogDetail(context);
      pdwhPubAssignService.removeAllContext(context);
    } catch (Exception e) {
      logger.error(
          "PubAssignMatchKeywordsHandle保存匹配记录出错 ：psnId：" + context.getPsnId() + "pubId:" + context.getPdwhPubId(), e);
    }
    return null;
  }


}
