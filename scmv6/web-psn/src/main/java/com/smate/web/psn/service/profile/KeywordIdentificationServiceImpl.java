package com.smate.web.psn.service.profile;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.psn.model.profile.KeywordIdentification;
import com.smate.web.psn.dao.profile.KeywordIdentificationDao;
import com.smate.web.psn.dao.profile.PsnDisciplineKeyDao;
import com.smate.web.psn.dao.profile.RecommandKwDropHistoryDao;
import com.smate.web.psn.dao.psnrefreshinfo.PsnKwRmcDao;
import com.smate.web.psn.exception.PsnException;
import com.smate.web.psn.model.profile.RecommandKwDropHistory;

/**
 * 研究领域关键词认同Service
 * 
 * @author zyx
 * 
 */
@Service("keywordIdentificationService")
@Transactional(rollbackFor = Exception.class)
public class KeywordIdentificationServiceImpl implements KeywordIdentificationService {

  /**
   * 
   */
  private static final long serialVersionUID = -8095600241828834142L;

  private static final int keyLength = 150;

  @Autowired
  private PsnKwRmcDao psnKwRmcDao;

  @Autowired
  private RecommandKwDropHistoryDao dropHistoryDao;

  @Autowired
  private PsnDisciplineKeyDao disciplineKeyDao;

  @Autowired
  private KeywordIdentificationDao identificationDao;

  @Autowired
  private PersonManager personManager;

  public void recordDropRmKeyword(Long psnId, String keyword) throws PsnException {
    if (StringUtils.isNotBlank(keyword)) {
      String kwTxt = keyword.trim().toLowerCase();
      boolean isExists = psnKwRmcDao.isExists(psnId, kwTxt);
      if (isExists) {
        RecommandKwDropHistory entity = dropHistoryDao.findByIdAndKw(psnId, kwTxt);
        if (entity == null) {
          entity = new RecommandKwDropHistory();
          entity.setDropDate(new Date());
          entity.setKeyword(keyword);
          entity.setKwTxt(kwTxt);
          entity.setPsnId(psnId);
          dropHistoryDao.save(entity);
        }
      }
    }
  }

  @Override
  public void identificKeyword(Long psnId, Long keywordId, Long friendId) throws PsnException {
    Integer sum = null;
    KeywordIdentification k = identificationDao.findKeywordIdentification(psnId, keywordId, friendId);
    if (k == null) {
      k = new KeywordIdentification();
      k.setPsnId(psnId);
      k.setKeywordId(keywordId);
      k.setFriendId(friendId);
      k.setOpDate(new Date());
      identificationDao.save(k);
    }
  }

  @Override
  public List<Object[]> countIdentification(Long psnId, List<Long> kwIdList) throws PsnException {
    return identificationDao.countIdentification(psnId, kwIdList);
  }

  @Override
  public Long countOneIdentification(Long psnId, Long kwId) throws PsnException {
    return identificationDao.countOneIdentification(psnId, kwId);
  }

}
