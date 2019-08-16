package com.smate.center.batch.service.pub;

import java.util.List;
import java.util.Set;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.sns.pub.ExpertPubModel;
import com.smate.center.batch.model.sns.pub.NsfcExpertPub;
import com.smate.center.batch.model.sns.pub.PublicationForm;
import com.smate.center.batch.model.sns.pub.SyncProposalModel;

/**
 * 评议专家成果
 * 
 * @author oyh
 * 
 */
public interface ExpertPubService {

  /**
   * 评议专家：从我的成果->代表性论著
   * 
   * @param form
   * @throws ServiceException
   */
  void addPublicationFromMyMate(ExpertPubModel form) throws ServiceException;

  /**
   * 加载专家成果
   * 
   * @return
   * @throws ServiceException
   */
  List<NsfcExpertPub> getMyExpertPubs() throws ServiceException;

  /**
   * 移除专家成果
   * 
   * @param form
   * @throws ServiceException
   */
  void removeExpertPubs(ExpertPubModel form) throws ServiceException;

  /**
   * 保存专家成果
   * 
   * @param form
   * @throws ServiceException
   */
  void saveExpertPubs(ExpertPubModel form) throws ServiceException;

  /**
   * 更新 是否标注OPEN TAG
   * 
   * @param isTag
   * @param pubId
   * @param rptId
   * @throws ServiceException
   */
  void saveExpertPubTag(Integer isTag, Long key) throws ServiceException;

  /**
   * 更新专家成果收录情况
   * 
   * @param citation
   * @param key
   * @throws ServiceException
   */
  void saveExpertPubCitation(String citation, Long key) throws ServiceException;

  /**
   * 保存专家成果排序
   * 
   * @param form
   * @throws ServiceException
   */
  void savePublicationSort(ExpertPubModel form) throws ServiceException;

  /**
   * 查询已加入专家成果Id
   * 
   * @return
   * @throws ServiceException
   */
  Set<Long> getExpertPubIds() throws ServiceException;

  void syncPublicationToExpertuPub(PublicationForm loadXml) throws ServiceException;

  /**
   * 加载评议专家成果
   * 
   * @param model
   * @return
   * @throws ServiceException
   */
  List<NsfcExpertPub> loadExpertPubsByGuid(SyncProposalModel model) throws ServiceException;

  /**
   * 根据pubId查找专家成果
   * 
   * @param pubId
   * @return
   * @throws ServiceException
   */
  List<NsfcExpertPub> findNsfcExpertPubsByPubId(Long pubId) throws ServiceException;

  /**
   * 保存专家成果
   * 
   * @param nsfcExpertPubList
   * @throws ServiceException
   */
  void saveNsfcExpertPubList(List<NsfcExpertPub> nsfcExpertPubList) throws ServiceException;

}
