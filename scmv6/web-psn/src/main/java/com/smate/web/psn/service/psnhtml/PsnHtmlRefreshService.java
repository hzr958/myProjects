package com.smate.web.psn.service.psnhtml;

import java.util.List;

import com.smate.web.psn.exception.PsnException;
import com.smate.web.psn.model.psnhtml.PsnHtmlRefresh;

public interface PsnHtmlRefreshService {

  List<PsnHtmlRefresh> getPsnHtmlNeedRefresh(Integer max) throws PsnException;

  void updatePsnHtmlRefresh(Long psnId) throws PsnException;
}
