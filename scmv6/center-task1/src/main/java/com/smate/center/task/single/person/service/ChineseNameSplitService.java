package com.smate.center.task.single.person.service;

import java.util.List;

import com.smate.center.task.model.sns.pub.NameSplit;

public interface ChineseNameSplitService {

  public List<NameSplit> getToHandleList(Integer size);

  public void getLastNameAndFirstName(NameSplit name) throws Exception;

  public void updateNameListStatus(NameSplit name, Integer status);
}
