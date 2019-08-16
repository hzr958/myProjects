package com.smate.web.v8pub.service.pub.fileimport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.HtmlUtils;

import com.smate.core.base.pub.enums.PubLibraryEnum;
import com.smate.core.base.pub.vo.PubDetailVO;
import com.smate.core.base.utils.cache.SnsCacheService;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.web.v8pub.consts.V8pubConst;
import com.smate.web.v8pub.dao.sns.pubtype.ConstPubTypeDao;
import com.smate.web.v8pub.dto.AwardsInfoDTO;
import com.smate.web.v8pub.dto.BookInfoDTO;
import com.smate.web.v8pub.dto.ConferencePaperDTO;
import com.smate.web.v8pub.dto.JournalInfoDTO;
import com.smate.web.v8pub.dto.MemberInsDTO;
import com.smate.web.v8pub.dto.PatentInfoDTO;
import com.smate.web.v8pub.dto.PubFulltextDTO;
import com.smate.web.v8pub.dto.PubMemberDTO;
import com.smate.web.v8pub.dto.PubSituationDTO;
import com.smate.web.v8pub.dto.PubTypeInfoDTO;
import com.smate.web.v8pub.dto.ThesisInfoDTO;
import com.smate.web.v8pub.exception.ServiceException;
import com.smate.web.v8pub.po.pubType.ConstPubType;
import com.smate.web.v8pub.service.fileimport.extract.PubFileInfo;
import com.smate.web.v8pub.service.pubdetailquery.PubDetailHandleService;
import com.smate.web.v8pub.service.searchimport.PubImportService;
import com.smate.web.v8pub.utils.RestTemplateUtils;
import com.smate.web.v8pub.vo.PendingImportPubVO;
import com.smate.web.v8pub.vo.importfile.ChangePub;
import com.smate.web.v8pub.vo.importfile.ImportSaveVo;
import com.smate.web.v8pub.vo.importfile.ImportfileVo;
import com.smate.web.v8pub.vo.searchimport.PubImportVO;

@Service
@Transactional(rollbackFor = Exception.class)
public class PubFileImportImpl implements PubFileImport {
  Logger logger = LoggerFactory.getLogger(getClass());
  private final String SEARCHIMPORTPUBCACHE = "searchImportPubCache";
  @Autowired
  private ConstPubTypeDao constPubTypeDao;
  @Autowired
  private PubImportService pubImportService;
  @Autowired
  private SnsCacheService cacheService;

  @Value("${pub.saveorupdate.url}")
  private String SAVEORUPDATE_URL;
  @Autowired
  private RestTemplate restTemplate;
  @Autowired
  private PubDetailHandleService pubDetailHandleService;

  @Override
  public List<ConstPubType> getConstPubTypeAll() throws ServiceException {
    return constPubTypeDao.getAllTypes(LocaleContextHolder.getLocale());
  }

  @Override
  public List<PendingImportPubVO> getInitImportPubVOList(List<PendingImportPubVO> pubList, ImportfileVo importfileVo)
      throws ServiceException {
    PubImportVO pubImportVo = new PubImportVO();
    pubImportVo.setPendingImportPubs(pubList);
    pubImportVo.setDbCode(importfileVo.getDbType());
    pubImportVo.setPubImportType("file");
    pubImportVo.setDbType("1");
    pubImportVo.setPsnId(SecurityUtils.getCurrentUserId());
    try {
      pubImportService.initImportPubInfo(pubImportVo);// 初始化构建一些字段
      importfileVo.setCacheKey(pubImportVo.getCacheKey());// 获取缓存key
    } catch (ServiceException e) {
      logger.error("校验和初始化文件导入成果出错，dbCode={}", pubImportVo.getDbCode(), e);
      throw new ServiceException(e);
    }
    return pubImportVo.getPendingImportPubs();
  }

  public void deleteImportfileVoInCache(String key) throws ServiceException {
    Long psnId = SecurityUtils.getCurrentUserId();
    if (psnId > 0L) {
      cacheService.remove(SEARCHIMPORTPUBCACHE, key);
    }
  }

  /**
   * 获取时间戳
   * 
   * @return
   */
  private String getTimeStamp() {
    long time = System.currentTimeMillis();
    String timeStamp = String.valueOf(time / 1000);
    return timeStamp;
  }

  public Map<Integer, PendingImportPubVO> getImportfileVoInCache(String key) throws ServiceException {
    Long psnId = SecurityUtils.getCurrentUserId();
    if (psnId > 0L) {
      return (Map<Integer, PendingImportPubVO>) cacheService.get(SEARCHIMPORTPUBCACHE, key);
    }
    return null;
  }

  @Override
  public Map<String, Integer> savePublist(ImportSaveVo importSaveVo) throws ServiceException {
    Map<Integer, PendingImportPubVO> cachePub = getImportfileVoInCache(importSaveVo.getCacheKey());
    Map<String, Integer> result = new HashMap<String, Integer>();
    if (cachePub != null) {
      List<PendingImportPubVO> pubList = new ArrayList<PendingImportPubVO>(cachePub.values());
      List<ChangePub> changPubList = importSaveVo.getChangPubList();
      result = postJson(pubList, changPubList);// 处理数据和保存
      deleteImportfileVoInCache(importSaveVo.getCacheKey());// 删除导入成果的缓存
    }
    return result;
  }

  private Map<String, Integer> postJson(List<PendingImportPubVO> pubList, List<ChangePub> changPubList) {
    Long psnId = SecurityUtils.getCurrentUserId();
    String des3PsnId = Des3Utils.encodeToDes3(psnId.toString());
    Integer suceessNum = 0;
    Integer errorNum = 0;
    for (PendingImportPubVO pendingPub : pubList) {
      PubTypeInfoDTO info = pendingPub.getPubTypeInfo();
      for (ChangePub changePub : changPubList) {
        if (changePub.getSeqNo().equals(pendingPub.getSeqNo())) {
          if (changePub.getRepeatSelect() != 0) {// 选中，不是跳过
            pendingPub.setDes3PsnId(des3PsnId);
            pendingPub.setSituations(getSitationList(changePub.getSitations()));
            buildPubMenberDTO(pendingPub);// 构建人员信息
            pendingPub.setPubGenre(1);
            if (!pendingPub.getPubType().equals(changePub.getPubType())) {// 切换了类型，不能要info字段了
              this.setPubTypeInfo(pendingPub, changePub.getPubType());
              pendingPub.setPubType(changePub.getPubType());
            }
            if (changePub.getRepeatSelect() == 1) {// 更新
              pendingPub.setPubHandlerName("updateSnsPubHandler");
              updateDupPub(pendingPub);// 作者、全文、附件不更新
            }
            if (changePub.getRepeatSelect() == 2) {// 新增
              pendingPub.setPubHandlerName("saveSnsPubHandler");
            }
            if (StringUtils.isNotBlank(changePub.getDes3PubFileId())) {
              pendingPub.setPubSourceFileId(
                  NumberUtils.parseLong(Des3Utils.decodeFromDes3(changePub.getDes3PubFileId()), 0L));
            }
            pendingPub.setPermission(changePub.getPubPermission());// 设置权限
            Long dupPubId = NumberUtils.toLong(Des3Utils.decodeFromDes3(changePub.getDupPubId()));
            if (NumberUtils.isNotNullOrZero(dupPubId)) {
              pendingPub.setDes3PubId(changePub.getDupPubId());
              pendingPub.setPubId(dupPubId);
            }

            String result = savePub(pendingPub);// 调用接口保存成果
            Map<String, String> resultMap = JacksonUtils.jsonToMap(result);
            if ("SUCCESS".equals(resultMap.get("status"))) {
              suceessNum++;
            } else {
              errorNum++;
            }

          }
        }
      }
    }
    Map<String, Integer> resultMap = new HashMap<String, Integer>();
    resultMap.put("totalResult", suceessNum);
    resultMap.put("errorNum", errorNum);
    return resultMap;
  }

  /**
   * 构建作者信息
   * 
   * @param pendingPub
   */
  private void buildPubMenberDTO(PendingImportPubVO pendingPub) {
    List<PubMemberDTO> menberList = new ArrayList<PubMemberDTO>();
    String nameStr = pendingPub.getAuthorNames();
    nameStr = HtmlUtils.htmlUnescape(nameStr);
    String unitInfo = HtmlUtils.htmlUnescape(pendingPub.getUnitInfo());// 作者单位信息
    String insNameStr = HtmlUtils.htmlUnescape(pendingPub.getOrganization());
    String abbrNameStr = HtmlUtils.htmlUnescape(pendingPub.getAuthorNamesAbbr());// 名字简称
    String email = HtmlUtils.htmlUnescape(pendingPub.getEmail());
    String rp = "";// 包含所有通信作者的字符串
    int index = 0;// 获取第几个邮箱
    if (StringUtils.isNotBlank(nameStr)) {
      String[] names = nameStr.split(";");
      String[] insNames = null;
      String[] abbrNames = {};// 简称拆分数组
      String[] emails = {};// 邮箱拆分数组
      String[] units = {};// 作者单位拆分数据
      if (StringUtils.isNotBlank(nameStr) && StringUtils.isNotBlank(insNameStr)) {
        insNames = insNameStr.split(";");
        int endIndex = insNameStr.lastIndexOf("(reprint author)");
        if (endIndex > 0) {
          rp = insNameStr.substring(0, endIndex);
        }
      }
      if (StringUtils.isNotEmpty(abbrNameStr) && StringUtils.isNotEmpty(email)) {
        abbrNames = abbrNameStr.split(";");
        emails = email.split(";");
      }
      if (StringUtils.isNotEmpty(unitInfo)) {
        units = unitInfo.split("\\. ;");// 由于split传入的是一个正则表达式,所以需要对.进行转义,以. ;分割,防止作者名称中出现.;结尾的,导致c1拆分不正常 SCM-24071
      }
      for (int i = 0; i < names.length; i++) {
        String name = names[i];
        PubMemberDTO menber = new PubMemberDTO();
        menber.setName(name);
        menber.setSeqNo(i + 1);
        menber.setAbbrName(abbrNames != null && abbrNames.length > i ? abbrNames[i] : "");
        menberList.add(menber);
      }
      buildUnitInfo(menberList, units);
      buildCommAuthor(menberList, index, rp, emails);
    }
    pendingPub.setMembers(menberList);
  }

  /**
   * 构建作者单位,由于文件存在名字顺序问题,所以使用双重循环
   */
  public void buildUnitInfo(List<PubMemberDTO> memberList, String[] units) {
    if (memberList.size() > 0 && units.length > 0) {
      for (PubMemberDTO pubMemberDTO : memberList) {
        List<MemberInsDTO> insNameList = new ArrayList<MemberInsDTO>();
        MemberInsDTO ins = new MemberInsDTO();
        for (int i = 0; i < units.length; i++) {
          String currUnit = units.length > i ? units[i] : "";
          int lastIndex = currUnit != null ? currUnit.lastIndexOf("]") : -1;
          if (lastIndex > 0 && StringUtils.isNotEmpty(pubMemberDTO.getName())
              && currUnit.indexOf(pubMemberDTO.getName().trim()) > 0) {
            currUnit = currUnit.substring(lastIndex + 1, currUnit.length());
            ins.setInsName(currUnit != null ? currUnit.replaceAll("\\.", "").trim() : "");
            insNameList.add(ins);
          }
        }
        pubMemberDTO.setInsNames(insNameList);
      }
    }
  }

  /**
   * 构建通信作者
   * 
   * @param menberList
   * @param index
   * @param rp
   * @param email
   */
  public void buildCommAuthor(List<PubMemberDTO> memberList, int index, String rp, String[] email) {
    if (memberList.size() > 0 && email.length > 0) {
      for (PubMemberDTO param : memberList) {
        if (rp.indexOf(param.getAbbrName().trim()) != -1) {
          param.setEmail(email[index++]);
          param.setCommunicable(true);
        }
      }
    }
  }

  // 更新重复的成果json数据。作者、全文、附件不更新
  @SuppressWarnings("rawtypes")
  protected void updateDupPub(PendingImportPubVO pub) {
    Map<String, Object> detailParams = new HashMap<>();
    PubDetailVO pubDetailVO = new PubDetailVO();
    detailParams.put(V8pubConst.DESC_PUB_ID, pub.getDes3PubId());
    detailParams.put("serviceType", "snsPub");
    pubDetailVO = pubDetailHandleService.queryPubDetail(detailParams);
    if (pubDetailVO != null) {
      if (pubDetailVO.getFullText() != null) {
        PubFulltextDTO fulltext = pubDetailVO.getFullText();
        fulltext.setDes3fileId(fulltext.getDes3fileId());
        PubFulltextDTO fulltextDTO = new PubFulltextDTO();
        if (fulltext != null) {
          fulltextDTO.setDes3fileId(fulltext.getDes3fileId());
          fulltextDTO.setFileName(fulltext.getFileName());
          fulltextDTO.setPermission(fulltext.getPermission());
        }
        pub.setFullText(fulltextDTO);
      }
      if (pubDetailVO.getAccessorys() != null) {
        pub.setAccessorys(pubDetailVO.getAccessorys());
      }
      pub.setAuthorNames(pubDetailVO.getAuthorName());
      pub.setPubId(pubDetailVO.getPubId());
      pub.setMembers(pubDetailVO.getMembers());
    }
  }

  /**
   * 
   * @param sitationList
   * @return
   */
  private void setPubTypeInfo(PendingImportPubVO<PubTypeInfoDTO> pub, Integer pubType) {
    // switch (pubType) {
    // case 1: // 奖励
    // pub.setPubTypeInfo(new AwardsInfoDTO());
    // break;
    // case 2:// 书籍
    // pub.setPubTypeInfo(new BookInfoDTO());
    // break;
    // case 3:// 会议论文
    // pub.setPubTypeInfo(new ConferencePaperDTO());
    // break;
    // case 4:// 期刊论文
    // pub.setPubTypeInfo(new JournalInfoDTO());
    // break;
    // case 5:// 专利
    // pub.setPubTypeInfo(new PatentInfoDTO());
    // break;
    // case 7:// 其他
    // pub.setPubTypeInfo(new OtherInfoDTO());
    // break;
    // case 8:// 学位论文
    // pub.setPubTypeInfo(new ThesisInfoDTO());
    // break;
    // case 10:// 书籍章节
    // pub.setPubTypeInfo(new BookInfoDTO());
    // break;
    // }
    PubFileInfo pubInfo = pub.getPubFileInfo();
    switch (pubType) {
      case 1:
        AwardsInfoDTO awardsInfo = new AwardsInfoDTO();
        awardsInfo.setCategory(pubInfo.getAwardCategory());// 奖项种类
        awardsInfo.setIssuingAuthority(pubInfo.getIssueInsName());// 授奖机构
        awardsInfo.setIssueInsId(Long.getLong(pubInfo.getIssue()));// 授奖机构id
        awardsInfo.setCertificateNo(pubInfo.getCertificateNo());// 证书编号
        awardsInfo.setAwardDate(changeStringToDate(pubInfo.getAwardDate()));// 授奖日期
        awardsInfo.setGrade(pubInfo.getAwardGrade());// 奖项等级
        pub.setPubTypeInfo(awardsInfo);
        break;
      case 2:
      case 10:
        // 书籍 取booktitle 书著 取 title
        BookInfoDTO bookInfo = new BookInfoDTO();
        pub.setTitle(pubInfo.getTitle());// 书籍名
        bookInfo.setName(pubInfo.getBookTitle());// 书名
        bookInfo.setSeriesName(pubInfo.getSeriesName());// 丛书名称
        bookInfo.setEditors(pubInfo.getEditors());// 编辑
        bookInfo.setISBN(pubInfo.getISBN());// 国际标准图书编号
        bookInfo.setPublisher(pubInfo.getPublisher());// 出版社
        bookInfo.setTotalPages(pubInfo.getTotalPages());// 总页数
        bookInfo.setPageNumber(pubInfo.getPageNumber()); // 起止页码或者文章号
        bookInfo.setType(pubInfo.getCategoryValue());// 书籍/著作类型
        bookInfo.setTotalWords(pubInfo.getTotalWords());// 总字数
        bookInfo.setChapterNo(pubInfo.getChapterNo());// 章节号;
        bookInfo.setLanguage(pubInfo.getLanguage());
        pub.setPubTypeInfo(bookInfo);
        break;
      case 3:
        ConferencePaperDTO conferencePaper = new ConferencePaperDTO();
        // 论文类别// 国际学术会议/国内学术会议
        conferencePaper.setPaperType(pubInfo.getPaperTypeValue());
        conferencePaper.setName(pubInfo.getConfName());// 会议名称
        conferencePaper.setPapers(pubInfo.getPapersName());// 会议集名confName
        conferencePaper.setOrganizer(pubInfo.getOrganizer());// 会议组织者
        conferencePaper.setStartDate(changeStringToDate(pubInfo.getStartDate()));// 开始日期
        conferencePaper.setEndDate(changeStringToDate(pubInfo.getEndDate()));// 结束日期
        conferencePaper.setPageNumber(pubInfo.getPageNumber());// 起止页码或者文章号
        pub.setPubTypeInfo(conferencePaper);
        break;
      case 4:
        JournalInfoDTO journalInfo = new JournalInfoDTO();
        journalInfo.setJid(null);// 期刊id
        journalInfo.setName(pubInfo.getOriginal());// 期刊名称
        journalInfo.setVolumeNo(pubInfo.getVolumeNo());// 卷号
        journalInfo.setIssue(pubInfo.getIssue());// 期号
        journalInfo.setISSN(pubInfo.getIssn());
        journalInfo.setPageNumber(pubInfo.getPageNumber());// 起止页码或者文章号
        journalInfo.setPublishStatus(pubInfo.getPublicationStatus());// 发表状态(P已发表/A已接收)
        pub.setPubTypeInfo(journalInfo);
        break;
      case 5:
        PatentInfoDTO patentInfo = new PatentInfoDTO();
        patentInfo.setType(pubInfo.getPatenType());// 专利类别，发明专利51/实用新型52/外观设计53
        patentInfo.setArea(pubInfo.getPatentArea());// 专利国家
                                                    // 中国专利/美国专利/欧洲专利/WIPO专利/日本专利/其他
        if ("1".equals(pubInfo.getPatentStatus()) || "授权".equals(pubInfo.getPatentStatus())) {
          patentInfo.setStatus(1);// 专利状态，申请/授权
        } else {
          patentInfo.setStatus(0);// 专利状态，申请/授权
        }
        patentInfo.setApplier(pubInfo.getApplier());// 申请人 (专利为申请状态)
        patentInfo.setApplicationDate(changeStringToDate(pubInfo.getApplicationDate()));// 申请（公开）日期
        patentInfo.setIPC(pubInfo.getIpc());
        patentInfo.setCPC(pubInfo.getCpc());
        patentInfo.setPatentee(pubInfo.getApplier());// 专利权人 (专利为授权状态)
        patentInfo.setStartDate(changeStringToDate(pubInfo.getStartDate()));// 专利生效起始日期
                                                                            // (专利为授权状态)
        patentInfo.setEndDate(changeStringToDate(pubInfo.getEndDate()));// 专利失效结束日期
                                                                        // (专利为授权状态)
        patentInfo.setApplicationNo(pubInfo.getApplicationNo());// 申请号
        patentInfo.setPublicationOpenNo(pubInfo.getPatentOpenNo());// 专利公开（公告）号
        patentInfo.setTransitionStatus(pubInfo.getPatentChangeStatus());// 专利转换状态
        patentInfo.setPrice(pubInfo.getPatentPrice());// 交易金额
        patentInfo.setIssuingAuthority(pubInfo.getIssuingAuthority());// 专利授权组织，颁发机构
        pub.setPubTypeInfo(patentInfo);
        break;
      case 8:
        ThesisInfoDTO thesisInfo = new ThesisInfoDTO();
        thesisInfo.setDegree(pubInfo.getDegree());// 学位
        thesisInfo.setDefenseDate(changeStringToDate(pubInfo.getDefenseDate()));// 答辩日期
        thesisInfo.setIssuingAuthority(pubInfo.getIssuingAuthority());// 颁发单位
        thesisInfo.setDepartment(pubInfo.getDepartment());// 部门
        thesisInfo.setISBN(pubInfo.getISBN());// 国际标准图书编号
        pub.setPubTypeInfo(thesisInfo);
        break;
      default:
        break;
    }
  }

  private String changeStringToDate(String dateStr) {
    if (StringUtils.isEmpty(dateStr)) {
      return "";
    }
    dateStr = dateStr.replace("/", "-");
    dateStr = dateStr.replace(".", "-");
    return dateStr;
  }

  /*
   * 获取收录情况
   */
  private List<PubSituationDTO> getSitationList(List<String> sitationList) {
    return Optional.ofNullable(sitationList).orElse(new ArrayList<String>()).stream().map(s -> {
      PubSituationDTO sit = new PubSituationDTO();
      sit.setLibraryName(PubLibraryEnum.parse(s).desc);
      sit.setSitStatus(true);
      return sit;
    }).collect(Collectors.toList());
  }

  private String savePub(PendingImportPubVO pub) {
    String pubJson = JacksonUtils.jsonObjectSerializer(pub);
    String result = "";
    try {
      result = RestTemplateUtils.post(restTemplate, SAVEORUPDATE_URL, pubJson);
    } catch (Exception e) {
      logger.error("保存成果json出错 pubJson={}", pubJson, e);
      throw e;
    }
    return result;
  }
}
