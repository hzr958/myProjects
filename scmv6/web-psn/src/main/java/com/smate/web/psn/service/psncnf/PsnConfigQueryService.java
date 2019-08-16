package com.smate.web.psn.service.psncnf;



import java.util.List;
import java.util.Map;

import com.smate.web.psn.exception.ServiceException;

public interface PsnConfigQueryService {

  @SuppressWarnings("rawtypes")
  public List<Map> getPsnConfigContactByPsnIds(List<Long> psnIdList) throws ServiceException;

  public Boolean checkPsnWork(Long psnId, Long insId, String insNameZh, String insNameEn, Integer anyView)
      throws ServiceException;
}
