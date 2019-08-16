package com.smate.center.task.service.fund;

import java.util.List;

import com.smate.center.task.model.fund.sns.PsnFundRecommend;
import com.smate.core.base.utils.model.security.Person;

public interface PsnFundRcmdMailService {
  final static Integer MAIL_LIST_SIZE_LIMIT = 3;

  /**
   * 获取当天加入的基金信息
   * 
   * @return
   */
  List<Long> getFundList();

  List<Long> getPsnIdsByFund(List<Long> fundIds);

  Person getPsnInfo(Long psnId);

  /**
   * 获取人员的推荐基金列表.
   * 
   * @param psnId
   * @param fundIds
   * @return
   */
  List<PsnFundRecommend> getPsnFundRecommendList(Long psnId, List<Long> fundIds);

  void buildRcmdMailInfo(Person person, List<PsnFundRecommend> reFundList);

  boolean getDataByEmail(String email);

  void saveWeChatMessagePsn(Person person, PsnFundRecommend psnFundRecommend, Long openId, int totalFund);

  Long getUserOpenId(Long personId, String token);

  boolean getDataByOpenId(Long openId, String token);

  void updateSendmailStatus(Long psnId, List<Long> fundIds);

}
