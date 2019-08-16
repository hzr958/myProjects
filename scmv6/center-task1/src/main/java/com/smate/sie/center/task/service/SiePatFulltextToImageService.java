package com.smate.sie.center.task.service;

import java.util.List;

import com.smate.sie.center.task.model.SiePatFullTextRefresh;

/***
 * 成果全文缩略图生成
 * 
 * @author 叶星源
 * @Date 20180801
 */
public interface SiePatFulltextToImageService {

  List<SiePatFullTextRefresh> getNeedRefreshData(int size);

  void savePatFulltextImageRefresh(SiePatFullTextRefresh siePatFullTextRefresh);

  void delPatFulltextImageRefresh(Long PatId);

  void refreshData(SiePatFullTextRefresh siePatFullTextRefresh);

  void convertFail(SiePatFullTextRefresh siePatFullTextRefresh);

}
