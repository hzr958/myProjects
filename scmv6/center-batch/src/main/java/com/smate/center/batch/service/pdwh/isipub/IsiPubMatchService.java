package com.smate.center.batch.service.pdwh.isipub;

import java.io.Serializable;
import java.util.List;

import com.smate.center.batch.model.pdwh.pub.isi.IsiPubAssign;
import com.smate.center.batch.model.pdwh.pub.isi.IsiPubMatchAssign;
import com.smate.center.batch.model.pdwh.pub.isi.IsiPubMatchAuthor;
import com.smate.center.batch.model.pdwh.pub.isi.IsiPubMatchEmail;
import com.smate.center.batch.model.pdwh.pub.isi.IsiPubMatchJournal;
import com.smate.center.batch.model.pdwh.pub.isi.IsiPubMatchKeyword;

/**
 * ISI成果拆分匹配表相关操作的业务逻辑实现接口.
 * 
 * @author mjg
 * 
 */
public interface IsiPubMatchService extends Serializable {

  /**
   * 初始化ISI匹配结果.
   * 
   * @param pubId
   * @param psnId
   * @return
   */
  IsiPubMatchAssign initIsiPubMatchAssign(Long pubId, Long psnId);

  /**
   * 获取ISI匹配结果记录.
   * 
   * @param pubId
   * @param psnId
   * @return
   */
  IsiPubMatchAssign getIsiPubMatchAssign(Long pubId, Long psnId);

  /**
   * 排除用户已确认或已拒绝的成果.
   * 
   * @param psnId
   * @param pubIdList
   */
  void excludeMatchedPub(String psnId, List<Long> pubIdList);

  /**
   * 补充人员信息.
   * 
   * @param pubId
   * @param author 成果作者.
   * @param authorList 成果所有作者列表.
   */
  void supplePsnPmInfo(Long pubId, Long psnId, IsiPubMatchAuthor author, List<IsiPubMatchAuthor> authorList);

  /**
   * 获取IsiPubAssign.
   * 
   * @param pubId
   * @param insId
   * @return
   */
  IsiPubAssign getIsiPubAssign(Long pubId, Long insId);

  /**
   * 查找单位所有已匹配上的成果.
   * 
   * @param insId
   * @return
   */
  List<IsiPubAssign> getIsiPubAssignList(Long insId);

  /**
   * 根据人员ID获取匹配的成果ID列表.
   * 
   * @param psnId
   * @return
   */
  List<Long> getPubIdListByPsnId(Long psnId);

  /**
   * 查找单位所有已匹配上的成果ID.
   * 
   * @param insId
   * @return
   */
  List<Long> getIsiPubAssignIdList(Long insId);

  /**
   * 根据邮件获取成果ID列表.
   * 
   * @param email
   * @return
   */
  List<Long> getPubIdListByEmail(String email);

  /**
   * 根据成果ID获取成果作者的邮件和用户序号列表.
   * 
   * @param pubId
   * @return
   */
  List<IsiPubMatchEmail> getEmailListByPubId(Long pubId);

  /**
   * 检查是否已匹配到成果.
   * 
   * @param psnId
   * @return true-已匹配到；false-未匹配到.
   */
  boolean isExistMatchedPub(Long psnId);

  /**
   * 删除匹配到的未确认的成果.
   * 
   * @param psnId
   */
  void deleteUnFirmPub(Long psnId);

  /**
   * 保存成果匹配结果记录.
   * 
   * @param assign
   */
  void saveIsiPubMatchAssign(IsiPubMatchAssign assign);

  /**
   * 获取成果的作者列表.
   * 
   * @param pubId
   * @return
   */
  List<IsiPubMatchAuthor> getIsiPubMatchAuthorList(Long pubId);

  /**
   * 根据成果ID获取期刊的ISSN和ISSN的hash值列表.
   * 
   * @param pubId
   * @return
   */
  List<IsiPubMatchJournal> getMatchedJournalList(Long pubId);

  /**
   * 根据成果ID获取期刊的关键词hash值列表.
   * 
   * @param pubId
   */
  List<IsiPubMatchKeyword> getPubKWList(Long pubId);

  /**
   * 保存成果拆分的邮件.
   * 
   * @param email
   */
  void saveIsiPubMatchEmail(IsiPubMatchEmail email);

  /**
   * 根据成果ID和邮件获取成果拆分的邮件记录.
   * 
   * @param pubId
   * @param email
   * @return
   */
  IsiPubMatchEmail getIsiPubMatchEmail(Long pubId, String email);

  /**
   * 保存成果拆分的作者.
   * 
   * @param author
   */
  void saveIsiPubMatchAuthor(IsiPubMatchAuthor author);

  /**
   * 根据成果ID和名称获取成果拆分的作者记录.
   * 
   * @param pubId
   * @param initName
   * @return
   */
  IsiPubMatchAuthor getIsiPubMatchAuthor(Long pubId, String initName);

  /**
   * 保存成果拆分的期刊.
   * 
   * @param journal
   */
  void saveIsiPubMatchJournal(IsiPubMatchJournal journal);

  /**
   * 根据成果ID和ISSN获取成果拆分的期刊记录.
   * 
   * @param pubId
   * @param fullName
   * @return
   */
  IsiPubMatchJournal getIsiPubMatchJournal(Long pubId, String issn);

  /**
   * 保存成果拆分的关键词.
   * 
   * @param journal
   */
  void saveIsiPubMatchKeyword(IsiPubMatchKeyword keyword);

  /**
   * 根据成果ID和关键词Hash获取成果拆分的关键词记录.
   * 
   * @param pubId
   * @param fullName
   * @return
   */
  IsiPubMatchKeyword getIsiPubMatchKeyword(Long pubId, Long kwHash);

  /**
   * 获取和人员名称匹配的单位成果.
   * 
   * @param insId
   * @param psnEnNameList
   * @param size
   * @return
   */
  List<Long> getMatchAuPubList(Long insId, List<String> psnEnNameList, int size);

  /**
   * 保存匹配成果的合作者邮件.
   * 
   * @param psnId
   * @param pubId
   * @param emailMatchedNum
   * @param score
   */
  void updateAssignCoEmail(Long psnId, Long pubId, Integer emailMatchedNum, Integer score);

  /**
   * 保存匹配成果的合作者名称.
   * 
   * @param psnId
   * @param pubId
   * @param coFNameNum
   * @param coINameNum
   * @param score
   */
  void updateAssignCoName(Long psnId, Long pubId, Integer coFNameNum, Integer coINameNum, Integer score);

  /**
   * 保存匹配成果的期刊.
   * 
   * @param psnId
   * @param pubId
   * @param jnlNum
   * @param score
   */
  void updateAssignJnl(Long psnId, Long pubId, Integer jnlNum, Integer score);

  /**
   * 保存匹配成果的关键词.
   * 
   * @param psnId
   * @param pubId
   * @param matchedKwNum
   * @param score
   */
  void updateAssignKw(Long psnId, Long pubId, Integer matchedKwNum, Integer score);
}
