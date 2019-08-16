package com.smate.sie.center.task.service;

import java.util.List;

import com.smate.sie.center.task.model.SiePrjFullTextRefresh;

/***
 * 成果全文缩略图生成
 * 
 * @author 叶星源
 * @Date 20180801
 */
public interface SiePrjFulltextToImageService {

  List<SiePrjFullTextRefresh> getNeedRefreshData(int size);

  void savePrjFulltextImageRefresh(SiePrjFullTextRefresh SiePrjFullTextRefresh);

  void delPrjFulltextImageRefresh(Long PrjId);

  void refreshData(SiePrjFullTextRefresh SiePrjFullTextRefresh);

  void convertFail(SiePrjFullTextRefresh siePrjFullTextRefresh);

}
