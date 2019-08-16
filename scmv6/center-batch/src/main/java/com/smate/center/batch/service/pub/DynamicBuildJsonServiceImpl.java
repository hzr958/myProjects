package com.smate.center.batch.service.pub;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.sns.pub.Publication;
import com.smate.center.batch.service.psn.PersonManager;
import com.smate.core.base.utils.common.HtmlUtils;
import com.smate.core.base.utils.constant.ServiceConstants;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.model.security.Person;
import com.smate.core.base.utils.string.ServiceUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 动态生成service.
 * 
 * @author chenxiangrong
 * 
 */
@Service("dynamicBuildJsonService")
@Transactional(rollbackFor = Exception.class)
public class DynamicBuildJsonServiceImpl implements DynamicBuildJsonService {
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  private static final int TOP_N = 3;

  @Autowired
  private PersonManager personManager;
  @Autowired
  private PublicationService publicationService;
  @Value("${domainscm}")
  private String domainscm;


  /**
   * 添加成果、文献动态信息.
   */
  @SuppressWarnings("unchecked")
  @Override
  public String addPubRefDynamic(Long psnId, JSONObject jsonObject) throws ServiceException {
    // 基本参数
    Map<String, Object> jsonMap = getBaseProducerInfo(psnId, "");

    // 相应参数
    jsonMap.put("resType", jsonObject.get("resType"));
    List<Map<String, Object>> paramList =
        (List<Map<String, Object>>) JSONArray.fromObject(jsonObject.get("resDetails"));
    jsonMap.put("resTotal", paramList.size());
    jsonMap.put("resDetails", getPubRefDetails(paramList, TOP_N));
    return JacksonUtils.jsonObjectSerializer(jsonMap);
  }

  /**
   * 获取动态产生者的基本信息.
   * 
   * @param psnId
   * @return
   * @throws ServiceException
   */
  private Map<String, Object> getBaseProducerInfo(Long psnId, String key) throws ServiceException {
    Map<String, Object> jsonMap = new HashMap<String, Object>();
    // 基本参数
    Person person;
    person = personManager.getPerson(psnId);
    jsonMap.put(key + "psnId", psnId);
    jsonMap.put(key + "des3PsnId", ServiceUtil.encodeToDes3(psnId + ""));
    jsonMap.put(key + "psnName", personManager.getPsnName(person, "zh_CN"));
    jsonMap.put(key + "psnEnName", personManager.getPsnName(person, "en_US"));
    jsonMap.put(key + "psnAvatar", person.getAvatars());
    jsonMap.put(key + "psnIns", person.getInsName());
    jsonMap.put(key + "psnTitle", person.getViewTitolo());
    return jsonMap;
  }


  /**
   * 获取成果、文献的详细信息.
   * 
   * @param resDetailJson
   * @return
   * @throws ServiceException
   */
  private List<Map<String, Object>> getPubRefDetails(List<Map<String, Object>> paramList, int top)
      throws ServiceException {

    try {
      List<Map<String, Object>> resDetailList = new ArrayList<Map<String, Object>>();
      Map<String, Object> resDetailMap = null;
      Map<String, Object> paramMap = null;
      Long resId = null;
      int resNode;
      String snsWebDomain = domainscm + "/";
      for (int i = 0, length = paramList.size(); i < length && i < top; i++) {
        paramMap = paramList.get(i);
        resDetailMap = new HashMap<String, Object>();
        resId = MapUtils.getLong(paramMap, "resId");
        resNode = MapUtils.getIntValue(paramMap, "resNode", ServiceConstants.SCHOLAR_NODE_ID_1);

        Publication publication = publicationService.getPub(resId);
        if (publication != null) {
          resDetailMap.put("resId", resId);
          resDetailMap.put("des3ResId", ServiceUtil.encodeToDes3(resId + ""));
          resDetailMap.put("resNode", resNode);
          resDetailMap.put("resTitle", getPublicationName(publication, "zh"));
          resDetailMap.put("resEnTitle", getPublicationName(publication, "en"));
          String authors = publication.getAuthorNames() == null ? "" : publication.getAuthorNames();
          resDetailMap.put("resAuthor", authors);
          resDetailMap.put("resEnauthor", authors);
          String resOther = "";
          String resEnOther = "";
          if (!StringUtils.isBlank(publication.getBriefDesc())) {
            resOther += publication.getBriefDesc().replace(">", "&gt;").replace("<", "&lt;");
            resEnOther += publication.getBriefDescEn().replace(">", "&gt;").replace("<", "&lt;");
          }
          resDetailMap.put("resOther", resOther);
          resDetailMap.put("resEnother", resEnOther);
          resDetailMap.put("resLink", snsWebDomain + "/scmwebsns/publication/view?des3Id="
              + ServiceUtil.encodeToDes3(resId.toString()) + "," + resNode);
          resDetailList.add(resDetailMap);
        }
      }
      return resDetailList;
    } catch (Exception e) {
      logger.info("获取成果、文献的详细信息", e);
      throw new ServiceException("获取成果、文献的详细信息", e);
    }
  }

  private String getPublicationName(Publication publication, String locale) throws ServiceException {

    String title = "";
    if (StringUtils.isEmpty(locale)) {
      locale = LocaleContextHolder.getLocale().getLanguage();
    }
    if ("zh".equals(locale)) {
      title = StringUtils.isNotBlank(publication.getZhTitle()) ? publication.getZhTitle() : publication.getEnTitle();
      title = HtmlUtils.subString(title, 50, "......");

    } else {
      title = StringUtils.isNotBlank(publication.getEnTitle()) ? publication.getEnTitle() : publication.getZhTitle();
      title = HtmlUtils.subString(title, 50, "......");

    }
    return title;
  }

}
