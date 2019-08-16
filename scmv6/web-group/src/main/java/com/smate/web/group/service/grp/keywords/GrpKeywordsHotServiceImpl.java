package com.smate.web.group.service.grp.keywords;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.keywords.dao.KeywordsHotDao;
import com.smate.core.base.keywords.model.KeywordsHot;
import com.smate.web.group.dao.grp.keywords.KeywordsHotRelatedDao;
import com.smate.web.group.model.group.pub.PubInfo;
import com.smate.web.group.model.grp.keywords.KeywordSplit;
import com.smate.web.group.model.grp.keywords.KeywordsHotRelated;

/**
 * 热词关键词服务类
 * 
 * @author wcw
 *
 */
@Service("grpKeywordsHotService")
@Transactional(rollbackFor = Exception.class)
public class GrpKeywordsHotServiceImpl implements GrpKeywordsHotService {

  @Autowired
  private KeywordsHotDao keywordsHotDao;
  @Autowired
  private KeywordsDicService keywordsDicService;
  @Autowired
  private KeywordsHotRelatedDao keywordsHotRelatedDao;

  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Override
  public Map<String, List<KeywordsHot>> queryPubRefHotKeyByLang(PubInfo pubinfo) throws Exception {

    return queryPubRefHotKeyByLang(pubinfo, null);
  }

  @Override
  public Map<String, List<KeywordsHot>> queryPubRefHotKeyByLang(PubInfo pubinfo, List<Long> excHotId) throws Exception {
    Map<String, List<KeywordsHot>> map = new HashMap<String, List<KeywordsHot>>();
    // 英文热词
    if (StringUtils.isNotBlank(pubinfo.getEnAbs()) || StringUtils.isNotBlank(pubinfo.getEnTitle())) {
      PubInfo pubinfoEn = new PubInfo();
      pubinfoEn.setEnAbs(pubinfo.getEnAbs());
      pubinfoEn.setEnTitle(pubinfo.getEnTitle());
      pubinfoEn.setEnKws(pubinfo.getEnKws());
      map.put("en", queryPubRefHotKey(pubinfoEn, excHotId));
    }
    // 中文热词
    if (StringUtils.isNotBlank(pubinfo.getZhAbs()) || StringUtils.isNotBlank(pubinfo.getZhTitle())) {
      PubInfo pubinfoZh = new PubInfo();
      pubinfoZh.setZhAbs(pubinfo.getZhAbs());
      pubinfoZh.setZhTitle(pubinfo.getZhTitle());
      pubinfoZh.setZhKws(pubinfo.getZhKws());
      map.put("zh", queryPubRefHotKey(pubinfoZh, excHotId));
    }

    return map;
  }

  @Override
  public List<KeywordsHot> queryPubRefHotKey(PubInfo pubinfo, List<Long> excHotId) throws Exception {
    try {
      // 拆分关键词.
      List<KeywordSplit> kwspList = keywordsDicService.findPubKeywords(pubinfo);
      // 查找拆分关键词的相关热词
      return this.queryKeywordsRefHotKey(kwspList, excHotId);
    } catch (Exception e) {
      logger.error("查询成果关键词相关热词列表.", e);
      throw new Exception("查询成果关键词相关热词列表.", e);
    }
  }

  @Override
  public List<KeywordsHot> queryKeywordsRefHotKey(List<KeywordSplit> kwspList, List<Long> excHotId) throws Exception {
    try {
      // 过滤拆分关键词中的非热词关键词.
      kwspList = filterKeywordSplitHotKey(kwspList);
      if (CollectionUtils.isEmpty(kwspList)) {
        return null;
      }
      // 查找热词相关热词（反查）
      List<Long> rhotkwIdList = new ArrayList<Long>();
      List<Object[]> relObjList = new ArrayList<Object[]>();
      for (KeywordSplit kwsp : kwspList) {
        for (Long hotKeyId : kwsp.getHotKeyIds()) {
          List<KeywordsHotRelated> relKwHList = this.keywordsHotRelatedDao.getKeywordsHotRelated(hotKeyId, excHotId);
          SECOND_LOOP: for (KeywordsHotRelated relKw : relKwHList) {
            // 查重，看是否存在了
            for (Object[] objs : relObjList) {
              Long cid = (Long) objs[0];
              if (cid.equals(relKw.getCid())) {
                objs[1] = relKw.getRelSim() * kwsp.getWeight() + (Double) objs[1];
                continue SECOND_LOOP;
              }
            }
            rhotkwIdList.add(relKw.getCid());
            // 不存在重复，构造一个，权重=相关度*权重
            Object[] relObj = new Object[] {relKw.getCid(), relKw.getRelSim() * kwsp.getWeight()};
            relObjList.add(relObj);
          }
        }
      }
      if (CollectionUtils.isEmpty(rhotkwIdList)) {
        return null;
      }
      // 通过相关热词ID找出热词，并将权重放入对象排序
      List<KeywordsHot> rhotkwList = this.keywordsHotDao.loadKeywordsHotByIds(rhotkwIdList);
      for (KeywordsHot rhotkw : rhotkwList) {
        for (Object[] objs : relObjList) {
          Long cid = (Long) objs[0];
          if (cid.equals(rhotkw.getId())) {
            rhotkw.setWeight((Double) objs[1]);
          }
        }
      }
      // 排序
      Collections.sort(rhotkwList);
      return rhotkwList;

    } catch (Exception e) {
      logger.error("查询拆分关键词的相关热词.", e);
      throw new Exception("查询拆分关键词的相关热词.", e);
    }
  }

  /**
   * 过滤拆分关键词中的非热词关键词.
   * 
   * @param kwspList
   * @return
   */
  private List<KeywordSplit> filterKeywordSplitHotKey(List<KeywordSplit> kwspList) throws Exception {
    try {
      // 获取字符串查找
      List<String> splitKwList = new ArrayList<String>();
      for (KeywordSplit kwsp : kwspList) {
        splitKwList.add(kwsp.getKwtxt());
      }
      List<KeywordsHot> kwhList = this.keywordsHotDao.getKeywordsHot(splitKwList);
      outer_loop: for (int i = 0; i < kwspList.size(); i++) {
        boolean flag = true;
        KeywordSplit kwsp = kwspList.get(i);
        for (KeywordsHot kwh : kwhList) {
          // 找到热词
          if (kwsp.getKwtxt().equals(kwh.getKwTxt()) || kwsp.getKwtxt().equals(kwh.getEkwTxt())) {
            kwsp.setHotKeyId(kwh.getId());
            // 英文情况下可能会有多个热词
            if (CollectionUtils.isEmpty(kwsp.getHotKeyIds())) {
              kwsp.setHotKeyIds(new ArrayList<Long>());
            }
            kwsp.getHotKeyIds().add(kwh.getId());
            flag = false;
          }
        }
        if (flag) {
          // 未找到热词，从列表中移除
          kwspList.remove(i);
          i--;
        }
      }

      return kwspList;
    } catch (Exception e) {
      logger.error("过滤拆分关键词中的非热词关键词.", e);
      throw new Exception("过滤拆分关键词中的非热词关键词.", e);
    }
  }
}
