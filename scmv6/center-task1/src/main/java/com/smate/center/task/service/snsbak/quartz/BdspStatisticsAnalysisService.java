package com.smate.center.task.service.snsbak.quartz;

import java.util.List;

import com.smate.center.task.model.snsbak.bdsp.BdspProject;
import com.smate.center.task.model.snsbak.bdsp.BdspProposal;
import com.smate.center.task.model.snsbak.bdsp.PubPdwhAddrStandard;

public interface BdspStatisticsAnalysisService {

  List<BdspProject> findProjectList(int batchSize) throws Exception;

  List<BdspProposal> findProposalList(int batchSize) throws Exception;

  List<PubPdwhAddrStandard> findPaperPatentList(int batchSize) throws Exception;

  void doProjectRegister(BdspProject one) throws Exception;

  void doProposalRegister(BdspProposal one) throws Exception;

  void doPaperPatentRegister(PubPdwhAddrStandard one) throws Exception;

  /**
   * 初始化-删除旧数据
   */
  void initDelOldData() throws Exception;

  /**
   * 初始化-处理临时数据
   */
  void initDealTempData() throws Exception;

  /**
   * dealing-处理项目数据
   */
  void dealPrjData() throws Exception;

  /**
   * dealing-处理申请数据
   */
  void dealPrpData() throws Exception;

  /**
   * dealing-资助率
   */
  void dealFundRateData() throws Exception;

  /**
   * dealing-处理论文数据
   */
  void dealPaperData() throws Exception;

  /**
   * dealing-处理专利数据
   */
  void dealPatentData() throws Exception;

  /**
   * 清理临时数据 如果只要跑部分数据，建议注释该方法，然后手动清理要重跑的数据
   * 
   * @throws Exception
   */
  void initDelTempData();



}
