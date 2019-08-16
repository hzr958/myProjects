package com.smate.web.psn.service.profile;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.HtmlUtils;

import com.smate.core.base.consts.dao.ConstKeyDiscDao;
import com.smate.core.base.consts.model.ConstKeyDisc;
import com.smate.core.base.psn.model.profile.KeywordIdentification;
import com.smate.core.base.psn.model.profile.PsnDisciplineKey;
import com.smate.core.base.utils.dao.security.PersonDao;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.service.psn.PsnSolrInfoModifyService;
import com.smate.core.base.utils.string.ServiceUtil;
import com.smate.web.psn.dao.keywork.CategoryMapScmNsfcDao;
import com.smate.web.psn.dao.keywork.IdentificationDao;
import com.smate.web.psn.dao.keywork.KeywordsEnTranZhDao;
import com.smate.web.psn.dao.keywork.KeywordsZhTranEnDao;
import com.smate.web.psn.dao.keywork.PsnScienceAreaDao;
import com.smate.web.psn.dao.keywork.RermcPsnKwHotDao;
import com.smate.web.psn.dao.keywork.VKeywordsDicDao;
import com.smate.web.psn.dao.profile.KeywordIdentificationDao;
import com.smate.web.psn.dao.profile.PsnDisciplineKeyDao;
import com.smate.web.psn.dao.profile.PsnUpdateDiscLogDao;
import com.smate.web.psn.dao.profile.RecommandKwDropHistoryDao;
import com.smate.web.psn.dao.psnrefreshinfo.PsnKwRmcDao;
import com.smate.web.psn.dao.psnrefreshinfo.PsnRefreshUserInfoDao;
import com.smate.web.psn.dao.rcmdsync.RcmdSyncPsnInfoDao;
import com.smate.web.psn.exception.PsnException;
import com.smate.web.psn.exception.ServiceException;
import com.smate.web.psn.model.homepage.PersonProfileForm;
import com.smate.web.psn.model.keyword.CategoryMapScmNsfc;
import com.smate.web.psn.model.keyword.KeywordsEnTranZh;
import com.smate.web.psn.model.keyword.KeywordsZhTranEn;
import com.smate.web.psn.model.keyword.PsnScienceArea;
import com.smate.web.psn.model.keyword.VKeywordsDic;
import com.smate.web.psn.model.profile.PsnUpdateDiscLog;
import com.smate.web.psn.model.profile.RecommandKwDropHistory;
import com.smate.web.psn.model.psninfo.PsnRefreshUserInfo;
import com.smate.web.psn.model.psnrefreshinfo.PsnKwRmc;
import com.smate.web.psn.model.rcmdsync.RcmdSyncPsnInfo;

/**
 * 关键词服务类
 *
 * @author wsn
 * @createTime 2017年3月13日 下午4:10:46
 *
 */
@Service("psnDisciplineKeyService")
@Transactional(rollbackFor = Exception.class)
public class PsnDisciplineKeyServiceImpl implements PsnDisciplineKeyService {

  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private PsnDisciplineKeyDao psnDisciplineKeyDao;
  @Autowired
  private ConstKeyDiscDao constKeyDiscDao;
  @Autowired
  private PsnKwRmcDao psnKwRmcDao;
  @Autowired
  private KeywordsZhTranEnDao keywordsZhTranEnDao;
  @Autowired
  private KeywordsEnTranZhDao keywordsEnTranZhDao;
  @Autowired
  private RermcPsnKwHotDao rermcPsnKwHotDao;
  @Autowired
  private RcmdSyncPsnInfoDao rcmdSyncPsnInfoDao;
  @Autowired
  private PsnRefreshUserInfoDao psnRefreshUserInfoDao;
  @Autowired
  private RecommandKwDropHistoryDao recommandKwDropHistoryDao;
  @Autowired
  private PsnUpdateDiscLogDao psnUpdateDiscLogDao;
  @Autowired
  private PersonManager personManager;
  @Autowired
  private IdentificationDao identificationDao;
  @Autowired
  private KeywordIdentificationDao keywordIdentificationDao;
  @Autowired
  private PsnSolrInfoModifyService psnSolrInfoModifyService;
  @Autowired
  private PsnScienceAreaDao psnScienceAreaDao;
  @Autowired
  private CategoryMapScmNsfcDao categoryMapScmNsfcDao;
  @Autowired
  private VKeywordsDicDao vKeywordsDicDao;
  @Autowired
  private PersonalManager personalManager;
  @Autowired
  private PersonDao personDao;

  public static void main(String[] args) {
    List<PsnDisciplineKey> result = new ArrayList<PsnDisciplineKey>();
    List<PsnDisciplineKey> keyList = new ArrayList<PsnDisciplineKey>();
    result.addAll(keyList);
    System.out.println(result);
  }

  @Override
  public List<PsnDisciplineKey> findPsnKeyWords(Long psnId) {
    // TODO 最多取10个关键词，认同数最多的前10个
    List<PsnDisciplineKey> result = new ArrayList<PsnDisciplineKey>();
    List<PsnDisciplineKey> keyList = psnDisciplineKeyDao.findPsnDisciplineKeyByPsnId(psnId, 1);
    Long currentPsnId = SecurityUtils.getCurrentUserId();
    // 是否赞过该关键词
    if (currentPsnId != null) {
      for (PsnDisciplineKey key : keyList) {
        KeywordIdentification k = keywordIdentificationDao.findKeywordIdentification(psnId, key.getId(), currentPsnId);
        if (k != null) {
          key.setHasIdentified(true);
        }
      }
    }
    // 按认同数排序
    StringBuilder queryIds = new StringBuilder();
    List<Long> kwIdList = new ArrayList<Long>();
    for (PsnDisciplineKey key : keyList) {
      kwIdList.add(key.getId());
      queryIds.append("," + key.getId());
    }
    List<Object[]> mapList = new ArrayList<Object[]>();
    if (kwIdList.size() > 0) {
      mapList = keywordIdentificationDao.countIdentification(psnId, kwIdList);// 查找关键词统计数
    }
    if (mapList.size() > 0) {
      for (Object[] map : mapList) {
        Long kwId = (Long) map[0];
        Long count = (Long) map[1];
        for (PsnDisciplineKey key : keyList) {
          if (result.size() < 10 && key.getId().equals(kwId)) {// 取最多10个
            key.setIdentificationSum(count); // 添加统计数
            result.add(key);
          }
        }
      }
    }
    for (PsnDisciplineKey key : keyList) {// 上面添加的并不是keyList的全部，认同数为0的没查出来就没添加到result。
      if (result.size() < 10) {// 取最多10个
        boolean ishave = false;
        for (PsnDisciplineKey rk : result) {
          if (rk.getId().equals(key.getId())) {// 判断是否已经添加了
            ishave = true;
          }
        }
        if (!ishave) {// 没添加就加进来
          result.add(key);
        }
      } else {
        break;
      }
    }

    /*
     * List<Long> idenKeyId =
     * identificationDao.sortKwIdentification(queryIds.toString().replaceFirst(",", ""), psnId); //
     * 多于10个时只要前10个 if (idenKeyId != null && idenKeyId.size() >= 10) { idenKeyId = idenKeyId.subList(0,
     * 9); } else { if (idenKeyId == null) { idenKeyId = new ArrayList<Long>(); }
     * kwIdList.removeAll(idenKeyId); if (kwIdList != null && kwIdList.size() > 0) { for (int i = 0;
     * idenKeyId.size() < 10 && i < kwIdList.size(); i++) { idenKeyId.add(kwIdList.get(i)); } } } for
     * (Long id : idenKeyId) { for (PsnDisciplineKey key : keyList) { if (Long.compare(key.getId(), id)
     * == 0) { result.add(key); } } }
     */

    return result;
  }

  @Override
  public List<String> findPsnRecommendKeyWords(Long psnId, Locale locale, List<PsnDisciplineKey> psnDisKeyList) {
    List<PsnKwRmc> list = psnKwRmcDao.findPsnRecommendKeywords(psnId);
    // List<PsnDisciplineKey> psnDisKeyList =
    // psnDisciplineKeyDao.findPsnDisciplineKeyByPsnId(psnId, 1);
    List<String> result = new ArrayList<String>();
    if (!CollectionUtils.isEmpty(list)) {
      for (PsnKwRmc p : list) {
        String kw = this.translateKw(p.getKeyword(), locale);
        result = getPsnKwRmcFromLists(kw, psnDisKeyList, result);
      }
    }
    if (result.size() < 50) {
      List<String> reHotKwList = rermcPsnKwHotDao.getRermcHotByPsnId(psnId, 50 - result.size());
      if (!CollectionUtils.isEmpty(reHotKwList)) {
        for (String keyword : reHotKwList) {
          String kw = this.translateKw(keyword, locale);
          result = getPsnKwRmcFromLists(kw, psnDisKeyList, result);
        }
      }
    }
    if (CollectionUtils.isEmpty(result)) {
      // 如果推荐关键词为空，按照科技领域相关关键词进行推荐
      List<PsnScienceArea> scienceAreaList = psnScienceAreaDao.queryScienceArea(psnId, 1);
      if (CollectionUtils.isNotEmpty(scienceAreaList)) {
        List<Long> ids = new ArrayList<>();
        for (PsnScienceArea scienceArea : scienceAreaList) {
          if (scienceArea.getScienceAreaId() != null && scienceArea.getScienceAreaId() != 0) {
            ids.add(scienceArea.getScienceAreaId().longValue());
          }
        }
        if (CollectionUtils.isEmpty(ids)) {
          return result;
        }
        // 查找对应的nsfc 分类id
        List<CategoryMapScmNsfc> nsfcCategory = categoryMapScmNsfcDao.findNsfcCategoryIds(ids);
        if (CollectionUtils.isNotEmpty(nsfcCategory)) {
          List<String> nsfcIds = new ArrayList<>();
          for (CategoryMapScmNsfc nsfc : nsfcCategory) {
            if (nsfc.getScmCategoryId() != null && nsfc.getScmCategoryId() != 0l) {
              nsfcIds.add(nsfc.getNsfcCategoryId());
            }
          }
          if (CollectionUtils.isEmpty(nsfcIds)) {
            return result;
          }
          List<VKeywordsDic> keywords =
              vKeywordsDicDao.findKeywordsByNsfcCategoryIds(nsfcIds, Locale.US.equals(locale) ? 1 : 2);
          Iterator<VKeywordsDic> i = keywords.iterator();
          while (i.hasNext()) {
            String key = i.next().getKeyword();
            if (!result.contains(key)) {
              // 去除个人已有的关键词
              if (!keywordsisexist(key, psnDisKeyList)) {
                result.add(key);
              }
            }
          }
        }
      }

    }
    return miunsDuplicate(result);
  }

  /**
   * 经过中英文切换后,自身进行去重
   */
  public List<String> miunsDuplicate(List<String> result) {
    if (CollectionUtils.isEmpty(result)) {
      return result;
    }
    List<String> returnResult = new ArrayList<String>();
    for (String str : result) {
      if (!returnResult.contains(str)) {
        returnResult.add(str);
      }
    }
    return returnResult;
  }

  /**
   * 判断关键词是否已经存在于个人关键词表
   * 
   * @param keywords
   * @param psnDisKeyList
   * @return
   */
  public boolean keywordsisexist(String keywords, List<PsnDisciplineKey> psnDisKeyList) {
    if (CollectionUtils.isEmpty(psnDisKeyList)) {
      return false;
    } else {
      for (PsnDisciplineKey key : psnDisKeyList) {
        if (keywords.toLowerCase().equals(key.getKeyWords().toLowerCase())) {
          return true;
        }
      }
    }
    return false;
  }

  /**
   * 将关键词进行中英文翻译成对应的语言环境
   * 
   * @param kw
   * @param locale
   * @return
   */
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

  /**
   * 去除已经是人员关键词的推荐关键词
   * 
   * @param keyword
   * @param keyList
   * @param result
   * @return
   */
  private List<String> getPsnKwRmcFromLists(String keyword, List<PsnDisciplineKey> keyList, List<String> result) {
    if (CollectionUtils.isNotEmpty(keyList)) {
      boolean isExists = false;
      for (PsnDisciplineKey k : keyList) {
        if (keyword.toLowerCase().equals(k.getKeyWords().toLowerCase())) {
          isExists = true;
          break;
        }
      }
      if (!isExists) {
        result.add(keyword);
      }
    } else {
      result.add(keyword);
    }
    return result;
  }

  @Override
  public String savePsnKeywordsByForm(PersonProfileForm form) {
    List<String> keywordsList =
        this.savePsnKeywords(form.getPsnId(), form.getKeywordStr(), form.getShowScienceAreAndKeywordsConf());
    form.setPsnKeywords(keywordsList);
    return "success";

  }

  @Override
  public List<String> savePsnKeywords(Long psnId, String keyStr, Integer permission) {
    List<String> keywordsList = new ArrayList<String>();
    if (StringUtils.isNotBlank(keyStr)) {
      List keyList = JacksonUtils.jsonToList(keyStr);
      if (keyList != null && keyList.size() > 0) {
        for (int i = 0; i < keyList.size(); i++) {
          Map keyMap = (Map) keyList.get(i);
          if (keyMap.containsKey("keys")) {
            String keyword = keyMap.get("keys").toString();
            // 删除了所有关键词
            if (StringUtils.isBlank(keyword)) {
              // 同步人员信息
              this.updateRcmdSyncPsnKeywordsInfo(psnId, 1);
              return keywordsList;
            } else {
              keywordsList = this.updatePsnKeywords(psnId, keyword, keywordsList, permission);
            }
          }
        }
        // 将不在指定的关键词列表中的所有关键词设置为无效状态
        this.psnDisciplineKeyDao.updateKwStatusNotInKwList(psnId, 0, keywordsList);
      } else {// 将人员所有关键词记录置为无效状态
        this.psnDisciplineKeyDao.updateKwStatusByPsnId(psnId, 0);
      }
    }
    this.updateRcmdSyncPsnKeywordsInfo(psnId, 1);
    // 更新人员solr信息
    // psnSolrInfoModifyService.updateSolrPsnInfo(psnId);
    personalManager.refreshPsnSolrInfoByTask(psnId);
    return keywordsList;
  }

  /**
   * 同步关键词
   * 
   * @param psnId
   * @param kwztFlag
   */
  private void updateRcmdSyncPsnKeywordsInfo(Long psnId, Integer kwztFlag) {
    RcmdSyncPsnInfo rsp = rcmdSyncPsnInfoDao.get(psnId);
    PsnRefreshUserInfo psnRefInfo = psnRefreshUserInfoDao.get(psnId);
    if (rsp == null) {
      rsp = new RcmdSyncPsnInfo(psnId);
    }
    if (psnRefInfo == null) {
      psnRefInfo = new PsnRefreshUserInfo(psnId);
    }
    rsp.setKwztFlag(kwztFlag);
    psnRefInfo.setKwZt(1);
    rcmdSyncPsnInfoDao.save(rsp);
    psnRefreshUserInfoDao.save(psnRefInfo);
  }

  /**
   * 保存关键词
   * 
   * @param psnId
   * @param keyword
   * @return
   */
  private List<String> updatePsnKeywords(Long psnId, String keyword, List<String> keywordsList, Integer permission) {
    if (StringUtils.isNotBlank(keyword)) {
      if (keywordsList == null) {
        keywordsList = new ArrayList<String>();
      }
      keyword = HtmlUtils.htmlUnescape(keyword);
      keywordsList.add(keyword);
      // 新增或将关键词置为有效状态
      Integer saveResult = psnDisciplineKeyDao.saveKeys(keyword, psnId, null);
      // 推荐关键词中若有该关键词，则放入关键词删除历史表中
      this.recordDropRmKeyword(psnId, keyword);
      // 保存日志信息
      if (saveResult == 1) {
        if (permission == null) {
          permission = 0;
        }
        // 关键词模块权限为公开，则保存新增日志，用于发送邮件
        if (permission != 2 && StringUtils.isNotBlank(keyword)) { // 新增日志表
          PsnUpdateDiscLog log = new PsnUpdateDiscLog();
          log.setPsnId(psnId);
          log.setCreateDate(new Date());
          log.setStatus(0);
          log.setKeyWords(keyword);
          psnUpdateDiscLogDao.savePsnUpdateDiscLog(log);
        }
      }
    }
    return keywordsList;
  }

  /**
   * 保存关键词，新增新添加的or修改已有的
   * 
   * @param psnId
   * @param keyword
   * @return
   */
  private List<String> updatePsnKeywordsNew(Long psnId, String keyword, List<String> keywordsList, Integer permission) {
    if (StringUtils.isNotBlank(keyword)) {
      if (keywordsList == null) {
        keywordsList = new ArrayList<String>();
      }
      keyword = HtmlUtils.htmlUnescape(keyword);
      keywordsList.add(keyword);
      // 新增关键词(新增新添加的or修改已有的)
      psnDisciplineKeyDao.saveKeys(keyword.trim(), psnId, null);
      // PsnDisciplineKey pdk = new PsnDisciplineKey(keyword, psnId, 1, new Date());
      // psnDisciplineKeyDao.save(pdk);
      // 推荐关键词中若有该关键词，则放入关键词删除历史表中
      this.recordDropRmKeyword(psnId, keyword);
      // 保存日志信息
      if (permission == null) {
        permission = 0;
      }
      // 关键词模块权限为公开，则保存新增日志，用于发送邮件
      if (permission != 2 && StringUtils.isNotBlank(keyword)) { // 新增日志表
        PsnUpdateDiscLog log = new PsnUpdateDiscLog();
        log.setPsnId(psnId);
        log.setCreateDate(new Date());
        log.setStatus(0);
        log.setKeyWords(keyword);
        psnUpdateDiscLogDao.savePsnUpdateDiscLog(log);
      }
    }
    return keywordsList;
  }

  @Override
  public void recordDropRmKeyword(Long psnId, String keyword) {
    if (StringUtils.isNotBlank(keyword)) {
      String kwTxt = keyword.trim().toLowerCase();
      boolean isExists = psnKwRmcDao.isExists(psnId, kwTxt);
      if (isExists) {
        RecommandKwDropHistory entity = recommandKwDropHistoryDao.findByIdAndKw(psnId, kwTxt);
        if (entity == null) {
          entity = new RecommandKwDropHistory();
          entity.setDropDate(new Date());
          entity.setKeyword(keyword);
          entity.setKwTxt(kwTxt);
          entity.setPsnId(psnId);
          recommandKwDropHistoryDao.save(entity);
        }
      }
    }
  }

  @Override
  public PsnDisciplineKey findSomeIdentifyKwPsnIds(Long psnId, PsnDisciplineKey key) throws ServiceException {
    try {
      List<Long> psnIds = identificationDao.findSomeKwIdentificIds(psnId, key.getId());
      if (CollectionUtils.isNotEmpty(psnIds)) {
        key.setIdentifyAvatars(personDao.findPsnAvatarUrls(psnIds));
      }
    } catch (Exception e) {
      logger.error("获取关键词赞人员头像出错， psnId = " + psnId + ", discId = " + (key != null ? key.getId() : 0), e);
      throw new ServiceException(e);
    }
    return key;
  }

  /**
   * 1.找到不需要更新的关键词
   * 
   * 2.更新需要置为无效状态的关键词--------旧的关键词中去掉1中不需要更新的关键词
   * 
   * 3.保存新加的关键词-------当前要保存的关键词去掉1中不需要更新的关键词
   * 
   * 不用原先的先全部置为无效再一个一个检查是要新增还是更新，减少数据库操作
   */
  @Override
  public List<String> mobileSavePsnKeywords(Long psnId, List<String> keywordsList, Integer permission)
      throws PsnException {
    List<String> resultList = new ArrayList<String>();
    List<PsnDisciplineKey> notNeedUpdate = new ArrayList<PsnDisciplineKey>();
    List<Long> ids = new ArrayList<Long>();
    boolean needUpdate = true;
    try {
      List<PsnDisciplineKey> keyList = psnDisciplineKeyDao.findPsnDisciplineKeyByPsnId(psnId, 1);
      // 保存新的关键词
      if (CollectionUtils.isNotEmpty(keywordsList) && keywordsList.size() > 0) {
        for (int i = 0; i < keywordsList.size(); i++) {
          String keyword = keywordsList.get(i);
          if (StringUtils.isBlank(keyword)) {
            continue;
          } else {
            // 查找不需要更新状态的关键词
            needUpdate = true;
            if (CollectionUtils.isNotEmpty(keyList)) {
              for (PsnDisciplineKey key : keyList) {
                if (keyword.trim().equalsIgnoreCase(key.getKeyWords())) {
                  needUpdate = false;
                  notNeedUpdate.add(key);
                }
              }
            }
            // 新增新的关键词
            if (needUpdate) {
              resultList = this.updatePsnKeywordsNew(psnId, keyword.trim(), resultList, permission);
            }
          }
        }
      }
      // 更新指定关键词状态为无效
      keyList.removeAll(notNeedUpdate);
      for (PsnDisciplineKey key : keyList) {
        ids.add(key.getId());
      }
      if (CollectionUtils.isNotEmpty(ids)) {
        psnDisciplineKeyDao.updateKwStatusById(ids, 0);
      }
      // 标识需要同步关键词
      this.updateRcmdSyncPsnKeywordsInfo(psnId, 1);
      // 更新人员solr信息
      personalManager.refreshPsnSolrInfoByTask(psnId);
    } catch (Exception e) {
      logger.error("移动端保存人员关键词出错， psnId = " + psnId + ", permission = " + permission, e);
      throw new PsnException(e);
    }
    return resultList;
  }

  @Override
  public List<PsnDisciplineKey> findPsnEffectiveKeywods(Long psnId) throws PsnException {
    return psnDisciplineKeyDao.findPsnDisciplineKeyByPsnId(psnId, 1);
  }

  @Override
  public List<ConstKeyDisc> getConstKeyDiscs(String searchKey, Integer size) throws PsnException {
    return constKeyDiscDao.findKeys(searchKey, size);
  }

}
