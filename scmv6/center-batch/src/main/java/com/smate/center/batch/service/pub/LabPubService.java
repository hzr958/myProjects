package com.smate.center.batch.service.pub;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.sns.pub.LabPubModel;
import com.smate.center.batch.model.sns.pub.NsfcExpertPub;
import com.smate.center.batch.model.sns.pub.NsfcLabGroup;
import com.smate.center.batch.model.sns.pub.NsfcLabPub;
import com.smate.center.batch.model.sns.pub.PublicationForm;
import com.smate.center.batch.model.sns.pub.PublicationXml;
import com.smate.center.batch.model.sns.pub.StiasSyncGroup;
import com.smate.center.batch.model.sns.pub.SyncProposalModel;

/**
 * 评议专家成果
 * 
 * @author oyh
 * 
 */
public interface LabPubService {

  /**
   * 实验室成果：从我的成果->代表性论著
   * 
   * @param form
   * @throws ServiceException
   */
  int addPublicationFromMyMate(LabPubModel form) throws ServiceException;

  /**
   * 加载专家成果
   * 
   * @return
   * @throws ServiceException
   */
  List<NsfcLabPub> getMyLabPubs(Long pId) throws ServiceException;

  /**
   * 移除专家成果
   * 
   * @param form
   * @throws ServiceException
   */
  void removeLabPubs(LabPubModel form) throws ServiceException;

  /**
   * 保存专家成果
   * 
   * @param form
   * @throws ServiceException
   */
  void saveLabPubs(LabPubModel form) throws ServiceException;

  /**
   * 更新 是否标注OPEN TAG
   * 
   * @param isTag
   * @param pubId
   * @param rptId
   * @throws ServiceException
   */
  void saveLabPubTag(Integer isTag, Long key) throws ServiceException;

  /**
   * 更新专家成果收录情况
   * 
   * @param citation
   * @param key
   * @throws ServiceException
   */
  void saveLabPubCitation(String citation, Long key) throws ServiceException;

  /**
   * 保存专家成果排序
   * 
   * @param form
   * @throws ServiceException
   */
  void savePublicationSort(LabPubModel form) throws ServiceException;

  /**
   * 查询已加入专家成果Id
   * 
   * @return
   * @throws ServiceException
   */
  Set<Long> getLabPubIds(Long pId) throws ServiceException;

  void syncPublicationToLabPub(PublicationForm loadXml) throws ServiceException;

  /**
   * 加载评议专家成果
   * 
   * @param model
   * @return
   * @throws ServiceException
   */
  List<NsfcExpertPub> loadLabPubsByGuid(SyncProposalModel model) throws ServiceException;

  /**
   * 获取成果类型树状结构
   * 
   * @param list
   * @param needTop
   * @return
   * @throws ServiceException
   */
  String covertPubTypeTree(List<Map<String, String>> list, boolean needTop) throws ServiceException;

  List<NsfcLabGroup> findByLabPsnId(Long labPsnId) throws ServiceException;

  List<NsfcLabGroup> findByLabId(Long labId) throws ServiceException;

  /**
   * 保存实验室群组关系记录.
   * 
   * @param labGroup
   * @throws ServiceException
   */
  void syncSaveNsfcLabGroup(NsfcLabGroup labGroup) throws ServiceException;

  List<NsfcLabPub> loadLabPubsByLabId(Long labId, Integer year) throws ServiceException;

  PublicationXml getById(Long pubId) throws ServiceException;

  void updateLabStatus(StiasSyncGroup model) throws ServiceException;

  Long getRepPubsTotal(Long labPsnId, Long pId) throws ServiceException;

  NsfcLabGroup findLabById(Long pId) throws ServiceException;

  NsfcLabGroup findLab(Long labId, Integer year) throws ServiceException;

  /**
   * 获取实验室群组关系记录列表.
   * 
   * @param psnId
   * @return
   */
  List<NsfcLabGroup> getNsfcLabGroupList(Long psnId);

  /**
   * 删除实验室群组关系记录.
   * 
   * @param awardStatic
   */
  void delNsfcLabGroup(NsfcLabGroup labGroup);

  /**
   * 更新保存实验室群组关系记录.
   * 
   * @param labGroup
   */
  void saveNsfcLabGroup(NsfcLabGroup labGroup);
}
