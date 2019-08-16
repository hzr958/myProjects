package com.smate.web.psn.service.profile;

import java.util.List;
import java.util.Locale;

import com.smate.core.base.consts.model.ConstKeyDisc;
import com.smate.core.base.psn.model.profile.PsnDisciplineKey;
import com.smate.web.psn.exception.PsnException;
import com.smate.web.psn.exception.ServiceException;
import com.smate.web.psn.model.homepage.PersonProfileForm;

/**
 * 关键词
 *
 * @author wsn
 * @createTime 2017年3月13日 下午4:07:54
 *
 */
public interface PsnDisciplineKeyService {

  /**
   * 查找人员关键词
   * 
   * @param psnId
   * @return
   */
  List<PsnDisciplineKey> findPsnKeyWords(Long psnId);

  /**
   * 查询人员推荐关键词
   * 
   * @param psnId
   * @param size
   * @return
   */
  List<String> findPsnRecommendKeyWords(Long psnId, Locale locale, List<PsnDisciplineKey> psnDisKeyList);

  /**
   * 保存人员关键词
   * 
   * @param form
   * @return
   */
  String savePsnKeywordsByForm(PersonProfileForm form);

  /**
   * 保存人员关键词
   * 
   * @param psnId
   * @param keyStr
   * @return
   */
  List<String> savePsnKeywords(Long psnId, String keyStr, Integer permission);

  /**
   * 将删除的关键词放入关键词删除历史表
   * 
   * @param psnId
   * @param keyword
   */
  void recordDropRmKeyword(Long psnId, String keyword);

  /**
   * 获取关键词认同者头像
   * 
   * @param psnId
   * @param key
   * @return
   * @throws ServiceException
   */
  public PsnDisciplineKey findSomeIdentifyKwPsnIds(Long psnId, PsnDisciplineKey key) throws ServiceException;

  /**
   * 移动端保存人员关键词
   * 
   * @param psnId
   * @param keyStr
   * @param permission
   * @return
   * @throws PsnException
   */
  public List<String> mobileSavePsnKeywords(Long psnId, List<String> keyList, Integer permission) throws PsnException;

  /**
   * 查找人员关键词，只查关键词名称
   * 
   * @param psnId
   * @return
   * @throws PsnException
   */
  public List<PsnDisciplineKey> findPsnEffectiveKeywods(Long psnId) throws PsnException;

  /**
   * 自动提示关键词
   * 
   * @param searchKey
   * @param size
   * @return
   * @throws PsnException
   */
  public List<ConstKeyDisc> getConstKeyDiscs(String searchKey, Integer size) throws PsnException;
}
