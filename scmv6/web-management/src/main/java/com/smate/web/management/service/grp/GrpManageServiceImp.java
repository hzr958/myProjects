package com.smate.web.management.service.grp;

import com.smate.core.base.pub.consts.V8pubQueryPubConst;
import com.smate.core.base.pub.enums.PubSnsRecordFromEnum;
import com.smate.core.base.pub.util.PubDetailVoUtil;
import com.smate.core.base.pub.vo.PubDetailVO;
import com.smate.core.base.utils.cache.CacheService;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.web.management.dao.grp.GrpBaseInfoDAO;
import com.smate.web.management.dao.psn.OpenUserUnionDao;
import com.smate.web.management.model.grp.*;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.HtmlUtils;

import javax.annotation.Resource;
import java.io.File;
import java.io.Serializable;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 群组管理服务实现类
 *
 * @author aijiangbin
 * @create 2019-07-09 14:07
 **/

@Service ("grpManageService")
@Transactional (rollbackFor = Exception.class)
public class GrpManageServiceImp  implements  GrpManageService {
  public  static String  CACHE_GRP_INFOS = "cache_grp_infos";
  public  static String  PUB_TYPE = "1,2,3,4,5,7,8,10,11,12,13"; // 成果类型
  public  static Integer GROUP_PUB = 2;
  protected final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private GrpBaseInfoDAO grpBaseInfoDAO;
  @Resource( name="extractExcelFileService")
  private ExtractFileService  extractExcelFileService;
  @Autowired
  private CacheService cacheService;
  @Autowired
  private OpenUserUnionDao openUserUnionDao;
  @Autowired
  private RestTemplate restTemplate;
  @Value ("${initOpen.restful.url}")
  private String openResfulUrl;
  @Value("${domainscm}")
  private String domainscm;


  @Override
  public void findGrpList(GrpManageForm form) {
    grpBaseInfoDAO.findGrpList(form);
    if(CollectionUtils.isNotEmpty(form.getPage().getResult())){
      for(GrpBaseinfo info  :form.getPage().getResult()){
        GrpBaseShowInfo  showInfo  = new GrpBaseShowInfo();
        form.getShowInfos().add(showInfo);
        showInfo.setGrpName(info.getGrpName());
        showInfo.setGrpId(info.getGrpId());
        showInfo.setBrief(info.getGrpDescription());
        showInfo.setImage(info.getGrpAuatars());
      }
    }
  }

  @Override
  public Map<String, Object> extractFileData(File file , String sourceType , String sourceFileFileName) {
    Map<String, Object> map = new HashMap<>();
    map = extractExcelFileService.extractFile(file ,sourceFileFileName);
    return map;
  }

  @Override
  public void buildPendingGrpInfos(GrpManageForm form, List<GrpItemInfo> itemInfos) {
    for(GrpItemInfo itemInfo : itemInfos){
      GrpBaseShowInfo showInfo = new GrpBaseShowInfo();
      form.getShowInfos().add(showInfo);
      showInfo.setGrpName(itemInfo.getGroupName());
      showInfo.setBrief(itemInfo.getBreif());
    }
    // 换成对象
    cacheGrpItemInfos(itemInfos);
  }
  public void cacheGrpItemInfos (List<GrpItemInfo> itemInfos){
    GrpItemInfoCache  cache = new GrpItemInfoCache();
    cache.setList(itemInfos);
    cacheService.put(CACHE_GRP_INFOS , CacheService.EXP_HOUR_1,cache.getPsnId()+"", cache);
  }

  public void removeCacheGrpItemInfos (Long psnId ){
    cacheService.remove(CACHE_GRP_INFOS , psnId+"");
  }
  public List<GrpItemInfo>  getCacheGrpItemInfos (Long psnId){
    Serializable obj = cacheService.get(CACHE_GRP_INFOS, psnId + "");
    if(obj != null){
      GrpItemInfoCache  cache = (GrpItemInfoCache)obj;
      return  cache.getList();
    }
    return   null;
  }

  @Override
  public void savePendingGrpInfos(GrpManageForm form) {
    List<GrpItemInfo> cacheGrpItemInfos = getCacheGrpItemInfos(form.getPsnId());
    if(CollectionUtils.isEmpty(cacheGrpItemInfos)){
      return;
    }
    for(GrpItemInfo itemInfo : cacheGrpItemInfos){
      try{
        createGrp(form , itemInfo);
      }catch (Exception e){
        logger.error("调用open接口，创建群组异常"+itemInfo.toString());
      }
    }
    // 移除缓存
    removeCacheGrpItemInfos(form.getPsnId());
  }


  public void createGrp(GrpManageForm form ,GrpItemInfo itemInfo) throws Exception {

    Long openId = openUserUnionDao.getOpenIdByPsnId(form.getPsnId());
    if(NumberUtils.isNullOrZero(openId)){
      return ;
    }
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("openid", openId);
    map.put("token", "00000000lhd25dhl");

    Map<String, Object> dataMap = new HashMap<String, Object>();
    Map<String, Object> grpDataMap = new HashMap<String, Object>();
    grpDataMap.put("grpName", HtmlUtils.htmlUnescape(itemInfo.getGroupName()));
    grpDataMap.put("grpCategory", itemInfo.getGroupType());
    grpDataMap.put("grpDescription", HtmlUtils.htmlUnescape(itemInfo.getBreif()));
    grpDataMap.put("openType", itemInfo.getOpenType());
    if (StringUtils.isNotBlank(itemInfo.getKeyword())) {
      itemInfo.setKeyword(URLDecoder.decode(itemInfo.getKeyword(), "utf-8"));
    }
    grpDataMap.put("keyWords", itemInfo.getKeyword());
    grpDataMap.put("projectNo", "");
    grpDataMap.put("projectStatus", "");
    grpDataMap.put("firstCategoryId",itemInfo.getSmateFirstCategoryId());
    grpDataMap.put("secondCategoryId", itemInfo.getSmateSecondCategoryId());
    grpDataMap.put("nsfcCategoryId", itemInfo.getNsfcCategoryId());
    grpDataMap.put("ownerPsnId", itemInfo.getOwnerPsnId());
    dataMap.put("grpData", JacksonUtils.mapToJsonStr(grpDataMap));
    map.put("data", JacksonUtils.mapToJsonStr(dataMap));
    Object obj = restTemplate.postForObject(this.openResfulUrl, map, Object.class);
    Long grpId = getGrpId(JacksonUtils.jsonToMap(obj.toString()));
    if (grpId == null || grpId == 0L) {
       throw   new Exception("接口创建群组异常");
    }
    form.setCount(form.getCount()+1);
    form.setGrpId(grpId);
  }


  private Long getGrpId(Map<String, Object> resultMap) {
    Long grpId = 0L;
    if (resultMap != null && "success".equals(resultMap.get("status"))) {
      List<Map<String, Object>> result = (List<Map<String, Object>>) resultMap.get("result");
      if (result != null && result.size() > 0) {
        grpId = org.apache.commons.lang3.math.NumberUtils.toLong(result.get(0).get("grpId").toString());
      }
    }
    return grpId;

  }

  @Override
  public void findPdwhPub(GrpManageForm form) {
    Map<String, Object> map = new HashMap<>();
    map.put("token", "00000000"+"sh66info");
    map.put("openid", 99999999L);
    Map<String, Object> dataSend = new HashMap<String, Object>();
    dataSend.put("dataType", "paper");
    dataSend.put("searchKey", form.getSearchKey());
    dataSend.put("pageNo", 1);
    dataSend.put("pageSize", 50);
    dataSend.put("orderBy", "DEFAULT");
    dataSend.put("pubType", PUB_TYPE);
    //dataSend.put("pubIds", this.searchGetImpPubIds(form));
    map.put("data",JacksonUtils.mapToJsonStr(dataSend));
    Object obj = restTemplate.postForObject(this.openResfulUrl, map, Object.class);

    Map resultMap = JacksonUtils.jsonToMap(obj.toString());
    Map<String, Object> mapData = new HashMap<String, Object>();
    List resultList = resultMap != null ? (List) resultMap.get("result") : null;
    mapData = resultList != null ? (HashMap) resultList.get(0) : null;

    List<SearchPubShowInfo> pdList = new ArrayList<SearchPubShowInfo>();
    String items = mapData != null ? (String) mapData.get("items") : "";
    if (StringUtils.isNotBlank(items)) {
      List<Map<String, Object>> itemsList = JacksonUtils.jsonToList(items);
      if (itemsList != null && itemsList.size() > 0) {
        for (int i = 0; i < itemsList.size(); i++) {
          Map item = itemsList.get(i);
            SearchPubShowInfo pd = new SearchPubShowInfo();
            buildShowPubInfo(item, pd,form);
            pdList.add(pd);

        }
        form.setPubInfo(pdList);
        form.setCount(pdList.size());
      }
    }
  }


  private void buildShowPubInfo(Map item, SearchPubShowInfo pd ,GrpManageForm form) {
    // 把属性放到DemoCachePubInfo对象，为了调用统一构造来源和作者的接口
    String zhTitle = "";//
    String enTitle = "";
    if (item.get("pubId") != null && org.apache.commons.lang3.math.NumberUtils.isNumber(item.get("pubId").toString())) {
      pd.setPubId(org.apache.commons.lang3.math.NumberUtils.toLong(item.get("pubId").toString()));
    }
    if (item.get("pubDbId") != null && org.apache.commons.lang3.math.NumberUtils.isNumber(item.get("pubDbId").toString())) {
      pd.setDbid((org.apache.commons.lang3.math.NumberUtils.toInt(item.get("pubDbId").toString())));
    }
    if (StringUtils.isBlank(zhTitle) && item.get("pubTitle") != null)
      zhTitle = item.get("pubTitle").toString();
    if (StringUtils.isBlank(zhTitle) && item.get("pubTitle") != null)
      enTitle = item.get("pubTitle").toString();
    Object pubTypeId = item.get("pubTypeId");
    Object pubYear = item.get("pubYear");
    pd.setDoi(ObjectUtils.toString(item.get("doi")));
    pd.setDoiUrl(ObjectUtils.toString(item.get("doiUrl")));
    pd.setPubUrl(ObjectUtils.toString(item.get("pubShortUrl")));
    pd.setFundingInfo(ObjectUtils.toString(item.get("fundInfo")));
    pd.setAuthorNames(ObjectUtils.toString(item.get("authors")));
    pd.setBriefDesc(ObjectUtils.toString(item.get("pubBrief")));
    if (StringUtils.isBlank(pd.getFundingInfo())) {
      pd.setFundingInfo(ObjectUtils.toString(item.get("product_mark")));
    }
    if (zhTitle != null && StringUtils.isNotBlank(zhTitle.toString())) {
      pd.setTitle(zhTitle.toString());
    } else {
      if (enTitle != null)
        pd.setTitle(enTitle.toString());
    }
    pd.setTitle(pd.getTitle());
    if (pubYear != null && StringUtils.isNotBlank(pubYear.toString())) {
      pd.setPublishYear(org.apache.commons.lang3.math.NumberUtils.toLong(pubYear.toString()));
    }
    if (pubTypeId != null && StringUtils.isNotBlank(pubTypeId.toString())) {
      pd.setPubType(org.apache.commons.lang3.math.NumberUtils.toLong(pubTypeId.toString()));
    }
  }



  @Override
  public void importPdwhPubToGrp(GrpManageForm form) {
    Integer successNum = 0 ;
    String[] des3GrpSplit = form.getDesGrpIds().split(";");
    String[]  pubIdSplit = form.getPdwhPubIds().split(";");
    Integer index = 1 ;
    for(String grpId : des3GrpSplit){
      for(String  pubId : pubIdSplit){
        form.setDes3gGrpId(grpId);
        PubDetailVO  pubDetailVO = findPdwhPubDetail(pubId);
        boolean flag = savePubToGrp(pubDetailVO, form);
        if(flag && index ==1){
          form.setCount(++successNum);
        }
      }
      index ++ ;
    }
  }

  /**
   * 调用接口把成果，保存到群组
   * @param pubDetailVO
   * @param form
   */
  public  boolean savePubToGrp(PubDetailVO pubDetailVO , GrpManageForm form){

    if(pubDetailVO == null){
      return  false ;
    }
    Map<String, Object> map = new HashMap<>();
    Long pdwhPubId = pubDetailVO.getPubId();
    map.put("pubHandlerName", "saveSnsPubHandler");
    map.put("des3PdwhPubId", Des3Utils.encodeToDes3(pdwhPubId + ""));
    map.put("des3PsnId", Des3Utils.encodeToDes3(String.valueOf(form.getPsnId())));
    map.put("des3GrpId", form.getDes3gGrpId());
    map.put("isProjectPub", "1");
    map.put("pubGenre", GROUP_PUB);
    map.put("title", pubDetailVO.getTitle());
   //map.put("isPubConfirm", isPubConfirm);
    map.put("publishDate", pubDetailVO.getPublishDate());
    map.put("countryId", pubDetailVO.getCountryId());
    map.put("fundInfo", pubDetailVO.getFundInfo());
    map.put("citations", pubDetailVO.getCitations());
    map.put("doi", pubDetailVO.getDoi());
    map.put("summary", pubDetailVO.getSummary());
    map.put("keywords", pubDetailVO.getKeywords());
    map.put("srcFulltextUrl", pubDetailVO.getSrcFulltextUrl());
    map.put("pubType", pubDetailVO.getPubType() );
    map.put("recordFrom", PubSnsRecordFromEnum.IMPORT_FROM_PDWH);
    map.put("organization", pubDetailVO.getOrganization());
    map.put("sourceUrl", pubDetailVO.getSourceUrl());
    map.put("citedUrl", pubDetailVO.getCitationUrl());
    map.put("permission", 7);
    map.put("sourceId", pubDetailVO.getSourceId());
    map.put("srcDbId", pubDetailVO.getSrcDbId());
    map.put("dbId", pubDetailVO.getSrcDbId());
    map.put("fullText", pubDetailVO.getFullText());
    map.put("pubTypeInfo", pubDetailVO.getPubTypeInfo());
    map.put("HCP", pubDetailVO.getHCP());
    map.put("HP", pubDetailVO.getHP());
    map.put("OA", pubDetailVO.getOA());
    // 作者名不需要再传过去，基准库的members 直接到 个人库的members
    // map.put("authorNames", pdwhDetail.getAuthorNames());
    map.put("members", pubDetailVO.getMembers());
    map.put("situations", pubDetailVO.getSituations());
    String SERVER_URL = domainscm + V8pubQueryPubConst.PUBHANDLER_URL;
    Map<String, Object> result = (Map<String, Object>) getRemotePubInfo(map, SERVER_URL);
    if(result != null && result.get("des3PubId") != null){
      return  true;
    }
    return  false;
  }

  /**
   * 查找成果
   * @param pubId
   * @return
   */
  public PubDetailVO  findPdwhPubDetail(String  pubId){
    try {
      String SERVER_URL = domainscm + V8pubQueryPubConst.QUERY_PUB_DETAIL_URL;
      Map<String, Object> paramMap = new HashMap<>();
      paramMap.put(V8pubQueryPubConst.DES3_PUB_ID, Des3Utils.encodeToDes3(pubId));
      paramMap.put("serviceType", V8pubQueryPubConst.OPEN_PDWH_PUB);
      Map<String, Object> pubInfoMap = (Map<String, Object>) getRemotePubInfo(paramMap, SERVER_URL);
      PubDetailVO pubDetailVO = null;
      if (pubInfoMap != null) {
        pubDetailVO = PubDetailVoUtil.getBuilPubDetailVO(JacksonUtils.mapToJsonStr(pubInfoMap));
      }
      return  pubDetailVO ;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null ;
  }

  /**
   * 查询远程成果的信息
   *
   * @param paramMap
   * @param SERVER_URL
   * @return
   */
  public Object getRemotePubInfo(Map<String, Object> paramMap, String SERVER_URL) {
    // 设置请求头部
    HttpHeaders requestHeaders = new HttpHeaders();
    requestHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
    HttpEntity<String> requestEntity =
        new HttpEntity<String>(JacksonUtils.jsonObjectSerializer(paramMap), requestHeaders);
    Map<String, Object> object =
        (Map<String, Object>) restTemplate.postForObject(SERVER_URL, requestEntity, Object.class);
    return object;
  }
}
