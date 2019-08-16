package com.smate.sie.center.task.service;

import com.smate.sie.center.task.model.SieDataSrvPatTmpRefresh;

import java.util.List;

/***
 * 生成单位专利数据
 * 
 * @author 叶星源
 * @Date 20180911
 */
public interface SieDataSrvPatTmpService {

  List<SieDataSrvPatTmpRefresh> getNeedRefreshData(int size);

  void saveSieDataSrvPatTmpRefresh(SieDataSrvPatTmpRefresh sieDataSrvPatTmpRefresh);

  void refreshData(SieDataSrvPatTmpRefresh sieDataSrvPatTmpRefresh);

}
