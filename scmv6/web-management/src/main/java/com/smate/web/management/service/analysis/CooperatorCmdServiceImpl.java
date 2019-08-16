package com.smate.web.management.service.analysis;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.utils.cache.CacheService;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.model.Page;
import com.smate.core.base.utils.model.security.Person;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.web.management.model.analysis.ExpertDiscForm;
import com.smate.web.management.model.analysis.KeywordSplit;
import com.smate.web.management.model.analysis.PsnCmdForm;
import com.smate.web.management.model.analysis.PubInfo;
import com.smate.web.management.model.analysis.sns.KeywordsPsnQuery;

/**
 * 合作者推荐service实现.
 * 
 * @author pwl
 * 
 */
@Service("cooperatorCmdService")
@Transactional(rollbackFor = Exception.class)
public class CooperatorCmdServiceImpl extends PsnCmdServiceUtil implements CooperatorCmdService {

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private KeywordsDicService keywordsDicService;
  @Autowired
  private PersonManager personManager;
  @Autowired
  private KeywordsQueryPsnService keywordsQueryPsnService;
  @Autowired
  private CacheService cacheService;

  @Override
  public List<KeywordSplit> getKeywordsList(String title, String abs) throws Exception {
    try {
      PubInfo pubInfo = new PubInfo();
      String locale = LocaleContextHolder.getLocale().toString();
      if ("zh_CN".equals(locale)) {
        pubInfo.setZhTitle(title);
        pubInfo.setZhAbs(abs);
      } else {
        pubInfo.setEnTitle(title);
        pubInfo.setEnAbs(abs);
      }
      List<KeywordSplit> list = this.keywordsDicService.findPubKeywords(pubInfo);

      return new ArrayList<KeywordSplit>(list.subList(0, list.size() < 10 ? list.size() : 10));
    } catch (Exception e) {
      logger.error("根据标题和摘要拆分关键词出现异常：", e);
      throw e;
    }
  }

  // 生成图表的时候只截取5个词
  @Override
  public List<KeywordSplit> getKeywordsListForChart(String title, String abs) throws Exception {
    try {
      PubInfo pubInfo = new PubInfo();
      String locale = LocaleContextHolder.getLocale().toString();
      if ("zh_CN".equals(locale)) {
        pubInfo.setZhTitle(title);
        pubInfo.setZhAbs(abs);
      } else {
        pubInfo.setEnTitle(title);
        pubInfo.setEnAbs(abs);
      }
      List<KeywordSplit> list = this.keywordsDicService.findPubKeywords(pubInfo);

      return new ArrayList<KeywordSplit>(list.subList(0, list.size() < 10 ? list.size() : 10));
    } catch (Exception e) {
      logger.error("根据标题和摘要拆分关键词出现异常：", e);
      throw e;
    }
  }

  @Override
  @SuppressWarnings("unchecked")
  public List<ExpertDiscForm> getCooperatorDisc(PsnCmdForm form, String sessionId) throws Exception {
    try {
      String cacheKey = String.format(PsnCmdServiceUtil.EXPERT_PSN_CACHE_KEY_FORMAT, sessionId,
          PsnCmdServiceUtil.PAPER_COOPERATOR_CMD);
      // 获取人员ID.
      List<Long> psnIdList = new ArrayList<Long>();
      List<PsnCmdForm> psnCmdList =
          (ArrayList<PsnCmdForm>) cacheService.get(PsnCmdServiceUtil.EXPERT_PSN_CACHE, cacheKey);
      if (form.getLoadFlag() == 0 || psnCmdList == null) {// 重新加载
        Map<Long, Long> psnKeyMap = keywordsQueryPsnService
            .queryKeyPsnAndKeyCount(this.buildKeyList(form.getKeywordJson()), PsnCmdServiceUtil.CMD_PSN_COUNT);
        for (Map.Entry<Long, Long> entry : psnKeyMap.entrySet()) {
          psnIdList.add(entry.getKey());
        }
        if (CollectionUtils.isNotEmpty(psnIdList)) {
          psnIdList = this.removeCurrentUser(psnIdList); // 排除当前用户
          Person curPsn = personManager.getPerson(SecurityUtils.getCurrentUserId());
          psnIdList = personManager.getCooperatorPsnId(curPsn.getInsId(), psnIdList);
        }
      } else {
        for (PsnCmdForm psnCmd : psnCmdList) {
          psnIdList.add(psnCmd.getPsnId());
        }
      }

      return this.getPsnCmdDisc(psnIdList);
    } catch (Exception e) {
      logger.error("加载合作者研究领域出现异常：", e);
      throw new Exception(e);
    }
  }

  /**
   * 构造关键词查询列表.
   * 
   * @param keywordJson
   * @return
   */
  protected List<KeywordsPsnQuery> buildKeyList(String keywordJson) {
    List<KeywordsPsnQuery> list = new ArrayList<KeywordsPsnQuery>();
    if (StringUtils.isNotBlank(keywordJson)) {
      // JSONArray jsonArray = JSONArray.fromObject(keywordJson);
      List<Map<String, String>> jArray = JacksonUtils.jsonListUnSerializer(keywordJson);
      KeywordsPsnQuery keywordsPsnQuery = null;
      for (Map<String, String> map : jArray) {
        keywordsPsnQuery = new KeywordsPsnQuery(map.get("keyword"), 1D);
        list.add(keywordsPsnQuery);
      }
    }
    return list;
  }

  /**
   * 排除当前用户.
   * 
   * @param psnIdList
   * @return
   */
  protected List<Long> removeCurrentUser(List<Long> psnIdList) {
    Long currentUserId = SecurityUtils.getCurrentUserId();
    if (CollectionUtils.isNotEmpty(psnIdList)) {
      psnIdList.remove(currentUserId);
    }
    return psnIdList;
  }

  @Override
  public List<PsnCmdForm> getCooperatorInfo(Long currentPsnId, Long insId, List<Long> psnIdList,
      Map<Long, Long> commonKeyCountMap, List<Long> publishedJidList, List<Long> refJidList, List<Long> discIdList,
      List<String> psnTaughtList) throws Exception {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Page<PsnCmdForm> getCooperatorByPage(PsnCmdForm form, String sessionId, Page<PsnCmdForm> page)
      throws Exception {
    // TODO Auto-generated method stub
    return null;
  }
}
