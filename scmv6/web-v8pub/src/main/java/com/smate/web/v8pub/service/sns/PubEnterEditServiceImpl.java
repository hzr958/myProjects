package com.smate.web.v8pub.service.sns;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.HtmlUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.smate.core.base.consts.dao.InstitutionDao;
import com.smate.core.base.consts.model.Institution;
import com.smate.core.base.file.dao.ArchiveFileDao;
import com.smate.core.base.file.enums.FileTypeEnum;
import com.smate.core.base.file.model.ArchiveFile;
import com.smate.core.base.file.service.FileDownloadUrlService;
import com.smate.core.base.pub.consts.RestTemplateUtils;
import com.smate.core.base.pub.consts.V8pubQueryPubConst;
import com.smate.core.base.pub.enums.PubLibraryEnum;
import com.smate.core.base.pub.util.PDFDataUtils;
import com.smate.core.base.pub.vo.Accessory;
import com.smate.core.base.pub.vo.PubDetailVO;
import com.smate.core.base.utils.cache.CacheService;
import com.smate.core.base.utils.common.LocaleTextUtils;
import com.smate.core.base.utils.dao.security.PersonDao;
import com.smate.core.base.utils.file.ArchiveFileUtil;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.model.security.Person;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.core.base.utils.pubHash.PubHashUtils;
import com.smate.core.base.utils.pubxml.ImportPubXmlUtils;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.service.psn.PsnSolrInfoModifyService;
import com.smate.core.base.v8pub.restTemp.service.PubRestemplateService;
import com.smate.web.v8pub.consts.V8pubConst;
import com.smate.web.v8pub.dao.pdwh.CategoryHightechIndustryDAO;
import com.smate.web.v8pub.dao.sns.CategoryMapBaseDao;
import com.smate.web.v8pub.dto.AwardsInfoDTO;
import com.smate.web.v8pub.dto.BookInfoDTO;
import com.smate.web.v8pub.dto.ConferencePaperDTO;
import com.smate.web.v8pub.dto.IndustryDTO;
import com.smate.web.v8pub.dto.JournalInfoDTO;
import com.smate.web.v8pub.dto.MemberInsDTO;
import com.smate.web.v8pub.dto.OtherInfoDTO;
import com.smate.web.v8pub.dto.PatentInfoDTO;
import com.smate.web.v8pub.dto.PubFulltextDTO;
import com.smate.web.v8pub.dto.PubMemberDTO;
import com.smate.web.v8pub.dto.PubSituationDTO;
import com.smate.web.v8pub.dto.ScienceAreaDTO;
import com.smate.web.v8pub.dto.SoftwareCopyrightDTO;
import com.smate.web.v8pub.dto.StandardInfoDTO;
import com.smate.web.v8pub.dto.ThesisInfoDTO;
import com.smate.web.v8pub.exception.ServiceException;
import com.smate.web.v8pub.po.pdwh.CategoryHightechIndustry;
import com.smate.web.v8pub.po.sns.CategoryMapBase;
import com.smate.web.v8pub.po.sns.PubSnsPO;
import com.smate.web.v8pub.service.pdwh.PdwhPubDuplicateService;

@Service
@Transactional(rollbackFor = Exception.class)
public class PubEnterEditServiceImpl implements PubEnterEditService {

  private Logger logger = LoggerFactory.getLogger(getClass());

  public final static String DETAIL_URL = "/data/pub/query/detail";// 成果编辑查询

  @Autowired
  private CategoryMapBaseDao categoryMapBaseDao;
  @Autowired
  private CategoryHightechIndustryDAO categoryHightechIndustryDAO;
  @Autowired
  private ArchiveFileDao archiveFileDao;
  @Autowired
  private PersonDao personDao;
  @Autowired
  private PubRestemplateService pubRestemplateService;
  @Autowired
  private GroupPubService groupPubService;
  @Autowired
  private InstitutionDao institutionDao;
  @Autowired
  private PubSnsService pubSnsService;
  @Resource(name = "snsCacheService")
  private CacheService cacheService;
  @Value("${domainscm}")
  private String domainscm;
  @Resource(name = "restTemplate")
  private RestTemplate restTemplate;
  @Autowired
  private PsnSolrInfoModifyService psnSolrInfoModifyService;

  /**
   * 文件上下文路径 区分文件要放到哪个文件夹上
   */
  protected String basicPath = "upfile";

  @Autowired
  private FileDownloadUrlService fileDownloadUrlService;
  @Autowired
  private FileDownloadUrlService fileDownUrlService;
  @Autowired
  private PdwhPubDuplicateService pdwhPubDuplicateService;

  /**
   * 根据pubJson或成果id获取成果信息
   */
  @SuppressWarnings("rawtypes")
  @Override
  public PubDetailVO getBuilPubDetailVO(String pubJson, String des3PubId, String des3GrpId, Integer changType)
      throws ServiceException {
    PubDetailVO pubShow = null;
    Long pubId = NumberUtils.toLong(Des3Utils.decodeFromDes3(des3PubId));
    Long grpId = NumberUtils.toLong(Des3Utils.decodeFromDes3(des3GrpId));
    if (StringUtils.isNotBlank(pubJson)) {
      pubShow = this.getPubDetailVoJson(pubJson);// 切换成果类型
    } else if (getIsCurrentUserPub(pubId, grpId)) {// 是当前人的成果
      pubJson = pubRestemplateService.snsEditPubQuery(des3PubId);// 调用接口查询成果
      pubShow = this.getPubDetailVoJson(pubJson);
      pubShow.setOldPubType(pubShow.getPubType());
    } else {
      pubShow = this.initPubDetailVO();// 录入新成果
    }
    return pubShow;
  }

  @SuppressWarnings("rawtypes")
  @Override
  public PubDetailVO getPubDetailVoJson(String pubJson) {
    PubDetailVO pub = new PubDetailVO();
    Map<String, Object> map = null;
    try {
      map = JacksonUtils.json2Map(pubJson);
    } catch (IOException e) {
      logger.error("进入编辑页面转化json出错 ,pubJson={}", pubJson, e);
      throw new ServiceException();
    }

    Integer pubType = Integer.valueOf(Objects.toString(map.get("pubType")));
    switch (pubType) {
      case 1: // 奖励
        pub = (PubDetailVO) JacksonUtils.jsonObject(pubJson, new TypeReference<PubDetailVO<AwardsInfoDTO>>() {});
        break;
      case 2:// 书籍
        pub = (PubDetailVO) JacksonUtils.jsonObject(pubJson, new TypeReference<PubDetailVO<BookInfoDTO>>() {});
        break;
      case 3:// 会议论文
        pub = (PubDetailVO) JacksonUtils.jsonObject(pubJson, new TypeReference<PubDetailVO<ConferencePaperDTO>>() {});
        break;
      case 4:// 期刊论文
        pub = (PubDetailVO) JacksonUtils.jsonObject(pubJson, new TypeReference<PubDetailVO<JournalInfoDTO>>() {});
        break;
      case 5:// 专利
        pub = (PubDetailVO) JacksonUtils.jsonObject(pubJson, new TypeReference<PubDetailVO<PatentInfoDTO>>() {});
        break;
      case 7:// 其他
        pub = (PubDetailVO) JacksonUtils.jsonObject(pubJson, new TypeReference<PubDetailVO<OtherInfoDTO>>() {});
        break;
      case 8:// 学位论文
        pub = (PubDetailVO) JacksonUtils.jsonObject(pubJson, new TypeReference<PubDetailVO<ThesisInfoDTO>>() {});
        break;
      case 10:// 书籍章节
        pub = (PubDetailVO) JacksonUtils.jsonObject(pubJson, new TypeReference<PubDetailVO<BookInfoDTO>>() {});
        break;
      case 12:// 标准
        pub = (PubDetailVO) JacksonUtils.jsonObject(pubJson, new TypeReference<PubDetailVO<StandardInfoDTO>>() {});
        break;
      case 13:// 软件著作权
        pub = (PubDetailVO) JacksonUtils.jsonObject(pubJson, new TypeReference<PubDetailVO<SoftwareCopyrightDTO>>() {});
        break;
    }
    builParamsPubDetailVo(pub);// 构建一些参数
    return pub;
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  @Override
  public void builParamsPubDetailVo(PubDetailVO pub) throws ServiceException {
    builSitation(pub);// 构建成果来源
    String locale = LocaleContextHolder.getLocale().toString();

    // 设置type的名称
    if ("en_US".equals(locale)) {
      pub.setTypeName(V8pubConst.PUB_TYPE_MAP_EN.get(pub.getPubType()));
    } else {
      pub.setTypeName(V8pubConst.PUB_TYPE_MAP_ZH.get(pub.getPubType()));
    }
    // 构建关键词
    pub.setKeyWordsList(buildKeyWordsList(pub.getKeywords()));
    // 构建科技领域
    builScienceArea(pub);
    // 构建行业
    buildIndustry(pub);
    // 有附件的，重新构建全文的下载链接
    buildPubAccessory(pub);

    // 有全文的，重新构建全文的下载链接
    buildFulltext(pub);

    Long psnId = SecurityUtils.getCurrentUserId();
    pub.setDes3PsnId(Des3Utils.encodeToDes3(Objects.toString(psnId)));
    dealWithMembers(pub);
    // 国家默认设置为中国，当用户没有选择国家时 SCM-21656 成果编辑页面，国家和地区字段，不需要默认填写中国
    /*
     * if (NumberUtils.isNullOrZero(pub.getCountryId())) { pub.setCountryId(156L); }
     */
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  private void buildIndustry(PubDetailVO pub) {
    List<IndustryDTO> industrys = pub.getIndustrys();
    if (CollectionUtils.isEmpty(industrys)) {
      return;
    }
    for (IndustryDTO industry : industrys) {
      industry.setPubId(pub.getPubId());
      if (StringUtils.isNotBlank(industry.getIndustryCode())) {
        CategoryHightechIndustry categoryIndustry = categoryHightechIndustryDAO.get(industry.getIndustryCode());
        String locale = LocaleContextHolder.getLocale().toString();
        if ("en_US".equals(locale)) {
          industry.setIndustryName(categoryIndustry.getEname());
        } else {
          industry.setIndustryName(categoryIndustry.getName());
        }
      }
    }

  }

  private List<String> buildKeyWordsList(String keywords) {
    List<String> kws = new ArrayList<>();
    if (StringUtils.isEmpty(keywords)) {
      return kws;
    }
    keywords = HtmlUtils.htmlUnescape(keywords);
    List<String> kwList = ImportPubXmlUtils.parsePubKeywords2(keywords);
    for (String kw : kwList) {
      kw = HtmlUtils.htmlEscape(kw);
      kws.add(kw);
    }
    return kws;
  }

  private void buildFulltext(PubDetailVO<?> pub) {
    PubFulltextDTO pubFulltextDTO = pub.getFullText();
    if (pubFulltextDTO != null) {
      if (NumberUtils.isNotNullOrZero(pubFulltextDTO.getFileId())) {
        String downloadUrl = fileDownloadUrlService.getDownloadUrl(FileTypeEnum.SNS_FULLTEXT,
            pubFulltextDTO.getFileId(), pub.getPubId());
        pub.setFullTextDownloadUrl(downloadUrl);
        String simpleDownLoadUrl =
            fileDownloadUrlService.getShortDownloadUrl(FileTypeEnum.ARCHIVE_FILE, pubFulltextDTO.getFileId());
        pub.setSimpleDownLoadUrl(simpleDownLoadUrl);
      }
    }
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  private void dealWithMembers(PubDetailVO pub) {
    List<PubMemberDTO> members = pub.getMembers();
    boolean flag = false;
    for (PubMemberDTO pubMemberDTO : members) {
      flag = flag | pubMemberDTO.isOwner();
    }
    // members中不存有拥有者，不走下面的逻辑
    if (!flag) {
      for (PubMemberDTO member : members) {
        if (pub.getDes3PsnId().equals(member.getDes3PsnId())) {
          member.setOwner(true);
        }
      }
    }
  }

  /**
   * 构建附件
   * 
   * @param pubDetailVO
   * @param detailDOM
   */
  private void buildPubAccessory(PubDetailVO<?> pubDetailVO) {
    List<Accessory> accessorys = pubDetailVO.getAccessorys();
    if (CollectionUtils.isNotEmpty(accessorys)) {
      for (Accessory accessory : accessorys) {
        if (NumberUtils.isNotNullOrZero(accessory.getFileId())) {
          String accessoryFileUrl = fileDownUrlService.getDownloadAttachmentUrl(FileTypeEnum.FULLTEXT_ATTACHMENT,
              accessory.getFileId(), pubDetailVO.getPubId());
          accessory.setFileUrl(accessoryFileUrl);
          accessory.setIsUpdown(true);
        }
      }
    }
  }

  /**
   * 构建科技领域
   * 
   * @param pub
   */
  @SuppressWarnings({"rawtypes", "unchecked"})
  private void builScienceArea(PubDetailVO pub) {
    List<ScienceAreaDTO> scienceAreas = pub.getScienceAreas();
    for (ScienceAreaDTO area : scienceAreas) {
      if (area.getScienceAreaId() != null) {
        CategoryMapBase baseArea = categoryMapBaseDao.get(NumberUtils.toInt(Objects.toString(area.getScienceAreaId())));
        String locale = LocaleContextHolder.getLocale().toString();
        if ("en_US".equals(locale)) {
          area.setScienceAreaName(baseArea.getCategoryEn());
        } else {
          area.setScienceAreaName(baseArea.getCategoryZh());
        }
      }
    }
  }

  // 构建成果来源
  @SuppressWarnings({"rawtypes", "unchecked"})
  private void builSitation(PubDetailVO pub) {
    List<PubSituationDTO> sitationList = new ArrayList<PubSituationDTO>();
    String locale = LocaleContextHolder.getLocale().toString();

    PubSituationDTO sitation1 = new PubSituationDTO(PubLibraryEnum.SCIE.desc, "16", PubLibraryEnum.SCIE.desc);
    PubSituationDTO sitation2 = new PubSituationDTO(PubLibraryEnum.SSCI.desc, "17", PubLibraryEnum.SSCI.desc);
    PubSituationDTO sitation3 = new PubSituationDTO(PubLibraryEnum.ISTP.desc, "15", PubLibraryEnum.ISTP.desc);
    PubSituationDTO sitation4 = new PubSituationDTO(PubLibraryEnum.EI.desc, "14", PubLibraryEnum.EI.desc);
    PubSituationDTO sitation5 = new PubSituationDTO(PubLibraryEnum.CSSCI.desc, "34", PubLibraryEnum.CSSCI.desc);

    String PKU = PubLibraryEnum.PKU.desc;
    String showPKU = "zh_CN".equals(locale) ? "北大中文核心期刊" : PKU;
    PubSituationDTO sitation6 = new PubSituationDTO(PubLibraryEnum.PKU.desc);
    sitation6.setShowName(showPKU);

    String other = PubLibraryEnum.OTHER.desc;
    String ShowOther = "zh_CN".equals(locale) ? "其他" : other;
    PubSituationDTO sitation7 = new PubSituationDTO(PubLibraryEnum.OTHER.desc);
    sitation7.setShowName(ShowOther);

    sitationList.add(sitation1);
    sitationList.add(sitation2);
    sitationList.add(sitation3);
    sitationList.add(sitation4);
    sitationList.add(sitation5);
    sitationList.add(sitation6);
    sitationList.add(sitation7);
    // 构建收录来源
    List<PubSituationDTO> situations = pub.getSituations();
    if (situations != null && situations.size() > 0) {
      for (PubSituationDTO sit : sitationList) {
        for (PubSituationDTO situation : situations) {
          if (sit.getLibraryName().equals(situation.getLibraryName())) {
            sit.setSitStatus(situation.isSitStatus());
            sit.setSrcDbId(situation.getSrcDbId());
            sit.setSrcId(situation.getSrcId());
            sit.setSrcUrl(situation.getSrcUrl());
          }
        }
      }
    }
    pub.setSituations(sitationList);
  }

  /**
   * 新增成果进入页面初始化一些信息
   */
  @Override
  public PubDetailVO initPubDetailVO() throws ServiceException {
    Locale locale = LocaleContextHolder.getLocale();
    PubDetailVO pub = new PubDetailVO();
    pub.setPubType(4);// 默认打开期刊类型
    pub.setPermission(7);// 成果公开
    builSitation(pub);// 初始化收录情况
    // 设置type的名称
    if ("en_US".equals(locale.toString())) {
      pub.setTypeName(V8pubConst.PUB_TYPE_MAP_EN.get(pub.getPubType()));
    } else {
      pub.setTypeName(V8pubConst.PUB_TYPE_MAP_ZH.get(pub.getPubType()));
    }
    initPubMember(pub);// 添加当前人为作者
    pub.setCountryId(156L);// 国家设置为中国
    return pub;
  }

  /**
   * 添加成果时设置系统人为作者
   * 
   * @param pub
   */
  private void initPubMember(PubDetailVO pub) {
    Locale locale = LocaleContextHolder.getLocale();
    Long psnId = SecurityUtils.getCurrentUserId();
    Person person = personDao.get(psnId);
    pub.setDes3PsnId(Des3Utils.encodeToDes3(psnId.toString()));
    if (person != null) {
      List<PubMemberDTO> membersList = new ArrayList<>(); // 成果成员
      PubMemberDTO menber = new PubMemberDTO();
      menber.setDes3PsnId(Des3Utils.encodeToDes3(Objects.toString(psnId)));
      menber.setFirstAuthor(true);
      menber.setOwner(true);
      menber.setName(LocaleTextUtils.getLocaleText(locale, person.getName(), person.getEname()));
      menber.setEmail(person.getEmail());
      String insName = "";
      if (person.getInsId() != null) {
        Institution institution = institutionDao.getInsByIdNotStatus(person.getInsId());
        insName = Optional.ofNullable(institution)
            .map(ins -> LocaleTextUtils.getLocaleText(locale, ins.getZhName(), ins.getEnName())).orElse("");
      }
      if (StringUtils.isBlank(insName)) {
        insName = person.getInsName() == null ? "" : person.getInsName();
      }
      MemberInsDTO memberIns = new MemberInsDTO();
      memberIns.setInsId(person.getInsId());
      memberIns.setInsName(insName);
      List<MemberInsDTO> insList = new ArrayList<MemberInsDTO>();
      insList.add(memberIns);
      menber.setInsNames(insList);
      membersList.add(menber);
      pub.setMembers(membersList);
    }
  }

  /**
   * 调用接口更新或保存成果json
   */
  @Override
  public String saveOrUpdatePubJson(String pubJson) throws ServiceException {
    String result = "";
    try {
      if (isExistPub(pubJson)) {
        if (isCurrentUserPub(pubJson)) {// 是否是当前人的成果
          result = pubRestemplateService.saveOrUpdatePubJson(pubJson);
        }
      } else {
        Map<String, String> map = new HashMap<>();
        map.put("status", "isDel");
        result = JacksonUtils.jsonMapSerializer(map);
      }
    } catch (Exception e) {
      logger.error("保存成果json出错 pubJson={}", pubJson, e);
      throw e;
    }
    return result;
  }

  /**
   * 成果是否已删除
   * 
   * @param pubJson
   * @return
   */
  private boolean isExistPub(String pubJson) {
    if (StringUtils.isNotBlank(pubJson)) {
      Map<String, Object> resultMap = JacksonUtils.jsonToMap(pubJson);
      Long pubId = NumberUtils.toLong(Des3Utils.decodeFromDes3(Objects.toString(resultMap.get("des3PubId"))));
      Long grpId = NumberUtils.toLong(Des3Utils.decodeFromDes3(Objects.toString(resultMap.get("des3GrpId"))));
      if (NumberUtils.isNullOrZero(pubId)) {
        return true;
      }
      if (!NumberUtils.isNullOrZero(grpId)) { // 群组成果
        return isExistGrpPub(pubId, grpId);
      } else {
        return isExistPub(pubId);
      }
    } else {
      return false;
    }
  }

  private boolean isExistPub(Long pubId) {
    PubSnsPO pubSns = pubSnsService.get(pubId);
    if (pubSns != null && "DELETED".equals(pubSns.getStatus().toString())) {
      return false;
    } else {
      return true;
    }
  }

  private boolean isExistGrpPub(Long pubId, Long grpId) {
    Integer status = groupPubService.findGrpPubStatus(grpId, pubId);
    if (status != null && status == 0) {
      return true;
    } else {
      return false;
    }
  }

  /**
   * 是否是当前人的成果
   * 
   * @param pubJson
   * @return
   */
  private boolean isCurrentUserPub(String pubJson) {
    if (StringUtils.isNotBlank(pubJson)) {
      Map<String, Object> resultMap = JacksonUtils.jsonToMap(pubJson);
      Long pubId = NumberUtils.toLong(Des3Utils.decodeFromDes3(Objects.toString(resultMap.get("des3PubId"))));
      long ownerPsnId = NumberUtils.toLong(Des3Utils.decodeFromDes3(Objects.toString(resultMap.get("des3PsnId"))));
      if (ownerPsnId > 0 && ownerPsnId != SecurityUtils.getCurrentUserId().longValue()) { // 保存的人不是当前作者
        return false;
      }
      if (NumberUtils.isNullOrZero(pubId)) {
        return true;
      }
      Long grpId = NumberUtils.toLong(Des3Utils.decodeFromDes3(Objects.toString(resultMap.get("des3GrpId"))));
      return getIsCurrentUserPub(pubId, grpId);
    } else {
      return false;
    }
  }

  /**
   * 是否是当前人的成果
   * 
   * @param pubJson
   * @return
   */
  @Override
  public boolean getIsCurrentUserPub(Long pubId, Long grpId) {
    Long ownerPsnId = null;
    Long psnId = SecurityUtils.getCurrentUserId();
    if (NumberUtils.isNullOrZero(psnId)) {
      return false;
    }
    if (NumberUtils.isNullOrZero(pubId)) {
      // 说明是新增成果，不要判断是否可以编辑
      return false;
    }
    // 暂时注释 处理SCM-20598 20181015
    /*
     * if (!NumberUtils.isNullOrZero(grpId)) { // 群组成果编辑 ownerPsnId =
     * groupPubService.getPubOwnerPsnId(grpId, pubId);
     * 
     * } else { // 个人成果 ownerPsnId = psnPubService.getPubOwnerId(pubId); }
     */
    // ownerPsnId=v_pub_sns.create_psn_id
    PubSnsPO pubSns = pubSnsService.get(pubId);
    if (pubSns != null) {
      ownerPsnId = pubSns.getCreatePsnId();
    }

    if (!psnId.equals(ownerPsnId)) {
      return false;
    }

    return true;
  }

  @Override
  public PubDetailVO getBuilPubDetailVOByPdwh(String pubJson, String des3pdwhPubId, String des3GrpId) {

    PubDetailVO pubDetailVO = null;
    Map<String, String> param = new HashMap<String, String>();
    param.put("des3PubId", des3pdwhPubId);
    /* param.put("des3GrpId", grpId); */
    param.put("serviceType", V8pubQueryPubConst.PDWH_PUB);
    String requestJson = JacksonUtils.mapToJsonStr(param);
    // ----------------如果不为空则是切换成果
    if (StringUtils.isNotBlank(pubJson)) {
      pubDetailVO = this.getPubDetailVoJson(pubJson);// 切换成果类型
    } else {
      // 调用接口查询基准库成果
      pubDetailVO = getPubJson(pubJson, pubDetailVO, requestJson);
    }
    // 如果是在编辑页面 重新填满成果其他信息之后 不能把 Des3PubId置为空 否则无法保存
    if (StringUtils.isNotBlank(des3pdwhPubId)) {
      pubDetailVO.setPubId(null);
      pubDetailVO.setDes3PubId(null);
    }
    return pubDetailVO;
  }

  public PubDetailVO getPubJson(String pubjson, PubDetailVO pubDetailVO, String requestJson) {
    pubjson = RestTemplateUtils.post(restTemplate, domainscm + DETAIL_URL, requestJson);
    pubDetailVO = this.getPubDetailVoJson(pubjson);
    pubDetailVO.setOldPubType(pubDetailVO.getPubType());
    return pubDetailVO;
  }

  @Override
  public HashMap<String, Object> resolverPDF(Long fileId) throws ServiceException {
    HashMap<String, Object> resultMap = new HashMap<String, Object>();
    try {
      if (NumberUtils.isNotNullOrZero(fileId)) {
        // 1.解析pdf，获取成果DOI数据
        String DOI = readerPDF(fileId);
        // 2.通过DOI数据进行基准库成果查重，获取基准库的pubId
        if (StringUtils.isNotBlank(DOI)) {
          Long hashDoi = PubHashUtils.cleanDoiHash(DOI);
          Long hashCleanDoi = PubHashUtils.getDoiHashRemotePun(DOI);
          List<Long> pubIds = pdwhPubDuplicateService.dupByNotNullDoi(hashDoi, hashCleanDoi);
          if (CollectionUtils.isNotEmpty(pubIds)) {
            String des3pdwhPubId = Des3Utils.encodeToDes3(pubIds.get(0) + "");
            resultMap.put("result", "SUCCESS");
            resultMap.put("msg", des3pdwhPubId);
          } else {
            resultMap.put("result", "ERROR");
            resultMap.put("msg", "查重无结果");
          }
        } else {
          resultMap.put("result", "ERROR");
          resultMap.put("msg", "DOI为空");
        }
      }
    } catch (Exception e) {
      resultMap.put("result", "ERROR");
      logger.error("解析pdf文件，匹配基准库成果出错！fileId={}", fileId, e);
      throw new ServiceException(e);
    }
    return resultMap;
  }

  /**
   * 读取pdf文件
   * 
   * @param fileId
   * @return
   */
  private String readerPDF(Long fileId) throws ServiceException {
    try {
      String DOI = "";
      if (NumberUtils.isNotNullOrZero(fileId)) {
        ArchiveFile archiveFile = archiveFileDao.get(fileId);
        if (archiveFile != null) {
          String resUrl = "/" + basicPath + ArchiveFileUtil.getFilePath(archiveFile.getFilePath());
          URL fileUrl = new URL(domainscm.replace("https://", "http://") + resUrl);
          HttpURLConnection connection = (HttpURLConnection) fileUrl.openConnection();
          DataInputStream in = new DataInputStream(connection.getInputStream());
          BufferedInputStream buf = new BufferedInputStream(in);
          PDDocument doc = PDDocument.load(buf);
          // 读文本内容
          PDFTextStripper stripper = new PDFTextStripper();
          // 设置按顺序输出
          stripper.setSortByPosition(true);
          // 只读取前两页
          stripper.setStartPage(1);
          stripper.setEndPage(2);
          String content = stripper.getText(doc);
          // pdf内容的预处理
          content = PDFDataUtils.formatPDFData(content);
          // 匹配doi
          DOI = PDFDataUtils.matchDOI(content);
          // 关闭文件流
          in.close();
          buf.close();
        }
      }
      return DOI;
    } catch (Exception e) {
      logger.error("读取pdf文件出错！", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public void buildPubFulltext(PubDetailVO<?> pdwhPubShow, String des3FileId) {
    try {
      if (StringUtils.isNotBlank(des3FileId)) {
        Long fileId = NumberUtils.toLong(Des3Utils.decodeFromDes3(des3FileId));
        if (NumberUtils.isNotNullOrZero(fileId)) {
          ArchiveFile archiveFile = archiveFileDao.get(fileId);
          if (archiveFile != null) {
            PubFulltextDTO fullText = new PubFulltextDTO();
            fullText.setDes3fileId(des3FileId);
            fullText.setFileId(fileId);
            fullText.setFileName(archiveFile.getFileName());
            fullText.setPermission(0);
            pdwhPubShow.setFullText(fullText);
          }
        }
      }
    } catch (Exception e) {
      logger.error("构建成果全文信息出错！des3FileId={}", des3FileId, e);
      throw new ServiceException(e);
    }

  }

}
