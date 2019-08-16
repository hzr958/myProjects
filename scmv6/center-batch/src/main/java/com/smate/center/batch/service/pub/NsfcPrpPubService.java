package com.smate.center.batch.service.pub;

import java.util.List;
import java.util.Set;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.sns.pub.NsfcProposal;
import com.smate.center.batch.model.sns.pub.NsfcPrpPub;
import com.smate.center.batch.model.sns.pub.NsfcPrpPubModel;
import com.smate.center.batch.model.sns.pub.PublicationForm;
import com.smate.center.batch.model.sns.pub.SyncProposalModel;
import com.smate.core.base.utils.model.Page;

/**
 * 杰青申报成果模块
 * 
 * @author oyh
 * 
 */
public interface NsfcPrpPubService {
  /**
   * 获取对应的申报书列表
   * 
   * @return
   * @throws ServiceException
   */
  Page getProposalsByPsnId() throws ServiceException;

  /**
   * 根据申报书isisGuid获取申报项目信息
   * 
   * @param isisGuid
   * @return
   * @throws ServiceException
   */
  NsfcProposal getPrpByIsisGuid(String isisGuid, Long psnId) throws ServiceException;

  /**
   * 根据申报书Id获取杰青论著列表
   * 
   * @param prpId
   * @return
   * @throws ServiceException
   */
  List<NsfcPrpPub> getPrpPubsById(String isisGuid) throws ServiceException;

  /**
   * 根据申报书Id获取杰青论著列表
   * 
   * @param prpId
   * @return
   * @throws ServiceException
   */
  List<NsfcPrpPub> getPubsOrderByType(String isisGuid) throws ServiceException;

  /**
   * 查询已加入的论著
   * 
   * @param prpId
   * @return
   * @throws ServiceException
   */
  Set<Long> getPubIdsByGuid(String isisGuid) throws ServiceException;

  /**
   * 更新论著类型
   * 
   * @param prpPubKey
   * @param dType
   * @throws ServiceException
   */
  String updateDelieveType(Long prpPubKey, Integer dType) throws ServiceException;

  /**
   * 从我的成果库添加论著
   * 
   * @param form
   * @throws ServiceException
   */
  String addPublicationFromMyMate(NsfcPrpPubModel form) throws ServiceException;

  /**
   * 同步杰青成果数据
   * 
   * @param loadXml
   * @throws ServiceException
   */
  void syncPublicationToProposal(PublicationForm loadXml) throws ServiceException;

  /**
   * 保存杰青成果
   * 
   * @param form
   * @throws ServiceException
   */
  void saveProposalPublication(NsfcPrpPubModel form) throws ServiceException;

  /**
   * 移除杰青成果
   * 
   * @param form
   * @throws ServiceException
   */
  void removeProjectProposalPub(NsfcPrpPubModel form) throws ServiceException;

  /**
   * 读取杰青申报书的论著列表
   * 
   * @param guid
   * @param psnId
   * @return
   * @throws ServiceException
   */
  List<NsfcPrpPub> loadPrpPubsByGuid(SyncProposalModel model) throws ServiceException;

  /**
   * 同步保存一份isisGuid
   * 
   * @param isisGuid
   * @throws ServiceException
   */
  void syncSaveIsisGuid(SyncProposalModel model) throws ServiceException;

  /**
   * 保存杰青申报书
   * 
   * @param model
   * @throws ServiceException
   */
  void syncSaveProposal(SyncProposalModel model) throws ServiceException;

  /**
   * 根据guid保存申报书信息
   * 
   * @param guid
   * @throws ServiceException
   */
  void saveProposal(String guid) throws ServiceException;

  /**
   * 查询申报书是否可编辑
   * 
   * @param isisGuid
   * @return
   * @throws ServiceException
   */
  boolean isCanEditable(String isisGuid) throws ServiceException;

  /**
   * 近五年代表性论著统计
   */
  Long getRepPubsTotal(String isisGuid) throws ServiceException;

  /**
   * 更新收录情况
   * 
   * @param isisGuid
   * @param pubId
   * @throws ServiceException
   */
  void updatePrpPubCitation(String isisGuid, Long pubId, String citation) throws ServiceException;

  /**
   * 对添加到专家成果进行排序.
   * 
   * @param form
   * @throws ServiceException
   */
  void savePublicationSort(NsfcPrpPubModel form) throws ServiceException;

  /**
   * 检查排序是不是最新的数据（防止打开2个窗口进行排序）
   * 
   * @param form
   * @return
   * @throws ServiceException
   */
  boolean checkSort(NsfcPrpPubModel form) throws ServiceException;

  /**
   * 根据PubId获取申请书成果
   * 
   * @param pubId
   * @return
   * @throws ServiceException
   */
  List<NsfcPrpPub> findNsfcPrpPubByPubId(Long pubId) throws ServiceException;

  /**
   * 保存申请书成果
   * 
   * @param nsfcPrpPubList
   * @throws ServiceException
   */
  public void saveNsfcPrpPub(NsfcPrpPub nsfcPrpPub) throws ServiceException;

  /**
   * 获取杰青申报书报表记录列表.
   * 
   * @param psnId
   * @return
   */
  List<NsfcProposal> getNsfcProposalList(Long psnId);

  /**
   * 删除杰青申报书报表记录.
   * 
   * @param awardStatic
   */
  void delNsfcProposal(NsfcProposal proposal);

  /**
   * 更新保存杰青申报书报表记录.
   * 
   * @param proposal
   */
  void saveNsfcProposal(NsfcProposal proposal);
}
