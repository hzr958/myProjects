package com.smate.center.task.service.sns.pubAssign.handler;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.task.dao.pdwh.quartz.PdwhPubAuthorSnsPsnRecordDao;
import com.smate.center.task.dao.sns.quartz.FriendDao;
import com.smate.center.task.exception.ServiceException;
import com.smate.center.task.model.sns.pub.PubAssginMatchContext;
import com.smate.center.task.model.sns.pub.PubAssignLog;

@Service("pdwhPubAssignMatchFriendsHandler")
@Transactional(rollbackFor = Exception.class)
public class PdwhPubAssignMatchFriendsHandler implements PdwhPubAssignMatchService {
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Resource(name = "pdwhPubAssignMatchInsHandler")
  private PdwhPubAssignMatchService pdwhPubAssignMatchService;
  @Autowired
  private FriendDao friendDao;
  @Autowired
  private PdwhPubAuthorSnsPsnRecordDao PsnRecordDao;

  @Override
  public String handler(PubAssginMatchContext context, PubAssignLog assignLog) throws ServiceException {

    try {
      List<Long> psnFriendIds = friendDao.getFriendByPsn(context.getPsnId());
      List<Long> psnCooperatorIds = PsnRecordDao.getPsnCooperatorIds(context.getPsnId(), context.getPdwhPubId());
      for (Long psnFriendId : psnFriendIds) {
        if (psnCooperatorIds.size() > 0 && psnCooperatorIds.contains(psnFriendId)) {
          context.setFriendResult(1);
          break;
        }
      }
      if (context.getFriendResult() == 1) {
        if (assignLog.getScore() == 110L) {
          assignLog.setScore(115L);
        } else if (assignLog.getScore() == 105L) {
          assignLog.setScore(110L);
        } else if (assignLog.getScore() == 100L) {
          assignLog.setScore(105L);
        } else if (assignLog.getScore() == 0 && assignLog.getFullNameMatch() == 1) {
          assignLog.setScore(80L);
        } else if (assignLog.getScore() == 0 && assignLog.getInitNameMatch() == 1) {
          assignLog.setScore(35L);
        }
        assignLog.setFriendMatch(1);
      } else {
        assignLog.setFriendMatch(0);
      }
    } catch (Exception e) {
      logger.error(
          "PubAssignMatchFriendsHandle保存匹配记录出错 ：psnId：" + context.getPsnId() + "pubId:" + context.getPdwhPubId(), e);
    }
    return pdwhPubAssignMatchService.handler(context, assignLog);
  }

}
