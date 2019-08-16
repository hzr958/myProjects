package com.smate.center.batch.service.pdwh.prj;

import java.math.BigDecimal;
import java.util.List;

public interface HighTechKeywordsService {
  public List<BigDecimal> getHighTechPubToHandleList(Integer size);

  public void updateHighTechPubStatus(Long pubId, Integer status);

  public BigDecimal getHighTechPubToHandleNum();

  public Integer HighTechClassificationForPub(Long pubId) throws Exception;
}
