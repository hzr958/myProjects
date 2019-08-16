package com.smate.center.batch.service.pdwh.pubmatch;

import java.io.Serializable;
import java.util.List;

import com.smate.center.batch.model.pdwh.pub.cnki.CnkiPubAssign;
import com.smate.center.batch.model.pdwh.pub.cnki.CnkiPubMatchAssign;
import com.smate.center.batch.model.pdwh.pub.cnki.CnkiPubMatchAuthor;
import com.smate.center.batch.model.pdwh.pub.cnki.CnkiPubMatchJournal;
import com.smate.center.batch.model.pdwh.pub.cnki.CnkiPubMatchKeyword;

/**
 * CNKI成果拆分匹配表相关操作的业务逻辑实现接口.
 * 
 * @author mjg
 * 
 */
public interface CnkiPubMatchService extends Serializable {

  /**
   * 初始化保存成果匹配结果记录.
   * 
   * @param author
   * @param psnId
   */
  CnkiPubMatchAssign initCnkiPubMatchAssign(CnkiPubMatchAuthor author, Long psnId);

  /**
   * 排除用户已确认或已拒绝的成果.
   * 
   * @param psnId
   * @param pubIdList
   */
  void excludeMatchedPub(Long psnId, List<Long> pubInsList);

  /**
   * 获取CnkiPubAssign.
   * 
   * @param pubId
   * @param insId
   * @return
   */
  CnkiPubAssign getCnkiPubAssign(Long pubId, Long insId);

  /**
   * 查找单位的所有已匹配上机构的成果.
   * 
   * @param insId
   * @return
   */
  List<CnkiPubAssign> getCnkiPubAssign(Long insId);

  /**
   * 查找单位的所有已匹配上机构的成果ID.
   * 
   * @param insId
   * @param psnEnNameList
   * @param size
   * @return
   */
  List<Long> getCnkiPubAssignId(Long insId, List<String> psnEnNameList, int size);

  /**
   * 根据成果ID获取期刊的关键词和关键词的hash值列表.
   * 
   * @param pubId
   * @return
   */
  List<CnkiPubMatchKeyword> getMatchedKeywordByPubId(Long pubId);

  /**
   * 保存成果匹配结果记录.
   * 
   * @param assign
   */
  void saveCnkiPubMatchAssign(CnkiPubMatchAssign assign);

  /**
   * 获取成果的期刊列表.
   * 
   * @param pubId
   * @return
   */
  List<CnkiPubMatchJournal> getMatchedJournalList(Long pubId);

  /**
   * 根据人员ID获取匹配的成果ID列表.
   * 
   * @param psnId
   * @return
   */
  List<Long> getPubIdListByPsnId(Long psnId);

  /**
   * 获取CNKI已拆分成果表中的最大成果ID.
   * 
   * @return
   */
  Long getMaxPubMatchAssignId();

  /**
   * 获取用户匹配到的成果.
   * 
   * @param pubId
   * @param psnId
   * @return
   */
  CnkiPubMatchAssign getCnkiPubMatchAssign(Long pubId, Long psnId);

  /**
   * 获取成果的作者列表.
   * 
   * @param pubId
   * @return
   */
  List<CnkiPubMatchAuthor> getMatchedAuthorList(Long pubId);

  /**
   * 检查是否已匹配到成果.
   * 
   * @param psnId
   * @return true-已匹配到；false-未匹配到.
   */
  boolean isExistMatchedPub(Long psnId);

  /**
   * 删除已匹配到的待确认的成果.
   * 
   * @param psnId
   */
  void deleteUnFirmPub(Long psnId);

  /**
   * 获取用户名称获取匹配的成果ID记录.
   * 
   * @param pubId
   * @return
   */
  List<CnkiPubMatchAuthor> getCnkiPubMatchAuthorList(Long pubId, String name);

  /**
   * 保存成果拆分的作者记录.
   * 
   * @param author
   */
  void saveCnkiPubMatchAuthor(CnkiPubMatchAuthor author);

  /**
   * 根据成果ID和名称获取成果拆分的作者记录.
   * 
   * @param pubId
   * @param name
   * @return
   */
  CnkiPubMatchAuthor getCnkiPubMatchAuthor(Long pubId, String name);

  /**
   * 保存成果拆分的期刊记录.
   * 
   * @param journal
   */
  void saveCnkiPubMatchJournal(CnkiPubMatchJournal journal);

  /**
   * 根据成果ID和ISSN获取成果拆分的期刊记录.
   * 
   * @param pubId
   * @param issn
   * @return
   */
  CnkiPubMatchJournal getCnkiPubMatchJournal(Long pubId, String issn);

  /**
   * 保存成果拆分的关键词记录.
   * 
   * @param journal
   */
  void saveCnkiPubMatchKeyword(CnkiPubMatchKeyword keyword);

  /**
   * 根据成果ID和关键词Hash获取成果拆分的关键词记录.
   * 
   * @param pubId
   * @param name
   * @return
   */
  CnkiPubMatchKeyword getCnkiPubMatchKeyword(Long pubId, Long kwHash);

  /**
   * 保存匹配成果的合作者名称.
   * 
   * @param pubId
   * @param psnId
   * @param coFNameNum
   * @param score
   */
  void updateAssignCoName(Long pubId, Long psnId, Integer coFNameNum, Integer score);

  /**
   * 保存匹配成果的期刊.
   * 
   * @param pubId
   * @param psnId
   * @param journal
   * @param score
   */
  void updateAssignJnl(Long pubId, Long psnId, Integer journal, Integer score);

  /**
   * 保存匹配成果的关键词.
   * 
   * @param pubId
   * @param psnId
   * @param matchedKwNum
   * @param score
   */
  void updateAssignKw(Long pubId, Long psnId, Integer matchedKwNum, Integer score);
}
