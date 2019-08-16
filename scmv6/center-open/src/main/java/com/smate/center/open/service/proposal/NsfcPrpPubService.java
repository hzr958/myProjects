package com.smate.center.open.service.proposal;

import java.util.List;

import com.smate.center.open.model.nsfc.SyncProposalModel;
import com.smate.center.open.model.proposal.NsfcProposal;
import com.smate.center.open.model.proposal.NsfcPrpPub;


/**
 * 杰青申报成果模块
 * 
 * @author tsz
 * 
 */
public interface NsfcPrpPubService {


  /**
   * 保存杰青申报书
   * 
   * @param model @
   */
  void syncSaveProposal(SyncProposalModel model) throws Exception;


  /**
   * 同步保存一份isisGuid
   * 
   * @param isisGuid
   * @throws ServiceException
   */
  public void syncSaveIsisGuid(SyncProposalModel model) throws Exception;


  /**
   * 根据申报书isisGuid获取申报项目信息
   * 
   * @param isisGuid
   * @return
   * @throws ServiceException
   */
  NsfcProposal getPrpByIsisGuid(String guid, Long psnId) throws Exception;


  /**
   * 读取杰青申报书的论著列表
   * 
   * @param guid
   * @param psnId
   * @return
   * @throws ServiceException
   */
  List<NsfcPrpPub> loadPrpPubsByGuid(SyncProposalModel model) throws Exception;



}
