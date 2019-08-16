package com.smate.center.task.service.bdsp;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.snsbak.PubPdwhAddrInfoInit;
import com.smate.center.task.model.snsbak.PubPdwhAddrInfoInit1;
import com.smate.center.task.model.snsbak.PubPdwhAddrInfoInit2;

@Repository
public interface BdspDataStatisticsService {

  public List<PubPdwhAddrInfoInit1> getProvInteriorToHandleList();

  public void pubComparativeAnalysisByAreaProInterior(List<PubPdwhAddrInfoInit1> list);

  public List<PubPdwhAddrInfoInit2> getProvInternationalToHandleList();

  public List<PubPdwhAddrInfoInit> getCityToHandleList();

  public void pubComparativeAnalysisByAreaProvInternational(List<PubPdwhAddrInfoInit2> list);

  public void pubComparativeAnalysisByAreaCity(List<PubPdwhAddrInfoInit> list);

  public void pubIndependentPrCityAnalysis(List<PubPdwhAddrInfoInit> list);

}
