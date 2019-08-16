package com.smate.center.task.service.email;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.task.dao.email.PubConfirmPromoteDao;
import com.smate.center.task.dao.rcmd.quartz.PubConfirmRolPubDao;
import com.smate.center.task.dao.rol.quartz.RolPubXmlDao;
import com.smate.center.task.exception.DaoException;
import com.smate.center.task.exception.ServiceException;
import com.smate.center.task.model.rcmd.quartz.PubConfirmRolPub;
import com.smate.center.task.model.rcmd.quartz.PublicationConfirm;
import com.smate.center.task.model.rol.quartz.RolPubXml;
import com.smate.core.base.utils.dao.security.InsPortalDao;
import com.smate.core.base.utils.model.InsPortal;
import com.smate.core.base.utils.string.ServiceUtil;

/**
 * 成果认领邮件推广服务
 * 
 * @author zk
 */
@Service("snsPubConfirmPromoteService")
@Transactional(rollbackFor = Exception.class)
public class SnsPubConfirmPromoteServiceImpl implements SnsPubConfirmPromoteService {

  private static Logger logger = LoggerFactory.getLogger(SnsPubConfirmPromoteServiceImpl.class);
  /**
   * xml根节点.
   */
  public static final String PUB_XML_ROOT_XPATH = "/data";
  @Autowired
  private PubConfirmPromoteDao pubConfirmPromoteDao;

  @Autowired
  private PubConfirmRolPubDao pubConfirmRolPubDao;

  @Autowired
  private RolPubXmlDao rolPubXmlDao;
  @Autowired
  private InsPortalDao insPortalDao;

  /**
   * 取所有待认领的成果中分数最高取一条
   */
  @SuppressWarnings({"rawtypes", "unchecked"})
  @Override
  public Map getPubTitlePlusByScore(Long psnId) throws ServiceException {

    Map params = new HashMap<String, Object>();
    try {
      // 获取认领成果id
      List<PublicationConfirm> PubConfirmList = pubConfirmPromoteDao.getPublicationConfirmByScore(psnId, SIZE);
      List<PubConfirmRolPub> pubDetails = new ArrayList<PubConfirmRolPub>();
      if (CollectionUtils.isNotEmpty(PubConfirmList)) {
        // 得到成果标题等信息
        for (PublicationConfirm PubConfirm : PubConfirmList) {
          PubConfirmRolPub pubDetail = pubConfirmRolPubDao.getCfmRolPubBypubIdNew(PubConfirm.getRolPubId());
          pubDetail
              .setZhTitle(pubDetail.getZhTitle() == null || pubDetail.getZhTitle().isEmpty() ? pubDetail.getEnTitle()
                  : pubDetail.getZhTitle());
          if (pubDetail.getTypeId() != null) {
            if (pubDetail.getTypeId() == 3 || pubDetail.getTypeId() == 4 || pubDetail.getTypeId() == 8) {
              pubDetail.setPubTypeName("论文");
            } else if (pubDetail.getTypeId() == 5) {
              pubDetail.setPubTypeName("专利");
            } else if (pubDetail.getTypeId() == 1) {
              pubDetail.setPubTypeName("奖励");
            } else if (pubDetail.getTypeId() == 2) {
              pubDetail.setPubTypeName("书/著作");
            } else if (pubDetail.getTypeId() == 7) {
              pubDetail.setPubTypeName("其他");
            } else if (pubDetail.getTypeId() == 10) {
              pubDetail.setPubTypeName("书籍章节");
            }
          }
          InsPortal insPortal = insPortalDao.get(PubConfirm.getInsId());
          if (insPortal != null) {
            String url = insPortal.getDomain();
            if (url.startsWith("http://")) {
              url += "/scmwebrol";
            } else {
              url = "http://" + url;
              url += "/scmwebrol";
            }
            pubDetail.setPubUrl(
                url + "/index/pubSearch/view?des3Id=" + ServiceUtil.encodeToDes3(PubConfirm.getRolPubId().toString()));
          } else {
            pubDetail.setPubUrl("");
          }

          pubDetails.add(pubDetail);
          logger.info("成果信息" + pubDetail.getAuthorNames() + pubDetail.getBriefDesc() + pubDetail.getZhTitle());
        }
        if (CollectionUtils.isEmpty(pubDetails)) {
          return null;
        }
        PubConfirmRolPub pubDetail = pubDetails.get(0);
        params.put("pubTitle",
            pubDetail.getZhTitle() == null || pubDetail.getZhTitle().isEmpty() ? pubDetail.getEnTitle()
                : pubDetail.getZhTitle());
        if (params == null || params.isEmpty()) {
          return null;
        }
        // 计算有多少条成果
        Long pubSum = pubConfirmPromoteDao.getCfmPubCount(psnId);
        params.put("pubSum", pubSum == null || pubSum == 0L ? 0 : pubSum);
        params.put("pubDetails", pubDetails);
      } else {
        params.put("pubSum", "0");
      }
    } catch (Exception e) {
      logger.error("成果认领邮件推广获取最新年份、成果标题时出错！psnId=" + psnId, e);
      throw new ServiceException("成果认领邮件推广获取最新年份、成果标题时出错！psnId=" + psnId);
    }
    return params;
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  private Map getPubTitleParams(List<Map> pubTitleList, Map params) throws ServiceException {
    if (CollectionUtils.isNotEmpty(pubTitleList)) {
      Integer size = 0;
      for (Map m : pubTitleList) {
        StringBuffer sb = new StringBuffer();
        if (m.get("authorNames") != null && StringUtils.isNotBlank(m.get("authorNames").toString())) {
          sb.append(m.get("authorNames").toString() + "，");
        }
        if (m.get("title") != null && StringUtils.isNotBlank(m.get("title").toString())) {
          sb.append(m.get("title").toString() + "，");
        }
        if (m.get("briefDesc") != null && StringUtils.isNotBlank(m.get("briefDesc").toString())) {
          sb.append(m.get("briefDesc").toString());
        }
        params.put("title" + size, sb.toString());
        params.put("full" + size, m.get("fulltextFileid"));
        params.put("rolPubId" + size, m.get("rolPubId"));
        // 用于邮件标题
        if (size == 0) {
          params.put("title", m.get("title").toString());
        }
        size++;
      }
    }
    return params;
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  private Map getPubTitleParamsNew(Map pubTitleMap, Map params, Long psnId) throws ServiceException {

    try {
      // isi库
      List<Integer> isiDbId = new ArrayList<Integer>();
      isiDbId.add(15);
      isiDbId.add(16);
      isiDbId.add(17);
      // 成果标题
      if (pubTitleMap.get("title") != null && StringUtils.isNotBlank(pubTitleMap.get("title").toString())) {
        params.put("title", pubTitleMap.get("title"));
      }
      // params.put("author",
      // pubMemberRolDao.getPubMemberFirst(Long.valueOf(ObjectUtils.toString(pubTitleMap.get("rolPubId")))));
      params.put("dbId", isiDbId.contains(pubTitleMap.get("dbId")) ? 1 : 0);
      params.put("fulltextId", pubTitleMap.get("fulltextFileid"));
      params.put("rolPubId", pubTitleMap.get("rolPubId"));
      params.put("briefDesc", pubTitleMap.get("briefDesc"));
      this.getPubXmlParams(pubTitleMap, params);
      return params;
    } catch (Exception e) {
      logger.error("成果认领推广邮件，获取成果数据时出错！", e);
    }
    return null;
  }

  /**
   * 
   * @param pubTitleMap
   * @param params
   * @throws ServiceException
   */
  @SuppressWarnings({"rawtypes", "unchecked"})
  private void getPubXmlParams(Map pubTitleMap, Map params) throws ServiceException {

    RolPubXml rolPubXml = rolPubXmlDao.get(Long.valueOf(ObjectUtils.toString(pubTitleMap.get("rolPubId"))));
    if (rolPubXml == null || StringUtils.isBlank(rolPubXml.getPubXml())) {
      return;
    }
    Map pubXmlMap = this.handlePubXml(rolPubXml, Integer.valueOf(ObjectUtils.toString(pubTitleMap.get("dbId"))));
    if (MapUtils.isEmpty(pubXmlMap)) {
      return;
    }
    params.put("pubData", pubXmlMap.get("pubData"));
    params.put("pubYear", pubXmlMap.get("pubYear"));
  }

  // 处理成果xml
  @SuppressWarnings("rawtypes")
  private Map handlePubXml(RolPubXml rolPubXml, Integer dbId) throws ServiceException {
    Map params = new HashMap<String, Object>();
    try {
      // 构建document
      Document doc = DocumentHelper.parseText(rolPubXml.getPubXml());
      if (dbId == 4) {
        this.handlePubXmlForTypeFour(doc, params);
      }
      if (dbId == 3) {
        this.handlePubXmlForTypeThree(doc, params);
      }
    } catch (Exception e) {
      logger.error("成果认领推广邮件，处理成果xml出错！", e);
    }
    return params;
  }

  /**
   * 期刊论文 type=4
   * 
   * @param doc
   * @param params
   * @throws ServiceException
   */
  @SuppressWarnings({"unchecked", "rawtypes"})
  private void handlePubXmlForTypeFour(Document doc, Map params) throws ServiceException {

    String pub_jname_xpath = "/pub_journal";
    String pub_publishyear_xpath = "/publication";
    params.put("pubData", this.getXmlNodeAttribute(doc, pub_jname_xpath, "jname"));
    params.put("pubYear", this.getXmlNodeAttribute(doc, pub_publishyear_xpath, "publish_year"));

  }

  /**
   * 会议论文 type=3
   * 
   * @param doc
   * @param params
   * @throws ServiceException
   */
  @SuppressWarnings({"rawtypes", "unchecked"})
  private void handlePubXmlForTypeThree(Document doc, Map params) throws ServiceException {

    String pub_confname_xpath = "/pub_conf_paper";
    String pub_publishyear_xpath = "/publication";
    params.put("pubData", this.getXmlNodeAttribute(doc, pub_confname_xpath, "conf_name"));
    params.put("pubYear", this.getXmlNodeAttribute(doc, pub_publishyear_xpath, "publish_year"));
  }

  /**
   * @param xpath xml元素路径
   * @param attrName xml属性名
   * @return String
   */
  public String getXmlNodeAttribute(Document doc, String xpath, String attrName) {
    if (doc == null) {
      throw new java.lang.NullPointerException("xml doc is null.");
    }
    Node node = this.getNode(doc, xpath);
    if (node != null) {
      return getXmlNodeAttributeValue(node, attrName);
    }
    return "";
  }

  public Node getNode(Document doc, String xpath) {
    if (doc == null) {
      throw new java.lang.NullPointerException("xml doc is null.");
    }
    if (!xpath.startsWith(PUB_XML_ROOT_XPATH)) {
      xpath = PUB_XML_ROOT_XPATH + xpath;
    }
    return doc.selectSingleNode(xpath);
  }

  /**
   * @param node xml节点
   * @param attrName xml属性名
   * @return String
   */
  public String getXmlNodeAttributeValue(Node node, String attrName) {
    if (node == null) {
      return "";
    }
    String val = node.valueOf("@" + attrName);
    if (val == null) {
      val = "";
    }
    return val.trim();
  }

  /**
   * 取最大年份中最多三条成果
   */
  @SuppressWarnings({"rawtypes", "unchecked"})
  @Override
  public Map getPubTitlePlus(Long psnId) throws ServiceException {

    Map params = new HashMap<String, Object>();
    Integer lastYear;
    try {
      // 获取最大年份
      lastYear = pubConfirmPromoteDao.getInsLastYear(psnId);
      if (lastYear == 0) {
        return null;
      }
      // 获取最多三条成果
      List<Map> pubTitleList = pubConfirmPromoteDao.getLastYearThreePub(psnId, lastYear);
      params = getPubTitleParams(pubTitleList, params);
      if (params.isEmpty()) {
        return null;
      }
      // 获取最新一年未确认成果总数
      Integer pubSum = pubConfirmPromoteDao.getLastYearSum(psnId, lastYear);
      params.put("pubSum", pubSum == null || pubSum == 0 ? 0 : pubSum);
    } catch (DaoException e) {
      logger.error("成果认领邮件推广获取最新年份、成果标题时出错！", e);
    }
    return params;
  }
}
