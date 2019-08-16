package com.smate.center.open.service.keyword;

import java.util.HashMap;
import java.util.List;

import com.smate.center.open.model.nsfc.NsfcKwScoreForSorting;

public interface NsfcKeywordsService {

    /**
     * 获取近五年包含此关键词项目数作为tf discode为空时，不作学科限制
     * 
     */
    public Long getNsfcKwTf(String kw, String discode);

    /**
     * 获取最相关的关键词list，最多50个 discode为空时，不作学科限制
     * 
     */
    public List<String> getRelatedKw(String kw, String discode, String queryType) throws Exception;

    /**
     * 获取最相关的关键词list，最多50个,采用关键词hash查询，限定本词，范围更小更准确 discode为空时，不作学科限制
     * 
     */
    List<String> getRelatedKwByHash(String kw, String discode, String queryType) throws Exception;

    public Integer extractPrjKws(String title, String abstractStr, String kw, String category,
            List<HashMap<String, Object>> dataMap) throws Exception;

    public List<NsfcKwScoreForSorting> extractKwsFromPubOrPrjForNsfc(String title, String pubKw, String pubAbstract,
            String nsfcCategory);

    Integer extractKwsForIsis(String title, String abstractStr, String kws, String category,
            List<HashMap<String, Object>> dataMap) throws Exception;
}
