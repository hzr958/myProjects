package com.smate.center.task.service.sns.psn;

import java.util.List;

import com.smate.core.base.utils.model.security.Person;

public interface SendDealMessageNoticeMailService {

  List<Person> getpsnIds(Integer size, Long lastPnsId);

  void sendNoticeMail(Person person) throws Exception;

  Long getFulltextCount(Long currentUserId) throws Exception;

  Long getPsnCount();

}
