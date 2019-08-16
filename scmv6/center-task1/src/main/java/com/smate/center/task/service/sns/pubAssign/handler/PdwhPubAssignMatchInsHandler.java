package com.smate.center.task.service.sns.pubAssign.handler;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.task.dao.pdwh.quartz.PdwhPubAuthorSnsPsnRecordDao;
import com.smate.center.task.exception.ServiceException;
import com.smate.center.task.model.pdwh.pub.PdwhPubAuthorSnsPsnRecord;
import com.smate.center.task.model.sns.pub.PubAssginMatchContext;
import com.smate.center.task.model.sns.pub.PubAssignLog;
import com.smate.core.base.psn.dao.EducationHistoryDao;
import com.smate.core.base.psn.dao.WorkHistoryDao;

@Service("pdwhPubAssignMatchInsHandler")
@Transactional(rollbackFor = Exception.class)
public class PdwhPubAssignMatchInsHandler implements PdwhPubAssignMatchService {
  protected final Logger logger = LoggerFactory.getLogger(getClass());

  @Resource(name = "pdwhPubAssignMatchKeywordsHandler")
  private PdwhPubAssignMatchService PubAssignMatchHandler;
  @Autowired
  private PdwhPubAuthorSnsPsnRecordDao PsnRecordDao;
  @Autowired
  private EducationHistoryDao educationHistoryDao;
  @Autowired
  private WorkHistoryDao workHistoryDao;

  @Override
  public String handler(PubAssginMatchContext context, PubAssignLog assignLog) throws ServiceException {

    try {
      List<Long> psnEduInsId = new ArrayList<Long>();
      List<Long> psnWorkInsId = new ArrayList<Long>();
      if (context.getPubyear() == null) {
        psnEduInsId = educationHistoryDao.getPsnEduInsId(context.getPsnId());
        psnEduInsId = workHistoryDao.getPsnWorkInsId(context.getPsnId());
      } else {
        psnEduInsId = educationHistoryDao.getPsnEduByIdAndYear(context.getPsnId(), context.getPubyear());
        psnWorkInsId = workHistoryDao.getPsnWorkByIdAndYear(context.getPsnId(), context.getPubyear());// 查询工作经历insid
      }
      Set<Long> psnInsIds = new HashSet<>();
      List<Long> psnInsIdList = new ArrayList<Long>();
      if (CollectionUtils.isNotEmpty(psnEduInsId)) {
        psnInsIds.addAll(psnEduInsId);
      }
      if (CollectionUtils.isNotEmpty(psnWorkInsId)) {
        psnInsIds.addAll(psnWorkInsId);
      }
      psnInsIdList.addAll(psnInsIds);
      if (CollectionUtils.isNotEmpty(psnInsIds)) {
        PdwhPubAuthorSnsPsnRecord record =
            PsnRecordDao.getInsMatchStatus(context.getPsnId(), psnInsIdList, context.getPdwhPubId());
        if (record != null) {
          if (assignLog.getScore() == 115L) {
            assignLog.setScore(120L);
          } else if (assignLog.getScore() == 110L) {
            assignLog.setScore(115L);
          } else if (assignLog.getScore() == 105L) {
            assignLog.setScore(110L);
          } else if (assignLog.getScore() == 100L) {
            assignLog.setScore(105L);
          } else if (assignLog.getScore() == 80L) {
            assignLog.setScore(85L);
          } else if (assignLog.getScore() == 35L) {
            assignLog.setScore(40L);
          } else if (assignLog.getScore() == 0 && assignLog.getFullNameMatch() == 1) {
            assignLog.setScore(60L);
          } else if (assignLog.getScore() == 0 && assignLog.getInitNameMatch() == 1) {
            assignLog.setScore(30L);
          }
          assignLog.setInsMatch(1);
          context.setInsResult(1);
          context.setMatchedInsId(record.getInsId());
          PubAssignMatchHandler.handler(context, assignLog);
        } else {
          assignLog.setInsMatch(0);
          context.setMatchedInsId(null);
          PubAssignMatchHandler.handler(context, assignLog);
        }
      } else {
        assignLog.setInsMatch(0);
        context.setMatchedInsId(null);
        PubAssignMatchHandler.handler(context, assignLog);
      }
    } catch (Exception e) {
      logger.error("PubAssignMatchInsHandle保存匹配记录出错 ：psnId：" + context.getPsnId() + "pubId:" + context.getPdwhPubId(),
          e);
    }
    return null;
  }

}
