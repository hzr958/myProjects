package com.smate.center.batch.service.rol.pub;

import java.util.Set;

import com.smate.center.batch.exception.pub.ServiceException;

/**
 * 成果状态跳转服务。（批准成果使用）.
 * 
 * @author yamingd
 * 
 */
public interface PublicationRolStatusTransService {

  /**
   * RO直接从个人库拉成果过来.
   * 
   * @param pubPsnPair 为({
   *        'snsPubId':12222,'psnId':23455}|{'snsPubId':122232,'psnId':23455}|{'snsPubId':122223,'psnId':2345
   *        3 5 } )
   * @throws ServiceException
   */
  void pullFromSNS(String pubPsnPair) throws ServiceException;

  /**
   * 批准等待批准的成果. set publication.status=2.
   * 
   * @param pubIds
   * @throws ServiceException
   */
  void approve(Set<Long> pubIds) throws ServiceException;

  /**
   * 拒绝等待批准的成果. set publication.status=3.
   * 
   * @param pubIds
   * @throws ServiceException
   */
  void reject(Set<Long> pubIds) throws ServiceException;

  /**
   * 删除指派成果时，删除提交数据.
   * 
   * @param psnPubId
   * @throws ServiceException
   */
  void deleteByDisAssign(Long psnPubId) throws ServiceException;

  /**
   * 拒绝已批准的成果. set publication.status=3.
   * 
   * @param pubIds
   * @throws ServiceException
   */
  void rejectApproved(Set<Long> pubIds) throws ServiceException;

  /**
   * 删除单位成果. set publication.status=3.
   * 
   * @param pubId
   * @param functionName
   * @throws ServiceException
   */
  void delete(Long pubId, String functionName) throws ServiceException;

  /**
   * 删除ISI指派错误的成果.
   * 
   * @param xmlId
   * @param insId
   * @throws ServiceException
   */
  void delIsiMatchPub(Long xmlId, Long insId) throws ServiceException;

  /**
   * 删除PUBMED指派错误的成果.
   * 
   * @param xmlId
   * @param insId
   * @throws ServiceException
   */
  void delPubMedMatchPub(Long xmlId, Long insId) throws ServiceException;

  /**
   * 删除scopus指派错误的成果.
   * 
   * @param xmlId
   * @param insId
   * @throws ServiceException
   */
  void delSpsMatchPub(Long xmlId, Long insId) throws ServiceException;

  /**
   * 作废，保留，之前合并成果需要设置作者匹配到的人，合并单位成果. set othersPubId.status=3.
   * 
   * @param mainPubId 保留的成果
   * @param othersPubId 删除的成果
   * @param pubMemberMap
   * @throws ServiceException
   */
  void merge(Long mainPubId, Set<Long> othersPubId, String pubMemberMap) throws ServiceException;

  /**
   * 合并单位成果. set othersPubId.status=3.
   * 
   * @param mainPubId 保留的成果
   * @param othersPubId 删除的成果
   * @throws ServiceException
   */
  void merge(Long mainPubId, Set<Long> othersPubId) throws ServiceException;

  /**
   * 同意R的申请撤销请求.
   * 
   * @param pubIds
   * @throws ServiceException
   */
  void approveWithdrawReq(Set<Long> pubIds) throws ServiceException;

  /**
   * 拒绝R的申请撤销请求.
   * 
   * @param pubIds
   * @throws ServiceException
   */
  void rejectWithdrawReq(Set<Long> pubIds) throws ServiceException;

  public void delEiMatchPub(Long xmlId, Long insId) throws ServiceException;

  public void delPdwhMatchPub(Long xmlId, Long insId);
}
