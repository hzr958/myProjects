package com.smate.center.batch.service.rol.pub;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.rol.psn.PsnPmJournalRol;
import com.smate.center.batch.model.rol.pub.PsnPmConference;
import com.smate.center.batch.model.rol.pub.PubAssignEiScore;
import com.smate.center.batch.model.rol.pub.PublicationRol;
import com.smate.center.batch.model.rol.pub.SettingPubAssignScoreWrap;

/**
 * Ei成果匹配服务.
 * 
 * @author liqinghua
 * 
 */
public interface PubAssignEiMatchService extends Serializable {

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
   * 获取匹配上成果作者email的指定人员ID.
   * 
   * @param pubId
   * @param psnIds
   * @return
   * @throws ServiceException
   */
  public List<Object[]> getEmailMatchPubEmail(Long pubId, Long insId) throws ServiceException;

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
   * 获取匹配上成果期刊的指定人员ID与期刊列表.
   * 
   * @param pubId
   * @param psnIds
   * @return
   * @throws ServiceException
   */
  public Map<Long, PsnPmJournalRol> getPJMatchPubPJ(Long pubId, Set<Long> psnIds) throws ServiceException;

  /**
   * 获取匹配上成果会议论文的指定人员ID与期刊列表.
   * 
   * @param pubId
   * @param psnIds
   * @return
   * @throws ServiceException
   */
  public Map<Long, PsnPmConference> getPCMatchPubPc(Long pubId, Set<Long> psnIds) throws ServiceException;

  /**
   * 获取匹配上成果发表年份的指定人员的工作经历用户ID.
   * 
   * @param insId
   * @param pubYear
   * 
   * @param psnIds
   * @return
   * @throws ServiceException
   */
  public List<Long> getWkhMatchPubYear(Long insId, Integer pubYear, Set<Long> psnIds) throws ServiceException;


  /**
   * 保存成果匹配结果.
   * 
   * @param pubAssignScore
   * @throws ServiceException
   */
  public void saveAssignScore(PubAssignEiScore pubAssignScore) throws ServiceException;

  /**
   * 获取SettingPubAssignScore.
   * 
   * @param dbId
   * @return
   * @throws ServiceException
   */
  public SettingPubAssignScoreWrap getSettingPubAssignScore(Long dbId) throws ServiceException;

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

  /**
   * 获取匹配上isi成果作者名称的单位人员.
   * 
   * @param pubId
   * @param psnId
   * @return
   * @throws ServiceException
   */
  public List<Object[]> getEiPrefixNameMatchPubAuthor(Long pubId, Long insId) throws ServiceException;

  public List<Object[]> getEiFullNameMatchPubAuthor(Long pubId, Set<Long> psnIds, Long insId) throws ServiceException;

  public Map<Long, Long> getEiConMatchPubAuth(Long pubId, Set<Long> psnIds) throws ServiceException;

  public Map<Long, Long> getEiCoeMatchPubAuth(Long pubId, Set<Long> psnIds) throws ServiceException;

  public List<Object[]> getEiInitNameMatchPubAuthor(Long pubId, Long insId, Set<Long> psnIds) throws ServiceException;
}
