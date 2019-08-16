package com.smate.center.data.service.pub;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.data.dao.pub.HadoopPubKeywordsCombineDao;
import com.smate.center.data.dao.pub.PdwhPubKeywordsCategoryDao;
import com.smate.center.data.model.pub.HadoopPubKeywordsCombine;
import com.smate.center.data.model.pub.PdwhPubKeywordsCategory;

/**
 * 计算pdwh成果关键词组合频次相关服务实现类
 * 
 * @author lhd
 *
 */
@Service("pdwhPubKeywordsService")
@Transactional(rollbackFor = Exception.class)
public class PdwhPubKeywordsServiceImpl implements PdwhPubKeywordsService {
  @Autowired
  private PdwhPubKeywordsCategoryDao pdwhPubKeywordsCategoryDao;
  @Autowired
  private HadoopPubKeywordsCombineDao hadoopPubKeywordsCombineDao;

  @Override
  public List<PdwhPubKeywordsCategory> getPdwhPubKeywords(Integer size, Long startId, Long endId) {
    return pdwhPubKeywordsCategoryDao.getPdwhPubKeywords(size, startId, endId);
  }

  @Override
  public void saveOpResult(Long id, int status) {
    pdwhPubKeywordsCategoryDao.saveOpResult(id, status);
  }

  @Override
  public Boolean checkCombine(String pubKey) throws Exception {
    return hadoopPubKeywordsCombineDao.check(pubKey);
  }

  @Override
  public void saveCombineTable(String pubKey) throws Exception {
    if (!hadoopPubKeywordsCombineDao.check(pubKey)) {
      HadoopPubKeywordsCombine hc = new HadoopPubKeywordsCombine();
      hc.setPubKey(pubKey);
      hc.setCreateDate(new Date());
      hadoopPubKeywordsCombineDao.save(hc);
    }

  }

}
