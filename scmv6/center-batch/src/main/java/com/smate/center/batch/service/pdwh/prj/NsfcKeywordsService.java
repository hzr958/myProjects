package com.smate.center.batch.service.pdwh.prj;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.smate.center.batch.model.pdwh.prj.NsfcCategoryForKwdicUpdate;

public interface NsfcKeywordsService {

    public List<NsfcCategoryForKwdicUpdate> getNsfcDisciplineList(Integer status, Integer length, Integer size);

    public void updateNsfcCategoryForKwdicUpdate(NsfcCategoryForKwdicUpdate nsfcCategoryForKwdicUpdate);

    public Integer getNsfcDisciplineKwCounts(String discipline);

    public List<String> getNsfcDisciplineKw(String discipline);

    public void saveNsfcKws(Set<String> nsfcKws, Integer from, String applicationCode);

    public List<String> getNsfcKwSupplement(String discipline, Integer size);

    public void saveNsfcCategoryForKwdicUpdate(NsfcCategoryForKwdicUpdate nsfcCategoryForKwdicUpdate, Integer status);

    public Set<String> nsfcKwSortingByTfCotf(List<String> list, String discipline, Integer limit,
            Set<String> discKwNonDup);

    public List<BigDecimal> getToHandleKwList(Integer size);

    public void handleSubsets(Long prjId);

    public void updateStatus(Long prjId, Integer status);

    public List<String> getToHandleDiscode(Integer size);

    public Integer sortingKwInDiscode(String category, Integer[] lastThreeYears);

    public void updateDiscodeStatus(Integer status, String discode);

    public List<BigDecimal> getPdwhPubToHandleKwList(Integer size);

    public void updatePdwhPubStatus(Long pubId, Integer status);

    public Integer extractPdwhPubKws(Long pubId);

    public Integer extractPsnKwsFromPrjAndPub(Long PsnId);

    List<BigDecimal> getPsnToHandleKwList(Integer size);

    void updatePsnStatus(Long pubId, Integer status);

    Integer extractPrjKwsFromTitleAndAbsByTf(Long pubId);

    List<BigDecimal> getNsfcPrjToHandleKwList(Integer size);

    void updateNsfcPrjStatus(Long pubId, Integer status);

    Integer extractPrjKwsFromTitleAndAbsByTfEn(Long pubId);

    List<BigDecimal> getPsnToHandleKwList(Integer size, Integer status);

    Integer extractPsnKwsFromPrjAndPubExcludeTf(Long PsnId);

    Integer getKwSubsetsCotf(String category, Integer lang);

    List<String> getNsfcCategoryToHandleKwList(Integer size);

    void updateNsfcCategoryStatus(String category, Integer status);

    List<String> getNsfcCategoryToHandleKwList(Integer status, Integer size);

    Integer extractPrjKwsFromTitleAndAbsByTfForGaoxiao(Long pubId);

    void getScmPubKwsByNsfcBaseKws(String category, Integer language);

    List<String> getPubKws(List<String> kws, String category, Integer language);

    List<String> getNsfcBaseKwByCategory(String category, Integer language);

    Integer getPubKwTf(String category, String kw);

    Integer getPubKwCotf(String category, List<String> categoryNsfcKws, String kw, Integer language);

    void saveScmKwsInfo(String category, String kw, Integer tf, Integer cotf);

    void deleteScmKwsByCategory(String category, Integer language);

    public List<Map<String, Object>> getEncodedPubIdList(Integer size, Long lastId);

    Long getPdwhPubId(String title);

    List<String> getPdwhPubKwsFromDb(Long pubId);

    void saveKws(List<String> kws, Long pubId, String category);

    void updateNsfcPrjPubStatus(Long pubId, Integer status);

    List<String> getExtractKwsFromStr(Long pubId);

    List<BigDecimal> getSubsetsToHandleKwList(Integer size, Integer type);

    void updateScmPubSubsetsStatus(Long pubId, Integer type, Integer status, Long subSetSize);

    Integer extractJiangxiPrjKws(Long pubId);

    List<BigDecimal> getJiangxiPrjKwsToHandleKwList(Integer size);

    List<String> getGrpToHandleKwList();

    List<Map<String, Object>> getNsfcPrjInfo(String nsfcDisc, Integer type);

    List<Map<String, Object>> getNsfcPrjRelatedPub(String nsfcDisc);

    void saveNsfcKws(Map<String, Map<Integer, Double>> rsMap, String applicationCode);

    Set<String> getKwFromBracket(String kw);

    void saveNsfcKwsCotf(String kwCotfStr) throws Exception;

    Map<String, List<String>> getExtractZhKwsFromPrjStr(Long prjId);

    String getPubNsfcCategory(Long pubId);

    Long getTaskId();

    void insertIntoNsfcTaskStatus(Long taskId, String dataSection, Date startTime);

    void updateNsfcTaskStatus(Long taskId, Date endTime);

    Integer getTaskStatusByDataSection(String name);

    Integer getTranslateNsfcPrjKwsBySeq(Long pId);
}
