package com.smate.center.task.service.sns.pubAssign.handler;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.task.dao.email.PersonEmailDao;
import com.smate.center.task.dao.pdwh.quartz.PdwhPubAuthorSnsPsnRecordDao;
import com.smate.center.task.exception.ServiceException;
import com.smate.center.task.model.sns.pub.PubAssginMatchContext;
import com.smate.center.task.model.sns.pub.PubAssignLog;

@Service("pdwhPubAssignMatchEmailHandler")
@Transactional(rollbackFor = Exception.class)
public class PdwhPubAssignMatchEmailHandler implements PdwhPubAssignMatchService {
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Resource(name = "pdwhPubAssignMatchNameHandler")
  private PdwhPubAssignMatchService pdwhPubAssignMatchService;
  @Autowired
  private PdwhPubAssignService pdwhPubAssignService;
  @Autowired
  private PdwhPubAuthorSnsPsnRecordDao PsnRecordDao;
  @Autowired
  private PersonEmailDao personEmailDao;

  @Override
  public String handler(PubAssginMatchContext context, PubAssignLog assignLog) throws ServiceException {
    // email匹配
    if (CollectionUtils.isNotEmpty(context.getEmailList())) {
      for (String email : context.getEmailList()) {
        // 一个Email可能匹配上多个psnId，每个都需要推荐
        List<Long> psnIds = personEmailDao.findPsnIdByEmail(email);
        List<Long> psnIdList = PsnRecordDao.getPsnIdByPubId(context.getPdwhPubId());
        if (CollectionUtils.isNotEmpty(psnIds)) {
          for (Long psnId : psnIds) {
            if (psnIdList != null && psnIdList.contains(psnId)) {
              psnIdList.remove(psnId);
            }
            try {
              assignLog = pdwhPubAssignService.getPubAssignLog(context.getPdwhPubId(), psnId);
              if (assignLog == null) {
                assignLog = new PubAssignLog(context.getPdwhPubId(), psnId, new Date());
              }
              if (assignLog.getConfirmResult() == 1) {
                context.setEmailResult(0);
                context.setMatchedEmail(null);
                context.setMatchedFullName(null);
                context.setPubMemberId(null);
                context.setPubMemberName(null);
                context.setMatchedInitName(null);
                context.setInitNameMatch(null);
                continue;
              } else {
                assignLog.setScore(100L);
                assignLog.setEmailMatch(1);
                context.setPsnId(psnId);
                context.setEmailResult(1);
                context.setMatchedEmail(email);
              }
            } catch (Exception e) {
              logger.error("pdwhPubAssignMatchEmailHandler保存匹配记录出错 ：psnId：" + context.getPsnId() + "pubId:"
                  + context.getPdwhPubId(), e);
            }
            pdwhPubAssignMatchService.handler(context, assignLog);
          }
          if (psnIdList != null && psnIdList.size() > 0) {
            context.setPsnId(null);
            // 没有匹配上Email,就没有PsnId,故不在pub_assign_pub存记录
            pdwhPubAssignMatchService.handler(context, null);
          }

        } else {
          context.setPsnId(null);
          // 没有匹配上Email,就没有PsnId,故不在pub_assign_pub存记录
          pdwhPubAssignMatchService.handler(context, null);
        }
      }
    } else {
      context.setPsnId(null);
      // 没有匹配上Email,就没有PsnId,故不在pub_assign_pub存记录
      pdwhPubAssignMatchService.handler(context, null);
    }
    return null;
  }

}
