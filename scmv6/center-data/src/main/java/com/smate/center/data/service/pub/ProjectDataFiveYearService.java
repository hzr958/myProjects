package com.smate.center.data.service.pub;

import java.util.List;
import java.util.Set;

import com.smate.center.data.model.hadoop.pub.HKeywordsItem;
import com.smate.center.data.model.pub.ProjectDataFiveYear;

public interface ProjectDataFiveYearService {

  List<ProjectDataFiveYear> getProjectDataList(Integer size, Long startId, Long endId);

  Set<String> handlePubKeywords(String keywords);

  StringBuilder conbinePubKeywords(String applicationCode, Set<String> keywordsSet, StringBuilder strBuilder);

  public void saveData(List<HKeywordsItem> allList) throws Exception;

}
