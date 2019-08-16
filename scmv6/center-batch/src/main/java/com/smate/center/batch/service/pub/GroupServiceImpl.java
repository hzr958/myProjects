package com.smate.center.batch.service.pub;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;
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

import com.smate.center.batch.constant.PubGenreConstants;
import com.smate.center.batch.dao.pdwh.pub.PdwhPubFullTextDAO;
import com.smate.center.batch.dao.pdwh.pub.PubPdwhDetailDAO;
import com.smate.center.batch.dao.sns.friend.GroupInviteDao;
import com.smate.center.batch.dao.sns.prj.GroupInvitePsnDao;
import com.smate.center.batch.dao.sns.pub.GroupFundInfoDao;
import com.smate.center.batch.dao.sns.pub.GroupFundInfoMembersDao;
import com.smate.center.batch.dao.sns.pub.GroupPsnNodeDao;
import com.smate.center.batch.dao.sns.pub.GroupPsnRecommendDao;
import com.smate.center.batch.dao.sns.pub.GroupPubDao;
import com.smate.center.batch.dao.sns.pub.GroupRefDao;
import com.smate.center.batch.dao.sns.pub.GrpPubRcmdDao;
import com.smate.center.batch.dao.tmp.pdwh.PubFundingInfoDao;
import com.smate.center.batch.enums.pub.PublicationEnterFormEnum;
import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.pdwh.pub.PdwhPubFullTextPO;
import com.smate.center.batch.model.sns.prj.GroupInvitePsn;
import com.smate.center.batch.model.sns.prj.GroupMember;
import com.smate.center.batch.model.sns.pub.GroupFundInfo;
import com.smate.center.batch.model.sns.pub.GroupFundInfoMembers;
import com.smate.center.batch.model.sns.pub.GroupInvite;
import com.smate.center.batch.model.sns.pub.GroupPsn;
import com.smate.center.batch.model.sns.pub.GroupPsnNode;
import com.smate.center.batch.model.sns.pub.GroupPsnRecommend;
import com.smate.center.batch.model.sns.pub.GrpPubRcmd;
import com.smate.center.batch.model.sns.pub.PubXmlProcessContext;
import com.smate.center.batch.model.sns.pub.PublicationXml;
import com.smate.center.batch.model.tmp.pdwh.PubFundingInfo;
import com.smate.center.batch.oldXml.pub.PubXmlDocument;
import com.smate.center.batch.service.pdwh.pub.PublicationXmlPdwhService;
import com.smate.center.batch.service.psn.PersonManager;
import com.smate.core.base.pub.consts.V8pubQueryPubConst;
import com.smate.core.base.pub.enums.PubSnsRecordFromEnum;
import com.smate.core.base.utils.dao.security.PersonDao;
import com.smate.core.base.utils.data.XmlUtil;
import com.smate.core.base.utils.exception.SysServiceException;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.model.security.Person;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.string.IrisStringUtils;
import com.smate.web.v8pub.dom.PubSituationBean;
import com.smate.web.v8pub.dom.pdwh.PubPdwhDetailDOM;
import com.smate.web.v8pub.dto.PubFulltextDTO;

/**
 * 群组接口实现类.
 * 
 * @author zhuagnyanming
 * 
 */
@Service("groupService")
@Transactional(rollbackFor = Exception.class)
public class GroupServiceImpl implements GroupService {

  private static final long serialVersionUID = -4293398807687458953L;
  private static final Integer TOP_N = 8;
  protected final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private GroupInvitePsnDao groupInvitePsnDao;
  @Autowired
  private PersonManager personManager;
  @Autowired
  private GroupPsnSearchService groupPsnSearchService;
  @Autowired
  private GroupPubDao groupPubDao;
  @Autowired
  private GroupRefDao groupRefDao;
  @Autowired
  private GroupPsnNodeDao groupPsnNodeDao;
  @Autowired
  private GroupInviteDao groupInviteDao;
  @Autowired
  private GroupMemberManageService groupMemberManageService;
  @Autowired
  private GroupFundInfoDao groupFundInfoDao;
  @Autowired
  private PublicationXmlPdwhService publicationXmlPdwhService;
  @Autowired
  private ScholarPublicationXmlManager scholarPublicationXmlManager;
  @Autowired
  private PubFundingInfoDao pubFundingInfoDao;
  @Autowired
  private GroupFundInfoMembersDao groupFundInfoMembersDao;
  @Autowired
  private PersonDao personDao;
  @Autowired
  private GroupPsnRecommendDao groupPsnRecommendDao;
  @Autowired
  private PdwhPubFullTextDAO pdwhPubFullTextDAO;
  @Autowired
  private PubPdwhDetailDAO pubPdwhDetailDao;
  @Value("${domainscm}")
  private String snsDomain; // 科研之友域名
  @Resource(name = "restTemplate")
  private RestTemplate restTemplate;
  @Autowired
  private GrpPubRcmdDao grpPubRcmdDao;

  @Override
  public GroupInvitePsn findGroupInvitePsn(Long groupId) throws ServiceException {
    return findGroupInvitePsn(groupId, false);
  }

  @Override
  public GroupInvitePsn findGroupInvitePsn(Long groupId, boolean isActivity) throws ServiceException {
    Long userId = SecurityUtils.getCurrentUserId();
    GroupInvitePsn groupInvitePsn = null;
    try {
      groupInvitePsn = groupInvitePsnDao.findGroupInvitePsn(groupId, userId);

      Person person = personManager.getPersonByRecommend(userId);

      if (groupInvitePsn != null) {
        groupInvitePsn.setPsnName(personManager.getPsnName(person));
        groupInvitePsn.setPsnFirstName(person.getFirstName());
        groupInvitePsn.setPsnLastName(person.getLastName());
        if (isActivity) {
          groupInvitePsn.setIsActivity(ACTIVITY);// 激活
          groupInvitePsnDao.save(groupInvitePsn);
        }
      }

    } catch (Exception e) {
      logger.error("获取群组个人出错", e);
      throw new ServiceException(e);
    }

    return groupInvitePsn;
  }

  @Override
  public GroupPsn getGroupPsnInfo(Long groupId) {

    return groupPsnSearchService.getGroupPsn(groupId);// groupPsnDao.get(groupId);
  }

  @Override
  public void toSendSyncGroupInvitePsn(Long groupId) {
    // TODO 2015-10-24 MQ -done
    groupMemberManageService.toSendSyncGroupInvitePsn(groupId, null);
  }

  @Override
  public boolean checkPsnIsInPubGroup(Long psnId, Long pubId) throws ServiceException {
    boolean psnIsInPubGroup = false;
    try {
      List<Long> groupIdList = this.groupPubDao.queryGroupIdListByPubId(pubId);
      if (CollectionUtils.isNotEmpty(groupIdList)) {
        psnIsInPubGroup = this.groupInvitePsnDao.checkPsnInGroups(psnId, groupIdList);
      } else {
        groupIdList = this.groupRefDao.queryGroupIdListByRefId(pubId);
        if (CollectionUtils.isNotEmpty(groupIdList)) {
          psnIsInPubGroup = this.groupInvitePsnDao.checkPsnInGroups(psnId, groupIdList);
        }
      }
    } catch (Exception e) {
      logger.error(String.format("判断人员psnId={0}是否在成果pubId={1}所在的群组中出现异常：", psnId, pubId), e);
    }

    return psnIsInPubGroup;
  }

  @Override
  public GroupPsnNode findGroup(Long groupId) throws ServiceException {
    GroupPsnNode groupPsnNode = null;

    groupPsnNode = groupPsnNodeDao.findGroupPnsNode(groupId);

    return groupPsnNode;
  }

  @Override
  public boolean checkInviteIsValid(Long inviteId) throws ServiceException {

    GroupInvite groupInvite = this.groupInviteDao.get(inviteId);

    return (groupInvite != null);
  }

  @Override
  public GroupInvite getGroupInviteById(Long inviteId) throws ServiceException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public GroupMember findGroupMemberDetail(Long psnId) throws ServiceException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public List<GroupFundInfo> getGroupFundInfo(Integer size, Long startGroupId, Long endGroupId) {
    List<GroupFundInfo> toDoList = this.groupFundInfoDao.getToHandleList(size, startGroupId, endGroupId);
    return toDoList;
  }

  @Override
  public void saveOpResult(GroupFundInfo groupInfo, Integer status) {
    groupInfo.setStatus(status);
    this.groupFundInfoDao.save(groupInfo);
  }

  @Override
  public List<PubFundingInfo> getPubFundingInfoByFundingNo(String fundingNo) {
    if (StringUtils.isEmpty(fundingNo)) {
      return null;
    }
    List<PubFundingInfo> list = this.pubFundingInfoDao.getPubFundingInfoByFundingNo(fundingNo);
    return list;
  }

  @Override
  public void importPdwhPubIntoGroup(Long groupId, Long psnId, Long pubId, Integer dbId) throws Exception {
    String xmlStr = this.getPdwhPubXml(pubId, dbId);
    xmlStr = xmlStr.replace("&amp;amp;amp;", "&amp;");// 针对万方期刊中的&符号
    xmlStr = xmlStr.replaceAll("&#183;", "·"); // 针对万方 "·"没有转换
    // inputStr = inputStr.replace("&apos", "&apos;");// 针对isi
    // 处理&：如果&后面跟随的不是如下6个字符串（&lt; &gt; &amp; &apos; &quot;），则将&变为&amp;
    xmlStr = IrisStringUtils.filterSupplementaryCharsChina(xmlStr);
    Document doc = DocumentHelper.parseText(xmlStr);
    Node pitem = doc.selectSingleNode("/data");
    Node item = pitem.selectSingleNode("publication");
    Element ele = (Element) item;
    Integer pubType = NumberUtils.toInt(StringUtils.trimToEmpty(((Element) item).attributeValue("pub_type")));

    // 格式化标题、作者名、期刊名大小写，只有全部是大写时才格式化
    this.rebuildTitleAuthJnlName(ele);
    String ctitle = ele.attributeValue("ctitle");
    String etitle = ele.attributeValue("etitle");
    String dbCode = item.valueOf("@source_db_code");
    String authors = item.valueOf("@author_names");
    String authorsSpec = item.valueOf("@authors_names_spec");
    ele.addAttribute("match_owner", ObjectUtils.toString(matchPubAthor(authors, psnId)));
    ele.addAttribute("imp_match_owner_flag", "1");

    // 文件导入收录情况
    this.recordList(pitem, item);
    this.rebuildCountry(item, ele);
    // 根据成果中的作者是否pf匹配当前用户，给导入列表着色
    // 处理文件导入时中英文作者名
    this.rebuildAutherMatch(ele, authors, authorsSpec, psnId);
    this.rebuildImpXmlBriefDesc(pitem, item, ele, psnId);
    // 处理万方日期
    this.rebuildImpXmlWanFangDate(item, ele);

    // 导入群组不需要查重,只处理成果
    Long resutlId = this.scholarPublicationXmlManager.importPubXml(pitem.asXML(), psnId, pubType, 1, 0, groupId, null);

  }

  private String getPdwhPubXml(Long pubId, Integer dbId) throws Exception {
    if (pubId == null || dbId == null) {
      return null;
    }
    PublicationXml xml = publicationXmlPdwhService.getPdwhPubXmlById(pubId, dbId);
    if (xml == null || StringUtils.isBlank(xml.getXmlData())) {
      return null;
    }
    String xmlStr = "";
    try {
      xmlStr = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
      Document doc = DocumentHelper.parseText(xml.getXmlData());
      Node item = doc.selectSingleNode("/data");
      // Node item = (Node) nodes.get(0);
      Element pub = (Element) item.selectSingleNode("publication");
      pub.addAttribute("pubid", String.valueOf(pubId));
      pub.addAttribute("dbid", String.valueOf(dbId));
      if (null == item.selectSingleNode("pub_pdwh")) {
        Element em = ((Element) item).addElement("pub_pdwh");
        if (ArrayUtils.contains(ConstPdwhPubRefDb.ISI_LIST, dbId)) {
          em.addAttribute("isi_id", String.valueOf(pubId));
        }
        if (ConstPdwhPubRefDb.SCOPUS.equals(dbId)) {
          em.addAttribute("sps_id", String.valueOf(pubId));
        }
        if (ConstPdwhPubRefDb.EI.equals(dbId)) {
          em.addAttribute("ei_id", String.valueOf(pubId));
        }
        if (ConstPdwhPubRefDb.CNKI.equals(dbId)) {
          em.addAttribute("cnki_id", String.valueOf(pubId));
        }
        if (ConstPdwhPubRefDb.CNIPR.equals(dbId)) {
          em.addAttribute("cnipr_id", String.valueOf(pubId));
        }
        if (ConstPdwhPubRefDb.WanFang.equals(dbId)) {
          em.addAttribute("wf_id", String.valueOf(pubId));
        }
      }
      xmlStr = xmlStr + item.asXML();
    } catch (Exception e) {
      logger.error("获取基准库成果xml出错", e);
    }
    return xmlStr;
  }

  @SuppressWarnings("deprecation")
  private void rebuildTitleAuthJnlName(Element ele) {
    // 格式化各文献库的英文title大小写
    ele.setAttributeValue("etitle", XmlUtil.formatTitle(ele.attributeValue("etitle")));
    // 格式化各文献库的期刊名称大小写
    ele.setAttributeValue("original", XmlUtil.formatJnlTitle(ele.attributeValue("original")));
    // 格式化各文献库导入的作者名的大小写
    ele.setAttributeValue("author_names", XmlUtil.formatAuthors(ele.attributeValue("author_names")));
    // 格式化各文献库导入的作者名的大小写
    String namesSpec = XmlUtil.formatAuthors(ele.attributeValue("authors_names_spec"));
    namesSpec = namesSpec.replace("，", ";");
    ele.setAttributeValue("authors_names_spec", namesSpec);
  }

  public boolean matchPubAthor(String authorNames, Long psnId) throws SysServiceException {
    if (StringUtils.isBlank(authorNames)) {
      return false;
    }
    Person psn = personManager.getPerson(psnId);
    if (psn == null) {
      return false;
    }
    String firstName = XmlUtil.getCleanAuthorName(psn.getFirstName());
    String lastName = XmlUtil.getCleanAuthorName(psn.getLastName());
    String name = XmlUtil.getCleanAuthorName(psn.getName());
    String[] authors = authorNames.split(";|,|；");
    boolean flag = false;
    for (String pmName : authors) {
      pmName = pmName.toLowerCase();
      pmName = pmName.trim();
      if (StringUtils.isNotBlank(name)) {
        if (pmName.equalsIgnoreCase(psn.getName())) {
          flag = true;
          break;
        }
      }
      if (StringUtils.isBlank(firstName) || StringUtils.isBlank(lastName)) {
        flag = false;
        break;
      }
      String preF = firstName.substring(0, 1).toLowerCase();
      lastName = lastName.toLowerCase();
      // 尝试z lin 是否匹配上alen z lin或者 z alen lin
      int index = pmName.indexOf(preF);
      if (index > -1 && pmName.substring(index).endsWith(lastName)) {
        flag = true;
        break;
      }
      // 尝试lin z是否匹配上lin z alen或者lin alen z
      index = pmName.lastIndexOf(preF);
      if (index > 0 && pmName.substring(0, index).startsWith(lastName)) {
        flag = true;
        break;
      }
    }
    return flag;
  }

  private void recordList(Node pitem, Node item) {
    try {
      String list_ei = item.valueOf("@list_ei");
      if (StringUtils.isNotBlank(list_ei) && "是".equals(list_ei.trim())) {
        if (pitem.selectSingleNode("pub_list") == null)
          ((Element) pitem).addElement("pub_list");
        ((Element) pitem.selectSingleNode("pub_list")).addAttribute("list_ei", "1");
      }
      String list_sci = item.valueOf("@list_sci");
      if (StringUtils.isNotBlank(list_sci) && "是".equals(list_sci.trim())) {
        if (pitem.selectSingleNode("pub_list") == null)
          ((Element) pitem).addElement("pub_list");
        ((Element) pitem.selectSingleNode("pub_list")).addAttribute("list_sci", "1");
      }
      String list_istp = item.valueOf("@list_istp");
      if (StringUtils.isNotBlank(list_istp) && "是".equals(list_istp.trim())) {
        if (pitem.selectSingleNode("pub_list") == null)
          ((Element) pitem).addElement("pub_list");
        ((Element) pitem.selectSingleNode("pub_list")).addAttribute("list_istp", "1");
      }
      String list_ssci = item.valueOf("@list_ssci");
      if (StringUtils.isNotBlank(list_ssci) && "是".equals(list_ssci.trim())) {
        if (pitem.selectSingleNode("pub_list") == null)
          ((Element) pitem).addElement("pub_list");
        ((Element) pitem.selectSingleNode("pub_list")).addAttribute("list_ssci", "1");
      }
    } catch (Exception e) {
      logger.error("处理文件导入时的收录情况出错", e);
    }
  }

  private void rebuildCountry(Node item, Element ele) {
    Integer subIndex = item.valueOf("@city").lastIndexOf(",");
    if (!subIndex.equals(-1)) {
      ele.addAttribute("country_name", item.valueOf("@city").substring(subIndex + 1));
      ele.addAttribute("city", item.valueOf("@city").substring(0, subIndex));
    }
  }

  private void rebuildAutherMatch(Element ele, String authors, String authorsSpec, Long psnId) {
    if (StringUtils.isBlank(authors) && StringUtils.isNotBlank(authorsSpec)) {
      authors = authorsSpec;
      ele.addAttribute("author_names", authorsSpec);
    }
    ele.addAttribute("author_match", String.valueOf(isMatchMy(authors, psnId)));
  }

  private int isMatchMy(String authors, Long psnId) {
    int matchFlag = 0;
    try {
      Person person = personManager.getPerson(psnId);
      if (StringUtils.isBlank(authors) || null == person)
        return 0;
      authors = authors.replace(" ", "");
      authors = authors.replace(",", "");
      authors = authors.replace("-", "");
      authors = authors.replace(";", "");
      authors = authors.replace("，", "");
      authors = authors.replace("；", "");
      authors = authors.toLowerCase();
      String name = StringUtils.isNotBlank(person.getName()) ? person.getName().replace(" ", "") : "";
      String lastName = StringUtils.isNotBlank(person.getLastName()) ? person.getLastName().replace(" ", "") : "";
      String fristName = StringUtils.isNotBlank(person.getFirstName()) ? person.getFirstName().replace(" ", "") : "";
      if (StringUtils.isNotBlank(name)) {
        if (authors.indexOf(name.toLowerCase()) >= 0) {
          matchFlag = 1;
        }
      }
      if (StringUtils.isNotBlank(lastName) && StringUtils.isNotBlank(fristName)) {
        if (authors.indexOf((lastName + fristName).toLowerCase()) >= 0) {
          matchFlag = 1;
        }
      }
      if (StringUtils.isNotBlank(lastName) && StringUtils.isNotBlank(fristName)) {
        if (authors.indexOf((lastName + fristName.substring(0, 1)).toLowerCase()) >= 0) {
          matchFlag = 1;
        }
      }
    } catch (Exception e) {
      logger.error("判断成果的作者是否匹配当前登录用户出错,成果作者：{}", authors);
    }
    return matchFlag;
  }

  private void rebuildImpXmlBriefDesc(Node pitem, Node item, Element ele, Long psnId) throws SysServiceException {
    String pubTypeStr = item.valueOf("@pub_type");
    if (StringUtils.isBlank(pubTypeStr))
      return;
    int pubType = Integer.parseInt(pubTypeStr.trim());
    Map<String, String> briefMap = getRecordBriefMap(pitem, pubType, psnId); // 取得记录的source
    // 列
    if (briefMap != null) {
      ele.addAttribute("brief_desc", String.valueOf(briefMap.get("brief_desc_zh")));
      ele.addAttribute("brief_desc_en", String.valueOf(briefMap.get("brief_desc_en")));
    }
  }

  private Map<String, String> getRecordBriefMap(Node pitem, int pubType, Long psnId) throws SysServiceException {
    PubXmlProcessContext xmlContext = scholarPublicationXmlManager.buildXmlProcessContext(psnId, pubType);
    PubXmlDocument xmlDoc = scholarPublicationXmlManager.translateImportXml(xmlContext, pitem.asXML());
    Map<String, String> briefMap = scholarPublicationXmlManager.generateBriefFromImportXmlMap(xmlContext, xmlDoc,
        PublicationEnterFormEnum.SCHOLAR, pubType);
    xmlContext = null;
    xmlDoc = null;
    return briefMap;
  }

  private void rebuildImpXmlWanFangDate(Node item, Element ele) {
    // 针对万方日期处理
    String startDate = item.valueOf("@start_date").trim();
    if (StringUtils.isNotBlank(startDate)) {
      startDate = startDate.replace("年", "-").replace("月", "-").replace("日", "");
      ele.setAttributeValue("start_date", startDate);
    }
    String endDate = item.valueOf("@end_date").trim();
    if (StringUtils.isNotBlank(endDate)) {
      endDate = endDate.replace("年", "-").replace("月", "-").replace("日", "");
      ele.setAttributeValue("end_date", endDate);
    }
  }

  @Override
  public List<GroupFundInfoMembers> getGroupFundInfoMembers(Integer size, Long startGroupId, Long endGroupId) {
    List<GroupFundInfoMembers> toDoList = this.groupFundInfoMembersDao.getToHandleList(size, startGroupId, endGroupId);
    return toDoList;
  }

  @Override
  public void saveOpResult(GroupFundInfoMembers groupFundInfoMembers, Integer status) {
    groupFundInfoMembers.setStatus(status);
    this.groupFundInfoMembersDao.save(groupFundInfoMembers);
  }

  @Override
  public void handleGroupMemberInfo(GroupFundInfoMembers groupFundInfoMembers) throws Exception {
    if (groupFundInfoMembers == null || StringUtils.isEmpty(groupFundInfoMembers.getMembers())
        || groupFundInfoMembers.getGroupId() == null) {
      return;
    }

    Long groupId = groupFundInfoMembers.getGroupId();

    if (this.groupPsnRecommendDao.ifInfoExists(groupId)) {
      this.groupPsnRecommendDao.deleteInfo(groupId);
    }

    String[] members = StringUtils.split(groupFundInfoMembers.getMembers(), ",");
    for (String member : members) {
      if (StringUtils.isEmpty(member)) {
        continue;
      }
      this.matchExistingUser(member, groupFundInfoMembers.getGroupId());
    }

  }

  private void matchExistingUser(String member, Long groupId) {
    String[] strs = StringUtils.split(member, "-");
    if (strs == null || strs.length != 3) { // 不符合要求的不处理"张磊-zhanglei@siat.ac.cn-中国科学院深圳先进技术研究院"
      return;
    }
    String name = strs[0];
    String email = strs[1];
    if (StringUtils.isEmpty(name) || StringUtils.isEmpty(email)) {
      return;
    }
    name = name.toLowerCase().trim();
    email = email.toLowerCase().trim();
    List<Person> sysPsns = personDao.getPersonByNameAndEmail(name, email);
    if (CollectionUtils.isNotEmpty(sysPsns)) {
      Person psn = sysPsns.get(0);
      GroupPsnRecommend gpr = new GroupPsnRecommend();
      gpr.setGroupId(groupId);
      gpr.setRecommendType("5");
      gpr.setScore(100.00);
      gpr.setTempPsnId(psn.getPersonId());
      gpr.setTempPsnHeadUrl(psn.getAvatars());
      gpr.setTempPsnName(psn.getName());
      gpr.setTempPsnTitle(psn.getTitolo());
      gpr.setTempPsnLastName(psn.getLastName());
      gpr.setTempPsnFirstName(psn.getFirstName());
      this.groupPsnRecommendDao.save(gpr);
    }

  }

  public String importGroupPubNew(Long groupId, Long psnId, Long pdwhPubId, Integer dbId) {
    PubPdwhDetailDOM pdwhDetail = pubPdwhDetailDao.findById(pdwhPubId);
    if (pdwhDetail == null) {
      return null;
    }
    // 构建成果保存对象
    Map<String, Object> pub = buildPub(pdwhDetail, psnId, pdwhDetail.getPubType(), groupId, null);
    // 调用保存个人成果接口
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
    HttpEntity<String> entity = new HttpEntity<String>(JacksonUtils.mapToJsonStr(pub), headers);
    String saveUrl = snsDomain + V8pubQueryPubConst.PUBHANDLER_URL;
    String result = restTemplate.postForObject(saveUrl, entity, String.class);
    return result;
  }

  private Map<String, Object> buildPub(PubPdwhDetailDOM pdwhDetail, Long psnId, Integer pubType, Long groupId,
      Set<PubSituationBean> situationList) {
    Map<String, Object> map = new HashMap<>();
    Long pdwhPubId = pdwhDetail.getPubId();
    // 设置groupId标识为群组成果
    map.put("des3GroupId", Des3Utils.encodeToDes3(groupId + ""));
    map.put("pubHandlerName", "saveSnsPubHandler");
    map.put("des3PdwhPubId", Des3Utils.encodeToDes3(pdwhPubId + ""));
    map.put("des3PsnId", Des3Utils.encodeToDes3(String.valueOf(psnId)));
    map.put("pubGenre", PubGenreConstants.PDWH_SNS_PUB);
    map.put("title", pdwhDetail.getTitle());
    map.put("publishDate", pdwhDetail.getPublishDate());
    map.put("countryId", pdwhDetail.getCountryId());
    map.put("fundInfo", pdwhDetail.getFundInfo());
    map.put("citations", pdwhDetail.getCitations());
    map.put("doi", pdwhDetail.getDoi());
    map.put("summary", pdwhDetail.getSummary());
    map.put("keywords", pdwhDetail.getKeywords());
    map.put("srcFulltextUrl", pdwhDetail.getSrcFulltextUrl());
    map.put("pubType", pubType == null ? pdwhDetail.getPubType() : pubType);
    map.put("recordFrom", PubSnsRecordFromEnum.IMPORT_FROM_PDWH);
    map.put("organization", pdwhDetail.getOrganization());
    map.put("sourceUrl", pdwhDetail.getSourceUrl());
    map.put("citedUrl", pdwhDetail.getCitedUrl());
    map.put("permission", 7);
    map.put("sourceId", pdwhDetail.getSourceId());
    map.put("srcDbId", pdwhDetail.getSrcDbId());
    map.put("dbId", pdwhDetail.getSrcDbId());
    map.put("fullText", constructFullText(pdwhPubId));
    map.put("pubTypeInfo", pdwhDetail.getTypeInfo());
    /**
     * SCM-20268，SCM-20452 基准库导入至个人库，个人库中members由基准库的authorNames进行拆分构造
     * 逻辑迁移至成果保存中，ASDisposePubAuthorMatchImpl 这里不传members，只传authorNames就可以
     */
    map.put("authorNames", pdwhDetail.getAuthorNames());
    map.put("situations", situationList == null ? pdwhDetail.getSituations() : situationList);
    return map;
  }

  private PubFulltextDTO constructFullText(Long pdwhPubId) {
    PdwhPubFullTextPO p = pdwhPubFullTextDAO.getByPubId(pdwhPubId);
    if (p != null) {
      PubFulltextDTO pubFulltext = new PubFulltextDTO();
      pubFulltext.setDes3fileId(Des3Utils.encodeToDes3(String.valueOf(p.getFileId())));
      pubFulltext.setFileName(p.getFileName());
      pubFulltext.setPermission(p.getPermission());
      return pubFulltext;
    }
    return null;
  }

  @Override
  public void insertIntoRcmdPdwh(Long groupId, Long pdwhId, Integer publishYear) throws Exception {
    GrpPubRcmd gpr = grpPubRcmdDao.getGrpPubRcmdWithoutStatus(pdwhId, groupId);
    Date date = new Date();
    if (gpr == null) {
      gpr = new GrpPubRcmd();
      gpr.setGrpId(groupId);
      gpr.setPubId(pdwhId);
      gpr.setCreateDate(date);
    }
    gpr.setRcmdType(1);
    gpr.setStatus(0);
    gpr.setUpdateDate(date);
    gpr.setPublishYear(publishYear);
    gpr.setUpdatePsnId(99L);
    this.grpPubRcmdDao.save(gpr);
  }
}
