package com.smate.sie.center.task.service;

import java.util.List;

import com.smate.sie.center.task.model.InsGuid;

public interface InstitutionSieService {

  List<InsGuid> getNeedRefresh(int size);

  void refreshInsGuid(InsGuid insGuid);

}
