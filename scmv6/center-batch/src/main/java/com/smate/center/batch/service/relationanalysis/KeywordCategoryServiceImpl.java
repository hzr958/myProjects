package com.smate.center.batch.service.relationanalysis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.dao.sns.relationanalysis.KeywordCategoryDao;
import com.smate.center.batch.dao.sns.relationanalysis.KeywordsNewDao;
import com.smate.center.batch.model.sns.relationanalysis.KeywordCategory;
import com.smate.center.batch.model.sns.relationanalysis.KeywordsNew;

@Service("keywordCategoryService")
@Transactional(rollbackFor = Exception.class)
public class KeywordCategoryServiceImpl implements KeywordCategoryService {

  @Autowired
  private KeywordCategoryDao keywordCategoryDao;
  @Autowired
  private KeywordsNewDao keywordsNewDao;

  @Override
  public KeywordCategory getDataById(Integer id) {

    return keywordCategoryDao.getDataById(id);

  }

  @Override
  public void saveToKeywordsNew(KeywordsNew kws) {

    this.keywordsNewDao.save(kws);
  }

}
