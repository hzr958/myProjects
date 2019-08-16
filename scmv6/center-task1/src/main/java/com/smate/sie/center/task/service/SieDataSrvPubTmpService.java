package com.smate.sie.center.task.service;

import com.smate.sie.center.task.model.SieDataSrvPubTmpRefresh;

import java.util.List;

/***
 * 生成单位成果数据
 * 
 * @author 叶星源
 * @Date 20180911
 */
public interface SieDataSrvPubTmpService {

  List<SieDataSrvPubTmpRefresh> getNeedRefreshData(int size);

  void saveSieDataSrvPubTmpRefresh(SieDataSrvPubTmpRefresh sieDataSrvPatTmpRefresh);

  void refreshData(SieDataSrvPubTmpRefresh sieDataSrvPatTmpRefresh);

}
