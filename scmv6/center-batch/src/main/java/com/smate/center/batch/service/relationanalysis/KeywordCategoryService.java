package com.smate.center.batch.service.relationanalysis;

import com.smate.center.batch.model.sns.relationanalysis.KeywordCategory;
import com.smate.center.batch.model.sns.relationanalysis.KeywordsNew;

public interface KeywordCategoryService {

  public KeywordCategory getDataById(Integer id);

  public void saveToKeywordsNew(KeywordsNew kws);
}
