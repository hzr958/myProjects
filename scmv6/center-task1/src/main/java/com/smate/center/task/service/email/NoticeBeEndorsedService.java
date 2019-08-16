package com.smate.center.task.service.email;

import java.util.List;

import com.smate.core.base.psn.model.profile.KeywordIdentification;

/**
 * 赞同研究领域service类
 * 
 * @author zjh
 *
 */
public interface NoticeBeEndorsedService {

  List<Long> getBeEndorsedPsnId(Integer startSize) throws Exception;

  void sendEmail(Long psnId) throws Exception;

  void savetoStatus(KeywordIdentification psnId);

  List<KeywordIdentification> getCurrentEndorsedInfo(Integer startSize) throws Exception;

}
