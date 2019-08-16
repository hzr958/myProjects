package com.smate.center.task.service.pdwh.quartz;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.task.dao.pdwh.quartz.PdwhPublicationAllDao;
import com.smate.center.task.dao.pdwh.quartz.PsnPubAllRecommendDao;
import com.smate.center.task.dao.pub.seo.PubSeoSecondLevelSerachDao;
import com.smate.center.task.exception.ServiceException;
import com.smate.center.task.model.pdwh.quartz.PdwhIndexPublication;
import com.smate.center.task.model.pdwh.quartz.PdwhPublicationAll;
import com.smate.center.task.model.pdwh.quartz.PsnPubAllRecommend;
import com.smate.center.task.model.pdwh.quartz.PubAllkeyword;
import com.smate.center.task.model.pdwh.quartz.RefKwForm;
import com.smate.center.task.single.dao.solr.PdwhIndexPublicationDao;
import com.smate.center.task.single.service.pub.DynRecommendPubAllProducer;
import com.smate.core.base.utils.string.MapBuilder;

/**
 * 基准库成果服务
 * 
 * @author warrior
 * 
 */
@Service("publicationAllService")
@Transactional(rollbackFor = Exception.class)
public class PublicationAllServiceImpl implements PublicationAllService {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private DynRecommendPubAllProducer dynRecommendPubAllProducer;
  @Autowired
  private PubAllkeywordService pubAllkeywordService;
  @Autowired
  private PdwhPublicationAllDao pdwhPublicationAllDao;
  @Autowired
  private PsnPubAllRecommendDao psnPubAllRecommendDao;
  @Autowired
  private PdwhIndexPublicationDao pdwhIndexPublicationDao;
  @Autowired
  private PubSeoSecondLevelSerachDao pubSeoSecondLevelSerachDao;

  @SuppressWarnings("rawtypes")
  @Override
  public Map getBriefDesc(Long pubId, Integer dbid) throws ServiceException {
    try {
      return this.pdwhPublicationAllDao.getBriefDesc(pubId, dbid);
    } catch (Exception e) {
      logger.error("获取成果来源(briefDesc)出现异常_pdwh", pubId, dbid, e);
      throw new ServiceException(e);
    }
  }

  @Override
  public void deletePsnPubAllRecommend(Long psnId) throws ServiceException {
    psnPubAllRecommendDao.delPsnPubAllRecommend(psnId);
    // 发送论文推荐动态_MJG_SCM-5988.
    List<Long> psnIdList = new ArrayList<Long>();
    psnIdList.add(psnId);
    this.sendPubAllRecommendDyn(psnIdList);
  }

  /**
   * 发送论文推荐同步更新动态_MJG_SCM-5988.
   * 
   * @param psnIdList
   */
  @SuppressWarnings("unchecked")
  public void sendPubAllRecommendDyn(List<Long> psnIdList) {
    if (CollectionUtils.isNotEmpty(psnIdList)) {
      Map<Long, PdwhPublicationAll> pubAllMap = MapBuilder.getInstance().getMap();
      List<Long> removePsnIds = new ArrayList<Long>();
      for (Long psnId : psnIdList) {
        PdwhPublicationAll pubAll = psnPubAllRecommendDao.findPsnRefRecommendSimple(psnId);
        if (pubAll != null) {
          pubAllMap.put(psnId, pubAll);
        } else {
          removePsnIds.add(psnId);
        }
      }
      dynRecommendPubAllProducer.sendPubAllMsg(pubAllMap);
      dynRecommendPubAllProducer.sendRemovePsnPubAllRcmdMsg(removePsnIds);
    }
  }

  @Override
  public List<PsnPubAllRecommend> getPsnPuballOldList(Long psnId, int language) throws ServiceException {
    return psnPubAllRecommendDao.findPsnPubAllRecommend(psnId, language);
  }

  @Override
  public void deletePsnPubAllRecommend(Long psnId, List<PsnPubAllRecommend> list) throws ServiceException {
    if (CollectionUtils.isEmpty(list))
      return;
    for (PsnPubAllRecommend psnPubAllRecommend : list) {
      psnPubAllRecommendDao.delPsnPubAllRecommend(psnId, psnPubAllRecommend.getPubAllId());
    }
    // 发送论文推荐动态_MJG_SCM-5988.
    List<Long> psnIdList = new ArrayList<Long>();
    psnIdList.add(psnId);
    this.sendPubAllRecommendDyn(psnIdList);
  }

  @Override
  public void savePsnPubAllRecommend(List<PsnPubAllRecommend> list) throws ServiceException {
    try {
      if (CollectionUtils.isEmpty(list))
        return;
      List<Long> psnIdList = new ArrayList<Long>();
      for (PsnPubAllRecommend psnPubAllRecommend : list) {
        PsnPubAllRecommend oldobj =
            psnPubAllRecommendDao.getPsnRefRecommend(psnPubAllRecommend.getPsnId(), psnPubAllRecommend.getPubAllId());
        if (oldobj == null) {
          psnPubAllRecommendDao.save(psnPubAllRecommend);
        } else {
          oldobj.setKeywords(psnPubAllRecommend.getKeywords());
          oldobj.setScore(psnPubAllRecommend.getScore());
          oldobj.setLanguage(psnPubAllRecommend.getLanguage());
          oldobj.setRecDate(new Date());
          psnPubAllRecommendDao.savePubAllRecommend(oldobj);
        }
        // 加载人员列表_MJG_SCM-5988.
        if (!psnIdList.contains(psnPubAllRecommend.getPsnId())) {
          psnIdList.add(psnPubAllRecommend.getPsnId());
        }
      }
      this.sendPubAllRecommendDyn(psnIdList);
    } catch (Exception e) {
      logger.error("", e);
    }

  }

  @SuppressWarnings("rawtypes")
  @Override
  public List<RefKwForm> findPubAllByKwHashByRecommend(List<Long> kwHashList) throws ServiceException {
    try {
      List<PubAllkeyword> kwMapList = pubAllkeywordService.findPubAllByKwHash(kwHashList);
      if (CollectionUtils.isEmpty(kwMapList))
        return null;
      Map<Long, String> groups = new HashMap<Long, String>();
      for (PubAllkeyword pubAllkeyword : kwMapList) {
        groups.put(pubAllkeyword.getPubAllId(), "");
      }
      List<RefKwForm> newKwFormList = new ArrayList<RefKwForm>();
      for (Long puballId : groups.keySet()) {
        RefKwForm form = new RefKwForm();
        form.setPuballId(puballId);
        PdwhPublicationAll puball = pdwhPublicationAllDao.getPuballById(puballId);
        if (puball != null) {
          form.setJnlId(puball.getJnlId());
          form.setPubId(puball.getPubId());
          form.setDbid(puball.getDbid());
          Map jnlMap = pdwhPublicationAllDao.getBaseJnlByIssn(puball.getJnlId());
          if (jnlMap != null) {
            form.setIssn(Objects.toString(jnlMap.get("PISSN"), ""));
          }
        }
        if (form.getPubId() == null || StringUtils.isBlank(form.getIssn()))
          continue;
        List<String> kws = new ArrayList<String>();
        for (PubAllkeyword pd : kwMapList) {
          if (puballId.equals(pd.getPubAllId())) {
            kws.add(pd.getKeyword());
          }
        }
        form.setMatchKws(kws);
        form.setKwtf(kws.size());
        List<String> refKws = pubAllkeywordService.getPubAllKwsById(puballId);
        form.setRefKws(refKws);
        newKwFormList.add(form);
      }
      return newKwFormList;
    } catch (Exception e) {
      logger.error("关键词匹配基准文献关键词出错", e);
    }
    return null;
  }

  @Override
  public Long getCount(String code) {
    return pdwhIndexPublicationDao.getCount(code);
  }



  @Override
  public List<PdwhIndexPublication> getPubByIndexCode(String code, int startIndex, int maxCount) {
    return pdwhIndexPublicationDao.getPubByIndexCode(code, startIndex, maxCount);
  }

  @Override
  public List<PdwhIndexPublication> getNeedCleanData(Long lastId, int size) {
    return pdwhIndexPublicationDao.getNeedCleanData(lastId, size);
  }

  @Override
  public void updatePdwhIndexPubTitle(Long pubId, String result) {
    pdwhIndexPublicationDao.updatePdwhIndexPubTitle(pubId, result);
  }

  @Override
  public void updatePdwhIndexPubZhTitleAndPubIndexSecondLevel(Long pubId, String result) {
    pdwhIndexPublicationDao.updatePdwhIndexPubTitle(pubId, result);
    if (StringUtils.isNotBlank(result)) {
      String firstChar = result.substring(0, 1);
      if (StringUtils.isNotBlank(firstChar)) {
        pubSeoSecondLevelSerachDao.updateFirstLetter(pubId, firstChar, 2);
        return;
      }
    }
    pubSeoSecondLevelSerachDao.updateFirstLetter(pubId, "0", 2);
  }

}
