package com.smate.sie.center.task.service;

import java.util.List;

import com.smate.sie.center.task.model.PubFullTextRefresh;

/***
 * 成果全文缩略图生成
 * 
 * @author 叶星源
 * @Date 20180801
 */
public interface SiePubFulltextToImageService {

  List<PubFullTextRefresh> getNeedRefreshData(int size);

  void savePubFulltextImageRefresh(PubFullTextRefresh pubFullTextRefresh);

  void delPubFulltextImageRefresh(Long pubId);

  void refreshData(PubFullTextRefresh pubFullTextRefresh);

}
