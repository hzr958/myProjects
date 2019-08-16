package com.smate.center.batch.service.psn;

import java.util.List;
import java.util.Map;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.rcmd.journal.RcmdPsnJournal;
import com.smate.center.batch.model.sns.psn.FriendExpertJournal;

public interface PsnJournalService {

  /**
   * 获取用户发表期刊最高档次
   * 
   * @param psnId
   * @return
   * @throws ServiceException
   */
  int getPsnJnlMaxGrade(Long psnId) throws ServiceException;

  /**
   * 获取个人发表期刊等级
   * 
   * @param psnId
   * @return
   * @throws ServiceException
   */
  List<RcmdPsnJournal> getPsnJournal(Long psnId) throws ServiceException;

  /**
   * 用户发表期刊的次数
   * 
   * @param issn
   * @return
   * @throws ServiceException
   */
  int getPsnJnlCountByIssn(Long psnId, String issn) throws ServiceException;

  /**
   * 用户好友发表期刊数
   * 
   * @param psnId
   * @param issn
   * @return
   * @throws ServiceException
   */
  int getPsnFriendJnlCountByIssn(Long psnId, String issn) throws ServiceException;

  /**
   * 合作者发表的期刊
   * 
   * @param psnId
   * @return
   * @throws ServiceException
   */
  List<String> getPsnPubCopartnerByJnl(Long psnId) throws ServiceException;

  /**
   * 获取与issn匹配的人员
   * 
   * @param issn
   * @return
   * @throws ServiceException
   */
  List<Map<String, Object>> getPsnListByIssn(String issn) throws ServiceException;

  List<FriendExpertJournal> getFriendExpertJournal(Long psnId) throws ServiceException;

  List<Long> getSameIssnPsnId(String issnStr, List<Long> psnIds) throws ServiceException;

}
