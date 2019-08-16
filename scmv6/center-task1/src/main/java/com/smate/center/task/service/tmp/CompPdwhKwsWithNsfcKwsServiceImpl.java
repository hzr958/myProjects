package com.smate.center.task.service.tmp;

import com.smate.center.task.dao.pdwh.quartz.PdwhPubKeywordsSplitDao;
import com.smate.center.task.dao.tmp.TmpNsfcKeywordsDao;
import com.smate.center.task.dao.tmp.TmpNsfcKwsTaskRecordDao;
import com.smate.center.task.dao.tmp.TmpPdwhNsfcKwsMatchedDao;
import com.smate.center.task.model.pdwh.pub.PdwhPubKeywordsSplit;
import com.smate.center.task.model.tmp.TmpNsfcKeywords;
import com.smate.center.task.model.tmp.TmpNsfcKwsTaskRecord;
import com.smate.center.task.model.tmp.TmpPdwhNsfcKwsMatched;
import com.smate.core.base.utils.pubHash.PubHashUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class CompPdwhKwsWithNsfcKwsServiceImpl implements CompPdwhKwsWithNsfcKwsService {
  private final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private TmpNsfcKwsTaskRecordDao tmpNsfcKwsTaskRecordDao;
  @Autowired
  private TmpNsfcKeywordsDao tmpNsfcKeywordsDao;

  @Autowired
  private PdwhPubKeywordsSplitDao pdwhPubKeywordsDao;
  @Autowired
  private TmpPdwhNsfcKwsMatchedDao tmpPdwhNsfcKwsMatchedDao;

  @Override
  public List<Long> batchGetHasKwsPdwhPubIds(Integer size) {
    return tmpNsfcKwsTaskRecordDao.getUnprocessedPubKwsIds(size);
  }

  @Override
  public void generatePubKeywordHash() throws Exception {
    while (true) {
      List<TmpNsfcKeywords> noneKwsHashData = tmpNsfcKeywordsDao.getNoneKwsHashData();
      if (CollectionUtils.isEmpty(noneKwsHashData)) {
        return;
      }
      for (TmpNsfcKeywords tmpNsfcKeywords : noneKwsHashData) {
        Long keywordHash = PubHashUtils.getKeywordHash(tmpNsfcKeywords.getKeywords().trim());
        tmpNsfcKeywordsDao.generatePubKeywordHash(tmpNsfcKeywords.getId(), keywordHash);
      }
    }

  }

  @Override
  public void startMacthKeywords(Long Id) throws Exception {

    PdwhPubKeywordsSplit keywordSplt = pdwhPubKeywordsDao.get(Id);// 获取基准库成果关键词

    // 基准成果关键词
    List<TmpNsfcKeywords> nsfcKwsByHash = tmpNsfcKeywordsDao.getNsfcKwsByHash(keywordSplt.getKeywordHash());// 到nsfc关键词查询

    TmpNsfcKwsTaskRecord addkws = tmpNsfcKwsTaskRecordDao.get(Id);//

    // 没有匹配上
    if (CollectionUtils.isEmpty(nsfcKwsByHash)) {
      addkws.setStatus(2);// 该成果关键词不在nsfc关键词中
      // 保存到匹配记录表
      tmpPdwhNsfcKwsMatchedDao.save(new TmpPdwhNsfcKwsMatched(Id, keywordSplt.getKeyword(),
          keywordSplt.getKeywordHash(), null, "", "", "", "", "", 2, keywordSplt.getLanguage()));
      return;
    }

    addkws.setStatus(1);// 该成果关键词在nsfc关键词中
    // 保存到匹配记录表
    for (TmpNsfcKeywords tmpNsfcKeywords : nsfcKwsByHash) {
      tmpPdwhNsfcKwsMatchedDao.save(new TmpPdwhNsfcKwsMatched(Id, keywordSplt.getKeyword(),
          keywordSplt.getKeywordHash(), tmpNsfcKeywords.getResearchCode(), tmpNsfcKeywords.getResearchDirectionName(),
          tmpNsfcKeywords.getApplicationCode(), tmpNsfcKeywords.getMgmtDisciplineCode(),
          tmpNsfcKeywords.getMgmtDisciplineName(), tmpNsfcKeywords.getApplicationCodeName(), 1,
          keywordSplt.getLanguage()));
    }
  }

  @Override
  public void updateMatchStatus(Long id, Integer status) {
    TmpNsfcKwsTaskRecord addkws = tmpNsfcKwsTaskRecordDao.get(id);//
    addkws.setStatus(status);
  }
}
