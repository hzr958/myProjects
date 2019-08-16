package com.smate.center.task.service.publicpub;

import java.util.List;

import com.smate.center.task.model.common.CrossrefYearCount;

public interface CrossRefDataService {

  void saveYearCount(CrossrefYearCount yearCount);

  List<CrossrefYearCount> getYearCount();

  void updateYearCountStatus(Long year);

  List<CrossrefYearCount> getImportCrossrefData();

  void saveJson(Integer pubYear, String fileName, String json);

}
