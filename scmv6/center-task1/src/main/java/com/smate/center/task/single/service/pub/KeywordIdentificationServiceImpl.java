package com.smate.center.task.single.service.pub;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.task.dao.sns.psn.PsnDisciplineKeyDao;
import com.smate.center.task.dao.sns.quartz.KeywordsEnTranZhDao;
import com.smate.center.task.dao.sns.quartz.KeywordsZhTranEnDao;
import com.smate.center.task.dao.sns.quartz.PsnKwRmcDao;
import com.smate.center.task.dao.sns.quartz.RermcPsnKwHotDao;
import com.smate.center.task.model.sns.quartz.KeywordsEnTranZh;
import com.smate.center.task.model.sns.quartz.KeywordsZhTranEn;
import com.smate.center.task.model.sns.quartz.PsnKwRmc;
import com.smate.core.base.psn.model.profile.PsnDisciplineKey;
import com.smate.core.base.utils.string.ServiceUtil;

/**
 * 研究领域关键词认同Service
 * 
 * @author zyx
 * 
 */
@Service("keywordIdentificationService")
@Transactional(rollbackFor = Exception.class)
public class KeywordIdentificationServiceImpl implements KeywordIdentificationService {
  private static final int keyLength = 150;
  @Autowired
  private PsnKwRmcDao psnKwRmcDao;
  @Autowired
  private PsnDisciplineKeyDao disciplineKeyDao;
  @Autowired
  private KeywordsEnTranZhDao keywordsEnTranZhDao;
  @Autowired
  private KeywordsZhTranEnDao keywordsZhTranEnDao;
  @Autowired
  private RermcPsnKwHotDao rermcPsnKwHotDao;

  @Override
  public List<String> recommendKw(Long psnId, Locale locale) throws Exception {
    List<PsnKwRmc> list = psnKwRmcDao.findRecommendKw(psnId);

    List<String> strList = new ArrayList<String>();
    List<PsnDisciplineKey> keyList = disciplineKeyDao.findByPsnId(psnId);

    for (PsnKwRmc p : list) {
      String kw = this.translateKw(p.getKeyword(), locale);
      if (CollectionUtils.isNotEmpty(keyList)) {
        boolean isExists = false;
        for (PsnDisciplineKey k : keyList) {
          if (kw.toLowerCase().equals(k.getKeyWords().toLowerCase())) {
            isExists = true;
            break;
          }
        }
        if (!isExists) {
          strList.add(kw);
        }
      } else {
        strList.add(kw);
      }
    }
    // 补充满6个
    if (strList.size() < 50) {
      List<String> reHotKwList = rermcPsnKwHotDao.getRermcHotByPsnId(psnId, 50 - strList.size());
      if (CollectionUtils.isNotEmpty(reHotKwList)) {
        for (String hotkw : reHotKwList) {
          String kw = this.translateKw(hotkw, locale);

          if (CollectionUtils.isNotEmpty(keyList)) {
            boolean isExists = false;
            for (PsnDisciplineKey k : keyList) {
              if (kw.toLowerCase().equals(k.getKeyWords().toLowerCase())) {
                isExists = true;
                break;
              }
            }
            if (!isExists) {
              strList.add(kw);
            }
          } else {
            strList.add(kw);
          }
        }
      }
    }
    return strList;
  }

  private String translateKw(String kw, Locale locale) {
    if (ServiceUtil.isChineseStr(kw)) {
      if (locale.equals(Locale.US)) {
        KeywordsZhTranEn en = keywordsZhTranEnDao.findZhTranEnKw(kw);
        if (en != null) {
          kw = en.getEnKw();
        }
      }
    } else {
      if (!locale.equals(Locale.US)) {
        KeywordsEnTranZh zh = keywordsEnTranZhDao.findEnTranZhKw(kw);
        if (zh != null) {
          kw = zh.getZhKw();
        }
      }
    }

    return kw;
  }

}
