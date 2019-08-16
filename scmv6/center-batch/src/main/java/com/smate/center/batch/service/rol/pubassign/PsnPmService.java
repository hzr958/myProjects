package com.smate.center.batch.service.rol.pubassign;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.rol.psn.PsnPmJournalRol;
import com.smate.center.batch.model.rol.psn.RolPsnIns;
import com.smate.center.batch.model.rol.pub.PsnPmCnkiConame;
import com.smate.center.batch.model.rol.pub.PsnPmConference;
import com.smate.center.batch.model.rol.pub.PsnPmEmailRol;
import com.smate.center.batch.model.rol.pub.PsnPmKeyWordRol;
import com.smate.center.batch.model.rol.pub.SettingPubAssignKwWt;
import com.smate.center.batch.model.rol.pub.SettingPubAssignScore;

/**
 * 成果匹配用户信息service.
 * 
 * @author liqinghua
 * 
 */
public interface PsnPmService extends Serializable {

  /**
   * 添加用户确认邮件.
   * 
   * @param email
   * @param psnId
   * @return
   * @throws ServiceException
   */
  public PsnPmEmailRol savePsnPmEmail(String email, Long psnId) throws ServiceException;

  /**
   * 删除用户邮件.
   * 
   * @param email
   * @param psnId
   * @throws ServiceException
   */
  public void removePsnPmEmail(String email, Long psnId) throws ServiceException;

  /**
   * 保存会议名称.
   * 
   * @param name
   * @param psnId
   * @return
   * @throws ServiceException
   */
  public PsnPmConference savePsnPmConference(String name, Long psnId) throws ServiceException;

  /**
   * 保存用户ISI合作者姓名.
   * 
   * @param name
   * @param psnId
   * @throws ServiceException
   */
  public void savePsnPmIsiConame(String initName, String fullName, Long psnId) throws ServiceException;

  /**
   * 保存用户scopus合作者姓名.
   * 
   * @param name
   * @param psnId
   * @throws ServiceException
   */
  public void savePsnPmSpsConame(String name, Long psnId) throws ServiceException;

  /**
   * 保存用户scopus合作者姓名前缀.
   * 
   * @param name
   * @param psnId
   * @throws ServiceException
   */
  public void savePsnPmSpsCoPreName(String preName, Long psnId) throws ServiceException;

  /**
   * 保存用户Cnki合作者姓名.
   * 
   * @param name
   * @param psnId
   * @return
   * @throws ServiceException
   */
  public PsnPmCnkiConame savePsnPmCnkiConame(String name, Long psnId) throws ServiceException;

  /**
   * 保存用户确认成果ISI名称.
   * 
   * @param initName
   * @param fullName
   * 
   * @param psnId
   * @throws ServiceException
   */
  public void saveAddtPsnPmIsiName(String initName, String fullName, Long psnId) throws ServiceException;

  /**
   * 保存用户确认成果ISI名称.
   * 
   * @param name
   * @param psnId
   * @throws ServiceException
   */
  public void saveAddtPsnPmCnkiName(String name, Long psnId) throws ServiceException;

  /**
   * 保存用户确认期刊信息.
   * 
   * @param jid
   * @param jname
   * @param issn
   * @param psnId
   * @return
   * @throws ServiceException
   */
  public PsnPmJournalRol savePsnPmJournal(Long jid, String jname, String issn, Long psnId) throws ServiceException;

  /**
   * 保存用户确认关键词信息.
   * 
   * @param keyword
   * @param psnId
   * @return
   * @throws ServiceException
   */
  public PsnPmKeyWordRol savePsnPmKeyWord(String keyword, Long psnId) throws ServiceException;

  /**
   * 获取关键词权重配置信息.
   * 
   * @return
   * @throws ServiceException
   */
  public List<SettingPubAssignKwWt> getSettingPubAssignKwWt() throws ServiceException;

  /**
   * 获取成果库指派分数配置表.
   * 
   * @param dbid
   * @return
   * @throws ServiceException
   */
  public SettingPubAssignScore getSettingPubAssignScore(Long dbid) throws ServiceException;

  /**
   * 生成用户isi名称.
   * 
   * @param psnId
   * @throws ServiceException
   */
  public void generalPsnPmIsiName(Long psnId) throws ServiceException;

  /**
   * 保存用户CNKI中文名.
   * 
   * @param zhName
   * @param psnId
   * @throws ServiceException
   */
  public void saveUserCnkiZhName(String zhName, Long psnId) throws ServiceException;

  /**
   * 获取用户全称列表.
   * 
   * @param psnIds
   * @return
   * @throws ServiceException
   */
  public Map<Long, List<String>> getUserFullName(Set<Long> psnIds) throws ServiceException;

  /**
   * generalHashPubKeywordsSplit.
   * 
   * @param startId
   * @return
   * @throws ServiceException
   */
  public Long generalHashPubKeywordsSplit(Long startId) throws ServiceException;

  /**
   * 删除人员别名.
   * 
   * @param names
   * @param psnId
   * @return
   * @throws ServiceException
   */
  public void removePsnPmName(List<String> names, Long psnId) throws ServiceException;

  /**
   * 人员是否存在ISI别名.
   * 
   * @param psnId
   * @return
   * @throws ServiceException
   */
  public boolean existsPsnPmName(Long psnId) throws ServiceException;

  public void generalPsnPmIsiName(RolPsnIns psnIns) throws ServiceException;

}
