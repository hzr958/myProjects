package com.smate.sie.center.task.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.transaction.Transactional;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.smate.center.task.v8pub.dao.pdwh.PdwhMemberInsNameDAO;
import com.smate.center.task.v8pub.dao.pdwh.PdwhPubMemberDAO;
import com.smate.center.task.v8pub.dao.sns.PubPdwhDetailDAO;
import com.smate.center.task.v8pub.pdwh.po.PdwhMemberInsNamePO;
import com.smate.center.task.v8pub.pdwh.po.PdwhPubMemberPO;
import com.smate.center.task.v8pub.pdwh.po.PubPdwhPO;
import com.smate.core.base.utils.constant.SieConstPatType;
import com.smate.core.base.utils.dao.consts.Sie6ConstDictionaryDao;
import com.smate.core.base.utils.dao.consts.Sie6InstitutionDao;
import com.smate.core.base.utils.dao.consts.SieConstPatTypeDao;
import com.smate.core.base.utils.exception.SysServiceException;
import com.smate.core.base.utils.model.consts.Sie6Institution;
import com.smate.core.base.utils.model.consts.SieConstRegion;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.core.base.utils.service.consts.SieConstRegionService;
import com.smate.core.base.utils.string.IrisNumberUtils;
import com.smate.core.base.utils.string.StringUtils;
import com.smate.sie.center.task.dao.ImportPdwhPubDao;
import com.smate.sie.center.task.dao.SieDiffImportPdwhPubDao;
import com.smate.sie.center.task.dao.SiePubPdwhPODao;
import com.smate.sie.center.task.model.ImportPdwhPub;
import com.smate.sie.center.task.model.SieDiffImportPdwhPub;
import com.smate.sie.center.task.pdwh.service.PublicationXmlManager;
import com.smate.sie.center.task.pdwh.service.PublicationXmlManagerImpl;
import com.smate.sie.center.task.pdwh.utils.PubJsonTrimUtils;
import com.smate.sie.center.task.pdwh.utils.XmlUtil;
import com.smate.sie.core.base.utils.date.DateUtils;
import com.smate.sie.core.base.utils.pub.dom.PatAppliersBean;
import com.smate.sie.core.base.utils.pub.dom.SiePubSituationBean;
import com.smate.sie.core.base.utils.pub.dto.AwardsInfoDTO;
import com.smate.sie.core.base.utils.pub.dto.BookInfoDTO;
import com.smate.sie.core.base.utils.pub.dto.ConferencePaperDTO;
import com.smate.sie.core.base.utils.pub.dto.JournalInfoDTO;
import com.smate.sie.core.base.utils.pub.dto.MemberInsDTO;
import com.smate.sie.core.base.utils.pub.dto.PatentInfoDTO;
import com.smate.sie.core.base.utils.pub.dto.PubAttachmentsDTO;
import com.smate.sie.core.base.utils.pub.dto.PubMemberDTO;
import com.smate.sie.core.base.utils.pub.dto.ThesisInfoDTO;
import com.smate.sie.core.base.utils.pub.service.PubJsonDTO;
import com.smate.web.v8pub.dom.AwardsInfoBean;
import com.smate.web.v8pub.dom.BookInfoBean;
import com.smate.web.v8pub.dom.ConferencePaperBean;
import com.smate.web.v8pub.dom.JournalInfoBean;
import com.smate.web.v8pub.dom.PatentInfoBean;
import com.smate.web.v8pub.dom.PubSituationBean;
import com.smate.web.v8pub.dom.ThesisInfoBean;
import com.smate.web.v8pub.dom.pdwh.PubPdwhDetailDOM;

/**
 * 基准库指派任务服务
 * 
 * @author sjzhou
 * @param <K>
 * @param <K>
 * @param <V>
 *
 */

@Service("importPublicationsService")
@Transactional(rollbackOn = Exception.class)
public class ImportPublicationsServiceImpl<K, V> implements ImportPublicationsService {
  Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private PublicationXmlManager publicationXmlManager;
  @Autowired
  private SiePubPdwhPODao siePubPdwhPODao;
  @Autowired
  private ImportPdwhPubDao importPdwhPubDao;
  @Autowired
  private PubPdwhDetailDAO pubPdwhDetailDAO;
  @Autowired
  private Sie6ConstDictionaryDao sie6ConstDictionaryDao;
  @Autowired
  private SieConstRegionService sieConstRegionService;
  @Autowired
  private PdwhPubMemberDAO pdwhPubMemberDAO;
  @Autowired
  private PdwhMemberInsNameDAO pdwhMemberInsNameDAO;
  @Autowired
  private Sie6InstitutionDao sie6InstitutionDao;
  @Autowired
  private SieConstPatTypeDao sieConstPatTypeDao;
  @Autowired
  private SieDiffImportPdwhPubDao sieDiffImportPdwhPubDao;

  @Override
  public List<PubPdwhPO> getPwdhPubList(Integer size, Long insId) throws SysServiceException {
    List<PubPdwhPO> pdwhPubList = null;
    /**
     * 考虑到人为操作需重新到库里面查询最新的对象；若状态值为0直接跳出，同步下一个单位的数据。
     */
    ImportPdwhPub importPdwhPub = importPdwhPubDao.get(insId);
    if (importPdwhPub.getStatus() == 0) {
      return pdwhPubList;
    }
    Date updateTime = importPdwhPub.getUpdateTime();// 获取上次最后一条记录的更新时间，下次只导入这条记录之后的数据
    pdwhPubList = siePubPdwhPODao.getPdwhPublicationslist(size, updateTime, importPdwhPub.getInsId());
    return pdwhPubList;
  }

  @Override
  public void saveImportPdwhPub(List<PubPdwhPO> pdwhPubList, Long insId, Integer status) throws SysServiceException {
    ImportPdwhPub importPdwhPub = importPdwhPubDao.get(insId);
    if (CollectionUtils.isNotEmpty(pdwhPubList)) {
      PubPdwhPO pdwhPublication = pdwhPubList.get(pdwhPubList.size() - 1);
      Date updateDate = pdwhPublication.getGmtModified();
      importPdwhPub.setUpdateTime(updateDate);
    }
    if (status != null) {
      importPdwhPub.setStatus(status);
    }
    importPdwhPub.setInsId(insId);
    importPdwhPubDao.save(importPdwhPub);// 保存该次导入的最后一条记录的更新时间到表中
  }

  /**
   * 导入基准库的成果信息到sie相关的业务表中
   * 
   * @param pdwhPublications
   */
  @Override
  public Map<String, Object> importSiePublication(PubPdwhPO pdwhPublications, Long insId) throws SysServiceException {
    Map<String, Object> resultMap = new HashMap<String, Object>();
    Long pdwhPubid = pdwhPublications.getPubId();
    if (pdwhPubid == null) {
      return resultMap;
    }
    // 获取基准库json的DOM对象
    PubPdwhDetailDOM pubPdwhDetailDOM = pubPdwhDetailDAO.findByPubId(pdwhPubid);
    if (pubPdwhDetailDOM != null) {
      // 转换为PubJsonDTO 方便后续的构造参数
      PubJsonDTO pubJson = buildPubJsonDTO(pubPdwhDetailDOM, insId);
      // 利用反射去掉字段值左右两边空格
      PubJsonTrimUtils.trimAttributeValue(pubJson);
      if (pubJson != null) {
        try {
          resultMap = this.publicationXmlManager.backgroundImportPdwhPubJson(pubJson, pdwhPublications);
        } catch (Exception e) {
          StackTraceElement stackTraceElement = e.getStackTrace()[0];
          int lineNum = stackTraceElement.getLineNumber();
          String errMsg = "异常类文件名： " + stackTraceElement.getFileName() + "异常方法名 ： " + stackTraceElement.getMethodName()
              + "，错误行号 ： " + lineNum;
          logger.error("基准库指派成果保存报错, 基准库成果pubId=" + pdwhPublications.getPubId(), e);
          throw new SysServiceException(e.getMessage() + " --> " + errMsg);
        }
      }
    }
    return resultMap;
  }

  @SuppressWarnings("deprecation")
  private PubJsonDTO buildPubJsonDTO(PubPdwhDetailDOM pubPdwhDetailDOM, Long insId) throws SysServiceException {
    PubJsonDTO sieJson = new PubJsonDTO();
    String authorNames = XmlUtil.formateAuthorNames(pubPdwhDetailDOM.getAuthorNames());
    authorNames = authorNames.replaceAll("; ", ";");
    authorNames = authorNames.replaceAll(" ;", ";");
    if (authorNames.endsWith(";")) {
      authorNames = authorNames.substring(0, authorNames.length() - 1);
    }
    // 若超过20个作者，则只取前20个作者姓名
    authorNames = limitAuthorsName(authorNames);
    authorNames = StringEscapeUtils.escapeHtml4(authorNames);
    if (authorNames.length() > 2500) {
      authorNames = authorNames.substring(0, 2500);// ROL-6332
    }
    sieJson.authorNames = authorNames;
    sieJson.briefDesc = "";
    Integer citations = pubPdwhDetailDOM.getCitations();
    if (citations == null || citations == 0) {
      sieJson.citations = null;
    } else {
      sieJson.citations = citations;
    }
    if (citations != null && citations > 0) {
      sieJson.citationsUpdateTime = DateUtils.dateToStr2(new Date());
    }
    sieJson.citedUrl = pubPdwhDetailDOM.getCitedUrl();
    sieJson.dataFrom = 4;
    sieJson.des3fulltextId = "";
    sieJson.des3PsnId = "";
    sieJson.des3PubId = "";
    sieJson.disciplineCode = "";
    sieJson.disciplineName = "";
    sieJson.doi = pubPdwhDetailDOM.getDoi();
    sieJson.fulltextId = pubPdwhDetailDOM.getFulltextId();
    sieJson.fundInfo = pubPdwhDetailDOM.getFundInfo();
    sieJson.HCP = pubPdwhDetailDOM.isHCP() ? 1 : 0;
    sieJson.HP = pubPdwhDetailDOM.isHP() ? 1 : 0;
    sieJson.insId = insId;
    sieJson.isEdit = false;
    sieJson.isImport = false;
    sieJson.isPublicCode = 1;
    sieJson.isPublicName = "公开";
    sieJson.keywords = pubPdwhDetailDOM.getKeywords();
    sieJson.OA = pubPdwhDetailDOM.getOA();
    sieJson.organization = pubPdwhDetailDOM.getOrganization();
    sieJson.publishDate = pubPdwhDetailDOM.getPublishDate();
    sieJson.pubTypeCode = pubPdwhDetailDOM.getPubType();
    buildTypeInfo(sieJson, pubPdwhDetailDOM);
    buildMembers(sieJson, pubPdwhDetailDOM);
    buildSitutions(sieJson, pubPdwhDetailDOM);
    buildPubAttachments(sieJson, pubPdwhDetailDOM);
    sieJson.summary = pubPdwhDetailDOM.getSummary();
    sieJson.title = pubPdwhDetailDOM.getTitle();
    sieJson.srcDbId = pubPdwhDetailDOM.getSrcDbId();
    sieJson.isImport = false;
    sieJson.sourceId = pubPdwhDetailDOM.getSourceId();
    sieJson.sourceUrl = pubPdwhDetailDOM.getSourceUrl();
    sieJson.srcFulltextUrl = pubPdwhDetailDOM.getSrcFulltextUrl();
    return sieJson;
  }

  private String limitAuthorsName(String authorNames) {
    List<String> list = new ArrayList<String>();
    String[] namesArray = StringUtils.split(authorNames, ";");
    if (namesArray.length > 20) {
      for (int i = 0; i < 20; i++) {
        list.add(namesArray[i].trim());
      }
      return StringUtils.join(list.toArray(), "; ");
    } else {
      return authorNames;
    }
  }

  @SuppressWarnings("deprecation")
  private void buildTypeInfo(PubJsonDTO sieJson, PubPdwhDetailDOM pubPdwhDetailDOM) throws SysServiceException {
    try {
      Integer pubType = pubPdwhDetailDOM.getPubType();
      switch (pubType) {
        case 1:
          AwardsInfoDTO sie = new AwardsInfoDTO();
          AwardsInfoBean pdwh = (AwardsInfoBean) pubPdwhDetailDOM.getTypeInfo();
          // 利用反射去掉字段值左右两边空格
          // PubJsonTrimUtils.trimAttributeValue(pdwh);
          sie.setAwardDate(pdwh.getAwardDate());
          if (StringUtils.isNotBlank(pdwh.getCategory())) {
            String categoryCode =
                sie6ConstDictionaryDao.findCodeByCategoryAndName("award_category", pdwh.getCategory());
            sie.setCategoryCode(categoryCode);
            sie.setCategoryName(pdwh.getCategory());
          }
          sie.setCertificateNo(pdwh.getCertificateNo());
          if (StringUtils.isNotBlank(pdwh.getGrade())) {
            String gradeCode = sie6ConstDictionaryDao.findCodeByCategoryAndName("award_grade", pdwh.getGrade());
            sie.setGradeCode(gradeCode);
            sie.setGradeName(pdwh.getGrade());
          }
          sie.setIssuingAuthority(pdwh.getIssuingAuthority());
          // 利用反射去掉字段值左右两边空格
          // PubJsonTrimUtils.trimAttributeValue(sie);
          sieJson.pubTypeInfo = JSONObject.parseObject(JSONObject.toJSON(sie).toString());
          break;
        case 2:
        case 10:
          BookInfoDTO bookInfo = new BookInfoDTO();
          BookInfoBean pdwhBook = (BookInfoBean) pubPdwhDetailDOM.getTypeInfo();
          // 利用反射去掉字段值左右两边空格
          // PubJsonTrimUtils.trimAttributeValue(pdwhBook);
          bookInfo.setName(pdwhBook.getName());// 书名
          bookInfo.setISBN(pdwhBook.getISBN());// 国际标准图书编号
          bookInfo.setPublisher(pdwhBook.getPublisher());// 出版社
          String bookType = pdwhBook.getType().name();
          if ("MONOGRAPH".equals(bookType)) {
            bookInfo.setTypeCode("11");
            bookInfo.setTypeName("专著");
          } else if ("TEXTBOOK".equals(bookType)) {
            bookInfo.setTypeCode("13");
            bookInfo.setTypeName("教科书");
          } else if ("COMPILE".equals(bookType)) {
            bookInfo.setTypeCode("14");
            bookInfo.setTypeName("编著");
          } else {
            bookInfo.setTypeCode("");
            bookInfo.setTypeName("");
          }
          bookInfo.setTotalWords(pdwhBook.getTotalWords());// 总字数
          if ("中文".equals(pdwhBook.getLanguage())) {
            bookInfo.setLanguageCode("1");
            bookInfo.setLanguageName("中文");
          } else if (StringUtils.isNotBlank(pdwhBook.getLanguage()) && !"中文".equals(pdwhBook.getLanguage())) {
            bookInfo.setLanguageCode("2");
            bookInfo.setLanguageName("外文");
          } else {
            bookInfo.setLanguageCode("");
            bookInfo.setLanguageName("");
          }
          if (StringUtils.isNotBlank(pubPdwhDetailDOM.getPublishDate())) {
            int flag = DateUtils.compare(DateUtils.parseStringToDate(pubPdwhDetailDOM.getPublishDate()), new Date());// 时间大于当前时间时为已出版否则待出版
            if (flag < 0) {
              bookInfo.setPublishStatusName("已出版");
              bookInfo.setPublishStatusCode("1");
            } else {
              bookInfo.setPublishStatusName("待出版");
              bookInfo.setPublishStatusCode("0");
            }
          }

          String val = pdwhBook.getPageNumber();
          if (StringUtils.isNotBlank(val) && val.contains("-")) {
            String[] page = val.split("-");
            bookInfo.setStartPage(page[0]);
            bookInfo.setEndPage(page[1]);
          } else {// ROL-5417
            bookInfo.setArticleNo(val);
          }
          // 利用反射去掉字段值左右两边空格
          // PubJsonTrimUtils.trimAttributeValue(bookInfo);
          sieJson.pubTypeInfo = JSONObject.parseObject(JSONObject.toJSON(bookInfo).toString());
          break;
        case 3:
          ConferencePaperDTO conferencePaper = new ConferencePaperDTO();
          ConferencePaperBean pdwhConf = (ConferencePaperBean) pubPdwhDetailDOM.getTypeInfo();
          // 利用反射去掉字段值左右两边空格
          // PubJsonTrimUtils.trimAttributeValue(pdwhConf);
          conferencePaper.setName(pdwhConf.getName());// 会议名称
          conferencePaper.setOrganizer(pdwhConf.getOrganizer());// 会议组织者
          conferencePaper.setStartDate(changeStringToDate(pdwhConf.getStartDate()));// 开始日期
          conferencePaper.setEndDate(changeStringToDate(pdwhConf.getEndDate()));// 结束日期

          conferencePaper.setCountry(getCountryById(pubPdwhDetailDOM.getCountryId()));
          conferencePaper.setCity("");
          // 利用反射去掉字段值左右两边空格
          // PubJsonTrimUtils.trimAttributeValue(conferencePaper);
          sieJson.pubTypeInfo = JSONObject.parseObject(JSONObject.toJSON(conferencePaper).toString());
          break;
        case 4:
          JournalInfoDTO journalInfo = new JournalInfoDTO();
          JournalInfoBean pdwhJorrnal = (JournalInfoBean) pubPdwhDetailDOM.getTypeInfo();
          // 利用反射去掉字段值左右两边空格
          // PubJsonTrimUtils.trimAttributeValue(pdwhJorrnal);
          journalInfo.setJid(null);// 期刊id
          journalInfo.setName(pdwhJorrnal.getName());// 期刊名称
          journalInfo.setVolumeNo(pdwhJorrnal.getVolumeNo());// 卷号
          journalInfo.setIssue(pdwhJorrnal.getIssue());// 期号
          journalInfo.setISSN(pdwhJorrnal.getISSN());
          String val4 = pdwhJorrnal.getPageNumber();
          if (StringUtils.isNotBlank(val4) && val4.contains("-")) {
            String[] page = val4.split("-");
            journalInfo.setStartPage(page[0]);
            journalInfo.setEndPage(page[1]);
          } else {// ROL-5417
            journalInfo.setArticleNo(val4);
          }
          String publishStatus = pdwhJorrnal.getPublishStatus();
          if ("P".equals(publishStatus)) {
            journalInfo.setPublishStatusCode("P");
            journalInfo.setPublishStatusName("已发表");//
          } else if ("A".equals(publishStatus)) {
            journalInfo.setPublishStatusCode("A");
            journalInfo.setPublishStatusName("已接收");//
          }
          // 利用反射去掉字段值左右两边空格
          // PubJsonTrimUtils.trimAttributeValue(journalInfo);
          sieJson.pubTypeInfo = JSONObject.parseObject(JSONObject.toJSON(journalInfo).toString());
          break;
        case 5:
          PatentInfoDTO pat = new PatentInfoDTO();
          PatentInfoBean patBean = (PatentInfoBean) pubPdwhDetailDOM.getTypeInfo();
          // 利用反射去掉字段值左右两边空格
          // PubJsonTrimUtils.trimAttributeValue(patBean);
          pat.setApplicationDate(patBean.getApplicationDate());
          pat.setApplicationNo(patBean.getApplicationNo());
          pat.setPublicationOpenNo(patBean.getPublicationOpenNo());
          buildAppliers(pat, patBean);//
          pat.setAuthDate(patBean.getStartDate());// 有效期开始时间作为sie这边的授权时间。
          pat.setAuthNo(null);
          pat.setCeritficateNo(null);
          pat.setCPC(patBean.getCPC());
          pat.setIPC(patBean.getIPC());
          pat.setIssuingAuthority(patBean.getIssuingAuthority());
          Integer status = patBean.getStatus();
          if (status == 0) {
            pat.setPatentStatusCode("0");
            pat.setPatentStatusName("申请");
          } else if (status == 1) {
            pat.setPatentStatusCode("1");
            pat.setPatentStatusName("授权");
          }
          String patentType = patBean.getType();
          if (StringUtils.isNotBlank(patentType) && NumberUtils.isNumber(patentType)) {
            SieConstPatType sieConstPatType = sieConstPatTypeDao.get(IrisNumberUtils.createInteger(patentType));
            pat.setTypeCode(patentType);
            pat.setTypeName(sieConstPatType != null ? sieConstPatType.getName() : "");
          }
          // 利用反射去掉字段值左右两边空格
          // PubJsonTrimUtils.trimAttributeValue(pat);
          sieJson.pubTypeInfo = JSONObject.parseObject(JSONObject.toJSON(pat).toString());
          break;
        case 8:
          ThesisInfoDTO thesisInfo = new ThesisInfoDTO();
          ThesisInfoBean pdwhThesis = (ThesisInfoBean) pubPdwhDetailDOM.getTypeInfo();
          // 利用反射去掉字段值左右两边空格
          // PubJsonTrimUtils.trimAttributeValue(pdwhThesis);
          String degree = pdwhThesis.getDegree().name();
          if ("MASTER".equals(degree)) {
            degree = "硕士";
          } else if ("DOCTOR".equals(degree)) {
            degree = "博士";
          } else if ("OTHER".equals(degree)) {
            degree = "其他";
          } else {
            degree = "其他";
          }
          String degreeCode = sie6ConstDictionaryDao.findCodeByCategoryAndName("pub_thesis_programme", degree);
          thesisInfo.setDegreeCode(NumberUtils.toInt(degreeCode));
          thesisInfo.setDegreeName(degree);// 学位
          thesisInfo.setDefenseDate(changeStringToDate(pdwhThesis.getDefenseDate()));// 答辩日期
          thesisInfo.setIssuingAuthority(pdwhThesis.getIssuingAuthority());// 颁发单位
          thesisInfo.setDepartment(pdwhThesis.getDepartment());// 部门
          // 利用反射去掉字段值左右两边空格
          // PubJsonTrimUtils.trimAttributeValue(thesisInfo);
          sieJson.pubTypeInfo = JSONObject.parseObject(JSONObject.toJSON(thesisInfo).toString());
          break;
        default:
          break;
      }
    } catch (Exception e) {
      StackTraceElement stackTraceElement = e.getStackTrace()[0];
      int lineNum = stackTraceElement.getLineNumber();
      String errMsg = "异常类文件名： " + stackTraceElement.getFileName() + "异常方法名 ： " + stackTraceElement.getMethodName()
          + "，错误行号 ： " + lineNum;
      throw new SysServiceException(e.getMessage() + " --> " + errMsg);
    }
  }

  private void buildPubAttachments(PubJsonDTO sieJson, PubPdwhDetailDOM pubPdwhDetailDOM) {
    List<PubAttachmentsDTO> attachmentsList = new ArrayList<PubAttachmentsDTO>();
    sieJson.pubAttachments = JSONArray.parseArray(JSONObject.toJSON(attachmentsList).toString());
  }

  private void buildMembers(PubJsonDTO sieJson, PubPdwhDetailDOM pubPdwhDetailDOM) {
    List<PubMemberDTO> memberList = new ArrayList<PubMemberDTO>();
    // 直接从pdwh作者相关表取数据回来构建member节点 无需根据authorNames节点的值跟members集合去匹配了。
    List<PdwhPubMemberPO> members = pdwhPubMemberDAO.findByPubId(pubPdwhDetailDOM.getPubId());
    members = members.size() > 20 ? members.subList(0, 20) : members;
    for (PdwhPubMemberPO pdwhPubMemberPO : members) {
      PubMemberDTO sieMember = new PubMemberDTO();
      sieMember.setSeqNo(pdwhPubMemberPO.getSeqNo());
      sieMember.setPsnId(pdwhPubMemberPO.getPsnId());
      sieMember.setPmId(null);
      sieMember.setName(pdwhPubMemberPO.getName());
      if (pubPdwhDetailDOM.getPubType() != 5) {
        List<PdwhMemberInsNamePO> memberInsList = pdwhMemberInsNameDAO.findListByMemberId(pdwhPubMemberPO.getId());
        if (CollectionUtils.isNotEmpty(memberInsList)) {
          MemberInsDTO insDTO = null;
          PdwhMemberInsNamePO insPo = null;
          List<MemberInsDTO> memInsList = new ArrayList<>();
          for (int i = 0; i < memberInsList.size(); i++) {
            insDTO = new MemberInsDTO();
            insPo = new PdwhMemberInsNamePO();
            insPo = memberInsList.get(i);
            insDTO.setInsId(insPo.getInsId());
            insDTO.setInsName(insPo.getDept()); // 取pdwh的dept的值回来存到sie的ins_name中
            memInsList.add(insDTO);
          }
          sieMember.setInsNames(memInsList);
        } else {
          sieMember.setInsNames(new ArrayList<MemberInsDTO>());
        }
      } else { // 专利不做相关的insName的节点改动，还是原来的业务逻辑
        PdwhMemberInsNamePO memberIns = pdwhMemberInsNameDAO.findMemberByMemberId(pdwhPubMemberPO.getId());
        if (memberIns != null) {
          sieMember.setInsName(memberIns.getDept());// 取pdwh的dept的值回来存到sie的ins_name中
          sieMember.setInsId(memberIns.getInsId());
        } else {
          sieMember.setInsName(null);// 取pdwh的dept的值回来存到sie的ins_name中
          sieMember.setInsId(null);
        }
        sieMember.setInsNames(new ArrayList<MemberInsDTO>());
      }
      sieMember.setFirstAuthor(pdwhPubMemberPO.getFirstAuthor() == 1 ? true : false);
      sieMember.setEmail(pdwhPubMemberPO.getEmail());
      sieMember.setCommunicable(pdwhPubMemberPO.getCommunicable() == 1 ? true : false);
      // 利用反射去掉字段值左右两边空格
      // PubJsonTrimUtils.trimAttributeValue(sieMember);
      memberList.add(sieMember);
    }
    sieJson.memberList = memberList;
    sieJson.members = JSONArray.parseArray(JSONObject.toJSON(memberList).toString());
  }

  @SuppressWarnings("unchecked")
  private void buildSitutions(PubJsonDTO sieJson, PubPdwhDetailDOM pubPdwhDetailDOM) {
    Set<SiePubSituationBean> situationSet = new HashSet<SiePubSituationBean>();
    if (pubPdwhDetailDOM.getSituations() != null) {
      Set<PubSituationBean> sitList = pubPdwhDetailDOM.getSituations();
      if (sitList != null && sitList.size() > 0) {
        SiePubSituationBean siePubSituationBean = null;
        for (PubSituationBean p : sitList) {
          String libraryName = p.getLibraryName();
          if (StringUtils.isBlank(libraryName)) {
            continue;
          }
          siePubSituationBean = new SiePubSituationBean();
          siePubSituationBean.setSrcId(p.getSrcId());
          siePubSituationBean.setSrcDbId(p.getSrcDbId());
          siePubSituationBean.setSitStatus(p.isSitStatus());
          siePubSituationBean.setLibraryName(libraryName);
          siePubSituationBean.setSrcUrl(p.getSrcUrl());
          siePubSituationBean.setSitOriginStatus(p.isSitStatus() == true ? true : false);
          situationSet.add(siePubSituationBean);
        }
      }
    }
    sieJson.situations = JSONArray.parseArray(JSONObject.toJSON(situationSet).toString());
  }

  private void buildAppliers(PatentInfoDTO pat, PatentInfoBean patBean) {
    List<PatAppliersBean> appliers = new ArrayList<PatAppliersBean>();
    String patentee = patBean.getPatentee();
    String[] patentees = patentee.split(";");
    Long i = 1L;
    for (String ee : patentees) {
      if (StringUtils.isNotBlank(ee)) {
        Sie6Institution ins = sie6InstitutionDao.findInstitutionByInsName(ee);
        PatAppliersBean app = new PatAppliersBean();
        app.setApplierId(ins != null ? ins.getId() : null);
        app.setApplierName(ee);
        app.setSeqNo(i);
        appliers.add(app);
      }
      pat.setAppliers(appliers);
    }
    i++;
  }

  private String changeStringToDate(String dateStr) {
    if (StringUtils.isEmpty(dateStr)) {
      return "";
    }
    dateStr = dateStr.replace("/", "-");
    dateStr = dateStr.replace(".", "-");
    return dateStr;
  }

  private String getCountryById(Long countryId) throws SysServiceException {
    if (NumberUtils.isNotNullOrZero(countryId)) {
      SieConstRegion region = null;
      try {
        region = sieConstRegionService.getRegionById(countryId);
      } catch (Exception e) {
        logger.error("读取国家/地区错误", e);
        throw new SysServiceException("匹配国家/地区出错");
      }
      if (region != null) {
        return region.getName();
      } else {
        return "";
      }
    }
    return "";
  }

  @Override
  public void stPdwhData(List<Map<String, Object>> resultList, Long insId) {
    long patNewNum = 0;
    long patUpdateNum = 0;
    long pubNewNum = 0;
    long pubUpdateNum = 0;
    for (Map<String, Object> tempMap : resultList) {
      for (Map.Entry<String, Object> enter : tempMap.entrySet()) {
        String tempName = enter.getKey();
        switch (tempName) {
          case PublicationXmlManagerImpl.PatNewName:
            patNewNum = stNum(enter, patNewNum);
            break;
          case PublicationXmlManagerImpl.PatUpdateName:
            patUpdateNum = stNum(enter, patUpdateNum);
            break;
          case PublicationXmlManagerImpl.PubNewName:
            pubNewNum = stNum(enter, pubNewNum);
            break;
          case PublicationXmlManagerImpl.PubUpdateName:
            pubUpdateNum = stNum(enter, pubUpdateNum);
            break;
        }
      }
    }
    // 满足至少有一条更新记录的时候才记录
    if (patNewNum > 0 || patUpdateNum > 0 || pubNewNum > 0 || pubUpdateNum > 0) {
      SieDiffImportPdwhPub sieDiffImportPdwhPub = new SieDiffImportPdwhPub();
      sieDiffImportPdwhPub.setPatInsertCount(patNewNum);
      sieDiffImportPdwhPub.setPatUpdateCount(patUpdateNum);
      sieDiffImportPdwhPub.setPubInsertCount(pubNewNum);
      sieDiffImportPdwhPub.setPubUpdateCount(pubUpdateNum);
      sieDiffImportPdwhPub.setInsId(insId);
      Sie6Institution institution = sie6InstitutionDao.get(insId);
      if (institution != null) {
        sieDiffImportPdwhPub.setZhName(institution.getInsName());
      }
      sieDiffImportPdwhPub.setCreateDate(new Date());
      sieDiffImportPdwhPubDao.save(sieDiffImportPdwhPub);
    }
  }

  /**
   * 统计值为真，则该类型的值自加一
   */
  private long stNum(Entry<String, Object> enter, long patNewNum) {
    Object obj = enter.getValue();
    if (obj instanceof Boolean) {
      if ((Boolean) obj == true) {
        patNewNum++;
      }
    }
    return patNewNum;
  }
}
