package com.smate.web.psn.service.search;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.transaction.Transactional;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.solr.client.solrj.SolrServerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import com.smate.core.base.psn.model.profile.PsnDisciplineKey;
import com.smate.core.base.solr.model.QueryFields;
import com.smate.core.base.solr.service.SolrIndexSerivceImpl;
import com.smate.core.base.solr.service.SolrIndexService;
import com.smate.core.base.utils.dao.security.PersonDao;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.model.Page;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.string.ServiceUtil;
import com.smate.web.psn.dao.friend.FriendDao;
import com.smate.web.psn.dao.friend.FriendTempDao;
import com.smate.web.psn.exception.DaoException;
import com.smate.web.psn.model.psninfo.PsnInfo;
import com.smate.web.psn.model.search.PersonSearch;
import com.smate.web.psn.service.profile.PsnDisciplineKeyService;
import com.smate.web.psn.service.statistics.PsnStatisticsService;

/**
 * 人员检索服务实现.
 * 
 * @author xys
 *
 */
@Service("psnSearchService")
@Transactional(rollbackOn = Exception.class)
public class PsnSearchServiceImpl implements PsnSearchService {

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private SolrIndexService solrIndexService;

  @Autowired
  private PsnStatisticsService psnStatisticsService;

  @Autowired
  private PersonDao personDao;
  @Autowired
  private FriendDao friendDao;
  @Autowired
  private FriendTempDao friendTempDao;
  @Autowired
  private PsnDisciplineKeyService psnDisciplineKeyService;
  @Autowired
  private PersonSearchService personSearchService;

  @SuppressWarnings({"unchecked", "rawtypes"})
  @Override
  public Page<PersonSearch> getPsns(Page<PersonSearch> page, QueryFields queryFields) throws SolrServerException {

    List<PersonSearch> psnList = new ArrayList<PersonSearch>();
    if ("mobileSearchFriend".equals(queryFields.getFromPage())) {// 移动端
      excludeFriendIds(queryFields);
    }
    try {
      this.getUserNameAndInsFromStr(queryFields);
    } catch (Exception e) {
      logger.error("从人员检索输入中提取人员姓名与机构名称出错: ", e);
    }
    Map<String, Object> rsMap = solrIndexService.queryPersons(page.getParamPageNo(), page.getPageSize(), queryFields);
    String count = (String) rsMap.get(SolrIndexSerivceImpl.RESULT_NUMFOUND);
    String items = (String) rsMap.get(SolrIndexSerivceImpl.RESULT_ITEMS);
    String highlight = (String) rsMap.get(SolrIndexSerivceImpl.RESULT_HIGHLIGHT);
    List<Map<String, Object>> listItems = JacksonUtils.jsonToList(items);
    Map<String, Map<String, List<String>>> highligh = JacksonUtils.jsonToMap(highlight);
    if (listItems != null && listItems.size() > 0) {
      for (int i = 0; i < listItems.size(); i++) {
        Map item = listItems.get(i);
        PersonSearch personSearch = new PersonSearch();
        setperson(item, personSearch, highligh);
        psnList.add(personSearch);
      }
    }

    page.setResult(psnList);
    page.setTotalCount(NumberUtils.toLong(count));

    return page;

  }

  /**
   * 移动端-发现好友-排除好友psnId
   * 
   * @author lhd
   * @param queryFields
   */
  private void excludeFriendIds(QueryFields queryFields) {
    List<Long> psnIdsList = new ArrayList<Long>();
    try {
      List<Long> friendIdList = friendDao.getFriendListByPsnId(SecurityUtils.getCurrentUserId());
      List<Long> tempFriendIdList = friendTempDao.getTempFriendIds(SecurityUtils.getCurrentUserId());
      if (CollectionUtils.isNotEmpty(friendIdList)) {
        for (Long friId : friendIdList) {
          psnIdsList.add(friId);
        }
        if (CollectionUtils.isNotEmpty(tempFriendIdList)) {
          for (Long tempId : tempFriendIdList) {
            if (!psnIdsList.contains(tempId)) {
              psnIdsList.add(tempId);
            }
          }
        }
      } else {
        if (CollectionUtils.isNotEmpty(tempFriendIdList)) {
          for (Long tempId : tempFriendIdList) {
            psnIdsList.add(tempId);
          }
        }
      }
      psnIdsList.add(SecurityUtils.getCurrentUserId());
      queryFields.setPsnIdList(psnIdsList);
    } catch (Exception e) {
      logger.error("获取好友id出错", "当前人psnId=" + SecurityUtils.getCurrentUserId());
    }
  };

  @Override
  public String getPsnOtherInfo(String des3PsnIdsStrs) {
    String jsonInfoList;
    List<PsnInfo> psnInfoList = new ArrayList<PsnInfo>();
    if (StringUtils.isNotBlank(des3PsnIdsStrs)) {
      String[] des3PsnIds = des3PsnIdsStrs.split(",");
      if (des3PsnIds != null && des3PsnIds.length > 0) {
        for (String des3PsnId : des3PsnIds) {
          PsnInfo psnInfo = new PsnInfo();
          try {
            psnInfo.setDes3PsnId(des3PsnId);
            long psnId = NumberUtils.toLong(ServiceUtil.decodeFromDes3(des3PsnId));
            psnInfo.setPsnId(psnId);
            psnInfo.setPsnStatistics(psnStatisticsService.getPsnStatistics(psnId));
            psnInfo.setAvatarUrl(personDao.getPsnImgByObjectId(psnId));
            psnInfoList.add(psnInfo);
          } catch (Exception e) {
            logger.error("人员其他信息获取出错");
          }
        }
      }
    }
    jsonInfoList = JacksonUtils.listToJsonStr(psnInfoList);
    return jsonInfoList;
  }

  @SuppressWarnings("rawtypes")
  private void setperson(Map item, PersonSearch personSearch, Map<String, Map<String, List<String>>> highligh) {
    // 高亮psnName enPsnName title zhInsName enInsName zhUnit enUnit
    String id = ObjectUtils.toString(item.get("id"));
    String title = "";
    String psnName = "";
    String enPsnName = "";
    String zhInsName = "";
    String enInsName = "";
    String zhUnit = "";
    if (StringUtils.isNotBlank(id)) {
      Map<String, List<String>> highlightMap = highligh.get(id);
      if (highlightMap != null && highlightMap.size() > 0) {// 取高亮
        Set<String> keySet = highlightMap.keySet();
        for (String string : keySet) {
          StringBuffer sb = new StringBuffer();
          List<String> list = highlightMap.get(string);
          if (list == null)
            continue;
          for (String string2 : list) {
            sb.append(string2);
          }
          switch (string) {
            case "psnName":
              psnName = sb.toString();
              break;
            case "enPsnName":
              enPsnName = sb.toString();
              break;
            case "title":
              title = sb.toString();
              break;
            case "zhInsName":
              zhInsName = sb.toString();
              break;
            case "enInsName":
              enInsName = sb.toString();
              break;
            case "zhUnit":
              zhUnit = sb.toString();
              break;
            default:
              break;
          }
        }
      }
    }
    if (StringUtils.isBlank(psnName)) {
      psnName = ObjectUtils.toString(item.get("psnName"));
    }
    if (StringUtils.isBlank(enPsnName)) {
      enPsnName = ObjectUtils.toString(item.get("enPsnName"));
    }
    if (StringUtils.isBlank(title)) {
      title = ObjectUtils.toString(item.get("title"));
    }
    if (StringUtils.isBlank(zhInsName)) {
      zhInsName = ObjectUtils.toString(item.get("zhInsName"));
    }
    if (StringUtils.isBlank(enInsName)) {
      enInsName = ObjectUtils.toString(item.get("enInsName"));
    }
    if (StringUtils.isBlank(zhUnit)) {
      zhUnit = ObjectUtils.toString(item.get("zhUnit"));
    }
    String language = LocaleContextHolder.getLocale().toString();
    personSearch.setPsnId(NumberUtils.toLong(ObjectUtils.toString(item.get("psnId"))));
    personSearch.setPrjSum(NumberUtils.toInt(ObjectUtils.toString(item.get("psnPrjCount"))));
    personSearch.setPubSum(NumberUtils.toInt(ObjectUtils.toString(item.get("psnPubCount"))));
    personSearch.setPsnInfoIntegrity(NumberUtils.toInt(ObjectUtils.toString(item.get("psnInfoIntegrity"))));
    personSearch.setDes3PsnId(ServiceUtil.encodeToDes3(ObjectUtils.toString(personSearch.getPsnId())));
    personSearch.setPosition(StringUtils.isNotBlank(title) ? title : ObjectUtils.toString(item.get("title")));
    personSearch.setTitolo(personSearch.getPosition());
    personSearch.setOpenId(NumberUtils.toLong(ObjectUtils.toString(item.get("openId"))));
    if ("zh_CN".equals(language)) {
      personSearch.setName(StringUtils.isNotBlank(psnName) ? psnName : enPsnName);
      personSearch.setInsName(StringUtils.isNotBlank(zhInsName) ? zhInsName : enInsName);
    } else {
      personSearch.setName(StringUtils.isNotBlank(enPsnName) ? enPsnName : psnName);
      personSearch.setInsName(StringUtils.isNotBlank(enInsName) ? enInsName : zhInsName);
    }

    // 重构list中人员显示信息，与个人主页保持一致
    if (StringUtils.isBlank(personSearch.getInsName()) || StringUtils.isBlank(zhUnit)) {

      personSearch.setInsName((StringUtils.isBlank(personSearch.getInsName()) ? "" : personSearch.getInsName())
          + (StringUtils.isBlank(zhUnit) ? "" : zhUnit));
    } else {
      personSearch.setInsName(personSearch.getInsName() + ", " + zhUnit);
    }
    // SCM-15457，添加人员头像
    personSearch.setAvatars(personDao.getPsnImgByObjectId(personSearch.getPsnId()));
    // 添加关键词
    List<PsnDisciplineKey> keyList = psnDisciplineKeyService.findPsnKeyWords(personSearch.getPsnId());
    personSearch.setDiscList(keyList);
    Long currentPsnId = SecurityUtils.getCurrentUserId();
    try {
      if (currentPsnId != null && personSearch.getPsnId() != null) {
        boolean needButton = !friendDao.isFriend(currentPsnId, personSearch.getPsnId());
        if (currentPsnId.equals(personSearch.getPsnId())) {// 本人不显示添加好友按钮
          needButton = false;
        }
        personSearch.setNeedButton(needButton);// 是否显示添加好友按钮
      }
    } catch (DaoException e) {
      logger.error("检索人员查看是否是好有出错 currentPsnId=" + currentPsnId + "psnId=" + personSearch.getPsnId());
    }
  }

  @Override
  public Map<String, Object> getLeftMenu(QueryFields queryFields) {
    return null;
  }

  @SuppressWarnings("rawtypes")
  @Override
  public String getAvatarUrls(String des3PsnIdsStr) {
    Map<String, Object> map = new HashMap<String, Object>();
    String[] des3PsnIdsArr = StringUtils.split(des3PsnIdsStr, ",");
    if (des3PsnIdsArr != null && des3PsnIdsArr.length > 0) {
      List<Long> psnIds = new ArrayList<Long>();
      for (String des3PsnId : des3PsnIdsArr) {
        try {
          psnIds.add(NumberUtils.toLong(ServiceUtil.decodeFromDes3(des3PsnId)));
        } catch (Exception e) {
          logger.error("psnId解密或转换出现异常了喔!");
        }
      }
      if (!CollectionUtils.isEmpty(psnIds)) {
        List<Map> avatarUrls = personDao.getAvatarUrls(psnIds);
        map.put("avatarUrls", avatarUrls);
        map.put("result", "success");
      }
    }
    return JacksonUtils.jsonObjectSerializer(map);
  }

  // 修改用于app
  @Override
  public Map<String, Object> getAvatarUrlsMap(String des3PsnIdsStr) {
    Map<String, Object> map = new HashMap<String, Object>();
    String[] des3PsnIdsArr = StringUtils.split(des3PsnIdsStr, ",");
    if (des3PsnIdsArr != null && des3PsnIdsArr.length > 0) {
      List<Long> psnIds = new ArrayList<Long>();
      for (String des3PsnId : des3PsnIdsArr) {
        try {
          psnIds.add(NumberUtils.toLong(ServiceUtil.decodeFromDes3(des3PsnId)));
        } catch (Exception e) {
          logger.error("psnId解密或转换出现异常!");
        }
      }
      if (!CollectionUtils.isEmpty(psnIds)) {
        List<Map> avatarUrls = personDao.getAvatarUrls(psnIds);
        map.put("avatarUrls", avatarUrls);
      }
    }
    return map;
  }

  @Override
  public Page<PersonSearch> findPsn(Page<PersonSearch> page, QueryFields queryFields) throws SolrServerException {
    List<PersonSearch> psnList = new ArrayList<PersonSearch>();
    if (queryFields.getNeedExcludeFriendId()) {
      excludeFriendIds(queryFields);
    }
    Map<String, Object> rsMap =
        solrIndexService.queryPersonsYouMayKnow(page.getParamPageNo(), page.getPageSize(), queryFields);
    String count = (String) rsMap.get(SolrIndexSerivceImpl.RESULT_NUMFOUND);
    String items = (String) rsMap.get(SolrIndexSerivceImpl.RESULT_ITEMS);
    String highlight = (String) rsMap.get(SolrIndexSerivceImpl.RESULT_HIGHLIGHT);
    List<Map<String, Object>> listItems = JacksonUtils.jsonToList(items);
    Map<String, Map<String, List<String>>> highligh = JacksonUtils.jsonToMap(highlight);
    if (listItems != null && listItems.size() > 0) {
      for (int i = 0; i < listItems.size(); i++) {
        Map item = listItems.get(i);
        PersonSearch personSearch = new PersonSearch();
        setperson(item, personSearch, highligh);
        psnList.add(personSearch);
      }
    }

    page.setResult(psnList);
    page.setTotalCount(NumberUtils.toLong(count));
    if (page.getTotalCount() > 0) {
      Long totalPages = (long) Math.ceil((double) page.getTotalCount() / page.getPageSize());
      page.setTotalPages(totalPages);
    }
    return page;
  }

  @Override
  public void getPsnsForMsg(Page<PersonSearch> page, QueryFields queryFields) throws SolrServerException {
    // SCM-15917 hzr建议检索时去掉分词，对关键词进行完全匹配
    queryFields.setSearchString("\"" + queryFields.getSearchString() + "\"");
    queryFields.setFromPage("searchPersons");
    List<Long> excludePsnId = new ArrayList<>();
    excludePsnId.add(SecurityUtils.getCurrentUserId());
    queryFields.setPsnIdList(excludePsnId);
    List<PersonSearch> psnList = new ArrayList<PersonSearch>();
    Map<String, Object> rsMap = solrIndexService.queryPersons(page.getParamPageNo(), page.getPageSize(), queryFields);
    String count = (String) rsMap.get(SolrIndexSerivceImpl.RESULT_NUMFOUND);
    String items = (String) rsMap.get(SolrIndexSerivceImpl.RESULT_ITEMS);
    String highlight = (String) rsMap.get(SolrIndexSerivceImpl.RESULT_HIGHLIGHT);
    List<Map<String, Object>> listItems = JacksonUtils.jsonToList(items);
    Map<String, Map<String, List<String>>> highligh = JacksonUtils.jsonToMap(highlight);
    if (listItems != null && listItems.size() > 0) {
      for (int i = 0; i < listItems.size(); i++) {
        Map item = listItems.get(i);
        PersonSearch personSearch = new PersonSearch();
        setperson(item, personSearch, highligh);
        personSearch.setName(removeHtml(personSearch.getName()));
        // 已添加到setperson()
        // personSearch.setAvatars(personDao.getPsnImgByObjectId(personSearch.getPsnId()));
        psnList.add(personSearch);
      }
    }
    page.setResult(psnList);
    page.setTotalCount(NumberUtils.toLong(count));

  }

  private String removeHtml(String name) {
    return name.replaceAll("<[^>]+>", "").trim();
  }

  @Override
  public List<Long> findNotNeedFriendReqPsnIds(Long psnId) throws SolrServerException {
    List<Long> psnIdsList = new ArrayList<Long>();
    try {
      List<Long> friendIdList = friendDao.getFriendListByPsnId(psnId);
      // List<Long> tempFriendIdList =
      // friendTempDao.getTempFriendIds(psnId);
      psnIdsList.addAll(friendIdList);
      // psnIdsList.addAll(tempFriendIdList);
      psnIdsList.add(SecurityUtils.getCurrentUserId());
    } catch (Exception e) {
      logger.error("获取好友id出错", "当前人psnId=" + SecurityUtils.getCurrentUserId());
      throw new SolrServerException(e);
    }
    return psnIdsList;
  }

  /**
   * 抽取查询字符中的人名和机构名
   * 
   * @param queryFields
   * @return
   * @throws Exception
   */
  private Map<String, Set<String>> getUserNameAndInsFromStr(QueryFields queryFields) throws Exception {
    String search = queryFields.getSearchString();
    if (StringUtils.isEmpty(search)) {
      return null;
    }
    Map<String, Set<String>> mp = personSearchService.getExtractUserAndInsName(search);
    queryFields.setInsNames(mp.get("scm_ins_name"));
    queryFields.setUseNames(mp.get("scm_user_name"));
    return mp;
  }
}
