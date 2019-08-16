package com.smate.center.task.service.sns.pubAssign.handler;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.task.dao.pdwh.quartz.PdwhPubAuthorSnsPsnRecordDao;
import com.smate.center.task.dao.sns.pub.PubAssignLogDao;
import com.smate.center.task.exception.ServiceException;
import com.smate.center.task.model.pdwh.pub.PdwhPubAuthorSnsPsnRecord;
import com.smate.center.task.model.sns.pub.PubAssginMatchContext;
import com.smate.center.task.model.sns.pub.PubAssignLog;
import com.smate.core.base.utils.json.JacksonUtils;

@Service("pdwhPubAssignMatchNameHandler")
@Transactional(rollbackFor = Exception.class)
public class PdwhPubAssignMatchNameHandler implements PdwhPubAssignMatchService {
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Resource(name = "pdwhPubAssignMatchFriendsHandler")
  private PdwhPubAssignMatchService pdwhPubAssignMatchService;
  @Autowired
  private PdwhPubAssignService pdwhPubAssignService;
  @Autowired
  private PdwhPubAuthorSnsPsnRecordDao PsnRecordDao;
  @Autowired
  private PubAssignLogDao pubAssignLogDao;

  @Override
  public String handler(PubAssginMatchContext context, PubAssignLog assignLog) throws ServiceException {
    try {
      if (context.getPsnId() == null) {
        List<PdwhPubAuthorSnsPsnRecord> PsnRecordList = PsnRecordDao.getPsnRecordByPubId(context.getPdwhPubId());
        // 查重
        for (PdwhPubAuthorSnsPsnRecord psnRecord : PsnRecordList) {
          List<Long> rePsnIds = new ArrayList<Long>();
          if (CollectionUtils.isNotEmpty(rePsnIds)
              && (rePsnIds.contains(psnRecord.getPsnId()) || context.getRePsnIds().contains(psnRecord.getPsnId()))) {
            // 如果包含在rePsnIds说明已处理过
            continue;
          } else {
            List<PdwhPubAuthorSnsPsnRecord> psnRecords =
                PsnRecordDao.getPsnRecord(context.getPdwhPubId(), psnRecord.getPsnId());
            for (PdwhPubAuthorSnsPsnRecord RePsnRecord : psnRecords) {
              // 保留简称匹配上的记录存至pub_assign_log_detail表
              if (1 == RePsnRecord.getNameType() && StringUtils.isBlank(context.getMatchedFullName())) {
                context.setNameType(1);
                context.setMatchedFullName(RePsnRecord.getPsnName());
                context.setPubMemberId(RePsnRecord.getPubMemberId());
                context.setPubMemberName(RePsnRecord.getPubMemberName());
              } else if (1 != RePsnRecord.getNameType() && StringUtils.isBlank(context.getMatchedInitName())) {
                context.setNameType(0);
                context.setMatchedInitName(RePsnRecord.getPsnName());
                context.setPubMemberId(RePsnRecord.getPubMemberId());
                context.setPubMemberName(RePsnRecord.getPubMemberName());
              }
            }
            assignLog = pubAssignLogDao.getPubAssignLog(context.getPdwhPubId(), psnRecord.getPsnId());
            if (assignLog != null) {
              if (assignLog.getConfirmResult() == 1) {
                context.setEmailResult(0);
                context.setMatchedEmail(null);
                context.setPubMemberId(null);
                context.setPubMemberName(null);
                context.setMatchedFullName(null);
                context.setMatchedInitName(null);
                context.setInitNameMatch(null);
                continue;
              }
            } else {
              assignLog = new PubAssignLog(context.getPdwhPubId(), psnRecord.getPsnId(), new Date());
              assignLog.setScore(0L);
              assignLog.setEmailMatch(0);
            }
            if (context.getNameType() == null) {
              continue;
            } else if (StringUtils.isNotBlank(context.getMatchedFullName())) {
              assignLog.setFullNameMatch(1);
              assignLog.setInitNameMatch(0);
            } else {
              assignLog.setInitNameMatch(1);
              assignLog.setFullNameMatch(0);
            }
            context.setPsnId(assignLog.getPsnId());
            pdwhPubAssignMatchService.handler(context, assignLog);
            rePsnIds.add(psnRecord.getPsnId());
            context.setRePsnIds(rePsnIds);
          }
        }
      } else {

        List<PdwhPubAuthorSnsPsnRecord> psnRecord =
            PsnRecordDao.getPsnRecord(context.getPdwhPubId(), context.getPsnId());
        boolean fullName = false;
        boolean initName = false;
        if (CollectionUtils.isNotEmpty(psnRecord)) {
          for (PdwhPubAuthorSnsPsnRecord pdwhPubAuthorSnsPsnRecord : psnRecord) {
            if (pdwhPubAuthorSnsPsnRecord.getNameType() == null) {
              continue;
            } else if (1 == pdwhPubAuthorSnsPsnRecord.getNameType()) {
              fullName = true;
              context.setMatchedFullName(pdwhPubAuthorSnsPsnRecord.getPsnName());
              context.setPubMemberId(pdwhPubAuthorSnsPsnRecord.getPubMemberId());
              context.setPubMemberName(pdwhPubAuthorSnsPsnRecord.getPubMemberName());
            } else {
              initName = true;
              context.setMatchedInitName(pdwhPubAuthorSnsPsnRecord.getPsnName());
              context.setPubMemberId(pdwhPubAuthorSnsPsnRecord.getPubMemberId());
              context.setPubMemberName(pdwhPubAuthorSnsPsnRecord.getPubMemberName());
            }
          }
          if (!fullName && !initName) {
            String dupResult = pdwhPubAssignService.getPubDupucheckStatus(context.getPsnId(), context.getPdwhPubId());
            Map dupResultMap = JacksonUtils.jsonToMap(dupResult);
            if ("SUCCESS".equals(dupResultMap.get("status").toString())) {
              if (dupResultMap.get("msgList") != null) {// 查重到了
                assignLog.setIsAutoConfirm(1);
                assignLog.setConfirmResult(1);
                String msg = dupResultMap.get("msgList").toString();
                String[] dupPubIds = msg.split(",");
                assignLog.setSnsPubId(Long.valueOf(dupPubIds[0]));
                assignLog.setConfirmDate(new Date());
                assignLog.setCreateDate(new Date());
                for (String dupPubId : dupPubIds) {
                  pdwhPubAssignService.savePubPdwhSnsRelation(Long.valueOf(dupPubId), context.getPdwhPubId());
                }
                // 自动认领也要计算成果合作者
                pdwhPubAssignService.PsnPubCopartnerRcmd(context.getPsnId(), context.getPdwhPubId());
              }
              assignLog.setUpdateDate(new Date());
              // 所有没有匹配的字段都需要重置
              assignLog.setFullNameMatch(0);
              assignLog.setInitNameMatch(0);
              assignLog.setFriendMatch(0);
              assignLog.setInsMatch(0);
              assignLog.setKeywordsMatch(0);
            }
            pubAssignLogDao.saveWithNewTransaction(assignLog);
            pdwhPubAssignService.dealpubAssignLogDetail(context);
            return null;
          }
          if (fullName) {
            assignLog.setScore(110L);
            assignLog.setFullNameMatch(1);
            assignLog.setInitNameMatch(0);
            context.setNameType(1);
          } else {
            assignLog.setScore(105L);
            assignLog.setInitNameMatch(1);
            assignLog.setFullNameMatch(0);
            context.setNameType(0);
          }
          assignLog.setFriendMatch(0);
          assignLog.setInsMatch(0);
          assignLog.setKeywordsMatch(0);
          pdwhPubAssignMatchService.handler(context, assignLog);
        } else {
          String dupResult = pdwhPubAssignService.getPubDupucheckStatus(context.getPsnId(), context.getPdwhPubId());
          Map dupResultMap = JacksonUtils.jsonToMap(dupResult);
          if ("SUCCESS".equals(dupResultMap.get("status").toString())) {
            if (dupResultMap.get("msgList") != null) {// 查重到了
              assignLog.setIsAutoConfirm(1);
              assignLog.setConfirmResult(1);
              String msg = dupResultMap.get("msgList").toString();
              String[] dupPubIds = msg.split(",");
              assignLog.setSnsPubId(Long.valueOf(dupPubIds[0]));
              assignLog.setConfirmDate(new Date());
            }
            assignLog.setUpdateDate(new Date());
            // 所有没有匹配的字段都需要重置
            assignLog.setFriendMatch(0);
            assignLog.setFullNameMatch(0);
            assignLog.setInitNameMatch(0);
            assignLog.setInsMatch(0);
            assignLog.setKeywordsMatch(0);
          }
          pubAssignLogDao.saveWithNewTransaction(assignLog);
          pdwhPubAssignService.dealpubAssignLogDetail(context);
        }
      }

    } catch (Exception e) {
      logger.error(
          "pdwhPubAssignMatchNameHandler保存匹配记录出错 ：psnId：" + context.getPsnId() + "pubId:" + context.getPdwhPubId(), e);
    }
    return null;
  }

}
