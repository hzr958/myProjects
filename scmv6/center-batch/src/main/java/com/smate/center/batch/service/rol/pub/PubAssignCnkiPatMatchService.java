package com.smate.center.batch.service.rol.pub;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.rol.pub.PubAssignCnkiPatScore;
import com.smate.center.batch.model.rol.pub.PublicationRol;
import com.smate.center.batch.model.rol.pub.SettingPubAssignScoreWrap;

/**
 * CnkiPat成果匹配服务.
 * 
 * @author liqinghua
 * 
 */
public interface PubAssignCnkiPatMatchService extends Serializable {

  /**
   * 按成果匹配人员.
   * 
   * @param pub
   * @param insId
   * @throws ServiceException
   */
  public void assignByPub(PublicationRol pub, Long insId) throws ServiceException;

  /**
   * 按单位人员匹配成果.
   * 
   * @param psnId
   * @param insId
   * @throws ServiceException
   */
  public void assignByPsn(Long psnId, Long insId) throws ServiceException;

  /**
   * 获取匹配上cnki成果作者名称的单位人员.
   * 
   * @param pubId
   * @param psnId
   * @return
   * @throws ServiceException
   */
  public List<Object[]> getCnkiPatNameMatchPubAuthor(Long pubId, Long insId) throws ServiceException;

  /**
   * 获取匹配上单位人员的cnki成果列表.
   * 
   * @param psnId
   * @param insId
   * @return
   * @throws ServiceException
   */
  public List<Object[]> getCnkiPatPubAuthorMatchPsn(Long psnId, Long insId) throws ServiceException;

  /**
   * 获取匹配上成果关键词的指定人员ID与关键词列表.
   * 
   * @param pubId
   * @param psnIds
   * @return
   * @throws ServiceException
   */
  public Map<Long, Long> getKwMatchPubKw(Long pubId, Set<Long> psnIds) throws ServiceException;

  /**
   * 获取匹配上成果作者的指定用户的合作者个数.
   * 
   * @param pubId
   * @param psnIds
   * @return
   * @throws ServiceException
   */
  public Map<Long, Long> getCnkiPatConMatchPubAuth(Long pubId, Set<Long> psnIds) throws ServiceException;

  /**
   * 保存成果匹配结果.
   * 
   * @param pubAssignScore
   * @throws ServiceException
   */
  public void saveAssignScore(PubAssignCnkiPatScore pubAssignScore) throws ServiceException;

  /**
   * 获取SettingPubAssignScore.
   * 
   * @param dbId
   * @return
   * @throws ServiceException
   */
  public SettingPubAssignScoreWrap getSettingPubAssignScore(Long dbId) throws ServiceException;

  /**
   * 获取指定序号、机构ID的成果部门.
   * 
   * @param seqNo
   * @param pubId
   * @param insId
   * @return
   * @throws ServiceException
   */
  public Map<Integer, String> getInsPubDept(List<Integer> seqNos, Long pubId, Long insId) throws ServiceException;

  /**
   * 成果作者部门是否匹配指定用户所在部门.
   * 
   * @param psnId
   * @param insId
   * @param deptName
   * @return
   * @throws ServiceException
   */
  public boolean isPubDeptMatchPsnUnit(Long psnId, Long insId, String deptName) throws ServiceException;

  /**
   * 删除人员所在单位匹配关系.
   * 
   * @param psnId
   * @param insId
   * @throws ServiceException
   */
  public void removePubAssignScore(Long psnId, Long insId) throws ServiceException;

  /**
   * 删除成果匹配关系.
   * 
   * @param pubId
   * @throws ServiceException
   */
  public void removePubAssignScore(Long pubId) throws ServiceException;

  /**
   * 删除成果匹配关系.
   * 
   * @param pubId
   * @throws ServiceException
   */
  public void removePubPsnAssignScore(Long pubId, Long psnId) throws ServiceException;
}
