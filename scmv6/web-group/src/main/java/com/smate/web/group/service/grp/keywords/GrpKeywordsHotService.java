package com.smate.web.group.service.grp.keywords;

import java.util.List;
import java.util.Map;

import com.smate.core.base.keywords.model.KeywordsHot;
import com.smate.web.group.model.group.pub.PubInfo;
import com.smate.web.group.model.grp.keywords.KeywordSplit;

/**
 * 热词关键词服务类
 * 
 * @author wcw
 *
 */
public interface GrpKeywordsHotService {
  /**
   * 查询拆分关键词的相关热词.
   * 
   * @param kwspList
   * @return
   * @throws Exception
   */
  public List<KeywordsHot> queryKeywordsRefHotKey(List<KeywordSplit> kwspList, List<Long> excHotId) throws Exception;

  /**
   * 查询成果关键词相关热词列表，返回值区分中英文.
   * 
   * @param pubinfo
   * @return key:zh 中文；key:en 英文
   * @throws Exception
   */
  public Map<String, List<KeywordsHot>> queryPubRefHotKeyByLang(PubInfo pubinfo) throws Exception;

  /**
   * 查询成果关键词相关热词列表，返回值区分中英文.
   * 
   * @param pubinfo
   * @param excHotId 排除的hotkey id
   * @return key:zh 中文；key:en 英文
   * @throws Exception
   */
  public Map<String, List<KeywordsHot>> queryPubRefHotKeyByLang(PubInfo pubinfo, List<Long> excHotId) throws Exception;

  /**
   * 查询成果关键词相关热词列表.
   * 
   * @param pubinfo
   * @param excHotId 排除的hotkey id
   * @return
   * @throws Exception
   */
  public List<KeywordsHot> queryPubRefHotKey(PubInfo pubinfo, List<Long> excHotId) throws Exception;

}
